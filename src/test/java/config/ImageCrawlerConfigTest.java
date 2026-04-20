package config;

import org.junit.jupiter.api.Test;
import webcrawler.ImageCrawler;

import java.net.URI;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ImageCrawlerConfig class.
 * Verifies that configuration values are stored and returned correctly.
 */
public class ImageCrawlerConfigTest {

    @Test
    void testConfigStoresCorrectValues() {
        Path path = Path.of("downloads");
        ImageCrawlerConfig config = new ImageCrawlerConfig(2, 3, path);

        assertEquals(2, config.getNumberOfAllowedParallelWebsiteScans());
        assertEquals(3, config.getNumberOfAllowedParallelImageDownloads());
        assertEquals(path, config.getDownloadPath());
    }

    @Test
    void testConfigWithSingleThreads() {
        ImageCrawlerConfig config = new ImageCrawlerConfig(1, 1, Path.of("downloads"));

        assertEquals(1, config.getNumberOfAllowedParallelWebsiteScans());
        assertEquals(1, config.getNumberOfAllowedParallelImageDownloads());
    }

    @Test
    void testConfigWithHighThreadCounts() {
        ImageCrawlerConfig config = new ImageCrawlerConfig(100, 200, Path.of("downloads"));

        assertEquals(100, config.getNumberOfAllowedParallelWebsiteScans());
        assertEquals(200, config.getNumberOfAllowedParallelImageDownloads());
    }

    @Test
    void testDownloadPathIsCorrect() {
        Path expected = Path.of("downloads", "images");
        ImageCrawlerConfig config = new ImageCrawlerConfig(2, 3, expected);

        assertEquals(expected, config.getDownloadPath());
    }
}