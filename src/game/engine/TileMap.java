package game.engine;

import javafx.scene.image.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

//responsible for painting the map for the client and tracking nodes

public class TileMap extends ImageView{
    private final static String TILESET_64 = "game/engine/res/tileset/tile-64.png";
    private final static String TILESET_128 = "game/engine/res/tileset/tile-128.png";

    private int[][] map;
    private final int RESOLUTION_WIDTH; //Screen Resolution passed from main
    private final int RESOLUTION_HEIGHT;
    private final int TILE_LENGTH_X;    //Length of tiles
    private final int TILE_LENGTH_Y;
    private final int OFFSET_X;         //Offsets used for printing last tile row
    private final int OFFSET_Y;
    private final boolean OFFSET_X_FLAG;//Used for painting the edge of the tilemap to avoid ArrayOutOfBoundsException
    private final boolean OFFSET_Y_FLAG;

    public TileMap(int mapWidth , int mapHeight){
        RESOLUTION_WIDTH  = mapWidth;
        RESOLUTION_HEIGHT  = mapHeight;

        TILE_LENGTH_X = (int) Math.ceil(mapWidth / 64d);
        TILE_LENGTH_Y = (int) Math.ceil(mapHeight / 64d);

        OFFSET_X = TILE_LENGTH_X * 64 - RESOLUTION_WIDTH;
        OFFSET_Y = TILE_LENGTH_Y * 64 - RESOLUTION_HEIGHT;

        if(OFFSET_X == 0){
            OFFSET_X_FLAG = false;
        }
        else{
            OFFSET_X_FLAG = true;
        }


        if(OFFSET_Y == 0){
            OFFSET_Y_FLAG = false;
        }
        else{
            OFFSET_Y_FLAG = true;
        }

        map = generateMapArray();
        repaint();
    }

    //Method paints the map using the given map array and tileset
    public void repaint(){

        //loads tileset
        Image tileset = loadTileSet();

        //Pixel reader
        PixelReader tilereader = tileset.getPixelReader();

        //buffer for aRGB 64x64 tiles
        byte[] buffer = new byte[64 * 64 * 4];
        WritablePixelFormat<ByteBuffer> picFormat = WritablePixelFormat.getByteBgraInstance();

        //Pixel writer
        WritableImage paintedMap = new WritableImage(RESOLUTION_WIDTH , RESOLUTION_HEIGHT);
        PixelWriter tileWriter = paintedMap.getPixelWriter();

        //reads map node than paints the tile
        for(int x = 0; x < TILE_LENGTH_X; x++){
            for(int y = 0; y < TILE_LENGTH_Y; y++ ){
                //populate each rectangle with tile from PixelReader
                switch(map[y][x]){
                    case 0: //paint grass(OPEN NODE)
                        tilereader.getPixels(384 , 64 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 1: //paint horizontal path
                        tilereader.getPixels(384 , 192 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 2: //paint vertical path
                        tilereader.getPixels(448 , 128 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 3: //paint corner EAST TO NORTH
                        tilereader.getPixels(256 , 192 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 4: //paint corner SOUTH TO EAST
                        tilereader.getPixels(192 , 192 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 5: //paint corner NORTH TO EAST
                        tilereader.getPixels(192 , 128 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 6: //paint corner EAST TO SOUTH
                        tilereader.getPixels(256 , 128 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                    case 7: //paint grass and tower
                        tilereader.getPixels(384 , 512 , 64 , 64 , picFormat , buffer , 0 , 256);
                        break;
                }
                if(y == TILE_LENGTH_Y - 1 & OFFSET_Y_FLAG){
                    tileWriter.setPixels(x * 64 , y * 64, 64 , OFFSET_Y , picFormat , buffer , 0 , 256);
                }
                else{
                    tileWriter.setPixels(x * 64 , y * 64, 64 , 64 , picFormat , buffer , 0 , 256);
                }
            }
        };
        this.setImage(paintedMap);
    }
    private int[][] generateMapArray(){
        //to do
        //1. add algorithm to create map array

        //conditions
        //only path start and end points can be located on any of the edges
        //pathes must being in first column and end in last column
        int[][] map;
        //default pattern

        map = new int[][]
                {
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 5 , 1 , 1 , 1 , 1 , 1 , 1 , 1 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 2 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 5 , 1 , 1 , 6 , 0 , 0 , 2 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 2 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {1 , 1 , 1 , 1 , 1 , 1 , 3 , 0 , 0 , 4 , 1 , 1 , 3 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 },
                        {0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 }
                };
        return map;
    }

    //loads the tileset for paintMap
    private Image loadTileSet(){
        //to do: add higher resolution 128x128 tiles for higher resolution
        //oh yeah, I still need to add higher resoltion support

        //tilesize 64x64
        return new Image(TILESET_64);
    }

    //sets the map node for the given coordinates to input value than repaints adjustment
    public void setMapNode(int xCord , int yCord , int updatedValue){
        map[yCord][xCord] = updatedValue;
        this.repaint();
    }

    //checks to see if the node is open
    public boolean nodeOpen(int xCord , int yCord){
        if(map[yCord][xCord] != 0){
            return false;
        }
        return true;
    }

    //returns a path for monster animations
    //monsters travel on straight path between corners
    public ArrayList<Coordinate> getPath(){
        ArrayList<Coordinate> pathXY = new ArrayList<Coordinate>();
        boolean scanSwitch = false;
        int previousY = 0;
        int previousX = 0;

        //searches first column for first tile which is to be spawn location
        for(int y = 0; !scanSwitch; y++){
            if(map[y][0] > 0){
                pathXY.add(new Coordinate(0 , y));
                scanSwitch = true;
                previousY = y;
            }//end if - found first tile
        }//end for - found first tile

        //searches for corners by switching the search axis after each new corner is added
        findpath:
        for(int x = 0; scanSwitch; x++){
            //adds the final path coordinate before exiting loop
            if(x == TILE_LENGTH_X){
                pathXY.add(new Coordinate(x - 1 , previousY));
                break findpath;
            }//end if - no more corners
            if (map[previousY][x] > 2 & map[previousY][x] <7 & x != previousX){
                pathXY.add(new Coordinate(x , previousY));
                scanSwitch = false;
                previousX = x;
            }// end if - found corner
            for(int y = 0; !scanSwitch; y++){
                if (map[y][x] > 2 & map[y][x] <7 & y != previousY){
                    pathXY.add(new Coordinate(x , y));
                    scanSwitch = true;
                    previousY = y;
                }// end if - found corner
            }//end for - column scan
        }//end for - row scan
        return pathXY;
    }
}
