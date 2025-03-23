package com.miniProject.EduBlog.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.miniProject.EduBlog.entity.Blog;

@Repository
public interface BlogRepository extends MongoRepository<Blog, String> {
    List<Blog> findByCategory(String category);
    List<Blog> findByAuthor(String author);
    List<Blog> findByTitleContainingIgnoreCase(String title);
}
