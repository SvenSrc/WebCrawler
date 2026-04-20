package webcrawler;

import org.junit.jupiter.api.*;
import java.net.URI;
import java.nio.file.*;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ImageDownloader class.
 * Tests the resolveConflict logic and the downloadImage
 * functionality using a temporary folder and real image URLs.
 */
public class ImageDownloaderTest {

    private ImageDownloader downloader;
    private Path tempFolder;

    @BeforeEach
    void setUp() throws Exception {
        downloader = new ImageDownloader();
        tempFolder = Files.createTempDirectory("imagedownloader_test");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempFolder)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> p.toFile().delete());
    }

    // --- resolveConflict tests ---

    @Test
    void testResolveConflictNoConflict() {
        Path result = downloader.resolveConflict(tempFolder, "photo.jpg");
        assertEquals(tempFolder.resolve("photo.jpg"), result);
    }

    @Test
    void testResolveConflictSingleConflict() throws Exception {
        Files.createFile(tempFolder.resolve("photo.jpg"));

        Path result = downloader.resolveConflict(tempFolder, "photo.jpg");
        assertEquals(tempFolder.resolve("photo_1.jpg"), result);
    }

    @Test
    void testResolveConflictMultipleConflicts() throws Exception {
        Files.createFile(tempFolder.resolve("photo.jpg"));
        Files.createFile(tempFolder.resolve("photo_1.jpg"));
        Files.createFile(tempFolder.resolve("photo_2.jpg"));

        Path result = downloader.resolveConflict(tempFolder, "photo.jpg");
        assertEquals(tempFolder.resolve("photo_3.jpg"), result);
    }

    @Test
    void testResolveConflictNoExtension() throws Exception {
        Files.createFile(tempFolder.resolve("photo"));

        Path result = downloader.resolveConflict(tempFolder, "photo");
        assertEquals(tempFolder.resolve("photo_1"), result);
    }

    @Test
    void testResolveConflictDotsInFilename() throws Exception {
        Files.createFile(tempFolder.resolve("my.photo.gallery.jpg"));

        Path result = downloader.resolveConflict(tempFolder, "my.photo.gallery.jpg");
        // Should only treat the last dot as extension separator
        assertEquals(tempFolder.resolve("my.photo.gallery_1.jpg"), result);
    }

    // --- downloadImage tests ---

    @Test
    void testDownloadImageSavesFile() {
        URI imageUri = URI.create("http://www.w3schools.com/html/img_girl.jpg");

        downloader.downloadImage(imageUri, tempFolder);

        assertTrue(Files.exists(tempFolder.resolve("img_girl.jpg")));
    }

    @Test
    void testDownloadImageSameFileTwiceResolvesConflict() {
        URI imageUri = URI.create("http://www.w3schools.com/html/img_girl.jpg");

        downloader.downloadImage(imageUri, tempFolder);
        downloader.downloadImage(imageUri, tempFolder);

        assertTrue(Files.exists(tempFolder.resolve("img_girl.jpg")));
        assertTrue(Files.exists(tempFolder.resolve("img_girl_1.jpg")));
    }

    @Test
    void testDownloadImageFileIsNotEmpty() throws Exception {
        URI imageUri = URI.create("http://www.w3schools.com/html/img_girl.jpg");

        downloader.downloadImage(imageUri, tempFolder);

        Path saved = tempFolder.resolve("img_girl.jpg");
        assertTrue(Files.size(saved) > 0);
    }

    @Test
    void testDownloadImageInvalidURIDoesNotThrow() {
        URI invalidUri = URI.create("http://invalid.url.that.does.not.exist/image.jpg");

        // Should not throw — error is caught internally and logged
        assertDoesNotThrow(() -> downloader.downloadImage(invalidUri, tempFolder));
    }
}