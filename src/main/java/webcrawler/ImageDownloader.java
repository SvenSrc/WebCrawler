package webcrawler;

import interfaces.IImageDownloader;
import logger.Logger;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation of IImageDownloader.
 * Responsible for downloading images and saving them
 */
public class ImageDownloader implements IImageDownloader {

    /**
     * Downloads an image from the given URI and saves it to the specified folder.
     * Resolves filename conflicts automatically by appending a numeric suffix with resolveConflict(...).
     *
     * @param imageURI      The URI of the image to download
     * @param folder        The target folder to save the image into
     */
    @Override
    public void downloadImage(URI imageURI, Path folder) {
        try {
            Logger.logInfo("Starting Download", imageURI);

            String name = Path.of(imageURI.getPath()).getFileName().toString();
            Path targetPath = resolveConflict(folder, name);

            try (InputStream inputStream = imageURI.toURL().openStream()){
                Files.copy(inputStream, targetPath);
            }

            Logger.logInfo("Finished Download", imageURI);
        } catch (Exception e) {
            Logger.logError("Failed to Download", imageURI, e);
        }
    }

    /**
     * Resolves filename conflicts by appending an incrementing numeric suffix.
     * If the target file does not exist, the original filename is returned.
     * If it exists, suffixes are appended until a free name is found.
     * Example: photo.jpg -> photo_1.jpg -> photo_2.jpg
     * Internal helper Method
     *
     * @param folder    The folder where the file will be saved
     * @param fileName  The desired original filename including extension
     * @return          A Path that does not yet exist on the filesystem
     */
    private Path resolveConflict(Path folder, String fileName) {
        Path target = folder.resolve(fileName);

        if (!Files.exists(target)) {
            return target;
        }

        int dotIndex = fileName.lastIndexOf('.');
        String imageName = dotIndex >= 0 ? fileName.substring(0, dotIndex) : fileName;
        String extension = dotIndex >= 0 ? fileName.substring(dotIndex) : "";

        int counter = 1;
        while (true) {
            target = folder.resolve(imageName + "_" + counter + extension);
            if (!Files.exists(target)) return target;
            counter++;
        }
    }
}
