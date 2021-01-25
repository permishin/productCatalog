package com.example.productcatalog.service;

import com.example.productcatalog.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class LoadImages {

    @Value("${upload.path}")
    private String uploadPath;

    public String loadImg(MultipartFile file) throws IOException {
        String resultFileName = null;
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));


        }
        return resultFileName;
    }
}
