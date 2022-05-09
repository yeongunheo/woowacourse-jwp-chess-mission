package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomDaoImplTest {

    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl();
    }

    @Test
    void save() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));

        List<RoomDto> rooms = roomDao.findAll();
        RoomDto result = rooms.stream()
                .filter(roomDto -> roomDto.getRoomNumber() == roomId)
                .findFirst()
                .orElseThrow();

        assertThat(result.getRoomNumber()).isEqualTo(roomId);
        roomDao.deleteById(roomId);
    }

    @Test
    void findAll() {
        final int roomId = roomDao.save(RoomName.of("테스트2"), RoomPassword.of("1234"));
        List<RoomDto> rooms = roomDao.findAll();
        RoomDto result = rooms.stream()
                .filter(roomDto -> roomDto.getRoomNumber() == roomId)
                .findFirst()
                .orElseThrow();

        assertThat(result.getRoomNumber()).isEqualTo(roomId);
        roomDao.deleteById(roomId);
    }

    @Test
    void deleteById() {
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of("1234"));

        roomDao.deleteById(roomId);
        List<RoomDto> rooms = roomDao.findAll();
        Optional<RoomDto> result = rooms.stream()
                .filter(roomDto -> roomDto.getRoomNumber() == roomId)
                .findFirst();

        assertThat(result).isEmpty();
    }

    @Test
    void confirmPassword() {
        final String roomPassword = "1234";
        final int roomId = roomDao.save(RoomName.of("첫번째방"), RoomPassword.of(roomPassword));

        final boolean result = roomDao.confirmPassword(roomId, roomPassword);

        assertThat(result).isTrue();
        roomDao.deleteById(roomId);
    }
}
