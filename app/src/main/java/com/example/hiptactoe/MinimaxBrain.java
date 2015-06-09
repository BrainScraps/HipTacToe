package com.example.hiptactoe;

import java.util.ArrayList;
import java.util.Collections;

public class MinimaxBrain {
    private ArrayList<String> tempGameBoard;

    final int BOARD_NOT_FULL = -9999 ;

    public ArrayList<String> minimaxMove(ArrayList<String> gameBoard){
        this.tempGameBoard = new ArrayList<>(gameBoard);
        MinimaxEngine engine = new MinimaxEngine();
        int newScore = engine.miniMax(tempGameBoard);
        int engineChoice = (int)engine.getChoice().get(0);
        String token = (String) engine.getChoice().get(1);

        tempGameBoard.set(engineChoice, token);
        return tempGameBoard;

    }

    public int gameScore(ArrayList board) {
        // 3 horizontal
        if ((board.get(0).equals("O") && board.get(1).equals("O") && board.get(2).equals("O")) ||
                (board.get(3).equals("O") && board.get(4).equals("O") && board.get(5).equals("O")) ||
                (board.get(6).equals("O") && board.get(7).equals("O") && board.get(8).equals("O")) ||
                // 3 vertical
                (board.get(0).equals("O") && board.get(3).equals("O") && board.get(6).equals("O")) ||
                (board.get(1).equals("O") && board.get(4).equals("O") && board.get(7).equals("O")) ||
                (board.get(2).equals("O") && board.get(5).equals("O") && board.get(8).equals("O")) ||
                // 2 diagonal
                (board.get(0).equals("O") && board.get(4).equals("O") && board.get(8).equals("O")) ||
                (board.get(2).equals("O") && board.get(4).equals("O") && board.get(6).equals("O"))) {
            return 1;
        } else if (
                (board.get(0).equals("X") && board.get(1).equals("X") && board.get(2).equals("X")) ||
                        (board.get(3).equals("X") && board.get(4).equals("X") && board.get(5).equals("X")) ||
                        (board.get(6).equals("X") && board.get(7).equals("X") && board.get(8).equals("X")) ||
                        // 3 vertical
                        (board.get(0).equals("X") && board.get(3).equals("X") && board.get(6).equals("X")) ||
                        (board.get(1).equals("X") && board.get(4).equals("X") && board.get(7).equals("X")) ||
                        (board.get(2).equals("X") && board.get(5).equals("X") && board.get(8).equals("X")) ||
                        // 2 diagonal
                        (board.get(0).equals("X") && board.get(4).equals("X") && board.get(8).equals("X")) ||
                        (board.get(2).equals("X") && board.get(4).equals("X") && board.get(6).equals("X"))){
            return -1;
        } else if (emptySpaces(board)){
            return BOARD_NOT_FULL;
        } else {
            //draw game
            return 0;
        }
    }

    private boolean emptySpaces(ArrayList board) {
        return board.indexOf("") != -1;
    }

    private boolean computerTurn(ArrayList board) {
        int exes = Collections.frequency(board, "X");
        int ohs = Collections.frequency(board, "O");
        return !(ohs == exes);
    }

    private ArrayList<Integer> getEmptyPositions(ArrayList board){
        ArrayList<Integer> empties = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            String currentString = (String) board.get(i);
            if (currentString.equals("")) {
                empties.add(i);
            }
        }
        return empties;
    }

    private class MinimaxEngine {
        private ArrayList choice;

        public MinimaxEngine() {

        }

        public ArrayList getChoice(){
            return this.choice;
        }

        public int miniMax(ArrayList<String> board) {
            // gameScore will return -9999 if no one has won AND there are still empty spaces
            int gameResult = gameScore(board);
            if (gameResult != BOARD_NOT_FULL) {
                return gameResult;
            } else {
                boolean computerTurn = computerTurn(board);
                ArrayList scores = new ArrayList<>();
                ArrayList moves = new ArrayList<>();

                ArrayList<Integer> possibleMoves = new ArrayList<>(getEmptyPositions(board));
                for (int emptyPos : possibleMoves){
                    // determine whose turn it is
                    //assumes that the human player is "X" and will always go first
                    String token = computerTurn ? "O" : "X";
                    // construct the board with the move being considered
                    // then add the score of that board to 'scores' by a recursive call to minimax
                    ArrayList tempBoard;
                    tempBoard = new ArrayList<>(board);
                    tempBoard.set(emptyPos, token);
                    scores.add(miniMax(tempBoard));
                    // construct the move that is being considered as a [1, "X"] for example
                    ArrayList move = new ArrayList<>();
                    move.add(emptyPos);
                    move.add(token);
                    moves.add(move);
                }

                if (computerTurn){
                    ArrayList maxScore = new ArrayList<>();
                    //get the maximum score and its index
                    for (int i = 0; i < scores.size() ; i++) {
                        if (maxScore.size() == 0 ){
                            maxScore.add(scores.get(i));
                            maxScore.add(i);
                        } else {
                            if ((int) maxScore.get(0) <= (int) scores.get(i)){
                                maxScore.set(0, scores.get(i));
                                maxScore.set(1, i);
                            }
                        }
                    }
                    // get the best move and store it in the 'choice' class variable
                    ArrayList bestMove = (ArrayList) moves.get((int) maxScore.get(1));
                    choice = new ArrayList<>(bestMove);
                    return (int) scores.get((int) maxScore.get(1));
                } else {
                    ArrayList minScore = new ArrayList<>();
                    for (int i = 0; i < scores.size() ; i++) {
                        if (minScore.size() == 0 ){
                            minScore.add(scores.get(i));
                            minScore.add(i);
                        } else {
                            if ((int) minScore.get(0) >= (int) scores.get(i)){
                                minScore.set(0, scores.get(i));
                                minScore.set(1, i);
                            }
                        }
                    }
                    // get the best move and store it in the 'choice' class variable
                    ArrayList bestMove = (ArrayList) moves.get((int) minScore.get(1));
                    choice = new ArrayList<>(bestMove);
                    return (int) scores.get((int) minScore.get(1));
                }
            }
        }
    }
}
