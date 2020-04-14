import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;

public class TeamMaker extends JFrame implements ActionListener, MouseInputListener {

    private static final long serialVersionUID = 1L;

    static Scanner sc = new Scanner(System.in);
    private JScrollPane team_list_scroll;
    private JButton add_tm, rm_tm, exp_t, exp_tm, ed_elems;
    private JPanel center;

    public TeamMaker(String name) throws IOException {
        ImageIcon img = new ImageIcon("icon.png");
        setIconImage(img.getImage());
        setTitle(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (scrSize.getWidth() / 2), (int) ((scrSize.getHeight() / 2)));
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());

        center = new JPanel(new GridLayout(1, 2));
        JTabbedPane tab = new JTabbedPane();
        String[] campos = { "Dia", "Turno 1", "Turno 2" };
        DefaultTableModel tm = new DefaultTableModel(campos, 0);
        JTable tb = new JTable(tm);
        JScrollPane calendar_scroll = new JScrollPane(tb);
        LocalDate dt = LocalDate.now().plusMonths(1);
        String m = Months.get(dt.getMonth().ordinal());
        tab.addTab("-", null);
        tab.addTab(m, calendar_scroll);
        tab.setSelectedIndex(1);
        tab.addTab("+", null);
        tab.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JScrollPane z = (JScrollPane) tab.getComponentAt(tab.getTabCount() - 2);
                JTable zz = (JTable) (z.getViewport().getView());
                DefaultTableModel z1 = (DefaultTableModel) zz.getModel();
                if (Team.getAll().size() != 0 && z1.getRowCount() != 0) {
                    if (tab.getSelectedIndex() == tab.getTabCount() - 1) {

                        LocalDate d = dt.plusMonths(tab.getTabCount() - 1);
                        String g = Months.get(d.getMonth().ordinal());
                        DefaultTableModel tt = new DefaultTableModel(campos, 0);
                        JTable b = new JTable(tt);
                        JScrollPane c = new JScrollPane(b);
                        tab.addTab(g, c);
                        tab.addTab("+", null);
                        tab.setSelectedIndex((tab.getTabCount() - 2));
                        tab.remove((tab.getTabCount() - 3));

                        JScrollPane w = (JScrollPane) tab.getComponentAt(tab.getTabCount() - 3);
                        JTable ww = (JTable) (w.getViewport().getView());
                        DefaultTableModel w1 = (DefaultTableModel) ww.getModel();
                        int p = Integer
                                .parseInt((String) (w1.getValueAt((w1.getRowCount() - 1), (w1.getColumnCount() - 1))));

                        calendar(p, tab.getSelectedIndex());
                    } else if (tab.getSelectedIndex() == 0 && tab.getTabCount() > 3) {
                        tab.remove(tab.getTabCount() - 2);
                        tab.setSelectedIndex((tab.getTabCount() - 2));
                    } else {
                        tab.setSelectedIndex((tab.getTabCount() - 2));
                    }
                } else {
                    tab.setSelectedIndex((tab.getTabCount() - 2));
                }
            }
        });

        // Bottom bar ->
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        JPanel bottom_top = new JPanel(new GridLayout(1, 3));
        JPanel bottom_bottom = new JPanel(new GridLayout(1, 2));
        // <- Bottom bar

        // Buttons ->
        add_tm = new JButton("Criar Equipas");
        add_tm.addActionListener(this);
        add_tm.setEnabled(false);
        add_tm.addMouseListener(this);
        add_tm.setBackground(new Color(238, 238, 238));
        ed_elems = new JButton("Editar Elementos");
        ed_elems.addActionListener(this);
        ed_elems.addMouseListener(this);
        ed_elems.setBackground(new Color(238, 238, 238));
        rm_tm = new JButton("Remover Equipa");
        rm_tm.addActionListener(this);
        rm_tm.setEnabled(false);
        rm_tm.addMouseListener(this);
        rm_tm.setBackground(new Color(238, 238, 238));
        exp_t = new JButton("Exportar Turnos");
        exp_t.addActionListener(this);
        exp_t.addMouseListener(this);
        exp_t.setBackground(new Color(238, 238, 238));
        exp_tm = new JButton("Exportar Equipas");
        exp_tm.addActionListener(this);
        exp_tm.addMouseListener(this);
        exp_tm.setBackground(new Color(238, 238, 238));
        // <- Buttons

        // Team list ->
        JPanel teams = new JPanel(new GridLayout(2, 1));
        String[] fields = { "Nome", "Numero Mec.", "Graduacao" };
        DefaultTableModel tmdl = new DefaultTableModel(fields, 0);
        JTable team_detls = new JTable(tmdl);
        team_detls.setOpaque(false);
        JScrollPane detls_scroll = new JScrollPane(team_detls);
        detls_scroll.getViewport().setBackground(new Color(238, 238, 238));

        DefaultListModel list_comp = new DefaultListModel<>();
        JList team_list = new JList<>(list_comp);
        team_list.setOpaque(false);
        team_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        team_list_scroll = new JScrollPane(team_list);
        team_list_scroll.getViewport().setBackground(new Color(238, 238, 238));
        team_list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent le) {
                int idx = team_list.getSelectedIndex();
                tmdl.setRowCount(0);
                for (Firefighter t : Team.getAll().get(idx).getTm()) {
                    tmdl.addRow(new Object[] { t.getName(), t.getN_mec(), t.getGrad().getValue() });
                }
            }
        });
        // <- Team list

        teams.add(team_list_scroll);
        teams.add(detls_scroll);
        bottom_top.add(add_tm);
        bottom_top.add(rm_tm);
        bottom_top.add(ed_elems);
        bottom_bottom.add(exp_t);
        bottom_bottom.add(exp_tm);
        bottom.add(bottom_top);
        bottom.add(bottom_bottom);
        center.add(teams);
        center.add(tab);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Team creator ->
    private void createTeams() {

        Team.clear();
        if (!Firefighter.getAll().isEmpty() || Firefighter.getAll().size() >= Team.getNumberOfTeams()) {
            ArrayList<Firefighter> copy = Firefighter.getAll();

            ArrayList<Firefighter> pesado = new ArrayList<Firefighter>();
            ArrayList<Firefighter> ligeiro = new ArrayList<Firefighter>();
            ArrayList<Firefighter> tas = new ArrayList<Firefighter>();
            ArrayList<Firefighter> tat = new ArrayList<Firefighter>();
            ArrayList<Firefighter> est = new ArrayList<Firefighter>();
            ArrayList<ArrayList<Firefighter>> dbs = new ArrayList<ArrayList<Firefighter>>();
            dbs.add(pesado);
            dbs.add(ligeiro);
            dbs.add(tas);
            dbs.add(tat);
            dbs.add(est);

            int ind = 0;
            for (int i = 0; i < Firefighter.getAll().size(); i++) {
                if (ind < Team.getNumberOfTeams() && (copy.get(i).getGrad().ordinal() >= Graduacao.CHEFE.ordinal())) { // Assign
                                                                                                                       // team
                    Team x = new Team();
                    x.add(copy.get(i));
                    ind++;
                } else if (copy.get(i).getGrad().ordinal() >= Graduacao.CHEFE.ordinal()
                        && copy.get(i).getEstado() == Estado.ATIVO) { // Seperate people into
                    // groups of qualifications
                    if (copy.get(i).getPesado() && copy.get(i).getGrad() != Graduacao.ESTAGIARIO) {
                        pesado.add(copy.get(i));
                    }
                    if (copy.get(i).getLigeiro() && copy.get(i).getGrad() != Graduacao.ESTAGIARIO) {
                        ligeiro.add(copy.get(i));
                    }
                    if (copy.get(i).getTas()) {
                        tas.add(copy.get(i));
                    } else {
                        tat.add(copy.get(i));
                    }
                }
            }
            addTeamList(Team.getAll().size());

            Random r = new Random();
            Collections.shuffle(Team.getAll());
            for (Team x : Team.getAll()) {
                boolean ck2 = true;
                while (ck2) {
                    if (!x.getTruckDrivers() && !pesado.isEmpty()) {
                        int y = r.nextInt(pesado.size());
                        Firefighter w = pesado.get(y);
                        x.add(w);
                        pesado.remove(w);
                        ligeiro.remove(w);
                        if (w.getTas()) {
                            tas.remove(w);
                        } else {
                            tat.remove(w);
                        }
                    } else if (!x.getAmbulanceDrivers() && !ligeiro.isEmpty()) {
                        int y = r.nextInt(ligeiro.size());
                        Firefighter w = ligeiro.get(y);
                        x.add(w);
                        ligeiro.remove(w);
                        if (w.getTas()) {
                            tas.remove(w);
                        } else {
                            tat.remove(w);
                        }
                        if (w.getPesado()) {
                            pesado.remove(w);
                        }
                    } else if (!x.getTas() && !tas.isEmpty()) {
                        int y = r.nextInt(tas.size());
                        Firefighter w = tas.get(y);
                        x.add(w);
                        tas.remove(w);
                        if (w.getLigeiro()) {
                            ligeiro.remove(w);
                            if (w.getPesado()) {
                                pesado.remove(w);
                            }
                        }
                        if (w.getGrad() == Graduacao.ESTAGIARIO) {
                            est.remove(w);
                        }
                    } else if (!x.getEstg() && !est.isEmpty()) {
                        int y = r.nextInt(est.size());
                        Firefighter w = est.get(y);
                        x.add(w);
                        est.remove(w);
                        if (w.getTas()) {
                            tas.remove(w);
                        } else {
                            tat.remove(w);
                        }
                    } else {
                        ck2 = false;
                    }
                }

            }
            Collections.sort(Team.getAll());

            int y = Firefighter.getAll().size() / Team.getAll().size();
            for (ArrayList<Firefighter> x : dbs) {
                Collections.shuffle(x);
                int id = 0;
                while (!x.isEmpty()) {
                    if (id == Team.getAll().size()) {
                        id = 0;
                    }
                    Firefighter w = x.get(0);
                    if (Team.getAll().get(id).size() <= y) {
                        Team.getAll().get(id).add(w);
                        if (!ligeiro.isEmpty() && w.getLigeiro()) {
                            ligeiro.remove(w);
                            if (!pesado.isEmpty() && w.getPesado()) {
                                pesado.remove(w);
                            }
                        }
                        if (!tas.isEmpty() && w.getTas()) {
                            tas.remove(w);
                        } else if (!tat.isEmpty()) {
                            tat.remove(w);
                        }
                        if (!est.isEmpty() && w.getGrad() == Graduacao.ESTAGIARIO) {
                            est.remove(w);
                        }
                    }
                    id += 1;
                }
            }
        }
        calendar(0, 0);
    }
    // <- Team creator

    // Called by createTeams, it adds a team to the list ->
    private void addTeamList(int id) {
        JScrollPane b = team_list_scroll;
        JList c = (JList) (b.getViewport().getView());
        DefaultListModel j = ((DefaultListModel) c.getModel());
        j.clear();
        for (int i = 1; i <= id; i++) {
            j.addElement("Equipa " + String.valueOf(i));
        }
        rm_tm.setEnabled(true);
    }
    // <- Called by createTeams, it adds a team to the list

    // Removes a selected team ->
    private void rmTeam() {
        JScrollPane b = team_list_scroll;
        JList c = (JList) (b.getViewport().getView());
        if (((DefaultListModel) c.getModel()).size() == 0) {
            rm_tm.setEnabled(false);
            add_tm.setEnabled(true);
        } else if (((DefaultListModel) c.getModel()).size() == 1) {
            rm_tm.setEnabled(false);
            int e = c.getSelectedIndex();
            ((DefaultListModel) c.getModel()).removeElementAt(e);
            Team.remove(e);
        } else {
            int e = c.getSelectedIndex();
            ((DefaultListModel) c.getModel()).removeElementAt(e);
            Team.remove(e);
            if (((DefaultListModel) c.getModel()).size() + 1 > e) {
                for (int i = e; i < ((DefaultListModel) c.getModel()).size(); i++) {
                    String f = (String) ((DefaultListModel) c.getModel()).getElementAt(i);
                    String g = f.charAt(f.length() - 1) + "";
                    int h = Integer.parseInt(g);
                    g = "Equipa " + (--h);
                    ((DefaultListModel) c.getModel()).setElementAt(g, i);
                }
            }
        }
        JTabbedPane f = (JTabbedPane) center.getComponent(1);
        for (int i = 1; i < f.getTabCount(); i++) {
            f.remove(i);
        }
        f.setSelectedIndex(0);
        JScrollPane a = (JScrollPane) f.getSelectedComponent();
        JTable r = (JTable) (a.getViewport().getView());
        DefaultTableModel s = (DefaultTableModel) r.getModel();
        s.setRowCount(0);
        calendar(0, 0);
    }
    // <- Removes a selected team

    public void newDatabase(boolean z) {
        add_tm.setEnabled(z);
    }

    // Calendar organizer ->
    private void calendar(int firstteam, int offset) {
        int z = firstteam;
        z++;
        LocalDate dt = LocalDate.now();
        LocalDate date = dt.plusMonths(offset + 1);
        int num = date.lengthOfMonth();
        int x = date.getDayOfMonth();
        LocalDate day = date.minusDays(x - 1);

        List<String> shifts = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            String shift = String.valueOf(i) + ",";
            if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                if (z > Team.getAll().size()) {
                    z = 1;
                }
                shift += (String.valueOf(z++) + ",");
                if (z > Team.getAll().size()) {
                    z = 1;
                }
                shift += String.valueOf(z++);

            } else {
                if (z > Team.getAll().size()) {
                    z = 1;
                }
                shift += "" + "," + String.valueOf(z++);
            }
            shifts.add(shift);
            day = day.plusDays(1);
        }

        JTabbedPane f = (JTabbedPane) center.getComponent(1);
        JScrollPane a = (JScrollPane) f.getSelectedComponent();
        JTable b = (JTable) (a.getViewport().getView());
        DefaultTableModel c = (DefaultTableModel) b.getModel();
        c.setRowCount(0);
        for (int i = 0; i < shifts.size(); i++) {
            String[] d = shifts.get(i).split(",");
            String dia = d[0], t1 = d[1], t2 = d[2];
            c.addRow(new Object[] { dia, t1, t2 });
        }
    }
    // <- Calendar organizer

    // Export teams ->
    private void exportTeam() throws IOException {
        String u = null;
        JFileChooser dest = new JFileChooser();
        dest.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dest.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            u = dest.getSelectedFile().getAbsolutePath() + "/Equipas.csv";
        }
        File out = new File(u);
        PrintWriter w = new PrintWriter(out);
        for (Team x : Team.getAll()) {
            w.println("Equipa " + x.getId() + ", ,");
            for (Firefighter t : x.getTm()) {
                w.println(t.getN_mec() + "," + t.getName() + "," + t.getGrad().getValue());
            }
            w.println(" , , ");
        }
        w.close();
    }
    // <- Export teams

    // Export cal ->
    private void exportCalendar() throws IOException {
        String u = null;
        JFileChooser dest = new JFileChooser();
        dest.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dest.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            u = dest.getSelectedFile().getAbsolutePath();
        }

        JTabbedPane ff = (JTabbedPane) center.getComponent(1);

        for (int i = 0; i < ff.getTabCount() - 1; i++) {

            File out = new File((u + "\\" + ff.getTitleAt(i)) + ".csv");
            System.out.println(out.getAbsolutePath());
            PrintWriter w = new PrintWriter(out);

            JTabbedPane f = (JTabbedPane) center.getComponent(1);
            JScrollPane a = (JScrollPane) f.getComponentAt(i); /* f.getSelectedComponent(); */
            JTable b = (JTable) (a.getViewport().getView());
            DefaultTableModel c = (DefaultTableModel) b.getModel();

            w.println(f.getTitleAt(i));
            w.println("");
            for (int ii = 0; ii < c.getRowCount(); ii++) {
                w.println(c.getValueAt(ii, 0) + "," + c.getValueAt(ii, 1) + "," + c.getValueAt(ii, 2));
            }

            w.close();
        }
    }
    // <- Export cal

    // Action listener for all buttons ->
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        String c = b.getText();

        switch (c) {
            case "Criar Equipas":
                createTeams();
                break;
            case "Remover Equipa":
                rmTeam();
                break;
            case "Exportar Turnos":
                try {
                    exportCalendar();
                } catch (IOException e8) {
                }
                break;
            case "Exportar Equipas":
                try {
                    exportTeam();
                } catch (IOException e9) {
                }
                break;
            case "Editar Elementos":
                new Database(this);
                break;
        }
    }
    // <- Action listener for all buttons

    @Override
    public void mouseClicked(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        temp.setBackground(new Color(238, 238, 238));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        if (temp.isEnabled()) {
            temp.setBackground(Color.LIGHT_GRAY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        temp.setBackground(new Color(238, 238, 238));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        if (temp.isEnabled()) {
            temp.setBackground(Color.LIGHT_GRAY);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        temp.setBackground(new Color(238, 238, 238));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        JButton temp = (JButton) e.getSource();
        if (temp.isEnabled()) {
            temp.setBackground(Color.LIGHT_GRAY);
        }
    }

    public static void main(String[] args) throws IOException {
        new TeamMaker("Team Maker - Bombeiros Voluntarios de Anadia");
    }
}