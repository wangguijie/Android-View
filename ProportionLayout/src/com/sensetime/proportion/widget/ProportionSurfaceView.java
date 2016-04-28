package com.sensetime.proportion.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by SenseTime on 2015/1/10.
 */
public class ProportionSurfaceView extends SurfaceView {

	private static final boolean DEBUG = true;
	private static final String TAG = "ProportionSurfaceView";
	private int mProportionWidth = 9;
	private int mProportionHeight = 16;
	private Context mContext;
	private int[] mProportion = new int[2];
    private int[] mTranslation = new int[2];
	private ProportionMatch mProportionMatch;
	private int mOrientation;

	public ProportionSurfaceView(Context context) {
		super(context);
		mContext = context;
	}

	public ProportionSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public ProportionSurfaceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.ProportionLayout);
		mOrientation = getResources().getConfiguration().orientation;
		mProportionWidth = a.getInt(
				R.styleable.ProportionLayout_proportion_width, 9);
		mProportionHeight = a.getInt(
				R.styleable.ProportionLayout_proportion_height, 16);
		int matchType = a.getInt(
				R.styleable.ProportionLayout_proportion_matchType, 0);
		mProportionMatch = ProportionMatch.valueToMatchType(matchType);
	}
    
    public void setProportion(int proportionWidth, int proportionHeight){
    	mProportionWidth = proportionWidth;
    	mProportionHeight = proportionHeight;
    	requestLayout();
    }

    public int[] getProportion(){
    	mProportion[0] = mProportionWidth;
    	mProportion[1] = mProportionHeight;
    	return mProportion;
    }
    
    public int[] getTranslation(){
    	return mTranslation;
    }
    
    public void setProportionMatch(ProportionMatch match){
    	mProportionMatch = match;
    	requestLayout();
    }
    
    public ProportionMatch setProportionMatch(){
    	return mProportionMatch;
    }
    
    @Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if((mOrientation - newConfig.orientation) % 2 == 0){
			return;
		}
		mOrientation = newConfig.orientation;
		onOrientationChange();
	}
    
    public void onOrientationChange(){
    	int proportionWidth = mProportionWidth;
    	mProportionWidth = mProportionHeight;
    	mProportionHeight = proportionWidth;
    	switch (mProportionMatch) {
		case MATHC_HEIGHT:
			mProportionMatch = ProportionMatch.MATHC_WIDTH;
			break;
		case MATHC_WIDTH:
			mProportionMatch = ProportionMatch.MATHC_HEIGHT;
			break;
		default:
			break;
		}
    	requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int oldW = width;
        int oldH = height;
        if(DEBUG){
        	Log.d(TAG, "onMeasure oldSize = [" + width + " , " + height + "]" );
        }
        switch (mProportionMatch) {
		case MATHC_WIDTH:
			height = (int) (width * mProportionHeight / (float)mProportionWidth);
			break;
		case MATHC_HEIGHT:
			width = (int) (height * mProportionWidth / (float)mProportionHeight);
		case CENTER_INSIDE:
			if(width/(float)height >= mProportionWidth / (float)mProportionHeight){
				width = (int) (height * mProportionWidth / (float)mProportionHeight);
			}else{
				height = (int) (width * mProportionHeight / (float)mProportionWidth);
			}
			break;
		case FIT_CENTER:
			if(width/(float)height <= mProportionWidth / (float)mProportionHeight){
				width = (int) (height * mProportionWidth / (float)mProportionHeight);
			}else{
				height = (int) (width * mProportionHeight / (float)mProportionWidth);
			}
			break;
		default:
			break;
		}
        mTranslation[0] = (width - oldW)/2;
        mTranslation[1] = (height - oldH)/2;
        mTranslation[0] = mTranslation[0] < 0 ? 0 : mTranslation[0];
        mTranslation[1] = mTranslation[1] < 0 ? 0 : mTranslation[1];
        if(DEBUG){
        	Log.d(TAG, "onMeasure size = [" + width + " , " + height + "]" );
        	Log.d(TAG, "onMeasure mTranslation = " +mTranslation[0] + " / " + mTranslation[1] );
        }
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
