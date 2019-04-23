package com.gchaimke.luckynumbers;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Statistic {
    Context context;
    private ArrayList<Integer> _70ArrList = new ArrayList<>();
    private ArrayList<String> _countedArrList = new ArrayList<>();
    static String settingsFile="app_settings";

    private void generateArray(){
        for(int i=0;i<=70;i++){
            _70ArrList.add(0);
        }
    }

    private MainActivity ma = new MainActivity();
    JsoupAsyncTask js = new JsoupAsyncTask(ma.getContext());

    public ArrayList<String> getServerArrayList(Context context){
        String dataFromSettings = JsoupAsyncTask.getSettings(context);
        return new ArrayList<String>(Arrays.asList(dataFromSettings.split("\\r?\\n")));
    }

    private ArrayList<String> getServerArrayListAll(Context context){
        String dataFromSettings = JsoupAsyncTask.getSettings(context);
        return new ArrayList<String>(Arrays.asList(dataFromSettings.split("\\W+")));
    }

    public ArrayList<Integer> countNumbers(Context context){
        generateArray();
        ArrayList<String> list = getServerArrayListAll(context);
        int allNumsListSize = getServerArrayListAll(context).size();
        for(int i =0;i<allNumsListSize;i++){
            _70ArrList.set(Integer.parseInt(list.get(i)),_70ArrList.get(Integer.parseInt(list.get(i)))+1);
        }
        return _70ArrList;
    }
}
