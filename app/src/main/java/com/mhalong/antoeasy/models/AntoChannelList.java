package com.mhalong.antoeasy.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mhalong.antoeasy.manager.Contextor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class AntoChannelList {

    private static AntoChannelList instance;

    public static AntoChannelList getInstance() {
        if (instance == null)
            instance = new AntoChannelList();
        return instance;
    }

    private Context mContext;
    private List<AntoChannelItem> list = new ArrayList<>();

    private AntoChannelList() {
        mContext = Contextor.getInstance().getContext();
        loadCache();

    }

    public List<AntoChannelItem> getChannelList() {
        return list;
    }

    public void setChannelList(List<AntoChannelItem> list) {
        this.list = list;
    }

    public void saveCache() {
        SharedPreferences prefs = mContext.getSharedPreferences("channel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        List<AntoChannelItem> cache = new ArrayList<>();
        cache.addAll(AntoChannelList.getInstance().getChannelList());
        String json = new Gson().toJson(cache);
        editor.putString("json", json);
        editor.apply();
    }

    private void loadCache() {
        SharedPreferences prefs = mContext.getSharedPreferences("channel", Context.MODE_PRIVATE);
        String json = prefs.getString("json", null);
        if (json == null) {
            return;
        }
        List<AntoChannelItem> list = new Gson().fromJson(json, new TypeToken<List<AntoChannelItem>>() {
        }.getType());

        setChannelList(list);
    }


}
