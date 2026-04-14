package config;

import interfaces.IImageCrawlerConfig;

import java.nio.file.Path;

public class ImageCrawlerConfig implements IImageCrawlerConfig {

    private final int numberOfAllowedParallelWebsiteScans;
    private final int numberOfAllowedParallelImageDownloads;
    private final Path downloadPath;

    public ImageCrawlerConfig(int numberOfAllowedParallelWebsiteScans,
                              int numberOfAllowedParallelImageDownloads,
                              Path downloadPath){
        this.numberOfAllowedParallelWebsiteScans = numberOfAllowedParallelWebsiteScans;
        this.numberOfAllowedParallelImageDownloads = numberOfAllowedParallelImageDownloads;
        this.downloadPath = downloadPath;
    }

    @Override
    public int getNumberOfAllowedParallelWebsiteScans() {
        return numberOfAllowedParallelWebsiteScans;
    }

    @Override
    public int getNumberOfAllowedParallelImageDownloads() {
        return numberOfAllowedParallelImageDownloads;
    }

    @Override
    public Path getDownloadPath() {
        return downloadPath;
    }
}
