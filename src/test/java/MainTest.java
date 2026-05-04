import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import webcrawler.ImageCrawlerTest;
import webcrawler.ImageDownloaderTest;
import webcrawler.WebsiteAnalyzerTest;

@Suite
@SelectClasses({
        ImageCrawlerTest.class,
        ImageDownloaderTest.class,
        WebsiteAnalyzerTest.class,
        ImageCrawlerTest.class
})
public class MainTest {
    // Runs all Tests when running this MainTest
}