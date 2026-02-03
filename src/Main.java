import edu.aitu.oop3.db.*;
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
        BookingService bookingService = new BookingService(bookingRepo);
        MemberService memberService = new MemberService(memberRepo);
        MembershipService membershipService = new MembershipService(membershipRepo);
        Member member = new Member();
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
                            fitnessService.getByTrainerName(trainerName, trainerSurname);
                        }
                        case 5 -> {
                            System.out.print("Enter cost of the class: ");
                            int cost = sc.nextInt();
                            List<FitnessClass> fc = fitnessService.getByCost(cost);
                            System.out.println(fc != null ? fc : "Not found");
                        }

                        case 6 -> {
                            System.out.print("Class ID to book: ");
                            int id = sc.nextInt();
                            FitnessClass fc = fitnessService.findById(id);
                            bookingService.bookClass(member, fc);
                            System.out.println("Booked successfully");
                        }

                        case 7 -> {
                            System.out.print("Class ID to cancel: ");
                            int id = sc.nextInt();
                            FitnessClass fc = fitnessService.findById(id);
                            bookingService.cancelBooking(member, fc);
                            System.out.println("Booking cancelled");
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
                        case 1->{
                            System.out.println("Enter member id: ");
                            int id = sc.nextInt();
                            System.out.println("Enter type: ");
                            String  type = sc.nextLine();
                            System.out.println("Enter days: ");
                            int days = sc.nextInt();
                            membershipService.buyMembership(id,type, days);
                        }case 2->{
                            System.out.println("Enter member id: ");
                            membershipService.checkActive(sc.nextInt());
                        }case 3->{
                            System.out.println("Enter member id: ");
                            membershipService.deactivate(sc.nextInt());
                        }case 4 -> {
                            System.out.print("Enter member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter new membership type: ");
                            String type = sc.nextLine();
                            System.out.print("Enter duration (days): ");
                            int days = sc.nextInt();
                            membershipService.update(memberId, type, days);
                        }
                        case 5->{
                            System.out.print("Enter member id: ");
                            membershipService.findByMemberId(sc.nextInt());
                        }case 6 ->{
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
        try (Connection c = DatabaseConnection.getConnection()) {
            System.out.println("CONNECTED TO SUPABASE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataBaseCreation.init();
        new Main().run();
    }
}