package net.net.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorage {
    private String uploadDirectory;

    public String getUploadDir() {
        return uploadDirectory;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDirectory = uploadDir;
    }
}
