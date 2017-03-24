package com.example.huynguyen.controlvehicles;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String SETTING_FILE = "setting_values";

    public Settings(Context context){
        this.sharedPref = context.getSharedPreferences(SETTING_FILE, Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
    }
}