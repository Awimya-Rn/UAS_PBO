package uas;

public class Shield implements StatusEffect {
  private int duration;
  private final int flatReduce;
  private int absorbedTotal = 0;

  public Shield(int flatReduce, int duration) {
    this.flatReduce = flatReduce;
    this.duration = duration;
  }

  @Override
  public String name() {
    return "Shield";
  }

  @Override
  public void onTurnStart(Character self) {
    duration--;
    if (duration > 0) {
      System.out.printf("  [Efek Shield] aktif, sisa %d giliran\n", duration);
    }
  }

  public int reduceDamage(int baseDamage) {
    int reduction = Math.min(flatReduce, Math.max(0, baseDamage));
    absorbedTotal += reduction;
    return Math.max(0, baseDamage - flatReduce);
  }

  @Override
  public void onTurnEnd(Character self) {

  }

  @Override
  public boolean isExpired() {
    return duration <= 0;
  }

  public int getDuration() {
    return duration;
  }

  public int getAbsorbedTotal() {
    return absorbedTotal;
  }
}