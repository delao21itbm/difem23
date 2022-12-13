package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

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
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class M_pbrm_0514MB.
 */
@ManagedBean
@ViewScoped
public class M_pbrm_0514MB extends BaseDirectReport {

	/** The mes. */
	private String mes;

	/** The conctb. */
	private Conctb conctb;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	/** The contb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository contbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

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
	 * Gets the contb repository.
	 *
	 * @return the contb repository
	 */
	public ConctbRepository getContbRepository() {
		return contbRepository;
	}

	/**
	 * Sets the contb repository.
	 *
	 * @param contbRepository the new contb repository
	 */
	public void setContbRepository(ConctbRepository contbRepository) {
		this.contbRepository = contbRepository;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {

		jasperReporteName = "m_pbrm_0514";
		endFilename = jasperReporteName + ".pdf";

		listMes = tcMesRepository.findAll();

		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}

	}

	/** The parametros. */
	Map<String, Object> parametros;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		parametros = new HashMap<String, Object>();
		conctb = contbRepository.findByIdsector(getUserDetails().getIdSector());
		Integer sector = this.getUserDetails().getIdSector();
		TrPuestoFirma firma = null;

		parametros.put("year", conctb.getAnoemp());
		parametros.put("mesName", tcMesRepository.findByMes(mes).getDescripcion());
		parametros.put("lastDayOfMonth", getLastDayByAnoEmp(Integer.parseInt(mes), conctb.getAnoemp()));
		parametros.put("pathImage1", conctb.getImagePathLeft());
		parametros.put("pathImage2", conctb.getImagePathRigth());
		parametros.put("municipioName", this.getUserDetails().getMunicipio());
		parametros.put("municipioNo", conctb.getClave());
		parametros.put("idSector", sector);
		
			switch (conctb.getClave().substring(0, 1)) {
			case "0": // AYUNTAMIENTO
				parametros.put("noFirmas", 4);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				parametros.put("firmaL1", firma.getPuesto().getPuesto());
				parametros.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				parametros.put("firmaL2", firma.getPuesto().getPuesto());
				parametros.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F25.getValue());
				parametros.put("firmaL3", firma.getPuesto().getPuesto());
				parametros.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				parametros.put("firmaL4", firma.getPuesto().getPuesto());
				parametros.put("firmaN4", firma.getNombre());
				break;
			case "2": // ODAS (AGUA)
				parametros.put("noFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parametros.put("firmaL1", firma.getPuesto().getPuesto());
				parametros.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parametros.put("firmaL2", firma.getPuesto().getPuesto());
				parametros.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F12.getValue());
				parametros.put("firmaL3", firma.getPuesto().getPuesto());
				parametros.put("firmaN3", firma.getNombre());
				break;
			case "3": // DIF
				parametros.put("noFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parametros.put("firmaL1", firma.getPuesto().getPuesto());
				parametros.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parametros.put("firmaL2", firma.getPuesto().getPuesto());
				parametros.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parametros.put("firmaL3", firma.getPuesto().getPuesto());
				parametros.put("firmaN3", firma.getNombre());
				break;
			case "4": // IMCUFIDE (Deporte)
				parametros.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				parametros.put("firmaL2", firma.getPuesto().getPuesto());
				parametros.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F21.getValue());
				parametros.put("firmaL3", firma.getPuesto().getPuesto());
				parametros.put("firmaN3", firma.getNombre());
				break;
			}
		return parametros;
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

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}
}
