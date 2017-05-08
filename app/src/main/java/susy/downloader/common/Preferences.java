package susy.downloader.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import susy.downloader.model.ListYTfile;
import susy.downloader.model.fileYT;

/**
 * Created by susy on 8/05/17.
 */

public class Preferences {

    static final String PREFERENCES = "YOU_DOWN";

    public static void setNameListFolder(Context context, String folderListName) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("folder_list_name", folderListName);
        editor.commit();
    }

    public static String getNameListFolder(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, context.MODE_PRIVATE);
        return preferences.getString("folder_list_name", null);
    }

    public static void setListYTfile(Context context, ListYTfile listYTfile) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String stringMyObjectJson = gson.toJson(listYTfile);

        editor.putString("list_gson_ytfile",stringMyObjectJson);
        editor.commit();
    }

    public static ListYTfile getListYTfile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, context.MODE_PRIVATE);

        Gson gson = new Gson();
        String stringMyObjectJson = preferences.getString("list_gson_ytfile", null);
        ListYTfile myObject = gson.fromJson(stringMyObjectJson, ListYTfile.class);

        return myObject;
    }



}
