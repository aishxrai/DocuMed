package in.aishxrai.documed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = pref.edit();



        new Handler().postDelayed(() -> {
            boolean isLoggedIn = pref.getBoolean("isLoggedIn", false);
            if (isLoggedIn) {
                startActivity(new Intent(SplashActivity.this, AddActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}