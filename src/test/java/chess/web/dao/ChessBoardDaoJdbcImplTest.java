package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@JdbcTest
public class ChessBoardDaoJdbcImplTest {

    private ChessBoardDao chessBoardDao;
    private RoomDao roomDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDaoJdbcImpl(jdbcTemplate);
        passwordEncoder = new BCryptPasswordEncoder();
        roomDao = new RoomDaoJdbcImpl(jdbcTemplate, passwordEncoder);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(\n"
                + "    id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    name VARCHAR(5) NOT NULL,\n"
                + "    password VARCHAR(100) NOT NULL\n"
                + ");");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS board(\n"
                + "    id INT(10) NOT NULL,\n"
                + "    position VARCHAR(2) NOT NULL,\n"
                + "    piece VARCHAR(10) NOT NULL,\n"
                + "    CONSTRAINT board_PK PRIMARY KEY (id, position),\n"
                + "    FOREIGN KEY (id) REFERENCES room(id) ON DELETE CASCADE\n"
                + ");");
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int actual = chessBoardDao.save(roomId, Position.of("A2"), new StartedPawn(Color.WHITE));

        assertThat(actual).isEqualTo(roomId);
    }

    @Test
    void deleteAll() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, Position.of("A2"), new StartedPawn(Color.WHITE));
        chessBoardDao.deleteAll();

        Map<Position, Piece> board = chessBoardDao.findAll();
        assertThat(board).isEmpty();
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, Position.of("A2"), new StartedPawn(Color.WHITE));
        chessBoardDao.deleteById(roomId);

        Map<Position, Piece> board = chessBoardDao.findById(1);
        assertThat(board).isEmpty();
    }

    @Test
    void findAll() {
        Position position = Position.of("A2");
        Piece piece = new StartedPawn(Color.WHITE);

        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, position, piece);
        Map<Position, Piece> board = chessBoardDao.findAll();

        assertThat(board.get(position)).isEqualTo(piece);
    }

    @Test
    void findById() {
        Position position = Position.of("A2");
        Piece piece = new StartedPawn(Color.WHITE);

        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, position, piece);
        Map<Position, Piece> board = chessBoardDao.findById(roomId);

        assertThat(board.get(position)).isEqualTo(piece);
    }

}
