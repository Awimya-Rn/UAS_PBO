/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas;

import java.util.*;

public abstract class Character {
  private Integer id;
  private final String name;
  protected final int maxHealth;
  private int health;
  private final int attackPower;

  private final List<StatusEffect> effects = new ArrayList<>();

  protected Character(String name, int health, int attackPower) {
    if (health < 1 || attackPower < 1) {
      throw new IllegalArgumentException("health dan attackPower tidak boleh kurang dari 1");
    }
    this.id = null;
    this.name = name;
    this.maxHealth = health;
    this.health = health;
    this.attackPower = attackPower;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public final String getName() {
    return name;
  }

  public final int getAttackPower() {
    return attackPower;
  }

  public final int getHealth() {
    return health;
  }

  public final int getMaxHealth() {
    return maxHealth;
  }

  protected final void setHealth(int value) {
    this.health = Math.max(0, Math.min(value, maxHealth));
  }

  protected int onIncomingDamage(int base) {
    for (StatusEffect effect : effects) {
      if (effect instanceof Shield) {
        base = ((Shield) effect).reduceDamage(base);
      }
    }
    return base;
  }

  public final boolean isAlive() {
    return health > 0;
  }

  public final void takeDamage(int dmg) {
    int finalDmg = onIncomingDamage(dmg);
    int healthBefore = getHealth();
    setHealth(getHealth() - Math.max(0, finalDmg));
    System.out.printf("  %s menerima %d damage (HP: %d -> %d)\n", getName(), finalDmg, healthBefore, getHealth());
  }

  public final void heal(int amount) {
    int healthBefore = getHealth();
    setHealth(getHealth() + Math.max(0, amount));
    System.out.printf("  %s sembuh %d HP (HP: %d -> %d)\n", getName(), amount, healthBefore, getHealth());
  }

  public final void addEffect(StatusEffect e) {
    if (e != null) {
      effects.add(e);
    }
  }

  public final void performTurn(Character target) {
    startTurn(target);
    endTurn();
  }

  public final void startTurn(Character target) {
    if (!isAlive())
      return;
    effects.forEach(e -> e.onTurnStart(this));
    System.out.printf("[%s] %s (%d/%d) memilih target %s (%d/%d)\n",
        this.getClass().getSimpleName(), getName(), getHealth(), getMaxHealth(),
        target == null ? "?" : target.getName(), target == null ? 0 : target.getHealth(),
        target == null ? 0 : target.getMaxHealth());
    attack(target);
  }

  public final void endTurn() {
    if (!isAlive())
      return;
    effects.forEach(e -> e.onTurnEnd(this));
    Iterator<StatusEffect> it = effects.iterator();
    while (it.hasNext()) {
      StatusEffect effect = it.next();
      if (effect.isExpired()) {
        System.out.printf("  [Efek %s] pada %s telah berakhir.\n", effect.name(), getName());
        it.remove();
      }
    }
  }

  public final List<StatusEffect> getEffects() {
    return Collections.unmodifiableList(effects);
  }

  public abstract void attack(Character target);
}