package bomberman;

public class LimiteDeBombasAtingidoException extends RuntimeException {
    public LimiteDeBombasAtingidoException() {
        super("Você já atingiu o limite de bombas simultâneas.");
    }
}
