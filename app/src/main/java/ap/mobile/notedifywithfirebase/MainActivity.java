package ap.mobile.notedifywithfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ap.mobile.notedifywithfirebase.database.Note;
import ap.mobile.notedifywithfirebase.homepage.NoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvIdeas, rvBuy, rvGoals, rvGuidance, rvRoutine;
    private NoteAdapter ideasAdapter, buyAdapter, goalsAdapter, guidanceAdapter, routineAdapter;
    private List<Note> ideasList, buyList, goalList, guidanceList, routineList;
    private DatabaseReference notesRef;
    private TextView userNoteCount;
    private FloatingActionButton fab; // Deklarasikan FloatingActionButton
    private int totalNotes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ideasList = new ArrayList<>();
        buyList = new ArrayList<>();
        goalList = new ArrayList<>();
        guidanceList = new ArrayList<>();
        routineList = new ArrayList<>();

        userNoteCount = findViewById(R.id.userNoteCount);
        rvIdeas = findViewById(R.id.rvIdeas);
        rvBuy = findViewById(R.id.rvBuying);
        rvGoals = findViewById(R.id.rvGoals);
        rvGuidance = findViewById(R.id.rvGuidance);
        rvRoutine = findViewById(R.id.rvRoutine);

        rvIdeas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvBuy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGoals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGuidance.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRoutine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ideasAdapter = new NoteAdapter(this, ideasList);
        buyAdapter = new NoteAdapter(this, buyList);
        goalsAdapter = new NoteAdapter(this, goalList);
        guidanceAdapter = new NoteAdapter(this, guidanceList);
        routineAdapter = new NoteAdapter(this, routineList);

        rvIdeas.setAdapter(ideasAdapter);
        rvBuy.setAdapter(buyAdapter);
        rvGoals.setAdapter(goalsAdapter);
        rvGuidance.setAdapter(guidanceAdapter);
        rvRoutine.setAdapter(routineAdapter);

        notesRef = FirebaseDatabase.getInstance("https://notedifycreatenotes-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("notes");

        fab = findViewById(R.id.fab); // Inisialisasi FloatingActionButton

        // Menambahkan onClickListener untuk FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Anda dapat menambahkan aksi di sini, seperti membuka halaman baru
                // Sebagai contoh, membuka activity untuk menambah note baru
                Intent intent = new Intent(MainActivity.this, SelectCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadNotes();
    }

    private void loadNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ideasList.clear();
                buyList.clear();
                goalList.clear();
                guidanceList.clear();
                routineList.clear();

                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    if (note != null) {
                        note.setId(noteSnapshot.getKey()); // Set ID dari Firebase
                        switch (note.getCategory()){
                            case "Interesting Ideas":
                                ideasList.add(note);
                                break;
                            case "Buying Something":
                                buyList.add(note);
                                break;
                            case "Goals":
                                goalList.add(note);
                                break;
                            case "Guidance":
                                guidanceList.add(note);
                                break;
                            case "Routine Tasks":
                                routineList.add(note);
                                break;
                        }
                        totalNotes += 1;
                    }
                }
                if (ideasList.isEmpty()) {
                    ideasList.add(new Note("You haven't made any notes yet", "Made a note to organize your plans", "Interesting Ideas"));
                }
                if (buyList.isEmpty()) {
                    buyList.add(new Note("You haven't made any notes yet", "Made a note to organize your plans", "Buying Something"));
                }
                if (goalList.isEmpty()) {
                    goalList.add(new Note("You haven't made any notes yet", "Made a note to organize your plans", "Goals"));
                }
                if (guidanceList.isEmpty()) {
                    guidanceList.add(new Note("You haven't made any notes yet", "Made a note to organize your plans", "Guidance"));
                }
                if (routineList.isEmpty()) {
                    routineList.add(new Note("You haven't made any notes yet", "Made a note to organize your plans", "Routine Tasks"));
                }

                ideasAdapter.notifyDataSetChanged();
                buyAdapter.notifyDataSetChanged();
                goalsAdapter.notifyDataSetChanged();
                guidanceAdapter.notifyDataSetChanged();
                routineAdapter.notifyDataSetChanged();

                userNoteCount.setText(totalNotes + " notes");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load notes: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
