package entities;

import repositories.FitnessClassRepository;

import java.time.LocalDate;

public class FitnessClass{
    private final int id;
    private final String fitnessType;
    private final String fitnessDescription;
    private final String fitnessDate;
    private final String fitnessTime;
    private final int fitnessCost;
    private final String fitnessTrainerName;
    private final String fitnessTrainerSurname;
    private final int maxPlaces;
    private FitnessClass(Builder builder) {
        this.id = builder.id;
        this.fitnessType = builder.fitnessType;
        this.fitnessDescription = builder.fitnessDescription;
        this.fitnessDate = builder.fitnessDate;
        this.fitnessTime = builder.fitnessTime;
        this.fitnessCost = builder.fitnessCost;
        this.fitnessTrainerName = builder.fitnessTrainerName;
        this.fitnessTrainerSurname = builder.fitnessTrainerSurname;
        this.maxPlaces = builder.maxPlaces;
    }
    public static class Builder {
        private int id;
        private String fitnessType;
        private String fitnessDescription;
        private String fitnessDate;
        private String fitnessTime;
        private int fitnessCost;
        private String fitnessTrainerName;
        private String fitnessTrainerSurname;
        private int maxPlaces;

        public Builder id(int id) {
            this.id = id;
            return this;
        }
        public Builder fitnessType(String fitnessType) {
            this.fitnessType = fitnessType;
            return this;
        }
        public Builder fitnessDescription(String fitnessDescription) {
            this.fitnessDescription = fitnessDescription;
            return this;
        }
        public Builder fitnessDate(String fitnessDate) {
            this.fitnessDate = fitnessDate;
            return this;
        }
        public Builder fitnessTime(String fitnessTime) {
            this.fitnessTime = fitnessTime;
            return this;
        }
        public Builder fitnessCost(int fitnessCost) {
            this.fitnessCost = fitnessCost;
            return this;
        }
        public Builder fitnessTrainerName(String fitnessTrainerName) {
            this.fitnessTrainerName = fitnessTrainerName;
            return this;
        }
        public Builder fitnessTrainerSurname(String fitnessTrainerSurname) {
            this.fitnessTrainerSurname = fitnessTrainerSurname;
            return this;
        }
        public Builder maxPlaces(int maxPlaces) {
            this.maxPlaces = maxPlaces;
            return this;
        }
        public FitnessClass build() {
            return new FitnessClass(this);
        }

    }

    public int getId(){
        return id;
    }
    public String getFitnessType() {
        return fitnessType;
    }
    public String getFitnessDescription() {
        return fitnessDescription;
    }
    public String getFitnessDate() {
        return fitnessDate;
    }
    public String getFitnessTime() {
        return fitnessTime;
    }
    public int getFitnessCost(){
        return fitnessCost;
    }
    public String getFitnessTrainerName() {
        return fitnessTrainerName;
    }
    public String getFitnessTrainerSurname(){
        return fitnessTrainerSurname;
    }
    public int getMaxPlaces() {
        return maxPlaces;
    }

    @Override
    public String toString() {
        return "id:" + id + "\n" +
                "Fitness type: " + fitnessType + "\n" +
                "Fitness Description " + fitnessDescription + "\n" +
                "Cost: " + fitnessCost + "\n" +
                "Trainer" + fitnessTrainerName + " " + fitnessTrainerSurname + "\n" +
                "date and time: "  + fitnessDate + " " + fitnessTime;
    }
}