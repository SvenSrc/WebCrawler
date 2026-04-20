package webcrawler;

import config.ImageCrawlerConfig;
import interfaces.IImageCrawler;
import logger.Logger;

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

/**
 * Implementation of IImageCrawler.
 * Central coordinator of the web crawling process.
 */
public class ImageCrawler implements IImageCrawler {

    private final ImageCrawlerConfig config;
    private final ExecutorService imageDownloaderExecutor;
    private final ExecutorService websiteAnalyzerExecutor;
    private final List<Future<?>> downloadFutures = Collections.synchronizedList(new ArrayList<>());

    private final ImageDownloader imageDownloader = new ImageDownloader();
    private final WebsiteAnalyzer websiteAnalyzer = new WebsiteAnalyzer();

    private final AtomicInteger currentCrawls = new AtomicInteger(0);
    private final AtomicInteger currentDownloads = new AtomicInteger(0);

    /**
     * Creates a new ImageCrawler with the given configuration.
     * Initializes two thread pools:
     *  - one for website scanning
     *  - one for image downloading
     *  Both are bounded by the config values.
     *
     * @param config    The configuration object containing thread pool sizes
     *                  and the download directory path
     */
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

    /**
     * Adds a URI to the crawl queue. If a website scan slot is available the scan
     * starts immediately, otherwise it is queued until a slot frees up.
     * For each image found, a download task is submitted to the imageDownloaderExecutor.
     * Uses Future to ensure all downloads of a page finish before the scan is marked done.
     *
     * @param uri   The URI of the website to crawl
     */
    @Override
    public void crawl(URI uri) {
        currentCrawls.incrementAndGet();
        Logger.logCounter("Increment Crawl Amount", currentCrawls.get());

        String host = uri.getHost();

        Path webFolder = config.getDownloadPath().resolve(host);
        Path folder = getIncrementingFolder(webFolder);

        websiteAnalyzerExecutor.submit(() -> {
            try {
                List<URI> imageURIs = websiteAnalyzer.analyzeWebsite(uri);
                Files.createDirectories(folder);

                for (URI imageURI : imageURIs) {
                    currentDownloads.incrementAndGet();
                    Logger.logCounter("Increment Download Amount", currentDownloads.get());

                    Future<?> downloadFuture = imageDownloaderExecutor.submit(() -> {
                        try {
                            imageDownloader.downloadImage(imageURI, folder);
                        } finally {
                            currentDownloads.decrementAndGet();
                            Logger.logCounter("Decrement Download Amount", currentDownloads.get());

                        }
                    });
                    downloadFutures.add((downloadFuture));
                }

                for (Future<?> downloadFuture : downloadFutures) {
                    downloadFuture.get();
                }

            } catch (Exception e) {
                Logger.logError("Crawl Error", uri, e);
            } finally {
                currentCrawls.decrementAndGet();
                Logger.logCounter("Decrement Crawl Amount", currentCrawls.get());
            }
        });

    }

    /**
     * Returns whether the crawler is currently idle.
     * Only returns true if no websites are being scanned
     * and no images are being downloaded at the same time.
     *
     * @return  true if no scans or downloads are in progress, false otherwise
     */
    @Override
    public boolean isIdle() {
        return currentCrawls.get() == 0 && currentDownloads.get() == 0;
    }

    /**
     * Shuts down both executors.
     * First waits for all website scans to finish, then waits for
     * all image downloads to complete before returning.
     *
     * @throws Exception     If the shutdown is interrupted while waiting
     */
    @Override
    public void shutdown() throws Exception {
        websiteAnalyzerExecutor.shutdown();
        websiteAnalyzerExecutor.awaitTermination(15, TimeUnit.SECONDS);

        imageDownloaderExecutor.shutdown();
        imageDownloaderExecutor.awaitTermination(15, TimeUnit.SECONDS);

        Logger.logFinish();
    }

    /**
     * Returns the next available numbered subfolder inside the given host folder.
     * Starts at 1 and increments until a folder that does not yet exist is found.
     * Example: downloads/images/w3schools.com/1 -> 2 -> 3
     * Internal helper Method
     *
     * @param webFolder     The host folder to search for the next free subfolder
     * @return              Path to the next free numbered subfolder
     */
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
}