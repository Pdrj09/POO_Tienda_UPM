package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;
import etsisi.upm.util.Categories;
import etsisi.upm.util.Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class ServiceProduct extends Sellable {
    protected final LocalDateTime expirationDate;
    protected int numPeople;
    private double finalPrice;

    public ServiceProduct(int id, String name, double pricePerPerson, int maxPeople, LocalDateTime expirationDate) {
        super(id,
                name,
                pricePerPerson);

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
            String timeUnit = getMinimumTimeUnit() == ChronoUnit.HOURS ? Constants.HOURS : Constants.DAYS;
            StringBuilder error = new StringBuilder();
            error.append(Constants.ERROR_SERVICE_DATE_FEASIBILITY);
            error.append(getMinimumCreationTime());
            error.append(Constants.STR_BLANK_SPACE);
            error.append(timeUnit);
            error.append(Constants.IN_THE_FUTURE);
            throw new IllegalArgumentException(String.valueOf(error));
        }
    }

    // Abstract methods
    public abstract int getMinimumCreationTime();

    public abstract ChronoUnit getMinimumTimeUnit();

    // logic
    public boolean isFeasible(LocalDateTime creationTime) {
        long timeDifference = creationTime.until(expirationDate, getMinimumTimeUnit());
        return timeDifference >= getMinimumCreationTime();
    }

    public double getPricePerPerson() {
        return getPrice();
    }

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
        builder.append(Constants.STR_SERVICE_PRODUCT);
        builder.append(Constants.STR_PROD_ID).append(getId());
        builder.append(Constants.STR_PROD_NAME).append(getName()).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(Categories.EMPTY);

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