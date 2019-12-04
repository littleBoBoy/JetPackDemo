package com.example.jetpackroomdemo.rvadapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public abstract class BaseRVDataBindingAdapter<T, B extends ViewDataBinding> extends ListAdapter<T, BaseViewHolder<B>> {


    public Context getContext() {
        return context;
    }

    private Context context;

    public BaseRVDataBindingAdapter(Context context, DiffUtil.ItemCallback<T> itemCallback) {
        super(itemCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public BaseViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResId(viewType), parent, false);
        onCreate(binding);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<B> holder, int position) {
        onBinding(getItem(position), holder.getBinding(), position);
        holder.getBinding().executePendingBindings();
    }

    protected void onCreate(B binding) {

    }

    protected abstract @LayoutRes
    int getLayoutResId(int viewType);

    protected abstract void onBinding(T data, B binding, int position);

}
