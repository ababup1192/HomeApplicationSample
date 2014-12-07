package org.ababup1192.homeapplicationsample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        Button loginButton = (Button) findViewById(R.id.button_login);

        isLogout = sharedPreferences.getBoolean("is_logout", false);

        if (isLogout) {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(LoginActivity.this, HomeActivity.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            // 候補から(通常時の)ホームアプリを選ばせる。
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putBoolean("is_login", true);
                editor.apply();
                PackageManager packageManager = getPackageManager();
                ComponentName componentName = new ComponentName(LoginActivity.this, HomeActivity.class);
                // HomeActivity(アプリ自身を)をホームボタンの候補に入れる。
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                // 候補からホームアプリを選ばせる。
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isLogout) {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(LoginActivity.this, HomeActivity.class);
            editor = sharedPreferences.edit();
            // ログアウト処理の終了
            editor.putBoolean("is_logout", false);
            editor.apply();
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
