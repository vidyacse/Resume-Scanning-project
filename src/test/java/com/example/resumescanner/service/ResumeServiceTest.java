package com.example.resumescanner.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResumeServiceTest {

    @Test
    void test() throws Exception {

        ResumeService s = new ResumeService();

        MockMultipartFile f = new MockMultipartFile(
                "file",
                "r.txt",
                "text/plain",
                "Java".getBytes()
        );

        assertEquals("Success", s.processResume(f, "Java"));
    }
}