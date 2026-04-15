package interfaces;

import java.net.URI;
import java.nio.file.Path;

public interface IImageCrawler {

    void crawl(final URI uri);
    boolean isIdle();

    Path getIncrementingFolder(Path webFolder);
}
