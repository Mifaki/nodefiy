package com.example.notedify_recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesVH> {

    private final Context ctx;
    private List<Notes> dataset;
    private OnItemClickListener listener;
    private OnNoteDeletedListener deleteListener;
    private DatabaseReference notesRef;

    public interface OnNoteDeletedListener {
        void onNoteDeleted();
    }

    public NotesAdapter(Context ctx, List<Notes> dataset, OnItemClickListener listener, OnNoteDeletedListener deleteListener) {
        this.ctx = ctx;
        this.dataset = dataset;
        this.listener = listener;
        this.deleteListener = deleteListener;
        this.notesRef = FirebaseDatabase.getInstance(MainActivity.FirebaseURL).getReference("notes");
    }

    public class NotesVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle, tvContent, tvCategory, tvDate;
        private final ImageButton btDelete;
        private Notes note;

        public NotesVH(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvContent = itemView.findViewById(R.id.tvContent);
            this.tvCategory = itemView.findViewById(R.id.tvCategory);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.btDelete = itemView.findViewById(R.id.btDelete);

            this.btDelete.setOnClickListener(this);
            itemView.setOnClickListener(v -> listener.onItemClick(note));
        }

        public void bind(Notes note) {
            this.note = note;
            this.tvTitle.setText(note.getTitle());
            this.tvContent.setText(note.getContent());
            this.tvCategory.setText(note.getCategory());
            this.tvDate.setText(note.getDate());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btDelete) {
                deleteNote();
            }
        }

        private void deleteNote() {
            if (note != null && note.getId() != null) {
                notesRef.child(note.getId()).removeValue()
                        .addOnSuccessListener(unused -> {
                            if (deleteListener != null) {
                                deleteListener.onNoteDeleted();
                            }
                            Toast.makeText(ctx, "Note deleted", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(ctx, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
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
        holder.bind(this.dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public void setNotes(List<Notes> notes) {
        this.dataset = notes;
    }

    public interface OnItemClickListener {
        void onItemClick(Notes note);
    }
}