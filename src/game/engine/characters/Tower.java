package game.engine.characters;


import game.engine.Coordinate;
import game.engine.services.TowerAttackerService;

/*  represents the towers physical properties in the game
    must be constructed with coordinates
 */
public class Tower {
    private int attackDamage;
    private double attackSpeed;    //sleeps the thread after an attack is made
    private int attackRange;    //range value equals the distance the monster can be on the coordinate plane
    private int upgradeCost;
    private int sellCost;
    private Coordinate coords;
    private TowerAttackerService towerAttacker;
    private int[] towerTarget;

    public Tower(int x , int y){
        coords = new Coordinate(x , y);
        attackDamage = 1;
        attackSpeed = .5;
        attackRange = 200;
        towerTarget = new int[2];
        towerAttacker = new TowerAttackerService(this);
        towerAttacker.start();
    }
    public void upgradeTower(int userGold){
        attackDamage++;
        attackSpeed = attackSpeed - 0.1;
        attackRange = attackRange + 50;
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
