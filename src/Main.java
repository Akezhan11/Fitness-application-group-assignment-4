import edu.aitu.oop3.db.*;
import entities.MembershipType;
import exception.BookingAlreadyExistsException;
import exception.ClassFullException;
import exception.MembershipExpiredException;
import factories.MembershipKind;
import repositories.AttendanceRepository;
import repositories.implementations.DbClassBookingRepository;
import repositories.implementations.DbFitnessClassRepository;
import repositories.implementations.DbMemberRepository;
import repositories.implementations.DbMembershipRepository;
import repositories.implementations.DbAttendanceRepository;
import entities.FitnessClass;
import entities.Member;
import entities.Attendance;
import service.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public void run() {
        DbFitnessClassRepository fitnessRepo = new DbFitnessClassRepository();
        DbClassBookingRepository bookingRepo = new DbClassBookingRepository();
        DbMemberRepository memberRepo = new DbMemberRepository();
        DbMembershipRepository membershipRepo = new DbMembershipRepository();
        AttendanceRepository attendanceRepo = new DbAttendanceRepository();

        FitnessClassService fitnessService = new FitnessClassService(fitnessRepo);
        MemberService memberService = new MemberService(memberRepo);
        AttendanceService attendanceService = new AttendanceService(attendanceRepo);

        var membershipComponent =
                new components.membership.MembershipComponent(membershipRepo, attendanceRepo);
        var bookingComponent = new components.booking.ClassBookingComponent(
                bookingRepo,
                membershipComponent.membershipService()
        );

        var statisticsComponent =
                new components.statistics.StatisticsComponent(attendanceRepo, bookingRepo);
        var notificationComponent =
                new components.notification.NotificationComponent();

        MembershipService membershipService = membershipComponent.membershipService();
        BookingService bookingService = bookingComponent.bookingService();
        StatisticsService statisticsService = statisticsComponent.statisticsService();
        components.notification.Notifier notifier = notificationComponent.notifier();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Fitness application");
            System.out.println("Menu");
            System.out.println("1: Fitness classes");
            System.out.println("2: Membership");
            System.out.println("3: Members");
            System.out.println("4: Show all Bookings");
            System.out.println("5: Attendance");
            System.out.println("6: Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 -> {
                    System.out.println("1: Show all fitness classes");
                    System.out.println("2: Find fitness class by type");
                    System.out.println("3: Find fitness class by id");
                    System.out.println("4: Find classes by trainer name");
                    System.out.println("5: Find classes by cost");
                    System.out.println("6: Book fitness class");
                    System.out.println("7: Cancel booking");
                    System.out.println("8: Show bookings by fitness class");
                    System.out.println("9: Add new fitness class");
                    System.out.println("10: Get classes cheaper than");
                    System.out.println("11: Get classes with free places");
                    System.out.println("12: Get classes sorted by cost ascending");
                    System.out.println("13: Get classes sorted by date");
                    System.out.println("14: Get classes sorted by trainer name");
                    System.out.println("15: Get classes filtered by cost");
                    System.out.println("16: Get classes sorted by type");
                    System.out.println("17: Exit");

                    System.out.print("Enter your choice: ");
                    int choice2 = sc.nextInt();
                    sc.nextLine();

                    switch (choice2) {
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

                            List<FitnessClass> classes = fitnessService.getByTrainerName(trainerName, trainerSurname);

                            if (classes.isEmpty()) {
                                System.out.println("No fitness classes found for this trainer.");
                            } else {
                                classes.forEach(System.out::println);
                            }
                        }

                        case 5 -> {
                            System.out.print("Enter cost of the class: ");
                            int cost = sc.nextInt();
                            List<FitnessClass> fc = fitnessService.getByCost(cost);
                            System.out.println(fc != null ? fc : "Not found");
                        }

                        case 6 -> {
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

                        case 7 -> {
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
                        case 8 -> {
                            System.out.print("Fitness Class ID: ");
                            int classId = sc.nextInt();
                            sc.nextLine();

                            int cnt = statisticsService.classBookingsCount(classId);
                            System.out.println("Bookings for class: " + cnt);
                        }

                        case 9 -> {
                            System.out.print("Type: ");
                            String type = sc.nextLine();

                            System.out.print("Description: ");
                            String description = sc.nextLine();

                            System.out.print("Date: ");
                            String date = sc.nextLine();

                            System.out.print("Time: ");
                            String time = sc.nextLine();

                            System.out.print("Cost: ");
                            int cost = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Trainer name: ");
                            String trainerName = sc.nextLine();

                            System.out.print("Trainer surname: ");
                            String trainerSurname = sc.nextLine();

                            System.out.print("Max places: ");
                            int maxPlaces = sc.nextInt();
                            sc.nextLine();

                            FitnessClass fc = new FitnessClass.Builder()
                                    .fitnessType(type)
                                    .fitnessDescription(description)
                                    .fitnessDate(date)
                                    .fitnessTime(time)
                                    .fitnessCost(cost)
                                    .fitnessTrainerName(trainerName)
                                    .fitnessTrainerSurname(trainerSurname)
                                    .maxPlaces(maxPlaces)
                                    .build();

                            fitnessService.addFitnessClass(fc);
                            System.out.println("Fitness class added");
                        }
                        case 10 -> {
                            System.out.print("Enter max cost: ");
                            int cost = sc.nextInt();
                            sc.nextLine();

                            List<FitnessClass> classes = fitnessService.getClassesCheaperThan(cost);

                            classes.forEach(System.out::println);
                        }
                        case 11 -> {
                            List<FitnessClass> classes = fitnessService.getClassesWithFreePlaces();

                            classes.forEach(System.out::println);
                        }
                        case 12 -> {
                            List<FitnessClass> classes = fitnessService.sortByCostAscending();

                            classes.forEach(System.out::println);
                        }
                        case 13 -> {
                            List<FitnessClass> classes = fitnessService.sortByDate();

                            classes.forEach(System.out::println);
                        }
                        case 14 -> {
                            List<FitnessClass> classes = fitnessService.sortByTrainerName();

                            classes.forEach(System.out::println);
                        }
                        case 15 -> {
                            System.out.print("Max cost: ");
                            int maxCost = sc.nextInt();
                            sc.nextLine();
                            List<FitnessClass> filtered = fitnessService.getFilteredClasses(
                                    ((utils.Filter<FitnessClass>) fc -> fc.getFitnessCost() <= maxCost)
                                            .and(fc -> fc.getMaxPlaces() > 0)
                            );
                            if (filtered.isEmpty()) System.out.println("No classes found");
                            else filtered.forEach(System.out::println);
                        }
                        case 16 -> {
                            List<FitnessClass> classes =
                                    fitnessService.getSortedClasses(
                                            Comparator.comparing(FitnessClass::getFitnessType)
                                    );

                            classes.forEach(System.out::println);
                        }


                        case 17 -> {
                            System.out.println("Closing the program");
                            return;
                        }

                        default -> System.out.println("Unknown command");
                    }
                }

                case 2 -> {
                    System.out.println("1: Buy membership");
                    System.out.println("2: Check active membership");
                    System.out.println("3: Mark attendance");
                    System.out.println("4: Exit");

                    System.out.print("Enter your choice: ");
                    int choice3 = sc.nextInt();
                    sc.nextLine();

                    switch (choice3) {
                        case 1 -> {
                            System.out.print("Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Type (MONTHLY/YEARLY/VISIT_BASED): ");
                            String kindStr = sc.nextLine().trim().toUpperCase();

                            Integer visits = null;
                            if ("VISIT_BASED".equals(kindStr)) {
                                System.out.print("Visits limit: ");
                                visits = sc.nextInt();
                                sc.nextLine();
                            }

                            membershipService.buyMembership(memberId, factories.MembershipKind.valueOf(kindStr), visits);
                            System.out.println("Membership purchased");
                            notifier.notify("Membership purchased for member " + memberId);
                        }

                        case 2 -> {
                            System.out.print("Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            attendanceService.markVisit(memberId, java.time.LocalDate.now());
                            System.out.println("Visit marked");
                        }
                        case 3 -> {
                            System.out.print("Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            membershipService.checkActive(memberId);
                            System.out.println("Membership is active");
                        }
                        case 4 -> {
                            return;
                        }

                        default -> System.out.println("Unknown choice");
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
                    System.out.println("8: Show all members by gender");
                    System.out.println("9: Show members by email domain");
                    System.out.println("10: Show members with name starting with");
                    System.out.println("11: Show members sorted by surname and name");
                    System.out.println("12: Show members filtered by gender");
                    System.out.println("13: Show members filtered by email domain");
                    System.out.println("14: Show members sorted by name");
                    System.out.println("15: Compute statistics for member");
                    System.out.println("16: Exit");

                    System.out.print("Enter your choice: ");
                    int choice4 = sc.nextInt();
                    sc.nextLine();

                    switch (choice4) {
                        case 1 -> {
                            System.out.print("Name: ");
                            String name = sc.nextLine();

                            System.out.print("Surname: ");
                            String surname = sc.nextLine();

                            System.out.print("Phone number: ");
                            String phone = sc.nextLine();

                            System.out.print("Email: ");
                            String email = sc.nextLine();

                            System.out.print("Gender: ");
                            String gender = sc.nextLine();

                            Member m = new Member.Builder()
                                    .name(name)
                                    .surname(surname)
                                    .phoneNumber(phone)
                                    .email(email)
                                    .gender(gender)
                                    .build();

                            memberService.addMember(m);
                            System.out.println("Member added successfully");
                        }


                        case 2 -> {
                            List<Member> members = memberService.getAllMembers();
                            if (members.isEmpty()) {
                                System.out.println("No members found.");
                            } else {
                                members.forEach(System.out::println);
                            }
                        }

                        case 3 -> {
                            System.out.print("Enter member id: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            Member m = memberService.findMemberByid(id);
                            System.out.println(m != null ? m : "Member not found");
                        }

                        case 4 -> {
                            System.out.print("Enter member email: ");
                            String email = sc.nextLine();
                            Member m = memberService.findMemberByEmail(email);
                            System.out.println(m != null ? m : "Member not found");
                        }

                        case 5 -> {
                            System.out.print("Enter member phone number: ");
                            String phone = sc.nextLine();
                            Member m = memberService.findMemberByPhone(phone);
                            System.out.println(m != null ? m : "Member not found");
                        }

                        case 6 -> {
                            System.out.print("Member ID: ");
                            int id = sc.nextInt();
                            sc.nextLine(); // очистка

                            System.out.print("Name: ");
                            String name = sc.nextLine();

                            System.out.print("Surname: ");
                            String surname = sc.nextLine();

                            System.out.print("Phone: ");
                            String phone = sc.nextLine();

                            System.out.print("Email: ");
                            String email = sc.nextLine();

                            System.out.print("Gender: ");
                            String gender = sc.nextLine();

                            Member m = new Member.Builder()
                                    .id(id)
                                    .name(name)
                                    .surname(surname)
                                    .phoneNumber(phone)
                                    .email(email)
                                    .gender(gender)
                                    .build();

                            memberService.updateMember(m);
                            System.out.println("Member updated successfully");
                        }
                        case 7 -> {
                            System.out.print("Member ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            bookingService.getBookingsByMember(id).forEach(System.out::println);
                        }
                        case 8 -> {
                            List<Member> members = memberService.getAllMembers();
                            members.forEach(System.out::println);
                        }

                        case 9 -> {
                            System.out.print("Enter gender: ");
                            String gender = sc.nextLine();

                            List<Member> members = memberService.getMembersByGender(gender);

                            if (members.isEmpty()) {
                                System.out.println("No members found");
                            } else {
                                members.forEach(System.out::println);
                            }
                        }

                        case 10 -> {
                            System.out.print("Enter email domain (example: gmail.com): ");
                            String domain = sc.nextLine();

                            List<Member> members = memberService.getMembersByEmailDomain(domain);

                            members.forEach(System.out::println);
                        }

                        case 11 -> {
                            System.out.print("Enter name prefix: ");
                            String prefix = sc.nextLine();

                            List<Member> members = memberService.getMembersWithNameStartsWith(prefix);

                            members.forEach(System.out::println);
                        }

                        case 12 -> {
                            List<Member> members = memberService.sortBySurnameThenName();

                            members.forEach(System.out::println);
                        }

                        case 13 -> {
                            List<Member> filtered = memberService.getFilteredMembers(
                                    ((utils.Filter<Member>) m -> "Male".equalsIgnoreCase(m.getGender()))
                                            .and(m -> m.getEmail() != null && m.getEmail().toLowerCase().endsWith("gmail.com"))
                            );

                            if (filtered.isEmpty()) System.out.println("No members found");
                            else filtered.forEach(System.out::println);
                        }

                        case 14 -> {
                            List<Member> members = memberService.getSortedMembers(
                                    Comparator.comparing(Member::getName)
                            );

                            members.forEach(System.out::println);
                        }

                        case 15 -> {
                            System.out.print("Member ID: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            int visits = statisticsService.totalVisits(memberId);
                            System.out.println("Total visits: " + visits);

                            notifier.notify("Statistics computed for member " + memberId);
                        }

                        case 16 -> {
                            return;
                        }

                        default -> System.out.println("Unknown command");
                    }
                }

                case 4 -> {
                    bookingService.getAllBookings().forEach(System.out::println);
                }

                case 5 -> {
                    System.out.println("1: Mark attendance (today)");
                    System.out.println("2: Mark attendance (custom date)");
                    System.out.println("3: Show attendance by member id");
                    System.out.println("4: Exit");

                    System.out.print("Enter your choice: ");
                    int ch = sc.nextInt();
                    sc.nextLine();

                    switch (ch) {
                        case 1 -> {
                            System.out.print("Enter member id: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            Member m = memberService.findMemberByid(memberId);
                            if (m == null) {
                                System.out.println("Member not found");
                                break;
                            }

                            LocalDate date = LocalDate.now();

                            if (attendanceRepo.exists(memberId, date)) {
                                System.out.println("Already marked for today");
                                break;
                            }

                            Attendance a = new Attendance(memberId, date);
                            attendanceRepo.save(a);

                            System.out.println("Attendance saved. id=" + a.getId());
                        }

                        case 2 -> {
                            System.out.print("Enter member id: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            Member m = memberService.findMemberByid(memberId);
                            if (m == null) {
                                System.out.println("Member not found");
                                break;
                            }

                            System.out.print("Enter date (YYYY-MM-DD): ");
                            String dateStr = sc.nextLine();
                            LocalDate date = LocalDate.parse(dateStr);

                            if (attendanceRepo.exists(memberId, date)) {
                                System.out.println("Already marked for this date");
                                break;
                            }

                            Attendance a = new Attendance(memberId, date);
                            attendanceRepo.save(a);

                            System.out.println("Attendance saved. id=" + a.getId());
                        }

                        case 3 -> {
                            System.out.print("Enter member id: ");
                            int memberId = sc.nextInt();
                            sc.nextLine();

                            List<Attendance> list = attendanceRepo.findByMemberId(memberId);
                            if (list.isEmpty()) {
                                System.out.println("No attendance records");
                            } else {
                                for (Attendance a : list) {
                                    System.out.println("id=" + a.getId() + ", memberId=" + a.getMemberId() + ", date=" + a.getVisitDate());
                                }
                            }
                        }

                        case 4 -> {
                            break;
                        }

                        default -> System.out.println("Unknown command");
                    }
                }

                case 6 -> {
                    return;
                }

                default -> System.out.println("Unknown command");
            }
        }
    }

    public static void main(String[] args) {
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
