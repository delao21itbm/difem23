package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

// TODO: Auto-generated Javadoc
/**
 * The Class ECPIMB.
 */
@ManagedBean(name = "eCPIMB")
@ViewScoped
public class ECPIMB extends BaseDirectReport {

	/** The mes. */
	private Integer mes;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct RF009115MB ");
		jasperReporteName = "ECPI";
		endFilename = jasperReporteName + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer sector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());
		TrPuestoFirma firma = null;

		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("entidadClave", conctb.getClave());
		paramsReport.put("imagePath1", sector==1? conctb.getImagePathRigth(): conctb.getImagePathLeft());
		paramsReport.put("imagePath2", sector==1? "": conctb.getImagePathRigth());
		paramsReport.put("lastDayOfMonth", getLastDayByAnoEmp(mes, conctb.getAnoemp()));
		paramsReport.put("mesName",
				tcMesRepository.findByMes(StringUtils.leftPad(mes.toString(), 2, "0")).getDescripcion());
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("sql", this.generateQuery(sector, mes));

		switch (conctb.getClave().substring(0, 1)) {
		case "0":
			paramsReport.put("noFirmas", 4);
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F01.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F25.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F03.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F02.getValue());
			paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN4", firma.getNombre());
			break;
		case "2":
			paramsReport.put("noFirmas", 2);
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F11.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			break;
		case "3":
			paramsReport.put("noFirmas", 3);
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F07.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			break;
		case "4":
			paramsReport.put("noFirmas", 2);
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F20.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F21.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			break;
			default:
				break;
		}

		return paramsReport;
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
	 * Generate query.
	 *
	 * @param idSector the id sector
	 * @param mes      the mes
	 * @return the string
	 */
	public String generateQuery(Integer idSector, Integer mes) {
		StringBuilder cargo10 = new StringBuilder();
		StringBuilder abono10 = new StringBuilder();
		StringBuilder cargo50 = new StringBuilder();
		StringBuilder abono50 = new StringBuilder();
		StringBuilder ingreso = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Integer i = 1;

		while (i <= mes) {
			cargo10.append(" NVL(DI.CARGOS_" + i + ",0) + ");
			abono10.append(" NVL(DI.ABONOS_" + i + ",0) + ");
			cargo50.append(" NVL(CI.CARGOS_" + i + ",0) + ");
			abono50.append(" NVL(CI.ABONOS_" + i + ",0) + ");
			ingreso.append(" NVL(II.AUTOI_" + i + ",0) + ");
			i++;
		}
		query.append(
				"SELECT CUENTA||'  '||SCTA||'  '||SSCTA||'  '||SSSCTA||'  '||SSSSCTA CUENTA, NOMCTA, ESTIMADA, MODIFICADO_MES, RECAUDADO_MES, ")
				.append("MODIFICADO_ACUMULADO, RECAUDADO_ACUMULADO, (MODIFICADO_ACUMULADO -RECAUDADO_ACUMULADO) VARABS, ")
				.append("CASE WHEN MODIFICADO_ACUMULADO = 0 THEN 0 ")
				.append("ELSE ABS(((RECAUDADO_ACUMULADO - MODIFICADO_ACUMULADO) / MODIFICADO_ACUMULADO) * 100) END PORCENTAJE ")
				.append("FROM (SELECT CI.CUENTA, SUBSTR(DI.SCTA,7,4) SCTA,SUBSTR(DI.SSCTA,13,3)SSCTA, SUBSTR(DI.SSSCTA,2,3) SSSCTA, DI.SSSSCTA, DI.NOMCTA, ")
				.append("(NVL(DI.SALINI,0) + ((" + abono10.substring(0, abono10.length() - 2) + ") - ("
						+ cargo10.substring(0, cargo10.length() - 2) + "))) ESTIMADA, ")
				.append("NVL(II.AUTOI_" + mes + ",0) MODIFICADO_MES, ")
				.append("(NVL(CI.ABONOS_" + mes + ",0) - NVL(CI.CARGOS_" + mes + ",0)) RECAUDADO_MES, ")
				.append("(" + ingreso.substring(0, ingreso.length() - 2) + ") MODIFICADO_ACUMULADO, ")
				.append("((" + abono50.substring(0, abono50.length() - 2) + ") - ("
						+ cargo50.substring(0, cargo50.length() - 2) + ")) RECAUDADO_ACUMULADO")
				.append(" FROM CUENTA AS DI ")
				.append("INNER JOIN CUENTA AS CI ON CI.CUENTA= '8150' AND CI.SCTA=DI.SCTA AND CI.SSCTA=DI.SSCTA ")
				.append("AND CI.SSSCTA=DI.SSSCTA AND CI.SSSSCTA=DI.SSSSCTA AND CI.IDSECTOR = DI.IDSECTOR ")
				.append("LEFT JOIN INGRESO AS II ON DI.CUENTA = II.CUENTA AND DI.SCTA = II.SCTA AND DI.SSCTA=II.SSCTA ")
				.append("AND DI.SSSCTA = II.SSSCTA AND DI.SSSSCTA=II.SSSSCTA AND DI.IDSECTOR=II.IDSECTOR ")
				.append("WHERE (DI.CUENTA='8110')  AND DI.IDSECTOR=" + idSector)
				.append(" ORDER BY DI.CUENTA,DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA ) T1");

		System.out.println(query.toString());
		return query.toString();
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
			;
		}
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public Integer getMes() {
		return mes;
	}

	/**
	 * Sets the mes.
	 *
	 * @param mes the new mes
	 */
	public void setMes(Integer mes) {
		this.mes = mes;
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

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

}
