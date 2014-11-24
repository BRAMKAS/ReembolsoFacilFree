package br.com.reembolsofacil.free;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	//Ids para chamadas de activities
	protected static final int VIEW_DESPESAS = 2;
	protected static final int VIEW_VIAGEM   = 3;
	protected static final int VIEW_VIAGENS  = 4;
	protected static final int VIEW_MAIN     = 5;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	protected void aviso(String msgToast) {
		Toast.makeText(this, msgToast, Toast.LENGTH_SHORT).show();
	}
	
	protected String getResourceString(int stringId) {
		String ret = "";
		try {
		ret = getResources().getString(stringId);
		} catch (Exception e) {
			ret = e.getMessage();
		}
		return ret;
	}
	
	protected void aviso(int msgToast) {
		Toast.makeText(this, msgToast, Toast.LENGTH_SHORT).show();
	}
	
	protected void avisoLong(String msgToast) {
		Toast.makeText(this, msgToast, Toast.LENGTH_LONG).show();
	}
	
	protected void avisoLong(int msgToast) {
		Toast.makeText(this, msgToast, Toast.LENGTH_LONG).show();
	}

	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    //netInfo.isConnected()
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public boolean isOnline2() {
		try {
			URL url = new URL("http://www.google.com");

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("User-Agent", "Android Application:");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1000 * 3); // mTimeout is in seconds
			urlc.connect();
			if (urlc.getResponseCode() == 200) {
				return true;
			}
		} catch (MalformedURLException e1) {
			Log.e("Error", e1.getMessage());
		} catch (IOException e) {
			Log.e("Error", e.getMessage());
		}
		return false;
	}

	public boolean isOnline3() {
		ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return conMgr.getActiveNetworkInfo() != null &&
        	conMgr.getActiveNetworkInfo().isAvailable() &&
        	conMgr.getActiveNetworkInfo().isConnected();
	}
	
	protected class AvisoRunnable implements Runnable{

		private String msg;
		private Context ctx;
		
		public AvisoRunnable(Context ctx, String msg) {
			super();
			this.msg = msg;
			this.ctx = ctx;
		}

		public void run() {
			Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	protected class AvisoLongRunnable implements Runnable{

		private String msg;
		private Context ctx;
		
		public AvisoLongRunnable(Context ctx, String msg) {
			super();
			this.msg = msg;
			this.ctx = ctx;
		}

		public void run() {
			Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
			
		}	
	}
}
