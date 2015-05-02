package com.cs.silkcat.expensit;

import java.util.Calendar;
import java.util.TimeZone;

import com.cs.silkcat.expensit.Frm_Editacctitem.MyExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsoluteLayout;
import android.util.Log;

public class Grid_bills extends Activity implements OnItemLongClickListener {
	BilldbHelper billdb;
	View sv;
	EditText edit;
	@SuppressWarnings("deprecation")
	AbsoluteLayout alayout;
	int a = 10, b = 10;
	GridView grd;

	TextView total;

	DatePicker dp;
	Button okbtn;
	ListView lv;

	private int mYear;
	private int mMonth;
	private int mDay;

	String today;
	String[] from;
	int[] to;

	SimpleCursorAdapter mAdapter;
	Cursor cur;
	int _id;

	protected GridView listHands = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.grid_bills);
		billdb = new BilldbHelper(this);
		lv = (ListView) this.findViewById(R.id.listview01);

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH)+1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		today = mYear + "-" + mMonth;
		setTitle("Expenses (" + today + ")");
		cur = billdb.getBills(today);
		from = new String[] { "rowid", "name", "fee", "sdate", "desc" };
		to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		
		//int grid_items_id= context.getResources.getIdentifier(grid_items,"layout", getPackageName());
		mAdapter = new SimpleCursorAdapter(this, R.layout.grid_items, cur,
				from, to,1);
		lv.setAdapter(mAdapter);

		total = (TextView) findViewById(R.id.totalitem);
		total.setText(billdb.getBillsTotal(today));

		lv.setOnItemLongClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "Choose Month");// .setIcon(R.drawable.editbills);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showDialog("Choose Year and Month", "");
			return true;
		case 2:

			return true;

		}
		return false;
	}

	private void showDialog(String title, String text) {
		final DatePickerDialog dia = new DatePickerDialog(this,
				mDateSetListener, mYear, mMonth-1, mDay);

		dia.show();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear+1;
			mDay = dayOfMonth;
			today = mYear + "-" + mMonth;

			setTitle("Expenses (" + today + ")");
			cur = billdb.getBills(today);
			mAdapter.changeCursor(cur);
			//lv.setAdapter(mAdapter);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
		}
	};

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		_id=(int)id;
		new AlertDialog.Builder(this).setTitle("Alert").setMessage(
				"Delete this item?").setIcon(R.drawable.quit).setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
                        //Log.v("",""+_id);
						 billdb.delBills(_id);
						 mAdapter.changeCursor(cur);
						 ((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
						// finish();
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

		return true;

	}

}