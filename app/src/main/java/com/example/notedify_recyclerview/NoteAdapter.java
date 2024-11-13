package com.example.notedify_recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesVH> {
    private Context ctx;
    private List<Notes> originalData;
    private List<Notes> filteredData;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Notes note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoteAdapter(Context ctx, List<Notes> data) {
        this.ctx = ctx;
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public NotesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(this.ctx)
                .inflate(R.layout.item_note, parent, false);
        return new NotesVH(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesVH holder, int position) {
        Notes note = this.filteredData.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvCategory.setText(note.getCategory());
        holder.tvDate.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return this.filteredData.size();
    }

    public void filterByTitle(String query) {
        filteredData.clear();
        if (query.isEmpty()) {
            filteredData.addAll(originalData);
        } else {
            String lowercaseQuery = query.toLowerCase();
            for (Notes note : originalData) {
                if (note.getTitle().toLowerCase().contains(lowercaseQuery)) {
                    filteredData.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        filteredData.clear();
        if (category.isEmpty()) {
            filteredData.addAll(originalData);
        } else {
            for (Notes note : originalData) {
                if (note.getCategory().equalsIgnoreCase(category)) {
                    filteredData.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByDate(String date) {
        filteredData.clear();
        if (date.isEmpty()) {
            filteredData.addAll(originalData);
        } else {
            for (Notes note : originalData) {
                if (note.getDate().equals(date)) {
                    filteredData.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class NotesVH extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvCategory;
        private final TextView tvDate;

        public NotesVH(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.tvCategory = itemView.findViewById(R.id.tvCategory);
            this.tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(filteredData.get(position));
                    }
                }
            });
        }
    }
    public void clearNotes() {
        filteredData.clear();
        notifyDataSetChanged();
    }
    public void setNotes(List<Notes> notes) {
        this.filteredData = new ArrayList<>(notes);
        notifyDataSetChanged();
    }
}