package modelo;

import dao.AccesoTrabajador;
import exceptions.BDException;

import java.util.ArrayList;

public class Empresa {

    ArrayList<Trabajador> trabajadores;
    AccesoTrabajador accesoTrabajador;

    public Empresa(ArrayList<Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
        this.accesoTrabajador = new AccesoTrabajador();
    }

    public boolean esta(Trabajador t) {
        try {
            Trabajador trabajadorBD = accesoTrabajador.buscar(t.getIdentificador());
            return trabajadorBD != null;
        } catch (BDException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int devolverPosicion(int codigo) {
        for (int i = 0; i < trabajadores.size(); i++) {
            if (trabajadores.get(i).getIdentificador() == codigo) {
                return i;
            }
        }
        return -1;
    }

    public boolean altaTrabajador(Trabajador t) {
        try {
            return accesoTrabajador.insertar(t);
        } catch (BDException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bajaTrabajador(int codigo) {
        try {
            return accesoTrabajador.borrar(codigo);
        } catch (BDException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Trabajador buscarTrabajador(int codigo) {
        try {
            return accesoTrabajador.buscar(codigo);
        } catch (BDException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean modificarTrabajador(int codigo, String dni, String nombre, String apellidos,
                                       String direccion, String telefono, String puesto) {
        Trabajador trabajador = new Trabajador(
                codigo,
                dni,
                nombre,
                apellidos,
                direccion,
                telefono,
                puesto
        );

        try {
            return accesoTrabajador.actualizar(trabajador);
        } catch (BDException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[][] listarTrabajadores() {
        try {
            ArrayList<Trabajador> trabajadoresBD = accesoTrabajador.listar();

            String[][] datos = new String[trabajadoresBD.size()][7];

            for (int i = 0; i < trabajadoresBD.size(); i++) {
                datos[i][0] = Integer.toString(trabajadoresBD.get(i).getIdentificador());
                datos[i][1] = trabajadoresBD.get(i).getDni();
                datos[i][2] = trabajadoresBD.get(i).getNombre();
                datos[i][3] = trabajadoresBD.get(i).getApellidos();
                datos[i][4] = trabajadoresBD.get(i).getDireccion();
                datos[i][5] = trabajadoresBD.get(i).getTelefono();
                datos[i][6] = trabajadoresBD.get(i).getPuesto();
            }

            return datos;

        } catch (BDException e) {
            e.printStackTrace();
            return new String[0][7];
        }
    }

    public ArrayList<Trabajador> getTrabajadores() {
        try {
            return accesoTrabajador.listar();
        } catch (BDException e) {
            e.printStackTrace();
            return trabajadores;
        }
    }

    public void setTrabajadores(ArrayList<Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
    }
}