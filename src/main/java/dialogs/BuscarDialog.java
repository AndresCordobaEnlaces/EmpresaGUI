package dialogs;

import dao.AccesoTrabajador;
import exceptions.BDException;
import modelo.Empresa;
import modelo.Trabajador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author Andrés Córdoba
 *
 */
public class BuscarDialog extends JDialog implements ActionListener {

    Empresa empresa;
    AccesoTrabajador accesoTrabajador;

    JTable tabla;
    DefaultTableModel modeloTabla;
    TableRowSorter<DefaultTableModel> ordenador;

    JComboBox<String> tipoBusqueda;
    JTextField campoBuscar;
    JButton buscar;
    JButton limpiar;
    JButton cerrar;

    public BuscarDialog(Empresa empresa) {
        this.empresa = empresa;
        this.accesoTrabajador = new AccesoTrabajador();

        setResizable(false);
        setTitle("Buscar Trabajador");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelBuscar = new JPanel(new FlowLayout());

        panelBuscar.add(new JLabel("Buscar por:"));

        tipoBusqueda = new JComboBox<>();
        tipoBusqueda.addItem("Todos los campos");
        tipoBusqueda.addItem("Identificador");
        tipoBusqueda.addItem("DNI");
        panelBuscar.add(tipoBusqueda);

        campoBuscar = new JTextField(25);
        campoBuscar.addActionListener(this);
        panelBuscar.add(campoBuscar);

        buscar = new JButton("Buscar");
        buscar.addActionListener(this);
        panelBuscar.add(buscar);

        limpiar = new JButton("Limpiar");
        limpiar.addActionListener(this);
        panelBuscar.add(limpiar);

        add(panelBuscar, BorderLayout.NORTH);

        String[] columnas = {
                "Identificador",
                "DNI",
                "Nombre",
                "Apellidos",
                "Dirección",
                "Teléfono",
                "Puesto"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tabla = new JTable(modeloTabla);

        ordenador = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(ordenador);

        JScrollPane jsp = new JScrollPane(tabla);
        add(jsp, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        panelBotones.add(cerrar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        ordenador.setRowFilter(null);

        try {
            ArrayList<Trabajador> trabajadores = accesoTrabajador.listar();

            for (Trabajador trabajador : trabajadores) {
                anadirTrabajadorTabla(trabajador);
            }

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los trabajadores:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarTrabajador() {
        String opcion = tipoBusqueda.getSelectedItem().toString();
        String texto = campoBuscar.getText().trim();

        if (opcion.equals("Todos los campos")) {
            buscarEnTodosLosCampos(texto);
            return;
        }

        if (texto.equals("")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Introduce un valor para buscar.",
                    "Buscar trabajador",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        modeloTabla.setRowCount(0);
        ordenador.setRowFilter(null);

        try {
            Trabajador trabajador = null;

            if (opcion.equals("Identificador")) {
                try {
                    int identificador = Integer.parseInt(texto);
                    trabajador = accesoTrabajador.buscar(identificador);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El identificador debe ser un número.",
                            "Buscar trabajador",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

            } else if (opcion.equals("DNI")) {
                trabajador = accesoTrabajador.buscarPorDni(texto);
            }

            if (trabajador == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se ha encontrado ningún trabajador.",
                        "Buscar trabajador",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                anadirTrabajadorTabla(trabajador);
            }

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar el trabajador:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarEnTodosLosCampos(String texto) {
        cargarDatos();

        if (!texto.equals("")) {
            ordenador.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private void anadirTrabajadorTabla(Trabajador trabajador) {
        Object[] fila = {
                trabajador.getIdentificador(),
                trabajador.getDni(),
                trabajador.getNombre(),
                trabajador.getApellidos(),
                trabajador.getDireccion(),
                trabajador.getTelefono(),
                trabajador.getPuesto()
        };

        modeloTabla.addRow(fila);
    }

    private void limpiarBusqueda() {
        campoBuscar.setText("");
        tipoBusqueda.setSelectedIndex(0);
        cargarDatos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buscar || e.getSource() == campoBuscar) {
            buscarTrabajador();

        } else if (e.getSource() == limpiar) {
            limpiarBusqueda();

        } else if (e.getSource() == cerrar) {
            dispose();
        }
    }
}