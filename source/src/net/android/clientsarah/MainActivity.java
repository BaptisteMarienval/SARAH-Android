package net.android.clientsarah;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtText = (TextView) findViewById(R.id.txtText);
		txtURL = (TextView) findViewById(R.id.txtURL);
		txtIP= (EditText) findViewById(R.id.txtIP);

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
                
                String txtToSend = txtText.getText().toString();
                   
                String query ="http://"+txtIP.getText().toString()+"?emulate=";
                try {
                    query += URLEncoder.encode(txtToSend, "utf-8");
                    txtURL.setText(query);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
                

//                
//                try {
// 
//                    getInputStreamFromUrl(url);
//    
//                } catch (ActivityNotFoundException a) {
//
//                }
                
                
                try {
                    HttpClient client = new DefaultHttpClient();  
                    HttpGet get = new HttpGet(query);
                    HttpResponse responseGet = client.execute(get);  
                    HttpEntity resEntityGet = responseGet.getEntity();  
                    if (resEntityGet != null) {  
                        // do something with the response
                        String response = EntityUtils.toString(resEntityGet);
                        Log.i("GET RESPONSE", response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
	
//	public InputStream getInputStreamFromUrl(String url) {
//	    InputStream content = null;
//	    try {
//	      HttpClient httpclient = new DefaultHttpClient();
//	      HttpResponse response = httpclient.execute(new HttpGet(url));
//	      content = response.getEntity().getContent();
//	    } catch (Exception e) {
//	        
//	        Toast t = Toast.makeText(getApplicationContext(),
//	                "Network exception :"+e.getMessage(),
//                    Toast.LENGTH_SHORT);
//            t.show();
//	    }
//	      return content;
//	  }
}
