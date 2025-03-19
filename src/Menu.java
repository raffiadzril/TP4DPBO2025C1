import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(400, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.WHITE);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JRadioButton c1RadioButton;
    private JRadioButton c2RadioButton;
    private JRadioButton aRadioButton;
    private JRadioButton bRadioButton;

    JRadioButton[] kelasRadioButton = {
            c1RadioButton,
            c2RadioButton,
            aRadioButton,
            bRadioButton
    };
    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();


        // isi listMahasiswa
        populateList();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));


        // atur isi combo box
        String[] jenisKelamin = {"Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelamin));

        // atur radio button kelas
        String[] kelas = {"C1", "C2", "A", "B"};
        ButtonGroup groupKelas = new ButtonGroup();
        groupKelas.add(c1RadioButton);
        groupKelas.add(c2RadioButton);
        groupKelas.add(aRadioButton);
        groupKelas.add(bRadioButton);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Hapus data?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteData();
                    }
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedKelas = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();
                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                // pilih radio button yang sesuai dengan selectedKelas
                for (JRadioButton rb : kelasRadioButton) {
                    if (rb.getText().equals(selectedKelas)) {
                        rb.setSelected(true);
                        break;
                    }
                }



                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] columns = {"No", "NIM", "Nama", "Jenis Kelamin", "Kelas"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, columns);


        // isi tabel dengan listMahasiswa
        for(int i = 0; i < listMahasiswa.size(); i++) {
            Object[] row = new Object[5];
            row[0] = i + 1;
            row[1] = listMahasiswa.get(i).getNim();
            row[2] = listMahasiswa.get(i).getNama();
            row[3] = listMahasiswa.get(i).getJenisKelamin();
            row[4] = listMahasiswa.get(i).getKelas();
            temp.addRow(row);
        }

        return temp;
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String kelas = "";
        for (JRadioButton rb : kelasRadioButton) {
            if (rb.isSelected()) {
                kelas = rb.getText();
                break;
            }
        }


        // tambahkan data ke dalam list
        listMahasiswa.add(new Mahasiswa(nim, nama, jenisKelamin, kelas));

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert data berhasil");
        JOptionPane.showMessageDialog(null, "Insert data berhasil");

    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String kelas = "";
        for (JRadioButton rb : kelasRadioButton) {
            if (rb.isSelected()) {
                kelas = rb.getText();
                break;
            }
        }

        // ubah data mahasiswa di list
        listMahasiswa.get(selectedIndex).setNim(nim);
        listMahasiswa.get(selectedIndex).setNama(nama);
        listMahasiswa.get(selectedIndex).setJenisKelamin(jenisKelamin);
        listMahasiswa.get(selectedIndex).setKelas(kelas);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update data berhasil");
        JOptionPane.showMessageDialog(null, "Update data berhasil");

    }

    public void deleteData() {
        // hapus data dari list
        listMahasiswa.remove(selectedIndex);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete data berhasil");
        JOptionPane.showMessageDialog(null, "Delete data berhasil");
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedIndex(0);
        for (JRadioButton rb : kelasRadioButton) {
            rb.setSelected(false);
        }

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "C1"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "C2"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "C1"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "C2"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "A"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "B"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "C1"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "C2"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "B"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "C1"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "C2"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "B"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "C1"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "C2"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "A"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "B"));
    }
}
