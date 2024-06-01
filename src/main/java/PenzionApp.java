import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PenzionApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Osoba> osoby = new ArrayList<>();
    private List<Pokoj> pokoje = new ArrayList<>();

    public PenzionApp() {
        createGUI();
        initPokoje();
    }

    private void createGUI() {
        frame = new JFrame("Penzion App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Jméno", "Příjmení", "Číslo OP", "Adresa", "Číslo pokoje"}, 0);
        table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addButton = new JButton("Přidat osobu");
        addButton.addActionListener(new AddPersonActionListener());
        panel.add(addButton, BorderLayout.NORTH);

        JButton removeButton = new JButton("Odebrat osobu");
        removeButton.addActionListener(new RemovePersonActionListener());
        panel.add(removeButton, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void initPokoje() {
        for (int i = 0; i < 10; i++) {
            Pokoj pokoj = new Pokoj(i + 1, 3);
            pokoje.add(pokoj);
        }
    }

    private class AddPersonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jmeno = JOptionPane.showInputDialog("Zadejte jméno:");
            String prijmeni = JOptionPane.showInputDialog("Zadejte příjmení:");
            String cisloOP = JOptionPane.showInputDialog("Zadejte číslo OP:");
            String adresa = JOptionPane.showInputDialog("Zadejte adresu:");
            int pokojId = Integer.parseInt(JOptionPane.showInputDialog("Zadejte číslo pokoje (1-10):"));

            Pokoj pokoj = pokoje.get(pokojId - 1);
            if (pokoj.hasFreeSpace()) {
                Osoba osoba = new Osoba(jmeno, prijmeni, cisloOP, adresa, pokoj);
                osoby.add(osoba);
                tableModel.addRow(new Object[]{jmeno, prijmeni, cisloOP, adresa, pokojId});
                pokoj.addOsoba(osoba);
            } else {
                JOptionPane.showMessageDialog(frame, "Pokoj je plný!");
            }
        }
    }

    private class RemovePersonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow!= -1) {
                Osoba osoba = osoby.get(selectedRow);
                osoby.remove(osoba);
                tableModel.removeRow(selectedRow);
                osoba.getPokoj().removeOsoba(osoba);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PenzionApp();
            }
        });
    }
}

class Osoba {
    private String jmeno;
    private String prijmeni;
    private String cisloOP;
    private String adresa;
    private Pokoj pokoj;

    public Osoba(String jmeno, String prijmeni, String cisloOP, String adresa, Pokoj pokoj) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.cisloOP = cisloOP;
        this.adresa = adresa;
        this.pokoj = pokoj;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getCisloOP() {
        return cisloOP;
    }

    public String getAdresa() {
        return adresa;
    }

    public Pokoj getPokoj() {
        return pokoj;
    }
}

class Pokoj {
    private int id;
    private int capacity;
    private List<Osoba> osoby = new ArrayList<>();

    public Pokoj(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public boolean hasFreeSpace() {
        return osoby.size() < capacity;
    }

    public void addOsoba(Osoba osoba) {
        osoby.add(osoba);
    }

    public void removeOsoba(Osoba osoba) {
        osoby.remove(osoba);
    }
}