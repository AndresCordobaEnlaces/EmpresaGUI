package dialogs;

import dao.AccesoTrabajador;
import exceptions.BDException;
import modelo.Empresa;
import modelo.Trabajador;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author Andrés Córdoba
 *
 */
public class ListarDialog extends JDialog implements ActionListener {

    Empresa empresa;
    AccesoTrabajador accesoTrabajador;

    JTable tabla;
    DefaultTableModel modeloTabla;
    TableRowSorter<DefaultTableModel> ordenador;

    JButton modificar;
    JButton exportarCSV;
    JButton exportarJSON;
    JButton cerrar;

    boolean permitirModificar;

    public ListarDialog(Empresa empresa) {
        this(empresa, false);
    }

    public ListarDialog(Empresa empresa, boolean permitirModificar) {
        this.empresa = empresa;
        this.permitirModificar = permitirModificar;
        this.accesoTrabajador = new AccesoTrabajador();

        setResizable(false);
        setTitle("Listado Trabajadores");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

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

        if (permitirModificar) {
            modificar = new JButton("Modificar seleccionado");
            modificar.addActionListener(this);
            panelBotones.add(modificar);
        }

        exportarCSV = new JButton("Exportar CSV");
        exportarCSV.addActionListener(this);
        panelBotones.add(exportarCSV);

        exportarJSON = new JButton("Exportar JSON");
        exportarJSON.addActionListener(this);
        panelBotones.add(exportarJSON);

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        panelBotones.add(cerrar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        try {
            ArrayList<Trabajador> trabajadores = accesoTrabajador.listar();

            for (Trabajador trabajador : trabajadores) {
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

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los trabajadores:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void modificarTrabajadorSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un trabajador de la tabla.",
                    "Modificar trabajador",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
        int identificador = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());

        try {
            Trabajador trabajador = accesoTrabajador.buscar(identificador);

            if (trabajador == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se ha encontrado el trabajador en la base de datos.",
                        "Modificar trabajador",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            ModificarDialog modificarDialog = new ModificarDialog(this, trabajador);

            if (modificarDialog.isModificado()) {
                cargarDatos();
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

    private void guardarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo CSV", "csv"));

        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File fichero = asegurarExtension(fileChooser.getSelectedFile(), ".csv");

            try {
                exportarCSV(fichero);

                JOptionPane.showMessageDialog(
                        this,
                        "Fichero CSV exportado correctamente.",
                        "Exportación correcta",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al exportar CSV:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void guardarJSON() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar JSON");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo JSON", "json"));

        int opcion = fileChooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File fichero = asegurarExtension(fileChooser.getSelectedFile(), ".json");

            try {
                exportarJSON(fichero);

                JOptionPane.showMessageDialog(
                        this,
                        "Fichero JSON exportado correctamente.",
                        "Exportación correcta",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al exportar JSON:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private File asegurarExtension(File fichero, String extension) {
        if (!fichero.getName().toLowerCase().endsWith(extension)) {
            return new File(fichero.getAbsolutePath() + extension);
        }

        return fichero;
    }

    private void exportarCSV(File fichero) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(fichero.toPath(), StandardCharsets.UTF_8)) {

            for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                bw.write(escaparCSV(modeloTabla.getColumnName(i)));

                if (i < modeloTabla.getColumnCount() - 1) {
                    bw.write(";");
                }
            }

            bw.newLine();

            for (int filaVista = 0; filaVista < tabla.getRowCount(); filaVista++) {
                int filaModelo = tabla.convertRowIndexToModel(filaVista);

                for (int columna = 0; columna < modeloTabla.getColumnCount(); columna++) {
                    Object valor = modeloTabla.getValueAt(filaModelo, columna);
                    bw.write(escaparCSV(String.valueOf(valor)));

                    if (columna < modeloTabla.getColumnCount() - 1) {
                        bw.write(";");
                    }
                }

                bw.newLine();
            }
        }
    }

    private String escaparCSV(String texto) {
        if (texto == null) {
            return "";
        }

        String textoEscapado = texto.replace("\"", "\"\"");

        if (textoEscapado.contains(";") || textoEscapado.contains("\"")
                || textoEscapado.contains("\n") || textoEscapado.contains("\r")) {
            return "\"" + textoEscapado + "\"";
        }

        return textoEscapado;
    }

    private void exportarJSON(File fichero) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(fichero.toPath(), StandardCharsets.UTF_8)) {

            bw.write("[");
            bw.newLine();

            for (int filaVista = 0; filaVista < tabla.getRowCount(); filaVista++) {
                int filaModelo = tabla.convertRowIndexToModel(filaVista);

                bw.write("  {");
                bw.newLine();

                bw.write("    \"identificador\": " + modeloTabla.getValueAt(filaModelo, 0) + ",");
                bw.newLine();
                bw.write("    \"dni\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 1))) + "\",");
                bw.newLine();
                bw.write("    \"nombre\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 2))) + "\",");
                bw.newLine();
                bw.write("    \"apellidos\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 3))) + "\",");
                bw.newLine();
                bw.write("    \"direccion\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 4))) + "\",");
                bw.newLine();
                bw.write("    \"telefono\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 5))) + "\",");
                bw.newLine();
                bw.write("    \"puesto\": \"" + escaparJSON(String.valueOf(modeloTabla.getValueAt(filaModelo, 6))) + "\"");
                bw.newLine();

                if (filaVista < tabla.getRowCount() - 1) {
                    bw.write("  },");
                } else {
                    bw.write("  }");
                }

                bw.newLine();
            }

            bw.write("]");
        }
    }

    private String escaparJSON(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (permitirModificar && e.getSource() == modificar) {
            modificarTrabajadorSeleccionado();

        } else if (e.getSource() == exportarCSV) {
            guardarCSV();

        } else if (e.getSource() == exportarJSON) {
            guardarJSON();

        } else if (e.getSource() == cerrar) {
            dispose();
        }
    }
}