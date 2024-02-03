package cache;

import model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Stores spotify's categories to prevent excessive API calls
public class CategoryCache {
    // Maps a category name to its category object, useful for matching user input to categories
    private static HashMap<String, Category> categoryHashMap = new HashMap<>();

    // List to be returned anytime after our first category API call
    private static List<Category> categoryList = new ArrayList<>();

    //Getters and setters below
    public static HashMap<String, Category> getCategoryHashMap(){
        return categoryHashMap;
    }

    public static void setCategoryHashMap(HashMap<String, Category> categoryHashMap){
        CategoryCache.categoryHashMap = categoryHashMap;
    }

    public static List<Category> getCategoryList(){
        return categoryList;
    }

    public static void setCategoryList(List<Category> categoryList){
        CategoryCache.categoryList = categoryList;
    }
}
