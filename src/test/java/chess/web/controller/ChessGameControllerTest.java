package chess.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dao.RoomDao;
import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.service.ChessGameService;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import chess.web.service.fakedao.FakeRoomDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ChessGameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    ChessBoardDao chessBoardDao = new FakeChessBoardDao();
    PlayerDao playerDao = new FakePlayerDao();
    RoomDao roomDao = new FakeRoomDao();
    ChessGameService chessGameService = new ChessGameService(chessBoardDao, playerDao, roomDao);

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new ChessGameController(chessGameService),
                new ChessGameRestController(chessGameService))
                .build();
    }

    @Test
    void root() throws Exception {
        this.mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void play() throws Exception {
        final int boardId = 1;
        this.mockMvc.perform(get("/chess-games/" + boardId).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void end() throws Exception {
        final int boardId = 1;
        this.mockMvc.perform(get("/chess-games/" + boardId + "/end").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void move() throws Exception {
        final int boardId = 1;
        playerDao.save(boardId, Color.WHITE);

        chessBoardDao.save(1, Position.of("a2"), new StartedPawn(Color.WHITE));
        String content = objectMapper.writeValueAsString(new MoveDto(1, "a2", "a4"));

        this.mockMvc.perform(put("/chess-games/")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        playerDao.deleteAll();
    }

    @Test
    void createRoom() throws Exception {
        String content = objectMapper.writeValueAsString(new CreateRoomDto("첫번째게임", "1234"));

        this.mockMvc.perform(post("/chess-games")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void loadRoom() throws Exception {
        this.mockMvc.perform(get("/chess-games").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void initialize() throws Exception {
        final int boardId = 1;
        this.mockMvc.perform(put("/chess-games/" + boardId + "/initialization").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
