package com.example.hiptactoe;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class GameBoardActivity extends ActionBarActivity {
    ArrayList<String> gameBoard;
    MinimaxBrain brain;
    TileAdapter adapter;
    TextView tvWinner;
    ProgressBar pbThinking;
    private MinimaxAsyncListener listener;
    GridView gvTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeGameBoard();
        pbThinking = (ProgressBar) findViewById(R.id.pbThinking);
        listener = new MinimaxAsyncListener() {
            @Override
            public void onMoveReady(ArrayList result) {

                pbThinking.setVisibility(View.INVISIBLE);
                gameBoard.clear();
                gameBoard.addAll(result);
                adapter.notifyDataSetChanged();
                adapter.enableAll();
                checkForComputerWin();
            }

            @Override
            public void onThinking() {
                pbThinking.setVisibility(View.VISIBLE);
                adapter.disableAll();
            }
        };
        tvWinner = (TextView) findViewById(R.id.tvWinner);
        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
                adapter.notifyDataSetChanged();
            }
        });
        brain = new MinimaxBrain();

        adapter = new TileAdapter(this, R.layout.board_tile, gameBoard);
        gvTiles = (GridView) findViewById(R.id.gvTiles);
        gvTiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (gameBoard.get(position).equals("")){
                    markWithX(position);
                    adapter.notifyDataSetChanged();
                    int currentScore = brain.gameScore(gameBoard);
                    if (currentScore == brain.BOARD_NOT_FULL) {
                        minimaxBrainTakesTurn();
                    } else {
                        endGame(currentScore);
                    }

                    adapter.notifyDataSetChanged();

                }
            }

            private void markWithX(int position){
                gameBoard.set(position, "X");
            }
        });
        gvTiles.setAdapter(adapter);

    }

    private void checkForComputerWin() {
        int score = brain.gameScore(gameBoard);
        if (score == 1){
            endGame(score);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeGameBoard(){
        gameBoard = new ArrayList<>(Arrays.asList("", "", "",
                                                  "", "", "",
                                                  "", "", "" ));

    }
    private void newGame(){
        tvWinner.setText("");
        adapter.enableAll();
        initializeGameBoard();
        clearTokens();
    }

    private void clearTokens() {
        for(int i=0; i < gameBoard.size(); i++){
            ImageView iv = (ImageView) gvTiles.getChildAt(i).findViewById(R.id.ivGameToken);
            iv.setImageResource(android.R.color.transparent);
        }
    }

    public void endGame(int score){
        String text;
        switch (score){
            case 1:
                text = "Taytay Wins!";
                break;
            case -1:
                text = "Katy Wins!";
                break;
            case 0:
                text = "Bad Blood (Draw)";
                break;
            default:
                text = "";
                break;
        }

        tvWinner.setText(text);
        adapter.disableAll();

    }

    private void minimaxBrainTakesTurn(){
        new MinimaxAsyncTask(listener, brain).execute(gameBoard);
    }

    public class TileAdapter extends ArrayAdapter<String> {
        private int resource;
        private boolean clickEnabled;

        public TileAdapter(Context context,int resource,  ArrayList<String> tiles) {
            super(context, resource, tiles);
            this.resource = resource;
            this.clickEnabled = true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }
            String token = gameBoard.get(position);

            ImageView ivGameToken = (ImageView) convertView.findViewById(R.id.ivGameToken);
            setImageViewSource(token, ivGameToken);

            return convertView;

        }

        @Override
        public boolean isEnabled(int position) {
            return clickEnabled;
        }

        private void setImageViewSource(String token, ImageView iv){
            if (token.equals("X")){
                iv.setImageResource(R.drawable.katy);
            } else if (token.equals("O")) {
                iv.setImageResource(R.drawable.taylor);
            }
        }

        public void disableAll(){
            clickEnabled = false;
        }

        public void enableAll(){
            clickEnabled = true;
        }
    }

    public interface MinimaxAsyncListener {
        void onMoveReady(ArrayList result);

        void onThinking();
    }
}
