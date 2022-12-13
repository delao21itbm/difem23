package com.gem.sistema.web.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Catdep;
import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.domain.Xcatpro;
import com.gem.sistema.business.repository.catalogs.CatdepRepository;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.FirmasRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.QueryGeneratorService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.ennum.constans.ConstansEnum;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

// TODO: Auto-generated Javadoc
/**
 * The Class Reporte55MB.
 */
@ManagedBean(name = "reporte55MB")
@ViewScoped
public class Reporte55MB extends BaseDirectReport {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	/** The mes. */
	private String mes;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The secretaria. */
	private String secretaria;

	/** The list clvdep. */
	private List<String> listClvdep;

	/** The tcmes. */
	private TcMes tcmes;

	/** The xcatpro. */
	private Xcatpro xcatpro;

	private Catdep catdep;

	/** The firmas repository. */
	@ManagedProperty("#{firmasRepository}")
	private FirmasRepository firmasRepository;

	/** The catdep repository. */
	@ManagedProperty("#{catdepRepository}")
	private CatdepRepository catdepRepository;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{queryGeneratorService}")
	private QueryGeneratorService queryGeneratorService;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
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
	 * Sets the list mes.
	 *
	 * @param listMes the new list mes
	 */
	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
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
	 * Gets the secretaria.
	 *
	 * @return the secretaria
	 */
	public String getSecretaria() {
		return secretaria;
	}

	/**
	 * Sets the secretaria.
	 *
	 * @param secretaria the new secretaria
	 */
	public void setSecretaria(String secretaria) {
		this.secretaria = secretaria;
	}
	
	/**
	 * Gets the tcmes.
	 *
	 * @return the tcmes
	 */
	public TcMes getTcmes() {
		return tcmes;
	}

	/**
	 * Sets the tcmes.
	 *
	 * @param tcmes the new tcmes
	 */
	public void setTcmes(TcMes tcmes) {
		this.tcmes = tcmes;
	}

	/**
	 * Gets the xcatpro.
	 *
	 * @return the xcatpro
	 */
	public Xcatpro getXcatpro() {
		return xcatpro;
	}

	/**
	 * Sets the xcatpro.
	 *
	 * @param xcatpro the new xcatpro
	 */
	public void setXcatpro(Xcatpro xcatpro) {
		this.xcatpro = xcatpro;
	}

	/**
	 * Gets the firmas repository.
	 *
	 * @return the firmas repository
	 */
	public FirmasRepository getFirmasRepository() {
		return firmasRepository;
	}

	/**
	 * Sets the firmas repository.
	 *
	 * @param firmasRepository the new firmas repository
	 */
	public void setFirmasRepository(FirmasRepository firmasRepository) {
		this.firmasRepository = firmasRepository;
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
	 * Gets the list clvdep.
	 *
	 * @return the list clvdep
	 */
	public List<String> getListClvdep() {
		return listClvdep;
	}

	/**
	 * Sets the list clvdep.
	 *
	 * @param listClvdep the new list clvdep
	 */
	public void setListClvdep(List<String> listClvdep) {
		this.listClvdep = listClvdep;
	}

	/**
	 * Gets the catdep repository.
	 *
	 * @return the catdep repository
	 */
	public CatdepRepository getCatdepRepository() {
		return catdepRepository;
	}

	/**
	 * Sets the catdep repository.
	 *
	 * @param catdepRepository the new catdep repository
	 */
	public void setCatdepRepository(CatdepRepository catdepRepository) {
		this.catdepRepository = catdepRepository;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {

		jasperReporteName = "reporte55";
		endFilename = jasperReporteName + ".pdf";

		listMes = tcMesRepository.findAll();
		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}

		/*
		 * listClvdep =
		 * catdepRepository.getClvdep3ByIdsector(this.getUserDetails().getIdSector());
		 * if (!CollectionUtils.isEmpty(listClvdep)) { secretaria = listClvdep.get(0); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {

		Map<String, Object> parametrs = new HashMap<String, Object>();

		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TcPeriodo periodo = periodoRepositoy.findByTipoPeriodoAndPeriodo(1, Integer.valueOf(mes));
		Catdep catdep = catdepRepository.getCatdepByFirst3(idSector, secretaria);
		TrPuestoFirma firma = new TrPuestoFirma();
			
		parametrs.put("imagen", conctb.getImagePathLeft());
		parametrs.put("imagen2", conctb.getImagePathRigth());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
		parametrs.put("firmaP1", firma.getPuesto().getPuesto());
		parametrs.put("firmaN1", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
		parametrs.put("firmaP2",  firma.getPuesto().getPuesto());
		parametrs.put("firmaN2", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
		parametrs.put("firmaP3",  firma.getPuesto().getPuesto());
		parametrs.put("firmaN3", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
		parametrs.put("firmaP4",  firma.getPuesto().getPuesto());
		parametrs.put("firmaN4", firma.getNombre());
		parametrs.put("anio", conctb.getAnoemp());
		parametrs.put("No.Firmas", 4);
		parametrs.put("sector", idSector);
		parametrs.put("nombreEntidad", conctb.getNomDep());
		parametrs.put("mesName", periodo.getDescripcion());
		parametrs.put("lastDayOfMonth", getLastDayByAnoEmp(Integer.valueOf(mes), conctb.getAnoemp()));
		parametrs.put("claveSecretaria", catdep.getClvdep());
		parametrs.put("nameSecretaria", catdep.getNomdep());
		parametrs.put("sql", queryGeneratorService.queryAvancePresupuestalEgresosSecretaria(idSector,
				Integer.valueOf(mes), secretaria));

		return parametrs;

	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {

			Catdep catdep = catdepRepository.getCatdepByFirst3(this.getUserDetails().getIdSector(), secretaria);
			if (null != catdep) {
				RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
			} else {
				generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Info!", "La Secretaría es incorrecta");
			}
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {

			Catdep catdep = catdepRepository.getCatdepByFirst3(this.getUserDetails().getIdSector(), secretaria);
			if (null != catdep) {
				this.createFilePdfInline();
			} else {
				generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Info!", "La Secretaría es incorrecta");
			}
		}
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

	public Catdep getCatdep() {
		return catdep;
	}

	public void setCatdep(Catdep catdep) {
		this.catdep = catdep;
	}

	public QueryGeneratorService getQueryGeneratorService() {
		return queryGeneratorService;
	}

	public void setQueryGeneratorService(QueryGeneratorService queryGeneratorService) {
		this.queryGeneratorService = queryGeneratorService;
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
