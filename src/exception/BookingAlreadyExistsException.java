package exception;

public class BookingAlreadyExistsException extends RuntimeException{
    public BookingAlreadyExistsException(){
        super("You have booked this already");
    }
}
