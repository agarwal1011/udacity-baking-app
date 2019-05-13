package udacity.project.com.bakingapp.database;

import java.io.Serializable;

public class Ingredient implements Serializable {

    public Ingredient() {
    }

    private String quantity;

    private String measure;

    private String ingredient;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Ingredient [quantity = " + quantity + ", measure = " + measure + ", ingredient = " + ingredient + "]";
    }
}