package exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(int id) {
        super("Member with id:" + id +" not found");
    }
    public MemberNotFoundException(String email) {
        super("Member with :" + email +" not found");
    }
}
