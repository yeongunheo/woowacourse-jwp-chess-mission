package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.jdbc.JdbcTemplate;
import chess.web.jdbc.SelectJdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDaoImpl implements PlayerDao {

    @Override
    public int save(int id, Color color) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
                statement.setString(2, color.toString());
            }
        };
        final String sql = "INSERT INTO player (id, color) VALUES (?, ?)";
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
        final String sql = "DELETE FROM player";
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
        final String sql = "DELETE FROM player WHERE id = ?";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public Player findById(int id) {
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                Player player = null;
                if (resultSet.next()) {
                    Color color = Color.of(resultSet.getString("color"));
                    player = Player.of(color);
                }
                return player;
            }
        };
        final String sql = "SELECT color FROM player WHERE id = ?";

        return (Player) jdbcTemplate.executeQuery(sql);
    }
}
