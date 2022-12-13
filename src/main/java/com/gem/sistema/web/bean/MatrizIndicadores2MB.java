package com.gem.sistema.web.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.data.PageEvent;

import com.gem.sistema.business.domain.Catdaa;
import com.gem.sistema.business.domain.Cpd;
import com.gem.sistema.business.domain.Mir;
import com.gem.sistema.business.domain.Muninep;
import com.gem.sistema.business.domain.TcFicha;
import com.gem.sistema.business.domain.TcMatriz;
import com.gem.sistema.business.domain.TcMatrizNivel;
import com.gem.sistema.business.repository.catalogs.CatdaaRepository;
import com.gem.sistema.business.repository.catalogs.CpdRepository;
import com.gem.sistema.business.repository.catalogs.MuniNepRepository;
import com.gem.sistema.business.repository.catalogs.TcMatrizNivelRepository;
import com.gem.sistema.business.repository.catalogs.TcMatrizRepository;
import com.gem.sistema.business.service.indicadores.MatrizIndicadoresService;
import com.gem.sistema.util.UtilFront;

/**
 * @author Alfredo Neri
 *
 */
@ViewScoped
@ManagedBean(name = "matrizIndicadores2MB")
public class MatrizIndicadores2MB extends AbstractMB {

	private String dependenciaSearch = StringUtils.EMPTY;
	private List<TcMatriz> listMatriz;
	private List<Mir> codigosFinalidad;
	private List<Mir> codigosProposito;
	private List<Mir> codigosComponentes;
	private List<Mir> codigosActividades;
	private List<Catdaa> listAuxiliares;
	private List<Muninep> listProgramas;
	private List<Cpd> listemas;

	private Catdaa auxiliar = new Catdaa();
	private Integer idSector = this.getUserDetails().getIdSector();

	private int currentPage = 0;
	private int currentPageComponente = 0;
	private int currentPageActividad = 0;

	private boolean isEditingEncabezado = false;
	private boolean isEditingFinalidad = false;
	private boolean isEditingProposito = false;
	private boolean isEditingActividad = false;
	private boolean isEditingComponente = false;

	@ManagedProperty("#{matrizIndicadoresService}")
	private MatrizIndicadoresService indicadoresService;

	@ManagedProperty("#{tcMatrizRepository}")
	private TcMatrizRepository matrizRepository;

	@ManagedProperty("#{tcMatrizNivelRepository}")
	private TcMatrizNivelRepository matrizNivelRepository;

	@ManagedProperty("#{catdaaRepository}")
	private CatdaaRepository catdaaRepository;

	@ManagedProperty("#{muniNepRepository}")
	private MuniNepRepository muniNepRepository;

	@ManagedProperty("#{cpdRepository}")
	private CpdRepository cpdRepository;

	@PostConstruct
	public void init() {
		loadListPrincipal();

		if (!listMatriz.isEmpty()) {
			this.loadSecondList(0);
		}
	}

	public void search() {
		if (dependenciaSearch != StringUtils.EMPTY && dependenciaSearch != null) {
			listMatriz = indicadoresService.getMatrizes(idSector, dependenciaSearch);
			currentPage = 0;
			currentPageActividad = 0;
			currentPageComponente = 0;

		}
	}

	public void cleanSearch() {
		this.init();
		this.dependenciaSearch = StringUtils.EMPTY;
	}

	public void editEncabezado() {
		isEditingEncabezado = true;
	}

	public void editFinalidad(String programa) {
		isEditingFinalidad = true;
	}

	public void editProposito(String programa) {
		isEditingProposito = true;
	}

	public void editComponente(String programa) {
		isEditingComponente = true;
	}

	public void editActividad(String programa) {
		isEditingActividad = true;
	}

	public void cancel() {
		isEditingEncabezado = false;
		isEditingFinalidad = false;
		isEditingProposito = false;
		isEditingActividad = false;
		isEditingComponente = false;
	}

	public void saveEncabezado() {

		if (listMatriz.get(currentPage).getDependenciaAuxiliar().getId() != null) {
			if (listMatriz.get(currentPage).getPrograma().getId() != null) {
				if (listMatriz.get(currentPage).getTema().getId() != null) {
					Cpd cpd = cpdRepository.findOne(listMatriz.get(currentPage).getTema().getId());
					Catdaa catdaa = catdaaRepository
							.findOne(listMatriz.get(currentPage).getDependenciaAuxiliar().getId());
					Muninep muninep = muniNepRepository.findOne(listMatriz.get(currentPage).getPrograma().getId());
					listMatriz.get(currentPage).setDependenciaAuxiliar(catdaa);
					listMatriz.get(currentPage).setPrograma(muninep);
					listMatriz.get(currentPage).setTema(cpd);
					matrizRepository.save(listMatriz.get(currentPage));
					this.cancel();
					UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "Success",
							"Se ha actualizado correctamente el encabezado");
				} else {
					UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
							"Favor de Selecionar un Tema");
				}
			} else {
				UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
						"Favor de Selecionar un Programa");
			}
		} else {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
					"Favor de Selecionar una Dependencia Auxiliar");
		}
	}

	public void saveFinalidad(TcMatrizNivel finalidad) {

		if (finalidad.getFicha().getIndicador().getId() > 0) {
			TcFicha ficha = indicadoresService.getFicha(finalidad.getId());

			if (ficha != null) {

				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getFinalidad().getFicha().getIndicador().getId());
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);
			} else {

				ficha = new TcFicha();
				TcMatrizNivel matrizNivel = matrizNivelRepository.findOne(finalidad.getId());
				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getFinalidad().getFicha().getIndicador().getId());

				ficha.setMatrizNivel(matrizNivel);
				ficha.setIndicador(mir);

				indicadoresService.saveFicha(ficha);

			}
			this.loadListPrincipal();
			cancel();
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "Success",
					"Código de Indicador registrado correctamente en la Finalidad");
		} else {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
					"Favor de Seleccionar un Código de Indicador valido");
		}

	}

	public void saveProposito(TcMatrizNivel proposito) {

		if (proposito.getFicha().getIndicador().getId() > 0) {
			TcFicha ficha = indicadoresService.getFicha(proposito.getId());

			if (ficha != null) {

				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getProposito().getFicha().getIndicador().getId());
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);
			} else {

				ficha = new TcFicha();
				TcMatrizNivel matrizNivel = matrizNivelRepository.findOne(proposito.getId());
				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getProposito().getFicha().getIndicador().getId());

				ficha.setMatrizNivel(matrizNivel);
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);

			}
			this.loadListPrincipal();
			cancel();
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "Success",
					"Código de Indicador registrado correctamente en el Proposito");
		} else {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
					"Favor de Seleccionar un Código de Indicador valido");
		}

	}

	public void saveComponente(TcMatrizNivel componente) {

		if (componente.getFicha().getIndicador().getId() > 0) {
			TcFicha ficha = indicadoresService.getFicha(componente.getId());

			if (ficha != null) {

				Mir mir = indicadoresService.getMir(listMatriz.get(currentPage).getComponentes()
						.get(currentPageComponente).getFicha().getIndicador().getId());
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);
			} else {

				ficha = new TcFicha();
				TcMatrizNivel matrizNivel = matrizNivelRepository.findOne(componente.getId());
				Mir mir = indicadoresService.getMir(listMatriz.get(currentPage).getComponentes()
						.get(currentPageComponente).getFicha().getIndicador().getId());

				ficha.setMatrizNivel(matrizNivel);
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);

			}
			this.loadListPrincipal();
			cancel();
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "Success",
					"Código de Indicador registrado correctamente en el Componente");
		} else {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
					"Favor de Seleccionar un Código de Indicador valido");
		}

	}

	public void saveActividad(TcMatrizNivel actividad) {

		if (actividad.getFicha().getIndicador().getId() > 0) {
			TcFicha ficha = indicadoresService.getFicha(actividad.getId());

			if (ficha != null) {

				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getComponentes().get(currentPageComponente).getActividades()
								.get(currentPageActividad).getFicha().getIndicador().getId());
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);
			} else {

				ficha = new TcFicha();
				TcMatrizNivel matrizNivel = matrizNivelRepository.findOne(actividad.getId());
				Mir mir = indicadoresService
						.getMir(listMatriz.get(currentPage).getComponentes().get(currentPageComponente).getActividades()
								.get(currentPageActividad).getFicha().getIndicador().getId());

				ficha.setMatrizNivel(matrizNivel);
				ficha.setIndicador(mir);

				indicadoresService.saveDetalleMatriz(ficha);

			}
			this.loadListPrincipal();
			cancel();
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "Success",
					"Código de Indicador registrado correctamente en la Actividad");
		} else {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error",
					"Favor de Seleccionar un Código de Indicador valido");
		}

	}

	public void deleteIndicador(Long idMatriz, Long idIndicador) {

		indicadoresService.deleteIndicador(idMatriz, idIndicador);
		this.loadListPrincipal();
		cancel();
		UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO,
				"Se ha eliminado correctamente el Código de Indicador",
				"Se ha eliminado correctamente el Código de Indicador");
	}

	public void changePageMatriz(PageEvent event) {
		currentPage = event.getPage();

		TcMatriz matriz = listMatriz.get(currentPage);
		try {

			if (matriz.getExistXcatpro()) {
				this.loadSecondList(currentPage);

				currentPageActividad = 0;
				currentPageComponente = 0;

				RequestContext.getCurrentInstance().execute("PF('comWdg').getPaginator().setPage(0)");
				RequestContext.getCurrentInstance().execute("PF('actWdg').getPaginator().setPage(0)");
			}

		} catch (Exception e) {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_ERROR,
					"Ha ocurrido un Error, Favor de Contactar al Administrador", e.getMessage());
		}

	}

	public void changePageComponente(PageEvent event) {
		currentPageComponente = event.getPage();
		currentPageActividad = 0;

		RequestContext.getCurrentInstance().execute("PF('actWdg').getPaginator().setPage(0)");
	}

	public void changePageActividad(PageEvent event) {
		currentPageActividad = event.getPage();

	}

	private void loadListCombobox(String cvePrograma) {
		codigosFinalidad = indicadoresService.llenarListaCodigoIndicador("NF", cvePrograma);
		codigosProposito = indicadoresService.llenarListaCodigoIndicador("NP", cvePrograma);
		codigosComponentes = indicadoresService.llenarListaCodigoIndicador("NC", cvePrograma);
		codigosActividades = indicadoresService.llenarListaCodigoIndicador("NA", cvePrograma);
	}

	private void loadListPrincipal() {
		listMatriz = indicadoresService.getMatrizes(idSector);
	}

	private void loadSecondList(Integer indexPage) {
		TcMatriz matriz = listMatriz.get(indexPage);
		this.loadListCombobox(matriz.getPrograma().getCampo7().substring(0, 8));
		listAuxiliares = catdaaRepository.getByCatdgm(matriz.getDependencia().getClave(), idSector);
		listProgramas = muniNepRepository.getMuninepsforClvdepAndClvfunAndSector(matriz.getDependencia().getClave(),
				matriz.getPrograma().getCampo7().substring(0, 8), idSector);
		listemas = cpdRepository.getByTema(matriz.getTema().getCvetemas().substring(0, 6));
	}

	public String getdependenciaSearch() {
		return dependenciaSearch;
	}

	public void setdependenciaSearch(String dependenciaSearch) {
		this.dependenciaSearch = dependenciaSearch;
	}

	public MatrizIndicadoresService getIndicadoresService() {
		return indicadoresService;
	}

	public void setIndicadoresService(MatrizIndicadoresService indicadoresService) {
		this.indicadoresService = indicadoresService;
	}

	public CatdaaRepository getCatdaaRepository() {
		return catdaaRepository;
	}

	public void setCatdaaRepository(CatdaaRepository catdaaRepository) {
		this.catdaaRepository = catdaaRepository;
	}

	public List<TcMatriz> getListMatriz() {
		return listMatriz;
	}

	public void setListMatriz(List<TcMatriz> listMatriz) {
		this.listMatriz = listMatriz;
	}

	public List<Catdaa> getListAuxiliares() {
		return listAuxiliares;
	}

	public void setListAuxiliares(List<Catdaa> listAuxiliares) {
		this.listAuxiliares = listAuxiliares;
	}

	public Integer getIdSector() {
		return idSector;
	}

	public void setIdSector(Integer idSector) {
		this.idSector = idSector;
	}

	public List<Mir> getCodigosFinalidad() {
		return codigosFinalidad;
	}

	public void setCodigosFinalidad(List<Mir> codigosFinalidad) {
		this.codigosFinalidad = codigosFinalidad;
	}

	public List<Mir> getCodigosProposito() {
		return codigosProposito;
	}

	public void setCodigosProposito(List<Mir> codigosProposito) {
		this.codigosProposito = codigosProposito;
	}

	public List<Mir> getCodigosComponentes() {
		return codigosComponentes;
	}

	public void setCodigosComponentes(List<Mir> codigosComponentes) {
		this.codigosComponentes = codigosComponentes;
	}

	public List<Mir> getCodigosActividades() {
		return codigosActividades;
	}

	public void setCodigosActividades(List<Mir> codigosActividades) {
		this.codigosActividades = codigosActividades;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isEditingFinalidad() {
		return isEditingFinalidad;
	}

	public void setEditingFinalidad(boolean isEditingFinalidad) {
		this.isEditingFinalidad = isEditingFinalidad;
	}

	public boolean isEditingProposito() {
		return isEditingProposito;
	}

	public void setEditingProposito(boolean isEditingProposito) {
		this.isEditingProposito = isEditingProposito;
	}

	public boolean isEditingActividad() {
		return isEditingActividad;
	}

	public void setEditingActividad(boolean isEditingActividad) {
		this.isEditingActividad = isEditingActividad;
	}

	public boolean isEditingComponente() {
		return isEditingComponente;
	}

	public void setEditingComponente(boolean isEditingComponente) {
		this.isEditingComponente = isEditingComponente;
	}

	public boolean isEditingEncabezado() {
		return isEditingEncabezado;
	}

	public void setEditingEncabezado(boolean isEditingEncabezado) {
		this.isEditingEncabezado = isEditingEncabezado;
	}

	public Catdaa getAuxiliar() {
		return auxiliar;
	}

	public void setAuxiliar(Catdaa auxiliar) {
		this.auxiliar = auxiliar;
	}

	public List<Muninep> getListProgramas() {
		return listProgramas;
	}

	public void setListProgramas(List<Muninep> listProgramas) {
		this.listProgramas = listProgramas;
	}

	public List<Cpd> getListemas() {
		return listemas;
	}

	public void setListemas(List<Cpd> listemas) {
		this.listemas = listemas;
	}

	public CpdRepository getCpdRepository() {
		return cpdRepository;
	}

	public void setCpdRepository(CpdRepository cpdRepository) {
		this.cpdRepository = cpdRepository;
	}

	public MuniNepRepository getMuniNepRepository() {
		return muniNepRepository;
	}

	public void setMuniNepRepository(MuniNepRepository muniNepRepository) {
		this.muniNepRepository = muniNepRepository;
	}

	public TcMatrizRepository getMatrizRepository() {
		return matrizRepository;
	}

	public void setMatrizRepository(TcMatrizRepository matrizRepository) {
		this.matrizRepository = matrizRepository;
	}

	public TcMatrizNivelRepository getMatrizNivelRepository() {
		return matrizNivelRepository;
	}

	public void setMatrizNivelRepository(TcMatrizNivelRepository matrizNivelRepository) {
		this.matrizNivelRepository = matrizNivelRepository;
	}

}
