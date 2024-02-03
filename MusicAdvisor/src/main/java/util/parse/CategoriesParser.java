package util.parse;

import cache.CategoryCache;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Parses the categories
public class CategoriesParser implements Parser<Category>{
    @Override
    public List<Category> parse(String response) {
        HashMap<String, Category> categoriesMap = new HashMap<>();
        List<Category> categoriesList = new ArrayList<>();

        try {
            JsonObject jsonData = JsonParser.parseString(response).getAsJsonObject();
            JsonObject jsonCategories = jsonData.getAsJsonObject("categories");


            for (JsonElement item : jsonCategories.get("items").getAsJsonArray()) {
                JsonObject category = item.getAsJsonObject();
                String id = category.get("id").getAsString();
                String name = category.get("name").getAsString().toLowerCase();
                //When we get the category information we can add it to a hashmap to cache it
                categoriesMap.put(name, new Category(name, id));
            }

            CategoryCache.setCategoryHashMap(categoriesMap);

            categoriesList = new ArrayList<>(categoriesMap.size());
            categoriesList.addAll(categoriesMap.values());
            CategoryCache.setCategoryList(categoriesList);
        }catch(NullPointerException | IllegalStateException e){
            System.out.println("Error parsing data: " + e);
        }

        return categoriesList;
    }
}
