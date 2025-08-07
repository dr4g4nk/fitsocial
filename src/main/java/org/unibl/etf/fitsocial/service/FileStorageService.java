package org.unibl.etf.fitsocial.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.configuration.FileStorageProperties;
import org.unibl.etf.fitsocial.entity.FileType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path baseLocation;

    public FileStorageService(FileStorageProperties props) {
        this.baseLocation = Paths.get(props.getBaseDir())
                .toAbsolutePath()
                .normalize();
        try {
            // Kreiraj bazni direktorij
            Files.createDirectories(baseLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create base storage dir", ex);
        }
    }

    public String store(MultipartFile file, FileType type) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (filename.contains("..")) {
                throw new RuntimeException("Invalid path sequence in " + filename);
            }

            Path targetDir = baseLocation.resolve(type.getFolderName());
            Files.createDirectories(targetDir);

            filename = String.format("_%s", UUID.randomUUID()).concat(filename);

            Path target = targetDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    public String storeThumbnail(String videoUri){
        var targetFile = new File(videoUri);
        try {
            var thumbnailDir = baseLocation.resolve(FileType.THUMBNAIL.getFolderName());
            Files.createDirectories(thumbnailDir);
            return VideoThumbnailGenerator.generateThumbnail(targetFile, new File(thumbnailDir.toUri()), 1);
        }catch (IOException e) {
            throw new RuntimeException("Failed to store file ", e);
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadAsResource(String path) {
        try {
            // Normaliziraj i učitaj Path
            Path file = Paths.get(path).toAbsolutePath().normalize();

            // Opcionalno: provjeri da li se nalazi unutar baseLocation
            if (!file.startsWith(baseLocation)) {
                throw new RuntimeException("Access outside storage directory is not allowed");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + path);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + path, e);
        }
    }
}
