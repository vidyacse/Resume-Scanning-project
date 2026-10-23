package com.example.resumescanner.controller;

import com.example.resumescanner.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ResumeController {

    private final ResumeService service;

    public ResumeController(ResumeService service) {
        this.service = service;
    }

    // 🔐 Login Page
    @GetMapping("/")
    public String login() {
        return "login";
    }

    // 📤 Upload Page
    @GetMapping("/uploadPage")
    public String uploadPage() {
        return "upload";
    }

    // 📄 Handle Upload
    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file,
                               @RequestParam("skills") String skills,
                               Model model) {

        try {
            String result = service.processResume(file, skills);

            // ❌ If invalid skills found
            if (result.startsWith("Invalid")) {
                model.addAttribute("error", result);
                model.addAttribute("candidates", service.getCandidates());
                return "upload";
            }

            // ✅ Success
            model.addAttribute("success", "Resume processed successfully!");
            return "redirect:/dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Error processing resume!");
            return "upload";
        }
    }

    // 📊 Dashboard Page
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("candidates", service.getCandidates());
        return "dashboard";
    }
}