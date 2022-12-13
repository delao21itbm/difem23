package com.gem.sistema.business.dao.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.dao.ComparadorDAO;
import com.gem.sistema.business.dto.ComparadorDTO;

/**
 * @author Mateo Fecha Modificacion Autor Descripcion Version ------------------
 *         ----------------------------- ---------------------------------
 *         -------- 24/03/2021 Julio Cesar de la O Espinoza Se crea La DAO
 *         Implementacion 1.0 para la comparación de presupuesto
 * 		
 *		   29/03/2021		Javier Tenorio			Se agrega metodo generaQuery	1.1
 */
@Repository
public class ComparadorDAOImpl implements ComparadorDAO {

	private static final Logger log = LoggerFactory.getLogger(ComparadorDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	/*
	 * JTL29032021
	 */
	public String generaQuery(Integer mes) {
		StringBuilder sSql = new StringBuilder();
		StringBuilder presupuesto = new StringBuilder();
		StringBuilder cargos = new StringBuilder();
		StringBuilder abonos = new StringBuilder();
		sSql.append("SELECT T2.CUENTA, T2.SCTA, T2.SSCTA, T2.SSSCTA, T2.SSSSCTA, \r\n")
				.append("		T2.SALDO, T2.MONTO, (T2.SALDO - T2.MONTO) DISPONIBILIDAD\r\n").append("	FROM(\r\n")
				.append("		SELECT T1.CUENTA, T1.SCTA, T1.SSCTA, T1.SSSCTA, T1.SSSSCTA, \r\n")
				.append("			DECODE(T1.STACTA,'D',(T1.PRESUPUESTO + T1.CARGOS - T1.ABONOS), \r\n")
				.append("								 (T1.PRESUPUESTO + T1.ABONOS - T1.CARGOS) ) SALDO, T1.MONTO\r\n")
				.append("		FROM (\r\n")
				.append("			SELECT C.CUENTA, C.SCTA, C.SSCTA, C.SSSCTA, C.SSSSCTA, C.STACTA, ");
		abonos.append("( ");
		cargos.append("( ");
		presupuesto.append("( ");
		for (int i = 1; i <= mes; i++) {

			presupuesto.append(" P.AUTO1_" + i);
			cargos.append("  C.CARGOS_" + i);
			abonos.append("  C.ABONOS_" + i);
			if (i != mes) {
				presupuesto.append("  + ");
				cargos.append("  + ");
				abonos.append(" + ");
			} else {
				presupuesto.append(" ) PRESUPUESTO, ");
				cargos.append(" ) CARGOS, ");
				abonos.append(" ) ABONOS,");
			}
		}

		sSql.append(presupuesto).append(cargos).append(abonos).append("T.SALINI MONTO\r\n")
				.append("			FROM TMP_CUENTA T\r\n")
				.append("				INNER JOIN CUENTA C ON T.CUENTA = C.CUENTA AND \r\n")
				.append("										T.SCTA = C.SCTA AND\r\n")
				.append("										T.SSCTA = C.SSCTA AND\r\n")
				.append("										T.SSSCTA = C.SSSCTA AND\r\n")
				.append("										NVL(T.SSSSCTA,'') = C.SSSSCTA\r\n")
				.append("				INNER JOIN PASO P ON    T.SCTA = P.CLAVE AND\r\n")
				.append("										T.SSCTA = P.PROGRAMA AND\r\n")
				.append("										T.SSSCTA = P.PARTIDA AND\r\n")
				.append("										C.IDSECTOR = P.IDSECTOR\r\n")
				.append("				WHERE C.IDSECTOR = 2").append(")T1\r\n" + ")T2");
		log.debug(sSql.toString());
		return sSql.toString();
		
	}
	/*
	 * JTL29032021
	 */
	@Override
	public List<ComparadorDTO> comparar(Integer mes, String pathFile) {

		cargaTablaTemporal(pathFile);

		/*
		 * JTL29032021
		 */
		List<ComparadorDTO> listCompara = this.jdbcTemplate.query(generaQuery(mes), new RowMapper<ComparadorDTO>() {
		/*
		 * JTL29032021
		 */
			
			@Override
			public ComparadorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ComparadorDTO compara = new ComparadorDTO();
				compara.setCuenta(rs.getString("CUENTA"));
				compara.setsCta(rs.getString("SCTA"));
				compara.setSsCta(rs.getString("SSCTA"));
				compara.setSssCta(rs.getString("SSSCTA"));
				compara.setSsssCta(rs.getString("SSSSCTA"));
				compara.setSaldo(rs.getBigDecimal("SALDO"));
				compara.setMonto(rs.getBigDecimal("MONTO"));
				compara.setDisponibilidad(rs.getBigDecimal("DISPONIBILIDAD"));
				return compara;
			}

		});
		return listCompara;
	}

	private void cargaTablaTemporal(String pathFullFile) {
		this.jdbcTemplate.update("TRUNCATE TABLE TMP_CUENTA IMMEDIATE");
		String sSql = "INSERT INTO TMP_CUENTA (CUENTA, SCTA, SSCTA, SSSCTA, SSSSCTA, SALINI) VALUES (?, ?, ? ,?, ?, ?)";
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(pathFullFile));
			String sCadena = StringUtils.EMPTY;

			while ((sCadena = bf.readLine()) != null) {
				String data[] = sCadena.split(",");
				this.jdbcTemplate.update(sSql,data[0], data[1], data[2], data[3], data[4], data[5]);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
