package com.cindyle.mycard.record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cindyle.mycard.room.RecordBean;
import com.cindyle.mycard.tools.SwipeMenuLayout;
import com.example.mycard.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ItemViewHolder> {
    private RecordListener listener;
    List<RecordBean> list = new ArrayList<>();

    public interface RecordListener {
        void onClick(int pos, RecordBean data);
        void onRemove(int pos, RecordBean data);
    }

    public void setListener(RecordListener listener) {
        this.listener = listener;
    }

    public void setList(List<RecordBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        RecordBean data = list.get(position);

        holder.time.setText(data.time);
        holder.question.setText(data.question);

        holder.item.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(position, data);
        });

        holder.remove.setOnClickListener(v -> {
            holder.infoSL.quickClose();
            if (listener != null)
                listener.onRemove(position, data);
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item, remove;
        SwipeMenuLayout infoSL;
        TextView time, question;

        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            infoSL = itemView.findViewById(R.id.infoSL);
            item = itemView.findViewById(R.id.layout_item);
            time = itemView.findViewById(R.id.time);
            question = itemView.findViewById(R.id.question);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}