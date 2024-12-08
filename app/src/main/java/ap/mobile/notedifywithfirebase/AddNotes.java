package ap.mobile.notedifywithfirebase;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class AddNotes extends AppCompatActivity {
    private AddNotesFragment addNotesFragment;
    private SharedToFragment sharedToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        addNotesFragment = AddNotesFragment.newInstance(
                getIntent().getStringExtra("NOTE_ID"),
                getIntent().getStringExtra("NOTE_TITLE"),
                getIntent().getStringExtra("NOTE_CONTENT")
        );

        sharedToFragment = new SharedToFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, addNotesFragment)
                .commit();
    }

    public void switchToSharedFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, sharedToFragment);
        transaction.commit();
    }

    public void switchToAddNotesFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, addNotesFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AddNotesFragment fragment = (AddNotesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (fragment != null) {
            fragment.saveNoteAndNavigateBack();
        } else {
            super.onBackPressed();
        }
    }
}