package com.evol.marcosbr.util;

import java.util.Date;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Marcos Bayona Rijalba
 */
public class TablasH2 {

	public static void crearTablaMoneda(JdbcTemplate template) {
		String sqlTablaMoneda;

		sqlTablaMoneda = "DROP TABLE moneda IF EXISTS;";
		sqlTablaMoneda = "CREATE TABLE moneda(";
		sqlTablaMoneda = sqlTablaMoneda + "id INTEGER(11) PRIMARY KEY auto_increment, ";
		sqlTablaMoneda = sqlTablaMoneda + "nombre VARCHAR(50), ";
		sqlTablaMoneda = sqlTablaMoneda + "abreviatura VARCHAR(5), ";
		sqlTablaMoneda = sqlTablaMoneda + "jerarquia INTEGER(11), ";
        // Creamos los campos de auditoría
		sqlTablaMoneda = sqlTablaMoneda + "estado boolean DEFAULT true, ";
		sqlTablaMoneda = sqlTablaMoneda + "usuario_creacion VARCHAR(100), ";
		sqlTablaMoneda = sqlTablaMoneda + "fecha_creacion TIMESTAMP DEFAULT NOW(), ";
		sqlTablaMoneda = sqlTablaMoneda + "usuario_modificacion VARCHAR(100), ";
		sqlTablaMoneda = sqlTablaMoneda + "fecha_modificacion TIMESTAMP DEFAULT NOW()";
		sqlTablaMoneda = sqlTablaMoneda + ");";
		template.execute(sqlTablaMoneda);
	}

	public static void insertarRegistrosTablaMoneda(JdbcTemplate template) {
		String sqlInsertarMoneda;
		String[] monedas = { "EUROS", "DÓLAR", "SOLES" };
		String[] abreviaturas = { "EUR", "USD", "PEN" };
		int[] jerarquias = { 2, 1, 3 };

		for (int i = 0; i < monedas.length; i++) {
			sqlInsertarMoneda = "";
			sqlInsertarMoneda = sqlInsertarMoneda + "INSERT INTO moneda(nombre, abreviatura, jerarquia) VALUES (";
			sqlInsertarMoneda = sqlInsertarMoneda + "'" + monedas[i] + "', '" + abreviaturas[i] + "', " + jerarquias[i]
					+ ");";
			template.update(sqlInsertarMoneda);
		}
	}

	public static void crearTablaTipoCambio(JdbcTemplate template) {
		String sqlTablaTipoCambio;

		sqlTablaTipoCambio = "DROP TABLE tipocambio IF EXISTS;";
		sqlTablaTipoCambio = "CREATE TABLE tipocambio(";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "id INTEGER(11) PRIMARY KEY auto_increment, ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "idmoneda INTEGER(11), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "preciocompra DECIMAL(10,3), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "precioventa DECIMAL(10,3), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "fechacambio DATE, ";
        // Creamos los campos de auditoría
		sqlTablaTipoCambio = sqlTablaTipoCambio + "estado boolean DEFAULT true, ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "usuario_creacion VARCHAR(100), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "fecha_creacion TIMESTAMP DEFAULT NOW(), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "usuario_modificacion VARCHAR(100), ";
		sqlTablaTipoCambio = sqlTablaTipoCambio + "fecha_modificacion TIMESTAMP DEFAULT NOW()";
		sqlTablaTipoCambio = sqlTablaTipoCambio + ");";
		template.execute(sqlTablaTipoCambio);
	}

	public static void insertarRegistrosTablaTipoCambio(JdbcTemplate template) {
		String sqlInsertarTipoCambio;
		String fechaActual;

		// Obtenemos la fecha actual
		fechaActual = SistemaUtil.obtenerFechaComoString(new Date(), "yyyy/MM/dd");
//        fechaActual = SistemaUtil.obtenerFechaComoString(SistemaUtil.agregarDias(new Date(), 1), "yyyy/MM/dd");

		int[] monedas = { 1, 2, 3 }; // ID de las monedas
		Double[] precioCompra = { 1.08, 1.0, 0.24 };
		Double[] precioVenta = { 1.09, 1.0, 0.26 };

		for (int i = 0; i < monedas.length; i++) {
			sqlInsertarTipoCambio = "";
			sqlInsertarTipoCambio = sqlInsertarTipoCambio
					+ "INSERT INTO tipocambio(idmoneda, preciocompra, precioventa, fechacambio) VALUES (";
			sqlInsertarTipoCambio = sqlInsertarTipoCambio + monedas[i] + ", " + precioCompra[i] + ", " + precioVenta[i]
					+ ", TO_DATE ('" + fechaActual + "','yyyy-MM-dd'));";
			template.update(sqlInsertarTipoCambio);
		}
	}
	
	public static void crearTablaUsuario(JdbcTemplate template) {
        String sqlTablaUsuario;

        sqlTablaUsuario = "DROP TABLE usuario IF EXISTS;";
        sqlTablaUsuario = "CREATE TABLE usuario(";
        sqlTablaUsuario = sqlTablaUsuario + "id INTEGER(11) PRIMARY KEY auto_increment, ";
        sqlTablaUsuario = sqlTablaUsuario + "login VARCHAR(50), ";
        sqlTablaUsuario = sqlTablaUsuario + "contrasena VARCHAR(50), ";
        // Creamos los campos de auditoría
        sqlTablaUsuario = sqlTablaUsuario + "estado boolean DEFAULT true, ";
        sqlTablaUsuario = sqlTablaUsuario + "usuario_creacion VARCHAR(100), ";
        sqlTablaUsuario = sqlTablaUsuario + "fecha_creacion TIMESTAMP DEFAULT NOW(), ";
        sqlTablaUsuario = sqlTablaUsuario + "usuario_modificacion VARCHAR(100), ";
        sqlTablaUsuario = sqlTablaUsuario + "fecha_modificacion TIMESTAMP DEFAULT NOW()";
        sqlTablaUsuario = sqlTablaUsuario + ");";
        template.execute(sqlTablaUsuario);
    }

    public static void insertarRegistrosTablaUsuario(JdbcTemplate template) {
        String sqlInsertarUsuario;
        
        String[] logins = {"marcosbr", "admin"};
        String[] contrasenas = {"123456", "admin123"};

        for (int i = 0; i < logins.length; i++) {
            sqlInsertarUsuario = "";
            sqlInsertarUsuario = sqlInsertarUsuario + "INSERT INTO usuario(login, contrasena) VALUES (";
            sqlInsertarUsuario = sqlInsertarUsuario + "'" + logins[i] + "', '" + contrasenas[i] + "');";
            template.update(sqlInsertarUsuario);
        }
    }

}
