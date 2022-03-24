package com.icia.member.service;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.entity.MemberEntity;
import com.icia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository mr;
    @Override
    public Long save(MemberSaveDTO memberSaveDTO) {
        //JapRepository는 무조건 Entity만 받음.

        // MemberSaveDTO -> MemberEntity
        MemberEntity memberEntity = MemberEntity.saveMember(memberSaveDTO);
//        Long memberId = mr.save(memberEntity).getId();
//        return memberId;
        return mr.save(memberEntity).getId();
    }


    @Override
    public boolean login(MemberLoginDTO memberLoginDTO) {
        // 로그인 검증작업 필요함. db랑 입력한 정보가 맞는지.
        // mr.findBy~Email(~) 이 갖고있는 email을 넘겨서 memberEntity에 넣어줌.
        MemberEntity memberEntity = mr.findByMemberEmail(memberLoginDTO.getMemberEmail());
        // entity 조회 결과가 null 이 아니면
        if(memberEntity != null){
            // 위에서 아이디는 있는걸 확인했으니 여기서부터는 비밀번호 검증.
            if(memberLoginDTO.getMemberPassword().equals(memberEntity.getMemberPassword())){
                //memberEntity에 담겨있는 패스워드 == memberLoginDTO에 담겨있는 패스워드 => 확인
                return true;
            } else {
                return false;
            }
        } else{
        return false;
        }
    }

    // 회원목록조회
    @Override
    public List<MemberDetailDTO> findAll() {
        List<MemberEntity> memberEntityList = mr.findAll();
        List<MemberDetailDTO> memberList = new ArrayList<>();
        for(MemberEntity m : memberEntityList){
            memberList.add(MemberDetailDTO.toMemberDetailDTO(m));
        }
        System.out.println("serviceImple. detail리스트 : " + memberList);
        return memberList;
    }


    // 상세조회
    @Override
    public MemberDetailDTO findById(Long memberId) {
        // Entity -> DTO

      MemberEntity member =  mr.findById(memberId).get();
      MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(member);

        return memberDetailDTO;
    }

    // 삭제
    @Override
    public void deleteById(Long memberId) {
        mr.deleteById(memberId);
    }

    // 정보수정 화면요청을 위한 메서드
    @Override
    public MemberDetailDTO findByEmail(String memberEmail) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberEmail);
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        return memberDetailDTO;
    }

    // 수정처리
    @Override
    public Long update(MemberDetailDTO memberDetailDTO) {
        // update처리시 save메서드 호출
        // MemberDetailDTO -> MemberEntity
        // mr.save(엔티티객체);
        MemberEntity memberEntity = MemberEntity.toUpdateMember(memberDetailDTO);
       /* MemberEntity memberUpdate = mr.save(memberEntity);
        return memberUpdate.getId();
        아래 코드랑 같은 코드임.*/
        Long memberId = mr.save(memberEntity).getId();
        return memberId;
    }


}

