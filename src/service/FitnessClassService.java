package service;
import entities.FitnessClass;
import repositories.FitnessClassRepository;

import java.util.List;

public class FitnessClassService {

    private final FitnessClassRepository repository;

    public FitnessClassService(FitnessClassRepository repository) {
        this.repository = repository;
    }

    public void addFitnessClass(FitnessClass fitnessClass) {
        repository.save(fitnessClass);
    }

    public FitnessClass findById(int id) {
        FitnessClass fc = repository.findById(id);
        if (fc == null) {
            throw new RuntimeException("Fitness class not found");
        }
        return fc;
    }
    public FitnessClass findByType(String fitnessType){
        FitnessClass fc = repository.findByType(fitnessType);
        if (fc == null) {
            throw new RuntimeException("Fitness class not found");
        }
        return fc;
    }
    public List<FitnessClass> getByTrainerName(String trainerName, String trainerSurname) {
        if (trainerName == null || trainerName.isEmpty()) {
            throw new IllegalArgumentException("Trainer name cannot be empty");
        }
        return repository.findByTrainerName(trainerName,trainerSurname);
    }

    public List<FitnessClass> getByCost(int cost) {
        if (cost <= 0) {
            throw new IllegalArgumentException("Cost must be positive");
        }
        return repository.findByCost(cost);
    }

    public List<FitnessClass> getAll() {
        return repository.findAll();
    }
}
