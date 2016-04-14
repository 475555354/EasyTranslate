package com.example.kid.easytranslate;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kid.easytranslate.util.MD5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Random;

public class MainActivity extends Activity {

    private String q = null;
    private String from = "auto";
    private String to = "zh";

    private Spinner inputLanguage;
    private Spinner outputLanguage;
    private ImageButton exchangeButton;
    private ImageButton translateButton;
    private EditText inputBox;
    private TextView outputBox;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            if (message.obj != null){
                outputBox.setText(message.obj.toString());
            }
            super.handleMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);

        inputLanguage = (Spinner)findViewById(R.id.input_language);
        outputLanguage = (Spinner)findViewById(R.id.output_language);
        exchangeButton = (ImageButton)findViewById(R.id.exchange_button);
        translateButton = (ImageButton)findViewById(R.id.translate_button);
        inputBox = (EditText)findViewById(R.id.input_box);
        outputBox = (TextView)findViewById(R.id.output_box);

        String[] inputLanguages = getResources().getStringArray(R.array.input_languages);
        ArrayAdapter<String> leftAdapter = new ArrayAdapter<>
                (this, R.layout.spinner_item_left, inputLanguages);
        inputLanguage.setAdapter(leftAdapter);

        String[] outputLanguages = getResources().getStringArray(R.array.output_languages);
        ArrayAdapter<String> rightAdapter = new ArrayAdapter<>
                (this, R.layout.spinner_item_right, outputLanguages);
        outputLanguage.setAdapter(rightAdapter);

        inputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView)view;
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);

                switch (position) {
                    case 0:
                        from = "auto";
                        return;
                    case 1:
                        from = "zh";
                        return;
                    case 2:
                        from = "en";
                        return;
                    case 3:
                        from = "yue";
                        return;
                    case 4:
                        from = "wyw";
                        return;
                    case 5:
                        from = "jp";
                        return;
                    case 6:
                        from = "kor";
                        return;
                    case 7:
                        from = "fra";
                        return;
                    case 8:
                        from = "spa";
                        return;
                    case 9:
                        from = "th";
                        return;
                    case 10:
                        from = "ara";
                        return;
                    case 11:
                        from = "ru";
                        return;
                    case 12:
                        from = "pt";
                        return;
                    case 13:
                        from = "de";
                        return;
                    case 14:
                        from = "it";
                        return;
                }

                if(from != "zh" && outputLanguage.getSelectedItemPosition() == -1)
                    to = "zh";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                from = "auto";
                return;
            }
        });

        outputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView)view;
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);

                switch (position){
                    case 0:
                        to = "zh";
                        return;
                    case 1:
                        to = "en";
                        return;
                    case 2:
                        to = "yue";
                        return;
                    case 3:
                        to = "wyw";
                        return;
                    case 4:
                        to = "jp";
                        return;
                    case 5:
                        to = "kor";
                        return;
                    case 6:
                        to = "fra";
                        return;
                    case 7:
                        to = "spa";
                        return;
                    case 8:
                        to = "th";
                        return;
                    case 9:
                        to = "ara";
                        return;
                    case 10:
                        to = "ru";
                        return;
                    case 11:
                        to = "pt";
                        return;
                    case 12:
                        to = "de";
                        return;
                    case 13:
                        to = "it";
                        return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                to = "zh";
                return;
            }
        });

        exchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.info)
                        .create();
                alertDialog.show();
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = inputBox.getText().toString();
                translate(q, from, to);
            }
        });
        translateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    v.setBackgroundResource(R.mipmap.translate_click);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    v.setBackgroundResource(R.mipmap.translate);
                return false;
            }
        });

    }

    public void translate(String q,String from, String to){

        int salt = new Random().nextInt(10000);
        final String appid = "20160401000017405";
        final String token = "SIiXZhlNCHMi8lVRB3sm";

        final String string = appid + q + salt + token;
        String md5 = MD5.GetMD5Code(string, true);

        final String address = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + q
                + "&from=" + from + "&to=" + to + "&appid=" + appid + "&salt=" + salt
                + "&sign=" + md5;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line).append("\n");
                    }

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = (JSONArray)jsonObject.get("trans_result");
                    JSONObject dst = (JSONObject) array.get(0);
                    String translation = URLDecoder.decode(dst.getString("dst"),"utf-8");

                    Message message = new Message();
                    message.obj = translation;
                    mHandler.sendMessage(message);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

}
