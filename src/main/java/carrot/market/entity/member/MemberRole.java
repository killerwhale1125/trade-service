package carrot.market.entity.member;

public enum MemberRole {
    CLIENT("사용자");

    private String role;

    MemberRole(String role) {
        this.role = role;
    }
}
