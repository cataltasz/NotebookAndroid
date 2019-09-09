package com.cataltas.notebookum.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cataltas.notebookum.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    long[] insert(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Delete
    int delete(Note... notes);

    @Update
    int update(Note... notes);

}
