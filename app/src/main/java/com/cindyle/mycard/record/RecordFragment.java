package com.cindyle.mycard.record;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cindyle.mycard.MainActivity;
import com.example.mycard.databinding.FragmentRecordBinding;
import com.cindyle.mycard.room.RecordBean;
import com.cindyle.mycard.room.RecordDb;


public class RecordFragment extends Fragment {
    private RecordDb database;
    private RecordAdapter adapter;
    private RecordViewModel mVModel;
    private FragmentRecordBinding binding;
    private Activity host;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(getLayoutInflater());
        mVModel = new ViewModelProvider((FragmentActivity) getContext()).get(RecordViewModel.class);
        database = Room.databaseBuilder(getActivity(), RecordDb.class, "data_cards.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        mVModel.getRecordList(database);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setObserve();
    }

    public void initView() {
        adapter = new RecordAdapter();
        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(adapter);

        adapter.setListener(new RecordAdapter.RecordListener() {
            @Override
            public void onClick(int pos, RecordBean data) {
                if(host instanceof MainActivity) {
                    MainActivity main = (MainActivity) host;
                    main.openRecord(data.id);
                }
            }

            @Override
            public void onRemove(int pos, RecordBean data) {
                database.recordDao().deleteBean(data);
                setObserve();
            }
        });
    }

    public void setObserve(){
        if (adapter != null){
            mVModel.getRecordList(database);
            mVModel.list.observe(getActivity(), list -> {
                adapter.setList(list);
            });

            mVModel.isEmpty.observe(getActivity(), isEmpty ->{
                if (isEmpty){
                    binding.txtEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.txtEmpty.setVisibility(View.GONE);
                }
            });
        }
    }
}