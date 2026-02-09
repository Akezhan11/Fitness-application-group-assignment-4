package entities;

import java.time.LocalDate;

public class MembershipType {

    private final int id;
    private final int memberId;
    private final String type;
    private final double price;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean active;

    private MembershipType(Builder builder) {
        this.id = builder.id;
        this.memberId = builder.memberId;
        this.type = builder.type;
        this.price = builder.price;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.active = builder.active;
    }
    public int getId() { return id; }
    public int getMemberId() { return memberId; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }

    public static class Builder {
        private int id;
        private int memberId;
        private String type;
        private double price;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean active;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder memberId(int memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public MembershipType build() {
            if (type == null) {
                throw new IllegalStateException("Type cannot be null");
            }
            return new MembershipType(this);
        }
    }
}
