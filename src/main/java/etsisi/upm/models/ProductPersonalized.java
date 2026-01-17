package etsisi.upm.models;

import etsisi.upm.util.Constants;
import etsisi.upm.io.KV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ProductPersonalized extends Product {
    protected boolean personalizable;
    protected int maxPers;

    private List<String> customizations;

    public ProductPersonalized(Product prod, List<String> customizations) {
        super(prod.getId(), prod.getName(), prod.getPrice(), prod.getCategory(), prod.getMaxPers());
        this.customizations = customizations;
    }

    public List<String> getCustomizations() {
        return this.customizations;
    }

    public Integer getCustomizationsAmount(){
        return this.customizations.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductPersonalized product = (ProductPersonalized) o;
        if(this.getId() == product.getId()){
            return new HashSet<>(this.customizations).equals(new HashSet<>(product.getCustomizations()));
        }else return false;
    }


    @Override
    public int hashCode() {
        int result = Integer.hashCode(this.getId());
        result = 31 * result + new HashSet<>(this.customizations).hashCode();
        return result;
    }

    @Override
    public List<KV> getPresentableDetails() {
        List<KV> kvs = new ArrayList<>();
        //for personalized titles
        kvs.add(new KV("Custom. amount", String.valueOf(this.getCustomizationsAmount())));
        String customizationsStr = String.join(", ", this.getCustomizations());
        kvs.add(new KV("Details", customizationsStr));
        return kvs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.OPEN_BRACE);
        builder.append(Constants.STR_PRODUCT_PERSONALIZED);
        builder.append(Constants.STR_PROD_ID).append(id);
        builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(category);
        builder.append(Constants.STR_PRICE).append(price);
        builder.append(Constants.STR_MAXPERS).append(maxPers);
        builder.append(Constants.STR_PERONALIZATIONS).append(customizations);
        builder.append(Constants.CLOSE_BRACE);
        return builder.toString();
    }

    // Archivo: etsisi.upm.models/ProductPersonalized.java
    @Override
    public int compareTo(Sellable other) {
        //compareto of the father
        int comparison = super.compareTo(other);
        if (comparison != 0)
            return comparison;
        //we compare the personalizations
        if (other instanceof ProductPersonalized otherPersonalized) {
            //we sort the lists
            List<String> thisCustoms = new ArrayList<>(this.customizations);
            List<String> otherCustoms = new ArrayList<>(otherPersonalized.getCustomizations());
            Collections.sort(thisCustoms);
            Collections.sort(otherCustoms);

            String thisCustomStr = String.join(",", thisCustoms);
            String otherCustomStr = String.join(",", otherCustoms);

            return thisCustomStr.compareTo(otherCustomStr);
        }
        //fatal case: we force not null
        return 1;
    }

}
