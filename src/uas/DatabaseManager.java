package uas; 

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

  private static final String DB_URL = "jdbc:postgresql://localhost:5433/battle";
  private static final String USER = "postgres";
  private static final String PASS = "united20";

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, USER, PASS);
  }

  public void initializeDatabase() {
    try (Connection conn = getConnection();
        Statement stmt = conn.createStatement()) {
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void saveCharacter(Character character, String team) {
    if (character.getId() == null) {
      insertCharacter(character, team);
    } else {
      updateCharacter(character, team);
    }
    saveSkillsAndEffects(character);
  }

  private void insertCharacter(Character character, String team) {
    String sql = "INSERT INTO characters (name, type, max_health, current_health, attack_power, level, threat_level, team, strategy_type, strategy_param) "
        +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      String type = character.getClass().getSimpleName();
      String strategyType = null;
      int strategyParam = 0;

      if (character instanceof Player) {
        Player p = (Player) character;
        pstmt.setInt(6, p.getLevel());
        pstmt.setNull(7, Types.INTEGER);
        strategyType = p.getStrategy().getClass().getSimpleName();
        if (p.getStrategy() instanceof LevelScaledStrategy) {
          strategyParam = ((LevelScaledStrategy) p.getStrategy()).getBonusPerLevel();
        }
      } else if (character instanceof Enemy) {
        Enemy e = (Enemy) character;
        pstmt.setNull(6, Types.INTEGER);
        pstmt.setInt(7, e.getThreatLevel());
        strategyType = e.strategy.getClass().getSimpleName();
        if (e.strategy instanceof FixedStrategy) {
          strategyParam = ((FixedStrategy) e.strategy).getFixedDamage();
        }
      }

      pstmt.setString(1, character.getName());
      pstmt.setString(2, type);
      pstmt.setInt(3, character.getMaxHealth());
      pstmt.setInt(4, character.getHealth());
      pstmt.setInt(5, character.getAttackPower());
      pstmt.setString(8, team);
      pstmt.setString(9, strategyType);
      pstmt.setInt(10, strategyParam);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          character.setId(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateCharacter(Character character, String team) {
    String sql = "UPDATE characters SET name = ?, max_health = ?, current_health = ?, attack_power = ?, level = ?, threat_level = ?, team = ?, strategy_type = ?, strategy_param = ? WHERE id = ?";

    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      String strategyType = null;
      int strategyParam = 0;

      if (character instanceof Player) {
        Player p = (Player) character;
        pstmt.setInt(5, p.getLevel());
        pstmt.setNull(6, Types.INTEGER);
        strategyType = p.getStrategy().getClass().getSimpleName();
        if (p.getStrategy() instanceof LevelScaledStrategy) {
          strategyParam = ((LevelScaledStrategy) p.getStrategy()).getBonusPerLevel();
        }
      } else if (character instanceof Enemy) {
        Enemy e = (Enemy) character;
        pstmt.setNull(5, Types.INTEGER);
        pstmt.setInt(6, e.getThreatLevel());
        strategyType = e.strategy.getClass().getSimpleName();
        if (e.strategy instanceof FixedStrategy) {
          strategyParam = ((FixedStrategy) e.strategy).getFixedDamage();
        }
      }

      pstmt.setString(1, character.getName());
      pstmt.setInt(2, character.getMaxHealth());
      pstmt.setInt(3, character.getHealth());
      pstmt.setInt(4, character.getAttackPower());
      pstmt.setString(7, team);
      pstmt.setString(8, strategyType);
      pstmt.setInt(9, strategyParam);
      pstmt.setInt(10, character.getId());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void saveSkillsAndEffects(Character character) {
    if (character.getId() == null) {
      return;
    }

    deleteSkillsAndEffects(character.getId());

    if (character instanceof Player) {
      Player p = (Player) character;
      for (Skill skill : p.getSkills()) {
        int skillId = getSkillId(skill);
        if (skillId != -1) {
          insertCharacterSkillLink(character.getId(), skillId);
        }
      }
    }

    for (StatusEffect effect : character.getEffects()) {
      int effectId = getEffectId(effect);
      if (effectId != -1) {
        insertCharacterEffectLink(character.getId(), effectId);
      }
    }
  }

  // --- METODE HELPER YANG DIPERBAIKI ---

  /**
   * Menambahkan link antara karakter dan skill ke tabel character_skills.
   */
  private void insertCharacterSkillLink(int characterId, int skillId) {
    String sql = "INSERT INTO character_skills (character_id, skill_id) VALUES (?, ?)";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, characterId);
      pstmt.setInt(2, skillId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Menambahkan link antara karakter dan efek status ke tabel character_effects.
   */
  private void insertCharacterEffectLink(int characterId, int effectId) {
    String sql = "INSERT INTO character_effects (character_id, effect_id) VALUES (?, ?)";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, characterId);
      pstmt.setInt(2, effectId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // --- SISANYA METODE TIDAK BERUBAH ---

  private int getSkillId(Skill skill) {
    String sql = "SELECT id FROM skills WHERE name = ?";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, skill.name());
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  private int getEffectId(StatusEffect effect) {
    String sql = "SELECT id FROM status_effects WHERE name = ?";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, effect.name());
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  private void deleteSkillsAndEffects(int characterId) {
    String deleteSkillsSql = "DELETE FROM character_skills WHERE character_id = ?";
    String deleteEffectsSql = "DELETE FROM character_effects WHERE character_id = ?";
    try (Connection conn = getConnection()) {
      try (PreparedStatement pstmt = conn.prepareStatement(deleteSkillsSql)) {
        pstmt.setInt(1, characterId);
        pstmt.executeUpdate();
      }
      try (PreparedStatement pstmt = conn.prepareStatement(deleteEffectsSql)) {
        pstmt.setInt(1, characterId);
        pstmt.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteCharacter(int characterId) {
    String sql = "DELETE FROM characters WHERE id = ?";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, characterId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Character> loadCharacters(String team) {
    List<Character> characters = new ArrayList<>();
    String sql = "SELECT * FROM characters WHERE team = ?";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, team);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          Character c = mapRowToCharacter(rs);
          if (c != null) {
            loadSkillsAndEffects(c);
            characters.add(c);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return characters;
  }

  private void loadSkillsAndEffects(Character character) {
    if (character instanceof Player) {
      String sql = "SELECT s.* FROM skills s JOIN character_skills cs ON s.id = cs.skill_id WHERE cs.character_id = ?";
      try (Connection conn = getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, character.getId());
        try (ResultSet rs = pstmt.executeQuery()) {
          while (rs.next()) {
            Skill skill = mapRowToSkill(rs);
            if (skill != null) {
              ((Player) character).addSkill(skill);
            }
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String sql = "SELECT se.* FROM status_effects se JOIN character_effects ce ON se.id = ce.effect_id WHERE ce.character_id = ?";
    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, character.getId());
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          StatusEffect effect = mapRowToEffect(rs);
          if (effect != null) {
            character.addEffect(effect);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Skill mapRowToSkill(ResultSet rs) throws SQLException {
    String name = rs.getString("name");
    if ("HealSkill".equals(name)) {
      return new HealSkill(rs.getInt("amount"));
    } else if ("PiercingStrike".equals(name)) {
      return new PiercingStrike(rs.getDouble("multiplier"));
    }
    return null;
  }

  private StatusEffect mapRowToEffect(ResultSet rs) throws SQLException {
    String name = rs.getString("name");
    if ("Regen".equals(name)) {
      return new Regen(rs.getInt("per_turn"), rs.getInt("duration"));
    } else if ("Shield".equals(name)) {
      return new Shield(rs.getInt("flat_reduce"), rs.getInt("duration"));
    }
    return null;
  }

  private Character mapRowToCharacter(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    String type = rs.getString("type");
    int maxHealth = rs.getInt("max_health");
    int currentHealth = rs.getInt("current_health");
    int attackPower = rs.getInt("attack_power");
    String strategyType = rs.getString("strategy_type");
    int strategyParam = rs.getInt("strategy_param");

    Character character = null;
    AttackStrategy strategy = null;

    if ("LevelScaled".equals(strategyType)) {
      strategy = new LevelScaledStrategy(strategyParam);
    } else if ("Fixed".equals(strategyType)) {
      strategy = new FixedStrategy(strategyParam);
    }

    if ("Player".equals(type)) {
      int level = rs.getInt("level");
      character = new Player(name, maxHealth, attackPower, level, strategy);
    } else if ("Monster".equals(type)) {
      int threatLevel = rs.getInt("threat_level");
      character = new Monster(name, maxHealth, attackPower, threatLevel, strategy);
    } else if ("BossMonster".equals(type)) {
      int threatLevel = rs.getInt("threat_level");
      character = new BossMonster(name, maxHealth, attackPower, threatLevel, strategy);
    }

    if (character != null) {
      character.setId(id);
      character.setHealth(currentHealth);
    }

    return character;
  }
}