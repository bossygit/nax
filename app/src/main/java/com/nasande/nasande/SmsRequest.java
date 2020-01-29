package com.nasande.nasande;

import android.content.Context;
import android.telephony.SmsManager;
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

import java.util.HashMap;
import java.util.Map;

public class SmsRequest {
    public SmsRequest() {
    }

    public static final String URL_POST = "http://nasande.cg/process";
    public static final String SUCCESS_MSG = "Transfert reçu, Cliquez sur continuer à l'étape suivante. Merci pour la confiance. \n - Nasande.cg";
    private static final String TAG = SmsRequest.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 6000;
    private static String message = null;
    private static String senderAddress = null;
    private static String devise = null;
    private static String montant = null;
    private static String service = null;
    private static String trans = null;
    private static String fname = null;
    private static String lname = null;

    public void moneySms(String msg, String sender,String operator, String currency, String amount, String trans_id, Context context) {
        message = msg;
        senderAddress = sender;
        devise = currency;
        montant = amount;
        service = operator;
        trans = trans_id;

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int i = jObj.getInt("success");
                            if(i == 1){
                                String pass = jObj.getString("pass");
                                String identifiant = jObj.getString("numero");
                                String mess = "Rendez-vous sur https://nasande.cg/user/login pour vous connecter. Nom d'utilisteur : "+identifiant +" Mot de passe : "+pass +" .Merci pour la confiance. \n -Nasande.cg";
                                Log.d(TAG,"Taille "+ mess.length() +" Password" +pass +"identifiant "+identifiant );
                                Log.d(TAG,mess);

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
        Log.d(TAG,"Numero :"+senderAddress);

        SmsManager.getDefault().sendTextMessage("+242"+senderAddress,"+24206660016", message, null, null);
    }
}

