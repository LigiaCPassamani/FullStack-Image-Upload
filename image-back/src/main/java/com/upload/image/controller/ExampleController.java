package com.upload.image.controller;

import com.upload.image.model.Example;
import com.upload.image.service.ExampleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value ="/api/example")
@CrossOrigin("*")
public class ExampleController {

    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @PostMapping("/insert")
    public Example insertCampaign(@RequestPart("image") MultipartFile image) throws IOException {
        return exampleService.insertCampaign(image);
    }

    @GetMapping("/loadImage/{id}")
    public ResponseEntity<byte[]> loadImageCampaign(@PathVariable Long id) throws IOException {
        File image = exampleService.loadImage(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(image))).body(Files.readAllBytes(image.toPath()));
    }
}
