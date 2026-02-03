package repositories;

import entities.FitnessClass;
import java.util.List;
public interface FitnessClassRepository {
    void save(FitnessClass fitnessClass);
    FitnessClass findById(int id);
    FitnessClass findByType(String fitnessType);
    List<FitnessClass> findByTrainerName(String fitnessTrainerName, String fitnessTrainerSurname);
    List<FitnessClass> findByCost(int fitnessCost);
    List<FitnessClass> findAll();
}
