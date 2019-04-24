package com.gchaimke.luckynumbers;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;

public class Statistic {
    private ArrayList<Integer> _70ArrList = new ArrayList<>();

    private void generateArray(){
        for(int i=0;i<=70;i++){
            _70ArrList.add(0);
        }
    }

    public ArrayList<String> getServerArrayList(Context context){
        String dataFromSettings = JsoupAsyncTask.getSettings(context);
        return new ArrayList<>(Arrays.asList(dataFromSettings.split("\\r?\\n")));
    }

    private ArrayList<String> getServerArrayListAll(Context context){
        String dataFromSettings = JsoupAsyncTask.getSettings(context);
        return new ArrayList<>(Arrays.asList(dataFromSettings.split("\\W+")));
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

    public int randomWithRange(int min,int max){
        int range=max-min+1;
        return (int)(Math.random()*range)+min;
    }
}
