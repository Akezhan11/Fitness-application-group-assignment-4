package service;
import utils.Filter;
import utils.ListUtils;

import entities.FitnessClass;
import repositories.FitnessClassRepository;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FitnessClassService {
    private final FitnessClassRepository repository;
    public FitnessClassService(FitnessClassRepository repository) {
        this.repository = repository;
    }
    public void addFitnessClass(FitnessClass fitnessClass) {
        if (fitnessClass == null) {
            throw new IllegalArgumentException("FitnessClass cannot be null");
        }
        if (fitnessClass.getFitnessType() == null || fitnessClass.getFitnessType().isBlank()) {
            throw new IllegalArgumentException("Fitness type cannot be empty");
        }
        repository.save(fitnessClass);
    }
    public FitnessClass findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
        FitnessClass fc = repository.findById(id);
        if (fc == null) {
            throw new RuntimeException("Fitness class not found with id: " + id);
        }
        return fc;
    }
    public FitnessClass findByType(String fitnessType) {
        if (fitnessType == null || fitnessType.isBlank()) {
            throw new IllegalArgumentException("Fitness type cannot be empty");
        }
        FitnessClass fc = repository.findByType(fitnessType);
        if (fc == null) {
            throw new RuntimeException("Fitness class not found with type: " + fitnessType);
        }
        return fc;
    }
    public List<FitnessClass> getByTrainerName(String trainerName, String trainerSurname) {
        if (trainerName == null || trainerName.isBlank()) {
            throw new IllegalArgumentException("Trainer name cannot be empty");
        }
        return repository.findByTrainerName(trainerName, trainerSurname);
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
    public List<FitnessClass> getFilteredClasses(Filter<FitnessClass> filter) {
        return ListUtils.filter(getAll(), filter);
    }

    public List<FitnessClass> getSortedClasses(Comparator<FitnessClass> comparator) {

        return getAll()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    public List<FitnessClass> getClassesByTrainer(String trainerName) {
        return getFilteredClasses(fc ->
                fc.getFitnessTrainerName() != null &&
                        fc.getFitnessTrainerName().equalsIgnoreCase(trainerName));
    }
    public List<FitnessClass> getClassesWithFreePlaces() {

        return getFilteredClasses(fc ->
                fc.getMaxPlaces() > 0);
    }
    public List<FitnessClass> getClassesCheaperThan(int maxCost) {
        return getFilteredClasses(fc ->
                fc.getFitnessCost() <= maxCost);
    }
    public List<FitnessClass> sortByCostAscending() {
        return getSortedClasses(
                Comparator.comparingInt(FitnessClass::getFitnessCost)
        );
    }
    public List<FitnessClass> sortByDate() {
        return getSortedClasses(
                Comparator.comparing(FitnessClass::getFitnessDate)
        );
    }
    public List<FitnessClass> sortByTrainerName() {
        return getSortedClasses(
                Comparator.comparing(FitnessClass::getFitnessTrainerName)
        );
    }
}
