package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.Member;
import repositories.MemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbMemberRepository implements MemberRepository{
    private final DatabaseConnection db = DatabaseConnection.getInstance();
    private final List<Member> members = new ArrayList<>();
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
    public Optional<Member> findById(int id) {
        String sql = "SELECT * FROM members WHERE id=?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Member member = new Member.Builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .surname(rs.getString("surname"))
                        .phoneNumber(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .gender(rs.getString("gender"))
                        .build();

                return Optional.of(member);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Error finding member by id " + id, e);
        }
    }

    @Override
    public Member findByPhone(String phone) {
        String sql = "SELECT * FROM members WHERE phone=?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Member.Builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .surname(rs.getString("surname"))
                        .phoneNumber(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .gender(rs.getString("gender"))
                        .build();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding member by phone " + phone, e);
        }
    }
    @Override
    public Member findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email=?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                return new Member.Builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .surname(rs.getString("surname"))
                        .phoneNumber(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .gender(rs.getString("gender"))
                        .build();
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
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Member member = new Member.Builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .surname(rs.getString("surname"))
                        .phoneNumber(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .gender(rs.getString("gender"))
                        .build();
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all members", e);
        }
    }
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
