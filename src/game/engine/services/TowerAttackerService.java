package game.engine.services;

import game.MenuNavigator;
import game.engine.GameController;
import game.engine.GameManager;
import game.engine.GameState;
import game.engine.characters.Monster;
import game.engine.characters.Tower;
import javafx.animation.PathTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Iterator;

/*
    One service runs for each tower in play. The loop scans all
    monsters alive and reduces their health if they are within range
 */
public class TowerAttackerService extends Service<Void> {
    Tower tower;

    public TowerAttackerService(Tower tower){
        this.tower = tower;
    }

    @Override
    protected Task<Void> createTask(){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                int towerMinXRange = tower.getX() - tower.getAttackRange();
                int towerMaxXRange = tower.getX() + tower.getAttackRange();
                int towerMinYRange = tower.getY() - tower.getAttackRange();
                int towerMaxYRange = tower.getY() + tower.getAttackRange();
                while(GameState.getGame().getLives() > 0) {
                    for (Iterator<Monster> iterator = GameState.getGame().getMonstersAlive().iterator(); iterator.hasNext(); ) {
                        Monster monster = iterator.next();
                        if (monster.getX() > towerMinXRange
                                & monster.getX() < towerMaxXRange
                                & monster.getY() > towerMinYRange
                                & monster.getY() < towerMaxYRange) {
                            monster.takeDamage(tower.getAttackDamage());
                            tower.createProjectile(monster.getX() , monster.getY());
                            try{//thread sleeps if monster is hit
                                Thread.sleep((long) (tower.getAttackSpeed() * 1000));
                            } catch(InterruptedException ex){ex.printStackTrace();}
                        }//end if - attacked monster
                        break;
                    }//end for - monster list
                }//end while - lives
                return null;
            }//end Task method - call

        };
    }//end method - createTask
}//end service class - TowerAttackerService
