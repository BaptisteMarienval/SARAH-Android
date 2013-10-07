package net.android.clientsarah;
 
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.TextView;
 
public class JSoup extends AsyncTask<String, String, String> {
 
    String RESULT;
    @Override
    protected String doInBackground(String... query) {
    
      String result = "";
      Document document = null;
    try {
        document = Jsoup.connect(query[0]).get();
    } catch (IOException e) {
        e.printStackTrace();
    }

       
      // selector query
     // Elements nodeBlogStats = document.select("div.flip-container p");
      Elements nodeBlogStats = document.select("html body div#container.container div.stretch div#home.stretch div.tab-content div#modules.tab-pane div.gridster-wrapper div.gridster ul li.gridster-item div.plugin-gridster div.flipper div.plugin-body div.back p.plugin-footer-left a.btn");
      // check results

      int i = 0;
      while(nodeBlogStats.size() > 0 && i<nodeBlogStats.size()) {

          // get value
          if (!nodeBlogStats.get(i).attr("data-target").equals(""))
          result += nodeBlogStats.get(i).attr("data-target").substring(9)+"\n";
          i++;
      }
      RESULT = result;
    return result;
    }
    
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
 
        RESULT = result;
    }
    
    protected String getResult() {
        return RESULT;
    }
}