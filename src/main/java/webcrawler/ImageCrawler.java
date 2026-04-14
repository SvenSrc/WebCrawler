package webcrawler;

import config.ImageCrawlerConfig;
import interfaces.IImageCrawler;
import interfaces.IImageCrawlerConfig;

import java.awt.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageCrawler implements IImageCrawler {

    private final ImageCrawlerConfig config;
    private final ExecutorService imageDownloaderExecutor;
    private final ExecutorService websiteAnalyzerExecutor;

    private ImageDownloader imageDownloader = new ImageDownloader();
    private WebsiteAnalyzer websiteAnalyzer = new WebsiteAnalyzer();

    private final AtomicInteger currentCrawls = new AtomicInteger(0);
    private final AtomicInteger currentDownloads = new AtomicInteger(0);

    public ImageCrawler(
            ImageCrawlerConfig config,
            ImageDownloader imageDownloader,
            WebsiteAnalyzer websiteAnalyzer
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

        websiteAnalyzerExecutor.submit(() ->{
            try{
                websiteAnalyzer.anaylzeWebsite();
            } finally {
                currentCrawls.decrementAndGet();
            }
        });

    }

    @Override
    public boolean isIdle() {
        if(currentCrawls.get() == 0 && currentDownloads.get() == 0){
            return true;
        }else {
            return false;
        }
    }
}
