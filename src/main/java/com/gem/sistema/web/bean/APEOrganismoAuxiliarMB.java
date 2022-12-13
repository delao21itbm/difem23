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
import com.gem.sistema.business.repository.catalogs.CatdepRepository;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.FirmasRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.QueryGeneratorService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

// TODO: Auto-generated Javadoc
/**
 * The Class Reporte55MB.
 */
@ManagedBean(name = "aPEOrganismoAuxiliarMB")
@ViewScoped
public class APEOrganismoAuxiliarMB extends BaseDirectReport {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	/** The mes. */
	private String mes;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The secretaria. */
	private String secretaria;

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

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{queryGeneratorService}")
	private QueryGeneratorService queryGeneratorService;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;
	
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	@PostConstruct
	public void init() {

		jasperReporteName = "reporte55";
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

		Map<String, Object> parametrs = new HashMap<String, Object>();

		Integer idSector = this.getUserDetails().getIdSector();
		TcPeriodo periodo = periodoRepositoy.findByTipoPeriodoAndPeriodo(1, Integer.valueOf(mes));
		TrPuestoFirma firma = new TrPuestoFirma();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		
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
		parametrs.put("nombreEntidad",conctb.getNomDep());
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

			catdep = catdepRepository.getCatdepByFirstOrganismoAxiliar(this.getUserDetails().getIdSector(),
					secretaria);
			if (null != catdep) {
				RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
			} else {
				generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Info!", "El Organismo Auxiliar es incorrecto");
			}
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {

			catdep = catdepRepository.getCatdepByFirstOrganismoAxiliar(this.getUserDetails().getIdSector(),
					secretaria);
			if (null != catdep) {
				this.createFilePdfInline();
			} else {
				generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Info!", "El Organismo Auxiliar es incorrecto");
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

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public List<TcMes> getListMes() {
		return listMes;
	}

	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
	}

	public String getSecretaria() {
		return secretaria;
	}

	public void setSecretaria(String secretaria) {
		this.secretaria = secretaria;
	}

	public Catdep getCatdep() {
		return catdep;
	}

	public void setCatdep(Catdep catdep) {
		this.catdep = catdep;
	}

	public FirmasRepository getFirmasRepository() {
		return firmasRepository;
	}

	public void setFirmasRepository(FirmasRepository firmasRepository) {
		this.firmasRepository = firmasRepository;
	}

	public CatdepRepository getCatdepRepository() {
		return catdepRepository;
	}

	public void setCatdepRepository(CatdepRepository catdepRepository) {
		this.catdepRepository = catdepRepository;
	}

	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public QueryGeneratorService getQueryGeneratorService() {
		return queryGeneratorService;
	}

	public void setQueryGeneratorService(QueryGeneratorService queryGeneratorService) {
		this.queryGeneratorService = queryGeneratorService;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}
	
	

}
