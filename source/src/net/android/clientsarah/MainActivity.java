package net.android.clientsarah;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected static final int RESULT_SPEECH = 1;

	private ImageButton btnSpeak;
	private Button btnSend1;
	private Button btnSend2;
	private Button btnSend3;
	private Button btnSend4;
	TextView txtText;
	TextView txtURL;
	EditText txtIP;
	EditText txtLog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (TextView) findViewById(R.id.txtText);
		txtURL = (TextView) findViewById(R.id.txtURL);
		txtIP = (EditText) findViewById(R.id.txtIP);
		txtLog = (EditText) findViewById(R.id.txtLog);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		btnSpeak.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "fr-FR");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

		// Methode 1 : HttpClient -> HTTPGet
		btnSend1 = (Button) findViewById(R.id.btnSend1);
		btnSend1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				txtURL.setText("");
				String txtToSend = txtText.getText().toString();
				String query = "http://" + txtIP.getText().toString()
						+ "?emulate=";
				try {
					query += URLEncoder.encode(txtToSend, "utf-8");
					txtURL.setText(query);

					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(query);
					HttpResponse responseGet = client.execute(get);
					HttpEntity resEntityGet = responseGet.getEntity();
					if (resEntityGet != null) {
						String response = EntityUtils.toString(resEntityGet);
						txtLog.setText("Response = "
								+ responseGet.getStatusLine().getStatusCode()
								+ " - " + response);
					} else {
						txtLog.setText("Aucune réponse");
					}
				} catch (Exception e) {
					txtLog.setText("Exception levée : " + e.getMessage());
				}

			}
		});

		// Methode 2 : HttpURLConnection
		btnSend2 = (Button) findViewById(R.id.btnSend2);
		btnSend2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				txtURL.setText("");
				String txtToSend = txtText.getText().toString();
				String query = "http://" + txtIP.getText().toString()
						+ "?emulate=";
				try {
					query += URLEncoder.encode(txtToSend, "utf-8");
					txtURL.setText(query);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				HttpURLConnection con = null;
				URL url;
				InputStream is = null;
				try {
					url = new URL(query);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.connect();
					is = new BufferedInputStream(con.getInputStream());
				   
				} catch (IOException e) {
					txtLog.setText("Exception levée : " + e.getMessage());
				}
				 finally {
				     con.disconnect();
				   }

			}
		});

		// HTTP Post
		btnSend3 = (Button) findViewById(R.id.btnSend3);
		btnSend3.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				txtURL.setText("");
				InputStream content = null;
				String txtToSend = txtText.getText().toString();
				String query = "http://" + txtIP.getText().toString();
				try {
					//query += URLEncoder.encode(txtToSend, "utf-8");
					txtURL.setText(query);

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(query);
					List nameValuePairs = new ArrayList(1);
					// this is where you add your data to the post method
					nameValuePairs.add(new BasicNameValuePair("emulate",txtToSend));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httpPost);
					content = response.getEntity().getContent();
					txtLog.setText(content.toString());
				} catch (Exception e) {
					txtLog.setText("Exception levée : " + e.getMessage());
				}

			}
		});

		// Test sur URL simple
		btnSend4 = (Button) findViewById(R.id.btnSend4);
		btnSend4.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				txtURL.setText("");
				String query = "http://www.google.fr";
				try {
					txtURL.setText(query);

					HttpClient client = new DefaultHttpClient();
					// HttpClient client =
					// AndroidHttpClient.newInstance("Android");
					HttpGet get = new HttpGet(query);
					HttpResponse responseGet = client.execute(get);
					HttpEntity resEntityGet = responseGet.getEntity();
					if (resEntityGet != null) {
						String response = EntityUtils.toString(resEntityGet);
						txtLog.setText("Response = "
								+ responseGet.getStatusLine().getStatusCode()
								+ " - " + response);
					} else {
						txtLog.setText("Aucune réponse");
					}
				} catch (Exception e) {
					txtLog.setText("Exception levée : " + e.getMessage());
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				txtText.setText(text.get(0));
			}
			break;
		}

		}
	}
}
