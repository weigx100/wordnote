package com.study.electronic_dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordnoteActivity extends AppCompatActivity {

    Database database;

    TextView tvSearchatnote;
    Button btnSearchatnote;

    List<WordValue> wordValuelist = new ArrayList<>();

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
        setContentView(R.layout.activity_wordnote);

        tvSearchatnote=findViewById(R.id.tv_searchatnote);
        btnSearchatnote=findViewById(R.id.btn_searchatnote);

        database = new Database(this,"dict",null,2);
        final SQLiteDatabase liteDatabase = database.getWritableDatabase();

        btnSearchatnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = tvSearchatnote.getText().toString();
                wordValuelist.clear();
                Cursor cursor = liteDatabase.rawQuery("select * from dict where word like '%"+str+"%'", null);
                wordValuelist = getCursorReturnList(cursor);
                showListToScreen(wordValuelist);
            }
        });

        Cursor cursor = liteDatabase.query("dict", null, null, null, null, null, null);
        wordValuelist = getCursorReturnList(cursor);
        showListToScreen(wordValuelist);


    }


    public List<WordValue> getCursorReturnList(Cursor cursor){
        List<WordValue> words = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                WordValue wordValue = new WordValue();
                String word = cursor.getString(cursor.getColumnIndex("word"));
                wordValue.setWord(word);
                String pse = cursor.getString(cursor.getColumnIndex("pse"));
                wordValue.setPsE(pse);
                String prone = cursor.getString(cursor.getColumnIndex("prone"));
                wordValue.setPronE(prone);
                String psa = cursor.getString(cursor.getColumnIndex("psa"));
                wordValue.setPsA(psa);
                String prona = cursor.getString(cursor.getColumnIndex("prona"));
                wordValue.setPronA(prona);
                String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
                wordValue.setInterpret(interpret);
                String sentorig = cursor.getString(cursor.getColumnIndex("sentorig"));
                wordValue.setSentOrig(sentorig);
                String senttrans = cursor.getString(cursor.getColumnIndex("senttrans"));
                wordValue.setSentTrans(senttrans);
                words.add(wordValue);


            } while (cursor.moveToNext());
        }
        Collections.sort(words);
        return words;
    }

    public void showListToScreen(List<WordValue> words){//竖屏显示
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_wordlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        WordAdapter wordAdapter = new WordAdapter(words,0);
        recyclerView.setAdapter(wordAdapter);
    }

}
