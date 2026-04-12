package com.mgr.api.controller;

import com.mgr.api.dto.account.InternalLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class InternalAuthController {

    @Value("${server.port:8787}") // Lấy port của server, mặc định 8080
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/internal-login")
    public ResponseEntity<?> internalLogin(@RequestBody InternalLoginRequest request) {
        // 1. Chuẩn bị URL trỏ đến endpoint oauth token
        String url = "http://localhost:" + port + "/api/token";

        // 2. Thiết lập Header Authorization Basic (Fix cứng client_id và client_secret ở đây)
        // Ví dụ: client_id = "my-client", client_secret = "my-secret"
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("abc_client", "abc123");

        // 3. Chuẩn bị Body (Form data)
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());
        map.add("grant_type", request.getGrantType() != null ? request.getGrantType() : "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        try {
            // 4. Gọi đến API token và trả về kết quả cho client
            ResponseEntity<Object> response = restTemplate.postForEntity(url, entity, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }
}