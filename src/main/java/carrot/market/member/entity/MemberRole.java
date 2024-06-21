package carrot.market.member.entity;

public enum MemberRole {
    CLIENT("사용자");

    private String role;

    MemberRole(String role) {
        this.role = role;
    }
}
