package susy.downloader.common;

import android.content.Context;
import android.content.SharedPreferences;

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


}
