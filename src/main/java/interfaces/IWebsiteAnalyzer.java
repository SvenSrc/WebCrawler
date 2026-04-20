package interfaces;

import java.net.URI;
import java.util.List;

/**
 * Defines the contract for analyzing websites and extracting image URIs.
 * Implementations are responsible for fetching the HTML of a given website
 * and returning all image URIs found in img-elements.
 */
public interface IWebsiteAnalyzer {

    /**
     * Represents analyzing the given website and extracts all image URIs
     * found in img-elements of the HTML source.
     *
     * @param uri   The URI of the website to analyze
     * @return      A list of image URIs found on the page, empty list if none found
     */
    List<URI> analyzeWebsite(URI uri);

}
