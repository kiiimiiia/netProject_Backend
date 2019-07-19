package net.net.controller;

import net.net.entity.File;
import net.net.response.BaseResponse;
import net.net.response.FileResponse;
import net.net.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("C:/Users/EsmatRashidi/IdeaProjects/net_backend-master/Files/")
                .path(fileName)
                .toUriString();
        File newFile = new File(fileName,fileDownloadUri,file.getContentType());
        FileResponse response = new FileResponse(newFile);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/downloadFile/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
