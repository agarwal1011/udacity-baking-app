package udacity.project.com.bakingapp.utils;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import udacity.project.com.bakingapp.database.Ingredient;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.database.Step;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS_OBJECT = "ingredients";
    private static final String RECIPE_STEPS_OBJECT = "steps";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";
    //Ingredient
    private static final String RECIPE_INGREDIENTS_QUANTITY = "quantity";
    private static final String RECIPE_INGREDIENTS_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    //Step
    private static final String RECIPE_STEPS_ID = "id";
    private static final String RECIPE_STEPS_SHORT_DESCRIPTION = "shortDescription";
    private static final String RECIPE_STEPS_DESCRIPTION = "description";
    private static final String RECIPE_STEPS_VIDEO_URL = "videoURL";
    private static final String RECIPE_STEPS_THUMBNAIL_URL = "thumbnailURL";

    public static List<Recipe> getRecipeList(String recipeListJson) throws JSONException {
        if (TextUtils.isEmpty(recipeListJson)) {
            return null;
        }
        List<Recipe> recipeList = new ArrayList();
        JSONArray listOfRecipes = new JSONArray(recipeListJson);
        if (listOfRecipes != null) {
            for (int i = 0; i < listOfRecipes.length(); i++) {
                JSONObject recipeObject = listOfRecipes.getJSONObject(i);
                if (recipeObject != null) {
                    Recipe recipe = new Recipe();
                    recipe.setId(recipeObject.getString(RECIPE_ID));
                    recipe.setName(recipeObject.getString(RECIPE_NAME));
                    recipe.setServings(recipeObject.getString(RECIPE_SERVINGS));
                    recipe.setImage(recipeObject.getString(RECIPE_IMAGE));
                    recipe.setIngredients(getIngredients(recipeObject.getJSONArray(RECIPE_INGREDIENTS_OBJECT)));
                    recipe.setSteps(getSteps(recipeObject.getJSONArray(RECIPE_STEPS_OBJECT)));
                    recipeList.add(recipe);
                }
            }
        }
        return recipeList;
    }

    private static ArrayList<Ingredient> getIngredients(JSONArray ingredientsJsonArray) throws JSONException {
        if (ingredientsJsonArray == null) {
            return null;
        }
        ArrayList<Ingredient> ingredientList = new ArrayList();
        for (int i = 0; i < ingredientsJsonArray.length(); i++) {
            JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(i);
            if (ingredientJsonObject != null) {
                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(ingredientJsonObject.getString(RECIPE_INGREDIENTS_QUANTITY));
                ingredient.setMeasure(ingredientJsonObject.getString(RECIPE_INGREDIENTS_MEASURE));
                ingredient.setIngredient(ingredientJsonObject.getString(RECIPE_INGREDIENT));
                ingredientList.add(ingredient);
            }
        }
        return ingredientList;
    }

    private static ArrayList<Step> getSteps(JSONArray stepsJsonArray) throws JSONException {
        if (stepsJsonArray == null) {
            return null;
        }
        ArrayList<Step> stepList = new ArrayList();
        for (int i = 0; i < stepsJsonArray.length(); i++) {
            JSONObject stepJsonObject = stepsJsonArray.getJSONObject(i);
            if (stepJsonObject != null) {
                Step step = new Step();
                step.setId(stepJsonObject.getString(RECIPE_STEPS_ID));
                step.setDescription(stepJsonObject.getString(RECIPE_STEPS_DESCRIPTION));
                step.setShortDescription(stepJsonObject.getString(RECIPE_STEPS_SHORT_DESCRIPTION));
                step.setVideoURL(stepJsonObject.getString(RECIPE_STEPS_VIDEO_URL));
                step.setThumbnailURL(stepJsonObject.getString(RECIPE_STEPS_THUMBNAIL_URL));
                stepList.add(step);
            }
        }
        return stepList;
    }
}
