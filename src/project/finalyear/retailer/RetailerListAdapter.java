package project.finalyear.retailer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import project.finalyear.database.RetailerItem;
import project.finalyear.database.RetailerItemDataSource;
import project.finalyear.mongo.R;

public class RetailerListAdapter extends ArrayAdapter<RetailerItem> {

	private final List<RetailerItem> list;
	private final Activity context;
	private RetailerItemDataSource itemdatasource;

	public RetailerListAdapter(Activity context, List<RetailerItem> list,
			RetailerItemDataSource ds) {
		super(context, R.layout.retailer_row_layout, list);
		this.context = context;
		this.list = list;
		this.itemdatasource = ds;
	}

	public void updateList(List<RetailerItem> newlist) {
		list.clear();
		list.addAll(newlist);
	}

	static class ViewHolder {

		protected ImageView image;
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.retailer_row_layout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) view
					.findViewById(R.id.retailer_row_layout_priorityImage);
			viewHolder.text = (TextView) view.findViewById(R.id.retailer_name);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.retailer_row_layout_offer_checkBox);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							RetailerItem item = (RetailerItem) viewHolder.checkbox
									.getTag();
							item.setChecked(buttonView.isChecked());
							// update database
							if (itemdatasource.getCompletedStatus(item.getId()) != isChecked) {
								itemdatasource.updateItem(item);
							}
							// if we choose hide mode, we need to update the
							// list
							if (buttonView.isChecked()
									&& ((RetailerListActivity) context).getHide()) {
								List<RetailerItem> newList = ((RetailerListActivity) context)
										.getNewList();
								RetailerListAdapter.this.updateList(newList);
								RetailerListAdapter.this.notifyDataSetChanged();
							}
						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		RetailerItem currentItem = list.get(position);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentItem.getDueTime());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dueTimeStr = "";
		if (!currentItem.isNoDueTime()) {
			dueTimeStr = "\t(" + sdf.format(cal.getTime()) + ")";
		}
		if (currentItem.getDiscount() > 66) {
			holder.image.setImageResource(R.drawable.circle_green);
		} else if (currentItem.getDiscount() <= 66 && currentItem.getDiscount() > 33) {
			holder.image.setImageResource(R.drawable.circle_yellow);
		} else {
			holder.image.setImageResource(R.drawable.circle_red);
		}
		// truncate name if it is greater than 15 characters
		String itemName = currentItem.getRetailername();
		if (itemName.length() > 15) {
			itemName = itemName.substring(0, 15) + "...";
		}
		holder.text.setText(itemName + dueTimeStr);
		holder.checkbox.setChecked(list.get(position).isChecked());
		return view;
	}
}
