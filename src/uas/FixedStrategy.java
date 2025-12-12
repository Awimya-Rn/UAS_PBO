/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

public class FixedStrategy implements AttackStrategy {
  private int fixedDamage;

  public FixedStrategy(int fixedDamage) {
    this.fixedDamage = fixedDamage;
  }

  @Override
  public int computeDamage(Character self, Character target) {
    return fixedDamage;
  }

  public int getFixedDamage() {
    return fixedDamage;
  }
}