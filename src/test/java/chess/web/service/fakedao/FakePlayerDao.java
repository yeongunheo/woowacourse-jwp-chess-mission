package chess.web.service.fakedao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dao.PlayerDao;
import java.util.HashMap;
import java.util.Map;

public class FakePlayerDao implements PlayerDao {

    private Map<Integer, Color> players;

    public FakePlayerDao() {
        players = new HashMap<>();
        players.put(0, Color.WHITE);
    }

    @Override
    public int save(int id, Color color) {
        players.put(id, color);
        return id;
    }

    @Override
    public void deleteAll() {
        players.clear();
    }

    @Override
    public void deleteById(int id) {
        players.remove(id);
    }

    @Override
    public Player findById(int id) {
        return Player.of(players.get(id));
    }
}
