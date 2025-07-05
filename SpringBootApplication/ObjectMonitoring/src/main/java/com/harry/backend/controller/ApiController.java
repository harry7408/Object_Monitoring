package com.harry.backend.controller;

import com.harry.backend.Api;
import com.harry.backend.dto.FastApiResponse;
import com.harry.backend.service.ObjectMonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ObjectMonitorService objectMonitorService;

    @PostMapping(
            path = "/object/monitor",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Api<FastApiResponse> results(
            @RequestPart("image") MultipartFile image
    ) {
        try {
            return objectMonitorService.getFilteredResult(image);
        } catch (Exception e) {
            return Api.<FastApiResponse>builder()
                    .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }
}






