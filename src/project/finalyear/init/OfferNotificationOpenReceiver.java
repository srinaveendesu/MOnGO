package project.finalyear.init;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.plotprojects.retail.android.FilterableNotification;

public class OfferNotificationOpenReceiver extends BroadcastReceiver {

    private static String LOG_TAG = "Mela ON GO/CouponNotificationOpenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        FilterableNotification notification = intent.getParcelableExtra("notification");
        Log.d(LOG_TAG, "Opening notification: " + notification.toString());
        Intent openIntent = new Intent(context, ViewOfferActivity.class);
        openIntent.setAction(ViewOfferActivity.ACTION);
        openIntent.putExtras(intent.getExtras());
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(openIntent);
    }
}
