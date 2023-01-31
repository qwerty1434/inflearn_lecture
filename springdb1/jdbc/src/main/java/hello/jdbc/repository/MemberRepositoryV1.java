package hello.jdbc.repository;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {

    // DataSource 주입
    private final DataSource dataSource;
    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //save()...
    //findById()...
    //update()....
    //delete()....

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection(); // DBConnectionUtil.getConnection()에서 변경
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}