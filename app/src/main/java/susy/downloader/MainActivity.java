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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import susy.downloader.common.Preferences;

public class MainActivity extends AppCompatActivity implements MainView {

    private final int REQUEST_PERMISSIONS = 123;
    private static final int FILE_SELECT_CODE = 0;

    private String TYPE_AUDIO = "audio%2";
    private String TYPE_VIDEO = "video%2";
    private String TYPE_LIST = "list_type";

    MainView mainView;
    MainPresenter mainPresenter;

    Context context;
    TextView textDownloadList;
    EditText editTextUrl;
    EditText nameFolderEditText;
    TextView nameFile;
    TextView numberFilesListText;
    Button btaddList;
    Button btdeleteList;
    Button btdownloadList;
    Button btaudio;
    Button btvideo;
    ImageView helpImage;

    RelativeLayout loading;
    LinearLayout basicButtons;
    LinearLayout listButtons;

    Boolean permision = false;
    String link = "";

    public void setUI(){
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        nameFile = (TextView) findViewById(R.id.name_file_text);
        textDownloadList = (TextView) findViewById(R.id.text_download_list);
        nameFolderEditText = (EditText) findViewById(R.id.name_folder_edit_text);
        numberFilesListText = (TextView) findViewById(R.id.numberListFilesTextView);
        loading = (RelativeLayout) findViewById(R.id.loading);
        helpImage = (ImageView) findViewById(R.id.helpImage);
        btaddList = (Button) findViewById(R.id.btAddList);
        btdownloadList = (Button) findViewById(R.id.btDownloadList);
        btdeleteList = (Button) findViewById(R.id.btDeleteList);
        btaudio = (Button) findViewById(R.id.btaudio);
        btvideo = (Button) findViewById(R.id.btvideo);
        basicButtons = (LinearLayout) findViewById(R.id.linear_layout_basic_buttons);
        listButtons = (LinearLayout) findViewById(R.id.linear_layout_list_buttons);

        if(Preferences.getNameListFolder(context) != null){
            nameFolderEditText.setText(Preferences.getNameListFolder(context));
        }

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

        //get link
        clipBoard();

        editTextUrl.setText(link);

        //BUTTONS

        textDownloadList.setOnClickListener(clickDownloadList);
        btaddList.setOnClickListener(clickAddList);
        btdeleteList.setOnClickListener(clickDeleteList);
        nameFolderEditText.addTextChangedListener(textWatcher);
        btdownloadList.setOnClickListener(clickDownloadListButton);
        btaudio.setOnClickListener(clickBasicAudio);
        btvideo.setOnClickListener(clickBasicVideo);
        helpImage.setOnClickListener(clickHelp);

    }

    public void clipBoard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();

        if(clipData != null){
            link = clipData.getItemAt(0).getText().toString();
            mainPresenter.youtubeExtractorDetails(link);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        clipBoard();
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
    public void setYTNameFile(String name) {
        nameFile.setText(name);
    }

    @Override
    public void sizeListFiles(String sizeList) {
        numberFilesListText.setText(sizeList);
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

    //Buttons-Click
    View.OnClickListener clickDownloadList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(listButtons.getVisibility() == View.GONE){
                listButtons.setVisibility(View.VISIBLE);
                basicButtons.setVisibility(View.GONE);
            }else{
                listButtons.setVisibility(View.GONE);
                basicButtons.setVisibility(View.VISIBLE);
            }
        }
    };

    View.OnClickListener clickDownloadListButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainPresenter.downloadList(nameFolderEditText.getText().toString());
        }
    };


    View.OnClickListener clickDeleteList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage(context.getString(R.string.dialog_delete_title));
            alertDialog.setPositiveButton(context.getString(R.string.dialog_delete_yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainPresenter.deleteList();
                            numberFilesListText.setText(String.valueOf(mainPresenter.countList()));
                            nameFolderEditText.setText("");
                            dialog.dismiss();
                        }
                    });

            alertDialog.setNegativeButton(context.getString(R.string.dialog_delete_no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = alertDialog.create();
            dialog.show();

        }
    };

    View.OnClickListener clickAddList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nameFolderEditText.getText().toString().equals("")){
                Toast.makeText(context, context.getString(R.string.list_empty_name), Toast.LENGTH_SHORT).show();
            }else{
                //TODO action
                if(editTextUrl.getText().toString().equals("")){
                    Toast.makeText(context, context.getString(R.string.not_url_function), Toast.LENGTH_SHORT).show();
                }else{
                    //TODO do actions
                    mainPresenter.getYoutubeDownloadUrl(editTextUrl.getText().toString(),TYPE_LIST);
                    loading.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    View.OnClickListener clickHelp = new View.OnClickListener() {
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
            AlertDialog dialogHelp = alertHelp.create();
            dialogHelp.show();
        }
    };

    View.OnClickListener clickBasicAudio = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAudioVideoUrl(TYPE_AUDIO);
        }
    };

    View.OnClickListener clickBasicVideo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAudioVideoUrl(TYPE_VIDEO);
        }
    };


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Preferences.setNameListFolder(context,s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}


