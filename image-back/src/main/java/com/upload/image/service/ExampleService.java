package com.upload.image.service;

import com.upload.image.model.Example;
import com.upload.image.repository.ExampleRepository;
import com.upload.image.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;

    private final FileService fileService;



    public ExampleService(ExampleRepository exampleRepository, FileService fileService) {
        this.exampleRepository = exampleRepository;
        this.fileService = fileService;
    }

    public List<Example> findAll() {
        return exampleRepository.findAll();
    }

    public Example findById(Long id) {
        Optional<Example> object = exampleRepository.findById(id);
        if (object.isPresent()) {
            return object.get();
        } else {
            throw new ObjectNotFoundException("Object Not Found! Id: " + id + " Type " + Example.class.getSimpleName());
        }
    }

    public Example insertCampaign(MultipartFile image) throws IOException {
        Example example = new Example();
        Path imagePath = fileService.upload(image);
        example.setImageUrl(String.valueOf(imagePath));
        return exampleRepository.save(example);

    }

    public File loadImage(Long id) throws IOException {
        Example example = findById(id);
        return new File(example.getImageUrl());
    }


}
