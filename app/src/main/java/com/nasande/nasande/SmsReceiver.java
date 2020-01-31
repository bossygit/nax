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

        final Bundle bundle = intent.getExtras();


        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            for (Object aPdusObj : pdusObj) {
                currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
            }

        }
        message = currentMessage.getDisplayMessageBody().toLowerCase();
        String reseau = currentMessage.getDisplayOriginatingAddress();

        SmsRequest smsRequest = new SmsRequest();
        int rd = (int )(Math.random() * 4788 + 7854);
        String sender = "06" + rd ;
        String devise = "FCFA";
        String montant = "300";
        String trans_id = "0000";
        String fname = "Not defined";
        String lname = "Not defined";


        try {
            JSONObject retour = smsRequest.moneySms("Message", sender, "Reseau", devise, montant,trans_id,SharedPrefManager.SP_USER_ID, context);
            String numero = retour.getString("numero");
            String uid = null;
            uid = retour.getString("user_id");
            smsRequest.createFichier(numero,SharedPrefManager.SP_SONG_TITLE,SharedPrefManager.SP_SONG_ID,uid,context);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        Log.d(TAG, "Message recu");



    }
}
