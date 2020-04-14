import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;

public class Database extends JFrame implements ActionListener, MouseInputListener {

    private JButton add_elem = new JButton("Adicionar Elemento");
    private JButton pf = new JButton("Perfil");
    private JButton rm_elem = new JButton("Remover Elemento");
    private JButton ed_db = new JButton("Alterar Base de Dados");
    private JTable table;
    private TeamMaker z;
    private static int[] minimums = { 0, 0, 0, 0 }; // pesados, ligeiros, tas, estagiarios

    public Database(TeamMaker z) {
        this.z = z;
        windowmaker();
    }

    // Reads CSV file and adds elements ->
    private void newDatabase(String file) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(file));
        for (int i = 1; i < lines.size(); i++) {
            String x = lines.get(i);
            // Name, Mecanografico, Graduacao, TAS, Ligeiro, Pesado, Quadro, Estado
            Graduacao d = null;
            boolean a, b, c;
            Quadro f = null;
            Estado e = null;
            for (Graduacao g : Graduacao.values()) {
                if (x.split(",")[2].trim().equals(g.getValue())) {
                    d = g;
                    if (g == Graduacao.ESTAGIARIO) {
                        minimums[3]++;
                    }
                }
            }
            a = (x.split(",")[3].trim().toUpperCase().equals("TRUE") || x.split(",")[3].trim().equals("1")) ? true
                    : false;
            if (a) {
                minimums[2]++;
            }
            b = (x.split(",")[4].trim().toUpperCase().equals("TRUE") || x.split(",")[4].trim().equals("1")) ? true
                    : false;
            if (b) {
                minimums[1]++;
            }
            c = (x.split(",")[5].trim().toUpperCase().equals("TRUE") || x.split(",")[5].trim().equals("1")) ? true
                    : false;
            if (c) {
                minimums[0]++;
            }
            for (Quadro g : Quadro.values()) {
                if (x.split(",")[6].trim().equals(g.getValue())) {
                    f = g;
                }
            }
            for (Estado g : Estado.values()) {
                if (x.split(",")[7].trim().equals(g.getValue())) {
                    e = g;
                }
            }
            new Firefighter(x.split(",")[0].trim(), Integer.parseInt(x.split(",")[1].trim()), d, a, b, c, f, e);
        }
        refreshElems();
        numberDialog();
        z.newDatabase(true);
    }
    // <- Reads CSV file and adds elements

    // File chooser to retrieve CSV file ->
    private String filechooser() {
        String x = "";
        File c = null;
        while (!x.equals(".csv")) {
            JFileChooser b = new JFileChooser();
            if (b.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                c = b.getSelectedFile();
                x = c.getName().substring(c.getName().length() - 4);
            } else {
                return null;
            }
        }
        return c.getPath();
    }
    // <- File chooser to retrieve CSV file

    // User input for number or teams ->
    private void numberDialog() {
        JFrame a = new JFrame();
        a.setLayout(null);
        a.setSize(600, 600);
        String s = null;
        boolean c = true;
        s = JOptionPane.showInputDialog(a, "Numero de equipas", "Equipas", JOptionPane.QUESTION_MESSAGE);
        for (char x : s.toCharArray()) {
            if (!Character.isDigit(x)) {
                c = false;
                break;
            }
        }
        while (s.equals("") || !c || Integer.parseInt(s) > Firefighter.getAll().size()) {
            s = JOptionPane.showInputDialog(a, "Numero de equipas\n", "Numero de equipas",
                    JOptionPane.QUESTION_MESSAGE);
            c = true;
            for (char x : s.toCharArray()) {
                if (!Character.isDigit(x)) {
                    c = false;
                    break;
                }
            }
        }
        Team.setNumberOfTeams(Integer.parseInt(s));
    }
    // <- User input for number or teams

    // Refreshes the list of firefighter on the database window ->
    public void refreshElems() {
        DefaultTableModel table_model = ((DefaultTableModel) table.getModel());
        table_model.setRowCount(0);
        for (Firefighter x : Firefighter.getAll()) {
            table_model.addRow(new Object[] { x.getId(), x.getName(), x.getN_mec(), x.getGrad().getValue(), x.getTas(),
                    x.getLigeiro(), x.getPesado(), x.getQuadro(), x.getEstado() });
        }
        if (Firefighter.getAll().size() != 0) {
            pf.setEnabled(true);
        }
        if (Firefighter.getAll().size() != 0) {
            rm_elem.setEnabled(true);
        }
    }
    // <- Refreshes the list of firefighter on the database window

    // Replace database ->
    private boolean replaceDatabase() {
        JFrame a = new JFrame();
        a.setLayout(null);
        a.setSize(600, 600);
        int s = JOptionPane.showConfirmDialog(a, "Tem a certeza que deseja substituir a base de dados atual ?\n",
                "Atencao!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (s == JOptionPane.YES_OPTION) {
            clearDatabase();
            return true;
        } else {
            return false;
        }
    }
    // <- Replace database

    // Clears the database ->
    private void clearDatabase() {
        Firefighter.clear();
        Team.clear();
        ((DefaultTableModel) table.getModel()).setRowCount(0);
        z.newDatabase(false);
    }
    // <- Clears the database

    private void windowmaker() {
        // Edit firefighter database window frame
        ImageIcon img = new ImageIcon("icon.png");
        setIconImage(img.getImage());
        setTitle("Elementos");
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize((int) (scrSize.getWidth() / 1.4), (int) ((scrSize.getHeight() / 1.5)));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // JTabel list of all elements of the database
        String[] campos = { "id", "Nome", "Numero Mec.", "Graduacao", "Certificacao TAS", "Averbamento grupo 2",
                "Carta pesados", "Quadro", "Estado" };
        DefaultTableModel table_model = new DefaultTableModel(campos, 0);
        table = new JTable(table_model);
        table.setDefaultEditor(Object.class, null);
        JScrollPane table_scroll = new JScrollPane(table);
        if (Firefighter.getAll().size() != 0) {
            for (Firefighter x : Firefighter.getAll()) {
                table_model.addRow(new Object[] { x.getId(), x.getName(), x.getN_mec(), x.getGrad().getValue(),
                        x.getTas(), x.getLigeiro(), x.getPesado(), x.getQuadro(), x.getEstado() });
            }
        }

        add_elem.addActionListener(this);
        add_elem.addMouseListener(this);
        add_elem.setBackground(new Color(238, 238, 238));
        pf.addActionListener(this);
        pf.addMouseListener(this);
        pf.setBackground(new Color(238, 238, 238));
        if (Firefighter.getAll().size() == 0) {
            pf.setEnabled(false);
        } else {
            pf.setEnabled(true);
        }

        rm_elem.addActionListener(this);
        rm_elem.addMouseListener(this);
        rm_elem.setBackground(new Color(238, 238, 238));
        if (Firefighter.getAll().size() == 0) {
            rm_elem.setEnabled(false);
        } else {
            rm_elem.setEnabled(true);
        }

        ed_db.addActionListener(this);
        ed_db.addMouseListener(this);
        ed_db.setBackground(new Color(238, 238, 238));

        JPanel elems_buttons = new JPanel(new GridLayout(1, 4));
        elems_buttons.add(add_elem);
        elems_buttons.add(pf);
        elems_buttons.add(rm_elem);
        elems_buttons.add(ed_db);

        add(table_scroll, BorderLayout.CENTER);
        add(elems_buttons, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        String c = b.getText();
        switch (c) {
            case "Perfil":
                int w = (int) table.getValueAt(table.getSelectedRow(), 0);
                Firefighter a = null;
                for (int i = 0; i < Firefighter.getAll().size(); i++) {
                    if (Firefighter.getAll().get(i).getId() == w) {
                        a = Firefighter.getAll().get(i);
                    }
                }
                new Profile(a, this);

                break;
            case "Adicionar Elemento":
                new Profile(this);

                break;
            case "Remover Elemento":
                int y = (int) table.getValueAt(table.getSelectedRow(), 0);
                for (int i = 0; i < Firefighter.getAll().size(); i++) {
                    if (Firefighter.getAll().get(i).getId() == y) {
                        Firefighter.clear(Firefighter.getAll().get(i));
                    }
                }
                ((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());

                break;
            case "Alterar Base de Dados":
                if (Firefighter.getAll().size() != 0) {
                    replaceDatabase();
                }
                String x = filechooser();
                if (!x.equals(null)) {
                    try {
                        newDatabase(x);
                        refreshElems();

                    } catch (IOException e1) {
                        break;
                    }
                }
                break;
        }
    }

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
}