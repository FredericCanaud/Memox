package com.example.memox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.memox.db.NoteDB;
import com.example.memox.db.NoteModeleDB;
import com.example.memox.model.Note;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    private EditText inputNote;
    private NoteModeleDB modeleDB;
    private Note temp;
    public static final String NOTE_EXTRA_Key = "note_id";

    /**********************         INITIALISATION ACTIVITÉ     *************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = preferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_note);
        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);

        inputNote = findViewById(R.id.input_note);
        modeleDB = NoteDB.getInstance(this).noteModele();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = modeleDB.getNoteById(id);
            inputNote.setText(temp.getTexte());
        } else inputNote.setFocusable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    /**********************         SAUVEGARDE NOTE     *************************/

    private void onSaveNote() {
        String text = inputNote.getText().toString();
        if (!text.isEmpty()) {
            long date = new Date().getTime();
            if (temp == null) {
                temp = new Note(text, date);
                modeleDB.sauvegarderNote(temp);
            } else {
                temp.setTexte(text);
                temp.setDate(date);
                modeleDB.modifierNote(temp);
            }

            finish();
        }

    }
}
