package udacity.project.com.bakingapp.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import udacity.project.com.bakingapp.database.AppDatabase;
import udacity.project.com.bakingapp.database.Recipe;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Recipe>> mListOfRecipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mListOfRecipes = appDatabase.recipeDao().loadAllRecipes();
    }

    public LiveData<List<Recipe>> getListOfRecipes() {
        return mListOfRecipes;
    }
}
