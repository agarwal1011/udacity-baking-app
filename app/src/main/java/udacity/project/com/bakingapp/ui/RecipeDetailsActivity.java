package udacity.project.com.bakingapp.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.Ingredient;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.database.Step;
import udacity.project.com.bakingapp.utils.BakingUtil;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.Callbacks {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private RecipeDetailsViewModel mRecipeDetailsViewModel;
    private Recipe mRecipe;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BakingUtil.isTablet(this)) {
            setContentView(R.layout.activity_details_tablet);
        } else {
            setContentView(R.layout.activity_details);
        }

        mRecipeDetailsViewModel = ViewModelProviders.of(this).get(RecipeDetailsViewModel.class);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            mRecipe = (Recipe) extras.getSerializable(EXTRA_RECIPE);
            mRecipeDetailsViewModel.setRecipe(mRecipe);
            if (BakingUtil.isTablet(this)) {
                setUpDetailsPage();
            } else {
                launchRecipeDetailsFragment();
            }
        } else {
            launchFragmentShownBeforeActivityKilled(mRecipeDetailsViewModel.getCurrentFragment());
        }
    }

    private void setUpDetailsPage() {
        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(mRecipeDetailsViewModel.getRecipe());
        RecipeIngredientsFragment recipeIngredientsFragment = RecipeIngredientsFragment
                .newInstance(new ArrayList<>(mRecipeDetailsViewModel.getRecipe().getIngredients()));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_recipe_summary, recipeDetailsFragment)
                .commit();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_recipe_details, recipeIngredientsFragment)
                .commit();
    }

    private void launchFragmentShownBeforeActivityKilled(Fragment fragment) {
        if (BakingUtil.isTablet(this)) {
            setUpDetailsPage();
            return;
        }
    }

    @Override
    public void onIngredientsClicked(ArrayList<Ingredient> ingredients) {
        mRecipeDetailsViewModel.setIngredients(ingredients);
        launchIngredientsFragment();
    }

    @Override
    public void onStepClicked(Step step) {
        mRecipeDetailsViewModel.setCurrentStep(step);
        launchStepDetailsFragment();
    }

    private void setCurrentFragmentInViewModel() {
        mRecipeDetailsViewModel.setCurrentFragment(mCurrentFragment);
    }

    private void launchStepDetailsFragment() {
        mCurrentFragment = RecipeStepVideoFragment.newInstance(mRecipeDetailsViewModel.getRecipe().getSteps(),
                mRecipeDetailsViewModel.getCurrentStep());
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (BakingUtil.isTablet(this)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_recipe_details, mCurrentFragment)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment, mCurrentFragment)
                    .addToBackStack(null)
                    .commit();
        }
        setCurrentFragmentInViewModel();
    }

    private void launchRecipeDetailsFragment() {
        mCurrentFragment = RecipeDetailsFragment.newInstance(mRecipeDetailsViewModel.getRecipe());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.details_fragment, mCurrentFragment)
                .commit();

        setCurrentFragmentInViewModel();
    }

    private void launchIngredientsFragment() {
        mCurrentFragment = RecipeIngredientsFragment.newInstance(new ArrayList<>(mRecipeDetailsViewModel.getRecipe().getIngredients()));
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (BakingUtil.isTablet(this)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_recipe_details, mCurrentFragment)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment, mCurrentFragment)
                    .addToBackStack(null)
                    .commit();
        }
        setCurrentFragmentInViewModel();
    }
}
