import config.ImageCrawlerConfig;
import webcrawler.ImageCrawler;
import webcrawler.ImageDownloader;
import webcrawler.WebsiteAnalyzer;

import java.net.URI;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        ImageCrawlerConfig config = new ImageCrawlerConfig(
                2,
                3,
                Path.of("downloads/images")
        );
        ImageDownloader imageDownloader = new ImageDownloader();
        WebsiteAnalyzer websiteAnalyzer = new WebsiteAnalyzer();

        ImageCrawler imageCrawler = new ImageCrawler(config, imageDownloader, websiteAnalyzer);

        imageCrawler.crawl(URI.create("https://en.wikipedia.org/wiki/Main_Page"));
    }
}
