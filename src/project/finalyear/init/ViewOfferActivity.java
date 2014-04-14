package project.finalyear.init;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.plotprojects.retail.android.FilterableNotification;
import org.json.JSONObject;

import project.finalyear.mongo.R;

public class ViewOfferActivity extends Activity {

    public static final String ACTION = "com.plotprojects.retail.android.example.OPEN_NOTIFICATION";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.coupon);
        // Get notification from the intent
        FilterableNotification notification = getIntent().getParcelableExtra("notification");
        try {
            String category = new JSONObject(notification.getData()).getString("category");
            setTitle("Mela On Go: " + category);
        } catch (Exception e) {
            return;
        }
        setContentView(R.layout.notification_detail_coupon);
        ((TextView)findViewById(R.id.notificationMessageTextView)).setText(notification.getMessage());
    }
}