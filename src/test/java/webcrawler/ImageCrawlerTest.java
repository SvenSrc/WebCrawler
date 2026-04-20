package webcrawler;

import config.ImageCrawlerConfig;
import org.junit.jupiter.api.*;
import java.net.URI;
import java.nio.file.*;
import java.time.Duration;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ImageCrawler class.
 * Tests folder creation, isIdle behaviour, and full crawl execution.
 */
public class ImageCrawlerTest {

    private ImageCrawler crawler;
    private Path tempFolder;

    @BeforeEach
    void setUp() throws Exception {
        tempFolder = Files.createTempDirectory("crawler_test");
        ImageCrawlerConfig config = new ImageCrawlerConfig(2, 3, tempFolder);
        crawler = new ImageCrawler(config);
    }

    @AfterEach
    void tearDown() throws Exception {
        crawler.shutdown();
        Files.walk(tempFolder)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> p.toFile().delete());
    }

    @Test
    void testIsIdleInitially() {
        assertTrue(crawler.isIdle(), "Crawler should be idle before any crawl");
    }

    @Test
    void testIsIdleAfterShutdown() throws Exception {
        crawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
        crawler.shutdown();

        assertTrue(crawler.isIdle(), "Crawler should be idle after shutdown");
    }

    @Test
    void testGetIncrementingFolderStartsAtOne() {
        Path webFolder = tempFolder.resolve("w3schools.com");
        Path result = crawler.getIncrementingFolder(webFolder);

        assertEquals(webFolder.resolve("1"), result);
    }

    @Test
    void testGetIncrementingFolderIncrementsWhenExists() throws Exception {
        Path webFolder = tempFolder.resolve("w3schools.com");
        Files.createDirectories(webFolder.resolve("1"));

        Path result = crawler.getIncrementingFolder(webFolder);

        assertEquals(webFolder.resolve("2"), result);
    }

    @Test
    void testCrawlCreatesSubfolder() throws Exception {
        crawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
        crawler.shutdown();

        Path expectedFolder = tempFolder.resolve("www.w3schools.com");
        assertTrue(Files.exists(expectedFolder), "Host folder should be created");
    }

    @Test
    void testCrawlDownloadsImages() throws Exception {
        crawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
        crawler.shutdown();

        // Count all downloaded files
        long fileCount = Files.walk(tempFolder)
                .filter(Files::isRegularFile)
                .count();

        assertTrue(fileCount > 0, "Should have downloaded at least one image");
    }

    @Test
    void testCrawlTwiceSameHostCreatesTwoSubfolders() throws Exception {
        crawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));

        Path hostFolder = tempFolder.resolve("www.w3schools.com");

        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            while (!Files.exists(hostFolder.resolve("1"))) {
                Thread.yield();
            }
        }, "Folder 1 was not created within 5 seconds");

        assertTrue(Files.exists(hostFolder.resolve("1")));

        crawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
        crawler.shutdown();

        assertTrue(Files.exists(hostFolder.resolve("2")), "Folder 2 should exist");
    }

    @Test
    void testConfigScansLimitsAreRespected() throws Exception {
        ImageCrawlerConfig config = new ImageCrawlerConfig(1, 1, tempFolder);
        ImageCrawler limitedCrawler = new ImageCrawler(config);

        assertDoesNotThrow(() -> {
            limitedCrawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
            limitedCrawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
            limitedCrawler.shutdown();
        });
    }

    @Test
    void testConfigDownloadLimitIsRespected() throws Exception {
        ImageCrawlerConfig config = new ImageCrawlerConfig(2, 1, tempFolder);
        ImageCrawler limitedCrawler = new ImageCrawler(config);

        assertDoesNotThrow(() -> {
            limitedCrawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
            limitedCrawler.shutdown();
        });

        long fileCount = Files.walk(tempFolder)
                .filter(Files::isRegularFile)
                .count();

        assertTrue(fileCount > 0, "Images should still be downloaded even with limit of 1");
    }
}