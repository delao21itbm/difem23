package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;;

@ManagedBean(name = "estadoAnaliticoActivoMB")
@ViewScoped
public class EstadoAnaliticoActivoMB extends ReporteComparativo {

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@PostConstruct
	public void init() {
		jasperReporteName = "estadoAnaliticoActivo";
		endFilename = jasperReporteName + ".pdf";
		initAll();
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = getDefaultParams();
		Integer idSector = this.getUserDetails().getIdSector();
		parameters.put("query", this.generateQuery(idSector));
		return parameters;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateQuery(Integer idSector) {

		StringBuilder sQuery = new StringBuilder();
		String cargos = "";
		String abonos = "";

		if (mesFinalActual > 1) {
			for (int i = 1; i <= mesFinalActual; i++) {
				cargos += " CU.CARGOS_" + i + " +";
				abonos += " CU.ABONOS_" + i + " +";
			}
			cargos = cargos.substring(0, cargos.length() - 1);
			abonos = abonos.substring(0, abonos.length() - 1);
		} else {
			cargos = " '0' ";
			abonos = " '0' ";
		}
		sQuery.append("	SELECT 	M.CUENTA,M.NOMMAY,C.*																")
				.append("	FROM(SELECT M.CUENTA GRUPO,                                                             ")
				.append("			M.NOMMAY NOMBRE,                                                                ")
				.append("			SUM(SALINI) SALDOINICIAL,                                                       ")
				.append("			SUM(CARGOS) CARGOS,                                                             ")
				.append("			SUM(ABONOS) ABONOS,                                                             ")
				.append("			SUM(SALDO ) SALDOFINAL,                                                         ")
				.append("			SUM(SALDO -SALINI ) FLUJO                                                       ")
				.append("		FROM (                                                                              ")
				.append("			SELECT CUENTA,                                                                  ")
				.append("				SALINI,                                                                     ")
				.append("				CASE WHEN STACTA IN('A')                                                    ")
				.append("					THEN CASE WHEN CUENTA IN ('1115','1161')                                ")
				.append("						THEN ABS(SALINI + CARGOS - ABONOS)                                  ")
				.append("						ELSE ABS(SALINI - CARGOS + ABONOS)                                  ")
				.append("					END 	                                                                ")
				.append("					ELSE ABS(SALINI + CARGOS - ABONOS)                                      ")
				.append("				END	SALDO,                                                                  ")
				.append("				CARGOS,                                                                     ")
				.append("				ABONOS                                                                      ")
				.append("			FROM (                                                                          ")
				.append("				SELECT CU.CUENTA,                                                           ")
				.append("					CU.STACTA,                                                              ")
				.append("					CU.SALINI,                                                              ")
				.append("					" + cargos + " CARGOS," + abonos + " ABONOS                            	")
				.append("				FROM CUENTA CU	                                                            ")
				.append("				WHERE CU.IDSECTOR=2 AND CU.SCTA = '' AND	CU.CUENTA <= 1300 AND           ")
				.append("					SUBSTR(CU.CUENTA,2,	3) <> '000' AND	CU.NOTCTA <> 3                      ")
				.append("			) T1                                                                            ")
				.append("		)C RIGHT JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.CUENTA,1,3)||'0'                        ")
				.append("		WHERE M.CUENTA  <= 1300 AND SUBSTR(M.CUENTA,3,1)<>'0' AND SUBSTR(M.CUENTA,4,1)='0'  ")
				.append("		GROUP BY M.CUENTA,	M.NOMMAY                                                        ")
				.append("	) C INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.GRUPO,1,2)||'00'                           ")
				.append("	ORDER BY C.GRUPO                                                                        ");

		return sQuery.toString();
	}

	/** Metodo de validacion al descargar el PDF */
	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
		}
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

}
