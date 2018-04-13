package com.example.mingw.restaurant.Activities;

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
import com.example.mingw.restaurant.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SigninActivity extends AppCompatActivity {

    private String parameter;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        final EditText mUsername = (EditText) findViewById(R.id.etSigninUsername);
        final EditText mPassword = (EditText) findViewById(R.id.etSigninPassword);
        final EditText mPasswordRepeat = (EditText) findViewById(R.id.etSigninPasswordRepeat);
        final InputMethodManager inputMethodManager =(InputMethodManager) this.getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);

        Button mButtonSignin = (Button) findViewById(R.id.btSignin);
        mButtonSignin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String mUsernameText = mUsername.getText().toString();
                String mPasswordText = mPassword.getText().toString();
                String mPasswordRepeatText = mPasswordRepeat.getText().toString();
                inputMethodManager.hideSoftInputFromWindow(mPasswordRepeat.getWindowToken(), 0);
                if (!mUsernameText.isEmpty()&&!mPasswordText.isEmpty()&&!mPasswordRepeatText.isEmpty()) {
                    //TODO 验证联网注册功能
                    if (mPasswordText.equals(mPasswordRepeatText)) {
                        parameter = "?newusername="+mUsernameText + "&newpassword=" + mPasswordText;
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
        String url = "http://192.168.199.194:8080/food/signin" + parameter;
        @Override public void run() {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                    .url(url)
                    .build();
                Response response = client.newCall(request).execute();
                responseData = response.body().string();
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
