package exception;

public class ClassFullException extends RuntimeException{
    public ClassFullException(){
        super("This class is already full, try next class");
    }
}
