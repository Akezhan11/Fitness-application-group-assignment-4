package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.Member;
import repositories.MemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbMemberRepository implements MemberRepository{
    private final DatabaseConnection db = DatabaseConnection.getInstance();
    @Override
    public void save(Member member){
        String sql = """
                INSERT INTO members(name, surname, phone, email, gender) VALUES(?,?,?,?,?)
                """;
        try(Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,member.getName());
            ps.setString(2,member.getSurname());
            ps.setString(3,member.getPhoneNumber());
            ps.setString(4,member.getEmail());
            ps.setString(5,member.getGender());
            ps.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException("Error saving member ", e);
        }
    }
    @Override
    public Member findById(int id) {
        String sql = """
                SELECT * FROM members WHERE id=? ;
                """;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String phoneNumber = rs.getString("phone");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                return new Member(name, surname,phoneNumber,email, gender);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding member by id " + id, e);
        }
    }
    @Override
    public Member findByPhone(String phone) {
        String sql = """
                SELECT * FROM members WHERE phone=? ;
                """;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String phoneNumber = rs.getString("phone");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                return new Member(name, surname,phoneNumber,email, gender);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding member by phone " + phone, e);
        }
    }
    @Override
    public Member findByEmail(String email) {
        String sql = """
                SELECT * FROM members WHERE email=? ;
                """;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String phoneNumber = rs.getString("phone");
                String emailFromDb = rs.getString("email");
                String gender = rs.getString("gender");
                return new Member(name, surname,phoneNumber,emailFromDb, gender);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding member by email " + email, e);
        }
    }
    @Override
    public void update(Member member) {
        String sql = """
                UPDATE members SET name=?, surname=?, phone=?, email=?, gender=? WHERE id=?
                """;
        try(Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,member.getName());
            ps.setString(2,member.getSurname());
            ps.setString(3,member.getPhoneNumber());
            ps.setString(4,member.getEmail());
            ps.setString(5,member.getGender());
            ps.setInt(6,member.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Member with id " + member.getId() + " not found");
            }

        }catch(Exception e){
            throw new RuntimeException("Error updating member ", e);
        }
    }
    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM members;";
        List<Member> members = new ArrayList<>();

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String phoneNumber = rs.getString("phone");
                String email = rs.getString("email");
                String gender = rs.getString("gender");

                members.add(new Member(name, surname, phoneNumber, email, gender));
            }
            return members;

        } catch (Exception e) {
            throw new RuntimeException("Error finding all members", e);
        }
    }

}
