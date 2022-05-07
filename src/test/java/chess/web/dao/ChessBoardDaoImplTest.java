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

public class ChessBoardDaoImplTest {

    private ChessBoardDao chessBoardDao;
    private RoomDao roomDao;

    @BeforeEach
    void init() {
        roomDao = new RoomDaoImpl();

        chessBoardDao = new ChessBoardDaoImpl();
        chessBoardDao.deleteAll();
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int expected = roomId;
        final int actual = chessBoardDao.save(expected, Position.of("A2"), new StartedPawn(Color.WHITE));

        assertThat(actual).isEqualTo(expected);
        roomDao.deleteById(roomId);
    }

    @Test
    void deleteAll() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, Position.of("A2"), new StartedPawn(Color.WHITE));
        chessBoardDao.deleteAll();

        Map<Position, Piece> board = chessBoardDao.findAll();
        assertThat(board).isEmpty();
        roomDao.deleteById(roomId);
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        chessBoardDao.save(roomId, Position.of("A2"), new StartedPawn(Color.WHITE));
        chessBoardDao.deleteById(1);

        Map<Position, Piece> board = chessBoardDao.findById(1);
        assertThat(board).isEmpty();
        roomDao.deleteById(roomId);
    }

    @Test
    void findAll() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        Position position = Position.of("A2");
        Piece piece = new StartedPawn(Color.WHITE);

        chessBoardDao.save(1, position, piece);
        Map<Position, Piece> board = chessBoardDao.findAll();

        assertThat(board.get(position)).isEqualTo(piece);
        roomDao.deleteById(roomId);
    }

    @Test
    void findById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        Position position = Position.of("A2");
        Piece piece = new StartedPawn(Color.WHITE);

        chessBoardDao.save(roomId, position, piece);
        Map<Position, Piece> board = chessBoardDao.findById(roomId);

        assertThat(board.get(position)).isEqualTo(piece);
        roomDao.deleteById(roomId);
    }

}
