package chess;

public class Piece {
  private String name;
  
  private String color;
  
  private boolean firstMove = true;
  
  private boolean enPassant = false;
  
  public Piece(String n, String c) {
    this.name = n;
    this.color = c;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getColor() {
    return this.color;
  }
  
  public String getID() {
    String id = "";
    if (this.color.equals("white")) {
      id = id + "w";
    } else {
      id = id + "b";
    } 
    if (this.name.equalsIgnoreCase("pawn"))
      id = id + "P"; 
    if (this.name.equalsIgnoreCase("rook"))
      id = id + "R"; 
    if (this.name.equalsIgnoreCase("knight"))
      id = id + "N"; 
    if (this.name.equalsIgnoreCase("bishop"))
      id = id + "B"; 
    if (this.name.equalsIgnoreCase("queen"))
      id = id + "Q"; 
    if (this.name.equalsIgnoreCase("king"))
      id = id + "K"; 
    return id;
  }
  
  public void notFirstMove() {
    this.firstMove = false;
  }
  
  public boolean isFirstMove() {
    return this.firstMove;
  }
  
  public void enPassant() {
    this.enPassant = true;
  }
  
  public boolean passantAble() {
    return this.enPassant;
  }
}
