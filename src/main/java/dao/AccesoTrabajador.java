package dao;

import config.ConfigMySQL;
import exceptions.BDException;
import modelo.Trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccesoTrabajador {

    public boolean insertar(Trabajador trabajador) throws BDException {
        if (trabajador.getIdentificador() > 0) {
            return insertarConIdentificador(trabajador);
        } else {
            return insertarSinIdentificador(trabajador);
        }
    }

    private boolean insertarConIdentificador(Trabajador trabajador) throws BDException {
        String sql = "INSERT INTO trabajadores " +
                "(identificador, dni, nombre, apellidos, direccion, telefono, puesto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, trabajador.getIdentificador());
            sentencia.setString(2, trabajador.getDni());
            sentencia.setString(3, trabajador.getNombre());
            sentencia.setString(4, trabajador.getApellidos());
            sentencia.setString(5, trabajador.getDireccion());
            sentencia.setString(6, trabajador.getTelefono());
            sentencia.setString(7, trabajador.getPuesto());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    private boolean insertarSinIdentificador(Trabajador trabajador) throws BDException {
        String sql = "INSERT INTO trabajadores " +
                "(dni, nombre, apellidos, direccion, telefono, puesto) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, trabajador.getDni());
            sentencia.setString(2, trabajador.getNombre());
            sentencia.setString(3, trabajador.getApellidos());
            sentencia.setString(4, trabajador.getDireccion());
            sentencia.setString(5, trabajador.getTelefono());
            sentencia.setString(6, trabajador.getPuesto());

            int filas = sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    trabajador.setIdentificador(claves.getInt(1));
                }
            }

            return filas > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public void insertar(ArrayList<Trabajador> trabajadores) throws BDException {
        if (trabajadores == null) {
            return;
        }

        for (Trabajador trabajador : trabajadores) {
            try {
                insertar(trabajador);

            } catch (BDException e) {
                boolean actualizado = false;

                try {
                    actualizado = actualizar(trabajador);
                } catch (BDException e2) {
                    actualizado = false;
                }

                if (!actualizado) {
                    actualizarPorDni(trabajador);
                }
            }
        }
    }

    public boolean actualizar(Trabajador trabajador) throws BDException {
        String sql = "UPDATE trabajadores SET " +
                "dni = ?, " +
                "nombre = ?, " +
                "apellidos = ?, " +
                "direccion = ?, " +
                "telefono = ?, " +
                "puesto = ? " +
                "WHERE identificador = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, trabajador.getDni());
            sentencia.setString(2, trabajador.getNombre());
            sentencia.setString(3, trabajador.getApellidos());
            sentencia.setString(4, trabajador.getDireccion());
            sentencia.setString(5, trabajador.getTelefono());
            sentencia.setString(6, trabajador.getPuesto());
            sentencia.setInt(7, trabajador.getIdentificador());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public boolean actualizarPorDni(Trabajador trabajador) throws BDException {
        String sql = "UPDATE trabajadores SET " +
                "nombre = ?, " +
                "apellidos = ?, " +
                "direccion = ?, " +
                "telefono = ?, " +
                "puesto = ? " +
                "WHERE dni = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, trabajador.getNombre());
            sentencia.setString(2, trabajador.getApellidos());
            sentencia.setString(3, trabajador.getDireccion());
            sentencia.setString(4, trabajador.getTelefono());
            sentencia.setString(5, trabajador.getPuesto());
            sentencia.setString(6, trabajador.getDni());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public boolean borrar(int identificador) throws BDException {
        String sql = "DELETE FROM trabajadores WHERE identificador = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, identificador);

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public Trabajador buscar(int identificador) throws BDException {
        String sql = "SELECT identificador, dni, nombre, apellidos, direccion, telefono, puesto " +
                "FROM trabajadores WHERE identificador = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, identificador);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return crearTrabajador(resultado);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public Trabajador buscarPorDni(String dni) throws BDException {
        String sql = "SELECT identificador, dni, nombre, apellidos, direccion, telefono, puesto " +
                "FROM trabajadores WHERE dni = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, dni);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return crearTrabajador(resultado);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public boolean existeDni(String dni) throws BDException {
        String sql = "SELECT COUNT(*) FROM trabajadores WHERE dni = ?";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, dni);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }

            return false;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    public ArrayList<Trabajador> listar() throws BDException {
        ArrayList<Trabajador> trabajadores = new ArrayList<>();

        String sql = "SELECT identificador, dni, nombre, apellidos, direccion, telefono, puesto " +
                "FROM trabajadores ORDER BY identificador";

        try (Connection conexion = ConfigMySQL.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                trabajadores.add(crearTrabajador(resultado));
            }

            return trabajadores;

        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
    }

    private Trabajador crearTrabajador(ResultSet resultado) throws SQLException {
        return new Trabajador(
                resultado.getInt("identificador"),
                resultado.getString("dni"),
                resultado.getString("nombre"),
                resultado.getString("apellidos"),
                resultado.getString("direccion"),
                resultado.getString("telefono"),
                resultado.getString("puesto")
        );
    }
}