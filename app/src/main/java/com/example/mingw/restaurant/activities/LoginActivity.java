package com.example.mingw.restaurant.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.mingw.restaurant.R;
import okhttp3.FormBody;

import static com.example.mingw.restaurant.utils.HttpUtil.postFormByOkHttp;

/**
 * LoginActivity class
 * @author guangming
 * @date 2018/04/20
 */
public class LoginActivity extends AppCompatActivity {

    private static Handler handler = new Handler();
    private String username;
    private String password;
    private String server;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.bt_login_login_button);
        Button buttonSignup = findViewById(R.id.bt_login_signin_button);
        final EditText editTextUsername = findViewById(R.id.et_login_username);
        final EditText editTextPassword = findViewById(R.id.et_login_password);
        final EditText editTextServerIP = findViewById(R.id.et_login_server);
        final InputMethodManager inputMethodManager
            = (InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        final CheckBox checkBoxRememberPassword = findViewById(R.id.cbRememberPassword);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverIP = pref.getString("server_url", "192.168.199.194");
        server = "http://" + serverIP + ":8080/food/login";
        boolean isRemember = pref.getBoolean("remember_password", false);
        checkBoxRememberPassword.setChecked(isRemember);
        if (isRemember) {
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            editTextUsername.setText(username);
            editTextPassword.setText(password);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
                if (!username.isEmpty() && !password.isEmpty()) {
                    editor = pref.edit();
                    if (checkBoxRememberPassword.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("server_url", editTextServerIP.getText().toString());
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    ProgressBar p  = findViewById(R.id.pb_login_login_progress);
                    p.setVisibility(View.VISIBLE);
                    new Thread(new LoginThread()).start();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名与密码不匹配", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }


    private class LoginThread implements Runnable {
        String responseData;


        @Override public void run() {
            final String submitSuccessCode = "success";
            FormBody formBody;
            formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
            responseData = postFormByOkHttp(server, formBody);
            handler.post(new Runnable() {
                @Override public void run() {
                    editor = pref.edit();
                    editor.putString("current_username", username);
                    if (submitSuccessCode.equals(responseData)) {
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
