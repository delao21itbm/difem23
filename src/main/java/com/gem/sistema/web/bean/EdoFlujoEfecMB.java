package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class EdoFlujoEfecMB.
 */
@ManagedBean(name = "edoFlujoEfecMB")
@ViewScoped
public class EdoFlujoEfecMB extends ReportePeriodos {

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "EdoFlujoEfec";
		endFilename = jasperReporteName + ".pdf";
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
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
		Map<String, Object> parameters = new java.util.HashMap<String, Object>();

		parameters.put("pLastDay", getLastDayByAnoEmp(getMesSelected(), conctb.getAnoemp()));
		parameters.put("pDescripM", getNombreMesSelected().toUpperCase());
		parameters.put("pDescripMInicial", getNombreMesInicial().toUpperCase());
		parameters.put("pAn", conctb.getAnoemp().toString());
		parameters.put("pIdsector", this.getUserDetails().getIdSector());
		parameters.put("pMesFinal", getMesSelected());
		parameters.put("pMesInicial", getMesInicial());

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				parameters.put("pImg1", conctb.getImagePathRigth());
				parameters.put("pImg2", "");
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				parameters.put("pL27", firma.getPuesto().getPuesto());
				parameters.put("pN27", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				parameters.put("pL4", firma.getPuesto().getPuesto());
				parameters.put("pN4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				parameters.put("pL5", firma.getPuesto().getPuesto());
				parameters.put("pN5", firma.getNombre());
				break;
			case "2":
				parameters.put("pImg1", conctb.getImagePathRigth());
				parameters.put("pImg2", "");
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("pL27", firma.getPuesto().getPuesto());
				parameters.put("pN27", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parameters.put("pL4", firma.getPuesto().getPuesto());
				parameters.put("pN4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F12.getValue());
				parameters.put("pL5", firma.getPuesto().getPuesto());
				parameters.put("pN5", firma.getNombre());
				break;
			case "3":
				parameters.put("pImg1", conctb.getImagePathRigth());
				parameters.put("pImg2", "");
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("pL27", firma.getPuesto().getPuesto());
				parameters.put("pN27", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("pL4", firma.getPuesto().getPuesto());
				parameters.put("pN4", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("pL5", firma.getPuesto().getPuesto());
				parameters.put("pN5", firma.getNombre());
				break;
			case "4":
				parameters.put("pImg1", conctb.getImagePathRigth());
				parameters.put("pImg2", "");
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				parameters.put("pL27", firma.getPuesto().getPuesto());
				parameters.put("pN27", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F21.getValue());
				parameters.put("pL5", firma.getPuesto().getPuesto());
				parameters.put("pN5", firma.getNombre());
				break;
			}

		} else {
			parameters.put("pImg1", conctb.getImagePathLeft());
			parameters.put("pImg2", conctb.getImagePathRigth());

			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("pL4", firma.getPuesto().getPuesto());
			parameters.put("pN4", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("pL5", firma.getPuesto().getPuesto());
			parameters.put("pN5", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			parameters.put("pL1", firma.getPuesto().getPuesto());
			parameters.put("pN1", firma.getNombre());
		}
		return parameters;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
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
