package com.example.mingw.restaurant.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.example.mingw.restaurant.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.mingw.restaurant.utils.HttpUtil.postFormByOkHttp;

public class SigninActivity extends AppCompatActivity {

    private String mUsernameText;
    private String mPasswordText;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_signin_toolbar);
        setSupportActionBar(toolbar);
        final EditText mUsername = (EditText) findViewById(R.id.et_signin_username);
        final EditText mPassword = (EditText) findViewById(R.id.et_signin_password);
        final EditText mPasswordRepeat = (EditText) findViewById(R.id.et_signin_password_repeat);
        final InputMethodManager inputMethodManager =(InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);

        Button mButtonSignin = (Button) findViewById(R.id.bt_signin_signin_button);
        mButtonSignin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mUsernameText = mUsername.getText().toString();
                mPasswordText = mPassword.getText().toString();
                String mPasswordRepeatText = mPasswordRepeat.getText().toString();
                inputMethodManager.hideSoftInputFromWindow(mPasswordRepeat.getWindowToken(), 0);
                if (!mUsernameText.isEmpty()&&!mPasswordText.isEmpty()&&!mPasswordRepeatText.isEmpty()) {
                    //TODO 验证联网注册功能
                    if (mPasswordText.equals(mPasswordRepeatText)) {
                        new Thread(new SigninThread()).start();
                    } else {
                        Toast.makeText(SigninActivity.this, "两次输入的密码不一致\n"
                                + mPasswordText + "\n"
                                + mPasswordRepeatText,
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SigninActivity.this, "无效的用户名或密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SigninThread implements Runnable {
        String responseData;
        String url = "http://192.168.199.194:8080/food/signin";
        @Override public void run() {
            try {
                FormBody formBody = new FormBody.Builder().build();
                formBody = new FormBody.Builder()
                    .add("newusername", mUsernameText)
                    .add("newpassword", mPasswordText)
                    .build();
                responseData = postFormByOkHttp(url, formBody);
                handler.post(new Runnable() {
                    @Override public void run() {
                        if (responseData.equals("success")) {
                            Toast.makeText(SigninActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SigninActivity.this, LoginActivity.class));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
