package com.tyt.client.utilz;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tyt.client.ui.homeScreen.HomeAct;
import com.tyt.client.ui.homeScreen.HomeViewModel;
import com.tyt.client.ui.homeScreen.mapFrag.MapFrag;

import org.jetbrains.annotations.NotNull;

public class TouchableWrapper extends FrameLayout {

    private long lastTouched = 0;
    private static final long SCROLL_TIME = 150L;
    private final UpdateMapAfterUserInterection updateMapAfterUserInterection;

    public TouchableWrapper(Context context) {
        super(context);
        try {
            updateMapAfterUserInterection = (UpdateMapAfterUserInterection) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.e("TOuched==", "Downtouch");
                lastTouched = SystemClock.uptimeMillis();
                MapFrag.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                Log.e("TOuched==", "UPtouch");
                final long now = SystemClock.uptimeMillis();
                if (now - lastTouched > SCROLL_TIME) {
                    // Update the map
                    updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
                }
                MapFrag.mMapIsTouched = true;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public interface UpdateMapAfterUserInterection {
        public void onUpdateMapAfterUserInterection();
    }
}
