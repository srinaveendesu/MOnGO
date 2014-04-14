package project.finalyear.database;

import java.util.Comparator;


public class DueDateComparator implements Comparator<RetailerItem> {

	@Override
	public int compare(RetailerItem arg0, RetailerItem arg1) {
		
		//first due time, then priority;
		if (arg0.isNoDueTime()&&!arg1.isNoDueTime())
			return 1;
		if (arg1.isNoDueTime()&&!arg0.isNoDueTime())
			return -1;
		if ((arg0.isNoDueTime()&&arg1.isNoDueTime())||
		(!arg0.isNoDueTime()&&!arg1.isNoDueTime()&&(arg0.getDueTime()==arg1.getDueTime()))) {
			return (int)(arg1.getDiscount()-arg0.getDiscount());
		}
		return (int)(arg0.getDueTime()-arg1.getDueTime());
	}

}
