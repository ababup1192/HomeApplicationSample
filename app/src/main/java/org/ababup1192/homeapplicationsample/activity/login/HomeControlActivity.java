package org.ababup1192.homeapplicationsample.activity.login;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.ababup1192.homeapplicationsample.R;
import org.ababup1192.homeapplicationsample.activity.login.LoginActivity;

/**
 * デフォルトホームアプリケーションをコントロール(有効・無効)するダミーアクティビティ
 * ホームアプリの候補から選択された場合は、LoginActivityへ遷移(継承利用)
 */
public class HomeControlActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_activity_dummy, menu);
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
