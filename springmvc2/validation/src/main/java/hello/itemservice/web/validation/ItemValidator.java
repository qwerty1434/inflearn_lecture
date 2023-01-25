package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz); // isAssignableFrom은 자식클래스까지 커버됩니다.
    }

    @Override
    public void validate(Object target, Errors errors) { // BindingResult대신 Errors를 사용했습니다.
        Item item = (Item)target;


        // 검증 오류 결과를 BindingResult가 보관해 줍니다.

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // ModelAttribute로 넘어온 item에 itemName이 없으면
            errors.rejectValue("itemName","required"); // code를 required.item.itemName에서 첫번째 값인 required만 입력했습니다.
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.rejectValue("price","range", new Object[]{1000,1000000},null);
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.rejectValue("quantity","max", new Object[]{9999},null);
        }
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.reject("totalPriceMin",new Object[]{10000,resultPrice},null); // rejectValue가 아닌 reject
            }
        }
    }
}
