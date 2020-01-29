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



        Log.d(TAG, "Message recu");

        if (message.contains("recu") ){
            Log.d(TAG, "Message mtn");

            CheckSms checkSms = new CheckSms();
            String numero_expediteur = checkSms.getNumber(message);
            String montant_recu = checkSms.getAmount(message);
            String nom_expediteur = checkSms.getAmount(message);

            ArrayList<Title> title = new ArrayList<>();
            ArrayList<Amount> field_amount = new ArrayList<>();
            ArrayList<Telephone> field_telephone_number = new ArrayList<>();

            title.add(0,new Title("Transfert recu de " + nom_expediteur));
            field_amount.add(0,new Amount(montant_recu));
            field_telephone_number.add(0,new Telephone(numero_expediteur));

            Notification notification = new Notification(title,field_amount,field_telephone_number);

            mApiInstance = new RetrofitInstance().ObtenirInstance();

        }

    }
}
