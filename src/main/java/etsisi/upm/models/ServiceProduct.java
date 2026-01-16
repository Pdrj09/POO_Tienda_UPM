package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.util.Categories;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ServiceProduct extends Product {
    private final LocalDateTime expirationDate;
    private int numPeople;
    private double finalPrice;

    private final int minimumCreationTime;
    private final ChronoUnit minimumTimeUnit;
    private final String serviceType;

    public ServiceProduct(int id, String name, double pricePerPerson, int maxPeople,
                          LocalDateTime expirationDate, int minimumCreationTime,
                          ChronoUnit minimumTimeUnit, String serviceType) {
        super(id, name, pricePerPerson, Categories.EMPTY);

        this.minimumCreationTime = minimumCreationTime;
        this.minimumTimeUnit = minimumTimeUnit;
        this.serviceType = serviceType;

        //validation people logic
        if (maxPeople <= Constants.SERVICE_PROD_MINPEOPLE)
            throw new IllegalArgumentException(Constants.ERROR_TOOMANY_PEOPLE);
        else if (maxPeople > Constants.TIME_MAX_PEOPLE_SERVICE)
            this.numPeople = Constants.TIME_MAX_PEOPLE_SERVICE;
        else
            this.numPeople = maxPeople;

        this.finalPrice = Constants.SERVICE_PROD_BASEPRICE; //inicialization of the finalPrice
        this.expirationDate = expirationDate;

        // time validation
        if (!isFeasible(LocalDateTime.now())) {
            String timeUnitStr = this.minimumTimeUnit == ChronoUnit.HOURS ? Constants.HOURS : Constants.DAYS;
            StringBuilder error = new StringBuilder();
            error.append(Constants.ERROR_SERVICE_DATE_FEASIBILITY);
            error.append(getMinimumCreationTime());
            error.append(Constants.STR_BLANK_SPACE);
            error.append(timeUnitStr);
            error.append(Constants.IN_THE_FUTURE);
            throw new IllegalArgumentException(String.valueOf(error));
        }
    }

    // creation methods

    public static ServiceProduct createMeeting(int id, String name, double price, int people, LocalDateTime date) {
        return new ServiceProduct(id, name, price, people, date,
                Constants.TIME_MEETING_PLANNING_HOURS, ChronoUnit.HOURS, Constants.STR_MEETING);
    }

    public static ServiceProduct createFood(int id, String name, double price, int people, LocalDateTime date) {
        return new ServiceProduct(id, name, price, people, date,
                Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.STR_FOOD);
    }

    public static ServiceProduct createTransport(int id, String name, double price, int people, LocalDateTime date) {
        return new ServiceProduct(id, name, price, people, date,
                Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.STR_FOOD);
    }

    public static ServiceProduct createShow(int id, String name, double price, int people, LocalDateTime date) {
        return new ServiceProduct(id, name, price, people, date,
                Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.STR_FOOD);
    }

    public static ServiceProduct createInsurance(int id, String name, double price, int people, LocalDateTime date) {
        return new ServiceProduct(id, name, price, people, date,
                Constants.TIME_FOOD_PLANNING_DAYS, ChronoUnit.DAYS, Constants.STR_FOOD);
    }

    // Abstract methods
    public int getMinimumCreationTime() {return this.minimumCreationTime;}
    public ChronoUnit getMinimumTimeUnit(){return this.minimumTimeUnit;}

    // logic
    public boolean isFeasible(LocalDateTime creationTime) {
        long timeDifference = creationTime.until(expirationDate, getMinimumTimeUnit());
        return timeDifference >= getMinimumCreationTime();
    }

    public double getPricePerPerson() {
        return getPrice();
    }

    @Override
    public int getMaxPers() {
        return this.numPeople;
    }

    public double getFinalPrice() {
        return Utilities.round(finalPrice);
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public List<KV> toViewKVList() {
        List<KV> kvs = super.toViewKVList();
        kvs.removeIf(kv -> kv.key.equals("Category"));
        kvs.removeIf(kv -> kv.key.equals("Price"));
        kvs.removeIf(kv -> kv.key.equals("Max Personalizations"));

        kvs.add(new KV("Max Persons", String.valueOf(this.getMaxPers())));
        kvs.add(new KV("Price ud.", String.valueOf(getPricePerPerson())));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = expirationDate.format(formatter);
        kvs.add(new KV("Date of Event", formattedDate));

        return kvs;
    }

    // --- toString() ---
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Constants.OPEN_BRACE);
        builder.append(serviceType);
        builder.append(Constants.STR_PROD_ID).append(getId());
        builder.append(Constants.STR_PROD_NAME).append(getName()).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(getCategory());

        // use of the constant price per person
        builder.append(Constants.STR_PRICE_PERSON).append(getPrice());

        // add of the specific attribute for service product
        builder.append(Constants.STR_EXPIRATION).append(expirationDate);
        builder.append(Constants.STR_MAX_PEOPLE_ALLOWED).append(getMaxPers());
        builder.append(Constants.STR_FINAL_PRICE).append(getFinalPrice());
        builder.append(Constants.CLOSE_BRACE);

        return builder.toString();
    }
}