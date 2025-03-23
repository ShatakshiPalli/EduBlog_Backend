package com.miniProject.EduBlog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.miniProject.EduBlog.dto.BlogDto;
import com.miniProject.EduBlog.dto.UserDto;
import com.miniProject.EduBlog.entity.Blog;
import com.miniProject.EduBlog.repository.BlogRepository;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    // ✅ Implementing getAllBlogs()
    public List<BlogDto> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        
        // Convert List<Blog> to List<BlogDto>
        return blogs.stream().map(blog -> {
            String authorName = blog.getAuthor() != null ? blog.getAuthor() : "Anonymous";
            UserDto author = new UserDto(null, authorName, "user@example.com");
            
            return new BlogDto(
                blog.getId(),
                blog.getTitle(),
                blog.getCategory(),
                blog.getDescription(),
                blog.getDescription(), // Using description as content for now
                LocalDateTime.now(), // Using current time for now
                author
            );
        }).collect(Collectors.toList());
    }

    // ✅ Implementing createBlog()
    public String createBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setCategory(blogDto.getCategory());
        blog.setDescription(blogDto.getDescription());
        blog.setAuthor(blogDto.getAuthor() != null && blogDto.getAuthor().getUsername() != null 
                      ? blogDto.getAuthor().getUsername() 
                      : "Anonymous");

        blogRepository.save(blog);
        return "Blog created successfully!";
    }
    
    // Get blog by ID
    public BlogDto getBlogById(String id) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));
        
        String authorName = blog.getAuthor() != null ? blog.getAuthor() : "Anonymous";
        UserDto author = new UserDto(null, authorName, "user@example.com");
        
        return new BlogDto(
            blog.getId(),
            blog.getTitle(),
            blog.getCategory(),
            blog.getDescription(),
            blog.getDescription(), // Using description as content for now
            LocalDateTime.now(), // Using current time for now
            author
        );
    }
}
