package com.example.a01_mqtt_control_esp8266;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class mqttHepler {
    //mqtt服务器地址
    public static final String MQTT_SERVER = "tcp://test.mosquitto.org";
    //mqtt服务器连接用户名    此服务器不需要用户名和密码
    public static final String MQTT_USERNAME = "";
    //mqtt服务器连接密码
    public static final String MQTT_PASSWORD = "";

    MqttClient sampleClient;
    //mqtt_connect()连接mqtt函数
    public void mqtt_connect_init() throws MqttException {
        // 创建内存持久化存储对象（会话数据仅保存在内存中，进程结束后丢失）
        MemoryPersistence persistence = new MemoryPersistence();
        // 生成唯一客户端标识符（结合时间戳保证设备唯一性）
        String clientId = "AndroidClient" + System.currentTimeMillis();
        // 初始化MQTT客户端实例（指定服务器地址、客户端ID、持久化存储）
        sampleClient = new MqttClient(MQTT_SERVER, clientId, persistence);
        //定义需要订阅或发送的主题
        String[] topics = new String[] {"mqtt_SmartCar", "test2_mqtt", "test3_mqtt"};
        int[] qos = new int[] {1, 1, 1}; // 对应每个主题的服务质量等级
        // 创建并配置连接选项（即使不需要用户名密码）
        MqttConnectOptions connOpts = createConnectOptions(MQTT_USERNAME, MQTT_PASSWORD);
        try {
            sampleClient.connect(connOpts); // 实际建立连接
        } catch (MqttException e) {
            throw e;
        }

        // 设置异步回调处理器（处理连接状态变化和消息收发事件）
        sampleClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverUri) {
                try {
                    sampleClient.subscribe(topics, qos);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void connectionLost(Throwable cause) {

            }
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // 新消息到达回调（异步处理）
//                String payload = new String(message.getPayload());
//                // 根据主题处理消息
//                if (topic.equals("test1_mqtt")) {
//                    // 处理来自test1_mqtt主题的消息
//
//                } else if (topic.equals("test2_mqtt")) {
//                    // 处理来自test2_mqtt主题的消息
//
//                } else if (topic.equals("test3_mqtt")) {
//                    // 处理来自test3_mqtt主题的消息
//
//                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // 消息确认送达回调（QoS≥1时触发）
            }
        });
    }

    //创建MQTT连接配置参数
    public MqttConnectOptions createConnectOptions(String userName, String passWord) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置会话清理模式（true表示新建会话，覆盖历史记录）
        connOpts.setCleanSession(true);
        // 设置连接认证凭证
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        // 启用自动重连机制（网络恢复后自动重连）
        connOpts.setAutomaticReconnect(true);
        // 设置连接超时时间（单位：秒，默认30秒）
        connOpts.setConnectionTimeout(30);
        // 设置心跳间隔时间（单位：秒，默认20秒）
        connOpts.setKeepAliveInterval(20);
        return connOpts;
    }


    //mqtt_publish()发布mqtt函数
    // 消息发布方法
    public void publishMsg(String Mqtt_Topic,String Mqtt_Value,int Mqtt_Qos) {
        if (TextUtils.isEmpty(Mqtt_Value)) {
            Mqtt_Value = "Hello MQTT "; // 输入为空时使用默认消息
        }
        // 创建MQTT消息对象（消息体需转换为字节数组）
        MqttMessage message = new MqttMessage(Mqtt_Value.getBytes());
        // 设置服务质量等级（0-最多一次，保证端到端传输）
        message.setQos(Mqtt_Qos);
        try {
            // 确保客户端处于已连接状态
            if (sampleClient != null && sampleClient.isConnected()) {
                // 发布消息到指定主题（所有订阅该主题的客户端都会收到）
                sampleClient.publish(Mqtt_Topic, message);
            }
        } catch (MqttException e) {
            Log.e("MQTT", "发布消息失败", e);
        }
    }
    //mqtt_subscribe()订阅mqtt函数

    // mqtt_disconnect() 断开mqtt连接函数
    public void disconnect() {
        if (sampleClient != null && sampleClient.isConnected()) {
            try {
                // 触发断开连接操作（异步执行）
                sampleClient.disconnect();
                // 可选：添加断开回调监听（如果需要同步处理状态）
                // sampleClient.setCallback(new MqttCallback() {
                //     @Override
                //     public void connectionLost(Throwable cause) {
                //         Log.e("MQTT", "断开连接回调", cause);
                //     }
                // });
            } catch (MqttException e) {
                Log.e("MQTT", "断开连接失败", e);
            }
        }
    }
}


//public class MainActivity extends AppCompatActivity {
//    private EditText receive_messages;
//    private EditText send_messages;
//    private Button btn_submit;
//    private Button btn_connect;
//    private EditText subscribe_messages;
//    MqttClient sampleClient;
//    String broker = "tcp://test.mosquitto.org:1883";
//    String userName = "";
//    String passWord = "";
//    //设置订阅方订阅的Topic集合，遵循MQTT的订阅规则，可以是多级Topic集合
//    //  final String topicFilter = "test1_mqtt";
//    //服务质量，对应topicFilter
//    //   final int qos = 1;
//    /*
//     *  // 订阅多个主题
//     * String[] topics = new String[] {"test1_mqtt", "test2_mqtt", "test3_mqtt"};
//     * int[] qos = new int[] {1, 1, 1}; // 对应每个主题的服务质量等级
//     * sampleClient.subscribe(topics, qos);
//     */
//    String[] topics = new String[] {"test1_mqtt", "test2_mqtt", "test3_mqtt"};
//    int[] qos = new int[] {1, 1, 1}; // 对应每个主题的服务质量等级
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        receive_messages = findViewById(R.id.receive_messages);
//        send_messages = findViewById(R.id.send_messages);
//        btn_submit = findViewById(R.id.btn_submit);
//        btn_connect = findViewById(R.id.btn_connect);
//        // 添加这行代码初始化订阅消息显示组件
//        subscribe_messages = findViewById(R.id.subscribe_messages); // 确保XML中有对应id
//
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                publishMsg();
//                Toast.makeText(MainActivity.this, "已发送", Toast.LENGTH_SHORT).show();
//                // 清除EditText中的文本
//                send_messages.setText("");
//            }
//        });
//
//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点击此按键后，连接mqtt服务器
//                try {
//                    mqtt_connect_init();
//                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
//                } catch (MqttException e) {
//                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//

//
//
//
//    // 消息发布方法
//    public void publishMsg() {
//        // 获取并处理用户输入内容
//        String content = send_messages.getText().toString().trim();  //.trim()：这是一个方法调用，用于移除字符串两端的空白字符（包括空格、制表符、换行符等）。
//        if (TextUtils.isEmpty(content)) {
//            content = "Hello MQTT "; // 输入为空时使用默认消息
//        }
//        // 创建MQTT消息对象（消息体需转换为字节数组）
//        MqttMessage message = new MqttMessage(content.getBytes());
//        // 设置服务质量等级（0-最多一次，保证端到端传输）
//        message.setQos(0);
//        try {
//            // 确保客户端处于已连接状态
//            if (sampleClient != null && sampleClient.isConnected()) {
//                // 发布消息到指定主题（所有订阅该主题的客户端都会收到）
//                sampleClient.publish("test1_mqtt", message);
//                setTextInfo("publishMsg: " + message);
//            }
//        } catch (MqttException e) {
//            setTextInfo(" publishException: " + e.getMessage());
//        }
//    }
//
//    // UI线程更新方法
//    private void setTextInfo(final String text) {
//        // 在主线程更新UI组件（避免跨线程操作异常）
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                receive_messages.append(text + "\n"); // 追加显示接收信息
//            }
//        });
//    }
//}