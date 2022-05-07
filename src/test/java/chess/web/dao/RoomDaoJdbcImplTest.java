package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@JdbcTest
public class RoomDaoJdbcImplTest {

    private RoomDao roomDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        roomDao = new RoomDaoJdbcImpl(jdbcTemplate, passwordEncoder);

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room(\n"
                + "    id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    name VARCHAR(5) NOT NULL,\n"
                + "    password VARCHAR(100) NOT NULL\n"
                + ");");
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        List<RoomDto> rooms = roomDao.findAll();
        RoomDto roomDto = rooms.get(0);

        assertThat(roomDto.getRoomNumber()).isEqualTo(roomId);
    }

    @Test
    void findAll() {
        final int roomId1 = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));
        final int roomId2 = roomDao.save(RoomName.of("두번째방"), RoomPassword.of("1234"));
        List<RoomDto> rooms = roomDao.findAll();
        RoomDto roomDto1 = rooms.get(0);
        RoomDto roomDto2 = rooms.get(1);

        assertAll(() -> assertThat(roomDto1.getRoomNumber()).isEqualTo(roomId1),
                () -> assertThat(roomDto2.getRoomNumber()).isEqualTo(roomId2));
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));

        roomDao.deleteById(roomId);
        List<RoomDto> rooms = roomDao.findAll();

        assertThat(rooms).isEmpty();
    }

    @Test
    void confirmPassword() {
        final String roomPassword = "1234";
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of(roomPassword));

        final boolean result = roomDao.confirmPassword(roomId, roomPassword);

        assertThat(result).isTrue();
    }
}
