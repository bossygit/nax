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



        String reseau = currentMessage.getDisplayOriginatingAddress();
        message = currentMessage.getDisplayMessageBody().toLowerCase();

        SmsRequest smsRequest = new SmsRequest(context);



        String devise = "FCFA";

        String trans_id = "0000";
        String fname = "Not defined";
        String lname = "Not defined";

        if(message.contains("recu") && (reseau.equalsIgnoreCase("MobileMoney") || reseau.equalsIgnoreCase("+242064781414") || reseau.equalsIgnoreCase("+242056332008")  ))
        {
            sharedPrefManager = new SharedPrefManager(context);
            CheckSms smss = new CheckSms();
            String sender = smss.getNumberMtn(message);

            String montant = smss.getAmountMTN(message);

            smsRequest.moneySms("Message", sender, "Reseau", devise, montant,trans_id,sharedPrefManager.getSPUserId(), context);


        }

        else if(reseau.equalsIgnoreCase("161") )
        {
            sharedPrefManager = new SharedPrefManager(context);
            int rd = (int )(Math.random() * 4788421 + 7854123);
            message = "Vous avez recu 600 XAF du 24206"+rd+" sur votre compte Mobile Money";
            CheckSms smss = new CheckSms();

            String sender = smss.getNumberMtn(message);

            String montant = smss.getAmountMTN(message);

            smsRequest.moneySms("Message", sender, "Reseau", devise, montant,trans_id,sharedPrefManager.getSPUserId(), context);


        }


    }
}
