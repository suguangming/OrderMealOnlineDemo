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
import android.widget.EditText;
import android.widget.Toast;
import com.example.mingw.restaurant.R;
import okhttp3.FormBody;

import static com.example.mingw.restaurant.utils.HttpUtil.postFormByOkHttp;


public class SignupActivity extends AppCompatActivity {

    private static Handler handler = new Handler();
    private String username;
    private String password;
    private String serverIP;
    private String server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText editTextUsername = findViewById(R.id.et_signup_username);
        final EditText editTextPassword = findViewById(R.id.et_login_password);
        final EditText editTextPasswordRepeat = findViewById(R.id.et_signup_password_repeat);
        final EditText editTextServerIP = findViewById(R.id.et_signup_server);
        final InputMethodManager inputMethodManager
            = (InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        serverIP = pref.getString("server_url", "");
        if (!serverIP.isEmpty()) {
            editTextServerIP.setText(serverIP);
        }
        Button buttonSignup = findViewById(R.id.bt_signup_signup_button);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                serverIP = editTextServerIP.getText().toString();
                server = "http://" + serverIP + ":8080/food/signup";
                String mPasswordRepeatText = editTextPasswordRepeat.getText().toString();
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(editTextPasswordRepeat.getWindowToken(), 0);
                if (!username.isEmpty() && !password.isEmpty() &&
                    !mPasswordRepeatText.isEmpty()) {
                    if (password.equals(mPasswordRepeatText)) {
                        new Thread(new SignupThread()).start();
                    } else {
                        Toast.makeText(SignupActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT)
                            .show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "无效的用户名或密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button buttoBackTonSignin = findViewById(R.id.bt_signup_login_button);
        buttoBackTonSignin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


    /**
     * 在线注册线程
     */
    private class SignupThread implements Runnable {
        String signupSuccessCode = "success";
        String responseData;


        @Override public void run() {
            try {
                FormBody formBody;
                formBody = new FormBody.Builder()
                    .add("newusername", username)
                    .add("newpassword", password)
                    .build();
                responseData = postFormByOkHttp(server, formBody);
                handler.post(new Runnable() {
                    @Override public void run() {
                        if (signupSuccessCode.equals(responseData)) {
                            Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
