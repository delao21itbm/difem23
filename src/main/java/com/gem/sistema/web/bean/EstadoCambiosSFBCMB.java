package com.gem.sistema.web.bean;

import java.math.BigDecimal;
import java.util.HashMap;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.catalogos.EstadoCambiosSFBCMBService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class EstadoCambiosSFBCMB.
 */
/**
 * @author Alfredo
 *
 */
@ManagedBean(name = "estadoCambiosSFBCMB")
@ViewScoped
public class EstadoCambiosSFBCMB extends ReportePeriodos {
	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty(value = "#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** The estado cambios SFBCMB service. */
	@ManagedProperty(value = "#{estadoCambiosSFBCMBService}")
	private EstadoCambiosSFBCMBService estadoCambiosSFBCMBService;

	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		this.jasperReporteName = "EstadoCambiosSitFin";
		this.endFilename = this.jasperReporteName + ".pdf";
		this.mesAnterior = true;
		changePeriodo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		TrPuestoFirma firma = new TrPuestoFirma();
		conctb = conctbRepository.findByIdsector(idSector);

		String query3211 = estadoCambiosSFBCMBService.generaQuery3211(this.getUserDetails().getIdSector(),
				getMesInicial(), getMesSelected());
		System.out.println("::::->" + query3211);

		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.executeQuery(idSector, getMesInicial(), getMesSelected());
		BigDecimal origen3211 = edoSF3211DTO.getTotalAct().subtract(edoSF3211DTO.getTotalAnt());

		parameters.put("pNomMun", conctb.getNomDep());
		parameters.put("pLastDay", getLastDayByAnoEmp(getMesSelected(), conctb.getAnoemp()));
		parameters.put("pDescripMes", getNombreMesSelected().toUpperCase());
		parameters.put("pDescripMesInicial", getNombreMesInicial().toUpperCase());
		parameters.put("pAn", conctb.getAnoemp());
		parameters.put("pOrigen3211", origen3211);
		parameters.put("pSsqlActivo", estadoCambiosSFBCMBService.generaQueryActivo(this.getUserDetails().getIdSector(),
				getMesInicial(), getMesSelected()));
		parameters.put("pSsqlPasivo", estadoCambiosSFBCMBService.generaQueryPasivo(this.getUserDetails().getIdSector(),
				getMesInicial(), getMesSelected()));
		parameters.put("pSsql3211", query3211);

		parameters.put("pImg1", conctb.getImagePathLeft());
		parameters.put("pImg2", conctb.getImagePathRigth());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
				ConstantsClaveEnnum.CLAVE_F08.getValue());
		parameters.put("pL1", firma.getPuesto().getPuesto());
		parameters.put("pN1", firma.getNombre());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
				ConstantsClaveEnnum.CLAVE_F09.getValue());
		parameters.put("pL2", firma.getPuesto().getPuesto());
		parameters.put("pN2", firma.getNombre());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
				ConstantsClaveEnnum.CLAVE_F10.getValue());
		parameters.put("pL3", firma.getPuesto().getPuesto());
		parameters.put("pN3", firma.getNombre());

		return parameters;
	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(getMesSelected(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(getMesSelected(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.createFilePdfInline();
		}
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	/**
	 * Gets the estado cambios SFBCMB service.
	 *
	 * @return the estado cambios SFBCMB service
	 */
	public EstadoCambiosSFBCMBService getEstadoCambiosSFBCMBService() {
		return estadoCambiosSFBCMBService;
	}

	/**
	 * Sets the estado cambios SFBCMB service.
	 *
	 * @param estadoCambiosSFBCMBService the new estado cambios SFBCMB service
	 */
	public void setEstadoCambiosSFBCMBService(EstadoCambiosSFBCMBService estadoCambiosSFBCMBService) {
		this.estadoCambiosSFBCMBService = estadoCambiosSFBCMBService;
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
