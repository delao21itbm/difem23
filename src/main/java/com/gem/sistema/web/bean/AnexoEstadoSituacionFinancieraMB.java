package com.gem.sistema.web.bean;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class AnexoEstadoSituacionFinancieraMB.
 */
@ManagedBean
@ViewScoped
public class AnexoEstadoSituacionFinancieraMB extends BaseDirectReport {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";
//	private static final String DOWNLOAD_TXT = " jQuery('#form1\\\\:downTxt').click();";

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository contbRepository;
	
	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/** The mes. */
	private Integer mes;

	/** The cuenta inicial. */
	private Integer cuentaInicial;

	/** The cuenta final. */
	private Integer cuentaFinal;

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {

		LOGGER.info(":: En postconsruct AnexoEstadoSituacionFinancieraMB ");
		// reportId = 2;
		jasperReporteName = "AnexoEstadoSituacionFinanciera";
		endFilename = jasperReporteName + ".pdf";

		cuentaInicial = 1000;
		cuentaFinal = 9130;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {

		Map<String, Object> parameters = new java.util.HashMap<String, Object>();

		Integer sector = this.getUserDetails().getIdSector();
		Conctb conctb= contbRepository.findByIdsector(sector);
		TrPuestoFirma firma = new TrPuestoFirma();
		
		parameters.put("P_SECTOR", getUserDetails().getIdSector());
		parameters.put("P_MES", mes);
		parameters.put("P_IMG", sector==1? conctb.getImagePathRigth(): conctb.getImagePathLeft());
		parameters.put("P_CAMPO1", conctb.getNomDep());
		parameters.put("P_CAMPO3", conctb.getAnoemp().toString());
		parameters.put("P_CUENTA_INICIAL", cuentaInicial.toString());
		parameters.put("P_CUENTA_FIN", cuentaFinal.toString());
		
		if (sector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				parameters.put("P_L3", firma.getPuesto().getPuesto());
				parameters.put("P_N3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F04.getValue());
				parameters.put("P_L4", firma.getPuesto().getPuesto());
				parameters.put("P_N4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F05.getValue());
				parameters.put("P_L5", firma.getPuesto().getPuesto());
				parameters.put("P_N5", firma.getNombre());
				break;
			case "2":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parameters.put("P_L3", firma.getPuesto().getPuesto());
				parameters.put("P_N3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F04.getValue());
				parameters.put("P_L4", firma.getPuesto().getPuesto());
				parameters.put("P_N4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F05.getValue());
				parameters.put("P_L5", firma.getPuesto().getPuesto());
				parameters.put("P_N5", firma.getNombre());
				break;
			case "3":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("P_L3", firma.getPuesto().getPuesto());
				parameters.put("P_N3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F04.getValue());
				parameters.put("P_L4", firma.getPuesto().getPuesto());
				parameters.put("P_N4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F05.getValue());
				parameters.put("P_L5", firma.getPuesto().getPuesto());
				parameters.put("P_N5", firma.getNombre());
				break;
			case "4":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F21.getValue());
				parameters.put("P_L3", firma.getPuesto().getPuesto());
				parameters.put("P_N3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F04.getValue());
				parameters.put("P_L4", firma.getPuesto().getPuesto());
				parameters.put("P_N4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
						ConstantsClaveEnnum.CLAVE_F05.getValue());
				parameters.put("P_L5", firma.getPuesto().getPuesto());
				parameters.put("P_N5", firma.getNombre());
				break;
			}
		} else {
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
					ConstantsClaveEnnum.CLAVE_F04.getValue());
			parameters.put("P_L3", firma.getPuesto().getPuesto());
			parameters.put("P_N3", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
					ConstantsClaveEnnum.CLAVE_F05.getValue());
			parameters.put("P_L4", firma.getPuesto().getPuesto());
			parameters.put("P_N4", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, 
					ConstantsClaveEnnum.CLAVE_F27.getValue());
			parameters.put("P_L5", firma.getPuesto().getPuesto());
			parameters.put("P_N5", firma.getNombre());
		}
		
		
		return parameters;
	}

	

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
			;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	public StreamedContent generaReporteSimple(int type) {
		return null;
		/*
		 * Map<String, Object> paramsQuery = new java.util.HashMap<String,
		 * Object>(); paramsQuery.put("ID_REF", new Integer(0)); //FALTA return
		 * reporteadorDirectoImpl.getFileReport(tcReporte,paramsQuery,
		 * reporteName,type);
		 */
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
	 * @param mes
	 *            the new mes
	 */
	public void setMes(Integer mes) {
		this.mes = mes;
	}

	/**
	 * Gets the cuenta inicial.
	 *
	 * @return the cuenta inicial
	 */
	public Integer getCuentaInicial() {
		return cuentaInicial;
	}

	/**
	 * Sets the cuenta inicial.
	 *
	 * @param cuentaInicial
	 *            the new cuenta inicial
	 */
	public void setCuentaInicial(Integer cuentaInicial) {
		this.cuentaInicial = cuentaInicial;
	}

	/**
	 * Gets the cuenta final.
	 *
	 * @return the cuenta final
	 */
	public Integer getCuentaFinal() {
		return cuentaFinal;
	}

	/**
	 * Sets the cuenta final.
	 *
	 * @param cuentaFinal
	 *            the new cuenta final
	 */
	public void setCuentaFinal(Integer cuentaFinal) {
		this.cuentaFinal = cuentaFinal;
	}

	public ConctbRepository getContbRepository() {
		return contbRepository;
	}

	public void setContbRepository(ConctbRepository contbRepository) {
		this.contbRepository = contbRepository;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

}
