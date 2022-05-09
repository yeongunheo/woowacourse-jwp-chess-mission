package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.ScoreDto;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import chess.web.service.fakedao.FakeRoomDao;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    ChessBoardDao chessBoardDao = new FakeChessBoardDao();
    PlayerDao playerDao = new FakePlayerDao();
    RoomDao roomDao = new FakeRoomDao();
    ChessGameService chessGameService = new ChessGameService(chessBoardDao, playerDao, roomDao);

    @Test
    void start() {
        final int roomId = 1;
        chessGameService.start(roomId);

        assertThat(chessBoardDao.findById(roomId)).isNotNull();
    }

    @Test
    void move() {
        final int roomId = 1;
        chessBoardDao.save(roomId, Position.of("a2"), new StartedPawn(Color.WHITE));
        playerDao.save(roomId, Color.WHITE);
        PlayResultDto playResultDto = chessGameService.move(new MoveDto(1, "a2", "a4"));

        Map<String, Piece> board = playResultDto.getBoard();
        boolean isFinished = playResultDto.getIsFinished();

        assertAll(() -> assertNotNull(isFinished),
                () -> assertNotNull(board));
    }

    @Test
    void play() {
        final int roomId = 1;
        chessBoardDao.save(roomId, Position.of("a2"), new StartedPawn(Color.WHITE));
        playerDao.save(roomId, Color.WHITE);
        PlayResultDto playResultDto = chessGameService.play(1);

        Map<String, Piece> board = playResultDto.getBoard();
        String turn = playResultDto.getTurn();

        assertAll(() -> assertNotNull(board),
                () -> assertNotNull(turn));
    }

    @Test
    void status() {
        final int roomId = 1;
        chessBoardDao.save(roomId, Position.of("a2"), new StartedPawn(Color.WHITE));
        playerDao.save(roomId, Color.WHITE);

        final ScoreDto scoreDto = chessGameService.status(roomId);

        assertThat(scoreDto.getWhiteScore()).isEqualTo(1.0);
    }

    @Test
    void createRoom() {
        final String roomName = "방이름";
        final String roomPassword = "1234";

        chessGameService.createRoom(new CreateRoomDto(roomName, roomPassword));
        List<RoomDto> rooms = roomDao.findAll();
        RoomDto room = rooms.get(0);

        assertThat(room.getRoomName()).isEqualTo(RoomName.of(roomName));
    }

    @Test
    void loadChessGames() {
        final String roomName = "방이름";
        final String roomPassword = "1234";

        chessGameService.createRoom(new CreateRoomDto(roomName, roomPassword));
        List<RoomDto> rooms = chessGameService.loadChessGames();
        RoomDto room = rooms.get(0);

        assertThat(room.getRoomName()).isEqualTo(RoomName.of(roomName));
    }

    @Test
    void deleteRoomById() {
        final String roomName = "방이름";
        final String roomPassword = "1234";

        RoomDto roomDto = chessGameService.createRoom(new CreateRoomDto(roomName, roomPassword));
        final int roomNumber = roomDto.getRoomNumber();
        chessGameService.deleteRoomById(roomNumber);

        assertThat(roomDao.findAll()).isEmpty();
    }

    @Test
    void confirmPassword() {
        final String roomName = "방이름";
        final String roomPassword = "1234";

        int roomNumber = roomDao.save(RoomName.of(roomName), RoomPassword.of(roomPassword));
        final boolean result = chessGameService.confirmPassword(roomNumber, roomPassword);

        assertThat(result).isTrue();
    }
}
