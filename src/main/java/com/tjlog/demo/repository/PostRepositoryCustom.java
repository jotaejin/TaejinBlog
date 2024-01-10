package com.tjlog.demo.repository;

import com.tjlog.demo.domain.Post;
import com.tjlog.demo.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
