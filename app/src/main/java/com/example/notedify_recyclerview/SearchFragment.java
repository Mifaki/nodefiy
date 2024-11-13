package com.example.notedify_recyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements NoteAdapter.OnItemClickListener {
    private List<Notes> allNotes;
    private NoteAdapter notesAdapter;
    private RecyclerView rvNotes;
    private EditText etSearch;
    private ImageButton btnSearch;
    private Button btnTitle, btnCategory, btnDates;
    private String currentSearchMode = "title";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvNotes = view.findViewById(R.id.rvNotes);
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnTitle = view.findViewById(R.id.btnTitle);
        btnCategory = view.findViewById(R.id.btnCategory);
        btnDates = view.findViewById(R.id.btnDates);

        createDummyData();

        notesAdapter = new NoteAdapter(getContext(), new ArrayList<>());
        notesAdapter.setOnItemClickListener(this);
        rvNotes.setAdapter(notesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

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

        return view;
    }

    private void createDummyData() {
        allNotes = new ArrayList<>();
        allNotes.add(new Notes("The Who, What, Why of AI", "If AI is the answer, then what is the question?", "Interesting Idea", "01/02/2024"));
        allNotes.add(new Notes("New Product Idea Design", "Create a mobile app UI Kit that provide a basic notes functionality but with some improvement.", "Interesting Idea", "03/02/2024"));
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            notesAdapter.clearNotes();
            rvNotes.setVisibility(View.GONE);
        } else {
            List<Notes> searchResults = new ArrayList<>();
            if (currentSearchMode.equals("title")) {
                for (Notes note : allNotes) {
                    if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(note);
                    }
                }
            }
            if (!searchResults.isEmpty()) {
                notesAdapter.setNotes(searchResults);
                rvNotes.setVisibility(View.VISIBLE);
            } else {
                notesAdapter.clearNotes();
                rvNotes.setVisibility(View.GONE);
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
        Toast.makeText(getContext(), note.getTitle(), Toast.LENGTH_SHORT).show();
    }
}