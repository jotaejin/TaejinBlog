package com.tjlog.demo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tjlog.demo.domain.Post;
import com.tjlog.demo.domain.QPost;
import com.tjlog.demo.request.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tjlog.demo.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postSearch) {
        //10개의 데이터를 가져오는 페이징메서드
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())//데이터를 얼마나 가져올지
                .offset(postSearch.getOffset())//어디서부터 가져올지
                .orderBy(post.id.desc())
                .fetch();
    }
}
