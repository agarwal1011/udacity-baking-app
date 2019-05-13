package udacity.project.com.bakingapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.utils.BakingUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes = new ArrayList<>();
    private OnRecipeItemClickedListener mRecipeItemClickedListener;

    public RecipeAdapter(OnRecipeItemClickedListener recipeItemClickedListener, List<Recipe> recipeList) {
        this.mRecipeItemClickedListener = recipeItemClickedListener;
        mRecipes.clear();
        mRecipes.addAll(recipeList);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {
        Recipe recipe = mRecipes.get(position);
        if (recipe != null) {
            BakingUtil.loadImageIntoImageView((Activity) mRecipeItemClickedListener, recipe.getImage(), holder.mRecipeImage);
            holder.mRecipeName.setText(recipe.getName());
            holder.mRecipeServingsSize.setText(((Activity) mRecipeItemClickedListener).getString(R.string.servings_size, recipe.getServings()));
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mRecipeImage;
        private TextView mRecipeName;
        private TextView mRecipeServingsSize;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeImage = itemView.findViewById(R.id.recipe_thumbnail);
            mRecipeName = itemView.findViewById(R.id.recipe_name_tv);
            mRecipeServingsSize = itemView.findViewById(R.id.recipe_servings_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeItemClickedListener.onRecipeClicked(getAdapterPosition());
        }
    }

    public interface OnRecipeItemClickedListener {
        void onRecipeClicked(int position);
    }
}
