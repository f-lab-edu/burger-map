package burgermap.exception.store;

public class NotOwnerMemberException extends RuntimeException{
    public NotOwnerMemberException(String message) {
        super(message);
    }
}
