package burgermap.exception.food;

public class FoodAttributeNotExistException extends RuntimeException {
    public FoodAttributeNotExistException(String message) {
        super(message);
    }
}
