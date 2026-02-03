package service;

import entities.ClassBooking;
import entities.FitnessClass;
import entities.Member;
import exception.BookingAlreadyExistsException;
import exception.ClassFullException;
import exception.MembershipExpiredException;
import repositories.ClassBookingRepository;

import java.util.List;

public class BookingService {
    private final ClassBookingRepository bookingRepository;
    public BookingService(ClassBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    public void bookClass(Member member, FitnessClass fitnessClass){
        if (member == null || fitnessClass == null) {
            throw new IllegalArgumentException("Member or FitnessClass is null");
        }
        if (member.isExpired()) throw new MembershipExpiredException();
        if (bookingRepository.exists(member.getId(), fitnessClass.getId())) {
            throw new BookingAlreadyExistsException();
        }
        int bookedCount = bookingRepository.countByFitnessClassId(fitnessClass.getId());

        if (bookedCount >= fitnessClass.getMaxPlaces()){
            throw new ClassFullException();
        }
        ClassBooking booking = new ClassBooking(member, fitnessClass);

        bookingRepository.save(booking);
    }
    public void cancelBooking(Member member, FitnessClass fitnessClass) {

        if (member == null || fitnessClass == null) {
            throw new IllegalArgumentException("Member or FitnessClass is null");
        }

        ClassBooking booking = new ClassBooking(member, fitnessClass);
        bookingRepository.delete(booking);
    }
    public List<ClassBooking> getAllBookings(){
        return bookingRepository.findAll();
    }
    public List<ClassBooking> getBookingsByMember(int memberId) {
        return bookingRepository.findByMemberId(memberId);
    }
    public List<ClassBooking> getBookingsByFitness(int fitnessClassId) {
        return bookingRepository.findByClassId(fitnessClassId);
    }
}
