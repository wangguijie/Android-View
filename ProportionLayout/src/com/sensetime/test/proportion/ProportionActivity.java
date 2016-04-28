package com.sensetime.test.proportion;

import com.sensetime.proportion.widget.ProportionLayout;
import com.sensetime.proportion.widget.ProportionMatch;
import com.sensetime.proportion.widget.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * @author MatrixCV
 *
 * Activity
 * 
 */
public class ProportionActivity extends Activity implements OnClickListener {
	
	private ProportionLayout mProportionLayout;
	private int mClickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mProportionLayout = (ProportionLayout)findViewById(R.id.proporlayout);
		mProportionLayout.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		int[] proprotion = mProportionLayout.getProportion();
		mClickCount++;
		mProportionLayout.setProportionMatch(ProportionMatch.valueToMatchType(mClickCount%4));
//		if(proprotion[0]/(float)proprotion[1] == 3/(float)4){
//			mProportionLayout.setProportion(9, 16);
//		}else{
//			mProportionLayout.setProportion(3, 4);
//		}
	}
	
}
