package udacity.project.com.bakingapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.adapters.IngredientsAdapter;
import udacity.project.com.bakingapp.database.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsFragment extends Fragment {

    interface Arguments {
        String INGREDIENTS = "INGREDIENTS";
    }

    private List<Ingredient> mIngredients = new ArrayList<>();
    private RecyclerView mIngredientsListView;

    public static RecipeIngredientsFragment newInstance(ArrayList<Ingredient> ingredients) {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.INGREDIENTS, ingredients);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredients = (List<Ingredient>) getArguments().getSerializable(Arguments.INGREDIENTS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIngredientsListView = view.findViewById(R.id.ingredients_list);
        mIngredientsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setIngredientsAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    private void setTitle() {
        getActivity().setTitle(R.string.fragment_ingredients_title);
    }

    private void setIngredientsAdapter() {
        IngredientsAdapter recipeAdapter = new IngredientsAdapter(mIngredients);
        mIngredientsListView.setAdapter(recipeAdapter);
    }
}
