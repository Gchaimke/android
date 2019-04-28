package com.gchaimke.luckynumbers;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static TextView tvColumn1;
    static EditText lotteryCont;
    private static Context mContext;

    private StringBuilder strBuild = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        tvColumn1 = findViewById(R.id.column1);
        lotteryCont=findViewById(R.id.lotteryNum);

        TextView lastTime = findViewById(R.id.lastCheck);
        if(getLastCheckTime().equals("01-01-70 00:00")){
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }else{
            lastTime.setText(getLastCheckTime());
        }

        TextView showData = findViewById(R.id.show_data);
        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strStartOut = JsoupAsyncTask.getSettings(getBaseContext());
                tvColumn1.setText(strBuild.append("\n").append(getString(R.string.data_from_file)).append(strStartOut));
                TextView lastTime = findViewById(R.id.lastCheck);
                lastTime.setText(getLastCheckTime());
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView lastTime = findViewById(R.id.lastCheck);
                String str =lastTime.getText().toString();
                if(str.equals("")||str.equals("01-01-70 00:00")||str.equals("No internet")){
                    lastTime.setText("No internet");
                }else{
                    getStats();
                    lastTime.setText(getLastCheckTime());
                }

            }
        });

        Button btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                TextView lastTime = findViewById(R.id.lastCheck);
                lastTime.setText(getLastCheckTime());
            }
        });
    }

    public static Context getmContext(){
        return mContext;
    }

    private String getLastCheckTime(){
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.US);
        File file =new File(this.getFilesDir().getAbsolutePath()+"/"+"app_settings");
        return fmt.format(file.lastModified());
    }

    private void getStats(){
        Statistic st =new Statistic();
        ArrayList<Integer> cNumbers = st.countNumbers(getBaseContext());
        int hNum = st.getMax(cNumbers);
        int lNum = st.getMin(cNumbers);
        int highDW=hNum-hNum/4;
        int lowUp=lNum+2;

        strBuild.setLength(0);
        strBuild.append("\nNumbers with high rating from ").append(highDW).append(" to ").append(hNum).append("\n");
        ArrayList<Integer> highNumsStats = st.highNumbers(getBaseContext(),highDW);
        printArrList(highNumsStats);

        ArrayList<Integer> lowNumsStats = st.lowNumbers(getBaseContext(),lNum);
        strBuild.append("\nNumbers with low rating from ").append(lNum).append(" to ").append(lowUp).append("\n");
        printArrList(lowNumsStats);

        ArrayList<Integer> otherNumbers = st.otherNumbers(getBaseContext(),lowUp,highDW);
        strBuild.append("\nOther numbers\n");
        printArrList(otherNumbers);

        ArrayList<Integer> luckyNums = st.luckyNumbers(getBaseContext(),lowUp,highDW);
        strBuild.append("\n-----------\n");
        printArrList(luckyNums);
        tvColumn1.setText(strBuild);
    }

    private void printArrList(ArrayList<Integer> list){
        for(int i=0;i<=list.size()-1;i++){
            strBuild.append(list.get(i)).append("\t");
        }
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


}
