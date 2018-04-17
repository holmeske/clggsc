package tool.helper;

import android.text.Html;
import android.text.Spanned;

/**
 * Author：lvke
 * CreateDate：2017/8/23 16:03
 */

public final class Compat {

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

}
