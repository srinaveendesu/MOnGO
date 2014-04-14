package project.finalyear.database;

import java.util.Comparator;


public class DiscountComparator implements Comparator<RetailerItem> {

	@Override
	public int compare(RetailerItem arg0, RetailerItem arg1) {
		//first priority, then duetime;
		if (arg1.getDiscount()==arg0.getDiscount()) {
			if (arg0.isNoDueTime())
				return 1;
			if (arg1.isNoDueTime())
				return -1;
			return (int)(arg0.getDueTime()-arg1.getDueTime());
		}
		//inverse sorting
		return (int)(arg1.getDiscount()-arg0.getDiscount());
	}

}
