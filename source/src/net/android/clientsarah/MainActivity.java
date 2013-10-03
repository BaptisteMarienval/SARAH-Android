package net.android.clientsarah;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
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
	TextView txtInfos;
	EditText txtLog;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (TextView) findViewById(R.id.txtText);
		txtInfos = (TextView) findViewById(R.id.txtInfos);

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

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				txtLog.setText("");
				sendReq();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String port = sharedPrefs.getString("pref_portLabel", "8888");
        String ip = sharedPrefs.getString("pref_ipLabel", "192.168.0.X");
        
        txtInfos.setText(ip+":"+port);
		return true;
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_about:
	        startActivity(new Intent(this, AboutActivity.class));
	        return true;
	        case R.id.menu_help:
	        startActivity(new Intent(this, HelpActivity.class));
	        return true;
	        case R.id.menu_settings:
	        startActivity(new Intent(this, SettingsActivity.class));
	        return true;
	        default:
	        return super.onOptionsItemSelected(item);
	    }
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
	
	void sendReq() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        String port = sharedPrefs.getString("pref_portLabel", "8888");
        String ip = sharedPrefs.getString("pref_ipLabel", "192.168.0.X");
           
        String txtToSend = txtText.getText().toString();

        String query = "http://" + ip+":"+port+ "?emulate=";

        try {
            query += URLEncoder.encode(txtToSend, "utf-8");
        } catch (UnsupportedEncodingException e) {
            txtLog.setText("Exception lev�e : " + e.toString());
        }
        //txtURL.setText(query);
        
        ConnectTask req = new ConnectTask();
        req.execute(query);
        
        Toast.makeText(getApplicationContext(), "Sending Request : "+query , 1000).show();

        
    }
}
