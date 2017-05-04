package susy.downloader;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import at.huber.youtubeExtractor.Format;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity implements MainView {

    private final int REQUEST_PERMISSIONS = 123;
    private static final int FILE_SELECT_CODE = 0;

    private static final SparseArray<Format> FORMAT_MAP = new SparseArray<>();

    MainView mainView;
    MainPresenter mainPresenter;

    Context context;
    Button btDownload;
    EditText editTextUrl;
    ImageView helpImage;
    RelativeLayout loading;

    Boolean permision = false;
    String link = "";


    public void setUI(){
        btDownload = (Button) findViewById(R.id.buttonDownload);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        loading = (RelativeLayout) findViewById(R.id.loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setUI();
        requestPermissions();

        mainView = this;
        mainPresenter = new MainPresenter(context,mainView);

        //create data
        mainPresenter.isFolderExist();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if(clipData != null){
            link = clipData.getItemAt(0).getText().toString();
        }

        editTextUrl.setText(link);

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!permision){
                    Toast.makeText(context, context.getString(R.string.permisons_not_granted), Toast.LENGTH_SHORT).show();
                }else{
                    if(editTextUrl.getText().toString().equals("")){
                        Toast.makeText(context, context.getString(R.string.not_url_function), Toast.LENGTH_SHORT).show();
                    }else{
                        //TODO do actions
                        mainPresenter.getYoutubeDownloadUrl(editTextUrl.getText().toString());
                        loading.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }


    //Method to request permissons
    private void requestPermissions() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }else {
            //getYoutubeDownloadUrl(youtubeLink);
            permision = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    //getYoutubeDownloadUrl(youtubeLink);
                    permision = true;

                } else {
                    //not granted
                    permision = false;

                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/YouDownloader/");
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        switch(requestCode){
            case FILE_SELECT_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    System.out.println(FilePath);
                }
                break;

        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish(String msg) {
        if (!msg.equals("")){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public void hideLoadingLayout() {
        loading.setVisibility(View.GONE);
    }
}


