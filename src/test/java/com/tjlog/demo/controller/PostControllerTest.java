package com.tjlog.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjlog.demo.domain.Post;
import com.tjlog.demo.repository.PostRepository;
import com.tjlog.demo.request.PostCreate;
import com.tjlog.demo.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear(){
        postRepository.deleteAll();
    }

    @Test
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder().
                                title("sdd").
                                content("sdd")
                                .build();

        String json = objectMapper.writeValueAsString(request);//제이슨 형태로 가공 public getter만 접근이 가능

        System.out.println(json);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )// 해당 url로 요청
                .andExpect(status().isOk())// 응답 status를 ok로 테스트
                .andExpect(MockMvcResultMatchers.content().string(""))//요청 결과값이 테스트한 값과 똑같은지
                .andDo(print());
                //andDo = print,log를 사용할 수 있는 메서드 http요청에대한 요약을 남겨준다
    }

    @Test
    @DisplayName("/posts 요청시 title값 필수")
    void test2() throws Exception {
        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"내용입니다.\"}")

                ) // 해당 url로 요청
                .andExpect(status().isBadRequest())// 응답 status를 ok로 테스트
                .andDo(print());
        //andDo = print,log를 사용할 수 있는 메서드 http요청에대한 요약을 남겨준다
    }

    @Test
    @DisplayName("/posts 요청시 db값 저장")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder().
                title("제목입니다.").
                content("내용입니다.").
                build();

        String json = objectMapper.writeValueAsString(request);//제이슨 형태로 가공 public getter만 접근이 가능

        System.out.println(json);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")

                ) // 해당 url로 요청
                .andExpect(status().isOk())
                .andDo(print());
        //then
        assertEquals(1L,postRepository.count());
        //andDo = print,log를 사용할 수 있는 메서드 http요청에대한 요약을 남겨준다
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("12345")
                .content("bar")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)

                ) // 해당 url로 요청
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지 가져온다")
    void findAll() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0,20)//0~19
                .mapToObj(i -> Post.builder()
                        .title("tj 제목 -" + i)
                        .content("가오동 은어송마을" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                ) // 해당 url로 요청
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(10)))
                .andExpect(jsonPath("$[0].title").value("tj 제목 -19"))
                .andExpect(jsonPath("$[0].content").value("가오동 은어송마을19"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정")
    void editTtitle() throws Exception {
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


        //expected
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))//json으로 변경
                ) // 해당 url로 요청
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 삭제")
    void PostDelete() throws Exception {
        //given
        Post post = Post.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}",post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void NotPostSelect() throws Exception {
        //expected
        mockMvc.perform(delete("/posts/{postId}",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void NotPostUpdate() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("태진")
                .content("은어송마을1단지")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성지 제목에 '바보'는 포함 x")
    void test1() throws Exception {
        //given
        PostCreate request = PostCreate.builder().
                title("나는 바보입니다.").
                content("은어송마을")
                .build();

        String json = objectMapper.writeValueAsString(request);//제이슨 형태로 가공 public getter만 접근이 가능

        System.out.println(json);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )// 해당 url로 요청
                .andExpect(status().isBadRequest())
                .andDo(print());
        //andDo = print,log를 사용할 수 있는 메서드 http요청에대한 요약을 남겨준다
    }


}