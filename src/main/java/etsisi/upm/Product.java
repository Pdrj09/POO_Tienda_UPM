package etsisi.upm;


public class Product {
  ///Asignamos las variables
  private int id;
  private String name;
  private float price;
  private String nameOfCategory;
  //constructor

  public void Product(int id,String name , float price , String nameOfCategory ){
      this.id = id;
      this.name= name;
      this.price = price ;
      this.nameOfCategory = nameOfCategory;
  }

  //getters y setters
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
