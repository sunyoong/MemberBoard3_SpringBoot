package com.icia.member.controller;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.icia.member.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")
// RequestMapping : 모든 요청 받음(get, post, put, delete)
// GetMapping: get요청만 받음, PostMapping:post요청만 받음 => 이런게 다 html 프로토콜(인터넷)
@RequiredArgsConstructor // final이 붙은 애들만 생성자로 만들어줌!
public class MemberController {
    private final MemberService ms;
    // 회원가입폼
    @GetMapping("save")
    public String saveForm(){
        return "member/save";
    }

    // 회원가입
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO){
        Long memberId = ms.save(memberSaveDTO);
        return "member/login";
    }




    // 로그인폼
    @GetMapping("login")
    public String loginForm(){


        return "member/login";
    }


    // 로그인
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session
                       ){
        /*@RequestParam(defaultValue = "/") String redirectURL*/
        System.out.println("MemberController.login");
//        System.out.println("redirectURL = " + redirectURL);
        boolean loginResult = ms.login(memberLoginDTO);
        if(loginResult) {
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
//            return "member/mypage"; // 인터셉트 안할경우 그냥 기존 주소값 적어주면 됨.

            String redirectURL = (String) session.getAttribute("redirectURL");
            // 인터셉터를 거쳐서 오면 redirectURL에 값이 있을 것이고 그냥 로그인을 해서 오면 redirectURL에 값이 없을 것임
            // 따라서 if else로 구분을 해줌.
            if (redirectURL != null) {
                return "redirect:" + redirectURL;
            } else {
                return "redirect:/";
            }
//            return "redirect:" + redirectURL; // 인터셉터 사용시 리턴해야 하는 주소, 사용자가 요청한 주소로 보내주기 위해
        } else{
           return "member/login";
        }
        }

        //       if(bindingResult.hasErrors()){
//           return "member/login";
//       }
//
//       if(ms.login(memberLoginDTO)) {
//           // 목록 띄울때는 redirect:/!!!! 다시 컨트롤러로 요청해줘야 되기 때문에
//        return "redirect:/member/";
//       } else{
//           return "member/login";
//       }
//
//    }

        // 회원목록
    @GetMapping
    public String findAll(Model model){
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList", memberList);
        return "member/findAll";
    }

    // 상세조회(/member/5)
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId, Model model){
        MemberDetailDTO member = ms.findById(memberId);
        model.addAttribute("member", member);
        return "member/findById";
    }

    //조회(ajax)
    // @PathVariable뒤에 ("memberId")는 @PostMapping("{memberId}")와 변수 이름이 같으므로 생략 가능.
    @PostMapping("{memberId}")
        public @ResponseBody MemberDetailDTO detail(@PathVariable long memberId){
            MemberDetailDTO member = ms.findById(memberId);
            return member;
        }




    // 삭제(delete)
    @GetMapping("delete/{memberId}")
    public String deleteById(@PathVariable("memberId") Long memberId){
        ms.deleteById(memberId);
        return "redirect:/member/";
    }


    // 삭제(ajax)
    @DeleteMapping("{memberId}")
    public ResponseEntity deleteById2(@PathVariable long memberId){
         ms.deleteById(memberId);
         /*
         // 단순 화면 출력이 아닌 데이터를 리턴하고자 할 때 사용하는 리턴방식
         ResponseEntity : 데이터 & 상태코드(200, 400, 404 : 주소가 틀렸거나 없거나(주소나 파일먼저 살펴보기), 405:badRequest(get/post), 500:자바문법오류(대충 콘솔에 뜸. ex)nullpointException..)  등)를 함께 리턴할 수 있음
         ResponseBody : 데이터를 리턴할 수 있음
         * */
        // 200 코드를 리턴(200코드 이름은 ok // 코드 이름은 httpStatus 구글링 해서 찾아보기)
         return new ResponseEntity(HttpStatus.OK);
     /*   List<MemberDetailDTO> memberList = ms.findAll();
        return memberList;*/
    }

    // 정보수정(mypage->update) 화면요청
    @GetMapping("update")
    public String updateForm(HttpSession session, Model model){
       String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
       MemberDetailDTO memberDetailDTO = ms.findByEmail(memberEmail);
        model.addAttribute("member", memberDetailDTO);
        return "member/update";

    }
    //수정처리(post)
    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO){
        Long memberId = ms.update(memberDetailDTO);
        // 수정완료 후 해당회원의 상세페이지(findById.html) 출력
        return "redirect:/member/"+memberDetailDTO.getMemberId();
    }

    // 수정처리(put)
    @PutMapping("{memberId}")
    // json으로 데이터가 전달되면 @RequestBody로 받아줘야함.
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO){
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 로그아웃(logout)
    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
