package game;

import game.engine.GameManager;

import java.io.IOException;

/**
 * Created by jelly on 4/16/14.
 */
public class MainMenuController {

    public void startNewGame(){
        try{
            GameManager gameManager = new GameManager();
            gameManager.initialize();}catch (IOException ex){ex.printStackTrace();}
    }
    public void exitGame(){
        System.exit(1);
    }
}