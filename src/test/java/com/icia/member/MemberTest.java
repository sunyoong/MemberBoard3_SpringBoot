package com.icia.member;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberMapperDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.repository.MemberMapperRepository;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService ms;

    // 멤버맵퍼리파지토리 주입.
    @Autowired
    private MemberMapperRepository mmr;

    @Test
    @DisplayName("회원데이터생성")
    // 롤백은 하면 안됨! 디비에 넣고 뭔가를 할 거기 때문.

    public void newMembers() {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            ms.save(new MemberSaveDTO("email" + i, "password" + i, "name" + i));

        });

    }





    /*
    회원삭제 테스트코드를 만들어봅시다.
    회원삭제 시나리오를 작성해보고 코드도 짜보도록...

    1. 테스트 회원데이터 생성
    2. 회원삭제 ms.delete
     */

    @Test
    @Transactional
    @Rollback
    @DisplayName("회원삭제 테스트")
    public void MemberDeleteTest() {
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("삭제용회원이메일11", "삭제용회원비밀번호11", "삭제용회원이름11");
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(ms.findById(memberId));

        // 삭제진행
        ms.deleteById(memberId);
        /*System.out.println(ms.findById(memberId));*/

        // 삭제한 회원의 id로 조회를 시도했을 때 null이어야 테스트 통과
        // NoSuchElementException은 무시하고 테스트를 수행
        assertThrows(NoSuchElementException.class, () ->{
            assertThat(ms.findById(memberId)).isNull(); // 삭제회원 id 조회결과가 null이면 테스트 통과


        });

    }


    // 정보수정 테스트코드
    @Test
    @Transactional
    @Rollback
    @DisplayName("회원정보 수정처리")
    public void memberUpdate(){

        /*
            1. 신규회원등록
            2. 신규등록한 회원에 대한 이름 수정
            3. 신규등록시 사용한 이름과 DB에 저장된 이름이 일치하는지 판단
            4. 일치하지 않아야 테스트 통과
         */

        String memberEmail = "수정회원1";
        String memberPassword = "수정회원비번1";
        String memberName ="수정회원이름1";
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(memberEmail, memberPassword, memberName);
        Long saveMemberId = ms.save(memberSaveDTO);
        //db에 저장된 기존회원의 이름 가져와서 String saveMemberName 변수에 넣기
        String saveMemberName  = ms.findById(saveMemberId).getMemberName();

        String updateName = "수정용이름1";

        // 1. MemberDetailDTO에 새로운 내용 저장?
        // 2. 그 뒤에 updateMember객체를 담은 내용을 db에 가져가서 저장한ㄷ
        // 3. 다시 findById로 저장된 회원 아이디로 회원의 이름(변경된 이름) 을 조회해서 updateMemberName 변수에 담아준다.
        MemberDetailDTO updateMember = new MemberDetailDTO(saveMemberId, memberEmail, memberPassword, updateName);
        ms.update(updateMember);
        String updateMemberName = ms.findById(saveMemberId).getMemberName();


        // saveMemberName과 updateMemberName이 같은지 확인
        assertThat(saveMemberName).isEqualTo(updateMemberName);

       /* // 회원정보 저장
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("수정테스트 회원이메일1", "수정테스트 회원비번1", "수정테스트 회원이름1");

        // 넘버 불러오기
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(memberId);
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        // 정보수정처리
        Long updateMemberId = ms.update(memberDetailDTO);
        MemberDetailDTO updateMemberDTO = new MemberDetailDTO();
        updateMemberDTO.setMemberId(updateMemberId);


        // 기존의 저장된 이름과 수정된 이름이 같은지 확인
        // memberDetailDTO. 이름 = 기존 회원이름
        //

        // 결과
        assertThat(memberDetailDTO.getMemberName()).isEqualTo(updateMemberDTO.getMemberName());*/
    }


    @Test
    @DisplayName("mybatis 목록 출력 테스트")
    public void memberListTest(){
        List<MemberMapperDTO> memberList = mmr.memberList();
        for(MemberMapperDTO m: memberList){
            System.out.println("m.toString() : " + m.toString());
        }

        List<MemberMapperDTO> memberList2 = mmr.memberList();
        for(MemberMapperDTO m: memberList2){
            System.out.println("m.toString() : " + m.toString());
        }
    }


    // 회원가입테스트(mapper)
    @Test
    @DisplayName("mybatis 회원가입 테스트")
    public void memberSaveTest(){
        MemberMapperDTO memberMapperDTO = new MemberMapperDTO("회원이메일11", "회원비번11", "회원이름11");
        mmr.save(memberMapperDTO);

        MemberMapperDTO memberMapperDTO2 = new MemberMapperDTO("회원이메일22", "회원비번22", "회원이름22");
        mmr.save(memberMapperDTO2);




   }


}