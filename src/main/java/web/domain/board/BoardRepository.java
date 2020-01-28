package web.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import web.domain.user.User;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
