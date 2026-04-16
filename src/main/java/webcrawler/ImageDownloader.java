package webcrawler;

import interfaces.IImageDownloader;
import logger.Logger;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageDownloader implements IImageDownloader {
    @Override
    public void downloadImage(URI imageURI, Path folder) {
        try {
            Logger.logInfo("Starting Download", imageURI);

            String name = Path.of(imageURI.getPath()).getFileName().toString();
            Path targetFolder = resolveConflict(folder, name);

            InputStream inputStream = imageURI.toURL().openStream();
            Files.copy(inputStream, targetFolder);

            Logger.logInfo("Finished Download", imageURI);
        } catch (Exception e) {
            Logger.logError("Failed to Download", imageURI, e);
        }
    }

    @Override
    public Path resolveConflict(Path folder, String fileName) {
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
