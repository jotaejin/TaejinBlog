package com.tjlog.demo.request;

import com.tjlog.demo.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해라!")
    private String title;

    @NotBlank(message = "내용을 입력해라!")
    private String content;

    @Builder //테스트용 생성자
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate(PostCreate postCreate){
        if(postCreate.getTitle().contains("바보")){
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다");
        }
    }
    //빌더의 장점
    // - 가독성에 좋다.
    // - 값 생성에 대한 유연함
    // - 필요한 값만 받을 수 있다
    // - 객체의 불변성
}
