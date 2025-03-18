package com.miniProject.EduBlog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniProject.EduBlog.dto.PostRequest;
import com.miniProject.EduBlog.entity.Post;
import com.miniProject.EduBlog.entity.User;
import com.miniProject.EduBlog.repository.PostRepository;

@RestController
@RequestMapping("/api/posts")
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
        
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
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
        Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();
        if (!post.getAuthor().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("You can only delete your own posts");
        }

        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getMyPosts(@AuthenticationPrincipal User user) {
        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(posts);
    }
} 