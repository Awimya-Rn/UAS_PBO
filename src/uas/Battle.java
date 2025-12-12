package uas;

import java.util.*;

public class Battle {
  private final List<Character> teamA;
  private final List<Character> teamB;
  private int turnCount = 0;

  public Battle(List<Character> teamA, List<Character> teamB) {
    this.teamA = teamA;
    this.teamB = teamB;
  }

  private boolean isTeamAlive(List<Character> team) {
    return team.stream().anyMatch(c -> c.isAlive());
  }

  private Character findTargetForPlayer(List<Character> enemies) {
    return enemies.stream()
        .filter(c -> c.isAlive())
        .map(c -> (Enemy) c)
        .sorted(Comparator.comparing(Enemy::getThreatLevel).reversed()
            .thenComparing(Character::getHealth))
        .findFirst()
        .orElse(null);
  }

  private Character findTargetForEnemy(List<Character> players) {
    return players.stream()
        .filter(c -> c.isAlive())
        .sorted(Comparator.comparing(Character::getHealth).reversed()) 
        .findFirst()
        .orElse(null);
  }

  public void run() {
    System.out.println("=== PERTARUNGAN DIMULAI ===");
    while (isTeamAlive(teamA) && isTeamAlive(teamB)) {
      turnCount++;
      System.out.printf("\n=== Turn %d ===\n", turnCount);

      for (Character memberA : teamA) {
        if (memberA.isAlive() && isTeamAlive(teamB)) {
          Character target = findTargetForPlayer(teamB);
          if (target != null) {
            memberA.startTurn(target); 
          }
        }
        System.out.println();
      }


      if (!isTeamAlive(teamB))
        break;

      for (Character memberB : teamB) {
        if (memberB.isAlive() && isTeamAlive(teamA)) {
          Character target = findTargetForEnemy(teamA);
          if (target != null) {
            memberB.performTurn(target); 
          }
        }
        System.out.println();
      }

      for (Character memberA : teamA) {
        memberA.endTurn();
      }
    }

    System.out.println("\n=== HASIL AKHIR ===");
    if (isTeamAlive(teamA)) {
      System.out.println("Tim A Menang!");
    } else {
      System.out.println("Tim B Menang! (Tim A kalah)");
    }

    System.out.println("\nStatistik Sisa HP:");
    System.out.println("Tim A:");
    teamA.forEach(c -> System.out.printf("- %s: %d/%d %s\n",
        c.getName(), c.getHealth(), c.getMaxHealth(), c.isAlive() ? "" : "(KALAH)"));
    System.out.println("Tim B:");
    teamB.forEach(c -> System.out.printf("- %s: %d/%d %s\n",
        c.getName(), c.getHealth(), c.getMaxHealth(), c.isAlive() ? "" : "(KALAH)"));
  }
}
