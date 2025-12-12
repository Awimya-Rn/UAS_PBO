package uas;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Player extends Character {
  private int level;
  private AttackStrategy strategy;

  private final List<Skill> skills = new ArrayList<>();
  private final List<Integer> attackLog = new ArrayList<>();

  public Player(String name, int hp, int ap, int level, AttackStrategy strategy) {
    super(name, hp, ap);
    if (level < 1) {
      throw new IllegalArgumentException("level tidak boleh kurang dari 1");
    }
    if (strategy == null) {
      throw new IllegalArgumentException("Strategi tidak boleh null.");
    }
    this.level = level;
    this.strategy = strategy;
  }

  public final int getLevel() {
    return level;
  }

  public AttackStrategy getStrategy() {
    return strategy;
  }

  public void addSkill(Skill s) {
    /* non-null */
    if (s != null) {
      this.skills.add(s);
    }
  }

  public List<Skill> getSkills() {
    return Collections.unmodifiableList(skills);
  }

  public void recordDamageDealt(int dmg) {
    attackLog.add(dmg);
  }

  public List<Integer> getAttackLog() {
    return Collections.unmodifiableList(attackLog);
  }

  public int getTotalDamageDealt() {
    return attackLog.stream().mapToInt(Integer::intValue).sum();
  }

  @Override
  public void attack(Character target) {
    List<Skill> piercingSkills = skills.stream()
        .filter(s -> s instanceof PiercingStrike)
        .collect(Collectors.toList());
    boolean usedPiercing = false;
    if (!piercingSkills.isEmpty()) {
      if (ThreadLocalRandom.current().nextBoolean()) {
        usedPiercing = true;
        Skill ps = piercingSkills.get(0);
        System.out.printf("  %s menggunakan skill %s!\n", getName(), ps.name());
        ps.apply(this, target);
      }
    }
    if (!usedPiercing) {
      int damage = strategy.computeDamage(this, target);
      System.out.printf("  %s menyerang menggunakan %s (via %s).\n",
          getName(), "Normal Hit", strategy.getClass().getSimpleName());
      int before = target.getHealth();
      target.takeDamage(damage);
      int dealt = before - target.getHealth();
      recordDamageDealt(dealt);
    }
  }

}