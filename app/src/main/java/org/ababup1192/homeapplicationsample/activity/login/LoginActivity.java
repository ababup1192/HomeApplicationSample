package org.ababup1192.homeapplicationsample.activity.login;

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

import org.ababup1192.homeapplicationsample.activity.HomeActivity;
import org.ababup1192.homeapplicationsample.activity.HomeControlActivity;
import org.ababup1192.homeapplicationsample.R;

/**
 * ログイン用Activity
 */
public class LoginActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        Button loginButton = (Button) findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    /**
     * アプリをデフォルトホームアプリに設定後にHomeActivityへ遷移
     */
    public void login() {
        // 自アプリをホームアプリ選択の候補に入れる。
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(LoginActivity.this, HomeControlActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        // もし、デフォルトのホームアプリが設定されてなければ
        if (HomeApplicationUtil.isNoDefaultHomeApplication(getApplicationContext())) {
            editor = sharedPreferences.edit();
            editor.putBoolean("is_login", true);
            editor.apply();

            // ホームアプリの候補から自アプリを選択させる。
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            // デフォルトのホームアプリの設定をオフにさせる。
            HomeApplicationUtil.showAlertDialog(getApplicationContext(), LoginActivity.this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isLogin = sharedPreferences.getBoolean("is_login", false);
        boolean isLogout = sharedPreferences.getBoolean("is_logout", false);

        // ログインチェック(デフォルトアプリに設定されているか、ログインフラグは立っているか)
        if (HomeApplicationUtil.isDefaultHomeApplication(getApplicationContext()) && isLogin) {
            // HomeActivityへ遷移
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        if (isLogout) {
            // ログアウトフラグを折る
            editor = sharedPreferences.edit();
            editor.putBoolean("is_logout", false);
            editor.apply();

            // 自アプリをホームアプリの候補から外す。
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(LoginActivity.this, HomeControlActivity.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            // 候補からホームアプリを選ばせる。(候補がデフォルトランチャーだけの場合は、自動でデフォルトランチャーへ遷移)
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
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
