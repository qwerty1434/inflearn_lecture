package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId",required = false) Long memberId, Model model){
        // 로그인 안한 사용자
        if(memberId == null){
            return "home";
        }

        // 로그인이 적합한지 판별
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member",loginMember);
        return "loginHome"; // 로그인 한 사용자 전용 폼
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){ // request를 받음
        // 로그인 판별 - 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        // 로그인 안한 유저
        if(member == null){
            return "home";
        }


        model.addAttribute("member",member);
        return "loginHome"; // 로그인 한 사용자 전용 폼
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        // 세션 찾기, 로그인 안한 사용자가 들어올 수 있는데 그때 굳이 세션을 만들 필요는 없음
        HttpSession session = request.getSession(false);

        // 로그인 안한 유저 -> 일반 홈
        if(session == null){
            return "home";
        }

        // 로그인 판별 - 세션 관리자에 저장된 회원 정보 조회
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없다 -> 잘못된 정보로 로그인, 일반 홈
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되고 있다면 로그인홈으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome"; // 로그인 한 사용자 전용 폼
    }

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member loginMember,  Model model){

        // 로그인 안한 유저 -> 일반 홈
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되고 있다면 로그인홈으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome"; // 로그인 한 사용자 전용 폼
    }

}