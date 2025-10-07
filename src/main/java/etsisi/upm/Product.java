package etsisi.upm;


import java.util.Objects;

public class Product {
  ///We asign the variables
  private int id;
  private String name;
  private float price;
  private String nameOfCategory;

  //this is the constructor that creates a product
  public Product(int id,String name , float price , String nameOfCategory ){
      this.id = id;
      this.name= name;
      this.price = price ;
      this.nameOfCategory = nameOfCategory;
      System.out.println(toString());
  }
  /// update certain characteristics of product
    public int update(String name, String category, float price){
        this.name = name;
        this.nameOfCategory = category;
        this.price = price;
        return id;
    }

/// toString method
@Override
  public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("class:Product");
    builder.append(", id:").append(id);
    builder.append(", name:'").append(name).append("'");
    builder.append(", category:").append(nameOfCategory);
    builder.append(", price:").append(price);
    builder.append("}");
    return builder.toString();

  }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //getters and setters

  public void setId ( int id){
  this.id = id;
  }
  public int getId (){
    return id;
  }
  public void setName(String name){
    this.name = name; 
  }
  public String getName(){
    return name;
  }
  public void setPrice(float price){
    this.price = price;
  }
  public float getPrice(){
    return price;
  }
  public void setnameOfCategory( String category){
    this.nameOfCategory = category;
  }
  public String getnameOfCategory(){
    return nameOfCategory ;
  }



  
}
