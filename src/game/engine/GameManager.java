package game.engine;


import game.MenuNavigator;
import game.engine.characters.Monster;
import game.engine.characters.Tower;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;


//class used to delegate between game classes after game has been initialized
/*  class is used when user starts or loads a new game from the game.MainMenuController
    responsible for generating map array and parsing the array to paint the map onto
    stack pane

    starts threads for:
        spawning monster
        tower attack
 */
public class GameManager {
    private  TileMap gameMap;                      //updating tiles upon buying tower
    private  Group monsterLayer;                   //add and remove monsters from the map
    private  GameState game;                       //access game data
    private  Scene gameScene;                      //used by controller
    private  ArrayList<PathTransition> animationQueue;
    private  ArrayList<Monster> monsterRemovalQueue;
    private  GameController gameController;

    //Exception thrown in fxml file not found
    public void initialize() throws java.io.IOException{
        //initialize globals
        game = GameState.getNewGame();
        animationQueue = new ArrayList<PathTransition>();
        gameMap = new TileMap(1280 ,800);
        monsterLayer = new Group();

        //creates gui hierarchy
        FXMLLoader loader = new FXMLLoader(MenuNavigator.GAMEUI);
        StackPane gamePane = new StackPane();
        Group tilemapGroup = new Group();
        monsterLayer.getChildren().add(tilemapGroup);
        tilemapGroup.getChildren().add(gameMap);
        gamePane.getChildren().add(monsterLayer);

        //opens stream to get controller reference
        Node gameUI = (Node)loader.load(MenuNavigator.GAMEUI.openStream());
        gamePane.getChildren().add(gameUI);
        gameScene = new Scene(gamePane);
        gameScene.getStylesheets().add(GameManager.class.getResource("res/menu/gamestyle.css").toExternalForm());
        gameController = loader.<GameController>getController();
        gameController.setGameManager(this);

        MenuNavigator.stage.setScene(gameScene);
        Monster.setPath(gameMap.getPath());
        monsterRemovalQueue = new ArrayList<Monster>();
        startGameLoop();
    }

    public  Scene getGameScene(){
        return gameScene;
    }



    /*verifies the node is open and the user has resources
      called by GameController when user clicks buyTower button
    */
    public void buyTower(double xCords , double yCords){
        //converts the mouse click coordinates to tile format
        int xTile = (int)(xCords / 64);
        int yTile = (int)(yCords / 64);

        //node and resource check before tile update
        if(gameMap.nodeOpen(xTile,yTile)){
            if(game.getResources() > 49) {
                game.addTower(new Tower(xTile, yTile));
                game.setResources(game.getResources() - 50);
                gameMap.setMapNode(((int) (xCords / 64)), ((int) (yCords / 64)), 7);
            }//end if - has resources
        }//end if - is node open
    }//end method buyTower



    private void createMonster(int health){
        game.getMonstersAlive().add(new Monster(health));
        monsterLayer.getChildren().add(game.getMonstersAlive().get(game.getMonstersAlive().size() - 1).getView());
    }//end method - createMonster

    /*
        Updates each monsters location along the path
     */
    private void updateLocations(int timestamp){
        if(!game.getMonstersAlive().isEmpty()){
            for (Monster monster : game.getMonstersAlive()) {
                monster.updateLocation(1);
            }//end for checked monsters
        }//end if- monsters populated
    }//end method - update locations

    //updates FXML labels
    private void updateLabels(int timer){
        //labels must be updated through controller or reference must be passed in initialize
            gameController.updateLabels(
                Integer.toString(game.getLevel()) ,
                Integer.toString(game.getLives()) ,
                Integer.toString(game.getResources()) ,
                Integer.toString(game.getScore()) ,
                Integer.toString(timer)
        );
    }

    //creates an attack animation when the tower makes an attack.
    //called by TowerAttackerService
    public PathTransition createAttackAnimation(int towerX , int towerY , int monsterX , int monsterY){

        Path projectilePath = new Path();
        projectilePath.getElements().add(new MoveTo(towerX , towerY));
        projectilePath.getElements().add(new LineTo(monsterX , monsterY));

        PathTransition pathTransition = new PathTransition(Duration.millis(500) , projectilePath , new Circle(0 , 0 , 5 , Color.BLACK));

        animationQueue.add(pathTransition);
        return null;
    }

    /*
        plays any animations and queue and removes them upon completion
     */
    private void playAttackAnimations(){
        if(!animationQueue.isEmpty()){
            for(PathTransition pathTransition : animationQueue){
                monsterLayer.getChildren().add(pathTransition.getNode());
                pathTransition.play();
            }//end for - play animation
        }//end if - is not empty
    }

    /*
        Checks monsters for killSwitch than removes them and
        clears the deletion queue. Rewards or punishes player
        if the path was finished.
     */
    private synchronized void removeMonsters(){
        for (Monster monster : game.getMonstersAlive()){
            if (monster.killSwitch){
                monsterRemovalQueue.add(monster);
                if (monster.pathFinished){
                    game.setLives((game.getLives()) - 1);
                }// end if - monster finished path/remove life
                else{
                    game.setResources((game.getResources()) + monster.getReward());
                    game.setScore(game.getScore() + (monster.getReward() * game.getLevel()));
                }//end else - monster slain/ give reward
            }//end if - dead monster
        }//end for - add monster to removal queue
        for (Monster monster : monsterRemovalQueue) {
            if (monster.killSwitch) {
                monster.getView().setVisible(false);
                game.getMonstersAlive().remove(monster);
            }
        }
        monsterRemovalQueue.clear();
    }

    private void startGameLoop() {
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);
        final AnimationTimer timer = new AnimationTimer() {
            int timer = 10;

            @Override
            public void handle(long timestamp) {

                if (timestamp/ 1000000000 != lastUpdateTime.get()) {
                    timer--;
                    if(timer > 19) {
                        createMonster(3);
                    }
                    else if(timer <= 0){
                        game.setLevel(game.getLevel() + 1);
                        timer = 30;
                    }//end if - 30 second wave timer
                }//end if - second passed
                removeMonsters();
                updateLocations((int)(1));
               // playAttackAnimations();
              //  updateAttackAnimations();
                lastUpdateTime.set(timestamp / 1000000000);
                updateLabels(timer);
            }//end handle

        };
        timer.start();
    }

}//end class GameManager
