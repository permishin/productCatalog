package com.example.productcatalog.service;

import com.example.productcatalog.exceptions.FileStorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.Dir}")
    private String uploadDir;

    public void uploadFile(MultipartFile file) {

        try {
            Path copyLocation = Paths
                    .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));

            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }
    public String renameFile(MultipartFile file, String oldName) {
        String newName = UUID.randomUUID().toString();
        String newFileName = uploadDir + "/" + newName + "." + FilenameUtils.getExtension(oldName);
        File oldFileName = new File(uploadDir + "/" + oldName);
        File newFile = new File(newFileName);
        System.out.println("Результат переименования файла - " + oldFileName.renameTo(newFile));
        return newName + "." + FilenameUtils.getExtension(oldName);
    }
}

