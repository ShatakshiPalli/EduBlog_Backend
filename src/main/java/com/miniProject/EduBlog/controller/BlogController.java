package com.miniProject.EduBlog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniProject.EduBlog.entity.Post;
import com.miniProject.EduBlog.repository.PostRepository;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {

    private final PostRepository postRepository;

    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        
        if (!isAuthenticated && !posts.isEmpty()) {
            // For unauthenticated users, return limited posts and a message
            List<Post> limitedPosts = posts.subList(0, Math.min(posts.size(), 3));
            return ResponseEntity.ok(Map.of(
                "blogs", limitedPosts,
                "message", "Login to view more blogs",
                "total", posts.size(),
                "showing", limitedPosts.size()
            ));
        }
        
        // For authenticated users, return all posts
        return ResponseEntity.ok(Map.of(
            "blogs", posts,
            "total", posts.size(),
            "showing", posts.size()
        ));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
