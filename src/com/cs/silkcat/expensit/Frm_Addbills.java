package com.cs.silkcat.expensit;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Frm_Addbills extends Activity implements OnClickListener {

	EditText edittext_acctitem,EditTextDESC,Fee;
	TextView mDate;
	TextView mTime;
	static final int RG_REQUEST = 0;
	
	private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    Spinner s1;
    Button BtnDate,BtnTime;
    Button BtnCancel,BtnSave;
    
    BilldbHelper billdb;
    
    int acctitemid=-1;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setTitle("ExpensIT-Add Expense");		
		setContentView(R.layout.frm_addbills);
		
		edittext_acctitem = (EditText)findViewById(R.id.edittext_acctitem);		
		edittext_acctitem.setOnClickListener(this);
		
		EditTextDESC=(EditText)findViewById(R.id.EditTextDESC);	
		Fee=(EditText)findViewById(R.id.Fee);	
		
		BtnDate=(Button)findViewById(R.id.BtnDate);
		BtnDate.setOnClickListener(this);
		BtnTime=(Button)findViewById(R.id.BtnTime);
		BtnTime.setOnClickListener(this);
		
		BtnCancel=(Button)findViewById(R.id.BtnCancel);
		BtnCancel.setOnClickListener(this);
		BtnSave=(Button)findViewById(R.id.BtnSave);
		BtnSave.setOnClickListener(this);
		
		mDate = (TextView) findViewById(R.id.vdate);
        mTime = (TextView) findViewById(R.id.vtime);
		
		initTime();
        
        setDatetime();
        billdb = new BilldbHelper(this);
        s1=(Spinner) findViewById(R.id.Spinner01);        
        String[] from= new String[]{"caption"};
        int[] to=new int[]{android.R.id.text1};
        Cursor cur=billdb.getUserid();     
        SimpleCursorAdapter mAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, cur,from, to,1);      
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);           
        s1.setAdapter(mAdapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "Expense").setIcon(R.drawable.editbills);
		menu.add(0, 2, 0, "Exit").setIcon(R.drawable.quit);
		menu.add(0, 3, 0, "About");	
		return true;
	}

	@SuppressWarnings("deprecation")
	public void onClick(View v) {
		if (v.equals(edittext_acctitem)) {
			//test
			Editor sharedata = getSharedPreferences("data", 0).edit();
			sharedata.putString("item","hello getSharedPreferences");
			sharedata.commit();
			
			Intent intent = new Intent();
			intent.setClass(Frm_Addbills.this, Frm_Editacctitem.class);			
			startActivityForResult(intent, RG_REQUEST);
		} else if (v.equals(BtnTime)){
			showDialog(1);
		} else if (v.equals(BtnDate)){
			showDialog(2);
		} else if (v.equals(BtnCancel)){
			cancel();
		} else if (v.equals(BtnSave)){
			save();
		}
		
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent();
			//intent.setClass(Frm_Addbills.this, Grid_bills.class);
			intent.setClassName(Frm_Addbills.this,"com.cs.silkcat.expensit.Grid_bills");;
			startActivity(intent);
			return true;

		case 2:
			QuitApp();
			return true;
		case 3:
			  new AlertDialog.Builder(this) 
			    .setTitle("ExpensIT") 
			    .setMessage("freddyyang1124@gmail.com") 
			    .show();
			return true;
		}
		return false;
	}

    
    
	public void QuitApp() {
		new AlertDialog.Builder(Frm_Addbills.this).setTitle("Alert").setMessage(
				"Do you want to exit ExpensIT?").setIcon(R.drawable.quit).setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {	
						billdb.close();
						finish();
					}
				}).setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RG_REQUEST) {
			if (resultCode == RESULT_CANCELED) {
				// setTitle("Canceled...");
			} else if (resultCode == RESULT_OK) {
				// setTitle((String)data.getCharSequenceExtra("DataKey"));
				edittext_acctitem.setText((String) data.getCharSequenceExtra("name"));
				acctitemid=Integer.parseInt((String)data.getCharSequenceExtra("id"));
				Log.v("cola","get acctitemid="+acctitemid);
				
			}
		}
	}
	
	private void cancel(){
		Log.v("cola","u put cancel btn");
		edittext_acctitem.setText("");
		Fee.setText("");
		acctitemid=-1;
		initTime();setDatetime();
		EditTextDESC.setText("");
	}
	private void save(){
		if (acctitemid==-1){
			new AlertDialog.Builder(this)
			    .setMessage("Select an Expense")
			    .show();
			return;
		}
		int fee=0;
		String s=Fee.getText().toString();
		int pos=s.indexOf(".");
		if (pos>0){	
			if (s.length()-pos<3){
				s=s+"0";
			}
			fee=Integer.parseInt(s.substring(0,pos)+s.substring(pos+1,pos+3));		
		}else{			
		    fee=Integer.parseInt(s)*100;
			
		}
		if (billdb.Bills_save(acctitemid,fee,(int)s1.getSelectedItemId(), ((TextView)mDate).getText().toString(), ((TextView)mTime).getText().toString(),EditTextDESC.getText().toString())){
			Toast.makeText(this, "Successfully Added.", Toast.LENGTH_SHORT).show(); 
			cancel();
		}
		else{
			Toast.makeText(this, "Failed to add.", Toast.LENGTH_SHORT).show(); 
		}
	}

    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			QuitApp();
			return true;
			
		}
		return false;
	}
	private void initTime(){
		Calendar c = Calendar. getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
	}
	
	private void setDatetime(){
		mDate.setText("   "+mYear+"-"+mMonth+"-"+mDay);
        mTime.setText(pad(mHour)+":"+pad(mMinute));
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, false);
            case 2:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth-1, mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 1:            	
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
            case 2:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth-1, mDay);
                break;
        }
    }    
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear+1;
                mDay = dayOfMonth;

                setDatetime();
            }
        };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                setDatetime();
            }
        };
    private static String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }        
}