package com.example.notedify_recyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NotesAdapter.OnItemClickListener,
        NotesAdapter.OnNoteDeletedListener {

    public static final
    String FirebaseURL = "https://notedify-search-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView rvNotes;
    private NotesAdapter notesAdapter;
    private EditText etSearch;
    private ImageButton btnBack, btnSearch;
    private Button btnCategory, btnDates;
    private List<Notes> dataset;
    private FirebaseDatabase db;
    private DatabaseReference notesRef;

    private static final int SEARCH_TITLE = 0;
    private static final int SEARCH_CATEGORY = 1;
    private static final int SEARCH_DATE = 2;
    private int currentSearchMode = SEARCH_TITLE;
    private String lastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupFirebase();
        loadNotes();
    }

    private void initializeViews() {
        this.rvNotes = findViewById(R.id.rvNotes);
        this.etSearch = findViewById(R.id.etSearch);
        this.btnBack = findViewById(R.id.btnBack);
        this.btnSearch = findViewById(R.id.btnSearch);
        this.btnCategory = findViewById(R.id.btnCategory);
        this.btnDates = findViewById(R.id.btnDates);

        this.btnBack.setOnClickListener(this);
        this.btnSearch.setOnClickListener(this);
        this.btnCategory.setOnClickListener(this);
        this.btnDates.setOnClickListener(this);

        this.dataset = new ArrayList<>();
        this.notesAdapter = new NotesAdapter(this, this.dataset, this, this);
        this.rvNotes.setAdapter(this.notesAdapter);
        this.rvNotes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupFirebase() {
        this.db = FirebaseDatabase.getInstance(FirebaseURL);
        this.notesRef = this.db.getReference("notes");
    }

    private void loadNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataset.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Notes note = childSnapshot.getValue(Notes.class);
                    if (note != null) {
                        note.setId(childSnapshot.getKey());
                        dataset.add(note);
                    }
                }
                notesAdapter.notifyDataSetChanged();
                rvNotes.setVisibility(View.VISIBLE);

                if (!lastQuery.isEmpty()) {
                    performSearch();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load notes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnSearch) {
            performSearch();
        } else if (viewId == R.id.btnCategory) {
            toggleSearchMode(SEARCH_CATEGORY);
        } else if (viewId == R.id.btnDates) {
            toggleSearchMode(SEARCH_DATE);
        } else if (viewId == R.id.btnBack) {
            resetSearch();
        }
    }

    private void toggleSearchMode(int mode) {
        if (currentSearchMode == mode) {
            currentSearchMode = SEARCH_TITLE;
        } else {
            currentSearchMode = mode;
        }
        updateSearchModeButtons();
    }

    private void updateSearchModeButtons() {
        int purpleColor = ContextCompat.getColor(this, R.color.purple_500);
        int blackColor = ContextCompat.getColor(this, android.R.color.black);

        btnCategory.setTextColor(currentSearchMode == SEARCH_CATEGORY ? purpleColor : blackColor);
        btnDates.setTextColor(currentSearchMode == SEARCH_DATE ? purpleColor : blackColor);
    }

    private void resetSearch() {
        this.etSearch.setText("");
        lastQuery = "";
        currentSearchMode = SEARCH_TITLE;
        updateSearchModeButtons();
        loadNotes();
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        lastQuery = query;

        List<Notes> searchResults = new ArrayList<>();

        for (Notes note : this.dataset) {
            switch (currentSearchMode) {
                case SEARCH_CATEGORY:
                    if (note.getCategory().toLowerCase().contains(query)) {
                        searchResults.add(note);
                    }
                    break;
                case SEARCH_DATE:
                    if (note.getDate().toLowerCase().contains(query)) {
                        searchResults.add(note);
                    }
                    break;
                default:
                    if (note.getTitle().toLowerCase().contains(query)) {
                        searchResults.add(note);
                    }
                    break;
            }
        }

        this.notesAdapter.setNotes(searchResults);
        this.notesAdapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            this.rvNotes.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Notes note) {
        Toast.makeText(this, note.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteDeleted() {
        if (!lastQuery.isEmpty()) {
            performSearch();
        } else {
            loadNotes();
        }
    }
}