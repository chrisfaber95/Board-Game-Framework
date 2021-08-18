package DataStructure;

import java.util.Scanner;

public class Main implements Runnable {
    public static void main (String[] args) {
        Main main = new Main(new Reversi(Piece.PIECE2));
        Thread t = new Thread(main);
        t.start();
    }

    private Reversi reversi;

    private Main(Reversi reversi) {
        this.reversi = reversi;
        this.reversi.resetGame();
    }


    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {

            this.reversi.printBoard();

            switch (scan.nextLine()) {
                case "exit":
                    return;

                case "prev":
                    this.reversi.revertMove();
                    break;

                case "next":
                    this.reversi.algorithmMove(4);
                    break;

                case "place":
                    System.out.println("Possible: " + this.reversi.placeMove(this.reversi.getTileByPosition(6, 4)));
                    break;
            }
        }

        //long startTime = System.nanoTime();

        //System.out.println("Score: " + this.reversi.checkCurrentGameStatus());

        //long endTime   = System.nanoTime();
        //long totalTime = endTime - startTime;
        //System.out.println("Time took in microseconds: " + (totalTime/1000) + " nanoseconds: " + (totalTime/1000000));
    }
}
