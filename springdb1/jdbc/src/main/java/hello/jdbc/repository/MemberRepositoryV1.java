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

    // 저장
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)"; // sql 쿼리 작성
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            // 커넥션 획득
            con = getConnection();

            // sql문을 작성하고 넘김
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId()); // 파라미터 바인딩(첫번째 ?에 값넣기)
            pstmt.setInt(2, member.getMoney()); // 파라미터 바인딩(두번째 ?에 값넣기)
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null); // 커넥션 닫기, 둘 중 하나가 안닫혀서 exception이 터지면 나머지 하나도 안닫힐 수 있어서 이렇게 설계
        }
    }

    // 조회
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) { // 커서때문에 처음 한번은 호출해 줘야함, false면 데이터가 없다는 의미
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" +
                        memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    // 수정
    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // 삭제
    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

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