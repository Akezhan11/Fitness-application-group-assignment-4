import edu.aitu.oop3.db.*;
import entities.MembershipType;
import exception.BookingAlreadyExistsException;
import exception.ClassFullException;
import exception.MembershipExpiredException;
import repositories.implementations.DbClassBookingRepository;
import repositories.implementations.DbFitnessClassRepository;
import repositories.implementations.DbMemberRepository;
import repositories.implementations.DbMembershipRepository;
import entities.FitnessClass;
import entities.Member;
import service.BookingService;
import service.FitnessClassService;
import service.MemberService;
import service.MembershipService;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public void run(){
        Scanner sc = new Scanner(System.in);
        DbFitnessClassRepository fitnessRepo = new DbFitnessClassRepository();
        DbClassBookingRepository bookingRepo = new DbClassBookingRepository();
        DbMemberRepository memberRepo = new DbMemberRepository();
        DbMembershipRepository membershipRepo = new DbMembershipRepository();

        FitnessClassService fitnessService = new FitnessClassService(fitnessRepo);
        MembershipService membershipService = new MembershipService(membershipRepo);
        BookingService bookingService = new BookingService(bookingRepo, membershipService);
        MemberService memberService = new MemberService(memberRepo);

        while (true){
            System.out.println("Fitness application");
            System.out.println("Menu");
            System.out.println("1: Fitness classes");
            System.out.println("2: Membership");
            System.out.println("3: Members");
            System.out.println("4: Show all Bookings");
            System.out.println("5: Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1->{
                    System.out.println("1: Show all fitness classes");
                    System.out.println("2: Find fitness class by type");
                    System.out.println("3: Find fitness class by id");
                    System.out.println("4: Find classes by trainer name");
                    System.out.println("5: Find classes by cost");
                    System.out.println("6: Book fitness class");
                    System.out.println("7: Cancel booking");
                    System.out.println("8: Show bookings by fitness class");
                    System.out.println("9: Add new fitness class");
                    System.out.println("10: Exit");

                    System.out.print("Enter your choice: ");
                    int choice2 = sc.nextInt();
                    sc.nextLine();

                    switch(choice2){
                        case 1 -> {
                            List<FitnessClass> classes = fitnessService.getAll();
                            classes.forEach(System.out::println);
                        }

                        case 2 -> {
                            System.out.print("Enter class type: ");
                            String type = sc.nextLine();
                            FitnessClass fc = fitnessService.findByType(type);
                            System.out.println(fc != null ? fc : "Not found");
                        }

                        case 3 -> {
                            System.out.print("Enter class id: ");
                            int id = sc.nextInt();
                            FitnessClass fc = fitnessService.findById(id);
                            System.out.println(fc != null ? fc : "Not found");
                        }

                        case 4 -> {
                            System.out.print("Enter trainer name: ");
                            String trainerName = sc.nextLine();

                            System.out.print("Enter trainer surname: ");
                            String trainerSurname = sc.nextLine();

                            List<FitnessClass> classes =
                                    fitnessService.getByTrainerName(trainerName, trainerSurname);

                            if (classes.isEmpty()) {
                                System.out.println("No fitness classes found for this trainer.");
                            } else {
                                for (FitnessClass fc : classes) {
                                    System.out.println(fc);
                                }
                            }
                        }

                        case 5 -> {
                            System.out.print("Enter cost of the class: ");
                            int cost = sc.nextInt();
                            List<FitnessClass> fc = fitnessService.getByCost(cost);
                            System.out.println(fc != null ? fc : "Not found");
                        }

                        case 6 -> { // Book fitness class
                            System.out.print("Enter your Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter Class ID to book: ");
                            int classId = sc.nextInt();
                            sc.nextLine();

                            try {
                                Member member = memberService.findMemberByid(memberId);
                                FitnessClass fitnessClass = fitnessService.findById(classId);

                                if (member == null) {
                                    System.out.println("Error: Member not found.");
                                    break;
                                }
                                if (fitnessClass == null) {
                                    System.out.println("Error: Fitness class not found.");
                                    break;
                                }
                                membershipService.checkActive(memberId);
                                bookingService.bookClass(member, fitnessClass);
                                System.out.println("Booking successful for class '" + fitnessClass.getFitnessType() + "'!");

                            } catch (MembershipExpiredException e) {
                                System.out.println("Error: Your membership has expired or is inactive.");
                            } catch (ClassFullException e) {
                                System.out.println("Error: This class is already full.");
                            } catch (BookingAlreadyExistsException e) {
                                System.out.println("Error: You have already booked this class.");
                            } catch (RuntimeException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }

                        case 7 -> { // Cancel booking
                            System.out.print("Enter your Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter Class ID to cancel booking: ");
                            int classId = sc.nextInt();
                            sc.nextLine();

                            try {
                                Member member = memberService.findMemberByid(memberId);
                                FitnessClass fitnessClass = fitnessService.findById(classId);

                                if (member == null) {
                                    System.out.println("Error: Member not found.");
                                    break;
                                }
                                if (fitnessClass == null) {
                                    System.out.println("Error: Fitness class not found.");
                                    break;
                                }
                                bookingService.cancelBooking(member, fitnessClass);
                                System.out.println("Booking cancelled successfully for class '" + fitnessClass.getFitnessType() + "'.");

                            } catch (RuntimeException e) {
                                System.out.println("Error cancelling booking: " + e.getMessage());
                            }
                        }

                        case  8-> {
                            System.out.print("Fitness class ID: ");
                            int id = sc.nextInt();
                            bookingService.getBookingsByFitness(id)
                                    .forEach(System.out::println);
                        }

                        case  9-> {
                            FitnessClass fc = new FitnessClass();

                            System.out.print("Type: ");
                            fc.setFitnessType(sc.nextLine());

                            System.out.print("Description: ");
                            fc.setFitnessDescription(sc.nextLine());

                            System.out.print("Date: ");
                            fc.setFitnessDate(sc.nextLine());

                            System.out.print("Time: ");
                            fc.setFitnessTime(sc.nextLine());

                            System.out.print("Cost: ");
                            fc.setFitnessCost(sc.nextInt());
                            sc.nextLine();

                            System.out.print("Trainer name: ");
                            fc.setFitnessTrainerName(sc.nextLine());

                            System.out.print("Trainer surname: ");
                            fc.setFitnessTrainerSurname(sc.nextLine());

                            System.out.print("Max places: ");
                            fc.setMaxPlaces(sc.nextInt());

                            fitnessService.addFitnessClass(fc);
                            System.out.println("Fitness class added");
                        }
                        case 10 -> {
                            System.out.println("Closing the program");
                            return;
                        }

                        default -> System.out.println("Unknown command");
                    }
                }
                case 2 -> {
                    System.out.println("1: Buy membership");
                    System.out.println("2: Check if membership is active");
                    System.out.println("3: Deactivate membership");
                    System.out.println("4: Update membership");
                    System.out.println("5: Find memberships by member id");
                    System.out.println("6: Exit");
                    System.out.print("Enter your choice: ");
                    int choice3 = sc.nextInt();
                    sc.nextLine();
                    switch(choice3){
                        case 1 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();

                            System.out.print("Enter type: ");
                            String type = sc.nextLine();

                            System.out.print("Enter days: ");
                            int days = sc.nextInt();

                            try {
                                membershipService.buyMembership(id, type, days);
                                System.out.println("Membership bought successfully!");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }

                        case 2 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            try {
                                membershipService.checkActive(id);
                                System.out.println("Membership is active!");
                            } catch (MembershipExpiredException e) {
                                System.out.println("Membership is expired or inactive.");
                            }
                        }

                        case 3 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            try {
                                membershipService.deactivate(id);
                                System.out.println("Membership deactivated successfully!");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }

                        case 4 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter new type: ");
                            String newType = sc.nextLine();

                            System.out.print("Enter days: ");
                            int days = sc.nextInt();
                            sc.nextLine();

                            try {
                                membershipService.update(id, newType, days);
                                System.out.println("Membership updated successfully!");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }

                        case 5 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            MembershipType membership = membershipService.findByMemberId(id);

                            if (membership != null) {
                                System.out.println("Membership details:");
                                System.out.println(membership);
                            }
                        }
                        case 6 ->{
                            return;
                        }default -> System.out.println("Unknown choice");
                    }
                }
                case 3 -> {
                    System.out.println("1: Add member");
                    System.out.println("2: Show all members");
                    System.out.println("3: Find member by id");
                    System.out.println("4: Find member by email");
                    System.out.println("5: Find member by phone");
                    System.out.println("6: Update member");
                    System.out.println("7: Get bookings by member id");
                    System.out.println("8: Exit");
                    System.out.print("Enter your choice: ");
                    int choice4 = sc.nextInt();
                    sc.nextLine();
                    switch(choice4){
                        case 1 -> {
                            Member m = new Member();
                            System.out.print("Name: ");
                            m.setName(sc.nextLine());
                            System.out.print("Surname: ");
                            m.setSurname(sc.nextLine());
                            System.out.print("Phone number: ");
                            m.setPhoneNumber(sc.nextLine());
                            System.out.print("Email: ");
                            m.setEmail(sc.nextLine());
                            System.out.print("Gender: ");
                            m.setGender(sc.nextLine());
                            memberService.addMember(m);
                        }case 2->{
                            List<Member> members = memberService.getAllMembers();
                            if (members.isEmpty()) {
                                System.out.println("No members found.");
                            } else {
                                members.forEach(System.out::println);
                            }
                        }case 3->{
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            Member m = memberService.findMemberByid(id);
                            System.out.println(m != null ? m : "Member not found");
                        }case 4->{
                            System.out.print("Enter member email: ");
                            String email = sc.nextLine();
                            Member m = memberService.findMemberByEmail(email);
                            System.out.println(m != null ? m : "Member not found");
                        }case 5 -> {
                            System.out.print("Enter member phone number: ");
                            String phone = sc.nextLine();
                            Member m = memberService.findMemberByPhone(phone);
                            System.out.println(m != null ? m : "Member not found");
                        }case 6 ->{
                            Member m = new Member();

                            System.out.print("Member ID: ");
                            m.setId(sc.nextInt());
                            sc.nextLine();

                            System.out.print("Name: ");
                            m.setName(sc.nextLine());

                            System.out.print("Surname: ");
                            m.setSurname(sc.nextLine());

                            System.out.print("Phone: ");
                            m.setPhoneNumber(sc.nextLine());

                            System.out.print("Email: ");
                            m.setEmail(sc.nextLine());

                            System.out.print("Gender: ");
                            m.setGender(sc.nextLine());

                            memberService.updateMember(m);
                            System.out.println("Member updated successfully");

                        }case 7->{
                            System.out.print("Member ID: ");
                            int id = sc.nextInt();
                            bookingService.getBookingsByMember(id)
                                    .forEach(System.out::println);
                        }case 8 ->{
                            return;
                        }default -> System.out.println("Unknown command");
                    }
                }case 4 -> {
                    bookingService.getAllBookings().forEach(System.out::println);
                }case 5->{
                    return;
                }default -> System.out.println("Unknown command");
            }
        }
    }
    void main(){
        DatabaseConnection db = DatabaseConnection.getInstance();
        try (Connection con = db.getConnection()) {
            System.out.println("CONNECTED TO SUPABASE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataBaseCreation.init();
        new Main().run();
    }
}