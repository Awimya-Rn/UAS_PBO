package uas;

public interface StatusEffect {
  String name();
  void onTurnStart(Character self);
  void onTurnEnd(Character self);
  boolean isExpired();
}
