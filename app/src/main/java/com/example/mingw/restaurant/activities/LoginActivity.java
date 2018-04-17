package com.example.mingw.restaurant.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.example.mingw.restaurant.R;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.example.mingw.restaurant.utils.HttpUtil.postFormByOkHttp;
import static com.example.mingw.restaurant.utils.HttpUtil.sendOkHttpRequest;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_login_toolbar);
        setSupportActionBar(toolbar);
        Button buttonLogin = (Button) findViewById(R.id.bt_login_login_button);
        Button buttonSignin = (Button) findViewById(R.id.bt_login_signin_button);
        final EditText editTextUsername = (EditText) findViewById(R.id.et_login_username);
        final EditText editTextPassword = (EditText) findViewById(R.id.et_signin_password);
        final InputMethodManager inputMethodManager =(InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        final CheckBox checkBoxRememberPassword = (CheckBox) findViewById(R.id.cbRememberPassword);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember =pref.getBoolean("remember_password", false);
        checkBoxRememberPassword.setChecked(isRemember);
        if (isRemember){
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            editTextUsername.setText(username);
            editTextPassword.setText(password);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                inputMethodManager.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
                if (!username.isEmpty() && !password.isEmpty()) {
                    editor = pref.edit();
                    if (checkBoxRememberPassword.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    new Thread(new LoginThread()).start();
                } else {
                    Toast.makeText(LoginActivity.this, "invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
            }
        });
    }

    private class LoginThread implements Runnable {
        String responseData;
        String url = "http://192.168.199.194:8080/food/login";
        @Override public void run() {
            FormBody formBody = new FormBody.Builder().build();
            formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
            responseData = postFormByOkHttp(url, formBody);
            handler.post(new Runnable() {
                @Override public void run() {
                    Toast.makeText(LoginActivity.this, "resp" + responseData, Toast.LENGTH_SHORT).show();
                    if (responseData.equals("success")) {
                        editor = pref.edit();
                        editor.putString("current_username", username);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
            });
        }
    }
}
