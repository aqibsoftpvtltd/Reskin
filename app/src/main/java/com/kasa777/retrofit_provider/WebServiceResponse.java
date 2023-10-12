package com.kasa777.retrofit_provider;

import android.content.Context;
import android.util.Log;

import com.kasa777.utils.Alerts;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

public class WebServiceResponse {

    public static void handleResponse(Context mContext, Response<?> response, WebResponse webResponse) {
        Log.v("RESPONSE_CODE", String.valueOf(response.code()));
        Log.v("RESPONSE", String.valueOf(response));
        if (response.isSuccessful()) {
            if (response.body() != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getInt("status") == 55) {
                        Alerts.SessionLogout(mContext, null);
                    }else
                        webResponse.onResponseSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    webResponse.onResponseSuccess(response);
                }





            } else {
                webResponse.onResponseFailed(response.message());
            }
        } else {
            try {
                if (response.errorBody() != null) {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    webResponse.onResponseFailed(jObjError.getString("error"));
                } else {
                    webResponse.onResponseFailed(response.message());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}