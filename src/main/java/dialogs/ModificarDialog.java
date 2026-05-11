package dialogs;

import dao.AccesoTrabajador;
import exceptions.BDException;
import modelo.Trabajador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Andrés Córdoba
 *
 */
public class ModificarDialog extends JDialog implements ActionListener {

    JLabel etiquetaIdentificador;
    JTextField areaIdentificador;

    JLabel etiquetaDni;
    JTextField areaDni;

    JLabel etiquetaNombre;
    JTextField areaNombre;

    JLabel etiquetaApellidos;
    JTextField areaApellidos;

    JLabel etiquetaDireccion;
    JTextField areaDireccion;

    JLabel etiquetaTelefono;
    JTextField areaTelefono;

    JLabel etiquetaPuesto;
    JComboBox<String> comboPuesto;

    JButton aceptar;
    JButton cancelar;

    JPanel pIdentificador;
    JPanel pDni;
    JPanel pNombre;
    JPanel pApellidos;
    JPanel pDireccion;
    JPanel pTelefono;
    JPanel pPuesto;
    JPanel pBotones;

    Trabajador trabajador;
    AccesoTrabajador accesoTrabajador;

    boolean modificado = false;

    public ModificarDialog(JDialog padre, Trabajador trabajador) {
        super(padre, "Modificar Trabajador", true);

        this.trabajador = trabajador;
        this.accesoTrabajador = new AccesoTrabajador();

        setResizable(false);
        setSize(350, 380);
        setLayout(new FlowLayout());
        setLocationRelativeTo(padre);

        pIdentificador = new JPanel();
        pDni = new JPanel();
        pNombre = new JPanel();
        pApellidos = new JPanel();
        pDireccion = new JPanel();
        pTelefono = new JPanel();
        pPuesto = new JPanel();
        pBotones = new JPanel();

        etiquetaIdentificador = new JLabel("Identificador");
        areaIdentificador = new JTextField(15);
        areaIdentificador.setText(String.valueOf(trabajador.getIdentificador()));
        areaIdentificador.setEditable(false);
        pIdentificador.add(etiquetaIdentificador);
        pIdentificador.add(areaIdentificador);

        etiquetaDni = new JLabel("DNI                 ");
        areaDni = new JTextField(15);
        areaDni.setText(trabajador.getDni());
        pDni.add(etiquetaDni);
        pDni.add(areaDni);

        etiquetaNombre = new JLabel("Nombre         ");
        areaNombre = new JTextField(15);
        areaNombre.setText(trabajador.getNombre());
        pNombre.add(etiquetaNombre);
        pNombre.add(areaNombre);

        etiquetaApellidos = new JLabel("Apellidos      ");
        areaApellidos = new JTextField(15);
        areaApellidos.setText(trabajador.getApellidos());
        pApellidos.add(etiquetaApellidos);
        pApellidos.add(areaApellidos);

        etiquetaDireccion = new JLabel("Direccion      ");
        areaDireccion = new JTextField(15);
        areaDireccion.setText(trabajador.getDireccion());
        pDireccion.add(etiquetaDireccion);
        pDireccion.add(areaDireccion);

        etiquetaTelefono = new JLabel("Telefono       ");
        areaTelefono = new JTextField(15);
        areaTelefono.setText(trabajador.getTelefono());
        pTelefono.add(etiquetaTelefono);
        pTelefono.add(areaTelefono);

        etiquetaPuesto = new JLabel("Puesto                         ");
        comboPuesto = new JComboBox<>();

        comboPuesto.addItem("Programador");
        comboPuesto.addItem("Analista");
        comboPuesto.addItem("Arquitecto");
        comboPuesto.addItem("Jefe de Proyecto");

        if (!existePuesto(trabajador.getPuesto())) {
            comboPuesto.addItem(trabajador.getPuesto());
        }

        comboPuesto.setSelectedItem(trabajador.getPuesto());

        pPuesto.add(etiquetaPuesto);
        pPuesto.add(comboPuesto);

        add(pIdentificador);
        add(pDni);
        add(pNombre);
        add(pApellidos);
        add(pDireccion);
        add(pTelefono);
        add(pPuesto);

        aceptar = new JButton("Aceptar");
        aceptar.addActionListener(this);
        pBotones.add(aceptar);

        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        pBotones.add(cancelar);

        add(pBotones);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean existePuesto(String puesto) {
        for (int i = 0; i < comboPuesto.getItemCount(); i++) {
            if (comboPuesto.getItemAt(i).equals(puesto)) {
                return true;
            }
        }
        return false;
    }

    public boolean isModificado() {
        return modificado;
    }

    private boolean comprobarErrores(String dni, String nombre, String apellidos,
                                     String direccion, String telefono, String puesto) {

        if (dni.equals("") || dni.length() != 9) {
            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe tener longitud 9",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } else if (nombre.equals("")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir el nombre del trabajador",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } else if (apellidos.equals("")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir los apellidos del trabajador",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } else if (direccion.equals("")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir la dirección del trabajador",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } else if (telefono.equals("") || telefono.length() != 9) {
            JOptionPane.showMessageDialog(
                    this,
                    "El teléfono debe tener longitud 9",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } else if (puesto == null || puesto.equals("")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe introducir el puesto del trabajador",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aceptar) {
            int identificador = trabajador.getIdentificador();

            String dni = areaDni.getText().trim();
            String nombre = areaNombre.getText().trim();
            String apellidos = areaApellidos.getText().trim();
            String direccion = areaDireccion.getText().trim();
            String telefono = areaTelefono.getText().trim();
            String puesto = comboPuesto.getSelectedItem().toString();

            if (comprobarErrores(dni, nombre, apellidos, direccion, telefono, puesto)) {
                Trabajador trabajadorModificado = new Trabajador(
                        identificador,
                        dni,
                        nombre,
                        apellidos,
                        direccion,
                        telefono,
                        puesto
                );

                try {
                    boolean actualizado = accesoTrabajador.actualizar(trabajadorModificado);

                    if (actualizado) {
                        modificado = true;

                        JOptionPane.showMessageDialog(
                                this,
                                "Trabajador modificado correctamente.",
                                "Modificar trabajador",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "No se ha podido modificar el trabajador.",
                                "Modificar trabajador",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (BDException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error al modificar el trabajador:\n" + ex.getMessage(),
                            "Error BD",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } else if (e.getSource() == cancelar) {
            dispose();
        }
    }
}