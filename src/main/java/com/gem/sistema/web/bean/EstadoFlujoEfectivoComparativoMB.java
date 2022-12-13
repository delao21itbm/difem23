package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.reportador.ReportValidationException;

@ManagedBean(name = "estadoFlujoEfectivoComparativoMB")
@ViewScoped
public class EstadoFlujoEfectivoComparativoMB extends ReporteComparativo {

	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	@PostConstruct
	public void init() {
		jasperReporteName = "estadoFlujoEfectivoComparativo";
		endFilename = jasperReporteName + ".pdf";
		initAll();
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = getDefaultParams();

		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.getTotalForEfectivoByAnio(getMesFinalActual(),
				getMesFinalAnterior());
		parameters.put("pTotalEfectivoAnterior", edoSF3211DTO.getTotalAnt());
		parameters.put("pTotalEfectivoActual", edoSF3211DTO.getTotalAct());
		parameters.put("pQuery", this.getQuery(this.getUserDetails().getIdSector()));

		return parameters;
	}

	private String getQuery(Integer idsector) {
		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.getAnteriorByAnio(getMesFinalActual(), getMesFinalAnterior());
		StringBuilder query = new StringBuilder();
		String cargosact = "";
		String abonosact = "";
		String cargosant = "";
		String abonosant = "";
		String cargosactant = "";
		String abonosactant = "";
		String cargosantant = "";
		String abonosantant = "";
		for (int i = 1; i <= getMesFinalActual(); i++) {
			cargosact += " NVL(CARGOS_" + i + ",0)+";
			abonosact += " NVL(ABONOS_" + i + ",0)+";
		}
		for (int i = 1; i <= 12; i++) {
			cargosant += " NVL(CARGOS_" + i + ",0)+";
			abonosant += " NVL(ABONOS_" + i + ",0)+";
		}
		for (int i = 1; i < getMesFinalActual(); i++) {
			cargosactant += " NVL(CARGOS_" + i + ",0)+";
			abonosactant += " NVL(ABONOS_" + i + ",0)+";
		}
		for (int i = 1; i < 12; i++) {
			cargosantant += " NVL(CARGOS_" + i + ",0)+";
			abonosantant += " NVL(ABONOS_" + i + ",0)+";
		}

		if (cargosactant.equals(""))
			cargosactant = " 0 ";
		if (cargosantant.equals(""))
			cargosantant = " 0 ";
		if (abonosactant.equals(""))
			abonosactant = " 0 ";
		if (abonosantant.equals(""))
			abonosantant = " 0 ";

		cargosact = cargosact.substring(0, cargosact.length() - 1);
		abonosact = abonosact.substring(0, abonosact.length() - 1);
		cargosant = cargosant.substring(0, cargosant.length() - 1);
		abonosant = abonosant.substring(0, abonosant.length() - 1);
		cargosactant = cargosactant.substring(0, cargosactant.length() - 1);
		abonosactant = abonosactant.substring(0, abonosactant.length() - 1);
		cargosantant = cargosantant.substring(0, cargosantant.length() - 1);
		abonosantant = abonosantant.substring(0, abonosantant.length() - 1);
		query.append(
				"	SELECT GRUPO2,																													")
				.append("		DECODE(                                                                                                                     ")
				.append("			GRUPO2,	1,'Flujos de Efectivo de las Actividades de Operación',                                                         ")
				.append("					2,'Flujos de Efectivo de las Actividades de Inversión',                                                         ")
				.append("					3,'Flujo de Efectivo de las Actividades de Financiamiento',                                                     ")
				.append("					''                                                                                                              ")
				.append("		) NOMBRE_G2,                                                                                                                ")
				.append("		DECODE(GRUPO1,	1,'ORIGEN',                                                                                                 ")
				.append("						2,'APLICACION',                                                                                             ")
				.append("						''                                                                                                          ")
				.append("		) GRUPO1,                                                                                                                   ")
				.append("		CUENTA,NOMBRE,ACTUAL,ANTERIOR                                                                                               ")
				.append("		FROM((SELECT 1 GRUPO2 ,2 GRUPO1,N.CUENTA ,N.NOMGAS NOMBRE, DECODE(C.ACTUAL,NULL,0,C.ACTUAL) ACTUAL,                         ")
				.append("				DECODE(C.ANTERIOR,NULL,0,C.ANTERIOR) ANTERIOR                                                                       ")
				.append("			FROM(SELECT CASE WHEN CLVGAS IN (1000,2000,3000) THEN 5100 WHEN                                                         ")
				.append("					SUBSTR(CLVGAS,1,1)='4' THEN 5200 WHEN SUBSTR(CLVGAS,1,1)='8' THEN 5300 WHEN                                     ")
				.append("					SUBSTR(CLVGAS,1,1)='9' THEN 5400 ELSE 0 END CUENTA, CLVGAS, NOMGAS                                              ")
				.append("				FROM NATGAS                                                                                                         ")
				.append("				WHERE IDSECTOR=2 AND                                                                                                ")
				.append("					( CLVGAS IN (1000,2000,3000) OR	                                                                                ")
				.append("						( SUBSTR(CLVGAS,2,1)<>'0' AND	SUBSTR(CLVGAS,3,1)=0 AND                                                    ")
				.append("							SUBSTR(CLVGAS,1,1) IN(4,8) AND	CLVGAS NOT IN(9100,9900) )                                              ")
				.append("					)                                                                                                               ")
				.append("				UNION ALL                                                                                                           ")
				.append("				SELECT CUENTA,CUENTA,NOMMAY                                                                                         ")
				.append("				FROM MAYCTA                                                                                                         ")
				.append("				WHERE CUENTA IN (5500)                                                                                              ")
				.append("			)N	LEFT JOIN (                                                                                                         ")
				.append("				SELECT N.CUENTA ,N.ACTUAL ,B.ANTERIOR                                                                               ")
				.append("				FROM(SELECT CUENTA ,SUM(ACUMULADO) ACTUAL                                                                           ")
				.append("					FROM(SELECT CASE WHEN SUBSTR(CUENTA,1,1) =1 THEN 1000 WHEN SUBSTR(CUENTA,1,1) =2                                ")
				.append("							THEN 2000 WHEN SUBSTR(CUENTA,1,1) =3 THEN 3000 ELSE CUENTA END CUENTA,                                  ")
				.append("								ACUMULADO                                                                                           ")
				.append("						FROM(SELECT SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)||'00' CUENTA, 										")
				.append("								SUM( 																								")
				.append(" (" + cargosact + ") -").append("	( " + abonosact + " ) + NVL(C.SALINI,0) ) ACUMULADO ")
				.append("							FROM CUENTA C                                                                                           ")
				.append("							WHERE C.IDSECTOR=2 AND						(                                                           ")
				.append("							(C.CUENTA IN ('5100', '5200', '5300', '5400','5600','5700') AND                                         ")
				.append("								SSSCTA<>'') OR	(C.CUENTA IN ('5500') AND	C.SCTA='' ) )                                           ")
				.append("							GROUP BY SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)                                                    ")
				.append("						)C                                                                                                          ")
				.append("					)C                                                                                                              ")
				.append("					GROUP BY CUENTA                                                                                                 ")
				.append("				) N FULL JOIN (                                                                                                     ")
				.append("					SELECT CUENTA ,SUM(ACUMULADO) ANTERIOR                                                                          ")
				.append("					FROM(	SELECT CASE WHEN SUBSTR(CUENTA,1,1) =1 THEN 1000 WHEN SUBSTR(CUENTA,1,1) =2                             ")
				.append("							THEN 2000 WHEN SUBSTR(CUENTA,1,1) =3 THEN 3000 WHEN SUBSTR(CUENTA,1,1) IN(5,6)                          ")
				.append("							THEN 5500 WHEN SUBSTR(CUENTA,1,1)=4 THEN 4400 ELSE CUENTA END CUENTA, ACUMULADO                         ")
				.append("						FROM(SELECT SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)||'00' CUENTA, 										")
				.append("	SUM((" + cargosant + ")- ( " + abonosant + ") + NVL(C.SALINI,0) ) ACUMULADO ")
				.append("							FROM CUENTA_ANTERIOR C                                                                                  ")
				.append("							WHERE C.IDSECTOR=2 AND	(                                                                               ")
				.append("									(C.CUENTA IN ('5100', '5200', '5300', '5400','5600','5700') AND	SSSCTA<>'') OR                  ")
				.append("								(C.CUENTA IN ('5500') AND	C.SCTA='' ) )                                                           ")
				.append("							GROUP BY SUBSTR(DECODE(SSSCTA,'','5500',SSSCTA),1,2)                                                    ")
				.append("						)C                                                                                                          ")
				.append("					)C                                                                                                              ")
				.append("					GROUP BY CUENTA                                                                                                 ")
				.append("				)B	ON N.CUENTA=B.CUENTA                                                                                            ")
				.append("			)C	ON C.CUENTA=N.CLVGAS )                                                                                              ")
				.append("			UNION ALL(                                                                                                              ")
				.append("				SELECT 1 GRUPO2 , 1 GRUPO1,M.CUENTA,M.NOMMAY NOMBRE,DECODE(C.ACTUAL,NULL,0,C.ACTUAL) ACTUAL,                        ")
				.append("					DECODE(C.ANTERIOR ,NULL,0,C.ANTERIOR) ANTERIOR                                                                  ")
				.append("				FROM (SELECT N.CUENTA,N.ACTUAL,B.ANTERIOR                                                                           ")
				.append("					FROM(SELECT CU.CUENTA																							")
				.append(", SUM(( " + abonosact + " ) - ( " + cargosact + " ) + NVL(SALINI,0) )   ")
				.append("						ACTUAL                                                                                                      ")
				.append("					FROM CUENTA CU                                                                                                  ")
				.append("					WHERE (CU.CUENTA >= '4151' AND	CU.CUENTA <='4399') AND                                                         ")
				.append("						CU.SSSCTA <> '' AND	CU.IDSECTOR= 2                                                                          ")
				.append("					GROUP BY CU.CUENTA                                                                                              ")
				.append("				) N	INNER JOIN (                                                                                                    ")
				.append("					SELECT CU.CUENTA, 																								")
				.append(" SUM(( " + abonosant + ") - (" + cargosant + ") + NVL(SALINI,0) ) ANTERIOR   ")
				.append("					FROM CUENTA_ANTERIOR CU                                                                                         ")
				.append("					WHERE (CU.CUENTA >= '4151' AND	CU.CUENTA <='4399') AND                                                         ")
				.append("						CU.SCTA = '' AND	CU.IDSECTOR= 2                                                                          ")
				.append("					GROUP BY CU.CUENTA                                                                                              ")
				.append("				)B ON N.CUENTA=B.CUENTA                                                                                             ")
				.append("			)C RIGHT JOIN  MAYCTA M ON M.CUENTA=SUBSTR(C.CUENTA,1,3)||'0'                                                           ")
				.append("			WHERE SUBSTR(M.CUENTA,1,1)='4' 	AND SUBSTR(M.CUENTA,4,1)='0' AND SUBSTR(M.CUENTA,3,1)<>'0'                              ")
				.append("		)UNION ALL (			                                                                                                    ")
				.append("			SELECT 2 GRUPO2,1 GRUPO1, M.CUENTA,M.NOMMAY,N.ACTUAL,A.ANTERIOR                                                         ")
				.append("				FROM(SELECT SUBSTR(C.CUENTA,1,3) CUENTA,	                                                                        ")
				.append("					SUM((SALINI-CARGOSACT+ABONOSACT ) -(SALINI-CARGOSANT+ABONOSANT)) ACTUAL                                         ")
				.append("					FROM(SELECT CUENTA,                                                                                             ")
				.append("							NVL(C.SALINI,0.00) SALINI,                                                                              ")
				.append("		" + cargosactant + " CARGOSANT,                     ")
				.append("		" + abonosactant + " ABONOSANT,                     ")
				.append("		" + cargosact + " CARGOSACT,                        ")
				.append("		" + abonosact + " ABONOSACT                         ")
				.append("						FROM CUENTA C                                                                                               ")
				.append("						WHERE C.IDSECTOR=2 AND C.SCTA ='' AND	C.NOTCTA=0  AND                                                     ")
				.append("							SUBSTR(CUENTA,1,3) IN ('112','126')                                                                     ")
				.append("					)C GROUP BY  SUBSTR(C.CUENTA,1,3)                                                                               ")
				.append("				)N INNER JOIN(                                                                                                      ")
				.append("					SELECT SUBSTR(C.CUENTA,1,3) CUENTA,	                                                                            ")
				.append("						SUM((SALINI-CARGOSACT+ABONOSACT ) -(SALINI-CARGOSANT+ABONOSANT)) ANTERIOR                                   ")
				.append("					FROM(SELECT CUENTA,                                                                                             ")
				.append("							NVL(C.SALINI,0.00) SALINI,                                                                              ")
				.append(" " + cargosantant + " CARGOSANT,                       ")
				.append(" " + abonosantant + " ABONOSANT,   					")
				.append(" " + cargosant + " CARGOSACT,	                        ")
				.append(" " + abonosant + " ABONOSACT                           ")
				.append("						FROM CUENTA_ANTERIOR C                                                                                      ")
				.append("						WHERE C.IDSECTOR=2 AND C.SCTA ='' AND	C.NOTCTA=0  AND                                                     ")
				.append("							SUBSTR(CUENTA,1,3) IN ('112','126')                                                                     ")
				.append("					)C GROUP BY  SUBSTR(C.CUENTA,1,3)                                                                               ")
				.append("				)A ON N.CUENTA=A.CUENTA	INNER JOIN MAYCTA M ON N.CUENTA||'0' =M.CUENTA                                              ")
				.append("				UNION ALL                                                                                                           ")
				.append("				SELECT 2 ,2,CUENTA,NOMMAY,0,0 FROM MAYCTA WHERE CUENTA IN (1120,1260)                                               ")
				.append("		)UNION ALL (                                                                                                                ")
				.append("			SELECT 3 GRUPO2,C.GRUPO1,C.CUENTA,C.NOMBRE,C.ACTUAL,C.ANTERIOR                                                          ")
				.append("			FROM (SELECT C.CUENTA,C.GRUPO1,C.NOMBRE,C.ACTUAL,C.ANTERIOR                                                             ")
				.append("				FROM(SELECT 9014 CUENTA, 2 GRUPO1,                                                                                  ")
				.append("					'Otros Aplicaciones de Financiamiento' NOMBRE,                                                                  ")
				.append(" " + edoSF3211DTO.getTotalAct() + " -N.ACTUAL  ACTUAL ,    ")
				.append(" " + edoSF3211DTO.getTotalAnt() + " -A.ANTERIOR  ANTERIOR  ")
				.append("					FROM(SELECT 1 CUENTA, SUM((SALINI-CARGOSACT+ABONOSACT ) -(SALINI-CARGOSANT+ABONOSANT)) ACTUAL                   ")
				.append("						FROM(SELECT CUENTA,                                                                                         ")
				.append("								NVL(C.SALINI,0.00) SALINI,                                                                          ")
				.append(" " + cargosactant + " CARGOSANT,                   ")
				.append(" " + abonosactant + " ABONOSANT,                   ")
				.append(" " + cargosact + " CARGOSACT,	                    ")
				.append(" " + abonosact + " ABONOSACT 	                    ")
				.append("							FROM CUENTA C                                                                                           ")
				.append("							WHERE C.IDSECTOR=2 AND C.SCTA ='' AND	C.NOTCTA=0  AND                                                 ")
				.append("								SUBSTR(CUENTA,1,3) IN ('211','322')                                                                 ")
				.append("						)C GROUP BY  1                                                                                              ")
				.append("					)N INNER JOIN(                                                                                                  ")
				.append("						SELECT	1 CUENTA,SUM((SALINI-CARGOSACT+ABONOSACT ) -(SALINI-CARGOSANT+ABONOSANT)) ANTERIOR                  ")
				.append("						FROM(SELECT CUENTA,                                                                                         ")
				.append("								NVL(C.SALINI,0.00) SALINI,                                                                          ")
				.append(" " + cargosantant + " CARGOSANT,                 ")
				.append(" " + abonosantant + " ABONOSANT,	              ")
				.append("" + cargosant + " CARGOSACT,                     ")
				.append(" " + abonosant + "ABONOSACT                	  ")
				.append("							FROM CUENTA_ANTERIOR C                                                                                  ")
				.append("							WHERE C.IDSECTOR=2 AND C.SCTA ='' AND	C.NOTCTA=0  AND                                                 ")
				.append("								SUBSTR(CUENTA,1,3) IN ('211','322')                                                                 ")
				.append("						)C GROUP BY 1                                                                                               ")
				.append("					)A ON N.CUENTA=A.CUENTA                                                                                         ")
				.append("					UNION ALL                                                                                                       ")
				.append("		 			values                                                                                                          ")
				.append("		 				(9011,2,'Servicios de la Deuda',0,0),                                                                       ")
				.append("						(9012,2,'Interno',0,0),                                                                                     ")
				.append("						(9013,2,'Externo',0,0)                                                                                      ")
				.append("					) C                                                                                                             ")
				.append("				UNION ALL                                                                                                           ")
				.append("				values                                                                                                              ")
				.append("		 				(9001,1,'Endeudamiento Neto',0,0),                                                                          ")
				.append("						(9002,1,'Interno',0,0),                                                                                     ")
				.append("						(9003,1,'Externo',0,0),                                                                                     ")
				.append("						(9004,1,'Otros Orígenes de Financiamiento',0,0)                                                             ")
				.append("			) C                                                                                                                     ")
				.append("		)                                                                                                                           ")
				.append("	)C ORDER BY C.GRUPO2,GRUPO1 DESC,CUENTA                                                                                         ");
		return query.toString();
	}

	public EdoSF3211Service getEdoSF3211Service() {
		return edoSF3211Service;
	}

	public void setEdoSF3211Service(EdoSF3211Service edoSF3211Service) {
		this.edoSF3211Service = edoSF3211Service;
	}

}
