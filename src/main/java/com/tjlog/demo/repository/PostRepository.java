package com.tjlog.demo.repository;

import com.tjlog.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryCustom {
}
