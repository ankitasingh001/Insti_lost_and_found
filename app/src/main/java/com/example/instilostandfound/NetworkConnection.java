package com.example.instilostandfound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Class that contains various methods for network connection checking and displays messages accordingly
 */
public class NetworkConnection {
    public static final String ERR_DIALOG_TITLE = "No internet connection detected !";
    private static final String ERR_DIALOG_MSG = "Please check your device's network settings.";
    private static final String ERR_DIALOG_POSITIVE_BTN = "Settings";
    private static final String ERR_DIALOG_NEGATIVE_BTN = "Dismiss";

    /**
     * Check whether device is connected to internet
     * @param context the class object
     * @return true or false value
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Check whether devide is connected to wifi
     * @param context the class object
     * @return true or false value
     */
    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Check whether device is connected to internet via mobile network
     * @param context the class object
     * @return true or false value
     */
    public static boolean isConnectedToMobileNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * Method that displays dialog in case no internet is available
     * @param context class object
     */
    public static void showNoInternetAvailableErrorDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(ERR_DIALOG_TITLE)
                .setMessage(ERR_DIALOG_MSG)
                .setIcon(R.drawable.internet)
                .setPositiveButton(ERR_DIALOG_POSITIVE_BTN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(ERR_DIALOG_NEGATIVE_BTN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (isConnectedToInternet(context)
                                || isConnectedToMobileNetwork(context)
                                || isConnectedToWifi(context)) {

                        } else {
                            showNoInternetAvailableErrorDialog(context);
                            return;
                        }
                    }
                })
                .show();
    }
}
