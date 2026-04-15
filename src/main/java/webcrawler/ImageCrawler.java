package webcrawler;

import config.ImageCrawlerConfig;
import interfaces.IImageCrawler;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageCrawler implements IImageCrawler {

    private final ImageCrawlerConfig config;
    private final ExecutorService imageDownloaderExecutor;
    private final ExecutorService websiteAnalyzerExecutor;

    private final ImageDownloader imageDownloader = new ImageDownloader();
    private final WebsiteAnalyzer websiteAnalyzer = new WebsiteAnalyzer();

    private final AtomicInteger currentCrawls = new AtomicInteger(0);
    private final AtomicInteger currentDownloads = new AtomicInteger(0);
    private final AtomicInteger urlCounter = new AtomicInteger(0);

    public ImageCrawler(
            ImageCrawlerConfig config
            ){
        this.config = config;
        this.imageDownloaderExecutor = Executors.newFixedThreadPool(
                config.getNumberOfAllowedParallelImageDownloads()
        );
        this.websiteAnalyzerExecutor = Executors.newFixedThreadPool(
                config.getNumberOfAllowedParallelWebsiteScans()
        );
    }


    @Override
    public void crawl(URI uri) {
        currentCrawls.incrementAndGet();

        int folderNumber = urlCounter.incrementAndGet();
        Path folder = config.getDownloadPath().resolve(String.valueOf(folderNumber));

        websiteAnalyzerExecutor.submit(() ->{
            try{
                List<URI> imageURIs = websiteAnalyzer.anaylzeWebsite(uri);
                Files.createDirectories(folder);

                for (URI imageURI : imageURIs) {
                    currentDownloads.incrementAndGet();
                    imageDownloaderExecutor.submit(() -> {
                        try {
                            imageDownloader.downloadImage(imageURI, folder);
                        } finally {
                            currentDownloads.decrementAndGet();
                        }
                    });
                }
                } catch (Exception e) {
                    System.err.println("Error: " + uri + " - " + e.getMessage());
                }
            finally {
                currentCrawls.decrementAndGet();
            }
        });

    }

    @Override
    public boolean isIdle() {
        return currentCrawls.get() == 0 && currentDownloads.get() == 0;
    }
}
