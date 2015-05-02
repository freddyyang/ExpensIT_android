package com.cs.silkcat.expensit;

<<<<<<< HEAD
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Handler mHandler = new Handler();

	ImageView imageview;
	TextView textview;
	int alpha = 255;
	int b = 0;
	
	//@SuppressLint("NewApi") used for setImageAlpha, newer version of setAlpha
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imageview = (ImageView) this.findViewById(R.id.ImageView01);
		textview = (TextView) this.findViewById(R.id.TextView01);

		imageview.setAlpha(alpha);

		new Thread(new Runnable() {
			public void run() {
				initApp();
				
				while (b < 2) {
					try {
						if (b == 0) {
							Thread.sleep(1000);
							b = 1;
						} else {
							Thread.sleep(50);
						}

						updateApp();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				imageview.setAlpha(alpha);
				imageview.invalidate();


			}
		};

	}

	public void updateApp() {
		alpha -= 5;

		if (alpha <= 0) {
			b = 2;
			Intent in = new Intent(this, com.cs.silkcat.expensit.Frm_Addbills.class);
			startActivity(in);
			this.finish();
		}

		mHandler.sendMessage(mHandler.obtainMessage());

	}
	
	public void initApp(){
		 BilldbHelper billdb=new BilldbHelper(this);
  	     billdb.FirstStart(); 	     
  	     billdb.close();
  	     
  		 
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
			
		}
		return false;
	}
}
=======
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
>>>>>>> 3ab1257636e21099d0e6d668e6f4212e82280fd6
