package udacity.project.com.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients = new ArrayList<>();

    public IngredientsAdapter(List<Ingredient> ingredients) {
        mIngredients.clear();
        mIngredients.addAll(ingredients);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, final int position) {
        Ingredient ingredient = mIngredients.get(position);
        if (ingredient != null) {
            holder.mIngredientName.setText(ingredient.getIngredient());
            holder.mIngredientQuantity.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
        }
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientQuantity;
        private TextView mIngredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngredientName = itemView.findViewById(R.id.ingredient_name_tv);
            mIngredientQuantity = itemView.findViewById(R.id.ingredient_quantity_tv);
        }
    }
}