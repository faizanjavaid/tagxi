package com.tyt.client.utilz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

@SuppressLint("ViewConstructor")
public class TouchEventInterceptorLayout extends FrameLayout {

    public TouchEventInterceptorLayout(@NonNull @NotNull Context context) {
        super(context);
    }

    public TouchEventInterceptorLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = super.dispatchTouchEvent(ev);
        requestDisallowInterceptTouchEvent(true);
        return handled;
    }

}
