package com.example.memox.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.memox.model.Note;

/**********************         INITIALISATION BD     *************************/

@Database(entities = Note.class, version = 1)

public abstract class NoteDB extends RoomDatabase {

    public abstract NoteModeleDB noteModele();

    public static final String NOM_DB = "notesDb";
    private static NoteDB instanceDB;

    public static NoteDB getInstance(Context context) {
        if (instanceDB == null)
            instanceDB = Room.databaseBuilder(context, NoteDB.class, NOM_DB)
                    .allowMainThreadQueries()
                    .build();
        return instanceDB;
    }
}
