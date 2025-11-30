package etsisi.upm.models;

import etsisi.upm.Constants;
import etsisi.upm.util.Categories;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ProductPersonalized extends Product{
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.OPEN_BRACE);
        builder.append(Constants.STR_PRODUCT);
        builder.append(Constants.STR_PROD_ID).append(id);
        builder.append(Constants.STR_PROD_NAME).append(name).append(Constants.QUOTE);
        builder.append(Constants.STR_CATEGORY).append(category);
        builder.append(Constants.STR_PRICE).append(price);
        builder.append(Constants.STR_MAXPERS).append(maxPers);
        builder.append(Constants.STR_PERONALIZATIONS).append(customizations);
        builder.append(Constants.CLOSE_BRACE);
        return builder.toString();
    }

}
