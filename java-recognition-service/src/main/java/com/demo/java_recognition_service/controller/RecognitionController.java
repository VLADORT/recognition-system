package com.demo.java_recognition_service.controller;

import com.demo.java_recognition_service.exception.CouldNotParseFileException;
import com.demo.java_recognition_service.service.RecognitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
public class RecognitionController {

    private final RecognitionService recognitionService;

    public RecognitionController(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Content-Type= multipart/form-data", path = "/perform-recognition")
    public ResponseEntity<Resource> performKMeans(@RequestParam(value = "file") @Valid MultipartFile file) throws Throwable {

        log.info("Received file: \n" +
                "filename=\"" + file.getName() +"\"\n" +
                "contentType=\"" + file.getContentType() +"\"\n" +
                "size=\"" + file.getSize() +"\"\n");

        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(new ByteArrayResource(recognitionService.processThreshold(file.getBytes())));
        } catch (IOException ex) {
            throw new CouldNotParseFileException("Could not encode/decode file", ex);
        }
    }
}