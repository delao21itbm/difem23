package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class RF001028MB.
 */
@ManagedBean(name = "rF001028MB")
@ViewScoped
public class RF001028MB extends BaseDirectReport {

	/** The mes. */
	private String mes;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The conctb. */
	private Conctb conctb;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "RF001028_EdoAn";
		endFilename = jasperReporteName + ".pdf";

		listMes = tcMesRepository.findAll();

		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters;
		TcMes descripMes = tcMesRepository.findByMes(mes);
		parameters = new HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
		parameters.put("pImagenLeft", conctb.getImagePathLeft());
		parameters.put("pImagenRigth", conctb.getImagePathRigth());
		parameters.put("pNomMun", conctb.getNomDep());
		parameters.put("pClveMun", conctb.getClave());
		
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F12.getValue());
		parameters.put("pL2", firma.getPuesto().getPuesto());
		parameters.put("pN2", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F13.getValue());
		parameters.put("pL3", firma.getPuesto().getPuesto());
		parameters.put("pN3", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
		parameters.put("pL4", firma.getPuesto().getPuesto());
		parameters.put("pN4", firma.getNombre());

		parameters.put("pDescripMes", descripMes.getDescripcion());
		parameters.put("pLastDay", getLastDay(Integer.valueOf(mes)));
		parameters.put("pAn", conctb.getAnoemp());
		parameters.put("sqlFirst", this.generaQuery(this.getUserDetails().getIdSector(), Integer.valueOf(mes)));
		parameters.put("sqlSecond", this.generaQuerySecond(this.getUserDetails().getIdSector(), Integer.valueOf(mes)));
		return parameters;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Genera query.
	 *
	 * @param idsector the idsector
	 * @param mes      the mes
	 * @return the string
	 */
	public String generaQuery(Integer idsector, Integer mes) {

		StringBuilder sqlFirst = new StringBuilder();
		StringBuilder sqlSecond = new StringBuilder();
		String cargos = "";
		String abonos = "";
		String inicial = "";
		for (int i = 1; i <= mes; i++) {
			cargos += " NOM.CARGOS_" + i + "+";
			abonos += " NOM.ABONOS_" + i + "+";
			if (i <= 3) {
				inicial += " NOM.CARGOS_" + i + "+";
			}

		}

		cargos = cargos.substring(0, cargos.length() - 1);
		abonos = abonos.substring(0, abonos.length() - 1);
		inicial = inicial.substring(0, inicial.length() - 1);

		sqlFirst.append(
				"SELECT 	CLAVE, ESTIMADO, AMPLI_REDU, MODIFICADO, DEVENGADO, (DEVENGADO - ESTIMADO) DIFERENCIA "
						+ "	FROM ( "

						+ "SELECT 5 CLAVE, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI")
						+ " )DEVENGADO " + "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110'" + "			AND SUBSTR(DI.SCTA,7) = '4151' "
						+ "			AND DI.SSSSCTA <> '' " + "	) T1 UNION ALL "
						+ "SELECT 6, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI") + " ) DEVENGADO "
						+ "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4399' "
						+ "			AND DI.SSSSCTA <> '' " + "			AND DI.SSSSCTA <> '0006' " + "	) T1 "
						+ "	UNION ALL "
						+ "SELECT 7, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI") + " )DEVENGADO "
						+ "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4173' "
						+ "			AND DI.SSSSCTA <> '' " + "	) T1 " + "UNION ALL "
						+ "SELECT 9, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, "
						+ "				CASE WHEN DI.SCTA='0000004223' AND DI.SSSSCTA IN('0004','0005') THEN "
						+ "					DI.SALINI+ " + cargos.replace("NOM", "DI") + "				ELSE "
						+ "					DI.SALINI " + "				END SALINI, "
						+ "				CASE WHEN DI.SCTA='0000004223' AND DI.SSSSCTA IN('0004','0005') THEN "
						+ "					0 " + "				ELSE " + "				( "
						+ cargos.replace("NOM", "DI") + ") -	" + "				( " + abonos.replace("NOM", "DI")
						+ ") " + "				END AMPLI_REDU, " + "				( " + abonos.replace("NOM", "CI")
						+ " ) - " + " ( " + cargos.replace("NOM", "CI") + " ) DEVENGADO " + "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4223'"
						+ "			AND DI.SSSSCTA <> ''" + "	) T1 " + "UNION ALL "
						+ "SELECT 10, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI") + " ) DEVENGADO "
						+ "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4399' "
						+ "			AND DI.SSSSCTA = '0006' " + "	) T1 " + "UNION ALL " + "SELECT 1, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 2, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 3, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 4, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 8, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "	) T2 " + "	ORDER BY 1");

		System.out.println(sqlFirst.toString());
		return sqlFirst.toString();

	}

	public String generaQuerySecond(Integer idsector, Integer mes) {

		StringBuilder sqlSecond = new StringBuilder();
		String cargos = "";
		String abonos = "";
		String inicial = "";
		for (int i = 1; i <= mes; i++) {
			cargos += " NOM.CARGOS_" + i + "+";
			abonos += " NOM.ABONOS_" + i + "+";
			if (i <= 3) {
				inicial += " NOM.CARGOS_" + i + "+";
			}

		}

		cargos = cargos.substring(0, cargos.length() - 1);
		abonos = abonos.substring(0, abonos.length() - 1);
		inicial = inicial.substring(0, inicial.length() - 1);

		sqlSecond.append(
				"SELECT GRUPO, CLAVE, ESTIMADO, AMPLI_REDU, MODIFICADO, DEVENGADO, (DEVENGADO - ESTIMADO) DIFERENCIA "
						+ "	FROM ( " + "SELECT 1 GRUPO, 1 CLAVE, 0 ESTIMADO, 0 AMPLI_REDU, 0 MODIFICADO, 0 DEVENGADO "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 2, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 3, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 4, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 5, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 6, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 8, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 1, 9, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL " + "SELECT 2, 2, 0, 0, 0, 0 "
						+ "	FROM SYSIBM.SYSDUMMY1 " + "UNION ALL "
						+ "SELECT 2, 5 CLAVE, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM(" + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI")
						+ " ) DEVENGADO " + "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4151' "
						+ "			AND DI.SSSSCTA <> '' " + "	) T1 " + "UNION ALL "
						+ "SELECT 2, 7, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI")
						+ " ) DEVENGADO " + "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' AND "
						+ " (SUBSTR(DI.SCTA,7) = '4173' OR  (SUBSTR(DI.SCTA,7) = '4399' AND DI.SSSSCTA <> '0006') ) "
						+ "			AND DI.SSSSCTA <> '' " + "	) T1 " + "UNION ALL "
						+ "SELECT 2, 9, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, "
						+ "				CASE WHEN DI.SCTA='0000004223' AND DI.SSSSCTA IN('0004','0005') THEN "
						+ "					DI.SALINI+ " + inicial.replace("NOM", "DI") + "				ELSE "
						+ "					DI.SALINI " + "				END SALINI, "
						+ "				CASE WHEN DI.SCTA='0000004223' AND DI.SSSSCTA IN('0004','0005') THEN "
						+ "					0 " + "				ELSE " + "				( "
						+ cargos.replace("NOM", "DI") + ") -	" + "				( " + abonos.replace("NOM", "DI")
						+ ") " + "				END AMPLI_REDU, " + "				( " + abonos.replace("NOM", "CI")
						+ " ) - " + " ( " + cargos.replace("NOM", "CI") + " ) DEVENGADO " + "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4223' "
						+ "			AND DI.SSSSCTA <> '' " + "	) T1 " + "UNION ALL "
						+ "SELECT 3, 10, SUM(SALINI) ESTIMADO, SUM(AMPLI_REDU) AMPLI_REDU, SUM(SALINI + AMPLI_REDU) MODIFICADO, SUM(DEVENGADO) DEVENGADO "
						+ "	FROM( " + "		SELECT 	DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.SALINI, "
						+ "				( " + cargos.replace("NOM", "DI") + ") -	" + "				( "
						+ abonos.replace("NOM", "DI") + ") AMPLI_REDU, " + "				( "
						+ abonos.replace("NOM", "CI") + " ) - " + "	( " + cargos.replace("NOM", "CI") + " ) DEVENGADO "
						+ "			FROM CUENTA DI "
						+ "				INNER JOIN CUENTA CI ON DI.IDSECTOR = CI.IDSECTOR "
						+ "									AND	DI.SSSSCTA = CI.SSSSCTA "
						+ "									AND DI.SSSCTA = CI.SSSCTA "
						+ "									AND DI.SSCTA = CI.SSCTA "
						+ "									AND DI.SCTA = CI.SCTA "
						+ "									AND CI.CUENTA = '8150' " + "		WHERE DI.IDSECTOR = "
						+ idsector + "			AND DI.CUENTA = '8110' " + "			AND SUBSTR(DI.SCTA,7) = '4399' "
						+ "			AND DI.SSSSCTA = '0006'" + "	) T1 " + ")T2 " + "ORDER BY 1 ,2 ");

		System.out.println(sqlSecond.toString());
		return sqlSecond.toString();

	}

	public void validateMonth() {
		this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * Sets the mes.
	 *
	 * @param mes the new mes
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * Gets the list mes.
	 *
	 * @return the list mes
	 */
	public List<TcMes> getListMes() {
		return listMes;
	}

	/**
	 * Sets the list mes.
	 *
	 * @param listMes the new list mes
	 */
	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
	}

	/**
	 * Gets the tc mes repository.
	 *
	 * @return the tc mes repository
	 */
	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	/**
	 * Sets the tc mes repository.
	 *
	 * @param tcMesRepository the new tc mes repository
	 */
	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	/**
	 * Gets the conctb.
	 *
	 * @return the conctb
	 */
	public Conctb getConctb() {
		return conctb;
	}

	/**
	 * Sets the conctb.
	 *
	 * @param conctb the new conctb
	 */
	public void setConctb(Conctb conctb) {
		this.conctb = conctb;
	}

	/**
	 * Gets the conctb repository.
	 *
	 * @return the conctb repository
	 */
	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	/**
	 * Sets the conctb repository.
	 *
	 * @param conctbRepository the new conctb repository
	 */
	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}
}
