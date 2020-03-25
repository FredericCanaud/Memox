package com.example.memox.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.memox.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    public abstract NotesModeleDB notesInterface();

    public static final String NOM_DB = "notesDb";
    private static NotesDB instanceDB;

    public static NotesDB getInstance(Context context) {
        if (instanceDB == null)
            instanceDB = Room.databaseBuilder(context, NotesDB.class, NOM_DB)
                    .allowMainThreadQueries()
                    .build();
        return instanceDB;
    }
}
