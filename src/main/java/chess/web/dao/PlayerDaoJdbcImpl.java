package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerDaoJdbcImpl implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(int id, Color color) {
        final String sql = "INSERT INTO player (id, color) VALUES (?, ?)";
        this.jdbcTemplate.update(sql, id, color.toString());
        return id;
    }

    @Override
    public void deleteAll() {
        final String sql = "DELETE FROM player";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(int id) {
        final String sql = "DELETE FROM player WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public Player findById(int id) {
        final String sql = "SELECT color FROM player WHERE id = ?";
        try {
            String color = jdbcTemplate.queryForObject(sql, String.class, id);
            return Player.of(Color.of(color));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
