import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Profile extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField o;
    private JTextField q = new JTextField("<Numero Mecanografico>");
    private JComboBox<String> r = new JComboBox<String>(), s = new JComboBox<String>(), p = new JComboBox<String>();
    private JCheckBox u = new JCheckBox("TAS"), v = new JCheckBox("Averbamento grupo 2"),
            w = new JCheckBox("Carta pesados");
    private JButton sw = new JButton("Guardar"), cc = new JButton("Cancelar");

    public Profile(Firefighter a, Database z) {
        windowMaker();
        this.setTitle("Perfil - " + a.getName());
        o.setText(a.getName());
        q.setText(String.valueOf(a.getN_mec()));

        for (int i = 0; i < 9; i++) {
            if (i < 3) {
                if (r.getItemAt(i) == a.getQuadro().getValue()) {
                    r.setSelectedIndex(i);
                }
                if (s.getItemAt(i) == a.getEstado().getValue()) {
                    s.setSelectedIndex(i);
                }
            }
            if (p.getItemAt(i) == a.getGrad().getValue()) {
                p.setSelectedIndex(i);
            }
        }

        u.setSelected(a.getTas());
        v.setSelected(a.getLigeiro());
        w.setSelected(a.getPesado());

        // Write new information if something has changed ->
        sw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                a.setName(o.getText());
                a.setGrad(Graduacao.values()[p.getSelectedIndex() - 1]);
                for (char x : q.getText().toCharArray()) {
                    if (!Character.isDigit(x)) {
                        q.setText("0");
                    }
                }
                a.setN_mec(Integer.parseInt(q.getText()));
                a.setQuadro(Quadro.values()[r.getSelectedIndex() - 1]);
                a.setEstado(Estado.values()[s.getSelectedIndex() - 1]);
                a.setTas(u.isSelected());
                a.setLigeiro(v.isSelected());
                a.setPesado(w.isSelected());
                z.refreshElems();
                dispose();
            }
        });
        // <- Write new information if something has changed

        // Cancel ->
        cc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                z.refreshElems();
                dispose();
            }
        });
        // <- Cancel

        this.setVisible(true);
    }

    public Profile(Database z) {
        windowMaker();
        sw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (char x : q.getText().toCharArray()) {
                    if (!Character.isDigit(x)) {
                        q.setText("0");
                    }
                }
                new Firefighter(o.getText(), Integer.parseInt(q.getText()),
                        Graduacao.values()[p.getSelectedIndex() - 1], u.isSelected(), v.isSelected(), w.isSelected(),
                        Quadro.values()[r.getSelectedIndex() - 1], Estado.values()[s.getSelectedIndex() - 1]);
                z.refreshElems();
                dispose();
            }
        });
        cc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.setVisible(true);
    }

    private void windowMaker() {
        setLocationRelativeTo(null);
        setTitle("Perfil");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 1));

        // Name and graduation
        o = new JTextField("<Nome>");
        o.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        p.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        p.addItem("<Graduacao>");
        for (Graduacao i : Graduacao.values()) {
            p.addItem(i.getValue());
        }

        // Top set of boxes
        r.addItem("<Quadro>");
        r.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        s.addItem("<Estado>");
        s.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        for (int i = 0; i < 3; i++) {
            r.addItem(Quadro.values()[i].getValue());
            s.addItem(Estado.values()[i].getValue());
        }
        r.setSelectedIndex(0);
        s.setSelectedIndex(0);

        // Bottom set of boxe
        JPanel h = new JPanel(new FlowLayout());
        h.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        h.add(u);
        h.add(v);
        h.add(w);

        JPanel bt = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bt.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        bt.add(sw);
        bt.add(cc);

        JPanel a1 = new JPanel(new FlowLayout());
        a1.add(o);
        JPanel a2 = new JPanel(new FlowLayout());
        a2.add(p);
        JPanel a3 = new JPanel(new FlowLayout());
        q.setPreferredSize(new Dimension((int) (this.getWidth() * 0.9), 25));
        a3.add(q);
        JPanel a4 = new JPanel(new FlowLayout());
        a4.add(r);
        JPanel a5 = new JPanel(new FlowLayout());
        a5.add(s);

        this.add(a1);
        this.add(a2);
        this.add(a3);
        this.add(a4);
        this.add(a5);
        this.add(h);
        this.add(bt);
    }
}