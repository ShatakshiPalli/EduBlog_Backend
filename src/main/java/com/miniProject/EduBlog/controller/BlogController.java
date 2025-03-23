package com.miniProject.EduBlog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.miniProject.EduBlog.dto.BlogDto;
import com.miniProject.EduBlog.service.BlogService;

@RestController
@RequestMapping("/api/v1/blogs")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        
        List<BlogDto> blogs = blogService.getAllBlogs();
        
        if (!isAuthenticated && !blogs.isEmpty()) {
            // For unauthenticated users, return limited blogs and a message
            List<BlogDto> limitedBlogs = blogs.subList(0, Math.min(blogs.size(), 3));
            return ResponseEntity.ok(Map.of(
                "blogs", limitedBlogs,
                "message", "Login to view more blogs",
                "total", blogs.size(),
                "showing", limitedBlogs.size()
            ));
        }
        
        // For authenticated users, return all blogs
        return ResponseEntity.ok(Map.of(
            "blogs", blogs,
            "total", blogs.size(),
            "showing", blogs.size()
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable String id) {
        BlogDto blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody BlogDto blogDto) {
        String result = blogService.createBlog(blogDto);
        return ResponseEntity.ok(Map.of("message", result));
    }
}
