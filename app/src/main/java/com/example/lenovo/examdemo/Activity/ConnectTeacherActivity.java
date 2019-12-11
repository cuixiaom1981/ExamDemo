package com.example.lenovo.examdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lenovo.examdemo.R;
import com.example.lenovo.examdemo.Utils.JWebSocketClient;
import com.example.lenovo.examdemo.Utils.Util;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


public class ConnectTeacherActivity extends AppCompatActivity {

    private ListView onlineList;
    public JWebSocketClient client;
    private String data1 = "";
    //    private String []list=new String[2];
    private String[] list = new String[2];
    //    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_teacher);

        onlineList = (ListView) findViewById(R.id.onlinelist);

        initSocketClient();

        runOnUiThread(new Runnable() {
            public void run() {

                list[1] = "cui1";

                if (list.length != 0) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            ConnectTeacherActivity.this, android.R.layout.simple_list_item_1, list);
                    onlineList.setAdapter(adapter);

                    //item点击事件
                    onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent1 = new Intent(ConnectTeacherActivity.this, OnlineWebSocketActivity.class);
                            startActivity(intent1);
                            Intent intent = new Intent();
                            intent.setAction("com.xch.servicecallback.content");
                            intent.putExtra("item", list[position]);
                            sendBroadcast(intent);
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectTeacherActivity.this);
                    builder.setMessage("目前无教师在线");
                    builder.show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }


    /**
     * 初始化websocket连接
     */
    public void initSocketClient() {
        URI uri = URI.create(Util.ws);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);
                list = ToArray(message);
//                for (int i = 0;i<ToArray(message).length;i++){
//                    list.add(i,ToArray(message)[i]);
//                }
                Log.e("JWebSocketClientService", "list收到的消息：" + list[0]);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        data1 = data("221");
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                AlertDialog.Builder builder4 = new AlertDialog.Builder(ConnectTeacherActivity.this);
////                        builder4.setMessage("list内容" + list[0]);
//                                builder4.setMessage("list内容" + data1);
//                                builder4.show();
//
//                            }
//                        });
//                    }
//                });
                runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ConnectTeacherActivity.this);
                        builder4.setMessage("list内容" + list[0]);
//                        builder4.setMessage("list内容" + data1);
                        builder4.show();

                    }
                });


            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }


    private String[] ToArray(String res) {
        String[] arr2 = new String[1];
        try {
            JSONArray arr = new JSONObject(res).optJSONArray("userList");
            String[] arr1 = new String[arr.length()];

            if (arr != null) {
//                for (int i=0;i<arr.length();i++) {
//                    arr1[i]=(String)arr.get(i);
//                }
                arr1[0] = arr.get(0).toString();
                arr2[0] = arr1[0];
            }
        } catch (JSONException e) {

        }
        return arr2;
    }

    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }
//    public String data(String id) {
//        String data = "";
//        BufferedReader in;
//        try {
//            String http = "http://172.81.253.56:8080/demo/audioId/"+id;
//            Log.e("JWebSocketClientService", http);
//            URL url = new URL(http);
//            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
//            urlCon.setConnectTimeout(3000);
//            urlCon.setReadTimeout(30000);
//            urlCon.setRequestMethod("GET");
////            urlCon.setDoOutput(true);
//            urlCon.setDoInput(true);
//            urlCon.setUseCaches(false);
//            urlCon.setRequestProperty("Content-Type", "application/json;charset=utf-8");
//            urlCon.connect();
//            int responseCode = urlCon.getResponseCode();
//            String line = "";
//            String result = "";
//            in = new BufferedReader(new InputStreamReader(
//                    urlCon.getInputStream()));
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            in.close();
//
//            JSONObject jsonObject = new JSONObject(result);
//            data = jsonObject.getString("data");
//            Log.e("JWebSocketClientService", "语音转文字：" + data);
//
//        } catch (IOException e) {
//            System.out.println("io异常" + e);
//            e.printStackTrace();
//        }catch (JSONException e) {
//            System.out.println("json异常" + e);
//            e.printStackTrace();
//        }
//        return data;
//    }
}
