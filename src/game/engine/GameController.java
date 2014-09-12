package game.engine;

/*
    handles the button inputs for the game
    and links to fxml ui
 */

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class GameController {

    @FXML
    private Label currentScore;
    @FXML
    private Label currentResources;
    @FXML
    private Label currentLevel;
    @FXML
    private Label currentLives;
    @FXML
    private Label timeLabel;

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager){
        this.gameManager = gameManager;
    }

    //set mouse clicks to buy and place tower
    public void buyTower(){
        gameManager.getGameScene().setOnMouseClicked(new buyTower());
    }
    public void openMenu(){
        gameManager.pauseGame();
        //open Game Menu
    }
    public void updateLabels(String currentLevel , String currentLives , String currentResources , String currentScore , String timeLabel){
        this.currentLevel.setText(currentLevel);
        this.currentLives.setText(currentLives);
        this.currentResources.setText(currentResources);
        this.currentScore.setText(currentScore);
        this.timeLabel.setText(timeLabel);
    }


    //buy tower at mouse click tile
    class buyTower implements EventHandler<MouseEvent> {
        public void handle(MouseEvent me) {
            gameManager.buyTower(me.getX(),me.getY());
            }
        }

}
