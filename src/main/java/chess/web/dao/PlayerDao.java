package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;

public interface PlayerDao {

    int save(int id, Color color);

    void deleteAll();

    void deleteById(int id);

    Player findById(int id);
}
