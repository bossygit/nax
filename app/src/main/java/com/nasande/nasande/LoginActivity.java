package com.nasande.nasande;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mUserView;
    private TextInputEditText mPasswordView;
    private Button mConnexion;

    SharedPrefManager sharedPrefManager;
    private ApiService mApiInstance;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getSPIsLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mConnexion = findViewById(R.id.login_btn);
        mUserView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Connexion ...");

        mConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    public void showDialog() {

        if(mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideDialog() {

        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void attemptLogin() {
        showDialog();
        final String email = mUserView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        mApiInstance = new RetrofitInstance().ObtenirInstance();

        mApiInstance.loginRequest(new LoginData(email, password))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            hideDialog();

                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (!jsonRESULTS.getString("csrf_token").isEmpty()) {
                                    String csrf_token = jsonRESULTS.getString("csrf_token");
                                    String logout_token = jsonRESULTS.getString("logout_token");
                                    String user_id = jsonRESULTS.getJSONObject("current_user").getString("uid");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_CSRF_TOKEN, csrf_token);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LOGOUT_TOKEN, logout_token);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USER_ID, user_id);
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_LOGGED_IN, true);
                                    String basic_auth = email + ":" + password;
                                    byte[] bytes_basic_auth = basic_auth.getBytes();
                                    String encoded_basic_auth = android.util.Base64.encodeToString(bytes_basic_auth, android.util.Base64.DEFAULT);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_BASIC_AUTH, "Basic " + encoded_basic_auth.trim());

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(LoginActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}
