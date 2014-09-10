package game.engine.characters;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*  Projectile used for graphic when a monster
    is attacked by this tower
*/
public class Projectile extends Circle {

    private final double movementX;   //determines speed and direction of projectile
    private final double movementY;   //assume 30fps
    private final int monsterX;
    private boolean endofPath = false;

    Projectile(int towerX , int towerY ,int monsterX , int monsterY , Color color){
        super(towerX , towerY , 5 , color);
        this.monsterX = monsterX;

        //if monster location > than tower coordinates we must move in positive direction
        movementX = (monsterX - towerX) / 80;
        movementY = (monsterY - towerY) / 80;
    }

    //moves projectile along path to monster which was hit.
    public void updatePath(){
        setCenterX(getCenterX() + movementX);
        setCenterY(getCenterY() + movementY);

        //signifies projectile has reached monster
        if(getCenterX() - monsterX <= 0 ){
            endofPath = true;
            this.setVisible(false);
        }
    }
}

