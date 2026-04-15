package interfaces;

import java.net.URI;
import java.nio.file.Path;

public interface IImageDownloader {

    void downloadImage(URI imageURI, Path folder);
    Path resolveConflict(Path folder, String fileName);

}
