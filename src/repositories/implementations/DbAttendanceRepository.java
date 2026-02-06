package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.Attendance;
import repositories.AttendanceRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbAttendanceRepository implements AttendanceRepository {

    @Override
    public void save(Attendance attendance) {
        String sql = """
                INSERT INTO attendance(member_id, visit_date)
                VALUES (?, ?)
                """;

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, attendance.getMemberId());
            st.setDate(2, Date.valueOf(attendance.getVisitDate()));

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    attendance.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving attendance", e);
        }
    }

    @Override
    public boolean exists(int memberId, LocalDate date) {
        String sql = """
                SELECT 1 FROM attendance
                WHERE member_id = ? AND visit_date = ?
                LIMIT 1
                """;

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, memberId);
            st.setDate(2, Date.valueOf(date));

            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking attendance exists", e);
        }
    }

    @Override
    public List<Attendance> findByMemberId(int memberId) {
        String sql = """
                SELECT * FROM attendance
                WHERE member_id = ?
                ORDER BY visit_date DESC
                """;

        List<Attendance> list = new ArrayList<>();

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, memberId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAttendance(rs));
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding attendance by member", e);
        }
    }

    @Override
    public List<Attendance> findAll() {
        String sql = "SELECT * FROM attendance ORDER BY visit_date DESC";
        List<Attendance> list = new ArrayList<>();

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(mapAttendance(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all attendance", e);
        }
    }

    private Attendance mapAttendance(ResultSet rs) throws SQLException {
        return new Attendance(
                rs.getInt("id"),
                rs.getInt("member_id"),
                rs.getDate("visit_date").toLocalDate()
        );
    }
}
