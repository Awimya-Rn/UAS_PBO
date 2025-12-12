CREATE DATABASE battle;

CREATE TABLE IF NOT EXISTS characters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, 
    max_health INT NOT NULL,
    current_health INT NOT NULL,
    attack_power INT NOT NULL,
    level INT, 
    threat_level INT, 
    team CHAR(1) NOT NULL CHECK (team IN ('A', 'B')),
    strategy_type VARCHAR(50), 
    strategy_param INT 
);

CREATE TABLE IF NOT EXISTS skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, 
    amount INT, 
    multiplier DOUBLE PRECISION 
);

CREATE TABLE IF NOT EXISTS status_effects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL, 
    per_turn INT, 
    duration INT, 
    flat_reduce INT 
);

CREATE TABLE IF NOT EXISTS character_skills (
    character_id INT REFERENCES characters(id) ON DELETE CASCADE,
    skill_id INT REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (character_id, skill_id)
);

CREATE TABLE IF NOT EXISTS character_effects (
    character_id INT REFERENCES characters(id) ON DELETE CASCADE,
    effect_id INT REFERENCES status_effects(id) ON DELETE CASCADE,
    PRIMARY KEY (character_id, effect_id)
);

INSERT INTO skills (name, amount, multiplier) VALUES
('HealSkill', 20, NULL),
('PiercingStrike', NULL, 1.5)
ON CONFLICT DO NOTHING;

INSERT INTO status_effects (name, per_turn, duration, flat_reduce) VALUES
('Regen', 5, 3, NULL),
('Shield', NULL, 3, 5)
ON CONFLICT DO NOTHING;