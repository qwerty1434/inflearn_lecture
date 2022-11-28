package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){ // model: 데이터를 담아 view에 넘기는 역할을 합니다.
        model.addAttribute("data","hello!!!");
        return "helloworld"; // return값으로 resources/templates에 있는 뷰 파일의 이름을 적어줍니다.(뒤에 .html은 자동으로 붙습니다)
    }
}
