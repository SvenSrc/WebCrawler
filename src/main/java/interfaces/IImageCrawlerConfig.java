package interfaces;

/**
 * Defines the configuration contract for the ImageCrawler.
 * Implementation provides the parameters for controlling parallel
 * execution and the download location.
 */
public interface IImageCrawlerConfig {

    /**
     * Represents returning the maximum number of websites that can be scanned in parallel.
     *
     * @return  The maximum number of parallel website scans
     */
    int getNumberOfAllowedParallelWebsiteScans();

    /**
     * Represents returning the maximum number of images that can be downloaded in parallel.
     *
     * @return  The maximum number of parallel image downloads
     */
    int getNumberOfAllowedParallelImageDownloads();

    /**
     * Represents returning the path to the folder where downloaded images will be saved.
     *
     * @return  The download directory path
     */
    java.nio.file.Path getDownloadPath();

}
