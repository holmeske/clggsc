package com.sc.clgg.tool.helper;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import java.net.URL;

/**
 * Author：lvke
 * CreateDate：2017/8/23 16:03
 */

public final class Compat {

    static Html.ImageGetter imageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable;
            URL url;
            try {
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
    };

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
        } else {
            return Html.fromHtml(source, imageGetter, null);
        }
    }
}
