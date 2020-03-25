package com.example.memox.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.memox.model.Note;

import java.util.List;

@Dao
public interface NotesModeleDB {

     /***************************************************************
     *
     *   Methode d'insertion et de sauvegarde des notes dans la BD
     *   Parametre d'entree : Note
     *
     **************************************************************/
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     void sauvegarderNote(Note note);

     /***************************************************************
     *
     *   Méthode de suppression de notes dans la BD
     *   Parametre d'entree : Note
     *
     ***************************************************************/
     @Delete
     void supprimerNote(Note... note);

     /***************************************************************
     *
     *   Méthode de modification de notes dans la BD
     *   Parametre d'entree : Note
     *
     ***************************************************************/
     @Update
     void modifierNote(Note note);


     ///////////////////////////    REQUETES    /////////////////////


     /***************************************************************
     *
     *   Requete retournant toutes les notes de la BD
     *
     ***************************************************************/
     @Query("SELECT * FROM notes")
     List<Note> getNotes();

     /***************************************************************
     *
     *   Requete retournant une note en particulier, en fonction de
     *   son identifiant
     *   Parametre d'entree : Identifiant de la note (entier)
     *
     ***************************************************************/
     @Query("SELECT * FROM notes WHERE id = :noteId")
     Note getNoteById(int noteId);

     /***************************************************************
     *
     *   Requete supprimant une note de la BD
     *   Parametre d'entree : Identifiant de la note (entier)
     *
     ***************************************************************/
     @Query("DELETE FROM notes WHERE id = :noteId")
     void deleteNoteById(int noteId);

}
