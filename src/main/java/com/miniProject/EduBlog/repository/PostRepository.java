package com.miniProject.EduBlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniProject.EduBlog.entity.Post;
import com.miniProject.EduBlog.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
    List<Post> findAllByOrderByCreatedAtDesc();
} 