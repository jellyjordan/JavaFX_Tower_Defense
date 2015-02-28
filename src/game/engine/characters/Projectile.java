package game.engine.characters;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
    Projectile is created when the tower attacks a monster. The GameManager
    will create the animation using the start and end locations.
*/
public class Projectile extends Circle {

    private final int startX;    // Starting location of the projectile
    private final int startY;
    private final int endX;         // Destination of projectile
    private final int endY;

    Projectile(int towerX , int towerY ,int monsterX , int monsterY , Color color){
        super(towerX , towerY , 5 , color);
        startX = towerX;
        startY = towerY;
        endX = monsterX;
        endY = monsterY;
    }

    public int getEndX(){
        return endX;
    }

    public int getEndY(){
        return endY;
    }

    public int getStartX(){
        return startX;
    }

    public int getStartY(){
        return startY;
    }



}

