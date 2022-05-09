package chess.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JdbcTemplate {

    private final Connection connection;

    public JdbcTemplate() {
        connection = ConnectionManager.getConection();
    }

    public int executeUpdate(String sql) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"});
            setParameters(statement);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public abstract void setParameters(PreparedStatement statement) throws SQLException;
}
