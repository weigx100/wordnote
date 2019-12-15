package com.study.electronic_dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class Dictionary {
    public Context context=null;
    public String tableName=null;
    private Database dbHelper=null;
    private SQLiteDatabase dbR=null,dbW=null;


    public Dictionary(Context context,String tableName){
        this.context=context;
        this.tableName=tableName;
        dbHelper=new Database(context, tableName);
        dbR=dbHelper.getReadableDatabase();
        dbW=dbHelper.getWritableDatabase();

    }


    @Override
    protected void finalize() throws Throwable {      //在该对象销毁时，释放dbR和dbW
        // TODO Auto-generated method stub
        if (dbR!=null&&dbW!=null){
            dbR.close();
            dbW.close();
            dbHelper.close();
        }
        super.finalize();

    }


    public void insertWordToDict(WordValue w, boolean isOverWrite){
        if(w==null){
            return;
        }
        Cursor cursor=null;
        try{
            ContentValues values=new ContentValues();
            values.put("word",w.getWord() );
            values.put("pse", w.getPsE());
            values.put("prone",w.getPronE());
            values.put("psa", w.getPsA());
            values.put("prona", w.getPronA());
            values.put("interpret",w.getInterpret());
            values.put("sentorig", w.getSentOrig());
            values.put("senttrans", w.getSentTrans());
            cursor=dbR.query(tableName, new String[]{"word"}, "word=?", new String[]{w.getWord()}, null, null, null);
            if(cursor.getCount()>0){
                if(isOverWrite==false)
                    return;
                else{
                    dbW.update(tableName, values, "word=?",new String[]{ w.getWord()});
                }
            }else{
                dbW.insert(tableName, null, values);
            }
        }catch(Exception e){

        }finally{
            if(cursor!=null)
                cursor.close();
        }

    }

    public boolean isWordExist(String word){

        Cursor cursor=null;
        try{
            cursor=dbR.query(tableName, new String[]{"word"}, "word=?", new String[]{word}, null, null, null);
            if(cursor.getCount()>0){
                cursor.close();
                return true;
            }else{
                cursor.close();
                return false;
            }
        }finally{
            if(cursor!=null)
                cursor.close();
        }

    }

    public WordValue getWordFromDict(String searchedWord){
        WordValue w=new WordValue();
        w = null;
//        db.execSQL("create table dict(word text,pse text,prone text,psa text,prona text," +
//                "interpret text, sentorig text, senttrans text)");
        String[] columns=new String[]{"word",
                "pse","prone","psa","prona","interpret","sentorig","senttrans"};

        String[] strArray=new String[8];
        Cursor cursor=dbR.query(tableName, columns, "word=?", new String[]{searchedWord}, null, null, null);
        while(cursor.moveToNext()){
            for(int i=0;i<strArray.length;i++){
                strArray[i]=cursor.getString(cursor.getColumnIndex(columns[i]));

            }
            w=new WordValue(strArray[0],strArray[1],strArray[2],strArray[3],strArray[4],strArray[5],strArray[6],strArray[7]);
        }
        cursor.close();
        return w;
    }

    public WordValue getWordFromInternet(String searchedWord){
        WordValue wordValue=null;
        String tempWord=searchedWord;
        if(tempWord==null&& tempWord.equals(""))
            return null;
        char[] array=tempWord.toCharArray();
        if(array[0]>256)
            tempWord="_"+ URLEncoder.encode(tempWord);
        InputStream in=null;
        String str=null;
        try{
            String tempUrl=NetworkConnect.iCiBaURL1+tempWord+NetworkConnect.iCiBaURL2;
            in=NetworkConnect.getInputStreamByUrl(tempUrl);
            if(in!=null){
                JinShanXMLparser xmlParser=new JinShanXMLparser();
                InputStreamReader reader=new InputStreamReader(in,"utf-8");
                JinShanContentHandler contentHandler=new JinShanContentHandler();
                xmlParser.parseJinShanXml(contentHandler, new InputSource(reader));
                wordValue=contentHandler.getWordValue();
                wordValue.setWord(searchedWord);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return wordValue;
    }



    public String getPronEngUrl(String searchedWord){
        Cursor cursor=dbR.query(tableName, new String[]{"prone"}, "word=?", new String[]{searchedWord}, null, null, null);
        if(cursor.moveToNext()==false){
            cursor.close();
            return null;
        }
        String str=cursor.getString(cursor.getColumnIndex("prone"));
        cursor.close();
        return str;

    }

    public String getPronUSAUrl(String searchedWord){
        Cursor cursor=dbR.query(tableName, new String[]{"prona"}, "word=?", new String[]{searchedWord}, null, null, null);
        if(cursor.moveToNext()==false){
            cursor.close();
            return null;
        }
        String str=cursor.getString(cursor.getColumnIndex("prona"));
        cursor.close();
        return str;
    }

    public String getPsEng(String searchedWord){
        Cursor cursor=dbR.query(tableName, new String[]{"pse"}, "word=?", new String[]{searchedWord}, null, null, null);
        if(cursor.moveToNext()==false){
            cursor.close();
            return null;
        }
        String str=cursor.getString(cursor.getColumnIndex("pse"));
        cursor.close();
        return str;
    }

    public String getPsUSA(String searchedWord){
        Cursor cursor=dbR.query(tableName, new String[]{"psa"}, "word=?", new String[]{searchedWord}, null, null, null);
        if(cursor.moveToNext()==false){
            cursor.close();
            return null;
        }
        String str=cursor.getString(cursor.getColumnIndex("psa"));
        cursor.close();
        return str;
    }




    public String getInterpret(String searchedWord){
        Cursor cursor=dbR.query(tableName, new String[]{"interpret"}, "word=?", new String[]{searchedWord}, null, null, null);
        if(cursor.moveToNext()==false){
            cursor.close();
            return null;
        }
        String str=cursor.getString(cursor.getColumnIndex("interpret"));
        cursor.close();
        return str;

    }

}
