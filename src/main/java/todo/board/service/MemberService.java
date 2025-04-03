package todo.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import todo.board.dto.MemberResponseDto;
import todo.board.dto.SignUpResponseDto;
import todo.board.entity.Member;
import todo.board.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public SignUpResponseDto signUp(String username, String password, Integer age) {

        Member member = new Member(username, password, age);

        Member savedMember = memberRepository.save(member);

        return new SignUpResponseDto(savedMember.getId(), savedMember.getUsername(), savedMember.getAge());
    }

    public MemberResponseDto findById(Long id) {

        // null을 안전하게 다루기 위함.
        Optional<Member> optionalMember = memberRepository.findById(id);

        if(optionalMember.isEmpty()){
            // NOT FOUND는 클라이언트가 잘못 요청한 것이지만
            // 없다고 해서 정말 잘못된 요청인지도 고려해보아야함.
            // 200대 응답을 해주어도 괜찮음. ex) 204 - 요청은 성공했으나 NO CONTENT
            // 상태 코드에 통일성을 주어야함. 204로 할 것이면 204로 계속 설정 or 404로 계속 설정.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id : " + id);
        }

        Member findMember = optionalMember.get();

        return new MemberResponseDto(findMember.getUsername(), findMember.getAge());
    }

    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {

        // 이렇게 사용하지 않으면 optionalMember를 반복적으로 추가해야함.
        Member findMember = memberRepository.findByIdOrElseThrow(id);

        // 비밀번호가 일치하지 않을 경우
        if(!findMember.getPassword().equals(oldPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        findMember.updatePassword(newPassword);


    }
}
