package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.dto.ChessBoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChessBoardDaoJdbcImpl implements ChessBoardDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChessBoardDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(int id, Position position, Piece piece) {
        final String sql = "INSERT INTO board (id, position, piece) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql, String.valueOf(id), position.toString(), piece.toString());

        return id;
    }

    @Override
    public void deleteAll() {
        final String sql = "DELETE FROM board";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(int id) {
        final String sql = "DELETE FROM board WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public Map<Position, Piece> findAll() {
        final String sql = "SELECT position, piece FROM board";
        return selectPositionAndPiece(sql);
    }

    @Override
    public Map<Position, Piece> findById(int id) {
        final String sql = "SELECT position, piece FROM board WHERE id = " + id;
        return selectPositionAndPiece(sql);
    }

    private Map<Position, Piece> selectPositionAndPiece(String sql) {
        List<ChessBoardDto> chessBoardDtos = jdbcTemplate.query(sql, actorRowMapper);
        Map<Position, Piece> board = new HashMap<>();
        for (ChessBoardDto boardDto : chessBoardDtos) {
            String position = boardDto.getPosition();
            String piece = boardDto.getPiece();
            board.put(Position.of(position), PieceFactory.of(position, piece));
        }
        return board;
    }

    private RowMapper<ChessBoardDto> actorRowMapper = (resultSet, rowNum) -> {
        ChessBoardDto board = new ChessBoardDto(
                resultSet.getString("position"),
                resultSet.getString("piece")
        );
        return board;
    };
}
