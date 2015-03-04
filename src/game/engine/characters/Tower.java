package game.engine.characters;


import game.engine.Coordinate;
import game.engine.services.TowerAttackerService;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Towers are used by the player as the primary weapon
 * to defeat monsters.
 *
 * Each tower spawns a worker thread to poll for nearby monsters
 * to make attacks on.
 */
public class Tower {
    private static final int BUILD_TIME = 10000;    // Time used to build tower.
    private int attackDamage;                       // Determines amount of health to reduce from monsters per attack
    private double attackSpeed;                     // Determines the time a tower must wait after an attack
    private int attackRange;                        // Sets the minimum range the tower can make attacks in
    private int upgradeTime;                        // Time in milliseconds it takes to complete an upgrade.
    private int upgradeCost;                        // Determines the resource cost to upgrade the tower
    private int sellCost;                           // Determines the resources gained by selling the tower
    private ArrayList<Projectile> projectileList;   // Used by the gui thread to create animations for attacks
    private Coordinate coords;                      // Represents the coordinates of the tower on the map
    private TowerAttackerService towerAttacker;     // A worker thread for polling monster locations used for attacks


    /**
     * All towers are created with basic stats which are scaled with
     * upgrades.
     *
     * @param x
     * The x location on the map where the tower is placed.
     * @param y
     * The y location on the map where the tower is placed.
     */
    public Tower(int x , int y){
        projectileList = new ArrayList<Projectile>();
        coords = new Coordinate(x , y);
        attackDamage = 5;
        attackSpeed = 1.0;
        attackRange = 200;
        towerAttacker = new TowerAttackerService(this);
        towerAttacker.pollTower(BUILD_TIME);
        sellCost = 35;
        upgradeCost = 20;
        upgradeTime = 5000;
    }

    /**
     * Upgrades the towers stats.
     */
    public void upgradeTower(){
        attackDamage++;
        attackSpeed = attackSpeed - 0.1;
        attackRange = attackRange + 50;
        upgradeTime += 3000;
        upgradeCost += 20;
    }

    /**
     * Creates a projectile when the tower attacks a monster.
     *
     * @param target
     * The target location of the projectile
     */
    public void createProjectile(Monster target){
        projectileList.add(new Projectile(target , coords.getExactX() , coords.getExactY() , Color.BLACK));
    }


    public int getX(){
        return coords.getExactX();
    }

    public int getY(){
        return coords.getExactY();
    }

    public int getAttackRange(){
        return attackRange;
    }

    public int getAttackDamage(){
        return  attackDamage;
    }

    public double getAttackSpeed(){
        return attackSpeed;
    }

    public int getUpgradeCost(){
        return upgradeCost;
    }

    public int getSellCost(){
        return sellCost;
    }

    public int getUpgradeTime(){
        return upgradeTime;
    }

    public TowerAttackerService getTowerAttacker() {
        return towerAttacker;
    }

    public ArrayList<Projectile> getProjectileList() {
        return projectileList;
    }

    public Coordinate getCoords(){
        return coords;
    }

    public void setAttackDamage(int attackDamage){
        this.attackDamage = attackDamage;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

}
