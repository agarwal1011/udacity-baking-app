package udacity.project.com.bakingapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipes")
@TypeConverters(Converters.class)
public class Recipe implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String image;
    private String servings;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();

    @Ignore
    public Recipe() {
    }

    public Recipe(String id, String image, String servings, String name, List<Ingredient> ingredients,
                  List<Step> steps) {
        this.id = id;
        this.image = image;
        this.servings = servings;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Recipe [image = " + image + ", servings = " + servings + ", name = " + name + ", ingredients = " + ingredients + ", id = " + id + ", steps = " + steps + "]";
    }
}
