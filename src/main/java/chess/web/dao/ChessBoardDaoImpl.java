package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.jdbc.JdbcTemplate;
import chess.web.jdbc.SelectJdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardDaoImpl implements ChessBoardDao {

    @Override
    public int save(int id, Position position, Piece piece) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
                statement.setString(2, position.toString());
                statement.setString(3, piece.toString());
            }
        };
        final String sql = "INSERT INTO board (id, position, piece) VALUES (?, ?, ?)";
        jdbcTemplate.executeUpdate(sql);

        return id;
    }

    @Override
    public void deleteAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) {
                return;
            }
        };
        final String sql = "DELETE FROM board";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public void deleteById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
            }
        };
        final String sql = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public Map<Position, Piece> findAll() {
        final Map<Position, Piece> board = new HashMap<>();
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) {
                return;
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String position = resultSet.getString("position");
                    String piece = resultSet.getString("piece");
                    board.put(Position.of(resultSet.getString("position")), PieceFactory.of(position, piece));
                }
                return null;
            }
        };
        final String sql = "SELECT position, piece FROM board";
        jdbcTemplate.executeQuery(sql);

        return board;
    }

    @Override
    public Map<Position, Piece> findById(int id) {
        final Map<Position, Piece> board = new HashMap<>();
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String position = resultSet.getString("position");
                    String piece = resultSet.getString("piece");
                    board.put(Position.of(resultSet.getString("position")), PieceFactory.of(position, piece));
                }
                return null;
            }
        };
        final String sql = "SELECT position, piece FROM board WHERE id = ?";
        jdbcTemplate.executeQuery(sql);

        return board;
    }
}
