package interfaces;

import java.net.URI;

/**
 * Defines the contract for a web crawler.
 * Implementation scans websites for images
 * and coordinates parallel website scanning and image downloading.
 */
public interface IImageCrawler {

    /**
     * Represents adding a URI to the crawl queue.
     *
     * @param uri   The URI of the website to crawl
     */
    void crawl(final URI uri);

    /**
     * Represents returning whether the crawler is currently idle.
     * Only true if no scans or downloads are in progress.
     *
     * @return  true if idle, false otherwise
     */
    boolean isIdle();

    /**
     * Represents the shutdown of the crawler.
     * Waits for all ongoing scans and downloads to finish.
     *
     * @throws Exception    If shutdown is interrupted
     */
    void shutdown() throws Exception;
}
