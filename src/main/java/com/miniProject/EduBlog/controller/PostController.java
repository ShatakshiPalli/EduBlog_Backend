package com.miniProject.EduBlog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.miniProject.EduBlog.dto.PostRequest;
import com.miniProject.EduBlog.entity.Post;
import com.miniProject.EduBlog.entity.User;
import com.miniProject.EduBlog.repository.PostRepository;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setCategory(postRequest.getCategory());
        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        post.setAuthor(user);
        post.onCreate();
        
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(required = false) String category, @AuthenticationPrincipal User user) {
        List<Post> allPosts = postRepository.findAll();
        List<Post> filteredPosts;
        
        if (category != null && !category.equalsIgnoreCase("all")) {
            filteredPosts = allPosts.stream()
                .filter(post -> post.getCategory().equalsIgnoreCase(category))
                .toList();
        } else {
            filteredPosts = allPosts;
        }

        int total = allPosts.size();
        int showing = filteredPosts.size();
        String message;

        if (user == null) {
            message = "Please log in to see all posts";
            // If user is not logged in, only show first 3 posts
            filteredPosts = filteredPosts.stream().limit(3).toList();
            showing = filteredPosts.size();
        } else {
            message = "Showing all available posts";
        }

        var response = new java.util.HashMap<String, Object>();
        response.put("blogs", filteredPosts);
        response.put("message", message);
        response.put("total", total);
        response.put("showing", showing);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();
        if (!post.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        post.setTitle(postRequest.getTitle());
        post.setCategory(postRequest.getCategory());
        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        post.onUpdate();
        
        Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id, @AuthenticationPrincipal User user) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();
        if (!post.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("You can only delete your own posts");
        }

        postRepository.delete(post);
        return ResponseEntity.ok().body("Post deleted successfully");
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getMyPosts(@AuthenticationPrincipal User user) {
        List<Post> posts = postRepository.findByAuthor(user);
        return ResponseEntity.ok(posts);
    }
} 