package webcrawler;

import interfaces.IImageCrawler;
import interfaces.IImageCrawlerConfig;

import java.net.URI;
import java.nio.file.Path;

public class ImageCrawler implements IImageCrawlerConfig, IImageCrawler {
    @Override
    public void crawl(URI uri) {

    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public int getNumberOfAllowedParallelWebsiteScanes() {
        return 0;
    }

    @Override
    public int getNumberOfAllowedParallelImageDownloads() {
        return 0;
    }

    @Override
    public Path getDownloadPath() {
        return null;
    }
}
