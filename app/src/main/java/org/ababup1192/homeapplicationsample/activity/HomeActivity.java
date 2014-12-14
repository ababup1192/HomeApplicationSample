package org.ababup1192.homeapplicationsample.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.ababup1192.homeapplicationsample.R;
import org.ababup1192.homeapplicationsample.activity.login.LoginActivity;

public class HomeActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        Button logoutButton = (Button) findViewById(R.id.button_logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    /**
     * フラグ管理を終えてからLoginActivityへ遷移
     * その後、自アプリのデフォルトホームアプリ設定を解除
     */
    public void logout() {
        editor = sharedPreferences.edit();
        PackageManager packageManager = getPackageManager();
        // ログインフラグを折り、ログアウトフラグを立てる。
        editor.putBoolean("is_login", false);
        editor.putBoolean("is_logout", true);
        editor.apply();
        // LoginActivityへ遷移。
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        // 自アプリのデフォルトホームアプリ設定を解除
        packageManager.clearPackagePreferredActivities(getPackageName());
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                // バックキー無効
                case KeyEvent.KEYCODE_BACK:
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
                default:
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public void onResume() {
        super.onResume();

        boolean isLogin = sharedPreferences.getBoolean("is_login", false);
        // ログインしてなければ
        if (!isLogin) {
            // ログイン画面へ遷移
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
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
