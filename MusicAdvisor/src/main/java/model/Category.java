package model;

// Class models a category entity
public class Category {
    private String name;
    private String id;

    public Category(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString(){
        return this.name;
    }
}
