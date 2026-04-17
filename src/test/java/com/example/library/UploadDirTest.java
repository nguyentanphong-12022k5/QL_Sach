package com.example.library;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UploadDirTest {

    @Test
    void testUploadDirCreation() throws Exception {
        String uploadPath = "uploads/img/sach/";
        File uploadFolder = new File(uploadPath);
        
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        
        assertTrue(uploadFolder.exists(), "Upload directory should exist");
        
        // Test write access
        Path testFile = Paths.get(uploadPath + "test.txt");
        Files.write(testFile, "test content".getBytes());
        assertTrue(Files.exists(testFile), "Should be able to write to upload directory");
        
        // Clean up
        Files.delete(testFile);
    }
}
