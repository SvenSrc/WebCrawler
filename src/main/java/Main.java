import config.ImageCrawlerConfig;
import webcrawler.ImageCrawler;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point of the ImageCrawler application.
 * Configures and starts the crawler with a set of predefined URLs.
 */
public class Main {

    /**
     * Initializes the configuration, submits all URLs to the crawler
     * and shuts it down after all scans and downloads are complete.
     *
     * @param args  Command line arguments (not used)
     * @throws Exception If the crawler fails to shut down properly
     */
    public static void main(String[] args) throws Exception {

        ImageCrawlerConfig config = new ImageCrawlerConfig(
                2,
                3,
                Path.of("downloads/images")
        );
        ImageCrawler imageCrawler = new ImageCrawler(config);

        ArrayList<String> uris = new ArrayList<>(List.of(
                "http://en.wikipedia.org/wiki/Main_Page",
                "http://www.w3schools.com/html/html_images.asp",
                "https://github.com/"
        ));

        for(String uri : uris){
            imageCrawler.crawl(URI.create(uri));
        }

        imageCrawler.shutdown();

    }
}
