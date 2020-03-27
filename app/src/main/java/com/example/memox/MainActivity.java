package com.example.memox;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memox.adapters.NoteAdapter;
import com.example.memox.callbacks.MainActionModeCallback;
import com.example.memox.callbacks.NoteEventListener;
import com.example.memox.db.NoteModeleDB;
import com.example.memox.db.NoteDB;
import com.example.memox.model.Note;
import com.example.memox.helpers.NoteHelpers;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.memox.EditNoteActivity.NOTE_EXTRA_Key;

public class MainActivity extends AppCompatActivity implements NoteEventListener, Drawer.OnDrawerItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NoteAdapter adapter;
    private NoteModeleDB modeleDB;
    private MainActionModeCallback actionModeCallback;
    private int nbNotesSelectionnes = 0;
    private FloatingActionButton btnAjouterNote;
    private SharedPreferences parametres;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES="memox_settings";
    private int theme;

    /**********************         SAUVEGARDE NOTE     *************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parametres = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = parametres.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAjouterNote = findViewById(R.id.fab);
        btnAjouterNote.setOnClickListener(view -> onAddNewNote());

        modeleDB = NoteDB.getInstance(this).noteModele();
    }


    /*****************************        NAVBAR        *************************/


    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {


        /***********************     MENU DE LA NAVBAR         *************************/


        List<IDrawerItem> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem().withName("Accueil").withIcon(R.drawable.ic_home_black_24dp));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Notes").withIcon(R.drawable.ic_note_black_24dp));


        /***********************    FOOTER DE LA NAVBAR        *************************/


        List<IDrawerItem> stockyItems = new ArrayList<>();

        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Thème Sombre")
                .withChecked(theme == R.style.AppTheme_Dark)
                .withIcon(R.drawable.ic_dark_theme)
                .withOnCheckedChangeListener((drawerItem, buttonView, isChecked) -> {
                    if (isChecked) {
                        parametres.edit().putInt(THEME_Key, R.style.AppTheme_Dark).apply();
                    } else {
                        parametres.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                    }
                    TaskStackBuilder.create(MainActivity.this)
                            .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
                            .addNextIntent(getIntent()).startActivities();
                });

        stockyItems.add(new PrimaryDrawerItem().withName("Paramètres").withIcon(R.drawable.ic_settings_black_24dp));
        stockyItems.add(switchDrawerItem);


        /**********************      HEADER DE LA NAVBAR      *************************/


        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withEmail("frederic.canaud@etu.unilim.fr")
                        .withName("Frédéric CANAUD")
                        .withIcon(R.mipmap.ic_launcher_round))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .build();


        /**********************         ASPECT GRAPHIQUE      *************************/


        new DrawerBuilder()
                .withActivity(this) // ACTIVITY MAIN
                .withSavedInstance(savedInstanceState) // INSTANCE D'ACTIVITE
                .withOnDrawerItemClickListener(this) // LISTENERS

                .withToolbar(toolbar) // TOOLBAR
                .withDrawerItems(iDrawerItems) // MENU
                .withStickyDrawerItems(stockyItems) // FOOTER
                .withAccountHeader(header) // HEADER

                .withTranslucentNavigationBar(true) // TRANSLUCIDITE
                .build();

    }

    /**********************         CHARGEMENT DES NOTES     *************************/

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = modeleDB.getNotes();
        this.notes.addAll(list);
        this.adapter = new NoteAdapter(this, this.notes);
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();
        swipeToDeleteHelper.attachToRecyclerView(recyclerView);
    }
    /**********************         AFFICHAGE D'AUCUNE NOTE    *************************/

    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    /**********************         AJOUTER NOTE    *************************/

    private void onAddNewNote() {
        startActivity(new Intent(this, EditNoteActivity.class));

    }
    /**********************         AFFICHAGE DU MENU     *************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**********************         AFFICHAGE DES PARAMETRES    *************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    /**********************         MODIFIER NOTE     *************************/

    @Override
    public void onNoteClick(Note note) {
        Intent modifierNote = new Intent(this, EditNoteActivity.class);
        modifierNote.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(modifierNote);

    }

    /**********************         SELECTIONNER DES NOTES     *************************/

    @Override
    public void onNoteLongClick(Note note) {
        note.setChecked(true);
        nbNotesSelectionnes = 1;

        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if (note.isChecked())
                    nbNotesSelectionnes++;
                else nbNotesSelectionnes--;

                if (nbNotesSelectionnes > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                } else actionModeCallback.changeShareItemVisible(true);

                if (nbNotesSelectionnes == 0) {
                    actionModeCallback.getAction().finish();
                }

                actionModeCallback.setCount(nbNotesSelectionnes + "/" + notes.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });

        /**********************       CALLBACK SUPPRIMER / PARTAGER NOTE     *************************/

        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNote();

                actionMode.finish();
                return false;
            }

        };

        startActionMode(actionModeCallback);
        btnAjouterNote.hide();
        actionModeCallback.setCount(nbNotesSelectionnes + "/" + notes.size());
    }

    /**********************         PARTAGER NOTE    *************************/

    private void onShareNote() {

        Note note = adapter.getNotes().get(0);
        Intent partager = new Intent(Intent.ACTION_SEND);
        partager.setType("text/plain");
        String notetext = note.getTexte() + "\n\n Créé le : " +
                NoteHelpers.dateFromLong(note.getDate()) + "\n  grâce à l'application " +
                getString(R.string.app_name);
        partager.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(partager);


    }

    /**********************         SUPPRIMER NOTE PAR SELECTION    *************************/

    private void onDeleteMultiNotes() {

        List<Note> checkedNotes = adapter.getNotes();
        if (checkedNotes.size() != 0) {
            for (Note note : checkedNotes) {
                modeleDB.supprimerNote(note);
            }
            loadNotes();
            Toast.makeText(this, checkedNotes.size() + " Note(s) supprimée(s) !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Aucune note sélectionnée !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false);
        adapter.setListener(this);
        btnAjouterNote.hide();
    }

    /**********************         SUPPRIMER NOTE PAR SWAP    *************************/

    private ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    if (notes != null) {
                        // get swiped note
                        Note noteSwapee = notes.get(viewHolder.getAdapterPosition());
                        if (noteSwapee != null) {
                            supprimerParSwap(noteSwapee, viewHolder);

                        }

                    }
                }
            });

    private void supprimerParSwap(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Supprimer la note ?")
                .setPositiveButton("Supprimer", (dialogInterface, i) -> {
                    modeleDB.supprimerNote(swipedNote);
                    notes.remove(swipedNote);
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    showEmptyView();

                })
                .setNegativeButton("Annuler", (dialogInterface, i) -> recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition()))
                .setCancelable(false)
                .create().show();

    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }
}



