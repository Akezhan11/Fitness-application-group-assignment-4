package exception;

public class MembershipExpiredException extends RuntimeException{
    public MembershipExpiredException(){
        super("Membership expired");
    }
}
