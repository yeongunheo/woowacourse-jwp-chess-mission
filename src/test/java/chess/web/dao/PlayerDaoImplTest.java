package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class PlayerDaoImplTest {

    private PlayerDao playerDao;
    private RoomDao roomDao;

    @BeforeEach
    void init() {
        roomDao = new RoomDaoImpl();

        playerDao = new PlayerDaoImpl();
        playerDao.deleteAll();
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int expected = roomId;
        final int actual = playerDao.save(expected, Color.BLACK);

        assertThat(actual).isEqualTo(expected);
        roomDao.deleteById(roomId);
    }

    @Test
    void deleteAll() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int playerId = playerDao.save(roomId, Color.BLACK);
        playerDao.deleteAll();

        Player player = playerDao.findById(playerId);

        assertThat(player).isNull();
        roomDao.deleteById(roomId);
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int playerId = playerDao.save(roomId, Color.BLACK);
        playerDao.deleteById(playerId);

        Player player = playerDao.findById(playerId);

        assertThat(player).isNull();
        roomDao.deleteById(roomId);
    }

    @Test
    void findById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final Color expected = Color.BLACK;
        final int playerId = playerDao.save(roomId, expected);

        Player player = playerDao.findById(playerId);
        final Color actual = player.getColor();

        assertThat(actual).isEqualTo(expected);
        roomDao.deleteById(roomId);
    }
}
