package com.example.deundeun.review.application;

import com.example.deundeun.review.api.dto.response.ReceiptInfoDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpstageService {

    private static final String CLASSIFY_URL = "/document-classification";
    private static final String PARSE_URL = "/document-parsing";
    private final WebClient upstageWebClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${upstage.api.key}")
    private String upstageApiKey;

    /**
     * Base64 변환
     */
    private String encodeToBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("이미지 인코딩 실패");
        }
    }

    /**
     * 영수증 여부 판단
     */
    public boolean isReceipt(MultipartFile receiptImage) {

        String imageUrl = encodeToBase64(receiptImage);
        Map<String, Object> requestBody = Map.of(
                "model", "document-classify",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of(
                                                "type", "image_url",
                                                "image_url", Map.of("url", imageUrl)
                                        )
                                )
                        )
                ),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "document-classify",
                                "schema", Map.of(
                                        "type", "string",
                                        "oneOf", List.of(
                                                Map.of("const", "invoice", "description", "Commercial invoice"),
                                                Map.of("const", "receipt", "description", "Receipt"),
                                                Map.of("const", "contract", "description", "Contract document"),
                                                Map.of("const", "cv", "description", "Curriculum Vitae"),
                                                Map.of("const", "others", "description", "Other")
                                        )
                                )
                        )
                )
        );

        try {
            JsonNode response = upstageWebClient.post()
                    .uri(CLASSIFY_URL)
                    .header("Authorization", "Bearer " + upstageApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response == null) {
                return false;
            }

            log.info("[Upstage Classify Response] {}", response);

            String result = response.path("output").asText("");

            return result.equals("receipt");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 영수증 파싱
     */
    public ReceiptInfoDto extractReceiptInfo(MultipartFile receiptImage) {

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("output_formats", "[\"html\", \"text\"]");
            builder.part("base64_encoding", "[\"table\"]");
            builder.part("ocr", "auto");
            builder.part("coordinates", "true");
            builder.part("model", "document-parse");
            builder.part("document", receiptImage.getResource());

            String response = upstageWebClient.post()
                    .uri(PARSE_URL)
                    .header("Authorization", "Bearer " + upstageApiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("OCR 파싱 응답: {}", response);

            JsonNode root = objectMapper.readTree(response);

            JsonNode extracted = root
                    .path("results").path(0)
                    .path("extracted_data");

            return new ReceiptInfoDto(
                    extracted.path("store_name").asText(""),
                    extracted.path("card").asText(""),
                    extracted.path("total_amount").asText(""),
                    extracted.path("datetime").asText("")
            );

        } catch (Exception e) {
            log.error("영수증 OCR 파싱 오류", e);
            throw new RuntimeException("영수증 정보 추출 실패");
        }
    }

}
