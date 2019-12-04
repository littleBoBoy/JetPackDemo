package com.example.jetpackroomdemo.rvadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.jetpackroomdemo.R;
import com.example.jetpackroomdemo.Word;
import com.example.jetpackroomdemo.WordViewModel;
import com.example.jetpackroomdemo.rvadapter.base.BaseRVDataBindingAdapter;
import com.example.jetpackroomdemo.databinding.CellCardBinding;
import com.example.jetpackroomdemo.rvadapter.base.BaseViewHolder;


public class WordRecyclerViewAdapter extends BaseRVDataBindingAdapter<Word, CellCardBinding> {

    private WordViewModel wordViewModel;
    public int selectedIndex;

    public WordRecyclerViewAdapter(Context context, WordViewModel wordViewModel) {
        super(context, new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getWord().equals(newItem.getWord())
                        && oldItem.getChineseMeaning().equals(newItem.getChineseMeaning())
                        && oldItem.isChineseInvisiable() == newItem.isChineseInvisiable());
            }
        });
        this.wordViewModel = wordViewModel;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.cell_card;
    }

    @Override
    protected void onBinding(Word data, CellCardBinding binding, int position) {
        binding.setData(data);
        binding.setIndex(position + 1);
    }

    @Override
    protected void onCreate(CellCardBinding binding) {
        super.onCreate(binding);
        binding.getRoot().setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.youdao.com/w/eng/" + binding.tvWord.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            binding.getRoot().getContext().startActivity(intent);
        });
        binding.swithChineseInvisiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.getData().setChineseInvisiable(isChecked);
                wordViewModel.updateWords(binding.getData());
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder<CellCardBinding> holder) {
        super.onViewAttachedToWindow(holder);
        holder.getBinding().setIndex(holder.getAdapterPosition() + 1);
    }
}
