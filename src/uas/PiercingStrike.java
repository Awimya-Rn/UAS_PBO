package uas;

public class PiercingStrike implements Skill {
  private final double multiplier;

  public PiercingStrike(double multiplier) {
    this.multiplier = multiplier;
  }

  @Override
  public String name() {
    return "PiercingStrike (x" + multiplier + ")";
  }

  @Override
  public void apply(Character self, Character target) {
    int baseDamage;
    if (self instanceof Player) {
      baseDamage = ((Player) self).getStrategy().computeDamage(self, target);
    } else if (self instanceof Enemy) {
      baseDamage = ((Enemy) self).strategy.computeDamage(self, target);
    } else {
      baseDamage = self.getAttackPower();
    }

    int finalDamage = (int) (baseDamage * multiplier);
    int damageTanpaReduksi = finalDamage;
    int damageDenganReduksi = target.onIncomingDamage(finalDamage);
    int reduksiAktual = damageTanpaReduksi - damageDenganReduksi;
    int damageFinalDenganBypass = damageDenganReduksi + (int) (reduksiAktual * 0.25);
    finalDamage = damageFinalDenganBypass;
    System.out.printf("  Serangan menembus pertahanan target!\n");
    int healthSebelum = target.getHealth();
    target.setHealth(healthSebelum - finalDamage);
    System.out.printf("  %s menerima %d damage (Piercing). HP: %d -> %d\n",
        target.getName(), finalDamage, healthSebelum, target.getHealth());

    if (self instanceof Player) {
      ((Player) self).recordDamageDealt(finalDamage);
    }
  }
}