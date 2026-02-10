package repositories.implementations;

import edu.aitu.oop3.db.DatabaseConnection;
import entities.FitnessClass;
import repositories.FitnessClassRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbFitnessClassRepository implements FitnessClassRepository {
    private final DatabaseConnection db = DatabaseConnection.getInstance();
    @Override
    public void save(FitnessClass fitnessClass) {
        String sql = """
            INSERT INTO fitness(type,description,date,time,cost,trainer_first_name,trainer_last_name,max_places)
            VALUES (?,?,?,?,?,?,?,?);
        """;
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fitnessClass.getFitnessType());
            ps.setString(2, fitnessClass.getFitnessDescription());
            ps.setString(3, fitnessClass.getFitnessDate());
            ps.setString(4, fitnessClass.getFitnessTime());
            ps.setInt(5, fitnessClass.getFitnessCost());
            ps.setString(6, fitnessClass.getFitnessTrainerName());
            ps.setString(7, fitnessClass.getFitnessTrainerSurname());
            ps.setInt(8, fitnessClass.getMaxPlaces());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error saving fitness class ", e);
        }
    }
    @Override
    public Optional<FitnessClass> findById(int id) {
        String sql = "SELECT * FROM fitness WHERE id=?;";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                FitnessClass fc = new FitnessClass.Builder()
                        .id(rs.getInt("id"))
                        .fitnessType(rs.getString("type"))
                        .fitnessDescription(rs.getString("description"))
                        .fitnessDate(rs.getString("date"))
                        .fitnessTime(rs.getString("time"))
                        .fitnessCost(rs.getInt("cost"))
                        .fitnessTrainerName(rs.getString("trainer_first_name"))
                        .fitnessTrainerSurname(rs.getString("trainer_last_name"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();
                return Optional.of(fc);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding fitness by id " + id, e);
        }
    }

    @Override
    public FitnessClass findByType(String fitnessType) {
        String sql = "SELECT * FROM fitness WHERE type=?;";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fitnessType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new FitnessClass.Builder()
                        .id(rs.getInt("id"))
                        .fitnessType(rs.getString("type"))
                        .fitnessDescription(rs.getString("description"))
                        .fitnessDate(rs.getString("date"))
                        .fitnessTime(rs.getString("time"))
                        .fitnessCost(rs.getInt("cost"))
                        .fitnessTrainerName(rs.getString("trainer_first_name"))
                        .fitnessTrainerSurname(rs.getString("trainer_last_name"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding fitness class by type " + fitnessType, e);
        }
    }

    @Override
    public List<FitnessClass> findByTrainerName(String trainerName, String trainerSurname) {
        List<FitnessClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM fitness WHERE trainer_first_name = ? AND trainer_last_name = ?;";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trainerName);
            ps.setString(2, trainerSurname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FitnessClass fc = new FitnessClass.Builder()
                        .id(rs.getInt("id"))
                        .fitnessType(rs.getString("type"))
                        .fitnessDescription(rs.getString("description"))
                        .fitnessDate(rs.getString("date"))
                        .fitnessTime(rs.getString("time"))
                        .fitnessCost(rs.getInt("cost"))
                        .fitnessTrainerName(rs.getString("trainer_first_name"))
                        .fitnessTrainerSurname(rs.getString("trainer_last_name"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build();

                classes.add(fc);
            }
            return classes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding classes by trainer", e);
        }
    }
    @Override
    public List<FitnessClass> findByCost(int cost) {
        List<FitnessClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM fitness WHERE cost = ?;";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cost);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classes.add(new FitnessClass.Builder()
                        .id(rs.getInt("id"))
                        .fitnessType(rs.getString("type"))
                        .fitnessDescription(rs.getString("description"))
                        .fitnessDate(rs.getString("date"))
                        .fitnessTime(rs.getString("time"))
                        .fitnessCost(rs.getInt("cost"))
                        .fitnessTrainerName(rs.getString("trainer_first_name"))
                        .fitnessTrainerSurname(rs.getString("trainer_last_name"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build());
            }
            return classes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding classes by cost", e);
        }
    }
    @Override
    public List<FitnessClass> findAll() {
        List<FitnessClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM fitness;";
        try (Connection con = db.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                classes.add(new FitnessClass.Builder()
                        .id(rs.getInt("id"))
                        .fitnessType(rs.getString("type"))
                        .fitnessDescription(rs.getString("description"))
                        .fitnessDate(rs.getString("date"))
                        .fitnessTime(rs.getString("time"))
                        .fitnessCost(rs.getInt("cost"))
                        .fitnessTrainerName(rs.getString("trainer_first_name"))
                        .fitnessTrainerSurname(rs.getString("trainer_last_name"))
                        .maxPlaces(rs.getInt("max_places"))
                        .build());
            }
            return classes;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all fitness classes", e);
        }
    }
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM fitness WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
