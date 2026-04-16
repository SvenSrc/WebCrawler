package webcrawler;

import config.ImageCrawlerConfig;
import interfaces.IImageCrawler;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageCrawler implements IImageCrawler {

    private final ImageCrawlerConfig config;
    private final ExecutorService imageDownloaderExecutor;
    private final ExecutorService websiteAnalyzerExecutor;
    private final List<Future<?>> downloadFutures = Collections.synchronizedList(new ArrayList<>());

    private final ImageDownloader imageDownloader = new ImageDownloader();
    private final WebsiteAnalyzer websiteAnalyzer = new WebsiteAnalyzer();

    private final AtomicInteger currentCrawls = new AtomicInteger(0);
    private final AtomicInteger currentDownloads = new AtomicInteger(0);

    public ImageCrawler(
            ImageCrawlerConfig config
    ) {
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
        // Wo wird geprüft?
        currentCrawls.incrementAndGet();

        String host = uri.getHost();

        Path webFolder = config.getDownloadPath().resolve(host);
        Path folder = getIncrementingFolder(webFolder);

        websiteAnalyzerExecutor.submit(() -> {
            try {
                List<URI> imageURIs = websiteAnalyzer.anaylzeWebsite(uri);
                Files.createDirectories(folder);

                for (URI imageURI : imageURIs) {
                    currentDownloads.incrementAndGet();
                    Future<?> downloadFuture = imageDownloaderExecutor.submit(() -> {
                        try {
                            imageDownloader.downloadImage(imageURI, folder);
                        } finally {
                            currentDownloads.decrementAndGet();
                        }
                    });
                    downloadFutures.add((downloadFuture));
                }

                for(Future<?> downloadFuture : downloadFutures){
                    downloadFuture.get();
                }

            } catch (Exception e) {
                System.err.println("Error: " + uri + " - " + e.getMessage());
            } finally {
                currentCrawls.decrementAndGet();
            }
        });

    }

    @Override
    public boolean isIdle() {
        return currentCrawls.get() == 0 && currentDownloads.get() == 0;
    }

    @Override
    public Path getIncrementingFolder(Path webFolder) {
        int counter = 1;
        while (true) {
            Path candidate = webFolder.resolve(String.valueOf(counter));
            if (!Files.exists(candidate)) {
                return candidate;
            }
            counter++;
        }

    }

    @Override
    public void shutdown() throws Exception {
        websiteAnalyzerExecutor.shutdown();
        websiteAnalyzerExecutor.awaitTermination(15, TimeUnit.SECONDS);

        imageDownloaderExecutor.shutdown();
        imageDownloaderExecutor.awaitTermination(15, TimeUnit.SECONDS);

        System.out.println("Finished. Shutting down");
    }
}