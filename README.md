# LockScreen
适用于android4.0以上的系统，由于android4.0以上的许多技术都发生了变化，比如屏蔽home键技术，开机启动等。

1.android4.0之后开机启动实现
加入接收开机完成广播的权限<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
再加入接收开机完成广播的Receiver.
  <receiver android:name="com.example.lockscreen.BootReceiver">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />

                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </receiver>
2.为了之显示自己的锁屏界面，需要关闭系统锁屏，先需要添加关闭系统锁屏的权限<uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
3.关闭系统锁屏     
  KeyguardManager manager= (KeyguardManager) this.getSystemService(this.KEYGUARD_SERVICE);
  KeyguardManager.KeyguardLock keyguardLock=manager.newKeyguardLock("");
  keyguardLock.disableKeyguard();//关闭系统的锁屏
4.为了实现关闭屏幕和打开屏幕的时候进如自己的锁屏界面，需要注册屏幕开关的广播，有一点需要注意的是这个广播需要动态注册，不能静态注册，否则会接收不到屏幕开关的广播
  IntentFilter intentFilter=new IntentFilter(Intent.ACTION_SCREEN_OFF);
  intentFilter.addAction(Intent.ACTION_SCREEN_ON);
  registerReceiver(lockScreenBroadcastReceiver,intentFilter);
5.进入锁屏界面后需要做的是屏蔽home键和back键
private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
在setContentView之前添加homekey的标志，用来捕获home键事件，然后再捕获了home键事件后不往下分发，就实现了home键的屏蔽功能
this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//android4.0以后用来屏蔽home键

@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //setFlag，可以捕获了home键，然后不继续分发下去，让其他的程序不能捕获到home键的按下事件和长按事件，如果不想屏蔽home键，长按home键，那么可以返回false分发下去，或者清除flag不再捕获home键
        return true;
    }
    
  6.屏蔽back键
  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(LockActivity.this, "BACK", Toast.LENGTH_SHORT).show();
    }
    7.给application添加一条属性android:excludeFromRecents="true"，表示该activity不会出现在最近任务列表中，这样用户长按home键弹出最近任务列表就不会关闭锁屏进程。
    8.给application添加一条属性android:installLocation="internalOnly"，只允许安装在内存中，安装在存储卡的程序是接收不到开机启动的广播。
