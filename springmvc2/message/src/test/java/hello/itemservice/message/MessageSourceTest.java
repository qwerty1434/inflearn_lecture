package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource; // 스프링 부트가 자동으로 MessageSource를 등록해줬기 때문에 사용할 수 있습니다.

    @Test
    void helloMessage(){
        String result = messageSource.getMessage("hello",null,null);
        System.out.println("result = " + result);
        // locale을 설정하지 않았기 때문에 기본값인 messages.properties에 있는 hello인 안녕이 나옵니다.
        Assertions.assertThat(result).isEqualTo("안녕"); // 지금은 한글 인코딩이 안되서 깨져있어서 에러뜸
    }

    @Test
    void notFoundMessageCode(){
        // properties 파일에 해당 변수(code)가 없으면 NoSuchMessageException라는 에러가 발생합니다.
        Assertions.assertThatThrownBy(() -> messageSource.getMessage("noCode",null,null)).isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage(){
        // 메시지가 존재하지 않을 때 사용할 기본메시지를 인자로 넘겨줄 수 있습니다.
        String result = messageSource.getMessage("noCode",null,"기본 메시지",null);
        Assertions.assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage(){
        // 매개변수를 넘길수 있습니다. -> 이때 Object배열 형태로 인자를 넘겨야 합니다.
        String result = messageSource.getMessage("hello.name",new Object[]{"Spring"},null);
        Assertions.assertThat(result).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang(){
        // locale을 선택하지 않으면 기본인 messages.properties의 값이 사용됩니다.
        Assertions.assertThat(messageSource.getMessage("hello",null,null)).isEqualTo("안녕");
        // messages_kr이라는 파일이 존재하지 않기 때문에 default인 messages.properties에 있는 값으로 실행됩니다.
        Assertions.assertThat(messageSource.getMessage("hello",null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang(){
        // locale이 ENGLISH이기 때문에 messages_en.properties의 값으로 실행됩니다.
        Assertions.assertThat(messageSource.getMessage("hello",null,Locale.ENGLISH)).isEqualTo("hello");
    }


}
