package webcrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WebsiteAnalyzer class.
 * Uses the w3schools HTML images page as a real test target.
 */
public class WebsiteAnalyzerTest {

    private WebsiteAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new WebsiteAnalyzer();
    }

    @Test
    void testAnalyzeWebsiteReturnsImages() {
        URI uri = URI.create("http://www.w3schools.com/html/html_images.asp");

        List<URI> result = analyzer.analyzeWebsite(uri);

        assertFalse(result.isEmpty(), "Should find at least one image on w3schools");
    }

    @Test
    void testAnalyzeWebsiteAllUrisAreHttp() {
        URI uri = URI.create("http://www.w3schools.com/html/html_images.asp");

        List<URI> result = analyzer.analyzeWebsite(uri);

        // All URIs should be http, not https
        for (URI imageUri : result) {
            assertTrue(imageUri.toString().startsWith("http://"),
                    "URI should start with http:// but was: " + imageUri);
        }
    }

    @Test
    void testAnalyzeWebsiteInvalidUrlReturnsEmptyList() {
        URI uri = URI.create("http://invalid.url.that.does.not.exist.com");

        List<URI> result = analyzer.analyzeWebsite(uri);

        assertTrue(result.isEmpty(), "Invalid URL should return empty list");
    }

    @Test
    void testAnalyzeWebsiteReturnsNonEmptyUris() {
        URI uri = URI.create("http://www.w3schools.com/html/html_images.asp");

        List<URI> result = analyzer.analyzeWebsite(uri);

        // All returned URIs should have non-empty paths
        for (URI imageUri : result) {
            assertNotNull(imageUri);
            assertFalse(imageUri.toString().isEmpty());
        }
    }
}