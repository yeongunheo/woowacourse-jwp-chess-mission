package chess.web.controller;

import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.dto.PasswordDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.ScoreDto;
import chess.web.service.ChessGameService;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chess-games")
public class ChessGameRestController {

    private final ChessGameService service;

    public ChessGameRestController(ChessGameService service) {
        this.service = service;
    }

    @PutMapping()
    public ResponseEntity<PlayResultDto> move(@RequestBody MoveDto moveDto) {
        final PlayResultDto playResultDto = service.move(moveDto);
        return ResponseEntity.ok(playResultDto);
    }

    @PostMapping()
    public ResponseEntity<RoomDto> createRoom(@RequestBody CreateRoomDto createRoomDto) {
        final RoomDto roomDto = service.createRoom(createRoomDto);
        return ResponseEntity.created(URI.create("/chess-games/" + roomDto.getRoomNumber())).body(roomDto);
    }

    @GetMapping()
    public ResponseEntity<List<RoomDto>> loadRoom() {
        final List<RoomDto> roomDtos = service.loadChessGames();
        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<PlayResultDto> loadChessBoard(@PathVariable int id) {
        final PlayResultDto playResultDto = service.play(id);
        return ResponseEntity.ok(playResultDto);
    }

    @PutMapping("/{id}/initialization")
    public ResponseEntity initialize(@PathVariable int id) {
        service.start(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable int id) {
        final ScoreDto scoreDto = service.status(id);
        return ResponseEntity.ok(scoreDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoom(@PathVariable int id) {
        service.deleteRoomById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/password")
    public ResponseEntity confirmPassword(@PathVariable int id, @RequestBody PasswordDto password) {
        boolean result = service.confirmPassword(id, password.getPassword());
        if (!result) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
