package com.venomxc45.serverapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText title, description, tag;
    private Button register;
    private ProgressDialog progressDialog;
    private String fileDownloadPath = Constants.ROOT_URL + "json_convert/events.json";
    private String fileSavePath = "/" + Environment.getExternalStorageDirectory()+ "/Android/data/com.venomxc45.powwow/events.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (EditText) findViewById(R.id.etTitle);
        description = (EditText) findViewById(R.id.etDescription);
        tag = (EditText) findViewById(R.id.etTag);
        register = (Button) findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("MyApp", "No SDCARD");
        }
        else {
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Android/data/com.venomxc45.powwow");
            directory.mkdirs();
        }

        makeJson();
        downloadJSON();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Registering Event");
                progressDialog.show();
                makeEvent();
            }
        });
    }
    public void makeEvent(){
        final String _title = title.getText().toString();
        final String _description = description.getText().toString();
        final String _tag = tag.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    makeJson();
                    downloadJSON();
                }
                catch (JSONException e){
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    makeJson();
                    downloadJSON();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                downloadJSON();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("title",_title);
                params.put("description",_description);
                params.put("tag",_tag);
                return params;
            }
        };


                /*RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);*/

        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }
    public void makeJson(){
        StringRequest json = new StringRequest(Request.Method.POST, Constants.URL_JSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                downloadJSON();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(json);
    }

    public void downloadJSON(){
        try {
            File file = new File(fileSavePath);
            URL u = new URL(fileDownloadPath);
            URLConnection con = u.openConnection();
            int lengthOfContent = con.getContentLength();

            DataInputStream dataInputStream = new DataInputStream(u.openStream());
            byte[] buffer = new byte[lengthOfContent];
            dataInputStream.readFully(buffer);
            dataInputStream.close();

            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            dataOutputStream.write(buffer);
            dataOutputStream.flush();
            dataOutputStream.close();
            Toast.makeText(this, fileDownloadPath + fileSavePath, Toast.LENGTH_LONG).show();
        }

        catch (Exception e){

            Toast.makeText(MainActivity.this, e.getMessage() + "something is not working", Toast.LENGTH_LONG).show();
        }

    }
}
