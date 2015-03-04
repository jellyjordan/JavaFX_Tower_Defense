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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Service class is responsible for polling monsters to determine
 * which are in the tower's attack range. Once in range the tower
 * can commit to an attack.
 */
public class TowerAttackerService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> pollHandler;
    private Tower tower;
    private Monster target;

    public TowerAttackerService(Tower tower){
        this.tower = tower;
    }


    /**
     * Iterates through the monster list to find a target. Once one is found
     * a projectile is created which is used by the main gui thread to create
     *
     * @param delay
     * Specifies the delay before the service starts. This is used for creating
     * upgrade and build times.
     */
    public void pollTower(int delay){
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                int towerMinXRange = tower.getX() - tower.getAttackRange();
                int towerMaxXRange = tower.getX() + tower.getAttackRange();
                int towerMinYRange = tower.getY() - tower.getAttackRange();
                int towerMaxYRange = tower.getY() + tower.getAttackRange();
                Iterator<Monster> iterator = GameState.getGame().getMonstersAlive().iterator();

                /*
                    Polls monsters for location and attack if they are in range of the tower
                    After an attack is made the runnable will return and be scheduled after 1
                    second
                  */
                while (iterator.hasNext()) {
                    target = iterator.next();
                    // Breaks after an attack to prioritize the first monster
                    if (target.getX() > towerMinXRange
                            & target.getX() < towerMaxXRange
                            & target.getY() > towerMinYRange
                            & target.getY() < towerMaxYRange) {
                        tower.createProjectile(target);
                        target.takeDamage(tower.getAttackDamage());
                        break;
                    }
                }
            }
        };//end class runnable

         pollHandler = scheduler.scheduleWithFixedDelay(task , delay , 1000 , TimeUnit.MILLISECONDS);
    }//end method - createTask

    /**
     * Cancels/ends the tower attacker service.
     */
    public void cancel(){
        pollHandler.cancel(true);
    }
}//end service class - TowerAttackerService
