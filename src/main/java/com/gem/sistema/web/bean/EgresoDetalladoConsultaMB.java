package com.gem.sistema.web.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.dao.DataAccessException;

import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.EgresoDetalladoService;
import com.gem.sistema.business.service.reportador.ReportValidationException;

@ManagedBean(name = "egresoDetalladoConsultaMB")
@ViewScoped
public class EgresoDetalladoConsultaMB extends BaseDirectReport {

	private String area;
	private String dependencia;
	private String partida;
	private String proyecto;
	private String fuente;
	private List<TrPresupuestoDetallado> egresos = new ArrayList<TrPresupuestoDetallado>();

	@ManagedProperty("#{egresoDetalladoService}")
	private EgresoDetalladoService egresoDetalladoService;

	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	@PostConstruct
	public void init() {
		cargarEgresos();
	}

	public void cargarEgresos() {
		if (area != null && area != "") {
			try {
				Integer.parseInt(area);
			} catch (NumberFormatException e) {
				displayInfoMessage("El clave del area solo puede ser numerica");
				return;
			}
		}
		try {
			egresos = egresoDetalladoService.getAllByFilters(area, dependencia, partida, proyecto, fuente);
			if (egresos.isEmpty()) {
				this.displayInfoMessage("No se encontraron egresos");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			this.displayInfoMessage("No se encontraron egresos");
		}

	}

	public void limpiar() {
		this.area = StringUtils.EMPTY;
		this.dependencia = StringUtils.EMPTY;
		this.proyecto = StringUtils.EMPTY;
		this.partida = StringUtils.EMPTY;
		this.fuente = StringUtils.EMPTY;
		cargarEgresos();
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {

		return null;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {

		return null;
	}

	public void displayInfoMessage(String str) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", str);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	public List<TrPresupuestoDetallado> getEgresos() {
		return egresos;
	}

	public void setEgresos(List<TrPresupuestoDetallado> egresos) {
		this.egresos = egresos;
	}

	public EgresoDetalladoService getEgresoDetalladoService() {
		return egresoDetalladoService;
	}

	public void setEgresoDetalladoService(EgresoDetalladoService egresoDetalladoService) {
		this.egresoDetalladoService = egresoDetalladoService;
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

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

}
