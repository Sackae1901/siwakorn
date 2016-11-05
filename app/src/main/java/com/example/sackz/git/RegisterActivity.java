package com.example.sackz.git;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.service.voice.VoiceInteractionSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText eddisplaynameRE;
    private EditText edusernameRE;
    private EditText edconpasswordRE;
    private EditText edpasswordRE;
    private Button btnregister;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindWidget();
        setListener();

    }

    private void setListener() {
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    new Register(edusernameRE.getText().toString(),
                            edconpasswordRE.getText().toString(),
                            eddisplaynameRE.getText().toString(),
                            edpasswordRE.getText().toString());

                } else {
                    Toast.makeText(RegisterActivity.this, "กรุณากรองข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean validate() {
        String username = edusernameRE.getText().toString();
        String password = edpasswordRE.getText().toString();
        String passwordConfirm = edconpasswordRE.getText().toString();
        String displayName = eddisplaynameRE.getText().toString();

        if (username.isEmpty())
            return false;

        if (password.isEmpty())
            return false;

        if (passwordConfirm.equals(password))
            return false;

        if (displayName.isEmpty())
            return false;

        return true;
    }
    private void bindWidget(){
        eddisplaynameRE = (EditText) findViewById(R.id.eddisplaynameRE);
        edusernameRE = (EditText) findViewById(R.id.edusernameRE);
        edconpasswordRE = (EditText) findViewById(R.id.edconpasswordRE);
        edpasswordRE = (EditText) findViewById(R.id.edpasswordRE);
        btnregister = (Button) findViewById(R.id.btnregister);
    }

    private class Register extends AsyncTask<Void, Void, String> {

        private String username;
        private String password;
        private String passwordCon;
        private String displayName;

        public Register(String password, String username, String passwordCon, String displayName) {
            this.password = password;
            this.username = username;
            this.passwordCon = passwordCon;
            this.displayName = displayName;
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request;
            Response response;
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("passwordCon", passwordCon)
                    .add("displayName", displayName)
                    .build();
            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requestBody)
                    .build();
            try {

                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    return response.body().string();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject rootObj = new JSONObject(s);
                if (rootObj).has("result"){
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    if (resultObj.getInt("result")==1) {
                        Toast.makeText(RegisterActivity.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT);
                        finish();

                    }else {
                        Toast.makeText(RegisterActivity.this,resultObj.getString("result_desc"),Toast.LENGTH_SHORT);
                    }
                }
            }catch (JSONException ex){
            Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_SHORT).show();
        }
    }
}}


