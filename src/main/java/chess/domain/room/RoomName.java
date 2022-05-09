package chess.domain.room;

import java.util.Objects;

public class RoomName {

    private final String roomName;

    private RoomName(String roomName) {
        this.roomName = roomName;
    }

    public static RoomName of(String name) {
        return new RoomName(name);
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomName)) {
            return false;
        }
        RoomName roomName1 = (RoomName) o;
        return Objects.equals(getRoomName(), roomName1.getRoomName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomName());
    }

    @Override
    public String toString() {
        return roomName;
    }
}
