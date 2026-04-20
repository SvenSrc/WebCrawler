package config;

import interfaces.IImageCrawlerConfig;

import java.nio.file.Path;

/**
 * Implementation of IImageCrawlerConfig.
 * Holds the configuration parameters for the ImageCrawler.
 */
public class ImageCrawlerConfig implements IImageCrawlerConfig {

    private final int numberOfAllowedParallelWebsiteScans;
    private final int numberOfAllowedParallelImageDownloads;
    private final Path downloadPath;

    /**
     * Creates a new ImageCrawlerConfig with the given parameters.
     *
     * @param numberOfAllowedParallelWebsiteScans   Maximum number of websites scanned in parallel
     * @param numberOfAllowedParallelImageDownloads Maximum number of images downloaded in parallel
     * @param downloadPath                          Path to the folder where images will be saved
     */
    public ImageCrawlerConfig(int numberOfAllowedParallelWebsiteScans,
                              int numberOfAllowedParallelImageDownloads,
                              Path downloadPath) {
        this.numberOfAllowedParallelWebsiteScans = numberOfAllowedParallelWebsiteScans;
        this.numberOfAllowedParallelImageDownloads = numberOfAllowedParallelImageDownloads;
        this.downloadPath = downloadPath;
    }

    /**
     * Returns the maximum number of websites that can be scanned in parallel.
     *
     * @return  The maximum number of parallel website scans
     */
    @Override
    public int getNumberOfAllowedParallelWebsiteScans() {
        return numberOfAllowedParallelWebsiteScans;
    }

    /**
     * Returns the maximum number of images that can be downloaded in parallel.
     *
     * @return  The maximum number of parallel image downloads
     */
    @Override
    public int getNumberOfAllowedParallelImageDownloads() {
        return numberOfAllowedParallelImageDownloads;
    }

    /**
     * Returns the path to the folder where downloaded images will be saved.
     *
     * @return  The download directory path
     */
    @Override
    public Path getDownloadPath() {
        return downloadPath;
    }
}
