package empire.stark.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by YENMINH on 1/7/2016.
 * Code and Life
 */
public class CallReceiver extends BroadcastReceiver {
    private final String TAG = "[CallReceiver]";
    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(TAG, "onReceive " + state);
        if (state != null && !state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            context.getApplicationContext().startService(new Intent(context.getApplicationContext(), RecorderCallService.class));
        }
    }
}
