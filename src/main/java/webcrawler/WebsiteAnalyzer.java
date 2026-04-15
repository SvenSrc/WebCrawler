package webcrawler;

import interfaces.IWebsiteAnalyzer;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WebsiteAnalyzer implements IWebsiteAnalyzer {

    @Override
    public List<URI> anaylzeWebsite(URI uri) {
        List<URI> imageUris = new ArrayList<>();

        try {
            Document website = Jsoup.connect(uri.toString()).get();

            for (Element img : website.select("img")) {
                String src = img.absUrl("src");
                if (!src.isEmpty()) {
                    src = src.replace("https", "http");
                    imageUris.add(URI.create(src));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to analyze: " + uri + " - " + e.getMessage());
        }
        return imageUris;
    }
}
