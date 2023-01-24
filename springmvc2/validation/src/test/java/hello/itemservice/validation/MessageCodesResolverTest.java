package hello.itemservice.validation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required","item");
        // required를 errorCode를 찾으면 required.item, 그리고 required가 나옵니다 (세부적인 값이 먼저 나옴)
        for (String messageCode: messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        Assertions.assertThat(messageCodes).containsExactly("required.item","required");
    }

    @Test
    void messageCodesResolverField(){
        // field명과 fieldType을 넣을 수 있습니다.
        String[] messageCodes = codesResolver.resolveMessageCodes("required","item","itemName",String.class);
        // 가장 세부적인게 먼저 나옵니다.
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        // 대략 이런 기능을 해주는 겁니다.
        // bindingResult.rejectValue("itemName","required");
        // new FieldError("item","itemName",null,false,messageCodes,null,null);

        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required");
    }
}
