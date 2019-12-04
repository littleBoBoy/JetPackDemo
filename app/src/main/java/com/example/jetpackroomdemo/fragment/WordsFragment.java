package com.example.jetpackroomdemo.fragment;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.jetpackroomdemo.R;
import com.example.jetpackroomdemo.Word;
import com.example.jetpackroomdemo.WordViewModel;
import com.example.jetpackroomdemo.databinding.CellCardBinding;
import com.example.jetpackroomdemo.databinding.FragmentWordsBinding;
import com.example.jetpackroomdemo.rvadapter.WordRecyclerViewAdapter;
import com.example.jetpackroomdemo.rvadapter.base.BaseViewHolder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    private WordViewModel wordViewModel;
    private WordRecyclerViewAdapter cardAdapter;
    private FragmentWordsBinding binding;
    private NavController navController;
    private LiveData<List<Word>> listLiveData;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_words, container, false);
        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        binding.setVm(wordViewModel);
        cardAdapter = new WordRecyclerViewAdapter(requireContext(), wordViewModel);
        binding.wordsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.wordsRecyclerView.setAdapter(cardAdapter);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment);
        binding.btnInsert.setOnClickListener(v -> {
            navController.navigate(R.id.action_wordsFragment_to_addFragment);
        });
        binding.wordsRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager manager = (LinearLayoutManager) binding.wordsRecyclerView.getLayoutManager();
                if (manager != null) {
                    int first = manager.findFirstVisibleItemPosition();
                    int last = manager.findLastVisibleItemPosition();
                    for (int i = first; i <= last; i++) {
                        BaseViewHolder<CellCardBinding> baseViewHolder = (BaseViewHolder<CellCardBinding>) binding.wordsRecyclerView.findViewHolderForAdapterPosition(i);
                        if (baseViewHolder != null)
                            baseViewHolder.getBinding().setIndex(i + 1);
                    }
                }

            }
        });
        //两种视觉效果
//        listLiveData = wordViewModel.findWordsWithPatten("");
        listLiveData = wordViewModel.getAllWordsLive();
        listLiveData.observe(getViewLifecycleOwner(), words -> {
            WordRecyclerViewAdapter adapter = (WordRecyclerViewAdapter) binding.wordsRecyclerView.getAdapter();
            int oldSize = adapter.getItemCount();
            if (oldSize != words.size()) {
                if (oldSize < words.size()) {
                    binding.wordsRecyclerView.smoothScrollBy(0, -400);
                }
                adapter.submitList(words);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Word wordForm = ((BaseViewHolder<CellCardBinding>) viewHolder).getBinding().getData();
                Word wordTo = ((BaseViewHolder<CellCardBinding>) target).getBinding().getData();

                int id = wordForm.getId();
                wordForm.setId(wordTo.getId());
                wordTo.setId(id);

                wordViewModel.updateWords(wordForm, wordTo);

                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Word data = ((BaseViewHolder<CellCardBinding>) viewHolder).getBinding().getData();
                wordViewModel.deleteWords(data);
                Snackbar.make(binding.root, "删除了一个词汇", Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.insertWords(data);
                            }
                        }).show();
            }

            Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_forever_black_24dp);
            Drawable bg = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                int iconLeft, iconRight, iconTop, iconBottom;
                int backLeft, backRight, backTop, backBottom;

                backTop = itemView.getTop();
                backBottom = itemView.getBottom();

                iconTop = itemView.getTop() + iconMargin;
                iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    bg.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    bg.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    bg.setBounds(0, 0, 0, 0);
                    icon.setBounds(0, 0, 0, 0);
                }
                bg.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(binding.wordsRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(800);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String patten = newText.trim();
                if (listLiveData != null) {
                    listLiveData.removeObservers(getViewLifecycleOwner());
                }
                listLiveData = wordViewModel.findWordsWithPatten(patten);
                listLiveData.observe(getViewLifecycleOwner(), words -> {
                    WordRecyclerViewAdapter adapter = (WordRecyclerViewAdapter) binding.wordsRecyclerView.getAdapter();
                    int oldSize = adapter.getItemCount();
                    if (oldSize != words.size()) {
                        if (oldSize < words.size()) {
                            binding.wordsRecyclerView.smoothScrollBy(0, -400);
                        }
                        adapter.submitList(words);
                    }

                });
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }
}
