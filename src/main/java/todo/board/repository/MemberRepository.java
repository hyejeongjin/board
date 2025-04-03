package todo.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import todo.board.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUsername(String username);

    default Member findMemberByUsernameOrElseThrow(String username){
        return findMemberByUsername(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists username : " + username));
    }


    default Member findByIdOrElseThrow(Long id){
        // findById를 default 메서드 내부에서 사용하면 repository를 바로 optional이 아니라
        // member editing 자체를 반환 가능
        return findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id : " + id));
    }
}
