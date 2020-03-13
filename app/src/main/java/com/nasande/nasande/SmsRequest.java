package com.nasande.nasande;

import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsRequest {


    private static final String URL_POST = "http://nasande.cg/process";
    private static final String URL_FICHIER = "http://nasande.cg/fichier";
    private static final String TAG = SmsRequest.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 6000;
    private static String message = null;
    private static String senderAddress = null;
    private static String devise = null;
    private static String montant = null;
    private static String numero = "";
    private static String titre = null;
    private static String songId = "";
    private static String service = null;
    private static String trans = null;
    private static String fname = null;
    private static String lname = null;
    private static String identifiant = "";
    private static String idUser = "1";
    private Context con  = null;
    SharedPrefManager sharedPrefManager;
    final ArrayList<Integer> simCardList = new ArrayList<>();
    SubscriptionManager subscriptionManager;

    public SmsRequest() {
    }

    public SmsRequest(Context ctxt) {

        sharedPrefManager = new SharedPrefManager(ctxt);
        con = ctxt;
    }

    public void moneySms(String msg, String sender,String operator, String currency, String amount, String trans_id,String UserId, Context context) {
        Log.d(TAG,"Money sms");
        message = msg;
        senderAddress = sender;
        devise = currency;
        montant = amount;
        service = operator;
        trans = trans_id;
        idUser = UserId;






        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int i = jObj.getInt("success");
                            if(i == 1){
                                String pass = jObj.getString("pass");
                                identifiant = jObj.getString("numero");
                                idUser = jObj.getString("id");
                                Log.d(TAG,"Id du client"+jObj.getString("id"));
                                Log.d(TAG,"Numero du client"+jObj.getString("numero"));

                                createFichier(numero,sharedPrefManager.getSpSongTitle(),sharedPrefManager.getSpSongId(),sharedPrefManager.getSPUserId(),con);


                                String mess = "Allez sur https://nasande.cg/user/login pour vous connecter. Nom d'utilisteur : "+identifiant +" Mot de passe : "+pass +" .Merci pour la confiance. \n -Nasande.cg";
                                Log.d(TAG,"Taille "+ mess.length() +" Password" +pass +"identifiant "+identifiant );
                                Log.d(TAG,mess);

                                envoi_sms(mess);





                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("body", message);
                params.put("numero", senderAddress);
                params.put("devise", devise);
                params.put("montant", montant);
                params.put("operator", service);
                params.put("trans_id", trans);
                params.put("user_id", sharedPrefManager.getSPUserId());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);


    }

    private void createFichier(String number, String title,String idSong,String uid, Context context){

        numero = number;
        titre = title;
        songId = idSong;
        idUser = uid;


        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_FICHIER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int i = jObj.getInt("success");
                            if(i == 1){



                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("numero", numero);
                params.put("titre", titre);
                params.put("id", songId);
                params.put("userId", idUser);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    public void envoi_sms(String message) {
        Log.d(TAG,"Envoi de sms au : "+senderAddress);

        subscriptionManager = SubscriptionManager.from(con);
        final List<SubscriptionInfo> subscriptionInfoList;
        try {
            subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                int subscriptionId = subscriptionInfo.getSubscriptionId();
                simCardList.add(subscriptionId);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        int smsToSendFrom = simCardList.get(0);
        SmsManager.getSmsManagerForSubscriptionId(smsToSendFrom).sendTextMessage("+242"+senderAddress,"",message,null,null);


        //SmsManager.getDefault().sendTextMessage("+242"+senderAddress,"", message, null, null);
    }
}

