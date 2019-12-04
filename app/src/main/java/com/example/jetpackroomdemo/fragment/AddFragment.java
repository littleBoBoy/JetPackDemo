package com.example.jetpackroomdemo.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.jetpackroomdemo.R;
import com.example.jetpackroomdemo.Word;
import com.example.jetpackroomdemo.WordViewModel;
import com.example.jetpackroomdemo.databinding.FragmentAddBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private WordViewModel wordViewModel;
    private NavController controller;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        binding.setLifecycleOwner(this);
        wordViewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        controller = Navigation.findNavController(requireActivity(), R.id.fragment);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.btnSubmit.setEnabled(false);
        binding.etEnglishWord.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.etEnglishWord, 0);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = binding.etEnglishWord.getText().toString().trim();
                String chinese = binding.etChinses.getText().toString().trim();
                if (!english.isEmpty() && !chinese.isEmpty()) {
                    binding.btnSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.etChinses.addTextChangedListener(watcher);
        binding.etEnglishWord.addTextChangedListener(watcher);

        binding.btnSubmit.setOnClickListener(v -> {
            String english = binding.etEnglishWord.getText().toString().trim();
            String chinese = binding.etChinses.getText().toString().trim();
            Word word = new Word(english, chinese);
            wordViewModel.insertWords(word);
            controller.navigateUp();
        });
    }
}
