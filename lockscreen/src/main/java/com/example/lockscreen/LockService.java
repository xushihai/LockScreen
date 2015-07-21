package com.example.lockscreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LockService extends Service {
    public LockService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(lockScreenBroadcastReceiver,intentFilter);
        Toast.makeText(LockService.this, "注册屏幕开关监听器", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lockScreenBroadcastReceiver);
        Toast.makeText(LockService.this, "注销屏幕开关监听器", Toast.LENGTH_SHORT).show();
        startService(new Intent(this,LockService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public  BroadcastReceiver lockScreenBroadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG",intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LockService.this,LockActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    };
}
