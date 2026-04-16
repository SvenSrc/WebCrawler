package webcrawler;

import interfaces.IWebsiteAnalyzer;
import logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebsiteAnalyzer implements IWebsiteAnalyzer {

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
                    src = src.replace("https", "http");
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
