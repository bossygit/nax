package com.nasande.nasande;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.nasande.nasande.model.Amount;
import com.nasande.nasande.model.Notification;
import com.nasande.nasande.model.Telephone;
import com.nasande.nasande.model.Title;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private static SmsMessage currentMessage = null;
    private static String message = null;
    private ApiService mApiInstance;
    SharedPrefManager sharedPrefManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Message recu", Toast.LENGTH_SHORT).show();

        final Bundle bundle = intent.getExtras();


        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (Object aPdusObj : pdusObj) {
                currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
            }

        }
        message = currentMessage.getDisplayMessageBody().toLowerCase();
        String reseau = currentMessage.getDisplayOriginatingAddress();




    }
}
