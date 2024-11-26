package ap.mobile.notedifywithfirebase.homepage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ap.mobile.notedifywithfirebase.AddNotes;
import ap.mobile.notedifywithfirebase.R;
import ap.mobile.notedifywithfirebase.database.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());

        // Handle edit button click
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNotes.class);
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());
            context.startActivity(intent);
        });

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://notedifycreatenotes-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("notes")
                    .child(note.getId());

            databaseReference.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
                    notes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, notes.size());
                } else {
                    Toast.makeText(context, "Failed to delete note", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Handle item click (optional)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddNotes.class);
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        ImageButton editButton, deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
