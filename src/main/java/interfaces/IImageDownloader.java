package interfaces;

import java.net.URI;
import java.nio.file.Path;

/**
 * Defines the contract for downloading images from a URI and saving them to disk.
 * Implementations handle the file transfer and storage.
 */
public interface IImageDownloader {

    /**
     * Represents downloading an image from the given URI and saves it to the specified folder.
     *
     * @param imageURI  The URI of the image to download
     * @param folder    The target folder to save the image into
     */
    void downloadImage(URI imageURI, Path folder);
}
