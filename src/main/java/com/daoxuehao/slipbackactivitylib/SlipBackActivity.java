package com.daoxuehao.slipbackactivitylib;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ValueAnimator;


/**
 * Created by Yale on 2016/3/16.
 */
public class SlipBackActivity extends FragmentActivity {

    private int mTouchXDown = 0;
    private View mDecorView = null;
    private int mScreenWidth = 0;
    private int mTouchStartX = 20;
    private int mBackVelocityNum = 3000;
    private int mMoveGapTime = 500;
    private float mBackDistenceRateInScreen = 0.5f;
    private boolean mIsCanSlip = true;
    private VelocityTracker mVelocityTracker;
    private boolean mStartMove = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDecorView = (ViewGroup) SlipBackActivity.this.getWindow().getDecorView();

    }
    protected  void setCanSlipBack(boolean bSlip){
        mIsCanSlip = bSlip;
    }
    protected  void setTouchStartX(int x){
        mTouchStartX = x;
    }
    protected  void setBackVelocityNum(int num){
        mBackVelocityNum = num;
    }
    protected  void setMoveGapTime(int gapTime){
        mMoveGapTime = gapTime;
    }
    protected  void setBackDistenceRateInScreen(float rate){
        mBackDistenceRateInScreen = rate;
    }


    private boolean decorViewMove(MotionEvent event) {

        if (isCanSlip()) {

            if (veloctityForward(event)){
                return true;
            }
            int deltX = (int) (event.getRawX() - mTouchXDown);
            mDecorView.scrollTo(-deltX, mDecorView.getScrollY());
            return true;
        }
        return  false;

    }
    private  boolean veloctityForward(MotionEvent event){
        if (mVelocityTracker != null){
            final VelocityTracker velocityTracker = mVelocityTracker;
            velocityTracker.addMovement(event);
            velocityTracker.computeCurrentVelocity(1000);
            int v = (int) velocityTracker.getXVelocity();

            if (v>mBackVelocityNum){
                decorViewForward((int) event.getRawX());
                return true;
            }
        }else{
            return  true;
        }
        return  false;
    }
    private  void decorViewBack(int x){
        mStartMove = true;

        final ValueAnimator animator = ValueAnimator.ofInt(x,0);
        animator.setDuration(mMoveGapTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer integer = (Integer) animator.getAnimatedValue();
                mDecorView.scrollTo(-integer,mDecorView.getScrollY());
                if (integer == 0){
                    mStartMove = false;
                }
            }
        });
        animator.start();
    }

    private  void decorViewForward(final int x){

        mStartMove = true;
        final ValueAnimator animator = ValueAnimator.ofInt(x,mScreenWidth);
        animator.setDuration(mMoveGapTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer integer = (Integer) animator.getAnimatedValue();
                mDecorView.scrollTo(-integer,mDecorView.getScrollY());
                if (integer == mScreenWidth){
                    SlipBackActivity.this.finish();
                }
            }
        });

        animator.start();
    }
    private boolean decorViewMoveDone(int x) {

        if (mTouchXDown < mTouchStartX) {

            if (x < mScreenWidth*mBackDistenceRateInScreen){
                decorViewBack(x);
            }else{
                decorViewForward(x);
            }

            return true;
        }
        return false;
    }

    private  boolean isCanSlip(){
        return mTouchXDown < mTouchStartX;
    }

    private  void initVelocity(MotionEvent event){

        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }else{
            mVelocityTracker.clear();
        }
        mVelocityTracker.addMovement(event);
    }

    private  void releaseVelocity(){
        if (mVelocityTracker != null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (!mIsCanSlip ){
            return super.dispatchTouchEvent(event);
        }
        if (mStartMove){
            return true;
        }

        int x = (int) event.getRawX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchXDown = x;

                boolean canSlip = isCanSlip();
                if (canSlip){
                    initVelocity(event);
                    return  true;
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                boolean ret = decorViewMove(event);
                if (ret){
                    return true;
                }
            }
            case MotionEvent.ACTION_CANCEL:{

                releaseVelocity();

                break;
            }
            case MotionEvent.ACTION_UP: {
                releaseVelocity();
                boolean ret =  decorViewMoveDone(x);
                if (ret){
                    return true;
                }

            }
        }
        return super.dispatchTouchEvent(event);
    }

}
