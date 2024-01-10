package com.tjlog.demo.controller;

import com.tjlog.demo.domain.Post;
import com.tjlog.demo.exception.InvalidRequest;
import com.tjlog.demo.request.PostCreate;
import com.tjlog.demo.request.PostEdit;
import com.tjlog.demo.request.PostSearch;
import com.tjlog.demo.response.PostResponse;
import com.tjlog.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//데이터를 검증하는 이유

//1. 클라이언트 개발자가 실수로 값을 안보낼 수 있다
//2. 클라이언트 버그로 값이 누락
//3.해커가 값을 임의로 조작해 보낼 수 있다
//4.db값을 저장할 때 의도치 않은 오류가 발생할 수 있다
//5.서버 개발자의 편안함을 위해서

@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //글등록
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        Post write = postService.write(request);
        //case1. 저장한 데이터 entity -> response 로 응답하기
        //case2. 저장한 데이터의 primary_id -> response 로 응답하기
        //case3. 응답 필요x -> 클라이언트에서 모든 글을 잘 관리

    }

    //페이징
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch){ //쿼리파라미터로 오기때문에 @ModelAttribute 사용
        return postService.getList(postSearch);
    }

    /**
     * /posts -> 글 검색 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개 조회
     */

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long id){
        return postService.get(id);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){
         postService.edit(postId,request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }
}
