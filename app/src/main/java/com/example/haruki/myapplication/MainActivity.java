package com.example.haruki.myapplication;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 1000;
    TextView textView;
    TextView textView1;
    TextView textView2;
    EditText editText;
    Button button;
    Button button2;

    String str;

    int lang ;

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textView.setText(editText.getText().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview);
        editText = (EditText) findViewById(R.id.edittext);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(buttonListener);

        // 言語選択 0:日本語、1:英語、2:オフライン、その他:General
        lang = 0;

        // 認識結果を表示させる
        textView1 = (TextView)findViewById(R.id.text_view);
        textView2 = (TextView)findViewById(R.id.resulttext);

        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 音声認識を開始
                speech();
            }
        });

        findViewById(R.id.button_1).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_3).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_4).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_5).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_6).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_7).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_8).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_9).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_0).setOnClickListener(buttonNumberListener);
        findViewById(R.id.button_dot).setOnClickListener(buttonNumberListener);

        findViewById(R.id.button_add).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.button_subtract).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.button_multiply).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.button_divide).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.button_equal).setOnClickListener(buttonOperatorListener);


    }

    private void speech(){
        // 音声認識が使えるか確認する
        try {
            // 音声認識の　Intent インスタンス
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            if(lang == 0){
                // 日本語
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.JAPAN.toString() );
            }
            else if(lang == 1){
                // 英語
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString() );
            }
            else if(lang == 2){
                // Off line mode
                intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
            }
            else{
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            }

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力");
            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            textView1.setText("No Activity " );
        }

    }

    // 結果を受け取るために onActivityResult を設置
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
                textView1.setText( candidates.get(0));
            }
        }
        //textView2.setText(textView1.getText());

        //if(textView1.getText().equals("1")){
            //button_3;
    //}


    }

    View.OnClickListener buttonNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;

            if (isOperatorKeyPushed == true) {
                editText.setText(button.getText());
            } else {
                editText.append(button.getText());
            }

            isOperatorKeyPushed = false;

        }
    };

    int recentOperator = R.id.button_equal; // 最近押された計算キー
    double result;  // 計算結果
    boolean isOperatorKeyPushed;    // 計算キーが押されたことを記憶

    View.OnClickListener buttonOperatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button operatorButton = (Button) view;
            double value = Double.parseDouble(editText.getText().toString());

            //音声認識から判断
            for(str = textView1.getText().toString();str.equals( "=");){
                if(str.equals( "+")){
                    result = calc(recentOperator, result, value);
                    editText.setText(String.valueOf(result));
                }else if(str.equals( "-")){
                    result = calc(recentOperator, result, value);
                    editText.setText(String.valueOf(result));
                }else if(str.equals( "*")){
                    result = calc(recentOperator, result, value);
                    editText.setText(String.valueOf(result));
                }else if(str.equals( "/")){
                    result = calc(recentOperator, result, value);
                    editText.setText(String.valueOf(result));
                }else{
                    value = Double.parseDouble(str);
                }

            }

            //if (recentOperator == R.id.button_equal)
            if (str.equals( "=")) {
                result = value;
            }// else {
               // result = calc(recentOperator, result, value);
                //editText.setText(String.valueOf(result));
            //}

            recentOperator = operatorButton.getId();
            textView.setText(operatorButton.getText());
            isOperatorKeyPushed = true;
        }
    };

    double calc(int operator, double value1, double value2) {
        switch (operator) {
                case R.id.button_add:
                    return value1 + value2;
                case R.id.button_subtract:
                    return value1 - value2;
                case R.id.button_multiply:
                    return value1 * value2;
                case R.id.button_divide:
                return value1 / value2;
            default:
                return 0;
        }
    }
}
