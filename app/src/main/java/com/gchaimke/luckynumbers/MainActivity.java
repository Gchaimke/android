package com.gchaimke.luckynumbers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private Document htmlDocument;
    private String htmlPageUrl = "https://www.pais.co.il/777/showMoreResults.aspx?fromIndex=1&amount=20";
    private TextView parsedHtmlNode;
    private ArrayList<String> mArrNumbers = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    String settingsFile="app_settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parsedHtmlNode = findViewById(R.id.parse_html);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //parsedHtmlNode.clearComposingText();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mArrNumbers.clear();
                sb.append("Getting Data from site...\n");
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                Elements allNumbers = htmlDocument.select("ol[class=cat_data_info archive _777]");
                int mElementsSize = allNumbers.size();
                for(int i=0;i<mElementsSize;i++){
                    mArrNumbers.add(allNumbers.eq(i).text());
                }
                for(int x=0;x<mArrNumbers.size();x++){
                    sb.append(x).append(" set:").append(mArrNumbers.get(x)).append("\n");
                    //htmlContentInStringFormat += x+" set: "+mArrNumbers.get(x)+"\n";
                }
                writeToFile("test",getBaseContext());
             } catch (IOException e) {
                sb.append("Error getting data :\n"+e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            int rNum=randomWithRange(11,15);
            String numsFromSet=getSettings(getBaseContext());
            //numsFromSet = settings.getString("numbers","No data");
            parsedHtmlNode.setText(numsFromSet+"\n"+rNum);
        }
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(settingsFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String getSettings(Context context){
        String ret="from settings";
        try{
            InputStream inputStream = context.openFileInput(settingsFile);
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveString=bufferedReader.readLine())!=null){
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret=stringBuilder.toString();
            }
        }catch(FileNotFoundException e){

        }catch (IOException e){
            Log.e("log Activity","Can not resd file: "+ e.toString());
        }
        return ret;
    }

    int randomWithRange(int min,int max){
        int range=max-min+1;
        return (int)(Math.random()*range)+min;
    }
}
