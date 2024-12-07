package ap.mobile.notedifywithfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ap.mobile.notedifywithfirebase.database.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNotes extends AppCompatActivity {
    private EditText titleEditText;
    private EditText noteContentEditText;
    private TextView lastEditedText;
    private SimpleDateFormat timeFormat;

    private DatabaseReference databaseReference;
    private String noteId; // ID Note untuk edit atau null jika membuat baru

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        databaseReference = FirebaseDatabase.getInstance("https://notedifycreatenotes-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("notes");

        initializeViews();
        setupClickListeners();

        // Periksa apakah ini mode edit
        noteId = getIntent().getStringExtra("NOTE_ID");
        if (noteId != null) {
            String title = getIntent().getStringExtra("NOTE_TITLE");
            String content = getIntent().getStringExtra("NOTE_CONTENT");

            titleEditText.setText(title);
            noteContentEditText.setText(content);
            updateLastEditedTime();
        } else {
            updateLastEditedTime();
        }
    }

    private void initializeViews() {
        titleEditText = findViewById(R.id.titleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        lastEditedText = findViewById(R.id.lastEditedText);
    }

    private void setupClickListeners() {
        findViewById(R.id.backButton).setOnClickListener(v -> saveNoteAndNavigateBack());
        findViewById(R.id.boldButton).setOnClickListener(v -> applyTextStyle("bold"));
        findViewById(R.id.italicButton).setOnClickListener(v -> applyTextStyle("italic"));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateLastEditedTime();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        titleEditText.addTextChangedListener(textWatcher);
        noteContentEditText.addTextChangedListener(textWatcher);
    }

    private void updateLastEditedTime() {
        String currentTime = timeFormat.format(new Date());
        lastEditedText.setText("Last edited on " + currentTime);
    }

    private void saveNoteAndNavigateBack() {
        String title = titleEditText.getText().toString().trim();
        String content = noteContentEditText.getText().toString().trim();

        if (!title.isEmpty() || !content.isEmpty()) {
            if (noteId != null) {
                // Edit existing note
                databaseReference.child(noteId).child("title").setValue(title);
                databaseReference.child(noteId).child("content").setValue(content);
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            } else {
                // Create new note
                Note note = new Note(title, content, getIntent().getStringExtra("NOTE_CATEGORY"), false);
                String id = databaseReference.push().getKey();
                note.setId(id);

                databaseReference.child(id).setValue(note).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void applyTextStyle(String style) {
        EditText focusedEditText = titleEditText.hasFocus() ? titleEditText : noteContentEditText;
        int start = focusedEditText.getSelectionStart();
        int end = focusedEditText.getSelectionEnd();

        if (start >= 0 && end > start) {
            Spannable str = focusedEditText.getText();
            if ("bold".equals(style)) {
                str.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if ("italic".equals(style)) {
                str.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNoteAndNavigateBack();
    }
}
