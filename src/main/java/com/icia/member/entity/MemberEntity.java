package com.icia.member.entity;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberSaveDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(length = 50, unique = true)
    private String memberEmail;

    @Column(length = 20)
    private String memberPassword;

    @Column
    private String memberName;


    // MemberSaveDTO -> MemberEntity 객체로 변환하기 위한 메서드
    public static MemberEntity saveMember(MemberSaveDTO memberSaveDTO) {
        MemberEntity memberEntity = new MemberEntity();
        // 아래 문장은   memberEntity.setMemberEmail(memberSaveDTO.getMemberEmail()); 이랑 똑같은 문장임.
//        String memberEmail = memberSaveDTO.getMemberEmail();
//        memberEntity.setMemberEmail(memberEmail);

        // 코드는 괄호 안에 있는 내용부터 보기!
        // memberSaveDTO.getMemberEmail()을 해서  memberEntity.setMemberEmail 한다.
        memberEntity.setMemberEmail(memberSaveDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberSaveDTO.getMemberPassword());
        memberEntity.setMemberName(memberSaveDTO.getMemberName());
        return memberEntity;
    }

    // DetailDTO를 받아서 엔티티로 변환!
    // MemberDetailDTO -> MemberEntity 객체로 변환하기 위한 메서드
    public static MemberEntity toUpdateMember(MemberDetailDTO memberDetailDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDetailDTO.getMemberId());
        memberEntity.setMemberEmail(memberDetailDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDetailDTO.getMemberPassword());
        memberEntity.setMemberName(memberDetailDTO.getMemberName());
        return memberEntity;
    }

}
