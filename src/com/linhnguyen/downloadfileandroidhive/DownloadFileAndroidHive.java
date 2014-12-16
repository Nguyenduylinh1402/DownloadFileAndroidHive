package com.linhnguyen.downloadfileandroidhive;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DownloadFileAndroidHive extends Activity {
	Button btnDownload;
	private ProgressDialog pDialog;
	//private static String file_url="http://users.ju.edu/xmountr/CS440/Programming%20Android(Oreilly--2011).pdf";
	private static String file_url="http://www.gocit.vn/files/Wrox.Professional.Android.4.Application.Development-www.gocit.vn.pdf";
	//private static String file_url="http://people.cs.deu.edu.tr/semih/Android_Books/Prentice%20Hall%20Android%20for%20Programmers,%20An%20App-Driven%20Approach%20(2012).pdf";
	public static final int PROGRESS_BAR_TYPE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_file_android_hive);
		btnDownload = (Button) findViewById(R.id.btnDownload);
		btnDownload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DownloadFileFromURL().execute(file_url);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_BAR_TYPE:
			pDialog = new ProgressDialog(this);// getApplicationContext no crash
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;

		default:
			return null;
		}
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(PROGRESS_BAR_TYPE);

		}

		@Override
		protected String doInBackground(String... params) {
			int count;
			URL url;
			try {
				url = new URL(params[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				int lengthOfFile = connection.getContentLength();
				//16021279
				//26486125
				InputStream inputStream = new BufferedInputStream(
						url.openStream(), 8192);
				OutputStream outputSteam = new FileOutputStream(
						"/sdcard/hive.pdf");

				byte data[] = new byte[1024];
				long total = 0;
				while ((count = inputStream.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lengthOfFile));
					outputSteam.write(data, 0, count);
				}
				outputSteam.flush();
				outputSteam.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			pDialog.setProgress(Integer.parseInt(values[0]));
		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();

		}

	}

}
