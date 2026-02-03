package repositories;
import java.util.List;
import entities.ClassBooking;

public interface ClassBookingRepository {
    void save(ClassBooking classBooking);
    boolean exists(int memberId, int fitnessClassId);
    int countByFitnessClassId(int fitnessClassId);
    List<ClassBooking> findAll();
    void delete(ClassBooking classBooking);
    List<ClassBooking> findByClassId(int fitnessClassId);
    List<ClassBooking> findByMemberId(int memberId);
}