package com.tyt.driver.utilz;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.tyt.driver.ui.homeScreen.mapscrn.MapScrn;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.e("TOuched==", "Downtouch");
                MapScrn.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                Log.e("TOuched==", "UPtouch");
                MapScrn.mMapIsTouched = true;
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
