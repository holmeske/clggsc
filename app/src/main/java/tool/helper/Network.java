package tool.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者：lvke
 * 创建时间：2017/7/27 17:10
 */

public class Network {
    public static boolean isAvailable(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = network.isAvailable();
        }
        return bisConnFlag;
    }
}
