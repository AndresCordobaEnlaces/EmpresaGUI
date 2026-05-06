package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.BDException;

import javax.swing.*;

public class ConfigMySQL {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URLBD = "jdbc:mysql://localhost:3306/empresa?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "empresa_andres";
    private static final String CLAVE = "case";

    /**
     * Abre una conexion con la base de datos MySQL.
     *
     * @author Andrés Córdoba
     */
    public static Connection abrirConexion() throws BDException {
        Connection conexion = null;

        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URLBD, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            throw new BDException(BDException.ERROR_CARGAR_DRIVER + e.getMessage());
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        }

        return conexion;
    }

    /**
     * Cierra una conexion abierta.
     *
     * @author Andrés Córdoba
     */
    public static void cerrarConexion(Connection conexion) throws BDException {
        try {
            conexion.close();
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_CERRAR_CONEXION + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection conexion = null;

        try {
            conexion = abrirConexion();

            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexión correcta con la base de datos empresa.");
            } else {
                System.out.println("No se ha podido conectar a la base de datos.");
            }

        } catch (BDException e) {
            System.out.println("Error al conectar:");
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error SQL:");
            System.out.println(e.getMessage());

        } finally {
            try {
                if (conexion != null) {
                    cerrarConexion(conexion);
                    System.out.println("Conexión cerrada.");
                }
            } catch (BDException e) {
                System.out.println("Error al cerrar la conexión:");
                System.out.println(e.getMessage());
            }
        }
    }
}