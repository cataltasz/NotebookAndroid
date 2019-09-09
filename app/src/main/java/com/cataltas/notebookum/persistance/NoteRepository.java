package com.cataltas.notebookum.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cataltas.notebookum.async.DeleteAsyncTask;
import com.cataltas.notebookum.async.InsertAsyncTask;
import com.cataltas.notebookum.async.UpdateAsyncTask;
import com.cataltas.notebookum.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context){
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNotDAO()).execute(note);
    }

    public void updateNote(Note note){
        new UpdateAsyncTask((mNoteDatabase.getNotDAO())).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask(){
        return mNoteDatabase.getNotDAO().getNotes();
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNotDAO()).execute(note);
    }

}
