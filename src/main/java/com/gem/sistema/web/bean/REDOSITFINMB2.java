
package com.gem.sistema.web.bean;

import java.math.BigDecimal;
/*

Object: Edo_SitFin.jrxml y Orden.jrxml

Fecha de Creación                    Autor                             Descripcion                      Versión                        Linea de Codigo

---------------       ---------------------------------     -----------------------------          -----------                     -------------------

10/03/2021            Luis Enrique Longino Nicolas. 		Se ajusta reporte para mostrar firmas     	1.0                             	
															en la misma hoja de cuentas de orden 
															
       
*/
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import org.primefaces.context.RequestContext;

// TODO: Auto-generated Javadoc
/**
 * The Class REDOSITFINMB.
 */
@ManagedBean
@ViewScoped
public class REDOSITFINMB2 extends ReporteComparativo {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";
	private static final String DOWNLOAD_TXT = " jQuery('#form1\\\\:downTxt').click();";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(REDOSITFINMB2.class);

	/** The edo SF 3211 DTO. */
	private EdoSF3211DTO edoSF3211DTO;

	/** The edo SF 3211 service. */
	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct rEDOSITFINMB ");
		jasperReporteName = "Edo_SitFin2";
		endFilename = jasperReporteName + ".pdf";
		initAll();
	}

	public Map<String, Object> getParametersReports() {

		Map<String, Object> paramsReport = getDefaultParams();
		Integer idSector = this.getUserDetails().getIdSector();

		edoSF3211DTO = edoSF3211Service.executeQuery(idSector, 1, mesFinalActual, false);

		paramsReport.put("queryActivo", getQueryActivo(idSector));
		paramsReport.put("queryPasivo",
				getQueryPasivo(idSector, edoSF3211DTO.getTotalAct(), edoSF3211DTO.getTotalAnt()));

		return paramsReport;
	}

	public void downloadTxt() {
		if (this.validatePolicyService.isOpenMonth(mesFinalActual, null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mesFinalActual, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mesFinalActual, null, this.getUserDetails().getIdSector());
			RequestContext.getCurrentInstance().execute(DOWNLOAD_TXT);
		}

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
	}

	private String getQueryPasivo(Integer sector, BigDecimal actual3211, BigDecimal anterior3211) {
		StringBuilder query = new StringBuilder();
		String abonoActual = getSumaCargoOAbono(false, "C.ABONOS_");
		String cargoActual = getSumaCargoOAbono(false, "C.CARGOS_");
		query.append(
				"SELECT  M.CUENTA GRUPO_2,M.NOMMAY NOMBRE_G2, C.GRUPO_1,C.NOMBRE_G1 ,C.CUENTA C_P,C.NOMMAY N_P,C.ACTUAL,C.ANTERIOR 	FROM (")
				.append("SELECT M.CUENTA GRUPO_1,M.NOMMAY NOMBRE_G1 ,C.CUENTA,C.NOMMAY,C.ACTUAL,C.ANTERIOR FROM( SELECT SUBSTR(C.CUENTA,1,2) GRUPO, ")
				.append("	MA.CUENTA,	                                                                                                    ")
				.append("	MA.NOMMAY,	                                                                                                    ")
				.append("	C.ACTUAL,                                                                                                       ")
				.append("	C.ANTERIOR                                                                                                      ")
				.append("FROM (                                                                                                             ")
				.append("	SELECT                                                                                                          ")
				.append("		C.CUENTA,                                                                                                   ")
				.append("		DECODE(UPPER(STACTA),'D',                                                                                   ")
				.append("			ACTUAL+CARGOS-ABONOS,                                                                                   ")
				.append("			ACTUAL-CARGOS+ABONOS                                                                                    ")
				.append("		) ACTUAL,                                                                                                   ")
				.append("		C.ANTERIOR                                                                                                  ")
				.append("	FROM(                                                                                                           ")
				.append("		SELECT                                                                                                      ")
				.append("			C.STACTA,                                                                                               ")
				.append("			SUBSTR(C.CUENTA,1,3) CUENTA,                                                                            ")
				.append("			SUM(DECODE (CUENTA,'3211',                                                                              ")
				.append("					DECODE(1,0,SALINI,").append(anterior3211).append("),	")
				.append("					SALINI                                                                                          ")
				.append("			)) ANTERIOR,                                                                                            ")
				.append("			SUM(DECODE (CUENTA,'3211',").append(actual3211).append(",	SALINI)	) ACTUAL, ")
				.append(abonoActual).append("			 ABONOS,").append(cargoActual).append("   CARGOS     ")
				.append("		FROM  CUENTA C                                                                                              ")
				.append("		WHERE C.CUENTA >= '2000' AND C.CUENTA <= '3321' AND                                                         ")
				.append("			C.SCTA ='' AND	C.IDSECTOR = 2 AND                                                                      ")
				.append("			(SUBSTR(C.CUENTA,3,2) ='00' OR	SUBSTR(C.CUENTA,4,1) <>'0')                                             ")
				.append("		GROUP BY C.STACTA, SUBSTR(C.CUENTA,1,3)                                                                     ")
				.append("		ORDER BY CUENTA                                                                                             ")
				.append("	)C) C INNER JOIN MAYCTA MA ON MA.CUENTA = C.CUENTA||'0'                                                     ")
				.append(") C INNER JOIN MAYCTA M ON M.CUENTA=C.GRUPO||'00'")
				.append(")C 	INNER JOIN MAYCTA M ON M.CUENTA=SUBSTR(C.CUENTA,1,1)||'000'");
		System.out.println(query.toString());
		return query.toString();
	}

	private String getQueryActivo(Integer sector) {
		StringBuilder query = new StringBuilder();
		String abonoActual = getSumaCargoOAbono(false, "C.ABONOS_");
		String cargoActual = getSumaCargoOAbono(false, "C.CARGOS_");

		query.append(
				"SELECT M.CUENTA GRUPO,M.NOMMAY NOMBRE ,C.CUENTA C_P,C.NOMMAY N_P,C.ACTUAL,C.ANTERIOR FROM( SELECT SUBSTR(C.CUENTA,1,2) GRUPO,")
				.append("	MA.CUENTA,														                                                                ")
				.append("	MA.NOMMAY,														                                                                ")
				.append("	C.ACTUAL,													                                                                    ")
				.append("	C.ANTERIOR													                                                                    ")
				.append("FROM (													                                                                            ")
				.append("		SELECT CUENTA,													                                                            ")
				.append("			DECODE(UPPER(STACTA),'D',													                                            ")
				.append("				SALINI+CARGOS-ABONOS,													                                            ")
				.append("				CASE WHEN (SUBSTR(CUENTA,1,2)='12' OR SUBSTR(CUENTA,1,3)='116') AND UPPER(STACTA)='A' THEN							")
				.append("					(SALINI * -1)+CARGOS-ABONOS													                                    ")
				.append("				ELSE													                                                            ")
				.append("					SALINI-CARGOS+ABONOS													                                        ")
				.append("				END ) ACTUAL ,													                                                    ")
				.append("			CASE WHEN (SUBSTR(CUENTA,1,2)='12' OR SUBSTR(CUENTA,1,3)='116') AND UPPER(STACTA)='A' THEN								")
				.append("					SALINI * -1													                                                    ")
				.append("				ELSE													                                                            ")
				.append("					SALINI													                                                        ")
				.append("				END  ANTERIOR													                                                    ")
				.append("		FROM(													                                                                    ")
				.append("			SELECT 	C.STACTA, 													                                                    ")
				.append("				SUBSTR(C.CUENTA,1,3) CUENTA,														                                ")
				.append("				SUM(C.SALINI) SALINI ,													                                            ")
				.append(abonoActual).append(" ABONOS,").append(cargoActual)
				.append("CARGOS										")
				.append("			FROM CUENTA C														                                                    ")
				.append("			WHERE C.CUENTA >= '1000' AND	C.CUENTA <= '1293' AND		C.SCTA = '' AND		C.IDSECTOR = 2 AND						")
				.append("					(SUBSTR(C.CUENTA, 3, 2) ='00' OR	SUBSTR(C.CUENTA, 4, 1) <>'0')												")
				.append("			GROUP BY C.STACTA, SUBSTR(C.CUENTA,1,3)													                                ")
				.append("		)													                                                                        ")
				.append("	) C													                                                                            ")
				.append("	INNER JOIN MAYCTA MA ON MA.CUENTA = C.CUENTA||'0'													                            ")
				.append(") C INNER JOIN MAYCTA M ON M.CUENTA=C.GRUPO||'00'");

		System.out.println(query.toString());
		return query.toString();
	}

	private String getSumaCargoOAbono(Boolean anterior, String nombre) {
		String suma = "";
		if (anterior) {
			return " 0 ";
		} else {
			for (int i = 1; i <= mesFinalActual; i++) {
				suma += "SUM(DECIMAL (" + nombre + i + ",20,2)) +";
			}
		}
		return suma.substring(0, suma.length() - 1);
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	/**
	 * Gets the edo SF 3211 service.
	 *
	 * @return the edo SF 3211 service
	 */
	public EdoSF3211Service getEdoSF3211Service() {
		return edoSF3211Service;
	}

	/**
	 * Sets the edo SF 3211 service.
	 *
	 * @param edoSF3211Service the new edo SF 3211 service
	 */
	public void setEdoSF3211Service(EdoSF3211Service edoSF3211Service) {
		this.edoSF3211Service = edoSF3211Service;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

}
