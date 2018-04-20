package com.example.mingw.restaurant.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mingw.restaurant.R;
import okhttp3.FormBody;

import static com.example.mingw.restaurant.utils.HttpUtil.postFormByOkHttp;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private String password;
    private String serverIP;
    private String server;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Button buttonLogin = (Button) findViewById(R.id.bt_login_login_button);
        Button buttonSignin = (Button) findViewById(R.id.bt_login_signin_button);

        final EditText usernameInput = (EditText) findViewById(R.id.et_login_username);
        final EditText passwordInput = (EditText) findViewById(R.id.et_login_password);
        final EditText serverInput = (EditText) findViewById(R.id.et_login_server);
        final InputMethodManager inputMethodManager =(InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        final CheckBox checkBoxRememberPassword = (CheckBox) findViewById(R.id.cbRememberPassword);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        serverIP = pref.getString("server_url", "192.168.199.194");
        server = "http://" + serverIP + ":8080/food/login";

        boolean isRemember =pref.getBoolean("remember_password", false);

        checkBoxRememberPassword.setChecked(isRemember);
        if (isRemember){
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            usernameInput.setText(username);
            passwordInput.setText(password);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                inputMethodManager.hideSoftInputFromWindow(passwordInput.getWindowToken(), 0);
                if (!username.isEmpty() && !password.isEmpty()) {
                    editor = pref.edit();
                    if (checkBoxRememberPassword.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("server_url", serverInput.getText().toString());
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    ProgressDialog progressDialog =  new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("登录中");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new Thread(new LoginThread()).start();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名与密码不匹配", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private class LoginThread implements Runnable {
        String responseData;
        @Override public void run() {
            FormBody formBody = new FormBody.Builder().build();
            formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
            responseData = postFormByOkHttp(server, formBody);
            handler.post(new Runnable() {
                @Override public void run() {
                    editor = pref.edit();
                    editor.putString("current_username", username);
                    if (responseData.equals("success")) {
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        editor.clear();
                    }
                }
            });
        }
    }
}
