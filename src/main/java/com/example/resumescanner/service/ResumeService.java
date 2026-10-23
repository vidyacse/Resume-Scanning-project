package com.example.resumescanner.service;

import com.example.resumescanner.model.ResumeCandidate;
import com.example.resumescanner.util.ResumeParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ResumeService {

    private final List<ResumeCandidate> candidates = new ArrayList<>();

    // 🔥 Expanded & Improved Skills Map
    private static final Map<String, String> SKILL_MAP = Map.ofEntries(

            // Programming
            Map.entry("java", "java"),
            Map.entry("py", "python"),
            Map.entry("python", "python"),
            Map.entry("js", "javascript"),
            Map.entry("javascript", "javascript"),
            Map.entry("c", "c"),
            Map.entry("cpp", "c++"),
            Map.entry("c++", "c++"),
            Map.entry("csharp", "c#"),
            Map.entry("c#", "c#"),

            // Web
            Map.entry("html", "html"),
            Map.entry("css", "css"),
            Map.entry("react", "react"),
            Map.entry("angular", "angular"),
            Map.entry("node", "node"),
            Map.entry("nodejs", "node"),
            Map.entry("node js", "node"),
            Map.entry("express", "express"),

            // Frameworks
            Map.entry("spring", "spring"),
            Map.entry("springboot", "spring"),
            Map.entry("spring boot", "spring"),
            Map.entry("django", "django"),
            Map.entry("flask", "flask"),

            // Database
            Map.entry("mysql", "mysql"),
            Map.entry("sql", "sql"),
            Map.entry("mongodb", "mongodb"),
            Map.entry("oracle", "oracle"),

            // Tools
            Map.entry("git", "git"),
            Map.entry("github", "github"),
            Map.entry("docker", "docker"),
            Map.entry("kubernetes", "kubernetes"),

            // Data / AI
            Map.entry("ml", "machine learning"),
            Map.entry("machinelearning", "machine learning"),
            Map.entry("ai", "artificial intelligence"),
            Map.entry("datascience", "data science"),
            Map.entry("data science", "data science"),
            Map.entry("pandas", "pandas"),
            Map.entry("numpy", "numpy"),

            // Cloud
            Map.entry("aws", "aws"),
            Map.entry("azure", "azure"),
            Map.entry("gcp", "gcp")
    );

    public String processResume(MultipartFile file, String requiredSkills) throws Exception {

        // 🔥 Clean resume text
        String text = ResumeParser.extractText(file)
                .toLowerCase()
                .replaceAll("[^a-z0-9+ ]", "")
                .replaceAll("\\s+", " ");

        String[] skillsArray = requiredSkills.split(",");
        int totalSkills = skillsArray.length;

        int matchedSkills = 0;
        List<String> invalidSkills = new ArrayList<>();

        for (String skill : skillsArray) {

            String cleaned = skill.trim().toLowerCase();

            // Normalize skill
            String normalized = SKILL_MAP.getOrDefault(cleaned, cleaned)
                    .replaceAll("\\s+", " ");

            // 🔥 Smart matching
            boolean isMatch =
                    text.contains(normalized) ||
                    text.replace(" ", "").contains(normalized.replace(" ", ""));

            if (isMatch) {
                matchedSkills++;
            } else {
                invalidSkills.add(cleaned);
            }
        }

        // ✅ Final Score (Percentage)
        int score = (matchedSkills * 100) / totalSkills;

        String name = file.getOriginalFilename();
        String email = "not_found@example.com";

        candidates.add(new ResumeCandidate(name, email, score));
        candidates.sort(Comparator.comparingInt(ResumeCandidate::getScore).reversed());

        // ❌ Show invalid skills if any
        if (!invalidSkills.isEmpty()) {
            return "Invalid skills: " + String.join(", ", invalidSkills);
        }

        return "Success";
    }

    public List<ResumeCandidate> getCandidates() {
        return candidates;
    }
}