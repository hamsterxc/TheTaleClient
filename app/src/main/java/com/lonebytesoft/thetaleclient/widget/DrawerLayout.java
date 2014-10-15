package com.lonebytesoft.thetaleclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Hamster
 * @since 13.10.2014
 * todo hack for PhotoViewAttacher : https://github.com/chrisbanes/PhotoView/issues/72 , wait for PhotoView 1.2.4
 */
public class DrawerLayout extends android.support.v4.widget.DrawerLayout {

    private static final String LOG_TAG = DrawerLayout.class.getSimpleName();

    public DrawerLayout(Context context) {
        super(context);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Throwable t) {
//            Log.e(LOG_TAG, "onInterceptTouchEvent", t);
            return false;
        }
    }

}
