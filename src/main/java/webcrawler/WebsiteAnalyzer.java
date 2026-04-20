package webcrawler;

import interfaces.IWebsiteAnalyzer;
import logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IWebsiteAnalyzer.
 * Responsible for fetching the HTML of a given website
 * extracting all image URIs found in img-elements.
 */
public class WebsiteAnalyzer implements IWebsiteAnalyzer {

    /**
     * Connects to the given website using Jsoup, selects all img-elements
     * and extracts their source URLs as URIs.
     * If a source URL uses https, it is converted to http because
     * servers block http clients on https, returning 403.
     *
     * @param uri   URI of the website to analyze
     * @return      A list of image URIs found on the page, empty list if none found
     */
    @Override
    public List<URI> analyzeWebsite(URI uri) {
        List<URI> imageUris = new ArrayList<>();

        try {
            Logger.logInfo("Connecting to Website", uri);

            Document website = Jsoup.connect(uri.toString()).get();

            for (Element img : website.select("img")) {
                Logger.logInfo("Found image element", uri);

                String src = img.absUrl("src");
                if (!src.isEmpty()) {
                    src = src.replace("https://", "http://");
                    imageUris.add(URI.create(src));
                }
            }
            Logger.logInfo("Finished Analyzing Website", uri);


        } catch (Exception e) {
            Logger.logError("Failed to Analyze", uri, e);
        }
        return imageUris;
    }
}
