package net.fenzz.dingplug;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

public class Utils {
	
	public static void openCLD(String packageName,Context context) { 
        PackageManager packageManager = context.getPackageManager(); 
        PackageInfo pi = null;   
         
            try { 
                 
                pi = packageManager.getPackageInfo("com.alibaba.android.rimet", 0); 
            } catch (NameNotFoundException e) { 
                 
            } 
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null); 
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER); 
            resolveIntent.setPackage(pi.packageName); 
 
            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0); 
 
            ResolveInfo ri = apps.iterator().next(); 
            if (ri != null ) { 
                String className = ri.activityInfo.name; 
 
                Intent intent = new Intent(Intent.ACTION_MAIN); 
                intent.addCategory(Intent.CATEGORY_LAUNCHER); 
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packageName, className); 
 
                intent.setComponent(cn); 
                context.startActivity(intent); 
            } 
    } 

}
