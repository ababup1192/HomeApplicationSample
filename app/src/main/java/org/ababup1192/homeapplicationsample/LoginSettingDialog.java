package org.ababup1192.homeapplicationsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class LoginSettingDialog {

    public static void show(final Context context, final Activity activity) {
        String errorMessage = "設定画面へ移動して、「デフォルトでの起動」と書かれた下の「設定を消去」というボタンを押して下さい。\n\n" +
                "その後、「戻る」ボタンを押してミマモールの設定画面に戻って下さい。";
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
        alertDlg.setTitle("ミマモールをデフォルトアプリに設定する");
        alertDlg.setMessage(errorMessage);
        alertDlg.setPositiveButton("設定画面へ移動する",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        Uri uri = Uri.fromParts("package", getResolvePackageName(context), null);
                        intent.setData(uri);
                        // アプリケーション管理の詳細画面を登録する
                        ComponentName comportName = ComponentName
                                .unflattenFromString("com.android.settings/.applications.InstalledAppDetails");
                        intent.setComponent(comportName);
                        context.startActivity(intent);
                    }
                });
        alertDlg.create().show();
    }

    public static String getResolvePackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo appInfo = packageManager.resolveActivity(intent, 0);
        return appInfo.activityInfo.packageName;
    }

}
