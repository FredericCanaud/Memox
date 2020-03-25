package com.example.memox.callbacks;

import com.example.memox.model.Note;

public interface NoteEventListener {

     /***************************************************************
     *
     *   Methode appelee lors d'un clic sur une note
     *   Parametre : Une note
     *
     ***************************************************************/
     void onNoteClick(Note note);

     /***************************************************************
     *
     *   Methode appelee lors d'un long clic sur une note
     *   Parametre : Une note
     *
     ***************************************************************/
    void onNoteLongClick(Note note);
}
