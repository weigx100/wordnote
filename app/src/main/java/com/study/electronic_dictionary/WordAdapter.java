package com.study.electronic_dictionary;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    //用于存储查询出的数据wordvalue
    private List<WordValue> words;
    private int solution;
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView word;
        TextView wordMean;
        View wordlistView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            word = (TextView)itemView.findViewById(R.id.tv_word);
            wordMean = (TextView)itemView.findViewById(R.id.et_wordmean);
            wordlistView = itemView;
        }
    }

    public WordAdapter(List<WordValue> words,int solution) {
        this.words = words;
        this.solution= solution;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlist_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.wordlistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                WordValue wordValue = words.get(position);
                if (solution == 0) {
                    Intent intent = new Intent(view.getContext(),WorddetailsActivity.class);
                    intent.putExtra("bean",wordValue);
                    view.getContext().startActivity(intent);
                }
                else if (solution == 1){
                    WordStorage.storeValue = wordValue;

                }
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WordValue wordValue = words.get(position);
        holder.word.setText(wordValue.getWord());
        holder.wordMean.setText(wordValue.getInterpret());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }



}
