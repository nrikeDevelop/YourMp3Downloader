package susy.downloader.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by susy on 8/05/17.
 */

public class ListYTfile {
    ArrayList<fileYT> ytArrayList;

    public ListYTfile(ArrayList<fileYT> ytArrayList) {
        this.ytArrayList = ytArrayList;
    }

    public ArrayList<fileYT> getYtArrayList() {
        return ytArrayList;
    }

    public void setYtArrayList(ArrayList<fileYT> ytArrayList) {
        this.ytArrayList = ytArrayList;
    }
}
