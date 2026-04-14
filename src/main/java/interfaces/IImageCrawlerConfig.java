package interfaces;

public interface IImageCrawlerConfig {

    int getNumberOfAllowedParallelWebsiteScanes();
    int getNumberOfAllowedParallelImageDownloads();
    java.nio.file.Path getDownloadPath();

}
