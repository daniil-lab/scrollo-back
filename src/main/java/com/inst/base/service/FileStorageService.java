package com.inst.base.service;

import com.inst.base.entity.user.User;
import com.inst.base.util.FileStorageProperties;
import com.inst.base.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ServiceException("Could not create the directory where the uploaded files will be stored.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean destroyFile(String filePath) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(filePath);

            System.out.println(targetLocation);

            Files.delete(targetLocation);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Error on removing file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String storeFile(MultipartFile file, User user) {
        // Normalize file name
        try {
            // Check if the file's name contains invalid characters
            if (StringUtils.cleanPath(file.getOriginalFilename()).contains("..")) {
                throw new ServiceException("Sorry! Filename contains invalid path sequence", HttpStatus.BAD_REQUEST);
            }

            if(user != null) {
                // Copy file to the target location (Replacing existing file with the same name)
                String[] splitFilename = StringUtils.cleanPath(file.getOriginalFilename()).split("\\.");

                Files.createDirectories(fileStorageLocation.resolve(user.getId().toString()));

                String fullFilename = user.getId().toString() + "/" + UUID.randomUUID() + "." + splitFilename[splitFilename.length - 1];

                Path targetLocation = this.fileStorageLocation.resolve(fullFilename);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                return fullFilename;
            }

            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ServiceException("Could not store file. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ServiceException("File not found " + fileName, HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            throw new ServiceException("File not found " + fileName, HttpStatus.NOT_FOUND);
        }
    }
}
