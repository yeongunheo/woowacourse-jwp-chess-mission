package chess.web.service.fakedao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dao.RoomDao;
import chess.web.dto.RoomDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeRoomDao implements RoomDao {

    Map<Integer, Map<RoomName, RoomPassword>> rooms = new HashMap<>();

    @Override
    public int save(RoomName name, RoomPassword password) {
        Map<RoomName, RoomPassword> roomNameAndPassword = new HashMap<>();
        roomNameAndPassword.put(name, password);
        rooms.put(1, roomNameAndPassword);
        return 1;
    }

    @Override
    public List<RoomDto> findAll() {
        List<RoomDto> roomDtos = new ArrayList<>();
        for (int roomNumber : rooms.keySet()) {
            Set<RoomName> roomNames = rooms.get(roomNumber).keySet();
            for (RoomName roomName : roomNames) {
                roomDtos.add(RoomDto.of(roomNumber, roomName));
            }
        }
        return roomDtos;
    }

    @Override
    public void deleteById(int id) {
        rooms.remove(id);
    }

    @Override
    public boolean confirmPassword(int id, String password) {
        Map<RoomName, RoomPassword> roomNameAndPassword = rooms.get(id);
        for (RoomName roomName : roomNameAndPassword.keySet()) {
            RoomPassword roomPassword = roomNameAndPassword.get(roomName);
            return roomPassword.getPassword().equals(password);
        }
        return false;
    }
}
