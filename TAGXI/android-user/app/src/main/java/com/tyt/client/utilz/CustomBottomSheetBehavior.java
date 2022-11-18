package com.tyt.client.utilz;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

/**
 * Custom Bottom Sheet Callback class to intercept the touch events in Bottom Sheets
 */

public class CustomBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {


    private boolean enableCollapse = true;

    public CustomBottomSheetBehavior() {
    }

    public CustomBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableCollapse(boolean enableCollapse) {
        this.enableCollapse = enableCollapse;
    }

    @Override
    public boolean onInterceptTouchEvent(@NotNull CoordinatorLayout parent, @NotNull V child, @NotNull MotionEvent event) {
       /* if (enableCollapse) {
            return false;
        }*/
        return super.onInterceptTouchEvent(parent, child, event);
    }
}
