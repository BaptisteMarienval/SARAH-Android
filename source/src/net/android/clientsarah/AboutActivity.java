package net.android.clientsarah;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

    TextView txtAbout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setText("Ceci correspondra a la page 'a propos'");
    }
    
    
}
