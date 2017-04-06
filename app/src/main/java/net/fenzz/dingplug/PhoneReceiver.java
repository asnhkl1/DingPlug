package net.fenzz.dingplug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.app.Service;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {

	private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);
            
        } else {
            //
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {
            
            case TelephonyManager.CALL_STATE_RINGING:
                mIncomingNumber = intent.getStringExtra("incoming_number");
                Log.i(TAG, "RINGING :" + mIncomingNumber);
                if(mIncomingNumber!=null&&mIncomingNumber.equals("15221926787")){
                	Utils.openCLD("com.alibaba.android.rimet", context);
                	DingService.instance.setServiceEnable();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mIncomingFlag) {
                    Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (mIncomingFlag) {
                    Log.i(TAG, "incoming IDLE");
                }
                break;
            }
        }
    }

}
