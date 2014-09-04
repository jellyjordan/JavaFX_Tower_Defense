package game.engine;


//Handles coordinates for characters
//Construct with tile location

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x , int y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        String coords = "( " + x + " , " + y + ")";
        return coords;
    }

    //gets the tile number
    public int getTileX(){
        return x;
    }
    public int getTileY(){
        return y;
    }

    //gets the center point of tile
    public int getExactX(){
        return x * 64 + 32;
    }
    public int getExactY(){
        return y * 64 + 32;
    }
}
