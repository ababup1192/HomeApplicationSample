package org.ababup1192.homeapplicationsample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isLogout;
    Context context;
    Activity activity;
    public final static int CURRENT_ONLY = 0;
    public final static int OR_ANDROID = 1;
    public final static int ANDROID_ONLY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        activity = this;
        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        Button loginButton = (Button) findViewById(R.id.button_login);

        isLogout = sharedPreferences.getBoolean("is_logout", false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager packageManager = getPackageManager();
                if (isResolveActivity(context, OR_ANDROID)) {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("is_login", true);
                    editor.apply();

                    ComponentName componentName = new ComponentName(LoginActivity.this, LoginActivityDummy.class);
                    // HomeActivity(アプリ自身を)をホームボタンの候補に入れる。
                    // 候補からホームアプリを選ばせる。
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    ComponentName componentName = new ComponentName(LoginActivity.this, LoginActivityDummy.class);
                    // HomeActivity(アプリ自身を)をホームボタンの候補に入れる。
                    // 候補からホームアプリを選ばせる。
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                    LoginSettingDialog.show(context, activity);
                }
            }
        });
    }

    boolean isMyLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);
        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
            if (myPackageName.equals(activity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isResolveActivity(Context context, int flag) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo appInfo = packageManager.resolveActivity(intent, 0);
        String packageName = appInfo.activityInfo.packageName;
        String currentPackageName = context.getPackageName();

        if (packageName.equals(currentPackageName) && flag == CURRENT_ONLY)
            return true;
        else if (packageName.equals(currentPackageName)
                || packageName.equals("android") && flag == OR_ANDROID)
            return true;
        else if (packageName.equals("android") && flag == ANDROID_ONLY)
            return true;
        else
            return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("home_application", Activity.MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("is_login", false);
        boolean isLogout = sharedPreferences.getBoolean("is_logout", false);

        // デフォルトログインチェック
        if (isMyLauncherDefault() && isLogin) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        if (isLogout) {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(LoginActivity.this, LoginActivityDummy.class);

            // ログアウト処理の終了
            editor = sharedPreferences.edit();
            editor.putBoolean("is_logout", false);
            editor.apply();

            // HomeActivity(アプリ自身を)をホームボタンの候補に入れる。
            // 候補からホームアプリを選ばせる。
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        isLogout = sharedPreferences.getBoolean("is_logout", false);

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
