package carrot.market.util.jwt;

import lombok.Builder;

/**
 * 멤버변수는 private final로 선언된다.
 * 필드별 getter가 자동으로 생성된다.
 * equals, hashcode, toString이 자동으로 생성된다.
 * 기본생성자는 제공하지 않으므로 필요한 경우 직접 생성해야 한다.
 * final 클래스이므로 다른 클래스를 상속하거나/상속시킬 수 없다.
 * private final fields 이외의 인스턴스 필드를 선언할 수 없다.
 */
@Builder
public record JwtToken (String grantType, String accessToken){
}
