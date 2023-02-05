package hello.jdbc.exception.translator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
public class SpringExceptionTranslatorTest {
    DataSource dataSource;
    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }
    // 스프링 예외 사용 X
    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammar";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(42122);
            int errorCode = e.getErrorCode();
            log.info("errorCode={}", errorCode);

            //org.h2.jdbc.JdbcSQLSyntaxErrorException -> h2에러
            log.info("error", e);
        }
    }

    @Test
    void exceptionTranslator() {
        String sql = "select bad grammar";
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(42122);

            // 스프링이 제공하는 변환기
            SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            // 변환기를 사용해 예외 변환
            // 해당 쿼리를 분석해 발생하는 예외를 스프링 예외로 변환한 뒤 반환해줌
            // 어떤 스프링 예외도 받을 수 있게 resultEx는 DataAccessException타입.(스프링 예외의 최상위는 DataAccessException이기 때문)
            DataAccessException resultEx = exTranslator.translate("작업 명", sql, e);
            log.info("resultEx", resultEx); // resultEx의 예외는 h2에러가 아닌 BadSqlGrammarException라는 스프링 외

            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }

}