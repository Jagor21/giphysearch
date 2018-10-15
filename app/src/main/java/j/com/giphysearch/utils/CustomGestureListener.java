package j.com.giphysearch.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final View view;

    public CustomGestureListener(View view) {
        this.view = view;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        if (e1.getY() > e2.getY()){
            return onSwipeUp();
        }

        if (e1.getY() < e2.getY()){
            return onSwipeDown();
        }

        return onTouch();
    }

    public abstract boolean onSwipeUp();
    public abstract boolean onSwipeDown();
    public abstract boolean onTouch();
}
