package com.example.kid.easytranslate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kid.easytranslate.util.HttpCallBackListener;
import com.example.kid.easytranslate.util.HttpUtil;
import com.example.kid.easytranslate.util.MD5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String q = null;
    private String from = "auto";
    private String to = "zh";

    private RelativeLayout mLayout;
    private FloatingActionButton translateButton;
    private Spinner inputLanguage;
    private Spinner outputLanguage;
    private ImageButton exchangeButton;
    private EditText inputBox;
    private TextView outputBox;
    private ProgressBar mProgressBar;

    private final String[] languageList = {"auto", "zh", "en", "yue", "wyw", "jp", "kor", "fra", "spa", "th", "ara", "ru", "pt", "de", "it"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = (RelativeLayout) findViewById(R.id.main_layout);
        inputLanguage = (Spinner)findViewById(R.id.input_language);
        outputLanguage = (Spinner)findViewById(R.id.output_language);
        exchangeButton = (ImageButton)findViewById(R.id.exchange_button);
        translateButton = (FloatingActionButton) findViewById(R.id.btn_trans);
        inputBox = (EditText)findViewById(R.id.input_box);
        outputBox = (TextView)findViewById(R.id.output_box);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        if(intent.getAction().equals(Intent.ACTION_SEND) && intent.getType().equals("text/plain")){
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            translate(sharedText, from, to);
            inputBox.setText(sharedText);
        }

        ArrayAdapter<CharSequence> leftAdapter = ArrayAdapter.createFromResource(
                this, R.array.input_languages, android.R.layout.simple_spinner_item
        );
        leftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputLanguage.setAdapter(leftAdapter);

        ArrayAdapter<CharSequence> rightAdapter = ArrayAdapter.createFromResource(
                this, R.array.output_languages, android.R.layout.simple_spinner_item
        );
        rightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outputLanguage.setAdapter(rightAdapter);

        inputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView)view;
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);

                from = languageList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                from = "auto";
            }
        });

        outputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView)view;
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);

                to = languageList[position + 1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                to = "zh";
            }
        });

        exchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int in = inputLanguage.getSelectedItemPosition();
                int out = outputLanguage.getSelectedItemPosition();

                if (in == 0){
                    Snackbar.make(mLayout, "Check your input language!", Snackbar.LENGTH_SHORT).show();
                }else {
                    inputLanguage.setSelection(out + 1);
                    outputLanguage.setSelection(in - 1);

                    translate(inputBox.getText().toString(), languageList[out + 1], languageList[in - 1]);
                }
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = inputBox.getText().toString();
                translate(q, from, to);
            }
        });

    }

    public void translate(String q,String from, String to){

        int salt = new Random().nextInt(10000);
        final String appid = "20160401000017405";
        final String token = "SIiXZhlNCHMi8lVRB3sm";

        StringBuilder sb = new StringBuilder();
        sb.append(appid).append(q).append(salt).append(token);
        String md5 = MD5.getMD5Code(sb.toString(), true);

        StringBuilder address = new StringBuilder();
        address.append("http://api.fanyi.baidu.com/api/trans/vip/translate?q=").append(q)
                .append("&from=").append(from).append("&to=").append(to).append("&appid=").append(appid)
                .append("&salt=").append(salt).append("&sign=").append(md5);

        if (inputBox.getText() != null && !inputBox.getText().toString().equals("")){
            mProgressBar.setVisibility(View.VISIBLE);
            HttpUtil.sendHttpRequest(address.toString(), new HttpCallBackListener() {
                @Override
                public void onFinish(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("trans_result");
                        JSONObject dst = array.getJSONObject(0);
                        final String translation = URLDecoder.decode(dst.getString("dst"), "utf-8");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                outputBox.setText(translation);
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        showError();
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    showError();
                }
            });
        }
        else
            Snackbar.make(mLayout, "type something to translate...", Snackbar.LENGTH_SHORT).show();
    }

    private void showError(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mLayout, "Something is wrong...", Snackbar.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
