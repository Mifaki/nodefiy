package com.example.notedify_recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements NoteAdapter.OnItemClickListener {
    private List<Notes> allNotes;
    private NoteAdapter notesAdapter;
    private RecyclerView rvNotes;
    private EditText etSearch;
    private ImageButton btnSearch;
    private Button btnTitle, btnCategory, btnDates;
    private String currentSearchMode = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        rvNotes = findViewById(R.id.rvNotes);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnTitle = findViewById(R.id.btnTitle);
        btnCategory = findViewById(R.id.btnCategory);
        btnDates = findViewById(R.id.btnDates);

        // Create dummy data
        createDummyData();

        // Set up RecyclerView with an empty list initially
        notesAdapter = new NoteAdapter(this, new ArrayList<>());
        notesAdapter.setOnItemClickListener(this);
        rvNotes.setAdapter(notesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up search button click listener
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(etSearch.getText().toString());
            }
        });

        // Set up button listeners
        btnTitle.setOnClickListener(v -> {
            currentSearchMode = "title";
            updateButtonColors();
            performSearch(etSearch.getText().toString());
        });

        btnCategory.setOnClickListener(v -> {
            currentSearchMode = "category";
            updateButtonColors();
            performSearch(etSearch.getText().toString());
        });

        btnDates.setOnClickListener(v -> {
            currentSearchMode = "dates";
            updateButtonColors();
            performSearch(etSearch.getText().toString());
        });

        updateButtonColors();
    }

    private void createDummyData() {
        allNotes = new ArrayList<>();
        allNotes.add(new Notes("The Who, What, Why of AI", "If AI is the answer, then what is the question?", "Interesting Idea", "01/02/2024"));
        allNotes.add(new Notes("New Product Idea Design", "Create a mobile app UI Kit that provide a basic notes functionality but with some improvement.", "Interesting Idea", "03/02/2024"));
        allNotes.add(new Notes("Meeting Notes: Project X", "Discussed timeline and resource allocation for Project X", "Work", "05/02/2024"));
        allNotes.add(new Notes("Birthday Gift Ideas", "List of potential gifts for Mom's birthday next month", "Personal", "07/02/2024"));
        allNotes.add(new Notes("Book Review: The Alchemist", "Thoughts and reflections on Paulo Coelho's The Alchemist", "Book Notes", "10/02/2024"));
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            notesAdapter.clearNotes();
            rvNotes.setVisibility(View.GONE);  // Sembunyikan RecyclerView jika tidak ada hasil
        } else {
            List<Notes> searchResults = new ArrayList<>();
            switch (currentSearchMode) {
                case "title":
                    for (Notes note : allNotes) {
                        if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            searchResults.add(note);
                        }
                    }
                    break;
                // Kamu juga bisa tambahkan "category" dan "dates" search di sini jika diperlukan.
            }

            if (!searchResults.isEmpty()) {
                notesAdapter.setNotes(searchResults);
                rvNotes.setVisibility(View.VISIBLE);  // Tampilkan RecyclerView jika ada hasil
            } else {
                notesAdapter.clearNotes();
                rvNotes.setVisibility(View.GONE);  // Sembunyikan RecyclerView jika tidak ada hasil
            }
        }
    }


    private void updateButtonColors() {
        btnTitle.setTextColor(currentSearchMode.equals("title") ? Color.parseColor("#6750A4") : Color.BLACK);
        btnCategory.setTextColor(currentSearchMode.equals("category") ? Color.parseColor("#6750A4") : Color.BLACK);
        btnDates.setTextColor(currentSearchMode.equals("dates") ? Color.parseColor("#6750A4") : Color.BLACK);
    }

    @Override
    public void onItemClick(Notes note) {
        Toast.makeText(this, note.getTitle(), Toast.LENGTH_SHORT).show();
    }
}