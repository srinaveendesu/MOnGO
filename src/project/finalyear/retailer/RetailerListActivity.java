package project.finalyear.retailer;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import project.finalyear.database.DueDateComparator;
import project.finalyear.database.DiscountComparator;
import project.finalyear.database.RetailerItem;
import project.finalyear.database.RetailerItemDataSource;
import project.finalyear.mongo.R;


@TargetApi(9)
public class RetailerListActivity extends Activity {

	private RetailerItemDataSource itemdatasource;
	private long userId = 0;
	private static final int SORT_DUEDATE = 0;
	private static final int SORT_PRIORITY = 1;
	private int sortType = 0;
	private boolean hide = false;

	public List<RetailerItem> getNewList() {
		List<RetailerItem> newList = null;
		if (itemdatasource == null) {
			return null;
		}
		if (hide) {
			newList = itemdatasource.getInCompleteListByUId(userId);
		} else {
			newList = itemdatasource.getRetailerListByUId(userId);
		}
		if (sortType == SORT_DUEDATE) {
			Collections.sort(newList, new DueDateComparator());
		} else {
			Collections.sort(newList, new DiscountComparator());
		}
		return newList;
	}

	public boolean getHide() {
		return this.hide;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_retailer_list);
		userId = getIntent().getLongExtra("project.finalyear.retailer.userid",
				0);
		String userName = getIntent().getStringExtra(
				"project.finalyear.retailer.username");
		if (userId == 0) {
			finish();
			return;
		}
		this.setTitle("OFFER list for Retailer " + userName);
		hide = false;
		sortType = SORT_DUEDATE;
		itemdatasource = new RetailerItemDataSource(this);

		
		final List<RetailerItem> retailerList = getNewList();
		final CheckBox cb = (CheckBox) findViewById(R.id.retailer_offer_list_hide_checkBox);
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (buttonView.isChecked()) {
					hide = true;
				} else {
					hide = false;
				}
				List<RetailerItem> newList = getNewList();
				final ListView retailerOfferListView = (ListView) findViewById(R.id.retailer_offer_listView);
				RetailerListAdapter adapter = (RetailerListAdapter) retailerOfferListView
						.getAdapter();
				adapter.updateList(newList);
				adapter.notifyDataSetChanged();
			}
		});
		final ListView retailerOfferListView = (ListView) findViewById(R.id.retailer_offer_listView);
		ArrayAdapter<RetailerItem> adapter = new RetailerListAdapter(this, retailerList,
				itemdatasource);
		retailerOfferListView.setClickable(true);
		retailerOfferListView.setLongClickable(true);
		retailerOfferListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final RetailerItem currentItem = retailerList.get(position);
				PopupMenu popupMenu = new PopupMenu(RetailerListActivity.this, view);
				popupMenu.getMenuInflater().inflate(R.menu.retailer_list_popup_menu,
						popupMenu.getMenu());
				popupMenu
						.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								if (item.getItemId() == R.id.edit_menu) {
									// edit
									Intent myIntent = new Intent(
											RetailerListActivity.this,
											EditItemActivity.class);
									myIntent.putExtra(
											"project.finalyear.retailer.userid",
											userId);
									myIntent.putExtra(
											"project.finalyear.retailer.itemid",
											currentItem.getId());
									startActivityForResult(myIntent, 0);
								} else {
									// delete
									itemdatasource.deleteItem(currentItem);
									List<RetailerItem> retailerList = getNewList();
									final ListView retailerOfferListView = (ListView) findViewById(R.id.retailer_offer_listView);
									RetailerListAdapter adapter = (RetailerListAdapter) retailerOfferListView
											.getAdapter();
									adapter.updateList(retailerList);
									adapter.notifyDataSetChanged();
								}
								return true;
							}
						});
				popupMenu.show();
				return true;
			}
		});
		retailerOfferListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_retailer_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.menu_duedate:
			sortType = SORT_DUEDATE;
			List<RetailerItem> retailerListd = getNewList();
			final ListView retailerOfferListViewd = (ListView) findViewById(R.id.retailer_offer_listView);
			RetailerListAdapter adapterd = (RetailerListAdapter) retailerOfferListViewd
					.getAdapter();
			adapterd.updateList(retailerListd);
			adapterd.notifyDataSetChanged();
			return true;
		case R.id.menu_priority:
			sortType = SORT_PRIORITY;
			List<RetailerItem> retailerListp = getNewList();
			final ListView retailerOfferListViewp = (ListView) findViewById(R.id.retailer_offer_listView);
			RetailerListAdapter adapterp = (RetailerListAdapter) retailerOfferListViewp
					.getAdapter();
			adapterp.updateList(retailerListp);
			adapterp.notifyDataSetChanged();
			return true;
		case R.id.menu_changepwd:
			Intent myIntent = new Intent(this, AddUserActivity.class);
			myIntent.putExtra("project.finalyear.retailer.userid", userId);
			startActivityForResult(myIntent, 0);
			return true;
		case R.id.menu_logout:
			setResult(RESULT_OK);
			finish();
			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		this.setResult(RESULT_OK);
		this.finish();
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			List<RetailerItem> retailerList = getNewList();
			final ListView retailerOfferListView = (ListView) findViewById(R.id.retailer_offer_listView);
			RetailerListAdapter adapter = (RetailerListAdapter) retailerOfferListView
					.getAdapter();
			adapter.updateList(retailerList);
			adapter.notifyDataSetChanged();
		}
	}

	public void onAddItemClick(View view) {
		Intent myIntent = new Intent(view.getContext(), EditItemActivity.class);
		myIntent.putExtra("project.finalyear.retailer.userid", userId);
		startActivityForResult(myIntent, 0);
	}
}
