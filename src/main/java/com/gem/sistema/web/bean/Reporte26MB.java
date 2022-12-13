package com.gem.sistema.web.bean;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class Reporte26MB.
 */
@ManagedBean(name = "reporte26MB")
@ViewScoped
public class Reporte26MB extends BaseDirectReport {

	/** The sem. */
	private Integer sem;

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Gets the sem.
	 *
	 * @return the sem
	 */
	public Integer getSem() {
		return sem;
	}

	/**
	 * Sets the sem.
	 *
	 * @param sem the new sem
	 */
	public void setSem(Integer sem) {
		this.sem = sem;
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

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "reporte26";
		endFilename = jasperReporteName + ".pdf";

	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getparametrsReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parametrs = new HashMap<String, Object>();		
		Integer sector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(sector);
		TrPuestoFirma firma = null;

		parametrs.put("pSector", this.getUserDetails().getIdSector());

		if (sem == Integer.valueOf(1)) {
			parametrs.put("pMesIni", 1);
			parametrs.put("pMesFin", 6);
		} else if (sem == Integer.valueOf(2)) {
			parametrs.put("pMesIni", 1);
			parametrs.put("pMesFin", 12);
		}

		parametrs.put("pNomMunicipio", conctb.getNomDep());
		parametrs.put("pNumMunicipio", conctb.getClave());
		switch (conctb.getClave().substring(0, 1)) {
		case "0":
			parametrs.put("pNoFirmas", 5);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F01.getValue());
			parametrs.put("pFL1", firma.getPuesto().getPuesto());
			parametrs.put("pFN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F25.getValue());
			parametrs.put("pFL2", firma.getPuesto().getPuesto());
			parametrs.put("pFN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F03.getValue());
			parametrs.put("pFL3", firma.getPuesto().getPuesto());
			parametrs.put("pFN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F16.getValue());
			parametrs.put("pFL4", firma.getPuesto().getPuesto());
			parametrs.put("pFN4", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F02.getValue());
			parametrs.put("pFL5", firma.getPuesto().getPuesto());
			parametrs.put("pFN5", firma.getNombre());
			
			break;
		case "2":
			parametrs.put("pNoFirmas", 3);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
			parametrs.put("pFL1", firma.getPuesto().getPuesto());
			parametrs.put("pFN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F11.getValue());
			parametrs.put("pFL3", firma.getPuesto().getPuesto());
			parametrs.put("pFN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F16.getValue());
			parametrs.put("pFL5", firma.getPuesto().getPuesto());
			parametrs.put("pFN5", firma.getNombre());
			break;
		case "3":
			parametrs.put("pNoFirmas", 4);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
			parametrs.put("pFL1", firma.getPuesto().getPuesto());
			parametrs.put("pFN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
			parametrs.put("pFL2", firma.getPuesto().getPuesto());
			parametrs.put("pFN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
			parametrs.put("pFL4", firma.getPuesto().getPuesto());
			parametrs.put("pFN4", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F16.getValue());
			parametrs.put("pFL5", firma.getPuesto().getPuesto());
			parametrs.put("pFN5", firma.getNombre());
			break;
		case "4":
			parametrs.put("pNoFirmas", 3);			
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F20.getValue());
			parametrs.put("pFL1", firma.getPuesto().getPuesto());
			parametrs.put("pFN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F21.getValue());
			parametrs.put("pFL3", firma.getPuesto().getPuesto());
			parametrs.put("pFN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F16.getValue());
			parametrs.put("pFL5", firma.getPuesto().getPuesto());
			parametrs.put("pFN5", firma.getNombre());
			break;
		default:
			break;
		}
	  parametrs.put("pImgMuni", conctb.getImagePathRigth());

		return parametrs;
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}
}
