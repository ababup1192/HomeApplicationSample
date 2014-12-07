package org.ababup1192.homeapplicationsample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HomeActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        Button logoutButton = (Button) findViewById(R.id.button_logout);

        boolean isLogin = sharedPreferences.getBoolean("is_login", false);

        // ログインされていないとき
        if (!isLogin) {
            this.finish();
            // Login Activityへ強制的に飛ばす。
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                ComponentName componentName = new ComponentName(HomeActivity.this, HomeActivity.class);
                editor = sharedPreferences.edit();
                // ログインフラグを折り、ログアウトフラグを立てた状態でログアウト。
                editor.putBoolean("is_login", false);
                editor.putBoolean("is_logout", true);
                editor.apply();
                // Login Activityへ飛ばす。
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                // HomeActivity(アプリ自身を)をホームボタンの候補から外す。
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        });

    }


    // バックキー無効
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
                default:
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
