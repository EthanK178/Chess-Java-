package chess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
  private Rectangle background;
  private ImageView tile = new ImageView();
  private StackPane stack = new StackPane();
  
  private int x;
  private int y;
  
  private boolean isEmpty;
  private Piece piece;
  boolean moveTile = false;
  boolean capturable = false;
  boolean isLight;
  
  int rank;
  String file;
  Color light = Color.rgb(194, 175, 161);
  Color dark = Color.rgb(99, 70, 49);
  
  Image wP;
  Image wR;
  Image wN;
  Image wB;
  Image wQ;
  Image wK;
  Image bP;
  Image bR;
  Image bN;
  Image bB;
  Image bQ;
  Image bK;
  
  public Tile(int xCoord, int yCoord, String f, int r, boolean l) throws FileNotFoundException {
    this.wP = new Image(new FileInputStream("src/images/WhitePawn.png"));
    this.wR = new Image(new FileInputStream("src/images/WhiteRook.png"));
    this.wN = new Image(new FileInputStream("src/images/WhiteKnight.png"));
    this.wB = new Image(new FileInputStream("src/images/WhiteBishop.png"));
    this.wQ = new Image(new FileInputStream("src/images/WhiteQueen.png"));
    this.wK = new Image(new FileInputStream("src/images/WhiteKing.png"));
    this.bP = new Image(new FileInputStream("src/images/BlackPawn.png"));
    this.bR = new Image(new FileInputStream("src/images/BlackRook.png"));
    this.bN = new Image(new FileInputStream("src/images/BlackKnight.png"));
    this.bB = new Image(new FileInputStream("src/images/BlackBishop.png"));
    this.bQ = new Image(new FileInputStream("src/images/BlackQueen.png"));
    this.bK = new Image(new FileInputStream("src/images/BlackKing.png"));
    this.x = xCoord;
    this.y = yCoord;
    this.rank = r;
    this.file = f;
    this.isEmpty = true;
    this.background = new Rectangle(this.x, this.y, 100.0D, 100.0D);
    if (l) {
      this.background.setFill(this.light);
      this.isLight = true;
    } else {
      this.background.setFill(this.dark);
      this.isLight = false;
    } 
    this.tile.setFitWidth(100.0D);
    this.tile.setFitHeight(100.0D);
    this.stack.setLayoutX((this.x + 50));
    this.stack.setLayoutY((this.y + 50));
    this.stack.getChildren().addAll(new Node[] { this.background, this.tile });
  }
  
  public void addPiece(String name) {
    if (name.equalsIgnoreCase("wP")) {
      this.tile.setImage(this.wP);
      this.piece = new Piece("Pawn", "white");
    } else if (name.equalsIgnoreCase("wR")) {
      this.tile.setImage(this.wR);
      this.piece = new Piece("Rook", "white");
    } else if (name.equalsIgnoreCase("wN")) {
      this.tile.setImage(this.wN);
      this.piece = new Piece("Knight", "white");
    } else if (name.equalsIgnoreCase("wB")) {
      this.tile.setImage(this.wB);
      this.piece = new Piece("Bishop", "white");
    } else if (name.equalsIgnoreCase("wQ")) {
      this.tile.setImage(this.wQ);
      this.piece = new Piece("Queen", "white");
    } else if (name.equalsIgnoreCase("wK")) {
      this.tile.setImage(this.wK);
      this.piece = new Piece("King", "white");
    } 
    if (name.equalsIgnoreCase("bP")) {
      this.tile.setImage(this.bP);
      this.piece = new Piece("Pawn", "black");
    } else if (name.equalsIgnoreCase("bR")) {
      this.tile.setImage(this.bR);
      this.piece = new Piece("Rook", "black");
    } else if (name.equalsIgnoreCase("bN")) {
      this.tile.setImage(this.bN);
      this.piece = new Piece("Knight", "black");
    } else if (name.equalsIgnoreCase("bB")) {
      this.tile.setImage(this.bB);
      this.piece = new Piece("Bishop", "black");
    } else if (name.equalsIgnoreCase("bQ")) {
      this.tile.setImage(this.bQ);
      this.piece = new Piece("Queen", "black");
    } else if (name.equalsIgnoreCase("bK")) {
      this.tile.setImage(this.bK);
      this.piece = new Piece("King", "black");
    } 
    this.isEmpty = false;
  }
  
  public void removePiece() {
    this.tile.setImage(null);
    this.isEmpty = true;
  }
  
  public void movePiece(Tile newTile) {
    removePiece();
    newTile.addPiece(getPiece().getID());
    newTile.getPiece().notFirstMove();
    if (newTile.getPiece().getColor().equalsIgnoreCase("white"))
      System.out.println("White: " + newTile.getPiece().getName() + " -> " + newTile.getLocation() + "\n"); 
    if (newTile.getPiece().getColor().equalsIgnoreCase("black"))
      System.out.println("Black: " + newTile.getPiece().getName() + " -> " + newTile.getLocation() + "\n"); 
  }
  
  public void select() {
    this.background.setFill(Color.GREEN);
  }
  
  public void potentialMove() {
    this.background.setFill(Color.AQUA);
    this.moveTile = true;
  }
  
  public void potentialCapture() {
    this.moveTile = true;
  }
  
  public void check() {
    this.background.setFill(Color.RED);
  }
  
  public void reset() {
    if (this.isLight) {
      this.background.setFill(this.light);
    } else {
      this.background.setFill(this.dark);
    } 
    this.moveTile = false;
    this.capturable = false;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public int getRank() {
    return this.rank;
  }
  
  public String getFile() {
    return this.file;
  }
  
  public boolean isEmpty() {
    return this.isEmpty;
  }
  
  public Piece getPiece() {
    return this.piece;
  }
  
  public boolean isMoveable() {
    return this.moveTile;
  }
  
  public StackPane getTile() {
    return this.stack;
  }
  
  public boolean isLight() {
    return this.isLight;
  }
  
  public String getLocation() {
    String r = this.file;
    r = r + (this.rank + 1);
    return r;
  }
}
