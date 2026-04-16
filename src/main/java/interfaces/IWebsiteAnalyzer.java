package interfaces;

import java.net.URI;
import java.util.List;

public interface IWebsiteAnalyzer {

    List<URI> analyzeWebsite(URI uri);

}
