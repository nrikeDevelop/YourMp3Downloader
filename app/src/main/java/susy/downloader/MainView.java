package susy.downloader;

/**
 * Created by susy on 4/05/17.
 */

public interface MainView {

    void setYTNameFile(String name);

    void sizeListFiles(String sizeList);

    void showMessage(String msg);

    void finish(String msg);

    void hideLoadingLayout();

}
