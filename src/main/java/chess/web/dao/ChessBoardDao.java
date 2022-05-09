package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import java.util.Map;

public interface ChessBoardDao {

    int save(int id, Position position, Piece piece);

    void deleteAll();

    void deleteById(int id);

    Map<Position, Piece> findAll();

    Map<Position, Piece> findById(int id);
}
