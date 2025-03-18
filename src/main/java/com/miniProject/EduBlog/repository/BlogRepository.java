package com.miniProject.EduBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miniProject.EduBlog.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
