package com.example.jetpackroomdemo.bindingadapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jetpackroomdemo.Word;
import com.example.jetpackroomdemo.rvadapter.WordRecyclerViewAdapter;

import java.util.List;

public class AdapterBinding {

    @BindingAdapter(value = "wordData")
    public static void setRvData(RecyclerView recyclerView, List<Word> words) {
        WordRecyclerViewAdapter adapter = ((WordRecyclerViewAdapter) recyclerView.getAdapter());
        if (null != adapter) {
            int oldSize = adapter.getItemCount();
            int newSize = words == null ? 0 : words.size();
            if (oldSize != newSize) {
                adapter.submitList(words);
                recyclerView.smoothScrollBy(0, -400);
            }
        }
    }

}
