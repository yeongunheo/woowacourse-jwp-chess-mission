package chess.web.dao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dto.RoomDto;
import chess.web.jdbc.JdbcTemplate;
import chess.web.jdbc.SelectJdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RoomDaoImpl implements RoomDao{

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public int save(RoomName name, RoomPassword password) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, name.toString());
                statement.setString(2, passwordEncoder.encode(password.toString()));
            }
        };
        final String sql = "INSERT INTO room (name, password) VALUES (?, ?)";
        int id = jdbcTemplate.executeUpdate(sql);

        return id;
    }

    @Override
    public List<RoomDto> findAll() {
        List<RoomDto> roomDtos = new ArrayList<>();
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                return;
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    int id = Integer.parseInt(resultSet.getString("id"));
                    RoomName roomName = RoomName.of(resultSet.getString("name"));
                    roomDtos.add(RoomDto.of(id, roomName));
                }
                return null;
            }
        };
        final String sql = "SELECT id, name FROM room";
        jdbcTemplate.executeQuery(sql);

        return roomDtos;
    }

    @Override
    public void deleteById(int id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
            }
        };
        final String sql = "DELETE FROM room WHERE id = ?";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public boolean confirmPassword(int id, String password) {
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, String.valueOf(id));
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                String dbPassword = null;
                if (resultSet.next()) {
                    dbPassword = resultSet.getString("password");
                }
                return dbPassword;
            }
        };
        final String sql = "SELECT password FROM room WHERE id = ?";
        String actualPassword = (String) jdbcTemplate.executeQuery(sql);

        return passwordEncoder.matches(password, actualPassword);
    }
}
