package com.example.a01_mqtt_control_esp8266;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    private Button btn_connect;
    private TextView tv_led;
    private Switch sw_led;
    private TextView mqtt_information;
    private Button btn_buzzer;

    private int left_seekbar_speed;
    private int right_seekbar_speed;
    //连接服务器按钮标志位
    boolean isConnected = false;
    private SeekBar left_seekbar;
    private SeekBar right_seekbar;
    private TextView left_text_seekbar;
    private TextView right_text_seekbar;
    private Button buttonTop;
    private Button buttonBottom;
    private Button buttonLeft;
    private Button buttonRight;
    //实例化mqttHepler对象
    mqttHepler mqttHelper = new mqttHepler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sw_led = findViewById(R.id.sw_led);
        btn_connect = findViewById(R.id.btn_connect);
        tv_led= findViewById(R.id.tv_led);
        mqtt_information = findViewById(R.id.mqtt_information);
        btn_buzzer= findViewById(R.id.btn_buzzer);
        left_seekbar = findViewById(R.id.left_seekbar);
        right_seekbar = findViewById(R.id.right_seekbar);
        left_text_seekbar = findViewById(R.id.left_text_seekbar);
        right_text_seekbar = findViewById(R.id.right_text_seekbar);
        buttonTop = findViewById(R.id.buttonTop);
        buttonBottom = findViewById(R.id.buttonBottom);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        //连接按钮点击事件
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected) {
                    //传入mqtt逻辑
                    //开始连接mqtt服务器
                    // 创建提醒对话框的建造器
//                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("连接MQTT服务器");
//                    builder.setMessage("确认连接");
//                    builder.setPositiveButton("取消",(dialog,which)->{
//                        Toast.makeText(MainActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
//                    });
//                    builder.setNegativeButton("确定",(dialog,which)->{
                        isConnected = true;
                        //代码逻辑
                        //mqtt代码逻辑
                        try {
                            mqttHelper.mqtt_connect_init();
                        } catch (MqttException e) {
                            throw new RuntimeException(e);
                        }
                        btn_connect.setText("mqtt连接成功");
                        Toast.makeText(MainActivity.this, "已连接MQTT服务器", Toast.LENGTH_SHORT).show();
                        mqtt_information.setText("服务器地址:"+mqttHelper.MQTT_SERVER+"\n"
                                                +"   端口号:"+"1883");
                  //  });
//                    AlertDialog dialog=builder.create();
//                    dialog.show();
                }
                else {
                    isConnected = false;
                    btn_connect.setText("连接mqtt");
                    //并且关闭灯光
                    sw_led.setChecked(false);
                    mqttHelper.publishMsg("mqtt_SmartCar","0",1);
                    //传入mqtt逻辑
                    //断开mqtt服务器连接
                    mqttHelper.disconnect();
                    mqtt_information.setText("智联万物，共创未来");
                    Toast.makeText(MainActivity.this, "服务器已断开", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //switch状态发生改变
        sw_led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isConnected) {
                    sw_led.setChecked(false);
                    isChecked=false;
                    Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                }
                if(isChecked){
                    //传入mqtt逻辑
                    mqttHelper.publishMsg("mqtt_SmartCar","open_led",1);
                    tv_led.setText("已打开");
                    tv_led.setTextColor(getResources().getColor(R.color.green));
                }
                else{
                    //传入mqtt逻辑
                    mqttHelper.publishMsg("mqtt_SmartCar","close_led",1);
                    tv_led.setText("已关闭");
                    tv_led.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
        //btn_buzzer触发监听器
        btn_buzzer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isConnected) {
                            Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                        }
                        // 按钮被按下
                        //发送mqtt消息
                        mqttHelper.publishMsg("mqtt_SmartCar","open_buzzer",1);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 按钮被松开
                        //发送mqtt消息
                        mqttHelper.publishMsg("mqtt_SmartCar","close_buzzer",1);
                        break;
                }
                return false;
            }
        });
        //滑动条监听
        right_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保使用String.valueOf()将整数值转换为字符串
                //改变过程中实时显示速度变化
                right_seekbar_speed=progress;
                right_text_seekbar.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 滑动开始时的操作（如果需要）
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 滑动停止时的操作（如果需要）
                //传入mqtt逻辑，将最终值传递
                mqttHelper.publishMsg("mqtt_SmartCar","right_speed:"+right_seekbar_speed,1);
            }
        });
        //滑动条监听
        left_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 确保使用String.valueOf()将整数值转换为字符串
                //改变过程中实时显示速度变化
                left_seekbar_speed=progress;
                left_text_seekbar.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 滑动开始时的操作（如果需要）
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 滑动停止时的操作（如果需要）
                //传入mqtt逻辑，将最终值传递
              //  mqttHelper.publishMsg("mqtt_SmartCar","left_speed"+progress,1);
                mqttHelper.publishMsg("mqtt_SmartCar","left_speed:"+left_seekbar_speed,1);
            }
        });
        //当按下按钮时发送信号，松开时发送结束信号
        buttonTop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isConnected) {
                            Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            buttonTop.setTextColor(getResources().getColor(R.color.white));
                            mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_Top",1);
                        }
                        break;
                        // 按钮被按下
                    case MotionEvent.ACTION_UP:
                        buttonTop.setTextColor(getResources().getColor(R.color.black));
                        mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_stop",1);
                        break;
                        // 按钮被松开
                }
                return false;
            }
        });
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isConnected) {
                            Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            buttonLeft.setTextColor(getResources().getColor(R.color.white));
                            //mqtt
                            mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_left",1);
                        }
                        break;
                    // 按钮被按下
                    case MotionEvent.ACTION_UP:
                        buttonLeft.setTextColor(getResources().getColor(R.color.black));
                        mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_stop",1);
                        break;
                    // 按钮被松开
                }
                return false;
            }
        });
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isConnected) {
                            Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            buttonRight.setTextColor(getResources().getColor(R.color.white));
                            mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_right",1);
                        }
                        break;
                    // 按钮被按下
                    case MotionEvent.ACTION_UP:
                        buttonRight.setTextColor(getResources().getColor(R.color.black));
                        mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_stop",1);
                        break;
                    // 按钮被松开
                }
                return false;
            }
        });
        buttonBottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isConnected) {
                            Toast.makeText(MainActivity.this, "服务器未连接", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                             buttonBottom.setTextColor(getResources().getColor(R.color.white));
                            mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_Bottom",1);
                        }
                        break;
                    // 按钮被按下
                    case MotionEvent.ACTION_UP:
                        buttonBottom.setTextColor(getResources().getColor(R.color.black));
                        mqttHelper.publishMsg("mqtt_SmartCar","SmartCar_stop",1);
                        break;
                    // 按钮被松开
                }
                return false;
            }
        });
    }
}