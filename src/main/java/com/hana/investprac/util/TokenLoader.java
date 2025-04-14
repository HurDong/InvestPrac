package com.hana.investprac.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class TokenLoader {

    public String loadToken() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("investtoken.json")) {
            if (is == null) throw new FileNotFoundException("investtoken.json not found in resources");

            ObjectMapper mapper = new ObjectMapper();
            TokenWrapper tokenWrapper = mapper.readValue(is, TokenWrapper.class);
            return tokenWrapper.getToken();
        } catch (IOException e) {
            throw new RuntimeException("토큰 파일 로딩 실패", e);
        }
    }


    @Data
    public static class TokenWrapper {
        private String token;
    }
}
