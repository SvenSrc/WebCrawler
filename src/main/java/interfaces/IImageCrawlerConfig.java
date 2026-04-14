package interfaces;

public interface IImageCrawlerConfig {

    int getNumberOfAllowedParallelWebsiteScans();
    int getNumberOfAllowedParallelImageDownloads();
    java.nio.file.Path getDownloadPath();

}
