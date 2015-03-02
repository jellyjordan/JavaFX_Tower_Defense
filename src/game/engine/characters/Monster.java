package game.engine.characters;

/**
 * Monsters are the enemies of the player. They're job is to traverse
 * the path. They are created during timed intervals and removed when
 * their healthPoints is zero or they reach the end point of the path.
 */

import game.engine.Coordinate;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Monster {
    private static ArrayList<Coordinate> path;  // Used by all monsters for pathing
    private Circle view;                        // Graphical view of monster
    private final int radius = 10;              // Graphical size of monster
    private int healthPoints;                   // Determines if the monster is still alive
    private int movementSpeed;                  // Determines time to complete path
    private int reward;                         // Monster death will trigger a resource reward
    private int nodeDirection;                  // Used for guiding the monster on the path
    private boolean moveX;                      // Used for monster pathing
    private boolean isDead;                     // Flag is signal monster removal
    private boolean pathFinished;               // Signals the monster finished the path alive.

    /**
     * Monster initialization
     *
     * @param healthPoints
     * The health points increase as the game progresses to increase
     * the difficulty for the player.
     */
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
    public boolean isDead(){
        return isDead;
    }

    public boolean isPathFinished(){
        return pathFinished;
    }

    /**
     * Sets the path that all monsters will travel upon.
     *
     * @param pathSet
     * The path created by the Tilemap which is used by all monsters
     * and set during the game's initialization phase.
     */
    public static void setPath(ArrayList<Coordinate> pathSet){
        path = pathSet;
    }
    
    /**
     * Reduces the monster's health points
     *
     * @param damage
     * The damage comes from the attacking tower which signals how
     * much health points are deduced from the monster.
     */
    public void takeDamage(int damage){
        healthPoints = healthPoints - damage;
        if (healthPoints <= 0){
            isDead = true;
            pathFinished = false;
        }
    }

    /**
     * Updates the location of the monster along the path that is created
     * by the TileMap in the GameManager initialize method. Movement is
     * made exclusively on the X or Y axis until the path is complete or the
     * monster's healthPoints reach 0.
     */
    public void updateLocation(int distance){

        // Move along the x axis
        if(moveX){
            view.setCenterX(view.getCenterX() + distance);
            // Reached a changing point in path , switch direction
            if(view.getCenterX() == path.get(nodeDirection).getExactX()){
                moveX = false;
                nodeDirection++;
                // Traversed all changing points, path ended
                if(nodeDirection == path.size()){
                    pathFinished = true;
                    isDead = true;
                }
            }
        }
        // Move along the y axis
        else{
            if(view.getCenterY() < path.get(nodeDirection).getExactY()) {
                view.setCenterY(view.getCenterY() + distance);
            }
            else{
                view.setCenterY(view.getCenterY() - distance);
            }
            // Reach changing point , switch direction
            if(view.getCenterY() == path.get(nodeDirection).getExactY()){
                moveX = true;
                nodeDirection++;
                if(nodeDirection == path.size()){
                    pathFinished = true;
                    isDead = true;
                }
            }
        }
    }


}
