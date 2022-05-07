package chess.web.service.fakedao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import java.util.HashMap;
import java.util.Map;

public class FakeChessBoardDao implements ChessBoardDao {

    private Map<Integer, Map<Position, Piece>> board;

    public FakeChessBoardDao() {
        board = new HashMap<>();
    }

    @Override
    public int save(int id, Position position, Piece piece) {
        Map<Position, Piece> positionAndPiece = new HashMap<>();
        positionAndPiece.put(position, piece);

        board.put(id, positionAndPiece);

        return id;
    }

    @Override
    public void deleteAll() {
        board.clear();
    }

    @Override
    public void deleteById(int id) {
        board.remove(id);
    }

    @Override
    public Map<Position, Piece> findAll() {
        Map<Position, Piece> result = new HashMap<>();
        for (int id : board.keySet()) {
            Map<Position, Piece> positionAndPieces = board.get(id);
            for (Position position : positionAndPieces.keySet()) {
                result.put(position, positionAndPieces.get(position));
            }
        }

        return result;
    }

    @Override
    public Map<Position, Piece> findById(int id) {
        return board.get(id);
    }
}
