package chess.web.controller;

import chess.web.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessGameController {

    private final ChessGameService service;

    public ChessGameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/chess-games/{id}")
    public String play(@PathVariable int id) {
        return "game";
    }

    @GetMapping("/chess-games/{id}/end")
    public String end(@PathVariable int id) {
        return "finished";
    }
}
