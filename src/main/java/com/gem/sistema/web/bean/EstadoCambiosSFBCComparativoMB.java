package com.gem.sistema.web.bean;

import java.math.BigDecimal;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;

// TODO: Auto-generated Javadoc
/**
 * The Class EstadoCambiosSFBCMB.
 */
/**
 * @author Alfredo
 *
 */
@ManagedBean(name = "estadoCambiosSFBCComparativoMB")
@ViewScoped
public class EstadoCambiosSFBCComparativoMB extends ReporteComparativo {
	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		this.jasperReporteName = "EstadoCambiosSitFinComparativo";
		this.endFilename = this.jasperReporteName + ".pdf";
		initAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = getDefaultParams();
		Integer idSector = this.getUserDetails().getIdSector();

		conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());
		parameters.put("query", getQuery(idSector));
		return parameters;
	}

	public String getQuery(Integer sector) {
		StringBuilder query = new StringBuilder();
		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.executeQuery(sector, 1, mesFinalActual);
		BigDecimal total = edoSF3211DTO.getTotalAct().subtract(edoSF3211DTO.getTotalAnt());

		String cargosAnt = "";
		String abonosAnt = "";
		String cargosAct = "";
		String abonosAct = "";

		if (mesFinalActual == 1) {
			cargosAnt = " 0.00";
			abonosAnt = " 0.00";
		} else {
			for (int i = 1; i < mesFinalActual; i++) {
				cargosAnt += " NVL(C.CARGOS_" + i + ",0.00)+";
				abonosAnt += " NVL(C.ABONOS_" + i + ",0.00)+";

			}
		}
		for (int i = 1; i <= mesFinalActual; i++) {
			cargosAct += " NVL(C.CARGOS_" + i + ",0.00)+";
			abonosAct += " NVL(C.ABONOS_" + i + ",0.00)+";

		}
		cargosAnt = cargosAnt.substring(1, cargosAnt.length() - 1);
		abonosAnt = abonosAnt.substring(1, abonosAnt.length() - 1);

		cargosAct = cargosAct.substring(1, cargosAct.length() - 1);
		abonosAct = abonosAct.substring(1, abonosAct.length() - 1);

		query.append(
				"	SELECT M.CUENTA GRUPO2,M.NOMMAY NOMBRE_G2,C.* 																								")
				.append("	FROM(SELECT M.CUENTA GRUPO1,M.NOMMAY NOMBRE_G1,C.CUENTA GRUPO,C.NOMMAY NOMBRE,C.ORIGEN,C.APLICACION                        ")
				.append("		FROM((SELECT T3.CUENTA,	T3.NOMCTA NOMMAY,                                                                               ")
				.append("					CASE WHEN T3.MESACT-T3.MESANT<=0 THEN T3.MESACT-T3.MESANT ELSE 0 END ORIGEN,                                ")
				.append("					CASE WHEN T3.MESACT-T3.MESANT>=0 THEN T3.MESACT-T3.MESANT ELSE 0 END APLICACION                             ")
				.append("				FROM (SELECT SUBSTR(M.CUENTA,1,2) GRUPO,M.NATCTA,M.CUENTA,M.NOMMAY NOMCTA,C.NOTCTA,                             ")
				.append("						CASE WHEN UPPER(M.NATCTA) ='D' OR C.CUENTA IN ('1115','1162') THEN                                      ")
				.append("							 SALINI+CARGOSANT-ABONOSANT ELSE                                                                    ")
				.append("							SALINI-CARGOSANT+ABONOSANT END MESANT,                                                              ")
				.append("						CASE WHEN UPPER(M.NATCTA) ='D' OR C.CUENTA IN ('1115','1162') THEN                                      ")
				.append("							 SALINI+CARGOSACT-ABONOSACT                                                                         ")
				.append("							ELSE SALINI-CARGOSACT+ABONOSACT END MESACT                                                          ")
				.append("					FROM(SELECT SUBSTR(C.CUENTA,1,3) CUENTA,C.NOTCTA NOTCTA,                                                    ")
				.append("							SUM(NVL(C.SALINI,0.00)) SALINI,                                                                     ")
				.append("		SUM(" + cargosAnt + ") CARGOSANT,   	SUM(" + abonosAnt + ") ABONOSANT, ")
				.append("							SUM(" + cargosAct + ") CARGOSACT,               ")
				.append("							SUM(" + abonosAct + ") ABONOSACT                    ")
				.append("						FROM CUENTA C                                                                                           ")
				.append("						WHERE C.IDSECTOR=2 AND C.SCTA ='' AND	C.NOTCTA=0  AND                                                 ")
				.append("							(C.CUENTA >='1000' AND	C.CUENTA <='1293')                                                          ")
				.append("						GROUP BY  SUBSTR(C.CUENTA,	1,3),C.NOTCTA                                                               ")
				.append("						ORDER BY SUBSTR(C.CUENTA,	1,3)                                                                        ")
				.append("					)C RIGHT  JOIN MAYCTA M ON M.CUENTA=C.CUENTA||'0'                                                           ")
				.append("					WHERE M.CUENTA >='1000' AND	M.CUENTA <='1293' AND SUBSTR(M.CUENTA,4,1)='0' AND SUBSTR(M.CUENTA,3,1)<>0      ")
				.append("				)T3 )                                                                                                           ")
				.append("		UNION ALL                                                                                                               ")
				.append("		(SELECT 	M.CUENTA  ,M.NOMMAY,                                                                                        ")
				.append("					CASE WHEN M.CUENTA='3210' THEN	CASE WHEN " + total + " <=0 THEN " + total
						+ " ELSE 0 END ")
				.append("					ELSE CASE WHEN T4.MESACT-T4.MESANT<=0 THEN T4.MESACT-T4.MESANT ELSE 0 END                                   ")
				.append("					END ORIGEN,                                                                                                 ")
				.append("					CASE WHEN M.CUENTA='3210' THEN	CASE WHEN " + total + " >=0 THEN " + total
						+ " ELSE 0 END ")
				.append("					ELSE CASE WHEN T4.MESACT-T4.MESANT>=0 THEN T4.MESACT-T4.MESANT ELSE 0 END                                   ")
				.append("					END APLICACION                                                                                              ")
				.append("				FROM (SELECT SUBSTR(T3.CUENTA,1,3) CUENTA,                                                                      ")
				.append("						SUM(CASE WHEN T3.CUENTA='3241' THEN                                                                     ")
				.append("							CASE WHEN T3.MESANT < 0 THEN                                                                        ")
				.append("								ABS(T3.MESANT)                                                                                  ")
				.append("								ELSE (T3.MESANT)*(-1) END                                                                       ")
				.append("							ELSE T3.MESANT END                                                                                  ")
				.append("						)MESANT,                                                                                                ")
				.append("						SUM(CASE WHEN T3.CUENTA='3241' THEN                                                                     ")
				.append("								CASE WHEN T3.MESACT < 0 THEN                                                                    ")
				.append("									ABS(T3.MESACT)                                                                              ")
				.append("								ELSE (T3.MESACT)*(-1) END                                                                       ")
				.append("							ELSE T3.MESACT END                                                                                  ")
				.append("						) MESACT                                                                                                ")
				.append("					FROM (SELECT T2.CUENTA,                                                                                     ")
				.append("							CASE WHEN T2.CUENTA='3241' AND T2.NATCTA='D' THEN                                                   ")
				.append("								T2.SALINI-T2.CARGOSANT+T2.ABONOSANT                                                             ")
				.append("							ELSE T2.MESANT END 	MESANT,                                                                         ")
				.append("							CASE WHEN T2.CUENTA='3241' AND T2.NATCTA='D' THEN                                                   ")
				.append("								T2.SALINI-T2.CARGOSACT+T2.ABONOSACT                                                             ")
				.append("							ELSE T2.MESACT END MESACT                                                                           ")
				.append("						FROM (                                                                                                  ")
				.append("							SELECT T1.NATCTA,T1.CUENTA,	T1.SALINI,	T1.CARGOSANT,T1.ABONOSANT,	                                ")
				.append("								T1.CARGOSACT,T1.ABONOSACT,                                                                      ")
				.append("								CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSANT-T1.ABONOSANT                              ")
				.append("								ELSE T1.SALINI-T1.CARGOSANT+T1.ABONOSANT END MESANT,                                            ")
				.append("								CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSACT-T1.ABONOSACT                              ")
				.append("								ELSE T1.SALINI-T1.CARGOSACT+T1.ABONOSACT END MESACT                                             ")
				.append("							FROM (SELECT SUBSTR(C.CUENTA,1,	2) GRUPO,                                                           ")
				.append("									C.STACTA NATCTA,                                                                            ")
				.append("									C.CUENTA CUENTA ,                                                                           ")
				.append("									NVL(C.SALINI,0.00) SALINI,                                                                  ")
				.append("					SUM(" + cargosAnt + ") CARGOSANT, SUM(" + abonosAnt + ") ABONOSANT,  ")
				.append("									SUM(" + cargosAct + ") CARGOSACT,           ")
				.append("									SUM(" + abonosAct + ") ABONOSACT			")
				.append("								FROM CUENTA C JOIN MAYCTA M ON C.CUENTA = M.CUENTA                                              ")
				.append("								WHERE C.IDSECTOR=2 AND	(C.CUENTA >='2000' AND	C.CUENTA <='3321') AND                          ")
				.append("									C.SCTA ='' AND	C.NOTCTA=0                                                                  ")
				.append("								GROUP BY SUBSTR(C.CUENTA,1,	2),	C.STACTA,C.CUENTA,C.SALINI                                      ")
				.append("								ORDER BY C.CUENTA                                                                               ")
				.append("							)T1                                                                                                 ")
				.append("						)T2                                                                                                     ")
				.append("					)T3 GROUP BY SUBSTR(T3.CUENTA,1,3)                                                                          ")
				.append("				)T4 RIGHT JOIN MAYCTA M ON M.CUENTA=T4.CUENTA||'0'                                                              ")
				.append("				WHERE  SUBSTR(M.CUENTA,3,1)<>'0' AND SUBSTR(M.CUENTA,4,1)='0'	AND M.CUENTA >='2000' AND	M.CUENTA <='3321'   ")
				.append("			)                                                                                                                   ")
				.append("		)C INNER JOIN MAYCTA M ON SUBSTR(C.CUENTA,1,2)||'00'=M.CUENTA                                                           ")
				.append("	)C INNER JOIN MAYCTA M ON SUBSTR(C.GRUPO,1,1)||'000'=M.CUENTA                                                              ")
				.append("	ORDER BY C.GRUPO                                                                                                           ");
		return query.toString();
	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.createFilePdfInline();
		}
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public EdoSF3211Service getEdoSF3211Service() {
		return edoSF3211Service;
	}

	public void setEdoSF3211Service(EdoSF3211Service edoSF3211Service) {
		this.edoSF3211Service = edoSF3211Service;
	}

}
