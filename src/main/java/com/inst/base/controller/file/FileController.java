package com.inst.base.controller.file;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class FileController {

    @GetMapping("/uploads/{folder}/{file}")
    public ResponseEntity<byte[]> getUpload(
            @PathVariable
                    String folder,
            @PathVariable
                    String file
    ) throws IOException {
        Path path = Path.of("./uploads/" + folder + "/" + file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(Files.probeContentType(path)));

        return ResponseEntity.ok().headers(httpHeaders).body(Files.readAllBytes(path));
    }
}
