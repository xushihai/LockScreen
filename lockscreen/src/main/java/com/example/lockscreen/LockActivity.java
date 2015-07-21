package com.example.lockscreen;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextClock;
import android.widget.Toast;


public class LockActivity extends AppCompatActivity {
    TextClock textClock;
    VelocityTracker velocityTracker;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//android4.0以后用来屏蔽home键
        KeyguardManager manager= (KeyguardManager) this.getSystemService(this.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock=manager.newKeyguardLock("");
        keyguardLock.disableKeyguard();//关闭系统的锁屏
        View view= LayoutInflater.from(this).inflate(R.layout.activity_lock,null);
        setContentView(view);
        getSupportActionBar().hide();
        textClock= (TextClock) findViewById(R.id.textClock);
        // 设置12时制显示格式
        textClock.setFormat12Hour("EEEE, MMMM dd, yyyy h:mmaa");
        // 设置24时制显示格式
        textClock.setFormat24Hour("hh:mm");

        TextClock dateClock= (TextClock) findViewById(R.id.dateClock);
        dateClock.setFormat24Hour("MM月dd日 EEEE");


    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void instaniateUi(){
        textClock= (TextClock) findViewById(R.id.textClock);
        textClock.setFormat12Hour("EEEE, MMMM dd, yyyy h:mmaa");
//        textClock.setFormat12Hour("MM月DD日");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==1){
                Toast.makeText(LockActivity.this, "解锁成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.setPackage("com.hmct.vision");
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(LockActivity.this, "解锁失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(LockActivity.this, "BACK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //setFlag，可以捕获了home键，然后不继续分发下去，让其他的程序不能捕获到home键的按下事件和长按事件，如果不想屏蔽home键，长按home键，那么可以返回false分发下去，或者清除flag不再捕获home键
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        if(velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                if(velocityTracker.getYVelocity()<-600){
                    Toast.makeText(getBaseContext(),"上划解锁",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getBaseContext(),UnLockActivity.class);
                    startActivityForResult(intent, 100);
                }
                velocityTracker.recycle();
                velocityTracker=null;
                break;
        }
        return true;
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        intentFilter.setPriority(1000);
//        registerReceiver(ShieldHomeLongClickBroadcast, intentFilter);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(ShieldHomeLongClickBroadcast);
//    }
//    private BroadcastReceiver ShieldHomeLongClickBroadcast =new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String reason = intent.getStringExtra("reason");
//            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
//                System.out.println("Intent.ACTION_CLOSE_SYSTEM_DIALOGS : " + intent.getStringExtra("reason"));
//
//                if (intent.getExtras()!=null && intent.getExtras().getBoolean("myReason")){
//                    ShieldHomeLongClickBroadcast.abortBroadcast();
//                }else if (reason != null){
//
//                    if (reason.equalsIgnoreCase("globalactions")){
//
//                        //屏蔽电源长按键的方法：
//                        Intent myIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//                        myIntent.putExtra("myReason", true);
//                        context.sendOrderedBroadcast(myIntent, null);
//                        System.out.println("电源  键被长按");
//
//                    }else if (reason.equalsIgnoreCase("homekey")){
//
//                        //屏蔽Home键的方法
//                        //在这里做一些你自己想要的操作,比如重新打开自己的锁屏程序界面，这样子就不会消失了
//                        System.out.println("Home 键被触发");
//
//                    }else if (reason.equalsIgnoreCase("recentapps")){
//                        Intent myIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//                        myIntent.putExtra("myReason", true);
//                        context.sendOrderedBroadcast(myIntent, null);
//                        //屏蔽Home键长按的方法
//                        System.out.println("Home 键被长按");
//                        Toast.makeText(LockActivity.this, "Home 键被长按", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    };
}
