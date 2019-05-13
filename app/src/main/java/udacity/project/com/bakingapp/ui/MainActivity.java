package udacity.project.com.bakingapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import udacity.project.com.bakingapp.AppExecutors;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.adapters.RecipeAdapter;
import udacity.project.com.bakingapp.database.AppDatabase;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.utils.BakingUtil;
import udacity.project.com.bakingapp.utils.JsonUtil;
import udacity.project.com.bakingapp.utils.NetworkUtil;
import udacity.project.com.bakingapp.widget.WidgetRecipeUpdateService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeItemClickedListener {

    private ProgressBar mProgressBar;
    private RecyclerView mRecipeListView;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mRecipeListView = findViewById(R.id.recipe_list);
        if (BakingUtil.isTablet(this)) {
            mRecipeListView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecipeListView.setLayoutManager(new LinearLayoutManager(this));
        }
        if (savedInstanceState != null) {
            LiveData<List<Recipe>> recipes = AppDatabase.getInstance(getApplicationContext()).recipeDao().loadAllRecipes();
            if (recipes != null) {
                mRecipes = recipes.getValue();
                setRecipeAdapter();
            } else {
                fetchRecipes();
            }
        } else {
            fetchRecipes();
        }
        setupViewModel();
    }

    private void fetchRecipes() {
        new FetchRecipesAsyncTask().execute();
    }

    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getListOfRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipes = recipes;
                setRecipeAdapter();
                //WidgetRecipeUpdateService.startActionUpdateRecipeWidgets(getApplicationContext());
            }
        });
    }

    private void setRecipeAdapter() {
        if (mRecipes != null && !mRecipes.isEmpty()) {
            RecipeAdapter recipeAdapter = new RecipeAdapter(this, mRecipes);
            mRecipeListView.setAdapter(recipeAdapter);
        }
    }

    private void showErrorDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.error_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void updateVisibility(boolean loadingDone) {
        mProgressBar.setVisibility(loadingDone ? View.GONE : View.VISIBLE);
        mRecipeListView.setVisibility(loadingDone ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClicked(int position) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, mRecipes.get(position));
        startActivity(intent);
    }

    class FetchRecipesAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            updateVisibility(false);
        }

        @Override
        protected void onPostExecute(String s) {
            updateVisibility(true);
            try {
                mRecipes = JsonUtil.getRecipeList(s);
                if (mRecipes == null || mRecipes.size() == 0) {
                    showErrorDialog();
                    return;
                }
                setRecipeAdapter();
                addRecipesToDatabase(mRecipes);
            } catch (JSONException e) {
                showErrorDialog();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                result = NetworkUtil.getRecipeList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private void addRecipesToDatabase(@NonNull final List<Recipe> recipes) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getApplicationContext()).recipeDao().deleteAllRecipes();
                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        if (recipe != null) {
                            AppDatabase.getInstance(getApplicationContext()).recipeDao().insertRecipe(recipes.get(i));
                        }
                    }
                }
            });
        }
    }
}
