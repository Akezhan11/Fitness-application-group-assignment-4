package repositories;

import entities.FitnessClass;
import java.util.List;
public interface FitnessClassRepository extends Repository<FitnessClass> {
    FitnessClass findByType(String fitnessType);
    List<FitnessClass> findByTrainerName(String fitnessTrainerName, String fitnessTrainerSurname);
    List<FitnessClass> findByCost(int fitnessCost);
}
