package game.engine.characters;

/*  represents the physical properties of game monsters
 */

import game.engine.Coordinate;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Monster {
    private final int radius = 10;              //size of monster
    private int healthPoints;                   //monsters life points
    private int movementSpeed;                  //speed
    private int reward;                         //gold reward per kill
    private int nodeDirection;                  //used to guide the monster along the path list
    private static ArrayList<Coordinate> path;  //monsters path points
    private Circle view;                        //monster graphic
    private boolean moveX;                      //used with nodeDirection to guide monster
    public boolean killSwitch;                  //sets flag for monster removal
    public boolean pathFinished;               //used to remove life or give reward based on killSwitch activation

    public Monster(int healthPoints){
        pathFinished = false;
        moveX = true;
        nodeDirection = 1;
        this.healthPoints = healthPoints;
        movementSpeed = 1;
        reward = 2;
        view = new Circle(path.get(0).getExactX() , path.get(0).getExactY() , radius);
        view.setFill(Color.RED);
    }

    public static void setPath(ArrayList<Coordinate> pathSet){
        path = pathSet;
    }

    /*
        Updates the location of the monster along the path that is created
        by the TileMap in the GameManager initialize method. Movement is
        made exclusively on the X or Y axis until the path is complete or the
        monster's healthPoints reach 0.
     */
    public void updateLocation(int distance){

        if(moveX){
            view.setCenterX(view.getCenterX() + distance);
            if(view.getCenterX() == path.get(nodeDirection).getExactX()){
                moveX = false;
                nodeDirection++;
                if(nodeDirection == path.size()){
                    pathFinished = true;
                    killSwitch= true;
                }//end if - end of route
            }//end if - switched direction
        }//end if - moved X
        else{
            if(view.getCenterY() < path.get(nodeDirection).getExactY()) {
                view.setCenterY(view.getCenterY() + distance);
            }
            else{
                view.setCenterY(view.getCenterY() - distance);
            }
            if(view.getCenterY() == path.get(nodeDirection).getExactY()){
                moveX = true;
                nodeDirection++;
                if(nodeDirection == path.size()){
                    pathFinished = true;
                    killSwitch= true;
                }//end if end of route
            }//end if - switched direction
        }//end if - moved Y


    }

    public int getX(){
        return ((int)view.getCenterX());
    }
    public int getY(){
        return ((int)view.getCenterY());
    }
    public int getReward(){
        return reward;
    }
    public Circle getView(){
        return view;
    }

    //damage received
    public void takeDamage(int damage){
        healthPoints = healthPoints - damage;
        if (healthPoints <= 0){
            killSwitch = true;
            pathFinished = false;
        }
    }
}
