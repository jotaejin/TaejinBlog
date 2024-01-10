package com.tjlog.demo.service;

import com.tjlog.demo.domain.Post;
import com.tjlog.demo.exception.PostNotFound;
import com.tjlog.demo.repository.PostRepository;
import com.tjlog.demo.request.PostCreate;
import com.tjlog.demo.request.PostEdit;
import com.tjlog.demo.request.PostSearch;
import com.tjlog.demo.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }
    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        //when
        postService.write(postCreate);
        //then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void findById(){
        //given
        Post requestPost = Post.builder()
                .title("제목입니다")
                .content("내용")
                .build();
        Post save = postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(save.getId());
        //then
        assertNotNull(post);
        assertEquals(1L,postRepository.count());
        assertEquals("제목입니다",post.getTitle());
        assertEquals("내용",post.getContent());

    }

    @Test
    @DisplayName("글 1페이지 조회")
    void findAll(){
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                            .title("tj 제목 -" + i)
                            .content("가오동 은어송마을" + i)
                            .build())
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC,"id");

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        //when
        List<PostResponse> posts = postService.getList(postSearch);
        //then
        assertEquals(10L, posts.size());
        assertEquals("tj 제목 -19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void edit(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("태진1")
                .content("은어송마을1단지")
                .build();
        //when
        postService.edit(post.getId(),postEdit);
        //then

        Post editPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재 x id= " + post.getId()));
        assertEquals("태진1",editPost.getTitle());
        assertEquals("은어송마을1단지",editPost.getContent());

    }

    @Test
    @DisplayName("글 내용 수정")
    void edit3(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();
        //when
        postService.edit(post.getId(),postEdit);
        //then

        Post editPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재 x id= " + post.getId()));
        assertEquals("태진",editPost.getTitle());
        assertEquals("초가집",editPost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void delete(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());
        //then
        assertEquals(0,postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송")
                .build();
        postRepository.save(post);

        //expected
       Assertions.assertThrows(PostNotFound.class, () ->{
           postService.get(post.getId() + 1L);
       });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void delete1(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class,() -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void editTest(){
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();
        //when
        postService.edit(post.getId(),postEdit);
        //expected
        assertThrows(PostNotFound.class,() -> {
            postService.edit(post.getId() + 1L, postEdit);
        });

    }

}