package com.example.hiptactoe;

import android.os.AsyncTask;

import java.util.ArrayList;

public class MinimaxAsyncTask extends AsyncTask<ArrayList, Void, ArrayList> {
    private GameBoardActivity.MinimaxAsyncListener listener;
    private MinimaxBrain brain;

    public MinimaxAsyncTask(GameBoardActivity.MinimaxAsyncListener listener, MinimaxBrain brain) {
        this.listener = listener;
        this.brain = brain;
    }

    @Override
    protected void onPreExecute() {
        listener.onThinking();
    }

    @Override
    protected ArrayList doInBackground(ArrayList... params) {
        return brain.minimaxMove(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        listener.onMoveReady(arrayList);
    }
}
