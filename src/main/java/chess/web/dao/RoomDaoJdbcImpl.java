package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoJdbcImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RoomDaoJdbcImpl(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int save(RoomName name, RoomPassword password) {
        final String encodePassword = passwordEncoder.encode(password.getPassword());
        final String sql = "INSERT INTO room (name, password) VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name.getRoomName());
            ps.setString(2, encodePassword);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public List<RoomDto> findAll() {
        final String sql = "SELECT id, name FROM room";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    @Override
    public void deleteById(int id) {
        final String sql = "DELETE FROM room WHERE id = " + id;
        this.jdbcTemplate.update(sql);
    }

    @Override
    public boolean confirmPassword(int id, String password) {
        final String sql = "SELECT password FROM room WHERE id = ?";
        final String dbPassword = jdbcTemplate.queryForObject(sql, String.class, id);
        return passwordEncoder.matches(password, dbPassword);
    }

    private RowMapper<RoomDto> actorRowMapper = (resultSet, rowNum) -> {
        RoomDto roomDto = RoomDto.of(
                Integer.parseInt(resultSet.getString("id")),
                RoomName.of(resultSet.getString("name"))
        );
        return roomDto;
    };
}
