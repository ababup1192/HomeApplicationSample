package org.ababup1192.homeapplicationsample.activity.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * デフォルトホームアプリケーションを解除するためのユーティリティクラス
 */
public class HomeApplicationUtil {

    /**
     * デフォルトホームアプリケーションの解除を促す(設定画面へ飛ばす)ダイアログを表示
     *
     * @param context  設定画面へ飛ばすIntent用Context
     * @param activity AlertDialog用Activity
     */
    public static void showAlertDialog(final Context context, final Activity activity) {
        String message = "設定画面へ移動して、「デフォルトでの起動」と書かれた下の「設定を消去」というボタンを押して下さい。\n\n" +
                "その後、「戻る」ボタンを押してアプリのログイン画面に戻って下さい。";
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(activity);
        alertDlg.setTitle("デフォルトアプリに設定する");
        alertDlg.setMessage(message);
        alertDlg.setPositiveButton("設定画面へ移動する",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // 現在のホームアプリのパッケージ名をIntentに指定
                        Uri uri = Uri.fromParts("package", getHomeAppPackageName(context), null);
                        intent.setData(uri);
                        // そのアプリの設定画面をIntentに指定
                        ComponentName comportName = ComponentName
                                .unflattenFromString("com.android.settings/.applications.InstalledAppDetails");
                        intent.setComponent(comportName);
                        // 設定画面へ遷移
                        context.startActivity(intent);
                    }
                });
        alertDlg.create().show();
    }

    /**
     * デフォルトアプリがないかチェック
     *
     * @param context Context
     * @return なければtrue
     */
    public static boolean isNoDefaultHomeApplication(Context context) {
        Intent filter = new Intent(Intent.ACTION_MAIN, null);
        filter.addCategory(Intent.CATEGORY_HOME);

        String currentHomeAppPackName = context.getPackageManager().resolveActivity(filter, 0).activityInfo.packageName;
        // 現在のデフォルトパッケージ名とターゲットのパッケージ名が一致しているか、デフォルトアプリが設定されてなければtrue
        return currentHomeAppPackName.equals("android");
    }

    /**
     * ターゲットのアプリがデフォルトホームアプリになっているかチェック
     *
     * @param context ターゲットアプリのContext
     * @return なっていればtrue
     */
    public static boolean isDefaultHomeApplication(Context context) {
        Intent filter = new Intent(Intent.ACTION_MAIN, null);
        filter.addCategory(Intent.CATEGORY_HOME);

        String targetPackageName = context.getPackageName();
        String currentHomeAppPackName = context.getPackageManager().resolveActivity(filter, 0).activityInfo.packageName;
        // 現在のデフォルトパッケージ名とターゲットのパッケージ名が一致しているか、デフォルトアプリが設定されてなければtrue
        return currentHomeAppPackName.equals(targetPackageName);
    }

    /**
     * 現在のホームアプリのパッケージ名取得
     *
     * @param context PackageManager用Context
     * @return PackageName
     */
    public static String getHomeAppPackageName(Context context) {
        Intent filter = new Intent(Intent.ACTION_MAIN, null);
        filter.addCategory(Intent.CATEGORY_HOME);

        return context.getPackageManager().resolveActivity(filter, 0).activityInfo.packageName;
    }

}
