package udacity.project.com.bakingapp.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Ingredient> stringToIngrdients(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        List<Ingredient> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String ingredientsToString(List<Ingredient> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public static List<Step> stringToSteps(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {
        }.getType();
        List<Step> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String StepsToString(List<Step> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}