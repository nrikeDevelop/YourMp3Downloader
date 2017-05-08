package susy.downloader.model;

import at.huber.youtubeExtractor.YtFile;

/**
 * Created by susy on 8/05/17.
 */

public class fileYT {

    YtFile ytFile;
    String name;

    public fileYT(YtFile ytFile, String name) {
        this.ytFile = ytFile;
        this.name = name;
    }

    public YtFile getYtFile() {
        return ytFile;
    }

    public void setYtFile(YtFile ytFile) {
        this.ytFile = ytFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
