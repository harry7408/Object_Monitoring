package com.harry.backend.service;

import com.harry.backend.Api;
import com.harry.backend.dto.FastApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ObjectMonitorService {

    public Api<FastApiResponse> getFilteredResult(MultipartFile image) throws Exception {
        FastApiResponse response = getResultFromApi(image);

        if (response == null || response.getResult() == null || response.getResult().isEmpty()) {
            return Api.<FastApiResponse>builder()
                    .resultCode(String.valueOf(HttpStatus.OK))
                    .message("식별된 물체가 없습니다")
                    .build();
        }

        // ✅ confidence 0.75 이상만 필터링
        List<FastApiResponse.ObjectDto> filteredResults = response.getResult().stream()
                .filter(result -> result.getConfidence() >= 0.75)
                .collect(Collectors.toList());

        FastApiResponse filteredResponse = new FastApiResponse();
        filteredResponse.setResult(filteredResults);

        return Api.<FastApiResponse>builder()
                .resultCode(String.valueOf(HttpStatus.OK))
                .message("식별된 결과")
                .data(filteredResponse)
                .build();
    }


    // 이미지를 분석하여 결과를 출력하는 API와 통신
    private FastApiResponse getResultFromApi(MultipartFile image) throws Exception {
        try {
            Resource fileResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            // 통신 요청 부분
            ResponseEntity<FastApiResponse> response =
                    restTemplate.postForEntity("http://localhost:8081/object_monitor", request, FastApiResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception();
        }
    }
}

