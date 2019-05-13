package udacity.project.com.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.adapters.RecipeVideoStepAdapter;
import udacity.project.com.bakingapp.database.Ingredient;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.database.Step;

import java.util.ArrayList;

public class RecipeDetailsFragment extends Fragment implements RecipeVideoStepAdapter.OnRecipeStepClickedListener {

    interface Arguments {
        String RECIPE = "RECIPE";
    }

    public interface Callbacks {
        void onIngredientsClicked(ArrayList<Ingredient> ingredients);

        void onStepClicked(Step step);
    }

    private Callbacks mCallback;

    private Button mIngredientsBtn;
    private RecyclerView mStepsList;
    private Recipe mRecipe;

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.RECIPE, recipe);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe = (Recipe) getArguments().getSerializable(Arguments.RECIPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIngredientsBtn = view.findViewById(R.id.ingredients_btn);
        mStepsList = view.findViewById(R.id.steps_list);
        mStepsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStepsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        wireListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    private void setTitle() {
        getActivity().setTitle(mRecipe.getName());
    }

    private void wireListeners() {
        mIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onIngredientsClicked(new ArrayList<>(mRecipe.getIngredients()));
            }
        });
        RecipeVideoStepAdapter adapter = new RecipeVideoStepAdapter(getActivity(), this, mRecipe.getSteps());
        mStepsList.setAdapter(adapter);
    }

    @Override
    public void onRecipeStepClicked(int position) {
        mCallback.onStepClicked(mRecipe.getSteps().get(position));
    }
}
