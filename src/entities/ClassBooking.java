package entities;

public class ClassBooking{
    private Member member;
    private FitnessClass fitnessClass;

    public ClassBooking(Member member, FitnessClass fitnessClass){
        setMember(member);
        setFitnessClass(fitnessClass);
    }
    public Member getMember(){
        return member;
    }
    public void  setMember(Member member){
        this.member = member;
    }
    public FitnessClass getFitnessClass() {
        return fitnessClass;
    }
    public void setFitnessClass(FitnessClass fitnessClass){
        this.fitnessClass = fitnessClass;
    }
}
