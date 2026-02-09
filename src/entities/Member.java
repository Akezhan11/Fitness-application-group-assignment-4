package entities;

import repositories.MemberRepository;
import java.time.LocalDate;

public class Member {
    private final int id;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String gender;
    private final String email;

    private Member(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.gender = builder.gender;
    }
    public static class Builder {
        private int id;
        private String name;
        private String surname;
        private String phoneNumber;
        private String email;
        private String gender;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Member build() {
            if (name == null || surname == null) {
                throw new IllegalStateException("Name and surname cannot be null");
            }
            return new Member(this);
        }
    }



    public int getId(){
        return id;
    }


    public String getName(){
        return name;
    }


    public String getSurname(){
        return surname;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getGender(){
        return gender;
    }


    public String getEmail(){
        return email;
    }
    @Override
    public String toString(){
        return "id: " + id + ", name: " + name +
                ", surname: " + surname + ", phone: " +
                phoneNumber + ", gender: " + gender +
                ", email: " + email;
    }
}
