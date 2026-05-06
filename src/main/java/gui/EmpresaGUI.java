package gui;

import dao.AccesoTrabajador;
import dialogs.AltaDialog;
import dialogs.BajaDialog;
import dialogs.ListarDialog;
import exceptions.BDException;
import ficheros.FicheroDatos;
import modelo.Empresa;
import modelo.Trabajador;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EmpresaGUI extends JFrame implements ActionListener {

    Empresa empresa;

    JButton altaTrabajador;
    JButton bajaTrabajador;
    JButton modificaTrabajador;
    JButton buscaTrabajador;
    JButton listarTrabajadores;
    JButton salir;

    AccesoTrabajador accesoTrabajador;

    public EmpresaGUI() {
        super("Gestión de personal");

        accesoTrabajador = new AccesoTrabajador();

        try {
            ArrayList<Trabajador> trabajadoresFichero =
                    FicheroDatos.obtenerTrabajadores("ficheroDatos\\empresa.dat");

            if (trabajadoresFichero != null && !trabajadoresFichero.isEmpty()) {
                accesoTrabajador.insertar(trabajadoresFichero);
            }

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar el fichero en la base de datos:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {
            System.out.println("No se pudo cargar el fichero empresa.dat: " + e.getMessage());
        }

        empresa = new Empresa(new ArrayList<Trabajador>());

        setSize(800, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));
        setLocationRelativeTo(null);

        altaTrabajador = new JButton("Añadir Trabajador");
        altaTrabajador.addActionListener(this);
        altaTrabajador.setIcon(cargarIcono("addUser.png"));
        add(altaTrabajador);

        bajaTrabajador = new JButton("Borrar Trabajador");
        bajaTrabajador.addActionListener(this);
        bajaTrabajador.setIcon(cargarIcono("removeUser.png"));
        add(bajaTrabajador);

        modificaTrabajador = new JButton("Modificar Trabajador");
        modificaTrabajador.addActionListener(this);
        modificaTrabajador.setIcon(cargarIcono("editUser.png"));
        add(modificaTrabajador);

        buscaTrabajador = new JButton("Buscar Trabajador");
        buscaTrabajador.addActionListener(this);
        buscaTrabajador.setIcon(cargarIcono("searchUser.png"));
        add(buscaTrabajador);

        listarTrabajadores = new JButton("Listar Trabajadores");
        listarTrabajadores.addActionListener(this);
        listarTrabajadores.setIcon(cargarIcono("list.png"));
        add(listarTrabajadores);

        salir = new JButton("Salir");
        salir.addActionListener(this);
        salir.setIcon(cargarIcono("exit.png"));
        add(salir);

        setVisible(true);
    }

    private ImageIcon cargarIcono(String nombreFichero) {
        java.net.URL url = getClass().getResource("/images/" + nombreFichero);

        if (url != null) {
            return new ImageIcon(url);
        }

        return new ImageIcon("images/" + nombreFichero);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == altaTrabajador) {
            new AltaDialog(empresa);

        } else if (e.getSource() == bajaTrabajador) {
            new BajaDialog(empresa);

        } else if (e.getSource() == modificaTrabajador) {
            modificarTrabajador();

        } else if (e.getSource() == buscaTrabajador) {
            buscarTrabajador();

        } else if (e.getSource() == listarTrabajadores) {
            new ListarDialog(empresa);

        } else if (e.getSource() == salir) {
            System.exit(0);
        }
    }

    private void buscarTrabajador() {
        try {
            String textoId = JOptionPane.showInputDialog(
                    this,
                    "Introduce el identificador del trabajador:"
            );

            if (textoId == null || textoId.trim().equals("")) {
                return;
            }

            int identificador = Integer.parseInt(textoId);

            Trabajador trabajador = accesoTrabajador.buscar(identificador);

            if (trabajador == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existe ningún trabajador con ese identificador.",
                        "Buscar trabajador",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Identificador: " + trabajador.getIdentificador() + "\n" +
                                "DNI: " + trabajador.getDni() + "\n" +
                                "Nombre: " + trabajador.getNombre() + "\n" +
                                "Apellidos: " + trabajador.getApellidos() + "\n" +
                                "Dirección: " + trabajador.getDireccion() + "\n" +
                                "Teléfono: " + trabajador.getTelefono() + "\n" +
                                "Puesto: " + trabajador.getPuesto(),
                        "Trabajador encontrado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El identificador debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar el trabajador:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void modificarTrabajador() {
        try {
            String textoId = JOptionPane.showInputDialog(
                    this,
                    "Introduce el identificador del trabajador que quieres modificar:"
            );

            if (textoId == null || textoId.trim().equals("")) {
                return;
            }

            int identificador = Integer.parseInt(textoId);

            Trabajador trabajador = accesoTrabajador.buscar(identificador);

            if (trabajador == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existe ningún trabajador con ese identificador.",
                        "Modificar trabajador",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String dni = JOptionPane.showInputDialog(this, "DNI:", trabajador.getDni());
            if (dni == null) {
                return;
            }

            String nombre = JOptionPane.showInputDialog(this, "Nombre:", trabajador.getNombre());
            if (nombre == null) {
                return;
            }

            String apellidos = JOptionPane.showInputDialog(this, "Apellidos:", trabajador.getApellidos());
            if (apellidos == null) {
                return;
            }

            String direccion = JOptionPane.showInputDialog(this, "Dirección:", trabajador.getDireccion());
            if (direccion == null) {
                return;
            }

            String telefono = JOptionPane.showInputDialog(this, "Teléfono:", trabajador.getTelefono());
            if (telefono == null) {
                return;
            }

            String puesto = JOptionPane.showInputDialog(this, "Puesto:", trabajador.getPuesto());
            if (puesto == null) {
                return;
            }

            Trabajador trabajadorModificado = new Trabajador(
                    identificador,
                    dni,
                    nombre,
                    apellidos,
                    direccion,
                    telefono,
                    puesto
            );

            boolean modificado = accesoTrabajador.actualizar(trabajadorModificado);

            if (modificado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Trabajador modificado correctamente.",
                        "Modificar trabajador",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se ha podido modificar el trabajador.",
                        "Modificar trabajador",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El identificador debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (BDException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al modificar el trabajador:\n" + e.getMessage(),
                    "Error BD",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new EmpresaGUI();
    }
}