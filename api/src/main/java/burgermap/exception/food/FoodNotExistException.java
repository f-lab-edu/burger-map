package burgermap.exception.food;

public class FoodNotExistException extends RuntimeException {
    public FoodNotExistException(Long foodId) {
        super("Food with food ID %d does not exist.".formatted(foodId));
    }
}
