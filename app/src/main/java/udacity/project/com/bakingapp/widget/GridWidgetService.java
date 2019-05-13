package udacity.project.com.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.AppDatabase;
import udacity.project.com.bakingapp.database.Recipe;
import udacity.project.com.bakingapp.ui.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Recipe> recipeList = new ArrayList<>();

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    @Override
    public void onDataSetChanged() {
        recipeList = AppDatabase.getInstance(mContext).recipeDao().loadAllRecipes().getValue();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item);
        remoteViews.setTextViewText(R.id.recipe_name_tv, recipeList.get(position).getName());

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putSerializable(RecipeDetailsActivity.EXTRA_RECIPE, recipeList.get(position));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.recipe_name_tv, fillInIntent);

        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
