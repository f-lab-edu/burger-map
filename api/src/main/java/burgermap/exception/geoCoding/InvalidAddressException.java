package burgermap.exception.geoCoding;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException(String address) {
        super("invalid address: %s".formatted(address));
    }
}
