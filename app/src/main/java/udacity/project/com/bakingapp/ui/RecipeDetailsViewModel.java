package udacity.project.com.bakingapp.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import udacity.project.com.bakingapp.database.Ingredient;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.database.Step;

import java.util.ArrayList;

public class RecipeDetailsViewModel extends ViewModel {

    private Fragment mCurrentFragment;
    private Recipe mRecipe;
    private Step mCurrentStep;
    private ArrayList<Ingredient> mIngredients;
    private int mCurrentStepIndex;

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.mCurrentFragment = currentFragment;
    }

    public Step getCurrentStep() {
        return mCurrentStep;
    }

    public void setCurrentStep(Step currentStep) {
        this.mCurrentStep = currentStep;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public int getCurrentStepIndex() {
        return mCurrentStepIndex;
    }

    public void setCurrentStepIndex(int currentStepIndex) {
        this.mCurrentStepIndex = currentStepIndex;
    }
}
