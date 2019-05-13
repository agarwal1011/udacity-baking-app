package udacity.project.com.bakingapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeVideoStepAdapter extends RecyclerView.Adapter<RecipeVideoStepAdapter.RecipeStepViewHolder> {

    private Activity mActivity;
    private List<Step> mSteps = new ArrayList<>();
    private int mCurrentClickedItemPosition = -1;
    private OnRecipeStepClickedListener mRecipeItemClickedListener;

    public RecipeVideoStepAdapter(Activity activity, OnRecipeStepClickedListener recipeItemClickedListener, List<Step> stepsList) {
        mActivity = activity;
        this.mRecipeItemClickedListener = recipeItemClickedListener;
        mSteps.clear();
        mSteps.addAll(stepsList);
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeStepViewHolder holder, final int position) {
        Step step = mSteps.get(position);
        if (step != null) {
            holder.mRecipeShortDescription.setText(step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRecipeShortDescription;

        public RecipeStepViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeShortDescription = itemView.findViewById(R.id.recipe_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < getItemCount(); i++) {
                if (i == getAdapterPosition()) {
                    v.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
                } else {
                    v.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
                }
            }
            mRecipeItemClickedListener.onRecipeStepClicked(getAdapterPosition());
        }
    }

    public interface OnRecipeStepClickedListener {
        void onRecipeStepClicked(int position);
    }
}
