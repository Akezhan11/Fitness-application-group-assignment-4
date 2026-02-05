package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.MembershipType;
import repositories.MembershipRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbMembershipRepository implements MembershipRepository {
    private final DatabaseConnection db = DatabaseConnection.getInstance();
    @Override
    public void save(MembershipType membership) {

        String sql = """
            INSERT INTO memberships (member_id, type, start_date, end_date, active)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, membership.getMemberId());
            ps.setString(2, membership.getType());
            ps.setDate(3, java.sql.Date.valueOf(membership.getStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(membership.getEndDate()));
            ps.setBoolean(5, membership.isActive());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error saving membership", e);
        }
    }

    @Override
    public MembershipType findByMemberId(int memberId) {

        String sql = """
            SELECT * FROM memberships WHERE member_id = ?
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MembershipType m = new MembershipType();
                m.setId(rs.getInt("id"));
                m.setMemberId(rs.getInt("member_id"));
                m.setType(rs.getString("type"));
                m.setStartDate(rs.getDate("start_date").toLocalDate());
                m.setEndDate(rs.getDate("end_date").toLocalDate());
                m.setActive(rs.getBoolean("active"));
                return m;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error finding membership", e);
        }
    }

    @Override
    public void update(MembershipType membership) {

        String sql = """
            UPDATE memberships
            SET type = ?, start_date = ?, end_date = ?, active = ?
            WHERE member_id = ?
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, membership.getType());
            ps.setDate(2, java.sql.Date.valueOf(membership.getStartDate()));
            ps.setDate(3, java.sql.Date.valueOf(membership.getEndDate()));
            ps.setBoolean(4, membership.isActive());
            ps.setInt(5, membership.getMemberId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error updating membership", e);
        }
    }

    @Override
    public void deactivate(int memberId) {

        String sql = """
            UPDATE memberships SET active = false WHERE member_id = ?
        """;

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error deactivating membership", e);
        }
    }
}
