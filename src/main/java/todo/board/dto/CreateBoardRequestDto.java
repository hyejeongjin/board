package todo.board.dto;

import lombok.Getter;

@Getter
public class CreateBoardRequestDto {

    private final String title;

    private final String contents;

    // 어떤 회원이 작성했는지 요청 정보에 담기 위해서.
    private final String username;

    public CreateBoardRequestDto(String title, String contents, String username) {
        this.title = title;
        this.contents = contents;
        this.username = username;
    }


}
