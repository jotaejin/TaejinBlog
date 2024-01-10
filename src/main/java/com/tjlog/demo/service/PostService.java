package com.tjlog.demo.service;

import com.tjlog.demo.domain.Post;
import com.tjlog.demo.domain.PostEditor;
import com.tjlog.demo.exception.PostNotFound;
import com.tjlog.demo.repository.PostRepository;
import com.tjlog.demo.request.PostCreate;
import com.tjlog.demo.request.PostEdit;
import com.tjlog.demo.request.PostSearch;
import com.tjlog.demo.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public Post write(PostCreate postCreate){
        //repository.save(postCreate)
        // postCreate -> entity
        postCreate.validate(postCreate);
        Post post = Post.builder().
                title(postCreate.getTitle()).
                content(postCreate.getContent()).build();
        Post save = postRepository.save(post);

        return save;
    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow((PostNotFound::new));//PostNotFound 에러를 발생

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /**
         * controller -> Webservice -> Repository
         */

    }

    //글이 너무 많은 경우 -> 비용이 많이 든다
    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional //save를 안해줘도 알아서 commit 해줌
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow((PostNotFound::new));

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();//post에있는 글들을 초기화

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();//build 하면 PostEditor 클래스로 리턴

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow((PostNotFound::new));

        postRepository.delete(post);
    }
}
