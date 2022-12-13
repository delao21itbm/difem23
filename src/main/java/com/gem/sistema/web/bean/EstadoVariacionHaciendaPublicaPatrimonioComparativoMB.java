package com.gem.sistema.web.bean;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;

// TODO: Auto-generated Javadoc
/**
 * The Class EstadoVariacionHaciendaPublicaPatrimonioMB.
 */
@ManagedBean
@ViewScoped
public class EstadoVariacionHaciendaPublicaPatrimonioComparativoMB extends ReporteComparativo {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct EstadoVariacionHaciendaPublicaPatrimonioMB ");
		jasperReporteName = "HaciendaPublicaPatrimonioComparativo";
		endFilename = jasperReporteName + ".pdf";
		initAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {

		Map<String, Object> parameters = getDefaultParams();
		parameters.put("query", this.generaQueryPatrimonio(this.getUserDetails().getIdSector()));

		return parameters;
	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mesFinalActual, null, this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
			;
		}
		this.createFilePdfInline();
	}

	/**
	 * Genera query patrimonio.
	 *
	 * @param idsector the idsector
	 * @param mes      the mes
	 * @return the string
	 */
	public String generaQueryPatrimonio(Integer idsector) {
		StringBuilder sSqlP = new StringBuilder();
		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.getTotalActualAndAnteriorByAnio(idsector, mesFinalActual);
		sSqlP.append(" SELECT GRUPO3, M.CUENTA GRUPO2,M.NOMMAY NOMBRE_G2,C.GRUPO1,	C.NOMBRE_G1,C.SALDO,")
				.append(" 		CASE WHEN GRUPO3=" + getAnioAnterior() + " AND GRUPO1=3220 THEN "
						+ edoSF3211Service.getTotalEjerciciosAnteriores(idsector) + " ELSE 0 END GENERADO,")
				.append(" 		CASE WHEN GRUPO3=" + getAnioAnterior() + " AND GRUPO1=3210 THEN "
						+ edoSF3211DTO.getTotalAnt() + " ELSE ")
				.append(" 			CASE WHEN GRUPO3=" + getAnio() + " AND GRUPO1=3210 THEN "
						+ edoSF3211DTO.getTotalAct() + " ")
				.append(" 			ELSE 0 END ").append(" 		END EJERCIDO,").append(" 		0 INS")
				.append(" FROM (")
				.append(" 	SELECT " + getAnioAnterior() + " GRUPO3,C.GRUPO1,M.NOMMAY NOMBRE_G1,C.SALDO")
				.append(" 	FROM (SELECT	SUBSTR(C.CUENTA,1,3)||'0' GRUPO1,")
				.append(" 			SUM(DECODE(CUENTA,3111,").append(" 				(C.SALINI-")
				.append(" 				(C.CARGOS_1+ C.CARGOS_2+ C.CARGOS_3+ C.CARGOS_4+ C.CARGOS_5+ C.CARGOS_6+ ")
				.append(" 					C.CARGOS_7+ C.CARGOS_8+ C.CARGOS_9+ C.CARGOS_10+ C.CARGOS_11+ C.CARGOS_12 )+")
				.append(" 				(C.ABONOS_1+ C.ABONOS_2+ C.ABONOS_3+ C.ABONOS_4+ C.ABONOS_5+ C.ABONOS_6+ ")
				.append(" 					C.ABONOS_7+ C.ABONOS_8+ C.ABONOS_9+ C.ABONOS_10+ C.ABONOS_11+ C.ABONOS_12 ))")
				.append(" 			,0))SALDO").append(" 		FROM CUENTA_ANTERIOR C")
				.append(" 		WHERE C.CUENTA BETWEEN '3000' AND	'3399' AND")
				.append(" 			C.SCTA='' AND	C.NOTCTA=0 AND	C.IDSECTOR=2")
				.append(" 		GROUP BY SUBSTR(C.CUENTA,1,3)")
				.append(" 	)C INNER JOIN MAYCTA M ON M.CUENTA=C.GRUPO1").append(" 	UNION ALL")
				.append(" 	SELECT " + getAnio() + " GRUPO3, CUENTA GRUPO1,NOMMAY NOMBRE_G1 ,0 SALDO")
				.append(" 		FROM MAYCTA").append(" 		WHERE CUENTA BETWEEN '3000' AND	'3399' AND")
				.append(" 			SUBSTR(CUENTA,3,1)<>0 AND SUBSTR(CUENTA,4,1)=0 ")
				.append(" ) C INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.GRUPO1,1,2)||'00'")
				.append(" ORDER BY GRUPO3,GRUPO1,M.CUENTA");
		return sSqlP.toString();
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
