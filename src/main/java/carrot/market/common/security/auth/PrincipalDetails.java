package carrot.market.common.security.auth;

import carrot.market.entity.member.Member;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@Slf4j
public class PrincipalDetails implements UserDetails {
    // Authentication 객체 안에 들어가는 정보는 UserDetails 타입이여야 함으로
    // UserDetails를 상속하여 구현체로 받아들임 ( 즉 같은 타입이 됨 )

    private Member member;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // OAuth 로그인 ( 유저와 attribute를 둘다 가지고 있음 )
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    //==========UserDetails 구현 메소드===========

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> String.valueOf(member.getMemberRole()));
        // GrantedAuthority 생성자를 만들어 오버라이드한 메소드로 user.getRole()를 호출하여 리턴
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    //계정 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // 아니오 ( true )
    }

    //계정이 활성화 되있는가?
    @Override
    public boolean isEnabled() {
        // 사이트에서 1년동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 했다면 User모델에
//        user.getLoginDate();
        // 현재시간 - 로그인 시간 -> 1년 초과할 시 return false
        return true;    // 아니오 ( true )
    }
}