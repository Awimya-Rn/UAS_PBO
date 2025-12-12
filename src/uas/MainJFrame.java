/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uas;

import java.awt.Component;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainJFrame extends javax.swing.JFrame {

    private DefaultListModel<Character> teamAModel;
    private DefaultListModel<Character> teamBModel;
    private DatabaseManager dbManager; 

    public MainJFrame() {
        initComponents(); 
        initCustomCode(); 
    }

    private void initCustomCode() {
        dbManager = new DatabaseManager();
        dbManager.initializeDatabase();

        teamAModel = new DefaultListModel<>();
        teamBModel = new DefaultListModel<>();

        listTeamA.setModel(teamAModel);
        listTeamB.setModel(teamBModel);

        listTeamA.setCellRenderer(new CharacterRenderer());
        listTeamB.setCellRenderer(new CharacterRenderer());

        loadTeamsFromDatabase();
    }

    private void loadTeamsFromDatabase() {
        teamAModel.clear();
        teamBModel.clear();

        List<Character> teamA = dbManager.loadCharacters("A");
        for (Character c : teamA) {
            teamAModel.addElement(c);
        }

        List<Character> teamB = dbManager.loadCharacters("B");
        for (Character c : teamB) {
            teamBModel.addElement(c);
        }
    }

    private void initComponents() {

        tabMain = new javax.swing.JTabbedPane();
        panelSetup = new javax.swing.JPanel();
        pnlTeamA = new javax.swing.JPanel();
        scrollA = new javax.swing.JScrollPane();
        listTeamA = new javax.swing.JList<>();
        btnAreaA = new javax.swing.JPanel();
        btnAddHero = new javax.swing.JButton();
        btnEditHero = new javax.swing.JButton();
        btnDelHero = new javax.swing.JButton();
        pnlTeamB = new javax.swing.JPanel();
        scrollB = new javax.swing.JScrollPane();
        listTeamB = new javax.swing.JList<>();
        btnAreaB = new javax.swing.JPanel();
        btnAddEnemy = new javax.swing.JButton();
        btnEditEnemy = new javax.swing.JButton();
        btnDelEnemy = new javax.swing.JButton();
        panelBattle = new javax.swing.JPanel();
        scrollLog = new javax.swing.JScrollPane();
        txtBattleLog = new javax.swing.JTextArea();
        btnStartBattle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle Simulator RPG");

        panelSetup.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        pnlTeamA.setBorder(javax.swing.BorderFactory.createTitledBorder("Team A (Heroes)"));
        pnlTeamA.setLayout(new java.awt.BorderLayout());

        listTeamA.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollA.setViewportView(listTeamA);

        pnlTeamA.add(scrollA, java.awt.BorderLayout.CENTER);

        btnAreaA.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        btnAddHero.setText("Add Hero");
        btnAddHero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHeroActionPerformed(evt);
            }
        });
        btnAreaA.add(btnAddHero);

        btnEditHero.setText("Edit");
        btnEditHero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditHeroActionPerformed(evt);
            }
        });
        btnAreaA.add(btnEditHero);

        btnDelHero.setText("Delete");
        btnDelHero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelHeroActionPerformed(evt);
            }
        });
        btnAreaA.add(btnDelHero);

        pnlTeamA.add(btnAreaA, java.awt.BorderLayout.SOUTH);

        panelSetup.add(pnlTeamA);

        pnlTeamB.setBorder(javax.swing.BorderFactory.createTitledBorder("Team B (Enemies)"));
        pnlTeamB.setLayout(new java.awt.BorderLayout());

        listTeamB.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollB.setViewportView(listTeamB);

        pnlTeamB.add(scrollB, java.awt.BorderLayout.CENTER);

        btnAreaB.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        btnAddEnemy.setText("Add Enemy");
        btnAddEnemy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEnemyActionPerformed(evt);
            }
        });
        btnAreaB.add(btnAddEnemy);

        btnEditEnemy.setText("Edit");
        btnEditEnemy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEnemyActionPerformed(evt);
            }
        });
        btnAreaB.add(btnEditEnemy);

        btnDelEnemy.setText("Delete");
        btnDelEnemy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelEnemyActionPerformed(evt);
            }
        });
        btnAreaB.add(btnDelEnemy);

        pnlTeamB.add(btnAreaB, java.awt.BorderLayout.SOUTH);

        panelSetup.add(pnlTeamB);

        tabMain.addTab("Setup Teams", panelSetup);

        panelBattle.setLayout(new java.awt.BorderLayout());

        txtBattleLog.setEditable(false);
        txtBattleLog.setColumns(20);
        txtBattleLog.setFont(new java.awt.Font("Monospaced", 0, 13)); 
        txtBattleLog.setRows(5);
        scrollLog.setViewportView(txtBattleLog);

        panelBattle.add(scrollLog, java.awt.BorderLayout.CENTER);

        btnStartBattle.setBackground(new java.awt.Color(255, 51, 51));
        btnStartBattle.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        btnStartBattle.setForeground(new java.awt.Color(255, 255, 255));
        btnStartBattle.setText("MULAI PERTARUNGAN");
        btnStartBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartBattleActionPerformed(evt);
            }
        });
        panelBattle.add(btnStartBattle, java.awt.BorderLayout.SOUTH);

        tabMain.addTab("Battle Log", panelBattle);

        getContentPane().add(tabMain, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnAddHeroActionPerformed(java.awt.event.ActionEvent evt) {
        openHeroDialog(null); 
    }

    private void btnEditHeroActionPerformed(java.awt.event.ActionEvent evt) {
        Character c = listTeamA.getSelectedValue();
        if (c instanceof Player) {
            openHeroDialog((Player) c); 
        }
    }

    private void btnDelHeroActionPerformed(java.awt.event.ActionEvent evt) {
        Character c = listTeamA.getSelectedValue();
        if (c != null) {
            dbManager.deleteCharacter(c.getId());
            teamAModel.removeElement(c);
        }
    }

    private void btnAddEnemyActionPerformed(java.awt.event.ActionEvent evt) {
        openEnemyDialog(null); 
    }

    private void btnEditEnemyActionPerformed(java.awt.event.ActionEvent evt) {
        Character c = listTeamB.getSelectedValue();
        if (c instanceof Enemy) {
            openEnemyDialog((Enemy) c); 
        }
    }

    private void btnDelEnemyActionPerformed(java.awt.event.ActionEvent evt) {
        Character c = listTeamB.getSelectedValue();
        if (c != null) {
            dbManager.deleteCharacter(c.getId());
            teamBModel.removeElement(c);
        }
    }

    private void btnStartBattleActionPerformed(java.awt.event.ActionEvent evt) {
        if (teamAModel.isEmpty() || teamBModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tim tidak boleh kosong!");
            return;
        }

        tabMain.setSelectedIndex(1); 
        txtBattleLog.setText("");
        btnStartBattle.setEnabled(false);

        List<Character> teamA = new ArrayList<>();
        for (int i = 0; i < teamAModel.size(); i++) {
            teamA.add(teamAModel.get(i));
        }

        List<Character> teamB = new ArrayList<>();
        for (int i = 0; i < teamBModel.size(); i++) {
            teamB.add(teamBModel.get(i));
        }

        PrintStream oldOut = System.out;
        PrintStream customOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    txtBattleLog.append(String.valueOf((char) b));
                    txtBattleLog.setCaretPosition(txtBattleLog.getDocument().getLength());
                });
            }
        });
        System.setOut(customOut);

        new Thread(() -> {
            try {
                Battle battle = new Battle(teamA, teamB);
                battle.run();
            } catch (Exception e) {
                e.printStackTrace(oldOut); 
            } finally {
                System.setOut(oldOut); 
                SwingUtilities.invokeLater(() -> btnStartBattle.setEnabled(true));
            }
        }).start();
    }


    private void openHeroDialog(Player existing) {
        JTextField txtName = new JTextField(existing == null ? "" : existing.getName());
        JSpinner spinHp = new JSpinner(
                new SpinnerNumberModel(existing == null ? 100 : existing.getMaxHealth(), 1, 9999, 10));
        JSpinner spinAp = new JSpinner(
                new SpinnerNumberModel(existing == null ? 15 : existing.getAttackPower(), 1, 999, 1));
        JSpinner spinLvl = new JSpinner(new SpinnerNumberModel(existing == null ? 1 : existing.getLevel(), 1, 100, 1));
        JCheckBox chkHeal = new JCheckBox("Skill: Heal");
        JCheckBox chkPierce = new JCheckBox("Skill: Piercing");

        if (existing != null) {
            for (Skill s : existing.getSkills()) {
                if (s instanceof HealSkill) {
                    chkHeal.setSelected(true);
                }
                if (s instanceof PiercingStrike) {
                    chkPierce.setSelected(true);
                }
            }
        }

        Object[] msg = { "Nama:", txtName, "HP:", spinHp, "AP:", spinAp, "Level:", spinLvl, "Skills:", chkHeal,
                chkPierce };
        if (JOptionPane.showConfirmDialog(this, msg, "Hero Data",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Player p = new Player(txtName.getText(), (int) spinHp.getValue(), (int) spinAp.getValue(),
                    (int) spinLvl.getValue(), new LevelScaledStrategy(2));
            if (chkHeal.isSelected()) {
                p.addSkill(new HealSkill(20));
            }
            if (chkPierce.isSelected()) {
                p.addSkill(new PiercingStrike(1.5));
            }
            p.addEffect(new Regen(5, 3)); 

            if (existing != null) {
                p.setId(existing.getId());
            }

            dbManager.saveCharacter(p, "A");

            if (existing != null) {
                int index = listTeamA.getSelectedIndex();
                teamAModel.set(index, p);
            } else {
                teamAModel.addElement(p);
            }
        }
    }

    private void openEnemyDialog(Enemy existing) {
        JTextField txtName = new JTextField(existing == null ? "" : existing.getName());
        JSpinner spinHp = new JSpinner(
                new SpinnerNumberModel(existing == null ? 80 : existing.getMaxHealth(), 1, 9999, 10));
        JSpinner spinAp = new JSpinner(
                new SpinnerNumberModel(existing == null ? 10 : existing.getAttackPower(), 1, 999, 1));
        JSpinner spinThreat = new JSpinner(
                new SpinnerNumberModel(existing == null ? 1 : existing.getThreatLevel(), 1, 5, 1));
        JComboBox<String> cmbType = new JComboBox<>(new String[] { "Monster", "Boss" });
        if (existing instanceof BossMonster) {
            cmbType.setSelectedIndex(1);
        }

        Object[] msg = { "Nama:", txtName, "HP:", spinHp, "AP:", spinAp, "Threat:", spinThreat, "Type:", cmbType };
        if (JOptionPane.showConfirmDialog(this, msg, "Enemy Data",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String name = txtName.getText();
            int hp = (int) spinHp.getValue();
            int ap = (int) spinAp.getValue();
            int threat = (int) spinThreat.getValue();
            Enemy e = (cmbType.getSelectedIndex() == 1)
                    ? new BossMonster(name, hp, ap, threat, new FixedStrategy(ap + 10))
                    : new Monster(name, hp, ap, threat, new FixedStrategy(ap));

            if (existing != null) {
                e.setId(existing.getId());
            }

            dbManager.saveCharacter(e, "B");

            if (existing != null) {
                int index = listTeamB.getSelectedIndex();
                teamBModel.set(index, e);
            } else {
                teamBModel.addElement(e);
            }
        }
    }

    static class CharacterRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Character) {
                Character c = (Character) value;
                setText(String.format("%s (HP: %d/%d)", c.getName(), c.getHealth(), c.getMaxHealth()));
            }
            return this;
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        java.awt.EventQueue.invokeLater(() -> new MainJFrame().setVisible(true));
    }

    private javax.swing.JPanel btnAreaA;
    private javax.swing.JPanel btnAreaB;
    private javax.swing.JButton btnDelEnemy;
    private javax.swing.JButton btnDelHero;
    private javax.swing.JButton btnEditEnemy;
    private javax.swing.JButton btnEditHero;
    private javax.swing.JButton btnAddEnemy;
    private javax.swing.JButton btnAddHero;
    private javax.swing.JButton btnStartBattle;
    private javax.swing.JList<Character> listTeamA;
    private javax.swing.JList<Character> listTeamB;
    private javax.swing.JPanel panelBattle;
    private javax.swing.JPanel panelSetup;
    private javax.swing.JPanel pnlTeamA;
    private javax.swing.JPanel pnlTeamB;
    private javax.swing.JScrollPane scrollA;
    private javax.swing.JScrollPane scrollB;
    private javax.swing.JScrollPane scrollLog;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTextArea txtBattleLog;
}