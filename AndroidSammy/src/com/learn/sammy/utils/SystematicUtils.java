package com.learn.sammy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class SystematicUtils {
	private static final boolean DEBUG = true;
	private static final String TAG = "SystematicManager";
	public static List<Map<String,Object>> queryActivity(Context context,String action,String category){
		if(action == null || category == null){
			Log.w(TAG, "queryActivity action == null || category == null");
		}
		List<Map<String,Object>> filterActivity = new ArrayList<Map<String,Object>>(); 
		Intent intent = new Intent(action);
		intent.addCategory(category);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
		if(resolveInfos == null){
			return filterActivity;
		}
		int len = filterActivity.size();
		for(int i = 0 ;i < len ;i++){
			ResolveInfo info = resolveInfos.get(i);
			CharSequence labelSeq = info.loadLabel(pm);
			String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;
            addItem(filterActivity,label,activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
		}
		return filterActivity;
	}
	
	private static void addItem(List<Map<String, Object>> filterActivity,
			String label, Intent activityIntent) {
		Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", label);
        temp.put("intent", activityIntent);
        filterActivity.add(temp);
	}

	public static Intent activityIntent(String pkg,String componentName){
		Intent intent = new Intent();
		intent.setClassName(pkg, componentName);
		return intent;
	}
}
