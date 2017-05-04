package susy.downloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;

import java.io.File;
import java.util.ArrayList;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/**
 * Created by susy on 4/05/17.
 */

public class MainPresenter {

    ArrayList<YtFile> ytFilesArray ;
    Context context;
    MainView mainView;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
        ytFilesArray = new ArrayList<>();
    }

    public void isFolderExist(){
        File fileDownload = new File(Environment.getExternalStorageDirectory() + "/YouDownloader");

        if(!fileDownload.exists()){
            fileDownload.mkdir();
        }
    }

    public void getYoutubeDownloadUrl(String youtubeLink) {

        new YouTubeExtractor(context) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {

                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    mainView.showMessage(context.getString(R.string.not_find_links));
                    mainView.hideLoadingLayout();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    YtFile ytFile = ytFiles.get(itag);
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {

                        //If url, contains audio , can save mp3
                        if(ytFile.getUrl().contains("audio")){
                            ytFilesArray.add(ytFile);
                        }
                    }
                }
                if(ytFilesArray.size() > 0 ){
                    changeNameUrlDownload(vMeta.getTitle(), ytFilesArray.get(0));
                }else{
                    mainView.showMessage(context.getString(R.string.not_find_links));
                    mainView.hideLoadingLayout();
                }
            }
        }.extract(youtubeLink, true, false);

    }

    public void changeNameUrlDownload(final String videoTitle, final YtFile ytfile) {
        // Display some buttons and let the user choose the format
        String filename;
        if (videoTitle.length() > 55) {
            filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
        } else {
            filename = videoTitle + "." + ytfile.getFormat().getExt();
        }

        filename = filename.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");

        downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
    }

    public void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {

        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        mainView.finish(context.getString(R.string.downloaded));

    }


}
