package project.finalyear.retailer;

import java.util.Calendar;

import com.plotprojects.retail.android.Plot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import project.finalyear.database.RetailerItem;
import project.finalyear.database.RetailerItemDataSource;
import project.finalyear.init.OfferService;
import project.finalyear.init.Settings;
import project.finalyear.mongo.R;
import com.plotprojects.retail.android.Plot;
import com.plotprojects.retail.android.PlotConfiguration;

@TargetApi(9)
public class EditItemActivity extends Activity {
	
	final static Object lock = new Object();
	private LatLongService latlongservice;
	final static String LOG_TAG = "Mela ON GO/EditItemActivity";
    private RetailerItemDataSource itemdatasource;
    final static String PLOT_ACCOUNT_ID = "srinaveen.desu@gmail.com";
    final static String PLOT_PUBLIC_KEY = "sFEdj8oeD10WNi9R";
    final static String PLOT_PRIVATE_KEY = "SyW7oKTV1Iu8DsPs";
    private long userId = 0;
    private long itemId = 0;
    private boolean checked;
    
    private static PlotConfiguration plotConfiguration = new PlotConfiguration(PLOT_PUBLIC_KEY);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plot.init(getApplicationContext(), plotConfiguration);
        latlongservice = new LatLongService(PLOT_ACCOUNT_ID, PLOT_PRIVATE_KEY);
     //   settings = new Settings(getApplicationContext());
        setTitle("Offer Form");
        setContentView(R.layout.activity_retailer_edit_item);
        userId = getIntent().getLongExtra("project.finalyear.retailer.userid",
                0);
        itemId = getIntent().getLongExtra("project.finalyear.retailer.itemid",
                0);
        itemdatasource = new RetailerItemDataSource(this);
        final EditText itemRetailerNameEditText = (EditText) findViewById(R.id.retailer_name_edit_item_editText);
        final EditText itemProductEditText = (EditText) findViewById(R.id.retailer_product_edit_item_editText);
        final EditText itemLatitudeEditText = (EditText) findViewById(R.id.retailer_latitude_edit_item_editText);
        final EditText itemLongitudeEditText = (EditText) findViewById(R.id.retailer_longitude_edit_item_editText);        
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.retailer_date_edit_item_checkBox);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.retailer_calender_edit_item_datePicker);
        final SeekBar discountBar = (SeekBar) findViewById(R.id.retailer_discount_edit_item_seekBar);
        
        final TextView discountValue = (TextView)findViewById(R.id.discountvalue);
        discountBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

        	   @Override 
        	   public void onProgressChanged(SeekBar seekBar, int progress, 
        	     boolean fromUser) { 
        	    // TODO Auto-generated method stub 
        		   discountValue.setText(String.valueOf(progress)/*+ "/" + seekBar.getMax()*/); 
        	   } 

        	   @Override 
        	   public void onStartTrackingTouch(SeekBar seekBar) { 
        	    // TODO Auto-generated method stub 
        	   } 

        	   @Override 
        	   public void onStopTrackingTouch(SeekBar seekBar) { 
        	    // TODO Auto-generated method stub 
        	   } 
        	       });
        
        if (itemId == 0) {
            this.setTitle("Add New Offer");
            setDueTimeCheckBox.setChecked(false);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 2);
            dueDatePicker.updateDate(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            discountBar.setProgress(0);
            dueDatePicker.setVisibility(View.GONE);
        } else {
            this.setTitle("Edit Offer");
            RetailerItem item = itemdatasource.getItemByItemId(itemId);
            if (item == null) {
                Toast toast = Toast.makeText(this, "Offer does not exist.",
                        Toast.LENGTH_LONG);
                toast.show();
                finish();
                this.setResult(RESULT_CANCELED);
                return;
            }
            itemRetailerNameEditText.setText(item.getRetailername());
            itemProductEditText.setText(item.getProduct());
            itemLatitudeEditText.setText(item.getLatitude());
            itemLongitudeEditText.setText(item.getLongitude());
            setDueTimeCheckBox.setChecked(!item.isNoDueTime());
            checked = item.isChecked();
            // finishCheckBox.setChecked(item.isChecked());
            Calendar cal = Calendar.getInstance();
            if (item.isNoDueTime()) {
                cal.add(Calendar.DAY_OF_MONTH, 2);
            } else {
                cal.setTimeInMillis(item.getDueTime());
            }
            dueDatePicker.updateDate(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            discountBar.setProgress((int) item.getDiscount());
            if (item.isNoDueTime()) {
                dueDatePicker.setVisibility(View.GONE);
            } else {
                dueDatePicker.setVisibility(View.VISIBLE);
            }
        }
        setDueTimeCheckBox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (buttonView.isChecked()) {
                            dueDatePicker.setVisibility(View.VISIBLE);
                        } else {
                            dueDatePicker.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_retailer_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void createlatlong(View button){
    	
    	final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Saving");
        progress.setMessage("Wait while saving...");
        progress.show();
        final double latitude_double = Double.parseDouble(((EditText)findViewById(R.id.retailer_latitude_edit_item_editText)).getText().toString()) ;
        final double longitude_double = Double.parseDouble(((EditText)findViewById(R.id.retailer_longitude_edit_item_editText)).getText().toString()); 
         
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
                 Coupon coupon = new Coupon(loc, params[0], params[1]);
                 try {
                     String placeId = latlongservice.createPlace(latitude_double, longitude_double);
                     String notificationId = latlongservice.createNotification(placeId, coupon.getNotification_Message(), coupon.getCategory());
                     return "Created offer notification with id " + notificationId;
                 //    return "Created offer notification with id " ;

                 } catch (Exception e) {
                     Log.e(LOG_TAG, "Error storing offer coupon", e);
                     return "Error setting location offer coupon"+latitude_double +longitude_double;
                 }

                 
             }

             protected void onPostExecute(String result) {
                 Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                 progress.hide();
             }
         };
         String message = "upto "+ ((SeekBar)findViewById(R.id.retailer_discount_edit_item_seekBar)).getProgress() + "% OFF on "
         				+((EditText)findViewById(R.id.retailer_product_edit_item_editText)).getText().toString() + " "
         				+ " at " + ((EditText)findViewById(R.id.retailer_name_edit_item_editText)).getText().toString();
         
         String category = ((EditText)findViewById(R.id.retailer_product_edit_item_editText)).getText().toString();
         task.execute(message, category);
    	
    	
    }

    public void onSaveClick(View view) {
    	
    	final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Saving");
        progress.setMessage("Wait while saving...");
        progress.show();
        final double latitude_double = Double.parseDouble(((EditText)findViewById(R.id.retailer_latitude_edit_item_editText)).getText().toString()) ;
        final double longitude_double = Double.parseDouble(((EditText)findViewById(R.id.retailer_longitude_edit_item_editText)).getText().toString()); 
         
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
                 Coupon coupon = new Coupon(loc, params[0], params[1]);
                 try {
                     String placeId = latlongservice.createPlace(latitude_double, longitude_double);
                     String notificationId = latlongservice.createNotification(placeId, coupon.getNotification_Message(), coupon.getCategory());
                     return "Created offer notification with id " + notificationId;
                 //    return "Created offer notification with id " ;

                 } catch (Exception e) {
                     Log.e(LOG_TAG, "Error storing offer coupon", e);
                     return "Error setting location offer coupon"+latitude_double +longitude_double;
                 }

                 
             }

             protected void onPostExecute(String result) {
                 Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                 progress.hide();
             }
         };
         String message = "upto "+ ((SeekBar)findViewById(R.id.retailer_discount_edit_item_seekBar)).getProgress() + "% OFF on "
         				+((EditText)findViewById(R.id.retailer_product_edit_item_editText)).getText().toString() + " "
         				+ " at " + ((EditText)findViewById(R.id.retailer_name_edit_item_editText)).getText().toString();
         
         String category = ((EditText)findViewById(R.id.retailer_product_edit_item_editText)).getText().toString();
         task.execute(message, category);
    	
    	
         
         
         
         
    	
    	
        final EditText itemRetailerNameEditText = (EditText) findViewById(R.id.retailer_name_edit_item_editText);
        final EditText itemProductEditText = (EditText) findViewById(R.id.retailer_product_edit_item_editText);
        final EditText itemLatitudeEditText = (EditText) findViewById(R.id.retailer_latitude_edit_item_editText);
        final EditText itemLongitudeEditText = (EditText) findViewById(R.id.retailer_longitude_edit_item_editText);
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.retailer_date_edit_item_checkBox);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.retailer_calender_edit_item_datePicker);
        final SeekBar discountBar = (SeekBar) findViewById(R.id.retailer_discount_edit_item_seekBar);
        
        
        
        // save data here
        if (itemRetailerNameEditText.getText().toString().isEmpty()) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Offer Creation Error");
            alertDialog.setMessage("Retailer name cannot be empty.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        if (itemId == 0) {
            // Add item
            String name = itemRetailerNameEditText.getText().toString();
            if (name.isEmpty()) {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Offer Creation Error");
                alertDialog.setMessage("Retailer name cannot be empty.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            String product = itemProductEditText.getText().toString();
            String latitude = itemLatitudeEditText.getText().toString();
            String longitude = itemLongitudeEditText.getText().toString();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Offer Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            long duetime = cal.getTimeInMillis();
            RetailerItem item = itemdatasource.createItem(userId, name, product,latitude,longitude,
                    duetime, !setDueTimeCheckBox.isChecked(), false,
                    discountBar.getProgress());                              
            
            
            
            if (item != null) {
                Toast toast = Toast.makeText(this, "Offer created.",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            // Update item
            RetailerItem item = new RetailerItem();
            item.setRetailername(itemRetailerNameEditText.getText().toString());
            item.setProduct(itemProductEditText.getText().toString());
            item.setLatitude(itemLatitudeEditText.getText().toString());
            item.setLongitude(itemLongitudeEditText.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Offer Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            item.setDueTime(cal.getTimeInMillis());
            item.setId(itemId);
            item.setUserId(userId);
            item.setNoDueTime(!setDueTimeCheckBox.isChecked());
            item.setChecked(checked);
            item.setDiscount(discountBar.getProgress());
            itemdatasource.updateItem(item);
            Toast toast = Toast.makeText(this, "Offer updated.",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        this.setResult(RESULT_OK);
        this.finish();
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

    public void onCancelClick(View view) {
        Toast toast = Toast
                .makeText(this, "Cancel Editing.", Toast.LENGTH_LONG);
        toast.show();
        this.setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        final EditText itemRetailerNameEditText = (EditText) findViewById(R.id.retailer_name_edit_item_editText);
        final EditText itemProductEditText = (EditText) findViewById(R.id.retailer_product_edit_item_editText);
        final EditText itemLatitudeEditText = (EditText) findViewById(R.id.retailer_latitude_edit_item_editText);
        final EditText itemLongitudeEditText = (EditText) findViewById(R.id.retailer_longitude_edit_item_editText);
        final CheckBox setDueTimeCheckBox = (CheckBox) findViewById(R.id.retailer_date_edit_item_checkBox);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.retailer_calender_edit_item_datePicker);
        final SeekBar discountBar = (SeekBar) findViewById(R.id.retailer_discount_edit_item_seekBar);
 
        	   
        // save data here
        if (itemId == 0) {
            // Add item
            String name = itemRetailerNameEditText.getText().toString();
            String product = itemProductEditText.getText().toString();
            String latitude = itemLatitudeEditText.getText().toString();
            String longitude = itemLongitudeEditText.getText().toString();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Offer Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            long duetime = cal.getTimeInMillis();
            RetailerItem item = itemdatasource.createItem(userId, name, product,latitude,longitude,
                    duetime, !setDueTimeCheckBox.isChecked(), false,
                    discountBar.getProgress());
            if (item != null) {
                Toast toast = Toast.makeText(this, "Offer created." + userId,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            // Update item
            RetailerItem item = new RetailerItem();
            item.setRetailername(itemRetailerNameEditText.getText().toString());
            item.setProduct(itemProductEditText.getText().toString());
            item.setLatitude(itemLatitudeEditText.getText().toString());
            item.setLongitude(itemLongitudeEditText.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 1);
            cal.set(dueDatePicker.getYear(), dueDatePicker.getMonth(),
                    dueDatePicker.getDayOfMonth(), 0, 0, 0);
            if (setDueTimeCheckBox.isChecked()) {
                Calendar nowcal = Calendar.getInstance();
                nowcal.set(Calendar.MILLISECOND, 0);
                nowcal.set(Calendar.SECOND, 0);
                nowcal.set(Calendar.MINUTE, 0);
                nowcal.set(Calendar.HOUR_OF_DAY, 0);
                if (nowcal.after(cal)) {
                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Create Offer Error");
                    alertDialog
                            .setMessage("Due date cannot be earlier than today.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            }
            item.setDueTime(cal.getTimeInMillis());
            item.setId(itemId);
            item.setUserId(userId);
            item.setNoDueTime(!setDueTimeCheckBox.isChecked());
            item.setChecked(checked);
            item.setDiscount(discountBar.getProgress());
            itemdatasource.updateItem(item);
            Toast toast = Toast.makeText(this, "Offer updated.",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        this.setResult(RESULT_OK);
        this.finish();
        super.onBackPressed();
    }
}


