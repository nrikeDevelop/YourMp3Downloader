package susy.downloader.model;

import java.util.ArrayList;

/**
 * Created by susy on 8/05/17.
 */

public class ListYTfile {
    ArrayList<FileYT> ytArrayList;

    public ListYTfile(ArrayList<FileYT> ytArrayList) {
        this.ytArrayList = ytArrayList;
    }

    public ArrayList<FileYT> getYtArrayList() {
        return ytArrayList;
    }

    public void setYtArrayList(ArrayList<FileYT> ytArrayList) {
        this.ytArrayList = ytArrayList;
    }
}
