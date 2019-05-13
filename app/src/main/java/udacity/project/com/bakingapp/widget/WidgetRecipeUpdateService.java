package udacity.project.com.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import udacity.project.com.bakingapp.R;

public class WidgetRecipeUpdateService extends IntentService {

    private static Context mContext;

    public WidgetRecipeUpdateService() {
        super("WidgetRecipeUpdateService");
    }

    public static void startActionUpdateRecipeWidgets(Context context) {
        mContext = context;
        Intent intent = new Intent(context, WidgetRecipeUpdateService.class);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleActionUpdateRecipeWidgets();
        }
    }

    private void handleActionUpdateRecipeWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        BakingAppWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
    }
}
