package gui;

import dao.AccesoTrabajador;
import dialogs.AltaDialog;
import dialogs.BajaDialog;
import dialogs.BuscarDialog;
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

/**
 *
 * @author Andrés Córdoba
 *
 */
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
            new ListarDialog(empresa, true);

        } else if (e.getSource() == buscaTrabajador) {
            new BuscarDialog(empresa);

        } else if (e.getSource() == listarTrabajadores) {
            new ListarDialog(empresa);

        } else if (e.getSource() == salir) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new EmpresaGUI();
    }
}