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

public class DbClassBookingRepository implements ClassBookingRepository{
    @Override
    public void save(ClassBooking classBooking) {
        String sql = """
                INSERT INTO bookings(member_id,class_id) VALUES (?,?);
                """;
        try(Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,classBooking.getMember().getId());
            ps.setInt(2,classBooking.getFitnessClass().getId());
            ps.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException("Error while saving booking ", e);
        }
    }
    @Override
    public boolean exists(int memberId, int fitnessClassId){
        String sql = """
                SELECT 1 FROM bookings WHERE member_id =? AND class_id =?;
                """;
        try(Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,memberId);
            ps.setInt(2,fitnessClassId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(Exception e){
            throw new RuntimeException("Error finding booking place " ,e);
        }
    }
    @Override
    public int countByFitnessClassId(int fitnessClassId){
        String sql = """
                SELECT COUNT(*) FROM bookings where class_id = ?;
        """;
        try(Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,fitnessClassId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch(Exception e){
            throw new RuntimeException("Error counting fitness class id " , e);
        }
    }
    @Override
    public void delete(ClassBooking classBooking) {
        String sql= """
                DELETE FROM booking WHERE member_id =? AND class_id =? ;
                """;
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,classBooking.getMember().getId());
            ps.setInt(2,classBooking.getFitnessClass().getId());
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Error while deleting data ",e);
        }
    }
    @Override
    public List<ClassBooking> findByClassId(int fitnessClassId) {
        List<ClassBooking> bookings = new ArrayList<>();
        String sql = """
        SELECT
            b.id AS booking_id,
            m.id AS member_id,
            m.name AS member_name,
            f.id AS class_id,
            f.type AS class_type,
            f.max_places AS max_places
        FROM bookings b
        JOIN members m ON b.member_id = m.id
        JOIN fitness f ON b.class_id = f.id
        WHERE f.id = ?
    """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fitnessClassId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Member member = new Member();
                member.setId(rs.getInt("member_id"));
                member.setName(rs.getString("member_name"));

                FitnessClass fitnessClass = new FitnessClass();
                fitnessClass.setId(rs.getInt("class_id"));
                fitnessClass.setFitnessType(rs.getString("class_type"));
                fitnessClass.setMaxPlaces(rs.getInt("max_places"));

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
        SELECT
            b.id AS booking_id,
            m.id AS member_id,
            m.name AS member_name,
            f.id AS class_id,
            f.type AS class_type,
            f.max_places AS max_places
        FROM bookings b
        JOIN members m ON b.member_id = m.id
        JOIN fitness f ON b.class_id = f.id
        WHERE m.id = ?
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Member member = new Member();
                member.setId(rs.getInt("member_id"));
                member.setName(rs.getString("member_name"));

                FitnessClass fitnessClass = new FitnessClass();
                fitnessClass.setId(rs.getInt("class_id"));
                fitnessClass.setFitnessType(rs.getString("class_type"));
                fitnessClass.setMaxPlaces(rs.getInt("max_places"));

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
            SELECT
                m.id AS member_id,
                m.name AS member_name,
                f.id AS class_id,
                f.type AS class_type,
                f.max_places AS max_places
            FROM bookings b
            JOIN members m ON b.member_id = m.id
            JOIN fitness f ON b.class_id = f.id;
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getInt("member_id"));
                member.setName(rs.getString("member_name"));

                FitnessClass fitnessClass = new FitnessClass();
                fitnessClass.setId(rs.getInt("class_id"));
                fitnessClass.setFitnessType(rs.getString("class_type"));
                fitnessClass.setMaxPlaces(rs.getInt("max_places"));

                bookings.add(new ClassBooking(member, fitnessClass));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bookings", e);
        }

        return bookings;
    }
}