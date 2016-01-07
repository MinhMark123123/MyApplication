package empire.stark.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YENMINH on 1/7/2016.
 * Code and Life
 */
public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.getApplicationContext().startService(new Intent(context.getApplicationContext(), RecorderCallService.class));
    }
}
