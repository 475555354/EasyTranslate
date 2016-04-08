package com.example.kid.easytraslate;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by niuwa on 2016/4/6.
 */
public class MainFragment extends Fragment {

    private String q = null;
    private String from = "auto";
    private String to = "zh";

    private Spinner inputLanguage;
    private Spinner outputLanguage;
    private ImageButton exchangeButton;
    private ImageButton translateButton;
    private EditText inputBox;
    private TextView outputBox;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        inputLanguage = (Spinner)v.findViewById(R.id.input_language);
        outputLanguage = (Spinner)v.findViewById(R.id.output_language);
        exchangeButton = (ImageButton)v.findViewById(R.id.exchange_button);
        translateButton = (ImageButton) v.findViewById(R.id.translate_button);
        inputBox = (EditText)v.findViewById(R.id.input_box);
        outputBox = (TextView) v.findViewById(R.id.output_box);

        String[] languages = getResources().getStringArray(R.array.languages);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_dropdown_item, languages);

        inputLanguage.setAdapter(adapter);
        outputLanguage.setAdapter(adapter);

        inputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        from = "auto";
                        Log.d("TEST", from);
                        return;
                    case 1:
                        from = "zh";
                        Log.d("TEST", from);
                        return;
                    case 2:
                        from = "en";
                        Log.d("TEST", from);
                        return;
                    case 3:
                        from = "jp";
                        return;
                }
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
                switch (position){
                    case 0:
                        to = "en";
                        return;
                    case 1:
                        to = "zh";
                        return;
                    case 2:
                        to = "en";
                        return;
                    case 3:
                        to = "jp";
                        return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                to = "zh";
                return;
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = inputBox.getText().toString();
                String translation = Translate.translate(q, from, to);
                outputBox.setText(translation);
            }
        });

        return v;
    }
}
