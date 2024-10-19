package burgermap.exception.member;

public class MemberNotExistException extends RuntimeException {
    public MemberNotExistException(Long member) {
        super("Member with member ID %d does not exist.".formatted(member));
    }
}
