package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.ClassBooking;
import entities.FitnessClass;
import entities.Member;
import repositories.ClassBookingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbClassBookingRepository implements ClassBookingRepository {
    private final DatabaseConnection db = DatabaseConnection.getInstance();
    @Override
    public void save(ClassBooking classBooking) {
        String sql = "INSERT INTO bookings(member_id, class_id) VALUES (?, ?);";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classBooking.getMember().getId());
            ps.setInt(2, classBooking.getFitnessClass().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving booking", e);
        }
    }

    @Override
    public boolean exists(int memberId, int fitnessClassId) {
        String sql = "SELECT 1 FROM bookings WHERE member_id = ? AND class_id = ?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, fitnessClassId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if booking exists", e);
        }
    }

    @Override
    public int countByFitnessClassId(int fitnessClassId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE class_id = ?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, fitnessClassId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error counting bookings for fitness class", e);
        }
    }

    @Override
    public void delete(ClassBooking classBooking) {
        String sql = "DELETE FROM bookings WHERE member_id = ? AND class_id = ?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, classBooking.getMember().getId());
            ps.setInt(2, classBooking.getFitnessClass().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking", e);
        }
    }

    @Override
    public List<ClassBooking> findByClassId(int fitnessClassId) {
        List<ClassBooking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.id AS booking_id,
                   m.id AS member_id, m.name AS member_name,
                   f.id AS class_id, f.type AS class_type, f.max_places AS max_places
            FROM bookings b
            JOIN members m ON b.member_id = m.id
            JOIN fitness f ON b.class_id = f.id
            WHERE f.id = ?;
            """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fitnessClassId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Member member = new Member.Builder()
                        .id(rs.getInt("member_id"))
                        .name(rs.getString("member_name"))
                        .build();

                FitnessClass fitnessClass = new FitnessClass.Builder()
                        .id(rs.getInt("class_id"))
                        .fitnessType(rs.getString("class_type"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();

                bookings.add(new ClassBooking(member, fitnessClass));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bookings by fitness class id", e);
        }
        return bookings;
    }
    @Override
    public List<ClassBooking> findByMemberId(int memberId) {
        List<ClassBooking> bookings = new ArrayList<>();
        String sql = """
            SELECT b.id AS booking_id,
                   m.id AS member_id, m.name AS member_name,
                   f.id AS class_id, f.type AS class_type, f.max_places AS max_places
            FROM bookings b
            JOIN members m ON b.member_id = m.id
            JOIN fitness f ON b.class_id = f.id
            WHERE m.id = ?;
            """;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Member member = new Member.Builder()
                        .id(rs.getInt("member_id"))
                        .name(rs.getString("member_name"))
                        .build();
                FitnessClass fitnessClass = new FitnessClass.Builder()
                        .id(rs.getInt("class_id"))
                        .fitnessType(rs.getString("class_type"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();

                bookings.add(new ClassBooking(member, fitnessClass));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bookings by member id", e);
        }
        return bookings;
    }
    @Override
    public List<ClassBooking> findAll() {
        List<ClassBooking> bookings = new ArrayList<>();
        String sql = """
            SELECT m.id AS member_id, m.name AS member_name,
                   f.id AS class_id, f.type AS class_type, f.max_places AS max_places
            FROM bookings b
            JOIN members m ON b.member_id = m.id
            JOIN fitness f ON b.class_id = f.id;
            """;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                Member member = new Member.Builder()
                        .id(rs.getInt("member_id"))
                        .name(rs.getString("member_name"))
                        .build();

                FitnessClass fitnessClass = new FitnessClass.Builder()
                        .id(rs.getInt("class_id"))
                        .fitnessType(rs.getString("class_type"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();

                bookings.add(new ClassBooking(member, fitnessClass));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bookings", e);
        }
        return bookings;
    }

}