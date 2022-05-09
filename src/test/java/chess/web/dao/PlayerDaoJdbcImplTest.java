package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@JdbcTest
public class PlayerDaoJdbcImplTest {

    private PlayerDao playerDao;
    private RoomDao roomDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        playerDao = new PlayerDaoJdbcImpl(jdbcTemplate);
        passwordEncoder = new BCryptPasswordEncoder();
        roomDao = new RoomDaoJdbcImpl(jdbcTemplate, passwordEncoder);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(\n"
                + "    id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    name VARCHAR(5) NOT NULL,\n"
                + "    password VARCHAR(100) NOT NULL\n"
                + ");");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS player(\n"
                + "    id INT(10) NOT NULL PRIMARY KEY,\n"
                + "    color VARCHAR (5) NOT NULL,\n"
                + "    FOREIGN KEY (id) REFERENCES room(id) ON DELETE CASCADE\n"
                + ");");
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int actual = playerDao.save(roomId, Color.BLACK);

        assertThat(actual).isEqualTo(roomId);
    }

    @Test
    void deleteAll() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        playerDao.save(roomId, Color.BLACK);
        playerDao.deleteAll();

        Player player = playerDao.findById(roomId);

        assertThat(player).isNull();
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        playerDao.save(roomId, Color.BLACK);
        playerDao.deleteById(roomId);

        Player player = playerDao.findById(roomId);

        assertThat(player).isNull();
    }

    @Test
    void findById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final Color expected = Color.BLACK;
        playerDao.save(roomId, expected);

        Player player = playerDao.findById(roomId);
        final Color actual = player.getColor();

        assertThat(actual).isEqualTo(expected);
    }
}
