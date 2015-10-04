package samaritan.wearhacks.ca.subwaysamaritan;

/**
 * Created by HenryChiang on 15-10-03.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;


import java.util.Hashtable;

public class FontManager {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(Context context) {
        String fontType = "fonts/magdacleanmono-regular.otf";
        Typeface typeface = fontCache.get(fontType);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontType);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontType, typeface);
        }

        return typeface;
    }


    public static Typeface settingFont(Context ctx, AttributeSet attrs, int[] i , int j) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, i);
        String customFont = a.getString(j);
        Typeface tf = null;
        try {
            tf = getTypeface(ctx);
        } catch (Exception e) {
            a.recycle();
            return null;
        }
        a.recycle();
        return tf;

    }
}