package net.android.clientsarah;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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
	private Button btnSend;
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
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				txtURL.setText("");
				String txtToSend = txtText.getText().toString();
				String query = "http://" + txtIP.getText().toString()
						+ "?emulate=";

				try {
					query += URLEncoder.encode(txtToSend, "utf-8");
				} catch (UnsupportedEncodingException e) {
					txtLog.setText("Exception levée : " + e.toString());
				}
				txtURL.setText(query);
				
				ConnectTask req = new ConnectTask();
				req.execute(query);
				
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
