package com.gem.sistema.web.bean;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static com.gem.sistema.util.UtilParseString.parseString;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.springframework.dao.DataAccessException;

import com.gem.sistema.business.domain.Catdep;
import com.gem.sistema.business.domain.Muninep;
import com.gem.sistema.business.domain.Natgas;
import com.gem.sistema.business.domain.TcMovimiento;
import com.gem.sistema.business.domain.TcTipoMovimiento;
import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.dto.PresupuestoDetalladoDTO;
import com.gem.sistema.business.repository.catalogs.CatdepRepository;
import com.gem.sistema.business.repository.catalogs.MuniNepRepository;
import com.gem.sistema.business.repository.catalogs.NatgasRepository;
import com.gem.sistema.business.repository.catalogs.TcAreaRepository;
import com.gem.sistema.business.repository.catalogs.TcMovimientoRepository;
import com.gem.sistema.business.repository.catalogs.TcTipoMovimientoRepository;
import com.gem.sistema.business.repository.catalogs.TrPresupuestoDetalladoRepository;
import com.gem.sistema.business.service.catalogos.EgresoDetalladoService;
import com.gem.sistema.util.Constants;
import com.gem.sistema.web.datamodel.DataModelGeneric;

@ManagedBean(name = "presupuestoEgresosMB")
@ViewScoped
public class PresupuestoEgresosMB extends AbstractMB {

	/** Sector */
	private Integer sector = this.getUserDetails().getIdSector();
	/** Tipos de movimientos disponibles */
	private List<TcTipoMovimiento> tipoMovimientos = new ArrayList<TcTipoMovimiento>();
	/** tipo de movimiento seleccionado */
	private TcTipoMovimiento tipoMovimiento;

	/**
	 * Movimientos actuales en captura deben de concidir en el folio y tipo de
	 * movimiento
	 */
	private List<TcMovimiento> movimientos = new ArrayList<TcMovimiento>();
	/** Movimiento en captura */
	private TcMovimiento movimiento = new TcMovimiento();
	private TcMovimiento movimientoToDelete = new TcMovimiento();
	private DataModelGeneric<TrPresupuestoDetallado> dataModelGeneric;
	/** Meses */
	private List<Integer> meses;
	/** Mes seleccionado */
	private Integer mes;
	/**
	 * Presupuestos disponibles cargados al inio del años ademas de los creados en
	 * el trascurso del mismo
	 */
	private List<String> selectDto;
	private List<TrPresupuestoDetallado> presupuestos;
	private List<TrPresupuestoDetallado> selectDetalles;
	private List<TrPresupuestoDetallado> tempList;
	private List<PresupuestoDetalladoDTO> listDto;
	private List<String> listaIntermedia;
	private List<PresupuestoDetalladoDTO> listaDtoAmpliacion;
	private Boolean banderaEnviar = Boolean.TRUE;
	private BigDecimal totalReduccion = BigDecimal.ZERO;
	private BigDecimal totalAmpliacion = BigDecimal.ZERO;
	private BigDecimal tempTotal = BigDecimal.ZERO;
	private Boolean banderaReduccion = Boolean.TRUE;
	private Boolean banderaVisible = Boolean.TRUE;
	private BigDecimal auxiliar = BigDecimal.ZERO;
	private BigDecimal tempTotalA = BigDecimal.ZERO;

	public void activarBandera() {
		banderaEnviar = Boolean.TRUE;
		banderaVisible = Boolean.TRUE;
		if (tipoMovimiento.getId() >= 4) {
			banderaEnviar = Boolean.FALSE;
			banderaVisible = Boolean.FALSE;
		}
	}

	public Boolean getBanderaEnviar() {
		return banderaEnviar;
	}

	public Boolean getBanderaReduccion() {
		return banderaReduccion;
	}

	public void setBanderaReduccion(Boolean banderaReduccion) {
		this.banderaReduccion = banderaReduccion;
	}

	public void setBanderaEnviar(Boolean banderaEnviar) {
		this.banderaEnviar = banderaEnviar;
	}

	public List<TrPresupuestoDetallado> getTempList() {
		return tempList;
	}

	public void setTempList(List<TrPresupuestoDetallado> tempList) {
		this.tempList = tempList;
	}

	public List<String> getListaIntermedia() {
		return listaIntermedia;
	}

	public void setListaIntermedia(List<String> listaIntermedia) {
		this.listaIntermedia = listaIntermedia;
	}

	public List<PresupuestoDetalladoDTO> getListaDtoAmpliacion() {
		return listaDtoAmpliacion;
	}

	public void setListaDtoAmpliacion(List<PresupuestoDetalladoDTO> listaDtoAmpliacion) {
		this.listaDtoAmpliacion = listaDtoAmpliacion;
	}

	public Integer getSector() {
		return sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	public List<PresupuestoDetalladoDTO> getListDto() {
		return listDto;
	}

	public void setListDto(List<PresupuestoDetalladoDTO> listDto) {
		this.listDto = listDto;
	}

	public List<String> getOrigen() {
		return origen;
	}

	public void setOrigen(List<String> origen) {
		this.origen = origen;
	}

	/** Variable para cptura o busqueda de presupuesto */
	private TrPresupuestoDetallado presupuesto;

	/** Lista de areas disponibles */
	private List<String> areas;
	/** Dependencias diponibles */
	private List<String> dependencias;
	/** Programas disponibles */
	private List<String> programas;
	/** Partidas disponibles */
	private List<String> partidas;

	/** area seleccionada */
	private String area = "";
	/** Dependencia seleccionada */
	private String dependencia = "";
	/** Programa seleccionada */
	private String programa = "";
	/** Partida seleccionada */
	private String partida = "";

	private String fuente = StringUtils.EMPTY;
	/** Folio temporal para generar multiples movimeinto con el mismo folio */
	private Integer folioTemporal;

	/** Folio capturado por usuario para busqueda */
	private Integer folioCaptured;

	/** Cargo capturado */
	private BigDecimal cargo = BigDecimal.ZERO;
	/** Abono capturado */
	private BigDecimal abono = BigDecimal.ZERO;

	@ManagedProperty("#{movimientoRepository}")
	private TcMovimientoRepository movimientoRepository;

	@ManagedProperty("#{tipoMovimientoRepository}")
	private TcTipoMovimientoRepository tipoMovimientoRepository;

	@ManagedProperty("#{tcPresupuestoDetalladoRepository}")
	private TrPresupuestoDetalladoRepository presupuestoRepository;

	@ManagedProperty("#{natgasRepository}")
	private NatgasRepository natgasRepository;

	@ManagedProperty("#{catdepRepository}")
	private CatdepRepository catdepRepository;

	@ManagedProperty("#{muniNepRepository}")
	private MuniNepRepository muniNepRepository;

	@ManagedProperty("#{tcAreaRepository}")
	private TcAreaRepository areaRepository;
	@ManagedProperty("#{egresoDetalladoService}")
	private EgresoDetalladoService detalladoService;
	private DualListModel<String> dualList;
	private List<String> destino;
	private List<String> origen;

	@PostConstruct
	public void init() {

		getDataForLists();

		destino = new ArrayList<String>();
		tempList = new ArrayList<TrPresupuestoDetallado>();
		origen = new ArrayList<String>();
		listDto = new ArrayList<PresupuestoDetalladoDTO>();
		listaIntermedia = new ArrayList<String>();
		listaDtoAmpliacion = new ArrayList<PresupuestoDetalladoDTO>();
		searchBudget();
		dualList = new DualListModel<String>(origen, destino);
		dataModelGeneric = new DataModelGeneric<TrPresupuestoDetallado>(presupuestos);
	}

	/** Imprime y muestra un error de para la excepcion */
	private void proccesDataAccesException(DataAccessException e) {
		displayInfoMessage("Ha ocurrido un error al consultar la informacion ");
		System.out.println(e.getMessage());
	}

	/** Optinene todos los datos para los selects */
	private void getDataForLists() {
		try {
			tipoMovimientos = tipoMovimientoRepository.findAll();
			dependencias = catdepRepository.findAllByIdsectorOrderByClvdep(sector).stream().map(Catdep::getClvdep)
					.collect(Collectors.toList());
			partidas = natgasRepository.findAllByIdsectorOrderByClvgas(sector).stream().map(Natgas::getClvgas)
					.collect(Collectors.toList());
			areas = areaRepository.findAllByOrderByClave().stream().map(a -> a.getClave().toString())
					.collect(Collectors.toList());
			programas = muniNepRepository.findAllByIdsectorOrderByCampo7(sector).stream().map(Muninep::getCampo7)
					.collect(Collectors.toList());
			meses = IntStream.range(1, 13).boxed().collect(Collectors.toList());
			mes = meses.get(0);
			tipoMovimiento = tipoMovimientos.get(0);

		} catch (DataAccessException e) {
			proccesDataAccesException(e);
		}
	}

	/**
	 * Busca presupuesto de acuerdo a los criterios selecionados o sin criterios
	 * optine todos los elementos
	 */
	public void searchBudget() {
		try {
			presupuestos = detalladoService.getAllByFilters(area, dependencia, partida, programa, fuente);
			if (presupuestos.isEmpty()) {
				displayInfoMessage("No se han encontrado presupuestos con los criterios proporcionados");
			} else {
				for (TrPresupuestoDetallado d : presupuestos) {
					BigDecimal total = d.getAutoAbril();
					if (total.compareTo(BigDecimal.ZERO) > 0) {
						String cadenaOrigen = d.getArea().getClave() + Constants.SEPARADOR
								+ d.getDependencia().getClvdep() + Constants.SEPARADOR + d.getProyecto().getCampo7()
								+ Constants.SEPARADOR + d.getFuente().getCuenta() + d.getFuente().getScta()
								+ d.getFuente().getSscta() + d.getFuente().getSsscta() + Constants.SEPARADOR
								+ d.getPartida().getClvgas() + Constants.SEPARADOR + d.getAutoAbril();
						origen.add(cadenaOrigen);

					}
				}
			}

		} catch (DataAccessException e) {
			proccesDataAccesException(e);
		}
	}

	/** Limpia las variables de busqueda y realiza una nueva busqueda */
	public void cleanFilters() {
		area = StringUtils.EMPTY;
		partida = StringUtils.EMPTY;
		dependencia = StringUtils.EMPTY;
		programa = StringUtils.EMPTY;
		fuente = StringUtils.EMPTY;
		listDto = new ArrayList<PresupuestoDetalladoDTO>();
		listaIntermedia = new ArrayList<String>();
		listaDtoAmpliacion = new ArrayList<PresupuestoDetalladoDTO>();
		banderaEnviar = Boolean.TRUE;
		banderaReduccion = Boolean.TRUE;
		banderaVisible = Boolean.TRUE;
		this.limpiarOrigen();

		searchBudget();

	}

	/** Optiene un nuevo folio y si es nulo asigna un 1 */
	public void generateInvoice() {
		if (origen.size() >= 2) {
			folioTemporal = movimientoRepository.getNexFolio();
			if (folioTemporal == null)
				folioTemporal = 1;
			/*
			 * if (tipoMovimiento.getId() >= 4) { banderaEnviar = Boolean.FALSE;
			 * banderaVisible = Boolean.FALSE; }
			 */

			if (tipoMovimiento.getId() <= 3) {
				for (PresupuestoDetalladoDTO pdt : listDto) {
					String cadenaOrigen = pdt.getArea() + Constants.SEPARADOR + pdt.getDependencia()
							+ Constants.SEPARADOR + pdt.getProyecto() + Constants.SEPARADOR + pdt.getFuente()
							+ Constants.SEPARADOR + pdt.getPartida() + Constants.SEPARADOR + pdt.getTotal();
					for (String ori : origen) {
						if (!cadenaOrigen.equals(ori))
							listaIntermedia.add(ori);
					}
				}
			}

			RequestContext.getCurrentInstance().execute("PF('modalDetalles').show()");
			System.out.println("Bandera render->" + this.banderaVisible);
		} else {
			generateNotificationFront(FacesMessage.SEVERITY_WARN, "info",
					"La cuenta seleccionada no contiene mas de un registro");

		}
	}

	public void saveMovement() {

		if (tipoMovimiento.getId() <= 3) {
			listDto.stream().forEach(pd -> saveReduccionAmpliacion(pd, false));
		}
		listaDtoAmpliacion.stream().forEach(pd -> saveReduccionAmpliacion(pd, true));
		this.cleanFilters();
		displayInfoMessage("Se ha guardado correctamente el movimiento");

	}

	public void quitarCero(Integer index) {
		for (int i = 0; i < listDto.size(); i++) {
			if (listDto.get(i) == listDto.get(index)) {
				listDto.get(i).setTotal(null);
			}
		}
	}

	private void saveReduccionAmpliacion(PresupuestoDetalladoDTO pdDto, Boolean bandera) {

		BigDecimal tReduccion = pdDto.getTotal().subtract(pdDto.getReduccion());

		pdDto.setTotal(tReduccion);
		detalladoService.guardarMovimiento(this.getUserDetails().getUsername(), tipoMovimiento, pdDto, mes,
				folioTemporal, bandera);

	}

	public void searchMovementByInvoice() {

		movimientos = movimientoRepository.findAllByFolio(folioCaptured);
		if (movimientos.isEmpty())
			displayInfoMessage("No se encontraron movimientos para el folio:" + folioCaptured);
		presupuesto = new TrPresupuestoDetallado();

	}

	public void searchMovementByTypeAndMonth() {
		movimientos = movimientoRepository.findAllByMesAndTipoMovimiento(mes, tipoMovimiento);
		if (movimientos.isEmpty())
			displayInfoMessage("No se encontraron movimientos para el folio:" + folioCaptured);
		presupuesto = new TrPresupuestoDetallado();
	}

	String[] parseStrings;

	public void onTransfer(TransferEvent event) {

		for (Object item : event.getItems()) {

			listDto.add(cadenaParseada(item.toString()));
			// Solo aplica cuando los tipos de movimiento son Ampliaciones Liquidas,
			// Ampliaciones No liquidas, Recursos complementarios y Reprogramación por
			// Devengados
			if (tipoMovimiento.getId() >= 4) {
				listaDtoAmpliacion.add(cadenaParseada(item.toString()));
			}
		}

	}

	private PresupuestoDetalladoDTO cadenaParseada(String cadenaOrigen) {
		parseStrings = parseString(cadenaOrigen, Constants.SEPARADOR);
		return new PresupuestoDetalladoDTO(Integer.valueOf(parseStrings[0]), parseStrings[1], parseStrings[2],
				parseStrings[3], parseStrings[4], new BigDecimal(parseStrings[5]));
	}

	public void rowSelected(SelectEvent event) {
		if (event.getObject() != null) {
			presupuesto = (TrPresupuestoDetallado) event.getObject();
		}
	}

	public void onRowSelect(SelectEvent event) {
		Integer id = Integer.valueOf(event.getObject().toString());

	}
	String[] filterDetallado=null;
	public void onSelect(SelectEvent event) {

		if (event.getObject() != null) {
			String detallado = (String) event.getObject();
			filterDetallado = parseString(detallado, Constants.SEPARADOR);
			String progr = filterDetallado[2].substring(0, 8);
			String partida = filterDetallado[4].substring(0, 1);
			origen = detalladoService.parseDual(origen, progr, filterDetallado[3], partida, tipoMovimiento,
					Constants.SEPARADOR);
			dualList = new DualListModel<String>(origen, destino);
			System.out.println(detallado);

		}

	}

	public void limpiarOrigen() {
		
		this.searchBudget();
		dualList = new DualListModel<String>(origen, destino);
		totalReduccion = BigDecimal.ZERO;
		listDto = new ArrayList<>();

	}

	public void onUnselect(UnselectEvent event) {

	}

	public void onReorder() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
	}

	public void onRowUnselect(UnselectEvent event) {

	}

	public TcMovimientoRepository getMovimientoRepository() {
		return movimientoRepository;
	}

	public void setMovimientoRepository(TcMovimientoRepository movimientoRepository) {
		this.movimientoRepository = movimientoRepository;
	}

	public TcTipoMovimientoRepository getTipoMovimientoRepository() {
		return tipoMovimientoRepository;
	}

	public void setTipoMovimientoRepository(TcTipoMovimientoRepository tipoMovimientoRepository) {
		this.tipoMovimientoRepository = tipoMovimientoRepository;
	}

	public List<TcTipoMovimiento> getTipoMovimientos() {
		return tipoMovimientos;
	}

	public void setTipoMovimientos(List<TcTipoMovimiento> tipoMovimientos) {
		this.tipoMovimientos = tipoMovimientos;
	}

	public List<TcMovimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<TcMovimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public List<Integer> getMeses() {
		return meses;
	}

	public void setMeses(List<Integer> meses) {
		this.meses = meses;
	}

	public TrPresupuestoDetalladoRepository getPresupuestoRepository() {
		return presupuestoRepository;
	}

	public void setPresupuestoRepository(TrPresupuestoDetalladoRepository presupuestoRepository) {
		this.presupuestoRepository = presupuestoRepository;
	}

	public TrPresupuestoDetallado getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(TrPresupuestoDetallado presupuesto) {
		this.presupuesto = presupuesto;
	}

	public TcTipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TcTipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public TcMovimiento getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(TcMovimiento movimiento) {
		this.movimiento = movimiento;
	}

	public List<TrPresupuestoDetallado> getPresupuestos() {
		return presupuestos;
	}

	public void setPresupuestos(List<TrPresupuestoDetallado> presupuestos) {
		this.presupuestos = presupuestos;
	}

	public List<String> getAreas() {
		return areas;
	}

	public void setAreas(List<String> areas) {
		this.areas = areas;
	}

	public List<String> getDependencias() {
		return dependencias;
	}

	public void setDependencias(List<String> dependencias) {
		this.dependencias = dependencias;
	}

	public List<String> getProgramas() {
		return programas;
	}

	public void setProgramas(List<String> programas) {
		this.programas = programas;
	}

	public List<String> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<String> partidas) {
		this.partidas = partidas;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}

	public Integer getFolioTemporal() {
		return folioTemporal;
	}

	public void setFolioTemporal(Integer folioTemporal) {
		this.folioTemporal = folioTemporal;
	}

	public Integer getFolioCaptured() {
		return folioCaptured;
	}

	public void setFolioCaptured(Integer folioCaptured) {
		this.folioCaptured = folioCaptured;
	}

	public BigDecimal getCargo() {
		return cargo;
	}

	public void setCargo(BigDecimal cargo) {
		this.cargo = cargo;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	public NatgasRepository getNatgasRepository() {
		return natgasRepository;
	}

	public void setNatgasRepository(NatgasRepository natgasRepository) {
		this.natgasRepository = natgasRepository;
	}

	public CatdepRepository getCatdepRepository() {
		return catdepRepository;
	}

	public void setCatdepRepository(CatdepRepository catdepRepository) {
		this.catdepRepository = catdepRepository;
	}

	public MuniNepRepository getMuniNepRepository() {
		return muniNepRepository;
	}

	public void setMuniNepRepository(MuniNepRepository muniNepRepository) {
		this.muniNepRepository = muniNepRepository;
	}

	public TcAreaRepository getAreaRepository() {
		return areaRepository;
	}

	public void setAreaRepository(TcAreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	public EgresoDetalladoService getDetalladoService() {
		return detalladoService;
	}

	public void setDetalladoService(EgresoDetalladoService detalladoService) {
		this.detalladoService = detalladoService;
	}

	public TcMovimiento getMovimientoToDelete() {
		return movimientoToDelete;
	}

	public void setMovimientoToDelete(TcMovimiento movimientoToDelete) {
		this.movimientoToDelete = movimientoToDelete;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public List<TrPresupuestoDetallado> getSelectDetalles() {
		return selectDetalles;
	}

	public void setSelectDetalles(List<TrPresupuestoDetallado> selectDetalles) {
		this.selectDetalles = selectDetalles;
	}

	public DataModelGeneric<TrPresupuestoDetallado> getDataModelGeneric() {
		return dataModelGeneric;
	}

	public void setDataModelGeneric(DataModelGeneric<TrPresupuestoDetallado> dataModelGeneric) {
		this.dataModelGeneric = dataModelGeneric;
	}

	public DualListModel<String> getDualList() {
		return dualList;
	}

	public void enviarDatos(AjaxBehaviorEvent event) {
		selectDto.stream().forEach(System.out::println);

	}

	public void setDualList(DualListModel<String> dualList) {
		this.dualList = dualList;
	}

	public List<String> getDestino() {
		return destino;
	}

	public void setDestino(List<String> destino) {
		this.destino = destino;
	}

	public void onInitRowEdit(final RowEditEvent event) {
		PresupuestoDetalladoDTO pdDto = (PresupuestoDetalladoDTO) event.getObject();
		for (PresupuestoDetalladoDTO temporal : listDto) {
			if (temporal.equals(pdDto)) {

				auxiliar = pdDto.getReduccion();
			}
		}
	}

	public void onRowEdit(RowEditEvent event) {
		totalReduccion = BigDecimal.ZERO;
		tempTotal = BigDecimal.ZERO;

		for (int i = 0; i < listDto.size(); i++) {
			tempTotal = tempTotal.add(listDto.get(i).getTotal());
			totalReduccion = totalReduccion.add(listDto.get(i).getReduccion());
			if (totalReduccion.compareTo(tempTotal) == 1) {

				generateNotificationFront(FacesMessage.SEVERITY_WARN, "info",
						"El total a reducir debe ser menor o igual al total.");
				RequestContext.getCurrentInstance()
						.execute("jQuery('#formDetalles\\\\:dtoEdit span.ui-icon-pencil').eq(" + i
								+ ").each(function(){jQuery(this).click()})");
				totalReduccion = totalReduccion.add(listDto.get(i).getReduccion());
			}

		}
		banderaReduccion = totalReduccion.compareTo(BigDecimal.ZERO) > 0 ? false : true;
	}

	public void onRowEditAmpliaciones(RowEditEvent event) {
		tempTotalA = BigDecimal.ZERO;
		totalAmpliacion = BigDecimal.ZERO;
		for (int i = 0; i < listaDtoAmpliacion.size(); i++) {
			tempTotalA = ((PresupuestoDetalladoDTO) event.getObject()).getAmpliacion();

			if (listaDtoAmpliacion.get(i).getAmpliacion().compareTo(BigDecimal.ZERO) == 0) {
				generateNotificationFront(FacesMessage.SEVERITY_WARN, "info",
						"La cantidad de las ampliaciones debe ser mayor a 0.");
				RequestContext.getCurrentInstance()
						.execute("jQuery('#formDetalles\\\\:dtoAmpliacion span.ui-icon-pencil').eq(" + i
								+ ").each(function(){jQuery(this).click()})");

			} else
				totalAmpliacion = totalAmpliacion.add(listaDtoAmpliacion.get(i).getAmpliacion());
		}
		banderaEnviar = totalAmpliacion.compareTo(totalReduccion) == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	public void onRowCancel(RowEditEvent event) {
		PresupuestoDetalladoDTO pdDto = (PresupuestoDetalladoDTO) event.getObject();
		for (PresupuestoDetalladoDTO temporal : listDto) {
			if (temporal.equals(pdDto)) {
				temporal.setReduccion(auxiliar);
			}
		}

	}

	public List<String> getSelectDto() {
		return selectDto;
	}

	public void setSelectDto(List<String> selectDto) {
		this.selectDto = selectDto;
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void generarAmpliacion() {
		List<PresupuestoDetalladoDTO> l = new ArrayList<PresupuestoDetalladoDTO>();
		for (String item : selectDto) {
			PresupuestoDetalladoDTO tmpDto = cadenaParseada(item);
			l.add(tmpDto);

		}

		listaDtoAmpliacion = l.stream().distinct().collect(Collectors.toList());

	}

	public BigDecimal getTotalReduccion() {
		return totalReduccion;
	}

	public void eliminarAmpliacion(Integer index) {
		totalReduccion = BigDecimal.ZERO;
		for (int i = 0; i < listDto.size(); i++) {
			if (listDto.get(i) == listDto.get(index)) {
				listDto.remove(listDto.get(i));
			} else
				totalReduccion = totalReduccion.add(listDto.get(i).getReduccion());

		}

		banderaReduccion = totalReduccion.compareTo(BigDecimal.ZERO) == 0 ? true : false;

	}

	public void eliminarAmpliaciones(Integer index) {
		totalAmpliacion = BigDecimal.ZERO;
		for (int i = 0; i < listaDtoAmpliacion.size(); i++) {
			if (listaDtoAmpliacion.get(i) == listaDtoAmpliacion.get(index)) {
				listaDtoAmpliacion.remove(listaDtoAmpliacion.get(i));
			}

		}

	}

	public void setTotalReduccion(BigDecimal totalReduccion) {
		this.totalReduccion = totalReduccion;
	}

	public BigDecimal getTotalAmpliacion() {
		return totalAmpliacion;
	}

	public void setTotalAmpliacion(BigDecimal totalAmpliacion) {
		this.totalAmpliacion = totalAmpliacion;
	}

	public Boolean getBanderaVisible() {
		return banderaVisible;
	}

	public void setBanderaVisible(Boolean banderaVisible) {
		this.banderaVisible = banderaVisible;
	}

}
