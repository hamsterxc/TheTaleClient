package com.lonebytesoft.thetaleclient.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class WrappingViewPager extends ViewPager {

    public WrappingViewPager(Context context) {
        super(context);
    }

    public WrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int viewPagerWidth = 0;
//        int viewPagerHeight = 0;
//        for(int i = getChildCount() - 1; i >= 0; i--) {
//            final View child = getChildAt(i);
//            child.measure(
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//
//            final int childWidth = child.getMeasuredWidth();
//            if(childWidth > viewPagerWidth) {
//                viewPagerWidth = childWidth;
//            }
//
//            final int childHeight = child.getMeasuredHeight();
//            if(childHeight > viewPagerHeight) {
//                viewPagerHeight = childHeight;
//            }
//        }
//
//        super.onMeasure(
//                MeasureSpec.makeMeasureSpec(viewPagerWidth, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(viewPagerHeight, MeasureSpec.EXACTLY));
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewPagerHeight = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            final int childHeight = child.getMeasuredHeight();
            if(childHeight > viewPagerHeight) {
                viewPagerHeight = childHeight;
            }
        }

        super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(viewPagerHeight, MeasureSpec.EXACTLY));
    }

}
