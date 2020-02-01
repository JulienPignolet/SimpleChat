package univ.lorraine.simpleChat.SimpleChat.fileUpload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import univ.lorraine.simpleChat.SimpleChat.model.File;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/getFile/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId, HttpServletRequest request) {
        File fileToGet = fileStorageService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileToGet.getContentType()))
                .body(new ByteArrayResource(fileToGet.getData()));
    }

    @GetMapping("/getFileData/{fileId}")
    public ResponseEntity<String> getFileData(@PathVariable Long fileId, HttpServletRequest request) {

        File fileToGet = fileStorageService.getFileById(fileId);

        return ResponseEntity.ok()
                .body(toJSON(fileToGet));
    }
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, HttpServletRequest request) {

        File fileToGet = fileStorageService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileToGet.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileToGet.getName()+ "\"")
                .body(new ByteArrayResource(fileToGet.getData()));
    }
    public String toJSON(File file)
    {
        JsonObject json = Json.createObjectBuilder()
                .add("fileId", file.getId())
                .add("fileName", file.getName())
                .add("fileType", file.getContentType())
                .add("fileSize", file.getData().length)
                .build();
        return json.toString();
    }
}

