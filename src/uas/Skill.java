package uas;

public interface Skill {
  String name();

  void apply(Character self, Character target);
}
