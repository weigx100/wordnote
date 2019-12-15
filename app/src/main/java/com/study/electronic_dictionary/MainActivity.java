package com.study.electronic_dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    TextView tvWordResult;
    TextView tvMeanResult;
    TextView tvSentenceResult;
    TextView tvSentenceCHS;
    TextView tvPhoneticsymbolUS;
    TextView tvPhoneticsymbolUK;
    Button btnAdd;

    Database database;
    Dictionary dictionary;
    Dictionary new_dictionary;
    WordValue wordValue=null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        Intent intent=new Intent();
        switch (id){
            case R.id.mi_dictionary:
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mi_wordnote:
                intent.setClass(this,WordnoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mi_help:
                intent.setClass(this,HelpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch=findViewById(R.id.et_search);
        btnSearch=findViewById(R.id.btn_search);
        tvWordResult=findViewById(R.id.tv_wordresult);
        tvMeanResult=findViewById(R.id.tv_meanresult);
        tvSentenceResult=findViewById(R.id.tv_sentenceresult);
        tvSentenceCHS=findViewById(R.id.et_sentenceCHS);
        tvPhoneticsymbolUS=findViewById(R.id.tv_phoneticsymbolUS);
        tvPhoneticsymbolUK=findViewById(R.id.tv_phoneticsymbolUK);
        btnAdd=findViewById(R.id.btn_add);

        dictionary=new Dictionary(this,"dict");
        new_dictionary=new Dictionary(this,"newdict");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if ("".equals(etSearch.getText().toString())) {
                            showToastInThread("请输入单词");
                        } else {
                            wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());
                            if (wordValue != null && wordValue.getInterpret() != null && !"".equals(wordValue.getPsA())) {
                                showResearchWordInterpret(wordValue);
                                Log.d("MainActivity", "if条件");
                                Log.d("MainActivity", "释义"+wordValue.getInterpret());
                            } else {
                                showToastInThread("未找到相关释义");
                                Log.d("MainActivity", "else条件");
                            }
                        }
                    }
                }).start();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if ("".equals(etSearch.getText().toString())) {
                            showToastInThread("请输入单词");
                        } else {
                            wordValue = dictionary.getWordFromDict(etSearch.getText().toString());
                            if (wordValue == null) {
                                wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());
                                if (wordValue == null) {
                                    showToastInThread("请检查单词是否拼写正确");
                                } else {
                                    dictionary.insertWordToDict(wordValue, true);
                                    showToastInThread("单词已存入数据库");
                                }
                            } else {
                                showToastInThread("该单词已存入数据库,请不要重复操作");
                            }
                        }
                    }
                }).start();
            }
        });
    }


    private void showResearchWordInterpret(final WordValue wordValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在此进行ui操作
                tvWordResult.setText(wordValue.getWord());
                tvMeanResult.setText(wordValue.getInterpret());
                if (isLand()) {
                    tvSentenceResult.setText(wordValue.getSentOrig());
                    tvSentenceCHS.setText(wordValue.getSentTrans());
                }
                else {
                    tvPhoneticsymbolUS.setText("美["+wordValue.psA+"]");
                    tvPhoneticsymbolUK.setText("英["+wordValue.psE+"]");
                }
            }
        });
    }


    private void showToastInThread(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }

    //判断横屏
    private boolean isLand() {
        Configuration mConfiguration = this.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            return false;
        }
        return false;
    }
}
