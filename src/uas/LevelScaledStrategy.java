/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

public class LevelScaledStrategy implements AttackStrategy {
  private final int bonusPerLevel;

  public LevelScaledStrategy(int bonusPerLevel) {
    this.bonusPerLevel = bonusPerLevel;
  }

  @Override
  public int computeDamage(Character self, Character target) {
    if (self instanceof Player) {
      Player p = (Player) self;
      return p.getAttackPower() + (p.getLevel() * bonusPerLevel);
    }
    return self.getAttackPower();
  }

  public int getBonusPerLevel() {
    return bonusPerLevel;
  }
}
