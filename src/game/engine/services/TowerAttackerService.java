package game.engine.services;

import game.engine.GameManager;
import game.engine.GameState;
import game.engine.characters.Monster;
import game.engine.characters.Tower;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Iterator;


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
                            System.out.println("BANG");
                            //GameManager.createAttackAnimation(tower.getX() , tower.getY() , monster.getX() , monster.getY());
                            monster.takeDamage(tower.getAttackDamage());
                            try{
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
