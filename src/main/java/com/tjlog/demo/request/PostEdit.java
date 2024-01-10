package com.tjlog.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
//코드가 postCreate와 비슷해도 기능이 다르므로 클래스를 분리해야한다 나중에 수정할때 코드가 다를수 있으므로
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해라!")
    private String title;

    @NotBlank(message = "내용을 입력해라!")
    private String content;

    @Builder //테스트용 생성자
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
