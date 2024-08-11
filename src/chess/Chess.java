package chess;

import java.io.FileNotFoundException;
import java.util.EventListener;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Chess extends Application implements EventListener {
  public static void main(String[] args) {
    launch(args);
  }
  
  BorderPane pane = new BorderPane();
  
  
  Scene scene;
  
  int tileX = 0;
  int tileY = 0;
  
  int moveCount = 0;
  
  double mouseX;
  double mouseY;
  
  boolean passanting;
  
  boolean gameOver = false;
  boolean wCheck = false;
  boolean bCheck = false;
  
  boolean selected;
  int selectedX = -1;
  int selectedY = -1;
  
  String turn = "white";
  
  Tile selectedTile;
  Tile whiteKing;
  Tile blackKing;
  Tile[][] board = new Tile[8][8];
  
  String[][] virtBoard;
  
  int virtWhiteKingRank;
  int virtBlackKingRank;
  int virtWhiteKingFile = 99;
  int virtBlackKingFile = 99;
  
  boolean[][] checkTiles = new boolean[8][8];
  
  public void start(Stage primaryStage) throws FileNotFoundException {
    this.scene = new Scene(this.pane, 800.0D, 800.0D);
    primaryStage.setTitle("Chess");
    primaryStage.setScene(this.scene);
    primaryStage.show();
    this.scene.setOnMouseClicked(this.mouseHandler);
    boolean light = true;
    int rankCounter = 7;
    for (int i = 0; i < 8; i++) {
      for (int ranks = 0; ranks < 8; ranks++) {
        if (i == 0) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "A", rankCounter, light);
        } else if (i == 1) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "B", rankCounter, light);
        } else if (i == 2) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "C", rankCounter, light);
        } else if (i == 3) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "D", rankCounter, light);
        } else if (i == 4) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "E", rankCounter, light);
        } else if (i == 5) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "F", rankCounter, light);
        } else if (i == 6) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "G", rankCounter, light);
        } else if (i == 7) {
          this.board[i][ranks] = new Tile(this.tileX, this.tileY, "H", rankCounter, light);
        } 
        this.pane.getChildren().add(this.board[i][ranks].getTile());
        this.tileY += 100;
        light = !light;
        rankCounter--;
      } 
      light = !light;
      this.tileY = 0;
      this.tileX += 100;
      rankCounter = 7;
    } 
    for (int x = 0; x < 8; x++) {
      this.board[x][1].addPiece("bP");
      this.board[x][6].addPiece("wP");
    } 
    this.board[0][7].addPiece("wR");
    this.board[1][7].addPiece("wN");
    this.board[2][7].addPiece("wB");
    this.board[3][7].addPiece("wQ");
    this.board[4][7].addPiece("wK");
    this.board[5][7].addPiece("wB");
    this.board[6][7].addPiece("wN");
    this.board[7][7].addPiece("wR");
    this.board[0][0].addPiece("bR");
    this.board[1][0].addPiece("bN");
    this.board[2][0].addPiece("bB");
    this.board[3][0].addPiece("bQ");
    this.board[4][0].addPiece("bK");
    this.board[5][0].addPiece("bB");
    this.board[6][0].addPiece("bN");
    this.board[7][0].addPiece("bR");
    for (int files = 0; files < 8; files++) {
      for (int ranks = 0; ranks < 8; ranks++) {
        if (!this.board[files][ranks].isEmpty()) {
          if (this.board[files][ranks].getPiece().getColor().equalsIgnoreCase("white") && this.board[files][ranks].getPiece().getName().equalsIgnoreCase("king"))
            this.whiteKing = this.board[files][ranks]; 
          if (this.board[files][ranks].getPiece().getColor().equalsIgnoreCase("black") && this.board[files][ranks].getPiece().getName().equalsIgnoreCase("king"))
            this.blackKing = this.board[files][ranks]; 
        } 
      } 
    } 
  }
  
  EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
      public void handle(MouseEvent e) {
        Chess.this.mouseX = e.getX();
        Chess.this.mouseY = e.getY();
        int x;
        for (x = 0; x < 8; x++) {
          for (int y = 0; y < 8; y++) {
            if (Chess.this.mouseX >= Chess.this.board[x][y].getX() && Chess.this.mouseX <= (Chess.this.board[x][y].getX() + 100))
              if (Chess.this.mouseY >= Chess.this.board[x][y].getY() && Chess.this.mouseY <= (Chess.this.board[x][y].getY() + 100))
                if (Chess.this.selected && Chess.this.selectedX == x && Chess.this.selectedY == y) {
                  Chess.this.selectedTile = null;
                  Chess.this.selected = false;
                  Chess.this.selectedX = -1;
                  Chess.this.selectedY = -1;
                  for (int q = 0; q < 8; q++) {
                    for (int v = 0; v < 8; v++) {
                      if (!Chess.this.board[q][v].isEmpty()) {
                        if (Chess.this.board[q][v].getPiece().getName().equalsIgnoreCase("king") && Chess.this.board[q][v].getPiece().getColor().equalsIgnoreCase("white")) {
                          if (Chess.this.turn.equalsIgnoreCase("white"))
                            if (Chess.this.wCheck) {
                              Chess.this.board[q][v].check();
                            } else {
                              Chess.this.board[q][v].reset();
                            }  
                        } else if (Chess.this.board[q][v].getPiece().getName().equalsIgnoreCase("king") && Chess.this.board[q][v].getPiece().getColor().equalsIgnoreCase("black")) {
                          if (Chess.this.turn.equalsIgnoreCase("black"))
                            if (Chess.this.bCheck) {
                              Chess.this.board[q][v].check();
                            } else {
                              Chess.this.board[q][v].reset();
                            }  
                        } else {
                          Chess.this.board[q][v].reset();
                        } 
                      } else {
                        Chess.this.board[q][v].reset();
                      } 
                    } 
                  } 
                } else if (!Chess.this.board[x][y].isEmpty() && Chess.this.board[x][y].getPiece().getColor().equals(Chess.this.turn) && !Chess.this.selected) {
                  Chess.this.selectedTile = Chess.this.board[x][y];
                  Chess.this.selectedTile.select();
                  Chess.this.selectedX = x;
                  Chess.this.selectedY = y;
                  Chess.this.selected = true;
                }   
          } 
        } 
        if (Chess.this.selected) {
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("pawn"))
            Chess.this.movePawn(Chess.this.selectedTile); 
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("bishop"))
            Chess.this.moveBishop(Chess.this.selectedTile); 
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("knight"))
            Chess.this.moveKnight(Chess.this.selectedTile); 
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("rook"))
            Chess.this.moveRook(Chess.this.selectedTile); 
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("queen")) {
            Chess.this.moveBishop(Chess.this.selectedTile);
            Chess.this.moveRook(Chess.this.selectedTile);
          } 
          if (Chess.this.selectedTile.getPiece().getName().equalsIgnoreCase("king"))
            Chess.this.moveKing(Chess.this.selectedTile); 
        } 
        if (Chess.this.selected && !Chess.this.gameOver)
          for (x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
              if (Chess.this.mouseX >= Chess.this.board[x][y].getX() && Chess.this.mouseX <= (Chess.this.board[x][y].getX() + 100))
                if (Chess.this.mouseY >= Chess.this.board[x][y].getY() && Chess.this.mouseY <= (Chess.this.board[x][y].getY() + 100)) {
                  if (!Chess.this.board[x][y].isEmpty())
                    if (Chess.this.selectedTile.getPiece().getID().substring(1).equals("K") && Chess.this.board[x][y].getPiece().getID().substring(1).equals("R"))
                      if (Chess.this.selectedTile.getPiece().isFirstMove() && Chess.this.board[x][y].getPiece().isFirstMove())
                        if (Chess.this.selectedTile.getPiece().getID().substring(1).equals("K"))
                          if (Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) < Chess.this.fileToIndex(Chess.this.board[x][y].getFile())) {
                            if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.selectedTile))
                              if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) + 1][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())]))
                                if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) + 2][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())])) {
                                  Chess.this.selectedTile.movePiece(Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) + 2][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())]);
                                  Chess.this.selectedTile = null;
                                  Chess.this.selected = false;
                                  Chess.this.board[x][y].movePiece(Chess.this.board[Chess.this.fileToIndex(Chess.this.board[x][y].getFile()) - 2][Chess.this.rankToIndex(Chess.this.board[x][y].getRank())]);
                                  if (Chess.this.turn.equalsIgnoreCase("white")) {
                                    Chess.this.turn = "black";
                                  } else {
                                    Chess.this.turn = "white";
                                  } 
                                  for (int i = 0; i < 8; i++) {
                                    for (int v = 0; v < 8; v++)
                                      Chess.this.board[i][v].reset(); 
                                  } 
                                }   
                          } else if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.selectedTile)) {
                            if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) - 1][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())]))
                              if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) - 2][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())])) {
                                Chess.this.selectedTile.movePiece(Chess.this.board[Chess.this.fileToIndex(Chess.this.selectedTile.getFile()) - 2][Chess.this.rankToIndex(Chess.this.selectedTile.getRank())]);
                                Chess.this.selectedTile = null;
                                Chess.this.selected = false;
                                Chess.this.board[x][y].movePiece(Chess.this.board[Chess.this.fileToIndex(Chess.this.board[x][y].getFile()) + 3][Chess.this.rankToIndex(Chess.this.board[x][y].getRank())]);
                                if (Chess.this.turn.equalsIgnoreCase("white")) {
                                  Chess.this.turn = "black";
                                } else {
                                  Chess.this.turn = "white";
                                } 
                                for (int i = 0; i < 8; i++) {
                                  for (int v = 0; v < 8; v++)
                                    Chess.this.board[i][v].reset(); 
                                } 
                              }  
                          }     
                  if (Chess.this.board[x][y].isMoveable())
                    if (Chess.this.isLegal(Chess.this.selectedTile, Chess.this.board[x][y])) {
                      if (Chess.this.board[x][y].isEmpty() && !Chess.this.selectedTile.getPiece().getID().substring(1).equals("P")) {
                        Chess.this.moveCount++;
                      } else {
                        Chess.this.moveCount = 0;
                      } 
                      Chess.this.selectedTile.movePiece(Chess.this.board[x][y]);
                      if (Chess.this.passanting) {
                        Chess.this.passanting = false;
                        if (y < 7)
                          if (!Chess.this.board[x][y + 1].isEmpty())
                            if (Chess.this.board[x][y + 1].getPiece().getID().equalsIgnoreCase("bP"))
                              if (Chess.this.board[x][y].getPiece().getID().equalsIgnoreCase("wP"))
                                if (Chess.this.board[x][y + 1].getPiece().passantAble())
                                  Chess.this.board[x][y + 1].removePiece();     
                        if (y > 0)
                          if (!Chess.this.board[x][y - 1].isEmpty())
                            if (Chess.this.board[x][y - 1].getPiece().getID().equalsIgnoreCase("wP"))
                              if (Chess.this.board[x][y].getPiece().getID().equalsIgnoreCase("bP"))
                                if (Chess.this.board[x][y - 1].getPiece().passantAble())
                                  Chess.this.board[x][y - 1].removePiece();     
                      } 
                      if (Chess.this.selectedTile.getRank() + 2 == Chess.this.board[x][y].getRank() && Chess.this.selectedTile.getPiece().getID().equals("wP")) {
                        Chess.this.board[x][y].getPiece().enPassant();
                        Chess.this.passanting = true;
                      } 
                      if (Chess.this.selectedTile.getRank() - 2 == Chess.this.board[x][y].getRank() && Chess.this.selectedTile.getPiece().getID().equals("bP")) {
                        Chess.this.board[x][y].getPiece().enPassant();
                        Chess.this.passanting = true;
                      } 
                      Chess.this.selectedTile = null;
                      Chess.this.selected = false;
                      boolean promoted = false;
                      int i;
                      for (i = 0; i < 8; i++) {
                        for (int v = 0; v < 8; v++) {
                          if (!Chess.this.board[i][v].isEmpty()) {
                            if (Chess.this.board[i][v].getPiece().getID().equals("wP") && v == 0)
                              while (!promoted) {
                                Object[] options = { "Rook", "Knight", "Bishop", "Queen" };
                                int n = JOptionPane.showOptionDialog(new JFrame(), "What piece should your pawn be promoted to?", "Pawn Promotion", 0, 3, null, options, options[0]);
                                if (n == 0) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("wR");
                                  promoted = true;
                                } 
                                if (n == 1) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("wN");
                                  promoted = true;
                                } 
                                if (n == 2) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("wB");
                                  promoted = true;
                                } 
                                if (n == 3) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("wQ");
                                  promoted = true;
                                } 
                              }  
                            if (Chess.this.board[i][v].getPiece().getID().equals("bP") && v == 7)
                              while (!promoted) {
                                Object[] options = { "Rook", "Knight", "Bishop", "Queen" };
                                int n = JOptionPane.showOptionDialog(new JFrame(), "What piece should your pawn be promoted to?", "Pawn Promotion", 0, 3, null, options, options[0]);
                                if (n == 0) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("bR");
                                  promoted = true;
                                } 
                                if (n == 1) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("bN");
                                  promoted = true;
                                } 
                                if (n == 2) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("bB");
                                  promoted = true;
                                } 
                                if (n == 3) {
                                  Chess.this.board[i][v].removePiece();
                                  Chess.this.board[i][v].addPiece("bQ");
                                  promoted = true;
                                } 
                              }  
                          } 
                        } 
                      } 
                      if (Chess.this.board[x][y].getPiece().getName().equalsIgnoreCase("king"))
                        if (Chess.this.board[x][y].getPiece().getColor().equals("white")) {
                          Chess.this.whiteKing = Chess.this.board[x][y];
                        } else {
                          Chess.this.blackKing = Chess.this.board[x][y];
                        }  
                      for (i = 0; i < 8; i++) {
                        for (int v = 0; v < 8; v++)
                          Chess.this.board[i][v].reset(); 
                      } 
                      if (Chess.this.turn.equalsIgnoreCase("white")) {
                        Chess.this.turn = "black";
                      } else {
                        Chess.this.turn = "white";
                      } 
                      if (!Chess.this.isLegal(Chess.this.board[x][y], Chess.this.board[x][y])) {
                        if (Chess.this.turn.equalsIgnoreCase("white")) {
                          Chess.this.wCheck = true;
                        } else {
                          Chess.this.bCheck = true;
                        } 
                      } else if (Chess.this.turn.equalsIgnoreCase("white")) {
                        Chess.this.wCheck = false;
                      } else {
                        Chess.this.bCheck = false;
                      } 
                    }  
                  for (int q = 0; q < 8; q++) {
                    for (int v = 0; v < 8; v++) {
                      if (!Chess.this.board[q][v].isEmpty()) {
                        if (Chess.this.board[q][v].getPiece().getName().equalsIgnoreCase("king") && Chess.this.board[q][v].getPiece().getColor().equalsIgnoreCase("white"))
                          if (Chess.this.turn.equalsIgnoreCase("white"))
                            if (Chess.this.wCheck)
                              Chess.this.board[q][v].check();   
                        if (Chess.this.board[q][v].getPiece().getName().equalsIgnoreCase("king") && Chess.this.board[q][v].getPiece().getColor().equalsIgnoreCase("black"))
                          if (Chess.this.turn.equalsIgnoreCase("black"))
                            if (Chess.this.bCheck)
                              Chess.this.board[q][v].check();   
                      } 
                    } 
                  } 
                  if (Chess.this.moveCount >= 50) {
                    Chess.this.gameOver = true;
                    JOptionPane.showMessageDialog(new JFrame(), "Draw by 50-Move Rule", "Game Over", 2);
                  } 
                  if (!Chess.this.legalMoves("white") && Chess.this.turn.equals("white"))
                    if (!Chess.this.wCheck) {
                      System.out.println("White is in Stalemate");
                      Chess.this.gameOver = true;
                      JOptionPane.showMessageDialog(new JFrame(), "Draw by Stalemate", "Game Over", 2);
                    } else {
                      Chess.this.gameOver = true;
                      JOptionPane.showMessageDialog(new JFrame(), "Black Wins by Checkmate", "Game Over", 2);
                    }  
                  if (!Chess.this.legalMoves("black") && Chess.this.turn.equals("black"))
                    if (!Chess.this.bCheck) {
                      System.out.println("Black is in Stalemate");
                      Chess.this.gameOver = true;
                      JOptionPane.showMessageDialog(new JFrame(), "Draw by Stalemate", "Game Over", 2);
                    } else {
                      Chess.this.gameOver = true;
                      JOptionPane.showMessageDialog(new JFrame(), "White Wins by Checkmate", "Game Over", 2);
                    }  
                  if (Chess.this.countMaterial()[0].substring(0, 1).equals("0"))
                    if (Chess.this.countMaterial()[1].substring(0, 1).equals("0"))
                      if (Chess.this.countMaterial()[4].substring(0, 1).equals("0"))
                        if (Chess.this.countMaterial()[2].substring(0, 1).equals("1") && Chess.this.countMaterial()[3].substring(0, 1).equals("0")) {
                          Chess.this.gameOver = true;
                          JOptionPane.showMessageDialog(new JFrame(), "Draw by Insufficient Material", "Game Over", 2);
                        } else if (Chess.this.countMaterial()[2].substring(0, 1).equals("0") && Chess.this.countMaterial()[3].substring(0, 1).equals("1")) {
                          Chess.this.gameOver = true;
                          JOptionPane.showMessageDialog(new JFrame(), "Draw by Insufficient Material", "Game Over", 2);
                        } else if (Chess.this.countMaterial()[2].substring(0, 1).equals("0") && Chess.this.countMaterial()[3].substring(0, 1).equals("0")) {
                          Chess.this.gameOver = true;
                          JOptionPane.showMessageDialog(new JFrame(), "Draw by Insufficient Material", "Game Over", 2);
                        }    
                }  
            } 
          }  
      }
    };
  
  public void movePawn(Tile tile) {
    if (tile.getPiece().getColor().equals("white")) {
      if (tile.getPiece().isFirstMove()) {
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 2].isEmpty())
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 2].potentialMove(); 
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].isEmpty())
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].potentialMove(); 
      } else if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].potentialMove();
      } 
      if (fileToIndex(tile.getFile()) - 1 >= 0 && rankToIndex(tile.getRank()) - 1 >= 0)
        if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase("black"))
            this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) + 1 < this.board.length && rankToIndex(tile.getRank()) - 1 >= 0)
        if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase("black"))
            this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) > 0)
        if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].getPiece().passantAble())
            this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) < 7)
        if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].getPiece().passantAble())
            this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].potentialCapture();   
    } else if (tile.getPiece().getColor().equals("black")) {
      if (tile.getPiece().isFirstMove()) {
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 2].isEmpty())
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 2].potentialMove(); 
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].isEmpty())
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].potentialMove(); 
      } else if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].potentialMove();
      } 
      if (fileToIndex(tile.getFile()) - 1 >= 0 && rankToIndex(tile.getRank()) + 1 < this.board.length)
        if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase("white"))
            this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) + 1 <= 7 && rankToIndex(tile.getRank()) + 1 < this.board.length)
        if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase("white"))
            this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) > 0)
        if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].getPiece().passantAble())
            this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].potentialCapture();   
      if (fileToIndex(tile.getFile()) < 7)
        if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].isEmpty())
          if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].getPiece().passantAble())
            this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].potentialCapture();   
    } 
  }
  
  public void moveBishop(Tile tile) {
    int count = 1;
    boolean move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) + count < this.board.length && rankToIndex(tile.getRank()) + count < this.board.length) {
        if (this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) + count].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) + count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) + count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) + count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) - count >= 0 && rankToIndex(tile.getRank()) + count < this.board.length) {
        if (this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) + count].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) + count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) + count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) + count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) - count >= 0 && rankToIndex(tile.getRank()) - count >= 0) {
        if (this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) - count].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) - count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) - count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank()) - count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) + count < this.board.length && rankToIndex(tile.getRank()) - count >= 0) {
        if (this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) - count].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) - count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) - count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank()) - count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
  }
  
  public void moveRook(Tile tile) {
    int count = 1;
    boolean move = true;
    while (move) {
      if (rankToIndex(tile.getRank()) - count >= 0) {
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - count].isEmpty()) {
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (rankToIndex(tile.getRank()) + count < this.board.length) {
        if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + count].isEmpty()) {
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + count].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + count].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + count].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) - count >= 0) {
        if (this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank())].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank())].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank())].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) - count][rankToIndex(tile.getRank())].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
    count = 1;
    move = true;
    while (move) {
      if (fileToIndex(tile.getFile()) + count < this.board.length) {
        if (this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank())].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank())].potentialMove();
          count++;
          continue;
        } 
        if (!this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank())].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) + count][rankToIndex(tile.getRank())].potentialCapture();
          move = false;
          continue;
        } 
        move = false;
        continue;
      } 
      move = false;
    } 
  }
  
  public void moveKnight(Tile tile) {
    if (fileToIndex(tile.getFile()) + 1 < this.board.length && rankToIndex(tile.getRank()) + 2 < this.board.length)
      if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 2].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 2].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 2].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 2].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) - 1 >= 0 && rankToIndex(tile.getRank()) + 2 < this.board.length)
      if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 2].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 2].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 2].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 2].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) - 1 >= 0 && rankToIndex(tile.getRank()) - 2 >= 0)
      if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 2].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 2].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 2].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 2].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) + 1 < this.board.length && rankToIndex(tile.getRank()) - 2 >= 0)
      if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 2].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 2].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 2].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 2].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) - 2 >= 0 && rankToIndex(tile.getRank()) - 1 >= 0)
      if (this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) - 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) - 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) - 1].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) - 2 >= 0 && rankToIndex(tile.getRank()) + 1 < this.board.length)
      if (this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) + 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) + 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) - 2][rankToIndex(tile.getRank()) + 1].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) + 2 < this.board.length && rankToIndex(tile.getRank()) + 1 < this.board.length)
      if (this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) + 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) + 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) + 1].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) + 2 < this.board.length && rankToIndex(tile.getRank()) - 1 >= 0)
      if (this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) - 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) - 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) + 2][rankToIndex(tile.getRank()) - 1].potentialCapture();
      }  
  }
  
  public void moveKing(Tile tile) {
    if (rankToIndex(tile.getRank()) - 1 >= 0) {
      if (fileToIndex(tile.getFile()) - 1 >= 0)
        if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].potentialMove();
        } else if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) - 1].potentialCapture();
        }  
      if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) - 1].potentialCapture();
      } 
      if (fileToIndex(tile.getFile()) + 1 < this.board.length)
        if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].potentialMove();
        } else if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) - 1].potentialCapture();
        }  
    } 
    if (rankToIndex(tile.getRank()) + 1 < this.board.length) {
      if (fileToIndex(tile.getFile()) - 1 >= 0)
        if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].potentialMove();
        } else if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank()) + 1].potentialCapture();
        }  
      if (this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].isEmpty()) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile())][rankToIndex(tile.getRank()) + 1].potentialCapture();
      } 
      if (fileToIndex(tile.getFile()) + 1 < this.board.length)
        if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].isEmpty()) {
          this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].potentialMove();
        } else if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
          this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank()) + 1].potentialCapture();
        }  
    } 
    if (fileToIndex(tile.getFile()) + 1 < this.board.length)
      if (this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) + 1][rankToIndex(tile.getRank())].potentialCapture();
      }  
    if (fileToIndex(tile.getFile()) - 1 >= 0)
      if (this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].isEmpty()) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].potentialMove();
      } else if (!this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].getPiece().getColor().equalsIgnoreCase(tile.getPiece().getColor())) {
        this.board[fileToIndex(tile.getFile()) - 1][rankToIndex(tile.getRank())].potentialCapture();
      }  
  }
  
  public boolean legalMoves(String turn) {
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if (!this.board[x][y].isEmpty())
          if (this.board[x][y].getPiece().getColor().equals(turn)) {
            if (this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("P")) {
              if (turn.equals("white")) {
                if (y > 0) {
                  if (x < 7)
                    if (!this.board[x + 1][y - 1].isEmpty())
                      if (isLegal(this.board[x][y], this.board[x + 1][y - 1]) && this.board[x + 1][y - 1].getPiece().getColor().equals("black"))
                        return true;   
                  if (x > 0)
                    if (!this.board[x - 1][y - 1].isEmpty())
                      if (isLegal(this.board[x][y], this.board[x - 1][y - 1]) && this.board[x - 1][y - 1].getPiece().getColor().equals("black"))
                        return true;   
                } 
                if (isLegal(this.board[x][y], this.board[x][y - 1]) && this.board[x][y - 1].isEmpty())
                  return true; 
                if (isLegal(this.board[x][y], this.board[x][y - 2]) && this.board[x][y].getPiece().isFirstMove() && this.board[x][y - 1].isEmpty() && this.board[x][y - 2].isEmpty())
                  return true; 
              } 
              if (turn.equals("black")) {
                if (y < 7) {
                  if (x < 7)
                    if (!this.board[x + 1][y + 1].isEmpty())
                      if (isLegal(this.board[x][y], this.board[x + 1][y + 1]) && this.board[x + 1][y + 1].getPiece().getColor().equals("white"))
                        return true;   
                  if (x > 0)
                    if (!this.board[x - 1][y + 1].isEmpty())
                      if (isLegal(this.board[x][y], this.board[x - 1][y + 1]) && this.board[x - 1][y + 1].getPiece().getColor().equals("white"))
                        return true;   
                } 
                if (isLegal(this.board[x][y], this.board[x][y + 1]) && this.board[x][y + 1].isEmpty())
                  return true; 
                if (isLegal(this.board[x][y], this.board[x][y + 2]) && this.board[x][y].getPiece().isFirstMove() && this.board[x][y + 1].isEmpty() && this.board[x][y + 2].isEmpty())
                  return true; 
              } 
            } 
            if (this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("R") || this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("Q")) {
              if (y < 7)
                for (int c = 1; y + c < 8; c++) {
                  if (this.board[x][y + c].isEmpty()) {
                    if (isLegal(this.board[x][y], this.board[x][y + c]))
                      return true; 
                  } else {
                    if (!this.board[x][y + c].getPiece().getColor().equalsIgnoreCase(turn))
                      if (isLegal(this.board[x][y], this.board[x][y + c]))
                        return true;  
                    break;
                  } 
                }  
              if (y > 0)
                for (int c = 1; y - c >= 0; c++) {
                  if (this.board[x][y - c].isEmpty()) {
                    if (isLegal(this.board[x][y], this.board[x][y - c]))
                      return true; 
                  } else {
                    if (!this.board[x][y - c].getPiece().getColor().equalsIgnoreCase(turn))
                      if (isLegal(this.board[x][y], this.board[x][y - c]))
                        return true;  
                    break;
                  } 
                }  
              if (x < 7)
                for (int c = 1; x + c < 8; c++) {
                  if (this.board[x + c][y].isEmpty()) {
                    if (isLegal(this.board[x][y], this.board[x + c][y]))
                      return true; 
                  } else {
                    if (!this.board[x + c][y].getPiece().getColor().equalsIgnoreCase(turn))
                      if (isLegal(this.board[x][y], this.board[x + c][y]))
                        return true;  
                    break;
                  } 
                }  
              if (x > 0)
                for (int c = 1; x - c >= 0; c++) {
                  if (this.board[x - c][y].isEmpty()) {
                    if (isLegal(this.board[x][y], this.board[x - c][y]))
                      return true; 
                  } else {
                    if (!this.board[x - c][y].getPiece().getColor().equalsIgnoreCase(turn))
                      if (isLegal(this.board[x][y], this.board[x - c][y]))
                        return true;  
                    break;
                  } 
                }  
            } 
            if (this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("B") || this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("Q")) {
              int counter = 1;
              counter = 1;
              while (x - counter >= 0 && y - counter >= 0) {
                if (this.board[x - counter][y - counter].isEmpty()) {
                  if (isLegal(this.board[x][y], this.board[x - counter][y - counter]))
                    return true; 
                } else {
                  if (!this.board[x - counter][y - counter].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - counter][y - counter]))
                      return true;  
                  break;
                } 
                counter++;
              } 
              counter = 1;
              while (x + counter < 8 && y + counter < 8) {
                if (this.board[x + counter][y + counter].isEmpty()) {
                  if (isLegal(this.board[x][y], this.board[x + counter][y + counter]))
                    return true; 
                } else {
                  if (!this.board[x + counter][y + counter].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + counter][y + counter]))
                      return true;  
                  break;
                } 
                counter++;
              } 
              counter = 1;
              while (x + counter < 8 && y - counter >= 0) {
                if (this.board[x + counter][y - counter].isEmpty()) {
                  if (isLegal(this.board[x][y], this.board[x + counter][y - counter]))
                    return true; 
                } else {
                  if (!this.board[x + counter][y - counter].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + counter][y - counter]))
                      return true;  
                  break;
                } 
                counter++;
              } 
              counter = 1;
              while (x - counter >= 0 && y + counter < 8) {
                if (this.board[x - counter][y + counter].isEmpty()) {
                  if (isLegal(this.board[x][y], this.board[x - counter][y + counter]))
                    return true; 
                } else {
                  if (!this.board[x - counter][y + counter].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - counter][y + counter]))
                      return true;  
                  break;
                } 
                counter++;
              } 
            } 
            if (this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("N")) {
              if (x - 1 >= 0 && y - 2 >= 0)
                if (!this.board[x - 1][y - 2].isEmpty()) {
                  if (!this.board[x - 1][y - 2].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - 1][y - 2]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x - 1][y - 2])) {
                  return true;
                }  
              if (x + 1 < 8 && y - 2 >= 0)
                if (!this.board[x + 1][y - 2].isEmpty()) {
                  if (!this.board[x + 1][y - 2].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + 1][y - 2]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x + 1][y - 2])) {
                  return true;
                }  
              if (x - 1 >= 0 && y + 2 < 8)
                if (!this.board[x - 1][y + 2].isEmpty()) {
                  if (!this.board[x - 1][y + 2].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - 1][y + 2]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x - 1][y + 2])) {
                  return true;
                }  
              if (x + 1 < 8 && y + 2 < 8)
                if (!this.board[x + 1][y + 2].isEmpty()) {
                  if (!this.board[x + 1][y + 2].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + 1][y + 2]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x + 1][y + 2])) {
                  return true;
                }  
              if (x - 2 >= 0 && y + 1 < 8)
                if (!this.board[x - 2][y + 1].isEmpty()) {
                  if (!this.board[x - 2][y + 1].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - 2][y + 1]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x - 2][y + 1])) {
                  return true;
                }  
              if (x - 2 >= 0 && y - 1 >= 0)
                if (!this.board[x - 2][y - 1].isEmpty()) {
                  if (!this.board[x - 2][y - 1].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x - 2][y - 1]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x - 2][y - 1])) {
                  return true;
                }  
              if (x + 2 < 8 && y + 1 < 8)
                if (!this.board[x + 2][y + 1].isEmpty()) {
                  if (!this.board[x + 2][y + 1].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + 2][y + 1]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x + 2][y + 1])) {
                  return true;
                }  
              if (x + 2 < 8 && y - 1 >= 0)
                if (!this.board[x + 2][y - 1].isEmpty()) {
                  if (!this.board[x + 2][y - 1].getPiece().getColor().equalsIgnoreCase(turn))
                    if (isLegal(this.board[x][y], this.board[x + 2][y - 1]))
                      return true;  
                } else if (isLegal(this.board[x][y], this.board[x + 2][y - 1])) {
                  return true;
                }  
            } 
            if (this.board[x][y].getPiece().getID().substring(1).equalsIgnoreCase("K")) {
              if (y > 0) {
                if (x < 7)
                  if (this.board[x + 1][y - 1].isEmpty() || !this.board[x + 1][y - 1].getPiece().getColor().equals(turn))
                    if (isLegal(this.board[x][y], this.board[x + 1][y - 1]))
                      return true;   
                if (this.board[x][y - 1].isEmpty() || !this.board[x][y - 1].getPiece().getColor().equals(turn))
                  if (isLegal(this.board[x][y], this.board[x][y - 1]))
                    return true;  
                if (x > 0)
                  if (this.board[x - 1][y - 1].isEmpty() || !this.board[x - 1][y - 1].getPiece().getColor().equals(turn))
                    if (isLegal(this.board[x][y], this.board[x - 1][y - 1]))
                      return true;   
              } 
              if (y < 7) {
                if (x < 7)
                  if (this.board[x + 1][y + 1].isEmpty() || !this.board[x + 1][y + 1].getPiece().getColor().equals(turn))
                    if (isLegal(this.board[x][y], this.board[x + 1][y + 1]))
                      return true;   
                if (this.board[x][y + 1].isEmpty() || !this.board[x][y + 1].getPiece().getColor().equals(turn))
                  if (isLegal(this.board[x][y], this.board[x][y + 1]))
                    return true;  
                if (x > 0)
                  if (this.board[x - 1][y + 1].isEmpty() || !this.board[x - 1][y + 1].getPiece().getColor().equals(turn))
                    if (isLegal(this.board[x][y], this.board[x - 1][y + 1]))
                      return true;   
              } 
              if (x > 0)
                if (this.board[x - 1][y].isEmpty() || !this.board[x - 1][y].getPiece().getColor().equals(turn))
                  if (isLegal(this.board[x][y], this.board[x - 1][y]))
                    return true;   
              if (x < 7)
                if (this.board[x + 1][y].isEmpty() || !this.board[x + 1][y].getPiece().getColor().equals(turn))
                  if (isLegal(this.board[x][y], this.board[x + 1][y]))
                    return true;   
            } 
          }  
      } 
    } 
    return false;
  }
  
  public boolean isLegal(Tile oldTile, Tile newTile) {
    virtBoard(oldTile, newTile);
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if (this.virtBoard[x][y].equals("wK")) {
          this.virtWhiteKingFile = x;
          this.virtWhiteKingRank = y;
        } else if (this.virtBoard[x][y].equals("bK")) {
          this.virtBlackKingFile = x;
          this.virtBlackKingRank = y;
        } 
      } 
    } 
    boolean canCapture = true;
    boolean kingFound = false;
    int counter = 0;
    for (int i = 0; i < 8; i++) {
      for (int y = 0; y < 8; y++) {
        if (!this.virtBoard[i][y].equals("[]"))
          if (this.turn.equalsIgnoreCase("white")) {
            if (this.virtBoard[i][y].equals("bP"))
              if (y < 7) {
                if (i > 0)
                  if (this.virtBoard[i - 1][y + 1].equals("wK"))
                    return false;  
                if (i < 7)
                  if (this.virtBoard[i + 1][y + 1].equals("wK"))
                    return false;  
              }  
            if (this.virtBoard[i][y].equals("bR") || this.virtBoard[i][y].equals("bQ")) {
              canCapture = true;
              if (this.virtWhiteKingFile < i && this.virtWhiteKingRank == y) {
                for (int c = 1; c < i - this.virtWhiteKingFile; c++) {
                  if (this.virtBoard[i - c][y] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtWhiteKingFile > i && this.virtWhiteKingRank == y) {
                for (int c = 1; c < this.virtWhiteKingFile - i; c++) {
                  if (this.virtBoard[i + c][y] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtWhiteKingRank < y && this.virtWhiteKingFile == i) {
                for (int c = 1; c < y - this.virtWhiteKingRank; c++) {
                  if (this.virtBoard[i][y + -c] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtWhiteKingRank > y && this.virtWhiteKingFile == i) {
                for (int c = 1; c < this.virtWhiteKingRank - y; c++) {
                  if (this.virtBoard[i][y + c] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
            } 
            if (this.virtBoard[i][y].equals("bB") || this.virtBoard[i][y].equals("bQ")) {
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i - counter >= this.virtWhiteKingFile && y - counter >= this.virtWhiteKingRank) {
                if (this.virtBoard[i - counter][y - counter].equals("wK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i - counter][y - counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i + counter <= this.virtWhiteKingFile && y - counter >= this.virtWhiteKingRank) {
                if (this.virtBoard[i + counter][y - counter].equals("wK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i + counter][y - counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i - counter >= this.virtWhiteKingFile && y + counter <= this.virtWhiteKingRank) {
                if (this.virtBoard[i - counter][y + counter].equals("wK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i - counter][y + counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i + counter <= this.virtWhiteKingFile && y + counter <= this.virtWhiteKingRank) {
                if (this.virtBoard[i + counter][y + counter].equals("wK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i + counter][y + counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
            } 
            if (this.virtBoard[i][y].equals("bN")) {
              if (y - 2 >= 0) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y - 2].equals("wK"))
                    return false;  
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y - 2].equals("wK"))
                    return false;  
              } 
              if (y + 2 <= 7) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y + 2].equals("wK"))
                    return false;  
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y + 2].equals("wK"))
                    return false;  
              } 
              if (i - 2 >= 0) {
                if (y - 1 >= 0)
                  if (this.virtBoard[i - 2][y - 1].equals("wK"))
                    return false;  
                if (y + 1 <= 7)
                  if (this.virtBoard[i - 2][y + 1].equals("wK"))
                    return false;  
              } 
              if (i + 2 <= 7) {
                if (y - 1 >= 0)
                  if (this.virtBoard[i + 2][y - 1].equals("wK"))
                    return false;  
                if (y + 1 <= 7)
                  if (this.virtBoard[i + 2][y + 1].equals("wK"))
                    return false;  
              } 
            } 
            if (this.virtBoard[i][y].equals("bK")) {
              if (y - 1 >= 0) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y - 1].equals("wK"))
                    return false;  
                if (this.virtBoard[i][y - 1].equals("wK"))
                  return false; 
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y - 1].equals("wK"))
                    return false;  
              } 
              if (y + 1 <= 7) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y + 1].equals("wK"))
                    return false;  
                if (this.virtBoard[i][y + 1].equals("wK"))
                  return false; 
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y + 1].equals("wK"))
                    return false;  
              } 
              if (i - 1 >= 0)
                if (this.virtBoard[i - 1][y].equals("wK"))
                  return false;  
              if (i + 1 <= 7)
                if (this.virtBoard[i + 1][y].equals("wK"))
                  return false;  
            } 
          } else {
            if (this.virtBoard[i][y].equals("wP"))
              if (y > 0) {
                if (i > 0)
                  if (this.virtBoard[i - 1][y - 1].equals("bK"))
                    return false;  
                if (i < 7)
                  if (this.virtBoard[i + 1][y - 1].equals("bK"))
                    return false;  
              }  
            if (this.virtBoard[i][y].equals("wR") || this.virtBoard[i][y].equals("wQ")) {
              canCapture = true;
              if (this.virtBlackKingFile < i && this.virtBlackKingRank == y) {
                for (int c = 1; c < i - this.virtBlackKingFile; c++) {
                  if (this.virtBoard[i - c][y] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtBlackKingFile > i && this.virtBlackKingRank == y) {
                for (int c = 1; c < this.virtBlackKingFile - i; c++) {
                  if (this.virtBoard[i + c][y] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtBlackKingRank < y && this.virtBlackKingFile == i) {
                for (int c = 1; c < y - this.virtBlackKingRank; c++) {
                  if (this.virtBoard[i][y - c] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
              canCapture = true;
              if (this.virtBlackKingRank > y && this.virtBlackKingFile == i) {
                for (int c = 1; c < this.virtBlackKingRank - y; c++) {
                  if (this.virtBoard[i][y + c] != "[]")
                    canCapture = false; 
                } 
                if (canCapture)
                  return false; 
              } 
            } 
            if (this.virtBoard[i][y].equals("wB") || this.virtBoard[i][y].equals("wQ")) {
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i - counter >= this.virtBlackKingFile && y - counter >= this.virtBlackKingRank) {
                if (this.virtBoard[i - counter][y - counter].equals("bK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i - counter][y - counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i + counter <= this.virtBlackKingFile && y - counter >= this.virtBlackKingRank) {
                if (this.virtBoard[i + counter][y - counter].equals("bK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i + counter][y - counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i - counter >= this.virtBlackKingFile && y + counter <= this.virtBlackKingRank) {
                if (this.virtBoard[i - counter][y + counter].equals("bK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i - counter][y + counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
              canCapture = true;
              kingFound = false;
              counter = 1;
              while (i + counter <= this.virtBlackKingFile && y + counter <= this.virtBlackKingRank) {
                if (this.virtBoard[i + counter][y + counter].equals("bK") && canCapture) {
                  kingFound = true;
                  break;
                } 
                if (this.virtBoard[i + counter][y + counter] != "[]")
                  canCapture = false; 
                counter++;
              } 
              if (kingFound)
                return false; 
            } 
            if (this.virtBoard[i][y].equals("wN")) {
              if (y - 2 >= 0) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y - 2].equals("bK"))
                    return false;  
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y - 2].equals("bK"))
                    return false;  
              } 
              if (y + 2 <= 7) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y + 2].equals("bK"))
                    return false;  
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y + 2].equals("bK"))
                    return false;  
              } 
              if (i - 2 >= 0) {
                if (y - 1 >= 0)
                  if (this.virtBoard[i - 2][y - 1].equals("bK"))
                    return false;  
                if (y + 1 <= 7)
                  if (this.virtBoard[i - 2][y + 1].equals("bK"))
                    return false;  
              } 
              if (i + 2 <= 7) {
                if (y - 1 >= 0)
                  if (this.virtBoard[i + 2][y - 1].equals("bK"))
                    return false;  
                if (y + 1 <= 7)
                  if (this.virtBoard[i + 2][y + 1].equals("bK"))
                    return false;  
              } 
            } 
            if (this.virtBoard[i][y].equals("wK")) {
              if (y - 1 >= 0) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y - 1].equals("bK"))
                    return false;  
                if (this.virtBoard[i][y - 1].equals("bK"))
                  return false; 
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y - 1].equals("bK"))
                    return false;  
              } 
              if (y + 1 <= 7) {
                if (i - 1 >= 0)
                  if (this.virtBoard[i - 1][y + 1].equals("bK"))
                    return false;  
                if (this.virtBoard[i][y + 1].equals("bK"))
                  return false; 
                if (i + 1 <= 7)
                  if (this.virtBoard[i + 1][y + 1].equals("bK"))
                    return false;  
              } 
              if (i - 1 >= 0)
                if (this.virtBoard[i - 1][y].equals("bK"))
                  return false;  
              if (i + 1 <= 7)
                if (this.virtBoard[i + 1][y].equals("bK"))
                  return false;  
            } 
          }  
      } 
    } 
    return true;
  }
  
  public void virtBoard(Tile oldTile, Tile newTile) {
    this.virtBoard = new String[8][8];
    if (!oldTile.getFile().equals(newTile.getFile()) || oldTile.getRank() != newTile.getRank()) {
      for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
          if (oldTile.getFile().equals(this.board[x][y].getFile()) && oldTile.getRank() == this.board[x][y].getRank()) {
            this.virtBoard[x][y] = "[]";
          } else if (newTile.getFile().equals(this.board[x][y].getFile()) && newTile.getRank() == this.board[x][y].getRank()) {
            this.virtBoard[x][y] = oldTile.getPiece().getID();
          } else if (!this.board[x][y].isEmpty()) {
            this.virtBoard[x][y] = this.board[x][y].getPiece().getID();
          } else {
            this.virtBoard[x][y] = "[]";
          } 
        } 
      } 
    } else {
      for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
          if (!this.board[x][y].isEmpty()) {
            this.virtBoard[x][y] = this.board[x][y].getPiece().getID();
          } else {
            this.virtBoard[x][y] = "[]";
          } 
        } 
      } 
    } 
  }
  
  public String[] countMaterial() {
    String[] material = new String[5];
    int pawnCount = 0;
    int rookCount = 0;
    int bishopCount = 0;
    int knightCount = 0;
    int queenCount = 0;
    material[0] = pawnCount + "P";
    material[1] = rookCount + "R";
    material[2] = bishopCount + "B";
    material[3] = knightCount + "N";
    material[4] = queenCount + "Q";
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        if (!this.board[x][y].isEmpty()) {
          if (this.board[x][y].getPiece().getID().substring(1).equals("P")) {
            pawnCount++;
            material[0] = pawnCount + "P";
          } 
          if (this.board[x][y].getPiece().getID().substring(1).equals("R")) {
            rookCount++;
            material[1] = rookCount + "R";
          } 
          if (this.board[x][y].getPiece().getID().substring(1).equals("B")) {
            bishopCount++;
            material[2] = bishopCount + "B";
          } 
          if (this.board[x][y].getPiece().getID().substring(1).equals("N")) {
            knightCount++;
            material[3] = knightCount + "N";
          } 
          if (this.board[x][y].getPiece().getID().substring(1).equals("Q")) {
            queenCount++;
            material[4] = queenCount + "Q";
          } 
        } 
      } 
    } 
    return material;
  }
  
  public int fileToIndex(String file) {
    if (file.equals("A"))
      return 0; 
    if (file.equals("B"))
      return 1; 
    if (file.equals("C"))
      return 2; 
    if (file.equals("D"))
      return 3; 
    if (file.equals("E"))
      return 4; 
    if (file.equals("F"))
      return 5; 
    if (file.equals("G"))
      return 6; 
    if (file.equals("H"))
      return 7; 
    return 0;
  }
  
  public int rankToIndex(int rank) {
    if (rank == 0)
      return 7; 
    if (rank == 1)
      return 6; 
    if (rank == 2)
      return 5; 
    if (rank == 3)
      return 4; 
    if (rank == 4)
      return 3; 
    if (rank == 5)
      return 2; 
    if (rank == 6)
      return 1; 
    if (rank == 7)
      return 0; 
    return 100;
  }
}

