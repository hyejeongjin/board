package todo.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import todo.board.dto.BoardResponseDto;
import todo.board.dto.BoardWithAgeResponseDto;
import todo.board.entity.Board;
import todo.board.entity.Member;
import todo.board.repository.BoardRepository;
import todo.board.repository.MemberRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //@Transactional을 안쓰는 이유 : SimpleJpaRepository 안에 이미 @Transactional이 선언되어 있음.
    public BoardResponseDto save(String title, String contents, String username) {

       Member findMember = memberRepository.findMemberByUsernameOrElseThrow(username);

        Board board = new Board(title, contents);
        board.setMember(findMember);

        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getContents());
    }

    public List<BoardResponseDto> findAll(){

        return boardRepository.findAll()
                .stream()
                .map(BoardResponseDto::toDto)
                .toList();
    }


    public BoardWithAgeResponseDto findById(Long id) {

        Board findBoard = boardRepository.findByIdOrElseThrow(id);
        Member writer = findBoard.getMember();

        return new BoardWithAgeResponseDto(findBoard.getTitle(), findBoard.getContents(), writer.getAge());
    }

    public void delete(Long id) {

        Board findBoard = boardRepository.findByIdOrElseThrow(id);

        boardRepository.delete(findBoard);
    }
}
