package com.gchaimke.luckynumbers;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

class Statistic {
    private ArrayList<Integer> luckyNums = new ArrayList<>();

    private ArrayList<Integer> genEmptyArray(){
        ArrayList<Integer> _70ArrList = new ArrayList<>();
        for(int i=0;i<=70;i++){
            _70ArrList.add(0);
        }
        return _70ArrList;
    }

    private ArrayList<Integer> genArray(){
        ArrayList<Integer> _70ArrList = new ArrayList<>();
        for(int i=1;i<=70;i++){
            _70ArrList.add(i);
        }
        return _70ArrList;
    }

    private ArrayList<String> getServerArrayList(Context context,int num){
        if(num==0){
            String dataFromSettings = JsoupAsyncTask.getSettings(context);
            return new ArrayList<>(Arrays.asList(dataFromSettings.split("\\r?\\n")));
        }
        String dataFromSettings = JsoupAsyncTask.getSettings(context);
        return new ArrayList<>(Arrays.asList(dataFromSettings.split("\\W+")));
    }

    private ArrayList<Integer> countNumbers(Context context){
        ArrayList<Integer> _70ArrList = genEmptyArray();
        ArrayList<String> list = getServerArrayList(context,1);
        int listSize = list.size();
        for(int i =0;i<listSize;i++){
            _70ArrList.set(Integer.parseInt(list.get(i)),_70ArrList.get(Integer.parseInt(list.get(i)))+1);
        }
        return _70ArrList;
    }

    ArrayList<Integer> highNumbers(Context context,int up){
        ArrayList<Integer> _70ArrList =  countNumbers(context);
        countNumbers(context);
        ArrayList<Integer> highList = new ArrayList<>();
        int listSize = _70ArrList.size();
        for(int i =1;i<=listSize-1;i++){
            if(_70ArrList.get(i)>=up)
                highList.add(i);
        }
        return highList;
    }

    ArrayList<Integer> lowNumbers(Context context,int low){
        ArrayList<Integer> _70ArrList = countNumbers(context);
        ArrayList<Integer> lowList = new ArrayList<>();
        int listSize = _70ArrList.size();
        for(int i =1;i<=listSize-1;i++){
            if(_70ArrList.get(i)<=low && _70ArrList.get(i)>0)
                lowList.add(i);
        }
        return lowList;
    }

    ArrayList<Integer> otherNumbers(Context context,int low,int high){
        ArrayList<Integer> list = genArray();
        list.removeAll(highNumbers(context,high));
        list.removeAll(lowNumbers(context,low));
        return list;
    }

    ArrayList<Integer> luckyNumbers(Context context,int low,int high){
        ArrayList<Integer> highNums = highNumbers(context,high);
        ArrayList<Integer> lowNums = lowNumbers(context,low);
        ArrayList<Integer> otherNums = otherNumbers(context,low,high);
        getRandom(highNums,3);
        getRandom(lowNums,4);
        getRandom(otherNums,9-luckyNums.size());
        Collections.sort(luckyNums);
        return luckyNums;
    }

    private void getRandom(ArrayList<Integer> list,int max){
        if(list.size()>0) {
            for (int i = 0; i < max; ) {
                int tmp = list.get(randomWithRange(list.size() - 1));
                if (luckyNums.contains(tmp)) {
                    tmp = list.get(randomWithRange(list.size() - 1));
                } else {
                    luckyNums.add(tmp);
                    i++;
                }
            }
        }
    }

    private int randomWithRange(int max){
        return (int)(Math.random()*max)+1;
    }
}
