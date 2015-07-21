package com.example.lockscreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class UnLockActivity extends AppCompatActivity implements View.OnClickListener {
    int FAULT_ERROR_TIMES=3;
    int fault_tolerant_times = FAULT_ERROR_TIMES;
    String password = "";
    View indicator1, indicator2, indicator3, indicator4;
    ArrayList<View> indicatorList = new ArrayList<>();
    View indicator;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    SharedPreferences sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//android4.0以后用来屏蔽home键
        setContentView(R.layout.activity_lock_password);
        getSupportActionBar().hide();
        indicator = findViewById(R.id.indicator);
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        indicator3 = findViewById(R.id.indicator3);
        indicator4 = findViewById(R.id.indicator4);
        indicatorList.add(indicator1);
        indicatorList.add(indicator2);
        indicatorList.add(indicator3);
        indicatorList.add(indicator4);


        TextView textView = (TextView) findViewById(R.id.digital_0);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_1);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_2);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_3);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_4);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_5);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_6);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_7);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_8);
        textView.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.digital_9);
        textView.setOnClickListener(this);

        Button del = (Button) findViewById(R.id.del_button);
        del.setOnClickListener(this);

        sf = getSharedPreferences("mydev", MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        if(sf.getBoolean("USER_NOT_CAN_UNLOCK",false)){
            Toast.makeText(getBaseContext(), "你的手机已被禁止使用，请联系管理员", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = v.getId();
        switch (id) {
            case R.id.digital_0:
            case R.id.digital_1:
            case R.id.digital_2:
            case R.id.digital_3:
            case R.id.digital_4:
            case R.id.digital_5:
            case R.id.digital_6:
            case R.id.digital_7:
            case R.id.digital_8:
            case R.id.digital_9:
                TextView textView = (TextView) v;
                String password_seperator = textView.getText().toString();
                password += password_seperator;
                int sum = password.length();
                setIndicator(sum);
                if (sum == 4) {
                    SharedPreferences sf = getSharedPreferences("mydev", MODE_PRIVATE);
                    String default_lockscreen_password = getString(R.string.default_lockscreen_password);
                    String custom_lockscreen_password = sf.getString("CUSTOM_LOCKSCREEN_PASSWORD", default_lockscreen_password);
                    if (password.equals(custom_lockscreen_password)) {
                        setResult(1);
                        finish();
                    } else {
                        fault_tolerant_times--;
                        AnimationUtils animationUtils = new AnimationUtils();
                        Animation anim = animationUtils.loadAnimation(getBaseContext(), R.anim.shake_anim);
                        indicator.startAnimation(anim);//抖动动画
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(300);
                        password = "";
                        setIndicator(0);
                        if (fault_tolerant_times >= 0)
                            Toast.makeText(getBaseContext(), "最多还能输错" + fault_tolerant_times + "次", Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(getBaseContext(), "错误次数超过了" + fault_tolerant_times + "次，你暂时不能使用手机了", Toast.LENGTH_SHORT).show();
                            sf.edit().putBoolean("USER_NOT_CAN_UNLOCK", true).commit();
                        }

                    }
                }
                break;
            case R.id.del_button:
                int length = password.length();
                if (length == 0 || length == 1) {
                    password = "";
                } else if (length >= 2 && length <= 3) {
                    password = password.substring(0, length - 1);
                }
                setIndicator(password.length());
                break;
        }
    }

    public void passwordRecovery(View btnView) {
        sf.edit().putString("CUSTOM_LOCKSCREEN_PASSWORD", null).commit();
        fault_tolerant_times = FAULT_ERROR_TIMES;
        sf.edit().putBoolean("USER_NOT_CAN_UNLOCK", false).commit();
    }

    public void setIndicator(int sum) {
        for (int i = 0; i < sum; i++) {
            indicatorList.get(i).getBackground().setLevel(10);
        }
        for (int i = sum; i < 4; i++) {
            indicatorList.get(i).getBackground().setLevel(5);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(-1);
        finish();
        Toast.makeText(getBaseContext(), "guanbi", Toast.LENGTH_SHORT).show();
    }
}
