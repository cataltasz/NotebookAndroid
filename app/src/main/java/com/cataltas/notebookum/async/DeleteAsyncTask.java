package com.cataltas.notebookum.async;

import android.os.AsyncTask;

import com.cataltas.notebookum.models.Note;
import com.cataltas.notebookum.persistance.NoteDAO;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDAO noteDAO;

    public DeleteAsyncTask(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        noteDAO.delete(notes);
        return null;
    }
}
