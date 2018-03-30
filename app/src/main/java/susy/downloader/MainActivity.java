package susy.downloader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainView {

    private final int REQUEST_PERMISSIONS = 123;
    private static final int FILE_SELECT_CODE = 0;

    private String TYPE_AUDIO = "audio%2";
    private String TYPE_VIDEO = "video%2";

    MainView mainView;
    MainPresenter mainPresenter;

    Context context;
    EditText editTextUrl;
    Button btaudio;
    Button btvideo;
    ImageView helpImage;
    RelativeLayout loading;

    Boolean permision = false;
    String link = "";

    public void setUI(){
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        loading = (RelativeLayout) findViewById(R.id.loading);
        helpImage = (ImageView) findViewById(R.id.helpImage);
        btaudio = (Button) findViewById(R.id.btaudio);
        btvideo = (Button) findViewById(R.id.btvideo);
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

        btaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAudioVideoUrl(TYPE_AUDIO);
            }
        });

        btvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAudioVideoUrl(TYPE_VIDEO);
            }
        });

        helpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertHelp = new AlertDialog.Builder(context);
                alertHelp.setMessage(context.getString(R.string.help));
                alertHelp.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alertHelp.create();
                dialog.show();

            }
        });

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

    public void getAudioVideoUrl(String type){
        if(!permision){
            Toast.makeText(context, context.getString(R.string.permisons_not_granted), Toast.LENGTH_SHORT).show();
        }else{
            if(editTextUrl.getText().toString().equals("")){
                Toast.makeText(context, context.getString(R.string.not_url_function), Toast.LENGTH_SHORT).show();
            }else{
                //TODO do actions
                mainPresenter.getYoutubeDownloadUrl(editTextUrl.getText().toString(),type);
                loading.setVisibility(View.VISIBLE);
            }
        }
    }
}


