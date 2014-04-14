package project.finalyear.init;

import project.finalyear.mongo.TodoMainActivity;
import project.finalyear.mongo.R;
import project.finalyear.retailer.RetailerMainActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.plotprojects.retail.android.Plot;
import com.plotprojects.retail.android.PlotConfiguration;

public class EntryActivity extends Activity
{
    final static Object lock = new Object();
    final static String LOG_TAG = "Mela ON GO/EntryActivity";
    final static String PLOT_ACCOUNT_ID = "srinaveen.desu@gmail.com";
    final static String PLOT_PUBLIC_KEY = "sFEdj8oeD10WNi9R";
    final static String PLOT_PRIVATE_KEY = "SyW7oKTV1Iu8DsPs";
    public String resp;
    private EditText lat_edit;
    private EditText lng_edit;
    private Button cordinate_button;
    private EditText address_edit;
    private EditText location_address_message;

    private static PlotConfiguration plotConfiguration = new PlotConfiguration(PLOT_PUBLIC_KEY);

    private OfferService offerService;
    private Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Plot.init(getApplicationContext(), plotConfiguration);
        offerService = new OfferService(PLOT_ACCOUNT_ID, PLOT_PRIVATE_KEY);
        settings = new Settings(getApplicationContext());
        setTitle("Mela On Go");
        setContentView(R.layout.activity_tab_init);
        // Create tabs
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("entry").setIndicator("entry").setContent(R.id.entry_tab));
        tabHost.addTab(tabHost.newTabSpec("categories").setIndicator("Categories").setContent(R.id.categories_tab));
        tabHost.addTab(tabHost.newTabSpec("coupons").setIndicator("Coupons").setContent(R.id.coupons_tab));
        //initialize
        cordinate_button = (Button) findViewById(R.id.generateCoordinateButton);
        address_edit =((EditText)findViewById(R.id.init_address_editText));
        location_address_message = ((EditText)findViewById(R.id.message));
        lat_edit = ((EditText)findViewById(R.id.init_lat_editText));
        lng_edit = ((EditText)findViewById(R.id.init_long_editText));
        cordinate_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncTaskRunner runner = new AsyncTaskRunner();
				String locationAddress = address_edit.getText().toString();
				runner.execute(locationAddress);
			}
		});
        // Restore state
        restoreState();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState();
    }
    public void createRetailer(View button){
    	button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent launchactivity= new Intent(EntryActivity.this,RetailerMainActivity.class);                               
				 startActivity(launchactivity); 
			}
		});
    	
    }
    public void createTodo(View button){
    	button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent launchactivity= new Intent(EntryActivity.this,TodoMainActivity.class);                               
				 startActivity(launchactivity); 
			}
		});
    	
    }

    public void createCoupon(View button) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Saving");
        progress.setMessage("Wait while saving...");
        progress.show();
        
        final double lat = Double.parseDouble(((EditText)findViewById(R.id.init_lat_editText)).getText().toString());
        final double lng = Double.parseDouble(((EditText)findViewById(R.id.init_long_editText)).getText().toString());

        
        if(lat>0 && lng>0){
        	
        	AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    Location loc = getLocation();
                    if (loc == null) {

                        return "Unable to get current location";
                    }
                    if (params[0].trim().isEmpty()) {
                        return "No message entered";
                    }
                    Offer coupon = new Offer(loc, params[0], params[1]);
                    try {
                        String placeId = offerService.createPlace(lat,lng);
                        String notificationId = offerService.createNotification(placeId, coupon.getNotification_Message(), coupon.getCategory());
                        return "Created coupon notification with id " + notificationId;

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error storing coupon", e);
                        return "Error storing coupon";
                    }
                }

                protected void onPostExecute(String result) {
                    Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                    progress.hide();
                }
            };
            String message = ((EditText)findViewById(R.id.message)).getText().toString();
            String category = ((Spinner)findViewById(R.id.cat)).getSelectedItem().toString();
            task.execute(message, category);
        	
        }
        else{
        	
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Location loc = getLocation();
                if (loc == null) {

                    return "Unable to get current location";
                }
                if (params[0].trim().isEmpty()) {
                    return "No message entered";
                }
                Offer coupon = new Offer(loc, params[0], params[1]);
                try {
                    String placeId = offerService.createPlace(loc.getLatitude(),loc.getLongitude());
                    String notificationId = offerService.createNotification(placeId, coupon.getNotification_Message(), coupon.getCategory());
                    return "Created coupon notification with id " + notificationId;

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error storing coupon", e);
                    return "Error storing coupon";
                }
            }

            protected void onPostExecute(String result) {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                progress.hide();
            }
        };
        String message = ((EditText)findViewById(R.id.message)).getText().toString();
        String category = ((Spinner)findViewById(R.id.cat)).getSelectedItem().toString();
        task.execute(message, category);
        
        }
    }

    private void restoreState() {
        Log.d(LOG_TAG, "restoreState");
        ((ToggleButton)findViewById(R.id.footwearButton)).setChecked(settings.getBoolean(Settings.FOOTWEAR_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.Clothing)).setChecked(settings.getBoolean(Settings.CLOTHING_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.electronicsButton)).setChecked(settings.getBoolean(Settings.ELECTRONICS_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.accessoriesButton)).setChecked(settings.getBoolean(Settings.ACCESSORIES_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.watchesButton)).setChecked(settings.getBoolean(Settings.WATCHES_PREFERENCE, false));
        ((ToggleButton)findViewById(R.id.generalButton)).setChecked(settings.getBoolean(Settings.GENERAL_PREFERENCE, false));
    }

    private void saveState() {
        Log.d(LOG_TAG, "saveState");
        settings.setBoolean(Settings.FOOTWEAR_PREFERENCE, ((ToggleButton) findViewById(R.id.footwearButton)).isChecked());
        settings.setBoolean(Settings.CLOTHING_PREFERENCE, ((ToggleButton) findViewById(R.id.Clothing)).isChecked());
        settings.setBoolean(Settings.ELECTRONICS_PREFERENCE, ((ToggleButton) findViewById(R.id.electronicsButton)).isChecked());
        settings.setBoolean(Settings.ACCESSORIES_PREFERENCE, ((ToggleButton) findViewById(R.id.accessoriesButton)).isChecked());
        settings.setBoolean(Settings.WATCHES_PREFERENCE, ((ToggleButton) findViewById(R.id.watchesButton)).isChecked());
        settings.setBoolean(Settings.GENERAL_PREFERENCE, ((ToggleButton) findViewById(R.id.generalButton)).isChecked());
    }

    private Location getLocation() {
        synchronized(lock) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Location networkLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (networkLocation != null) return networkLocation;
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		//private String resp;
		XMLHelper helper;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				// Do your long operations here and return the result
		
				String test = params[0];
				String delims = " ";
				String result="+",addr;
				addr = test.replaceAll(delims,result);
				resp = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + addr + "&sensor=false";
				helper = new XMLHelper();
				helper.get(resp);
			}
			catch (Exception e) {
				e.printStackTrace();
				resp = e.getMessage();
				return " error fetching place";
			}
			return resp;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
		//	finalResult.setText(result);
			StringBuilder builder = new StringBuilder();
			StringBuilder latitude= new StringBuilder();
			StringBuilder longitude= new StringBuilder();
			for (XmlValue post : helper.posts) {
			//	builder.append("\nstatus: " + post.getstatus());
				builder.append("location: " + post.getLocation());
			}
			for (XmlValue post : helper.posts) {			
				latitude.append(post.getLat());
			}
			for (XmlValue post : helper.posts) {			
				longitude.append(post.getLng());
			}
			//location_address_message.setText(builder.toString());
			lat_edit.setText(latitude.toString());
			lng_edit.setText(longitude.toString());
		}

		/*
		
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
		//	finalResult.setText(text[0]);
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}

}
