package uas;

public class BossMonster extends Enemy {
  private int turnCounter = 0;
  private int rageCount = 0;

  public BossMonster(String name, int hp, int ap, int threatLevel, AttackStrategy strategy) {
    super(name, hp, ap, threatLevel, strategy);
  }

  @Override
  public void attack(Character target) {
    turnCounter++;
    int baseDamage = getAttackPower();
    double multiplier = 1.0;
    String attackName = "Normal Hit";

    boolean hpRage = getHealth() < (getMaxHealth() * 0.5);
    boolean turnRage = (turnCounter % 3 == 0);

    if (hpRage || turnRage) {
      attackName = "RAGE STRIKE";
      multiplier = 2.0;
      rageCount++;
      String reason = "";
      if (hpRage)
        reason += "HP < 50%";
      if (hpRage && turnRage)
        reason += " & ";
      if (turnRage)
        reason += "Giliran ke-3";
      System.out.printf("  %s mengamuk! (Alasan: %s)\n", getName(), reason);
    } else {
      System.out.printf("  %s menggunakan %s (AP=%d).\n",
          getName(), attackName, baseDamage);
    }

    int finalDamage = (int) (baseDamage * multiplier);
    target.takeDamage(finalDamage);
  }

  public int getRageCount() {
    return rageCount;
  }
}