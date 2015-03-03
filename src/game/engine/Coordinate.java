package game.engine;


/**
 * Coordinate represents the x y tile location on the map.
 */

public class Coordinate {
    private int x;
    private int y;

    /**
     * Construction specificing the tile coordinates
     * @param x
     * X tile location
     * @param y
     * Y tile location
     */
    public Coordinate(int x , int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * Construction using the exact coordinates which are
     * converted to a tile
     * @param x
     * X clicked coordinate
     * @param y
     * Y clicked coordinate
     */
    public Coordinate(double x , double y){
        this.x = (int)(x / 64);
        this.y = (int)(y / 64);
    }

    public int getTileX(){
        return x;
    }
    public int getTileY(){
        return y;
    }

    public int getExactX(){
        return x * 64 + 32;
    }

    public int getExactY(){
        return y * 64 + 32;
    }

    /**
     * Compares the x/y locations
     * @param obj
     * The coordinate to compare
     * @return
     * true if x and y are equal
     */
    public boolean equals(Coordinate obj) {
        if(this.x == obj.x && this.y == obj.y){
            return true;
        }
        return false;
    }
}
