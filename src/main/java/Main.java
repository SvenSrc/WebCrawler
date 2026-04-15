import config.ImageCrawlerConfig;
import webcrawler.ImageCrawler;

import java.net.URI;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        ImageCrawlerConfig config = new ImageCrawlerConfig(
                2,
                3,
                Path.of("downloads/images")
        );
        ImageCrawler imageCrawler = new ImageCrawler(config);

        imageCrawler.crawl(URI.create("http://en.wikipedia.org/wiki/Main_Page"));
        imageCrawler.crawl(URI.create("http://www.w3schools.com/html/html_images.asp"));
    }
}
