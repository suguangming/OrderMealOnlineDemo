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

/**
 * SignupActivity class
 * @author guangming
 * @date 2018/04/20
 */
public class SignupActivity extends AppCompatActivity {

    private static Handler handler = new Handler();
    private String mUsernameText;
    private String mPasswordText;
    private String server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText mUsername = findViewById(R.id.et_signup_username);
        final EditText mPassword = findViewById(R.id.et_login_password);
        final EditText mPasswordRepeat = findViewById(R.id.et_signup_password_repeat);
        final InputMethodManager inputMethodManager
            = (InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String serverIP = pref.getString("server_url", "192.168.199.194");
        server = "http://" + serverIP + ":8080/food/signup";

        Button mButtonSignup = findViewById(R.id.bt_signin_signup_button);
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mUsernameText = mUsername.getText().toString();
                mPasswordText = mPassword.getText().toString();
                String mPasswordRepeatText = mPasswordRepeat.getText().toString();
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(mPasswordRepeat.getWindowToken(), 0);
                if (!mUsernameText.isEmpty() && !mPasswordText.isEmpty() &&
                    !mPasswordRepeatText.isEmpty()) {
                    if (mPasswordText.equals(mPasswordRepeatText)) {
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
                    .add("newusername", mUsernameText)
                    .add("newpassword", mPasswordText)
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