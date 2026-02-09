package components.booking;

import repositories.ClassBookingRepository;
import service.BookingService;
import service.MembershipService;

public class ClassBookingComponent {

    private final BookingService bookingService;

    public ClassBookingComponent(ClassBookingRepository bookingRepo,
                                 MembershipService membershipService) {
        this.bookingService = new BookingService(bookingRepo, membershipService);
    }

    public BookingService bookingService() {
        return bookingService;
    }
}
