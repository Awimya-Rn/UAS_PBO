package uas;

public class Monster extends Enemy {
  public Monster(String name, int hp, int ap, int threatLevel, AttackStrategy strategy) {
    super(name, hp, ap, threatLevel, strategy);
  }

  @Override
  public void attack(Character target) {
    int finalDamage = getAttackPower();

    System.out.printf("  %s menyerang dengan 'Scratch' (AP=%d).\n",
        getName(), finalDamage);
    target.takeDamage(finalDamage);
  }
}