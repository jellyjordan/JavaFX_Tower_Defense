package game.engine.characters;


import game.engine.Coordinate;
import game.engine.services.TowerAttackerService;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/*  represents the towers physical properties in the game
    must be constructed with coordinates
 */
public class Tower {
    private int attackDamage;
    private double attackSpeed;     //sleeps the thread after an attack is made
    private int attackRange;        //range value equals the distance the monster can be on the coordinate plane
    private int upgradeCost;        //upgrading changes projectile color
    private int sellCost;
    private ArrayList<Projectile> projectileList;   //holds the towers projectiles for animating when attack is made
    private Coordinate coords;                      //holds tile location and exact location on x/y axis
    private TowerAttackerService towerAttacker;     //polls game monsters to see if they are in tower's attackRange


    public Tower(int x , int y){
        projectileList = new ArrayList<Projectile>();
        coords = new Coordinate(x , y);
        attackDamage = 5;
        attackSpeed = 1.0;
        attackRange = 200;
        towerAttacker = new TowerAttackerService(this);
        towerAttacker.start();
    }
    public void upgradeTower(int userGold){
        attackDamage++;
        attackSpeed = attackSpeed - 0.1;
        attackRange = attackRange + 50;
    }

    public void createProjectile(int endX , int endY){
        projectileList.add(new Projectile(  endX
                                           ,endY
                                           ,Color.BLACK));
    }

    public void sellTower(){

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
    public int getAttackDamage(){return  attackDamage; }
    public double getAttackSpeed(){return attackSpeed;}

    public ArrayList<Projectile> getProjectileList() {
        return projectileList;
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

    /*  Projectile used for graphic when a monster
        is attacked by this tower
    */
    protected class Projectile extends Circle{

        private final double movementX;   //determines speed and direction of projectile
        private final double movementY;   //assume 30fps
        private final int monsterX;
        private boolean endofPath = false;

        Projectile(int monsterX , int monsterY , Color color){
            super(coords.getExactX() , coords.getExactY() , 5 , color);
            this.monsterX = monsterX;

            //if monster location > than tower coordinates we must move in positive direction
            movementX = (monsterX - coords.getExactX()) / 100;
            movementY = (monsterY - coords.getExactY()) / 100;

        }

        //moves projectile along path to monster which was hit.
        public void updatePath(){
            setCenterX(getCenterX() + movementX);
            setCenterY(getCenterY() + movementY);

            //signifies projectile has reached monster
            if(getCenterX() - monsterX < 20 ){
                endofPath = true;
                this.setVisible(false);
            }
        }
    }
}
