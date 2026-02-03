package entities;

import repositories.MemberRepository;
import java.time.LocalDate;

public class Member {
    private int id;
    private static int idGen;
    private String name;
    private String surname;
    private String phoneNumber;
    private String gender;
    private String email;
    private MembershipType membershipType;
    private LocalDate membershipEndDate;
    public Member(){}

    public Member(String name, String surname, String phoneNumber, String gender, String email) {
        this.id = idGen++;
        setName(name);
        setSurname(surname);
        setPhoneNumber(phoneNumber);
        setGender(gender);
        setEmail(email);
    }

    public boolean isExpired() {
        return membershipEndDate == null || membershipEndDate.isBefore(LocalDate.now());
    }


    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        if(name == null||name.length() <= 1){
            throw new IllegalArgumentException("Name must be at least 2 characters long!");
        }
        this.name = name;
    }


    public String getSurname(){
        return surname;
    }
    public void setSurname(String surname){
        if(surname == null||surname.length() <= 1){
            throw new IllegalArgumentException("Surname must be at least 2 characters long!");
        }
        this.surname = surname;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }


    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    @Override
    public String toString(){
        return "id: " + id + ", name: " + name +
                ", surname: " + surname + ", phone: " +
                phoneNumber + ", gender: " + gender +
                ", email: " + email;
    }
}
