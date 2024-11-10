package carrot.market.member.mock;

import carrot.market.member.controller.MemberController;
import carrot.market.member.service.MemberServiceImpl;
import carrot.market.member.service.port.MemberRepository;
import carrot.market.util.holder.PasswordEncoderHolder;
import lombok.Builder;

public class TestMemberContainer {
    public final MemberController memberController;
    public final MemberRepository memberRepository;

    @Builder
    public TestMemberContainer(PasswordEncoderHolder passwordEncoderHolder) {
        this.memberRepository = new FakeMemberRepository();
        MemberServiceImpl memberService = MemberServiceImpl.builder()
                .memberRepository(memberRepository)
                .passwordEncoder(passwordEncoderHolder)
                .build();
        this.memberController = MemberController.builder().memberService(memberService).build();
    }
}
