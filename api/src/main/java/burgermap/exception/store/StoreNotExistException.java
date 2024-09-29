package burgermap.exception.store;

public class StoreNotExistException extends RuntimeException {
    public StoreNotExistException(Long storeId) {
        super("Store with store ID %d does not exist.".formatted(storeId));
    }
}
