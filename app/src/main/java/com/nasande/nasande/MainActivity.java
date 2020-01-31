package com.nasande.nasande;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nasande.nasande.model.Fichier;
import com.nasande.nasande.model.Node;
import com.nasande.nasande.model.Title;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final int PICKFILE_RESULT_CODE = 1;
    public  static final int REQ_CODE_PICK_SOUNDFILE = 306;

    private Button btnChooseFile;
    private TextView tvItemPath;
    private EditText mTitre;
    SharedPrefManager sharedPrefManager;

    private Uri fileUri;
    private int GALLERY_INTENT_CALLED = 108;
    private String filePath;
    private int fid;

    private ApiService mApiInstance;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 207;

    private static int compte;



    ProgressDialog mProgressDialog;
    private String content_disposition;
    private RequestBody requestBodyByte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager = new SharedPrefManager(this);

        if (!sharedPrefManager.getSPIsLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        btnChooseFile = (Button) findViewById(R.id.btn_choose_file);
        tvItemPath = (TextView) findViewById(R.id.tv_file_path);
        mTitre = findViewById(R.id.titre);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Envois ...");


        btnChooseFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getMyPerms(); /* Got the permission bug removed */
                showDialog(); //TODO 1 show dialog after permissions are granted




                if (Build.VERSION.SDK_INT <19){
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("image/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choisir un fichier");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                } else {
                    /*
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                     */
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/mpeg");
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.nsd_get_audio)), REQ_CODE_PICK_SOUNDFILE);


                }




            }
        });
    }
    @AfterPermissionGranted(1000)
    private void getMyPerms(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(MainActivity.this,perms)){

            Toast.makeText(MainActivity.this, "Permissions granted", Toast.LENGTH_SHORT).show();

        }

        else {
            EasyPermissions.requestPermissions(MainActivity.this,"Nous avons besoin de recupere le fichier",1000,perms);
        }
    }
    private int sendAudioFile(String filePath){




        mApiInstance = new RetrofitInstance().ObtenirInstance();
        //String fileName ="/storage/emulated/0/DCIM/Camera/IMG_20191231_065522.jpg";
        File file = new File(filePath);
        String filename=filePath.substring(filePath.lastIndexOf("/")+1);
        content_disposition = "file;filename=\"" + filename + "\"";



        try {
            InputStream fileInputStream = new FileInputStream(
                    filePath);
            byte[] buf = new byte[fileInputStream.available()];
            while (fileInputStream.read(buf) != -1) ;
            requestBodyByte = RequestBody
                    .create(MediaType.parse("application/octet-stream"), buf);

        } catch (FileNotFoundException e) {
            hideDialog();
            e.printStackTrace();
        } catch (IOException e) {
            hideDialog();
            e.printStackTrace();
        }
        Call<ResponseBody> call = mApiInstance.postAudio(sharedPrefManager.getSPBasicAuth(),sharedPrefManager.getSPCsrfToken(),content_disposition,requestBodyByte);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Success audio", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    try {
                        String reponse = response.body().string();
                        JSONObject jsonRESULTS = new JSONObject(reponse);
                        fid = jsonRESULTS.getJSONArray("fid").getJSONObject(0).getInt("value");

                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SONG_ID, ""+ fid);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_SONG_TITLE, mTitre.getText().toString());

                        createNode(fid);





                        Log.d("MainActivity","Id : " + fid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "Error audio", Toast.LENGTH_SHORT).show();
                    hideDialog();


                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
        return fid;
    }

    private void createNode(int fid){
        showDialog();
        ArrayList<Title> title = new ArrayList<>();

        title.add(0, new Title(mTitre.getText().toString()));
        ArrayList<Fichier>  field_fichier_audio = new ArrayList<>();
        field_fichier_audio.add(0,new Fichier(24));

        Node node = new Node(title,field_fichier_audio);
        mApiInstance = new RetrofitInstance().ObtenirInstance();

        Call<ResponseBody> call =  mApiInstance.addNode(sharedPrefManager.getSPBasicAuth(),sharedPrefManager.getSPCsrfToken(),node);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Content created", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    try {
                        String reponse = response.body().string();
                        Log.d("MainActivity",reponse);
                        JSONObject jsonRESULTS = new JSONObject(reponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "Error non created", Toast.LENGTH_SHORT).show();
                    hideDialog();


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
    }

    private void sendFile(String filePath){


        mApiInstance = new RetrofitInstance().ObtenirInstance();
        String fileName ="/storage/emulated/0/DCIM/Camera/IMG_20191231_065522.jpg";
        File file = new File(filePath);
        String filename=filePath.substring(filePath.lastIndexOf("/")+1);
        content_disposition = "file;filename=\"" + filename + "\"";



        try {
            InputStream fileInputStream = new FileInputStream(
                    fileName);
            byte[] buf = new byte[fileInputStream.available()];
            while (fileInputStream.read(buf) != -1) ;
            requestBodyByte = RequestBody
                    .create(MediaType.parse("application/octet-stream"), buf);

        } catch (FileNotFoundException e) {
            hideDialog();
            e.printStackTrace();
        } catch (IOException e) {
            hideDialog();
            e.printStackTrace();
        }
        Call<ResponseBody> call = mApiInstance.postFile(sharedPrefManager.getSPBasicAuth(),sharedPrefManager.getSPCsrfToken(),content_disposition,requestBodyByte);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    try {
                        String reponse = response.body().string();
                        Log.d("MainActivity",reponse);
                        JSONObject jsonRESULTS = new JSONObject(reponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    hideDialog();


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, List perms) {
        // Add your logic here
    }

    @Override
    public void onPermissionsDenied(int requestCode, List perms) {
        // Add your logic here
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            FilesHelper fH = new FilesHelper();
            switch (requestCode) {
                case PICKFILE_RESULT_CODE:
                    if (resultCode == -1) {
                        fileUri = data.getData();
                        filePath = fileUri.getPath();
                        tvItemPath.setText(filePath);
                        //fileUpload(fileUri);
                        Toast.makeText(MainActivity.this, "Lower 19", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case GALLERY_KITKAT_INTENT_CALLED:



                    fileUri = data.getData();

                    String file = fH.sendPath(fileUri,MainActivity.this);

                    tvItemPath.setText(file);
                    sendFile(file);




                    break;

                case REQ_CODE_PICK_SOUNDFILE:



                    fileUri = data.getData();

                    String audioFile = fH.sendAudioPath(fileUri,MainActivity.this);

                    tvItemPath.setText(audioFile);
                    int id = sendAudioFile(audioFile);





                    break;
                default:
                    Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void showDialog() {

        if(mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideDialog() {

        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
    public void fileUpload(Uri fileUri){
        showDialog();
        File file = new File(fileUri.getPath());
        mApiInstance = new RetrofitInstance().ObtenirInstance();
        String fileName ="/storage/emulated/0/DCIM/Camera/IMG_20191231_065522.jpg";
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        Call<ResponseBody> call = mApiInstance.upload(description,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                hideDialog();
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}