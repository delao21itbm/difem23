package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.StreamedContent;
import com.gem.sistema.business.service.reportador.ReportValidationException;

// TODO: Auto-generated Javadoc
/**
 * The Class EdoActividadesMB.
 */
@ManagedBean
@ViewScoped
public class EdoActividades2MB extends ReporteComparativo {

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "edoactividadescComparativo";
		endFilename = jasperReporteName + ".pdf";
		initAll();
	}

	/**
	 * Generate querys.
	 *
	 * @param mes    the mes
	 * @param sector the sector
	 * @return the list
	 */
	public String generateQuerys(Integer sector) {
		String cargos = "";
		String abonos = "";
		StringBuilder query = new StringBuilder();
		Integer i = 1;

		while (i <= mesFinalActual) {
			cargos += " NVL(CARGOS_" + i + ",0) + ";
			abonos += " NVL(ABONOS_" + i + ",0) + ";
			i++;
		}
		cargos = cargos.substring(0, cargos.length() - 2);
		abonos = abonos.substring(0, abonos.length() - 2);
		query.append(
				" SELECT M.CUENTA GRUPO3,M.NOMMAY NOMBRE_G3, C.*																			")
				// GRUPO 3 4000 E INICIO DE INGRESOS
				.append(" FROM(SELECT M.CUENTA GRUPO2,M.NOMMAY NOMBRE_G2, C.* 																		")
				// GRUPO 2 4X00
				.append(" 	FROM(SELECT M.CUENTA GRUPO1,M.NOMMAY NOMBRE_G1,																			")
				// GRUPO1 4 4XX0
				.append(" 			DECODE(C.ACTUAL,NULL,0,C.ACTUAL)ACTUAL,                                                                         ")
				.append(" 			DECODE(C.ANTERIOR,NULL,0,C.ANTERIOR) ANTERIOR                                                                   ")
				.append(" 		FROM(SELECT N.CUENTA,N.ACTUAL,B.ANTERIOR 																			")
				// UNION DE TABLA CUENTA CON TABLA CUENTA_ANTERIOR
				.append(" 			FROM(SELECT 																									")
				// SUMAS
				.append(" 					CU.CUENTA,                                                                                              ")
				.append(" 					SUM(( " + abonos + " ) -		(" + cargos + ") +     ")
				.append(" 						NVL(SALINI,0)                                                                                       ")
				.append(" 					) ACTUAL                                                                                                ")
				.append(" 				FROM CUENTA CU                                                                                              ")
				.append(" 				WHERE                                                                                                       ")
				.append(" 					(CU.CUENTA >= '4151' AND CU.CUENTA <='4399') AND                                                        ")
				.append(" 					CU.SSSCTA <> '' AND                                                                                     ")
				.append(" 					CU.IDSECTOR= 2                                                                                          ")
				.append(" 				GROUP BY CU.CUENTA                                                                                          ")
				.append(" 			) N INNER JOIN (                                                                                                ")
				.append(" 				SELECT                                                                                                      ")
				.append(" 					CU.CUENTA,                                                                                              ")
				.append(" 					SUM(( NVL(ABONOS_1,0) + NVL(ABONOS_2,0) + NVL(ABONOS_3,0) + NVL(ABONOS_4,0) +                           ")
				.append(" 							NVL(ABONOS_5,0) + NVL(ABONOS_6,0)+ NVL(ABONOS_7,0) + NVL(ABONOS_8,0) +                          ")
				.append(" 							NVL(ABONOS_9,0) + NVL(ABONOS_10,0) + NVL(ABONOS_11,0) + NVL(ABONOS_12,0)) -                     ")
				.append(" 						(NVL(CARGOS_1,0) + NVL(CARGOS_2,0) + NVL(CARGOS_3,0)+NVL(CARGOS_4,0) +                              ")
				.append(" 							NVL(CARGOS_5,0) + NVL(CARGOS_6,0)+ NVL(CARGOS_7,0) + NVL(CARGOS_8,0) +                          ")
				.append(" 							NVL(CARGOS_9,0)+NVL(CARGOS_10,0) + NVL(CARGOS_11,0) + NVL(CARGOS_12,0)) +                       ")
				.append(" 						NVL(SALINI,0)                                                                                       ")
				.append(" 					) ANTERIOR                                                                                              ")
				.append(" 				FROM CUENTA_ANTERIOR CU                                                                                     ")
				.append(" 				WHERE                                                                                                       ")
				.append(" 					(CU.CUENTA >= '4151' AND CU.CUENTA <='4399') AND                                                        ")
				.append(" 					CU.SCTA = '' AND                                                                                        ")
				.append(" 					CU.IDSECTOR= 2                                                                                          ")
				.append(" 				GROUP BY CU.CUENTA                                                                                          ")
				.append(" 				ORDER BY CUENTA                                                                                             ")
				.append(" 			)B ON N.CUENTA=B.CUENTA                                                                                         ")
				.append(" 		)C RIGHT JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.CUENTA,1,3)||'0'                                                        ")
				.append(" 			WHERE M.CUENTA >= '4150' AND M.CUENTA <='4399'                                                                  ")
				.append(" 			AND SUBSTR(M.CUENTA,3,1)<>'0'                                                                                   ")
				.append(" 			AND SUBSTR(M.CUENTA,4,1)='0'                                                                                    ")
				.append(" 	)C INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.GRUPO1,1,2)||'00'                                                           ")
				.append(" )C INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.GRUPO1,1,1)||'000'                                                            ")
				.append(" UNION ALL                                                                                                                 ")
				.append(" SELECT	M.CUENTA GRUPO3,M.NOMMAY NOMBRE_G3, C.GRUPO2,C.NOMBRE_G2, 														")
				// CREACION DE GRUPO 3 E INICIO DE EGRESOS
				.append(" 	C.GRUPO1 ,C.NOMBRE_G1  ,C.ACTUAL,C.ANTERIOR                                                                             ")
				.append(" FROM (SELECT	 M.CUENTA GRUPO2,M.NOMMAY NOMBRE_G2,C.CUENTA GRUPO1 ,														")
				// CREACION DE GRUPO 2
				.append(" 		C.NOMGAS NOMBRE_G1 ,C.ACTUAL,C.ANTERIOR                                                                             ")
				.append(" 	FROM (SELECT N.CUENTA ,N.CLVGAS,N.NOMGAS,																				")
				// UNION DE NOMBRES CON SALDOS GRUPO1
				.append(" 			DECODE(C.ACTUAL,NULL,0,C.ACTUAL) ACTUAL,                                                                        ")
				.append(" 			DECODE(C.ANTERIOR,NULL,0,C.ANTERIOR) ANTERIOR                                                                   ")
				.append(" 		FROM(SELECT 																										")
				// SE OPTIENE LOS NOMBBRES Y CLAVES DE CADA CAPITULO
				.append(" 				CASE                                                                                                        ")
				.append(" 					WHEN CLVGAS IN (1000,2000,3000) THEN 5100                                                               ")
				.append(" 					WHEN SUBSTR(CLVGAS,1,1)='4' THEN 5200                                                                   ")
				.append(" 					WHEN SUBSTR(CLVGAS,1,1)='8' THEN 5300                                                                   ")
				.append(" 					WHEN SUBSTR(CLVGAS,1,1)='9' THEN 5400                                                                   ")
				.append(" 					ELSE 0                                                                                                  ")
				.append(" 				END CUENTA,                                                                                                 ")
				.append(" 				CLVGAS,                                                                                                     ")
				.append(" 				NOMGAS                                                                                                      ")
				.append(" 			FROM NATGAS                                                                                                     ")
				.append(" 			WHERE IDSECTOR=2                                                                                                ")
				.append(" 				AND (                                                                                                       ")
				.append(" 						CLVGAS IN (1000,2000,3000)                                                                          ")
				.append(" 						OR (                                                                                                ")
				.append(" 							SUBSTR(CLVGAS,2,1)<>'0'                                                                         ")
				.append(" 							AND SUBSTR(CLVGAS,3,1)=0                                                                        ")
				.append(" 							AND SUBSTR(CLVGAS,1,1) IN(4,8,9)                                                                ")
				.append(" 							AND CLVGAS NOT IN(9100,9900)                                                                    ")
				.append(" 						)                                                                                                   ")
				.append(" 					)                                                                                                       ")
				.append(" 			UNION ALL                                                                                                       ")
				.append(" 			SELECT CUENTA,CUENTA,NOMMAY FROM MAYCTA                                                                         ")
				.append(" 			 WHERE CUENTA IN (5500,5600)																					")
				// SE AGREGAN DESDE MAYCTA DADO A QUE NO EXISTEN EN NATGAS
				.append(" 			)N LEFT JOIN (                                                                                                  ")
				.append(" 			SELECT N.CUENTA ,N.ACTUAL ,B.ANTERIOR 																			")
				// UNION DE SALDO ANTERIORES Y ACTUALES
				.append(" 			FROM(SELECT CUENTA ,SUM(ACUMULADO) ACTUAL 																		")
				// SALDOS ACTUAL
				.append(" 				FROM(SELECT CASE                                                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =1 THEN 1000                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =2 THEN 2000                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =3 THEN 3000                                                            ")
				.append(" 							ELSE CUENTA                                                                                     ")
				.append(" 						END CUENTA,                                                                                         ")
				.append(" 						ACUMULADO                                                                                           ")
				.append(" 					FROM(SELECT                                                                                             ")
				.append(" 							SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)||'00' CUENTA,                                       ")
				.append(" 							SUM(( " + cargos + " ) -  ( " + abonos + " ) +  ")
				.append(" 								NVL(C.SALINI,0)                                                                             ")
				.append(" 							) ACUMULADO                                                                                     ")
				.append(" 						FROM	CUENTA C                                                                                    ")
				.append(" 						WHERE C.IDSECTOR=2 AND                                                                              ")
				.append(" 							(                                                                                               ")
				.append(" 								(C.CUENTA IN ('5100', '5200', '5300', '5400','5600','5700')                                 ")
				.append(" 								AND SSSCTA<>'')                                                                             ")
				.append(" 								OR (C.CUENTA IN ('5500') AND C.SCTA='' )                                                    ")
				.append(" 							)                                                                                               ")
				.append(" 						GROUP BY SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)                                                ")
				.append(" 						)C                                                                                                  ")
				.append(" 					)C GROUP BY CUENTA                                                                                      ")
				.append(" 			) N FULL JOIN (                                                                                                 ")
				.append(" 				SELECT CUENTA ,SUM(ACUMULADO) ANTERIOR 																		")
				// SALDOS ANTERIORES
				.append(" 				FROM(SELECT CASE                                                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =1 THEN 1000                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =2 THEN 2000                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) =3 THEN 3000                                                            ")
				.append(" 							WHEN SUBSTR(CUENTA,1,1) IN(5,6) AND SUBSTR(CUENTA,2,1) <>5 THEN 5600                            ")
				.append(" 							ELSE CUENTA                                                                                     ")
				.append(" 						END CUENTA,                                                                                         ")
				.append(" 						ACUMULADO                                                                                           ")
				.append(" 					FROM(SELECT                                                                                             ")
				.append(" 							SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)||'00' CUENTA,                                       ")
				.append(" 							SUM(( NVL(C.CARGOS_1,0) + NVL(C.CARGOS_2,0) + 	NVL(C.CARGOS_3,0)+NVL(C.CARGOS_4,0) +           ")
				.append(" 									NVL(C.CARGOS_5,0) + 	NVL(C.CARGOS_6,0)+ NVL(C.CARGOS_7,0) + NVL(C.CARGOS_8,0) + 	    ")
				.append(" 									NVL(C.CARGOS_9,0)+NVL(C.CARGOS_10,0) + NVL(C.CARGOS_11,0) + 	NVL(C.CARGOS_12,0)) -   ")
				.append(" 								( NVL(C.ABONOS_1,0) + NVL(C.ABONOS_2,0) +	NVL(C.ABONOS_3,0) +NVL(C.ABONOS_4,0) +          ")
				.append(" 									NVL(C.ABONOS_5,0) +	NVL(C.ABONOS_6,0)+  NVL(C.ABONOS_7,0) + NVL(C.ABONOS_8,0) +	        ")
				.append(" 									NVL(C.ABONOS_9,0) +NVL(C.ABONOS_10,0) + NVL(C.ABONOS_11,0) +	NVL(C.ABONOS_12,0)) +   ")
				.append(" 								NVL(C.SALINI,0)                                                                             ")
				.append(" 							) ACUMULADO                                                                                     ")
				.append(" 						FROM	CUENTA_ANTERIOR C                                                                           ")
				.append(" 						WHERE C.IDSECTOR=2 AND                                                                              ")
				.append(" 							(                                                                                               ")
				.append(" 								(C.CUENTA IN ('5100', '5200', '5300', '5400','5600','5700')                                 ")
				.append(" 								AND SSSCTA<>'')                                                                             ")
				.append(" 								OR (C.CUENTA IN ('5500') AND C.SCTA='' )                                                    ")
				.append(" 							)                                                                                               ")
				.append(" 						GROUP BY SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)                                                ")
				.append(" 						)C                                                                                                  ")
				.append(" 					)C GROUP BY CUENTA                                                                                      ")
				.append(" 				)B ON N.CUENTA=B.CUENTA                                                                                     ")
				.append(" 			)C ON C.CUENTA=N.CLVGAS                                                                                         ")
				.append(" 		) C RIGHT JOIN MAYCTA M   ON M.CUENTA=C.CUENTA                                                                      ")
				.append(" 	WHERE M.CUENTA >=5100 AND M.CUENTA<=5600 ORDER BY M.CUENTA                                                              ")
				.append(" )C INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.GRUPO2,1,1)||'000'                                                            ")
				.append(" ORDER BY GRUPO1                                                                                                           ");
		System.out.println("query: " + query.toString());
		return query.toString();
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = getDefaultParams();
		parameters.put("query", generateQuerys(this.getUserDetails().getIdSector()));

		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {

		return null;
	}

}
