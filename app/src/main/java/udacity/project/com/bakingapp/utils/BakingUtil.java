package udacity.project.com.bakingapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import udacity.project.com.bakingapp.R;

public class BakingUtil {

    public static void loadImageIntoImageView(Context context, String url, ImageView imageView) {
        if (context == null || TextUtils.isEmpty(url) || imageView == null) {
            return;
        }
        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.drawable_default_image)
                .error(R.mipmap.drawable_default_image)
                .into(imageView);
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
