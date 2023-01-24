package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("validation/v1//items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {
    // 주입받기
    private final ItemValidator itemValidator;
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨
        // 검증 오류 결과를 BindingResult가 보관해 줍니다.

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // ModelAttribute로 넘어온 item에 itemName이 없으면
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item","price","가격은 1,000 ~ 1,000,000까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 허용합니다."));
        }
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // 글로벌 에러는 FieldError가 아닌 ObjectError로 생성합니다.(매개변수가 달라짐)
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = "+ resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨
        // 검증 오류 결과를 BindingResult가 보관해 줍니다.

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // ModelAttribute로 넘어온 item에 itemName이 없으면
//            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수입니다."));
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,null,null,"상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
//            bindingResult.addError(new FieldError("item","price","가격은 1,000 ~ 1,000,000까지 허용합니다."));
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,null,null,"가격은 1,000 ~ 1,000,000까지 허용합니다."));

        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
//            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 허용합니다."));
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,null,null,"수량은 최대 9,999까지 허용합니다."));
        }
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // 글로벌 에러는 FieldError가 아닌 ObjectError로 생성합니다.(매개변수가 달라짐)
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = "+ resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨
        // bindingResult는 이미 자신의 target을 알고 있습니다.
        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={}",bindingResult.getTarget());


        // 검증 오류 결과를 BindingResult가 보관해 줍니다.

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // ModelAttribute로 넘어온 item에 itemName이 없으면
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(),false,new String[]{"required.item.itemName"},null,null));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item","price",item.getPrice(),false,new String[]{"range.item.price"},new Object[]{1000,1000000},null));

        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item","quantity",item.getQuantity(),false,new String[]{"max.item.quantity"},new Object[]{9999},null));
        }
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // 글로벌 에러는 FieldError가 아닌 ObjectError로 생성합니다.(매개변수가 달라짐)
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},new Object[]{10000,resultPrice},null));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨
        log.info("objectName={}",bindingResult.getObjectName());
        log.info("target={}",bindingResult.getTarget());


        // 검증 오류 결과를 BindingResult가 보관해 줍니다.

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // ModelAttribute로 넘어온 item에 itemName이 없으면
            bindingResult.rejectValue("itemName","required"); // code를 required.item.itemName에서 첫번째 값인 required만 입력했습니다.
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price","range", new Object[]{1000,1000000},null);
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity","max", new Object[]{9999},null);
        }
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000,resultPrice},null); // rejectValue가 아닌 reject
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }





//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨

        itemValidator.validate(item,bindingResult); // 한줄로 검증 끝

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }



    // 해당 컨트롤러가 호출될때마다 항상 먼저 호출됩니다.
    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // 기존코드에서 Model이 추가됨


        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){ // 에러가 존재하면
            log.info("errors = {}",bindingResult);
            // bindingResult는 직접 model에 담지 않아도 자동으로 함께 넘어갑니다.
            return "validation/v1/addForm";// 다시 입력폼으로 이동
        }


        // 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:validation/v1//items/{itemId}";
    }




    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:validation/v1//items/{itemId}";
    }

}

