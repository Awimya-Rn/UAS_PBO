package uas;

public class Regen implements StatusEffect {
  private int duration;
  private final int perTurn;
  private int totalHealed = 0;

  public Regen(int perTurn, int duration) {
    this.perTurn = perTurn;
    this.duration = duration;
  }

  @Override
  public String name() {
    return "Regen";
  }

  @Override
  public void onTurnStart(Character self) {

  }

  @Override
  public void onTurnEnd(Character self) {
    if (self.isAlive()) {
      System.out.printf("  [Efek Regen] ");
      self.heal(perTurn);
      totalHealed += perTurn;
    }
    duration--;
  }

  @Override
  public boolean isExpired() {
    return duration <= 0;
  }

  public int getDuration() {
    return duration;
  }

  public int getTotalHealed() {
    return totalHealed;
  }
}