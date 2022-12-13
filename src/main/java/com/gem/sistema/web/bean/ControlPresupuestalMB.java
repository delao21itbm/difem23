package com.gem.sistema.web.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;

import com.gem.sistema.business.domain.Cattpo;
import com.gem.sistema.business.domain.Firmas;
import com.gem.sistema.business.domain.Polien;
import com.gem.sistema.business.domain.TcContrato;
import com.gem.sistema.business.domain.TcControlPresupuestario;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TcStatusPago;
import com.gem.sistema.business.domain.TrAreaAccion;
import com.gem.sistema.business.domain.TrPresupuestoArea;
import com.gem.sistema.business.domain.TwAccionContrato;
import com.gem.sistema.business.domain.TwContratoFactura;
import com.gem.sistema.business.repository.catalogs.FirmasRepository;
import com.gem.sistema.business.repository.catalogs.PolienRepository;
import com.gem.sistema.business.repository.catalogs.TcContratosRepository;
import com.gem.sistema.business.repository.catalogs.TcStatusPagoRepository;
import com.gem.sistema.business.service.catalogos.ControlPresupuestarioService;

@ManagedBean(name = "controlPresupuestalMB")
@ViewScoped
public class ControlPresupuestalMB extends AbstractMB {

	private static final String CLAVE_FAM = "FAM";
	private static final String CLAVE_PAD = "PAD";

	private List<TrPresupuestoArea> listAreas;
	private List<TcStatusPago> liStatusPagos;
	private List<TcPeriodo> listMeses;
	private List<Cattpo> listTipos;

	private TcControlPresupuestario fam;
	private TcControlPresupuestario pad;

	private TcContrato tcContrato;
	private TwContratoFactura contratoFactura;
	private TwAccionContrato accionContratoSelected = null;
	private TrAreaAccion areaAccionSelected = null;
	
	private String tabchange;
	private Integer tabindex;
	
	private Boolean toggleFAM = Boolean.FALSE;
	private Boolean togglePAD = Boolean.FALSE;

	@ManagedProperty("#{polienRepository}")
	private PolienRepository polienRepository;
	
	@ManagedProperty("#{tcContratosRepository}")
	private TcContratosRepository contratosRepository;

	@ManagedProperty("#{tcStatusPagoRepository}")
	private TcStatusPagoRepository statusPagoRepository;

	@ManagedProperty(value = "#{controlPresupuestarioService}")
	private ControlPresupuestarioService controlPresupuestarioService;

	@PostConstruct
	public void init() {
//		controlPresupuestarios = controlPresupuestarioService.getControlPresupuestario();
		fam = controlPresupuestarioService.getControlPresupuestario(CLAVE_FAM);
		pad = controlPresupuestarioService.getControlPresupuestario(CLAVE_PAD);
		
		listAreas = controlPresupuestarioService.getAreasWithPresupuesto();
		if(!CollectionUtils.isEmpty(listAreas)) {
			this.areaAccionSelected = listAreas.get(0).getAccions().get(0);
		}
		
		this.tcContrato = new TcContrato();
		this.contratoFactura = new TwContratoFactura();
		this.contratoFactura.setPoliza(new Polien());

		this.liStatusPagos = statusPagoRepository.findAll();
		this.listMeses = controlPresupuestarioService.getActivePeriodos(this.getUserDetails().getIdSector());
		this.listTipos = controlPresupuestarioService.getAllTiposPoliza();
		
		togglePAD = Boolean.TRUE;
		toggleFAM = Boolean.FALSE;
	}
	
	public void onToggleFAM(ToggleEvent event) {

		this.toggleFAM = !toggleFAM;
		this.togglePAD = !togglePAD;

	}

	public void onTogglePAD(ToggleEvent event) {

		this.toggleFAM = !toggleFAM;
		this.togglePAD = !togglePAD;

	}

	public void updateFAM() {
		this.fam = controlPresupuestarioService.saveControlPresupuestario(fam);
		this.listAreas = controlPresupuestarioService.savePresupuestoAreas(listAreas);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!",
				"La información se ha actualizado correctamente."));
	}

	public void onTabChange(TabChangeEvent event) {
		
		this.areaAccionSelected = (TrAreaAccion) event.getData();
		System.out.println(this.areaAccionSelected.getAccion().getDescripcion());
		
		
		Tab tab = event.getTab();
		String msg = "Tab Changed Active Tab: " + areaAccionSelected.getId();
//		System.out.println("::::::::::" + areaAccionSelected.getId());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFOR", msg));
	}

	public void onRowToggle(ToggleEvent event) {

		this.accionContratoSelected = ((TwAccionContrato) event.getData());
//		if (event.getSource() instanceof TwAccionContrato) {
//			System.out.println(event.getData().getClass().toString());
//
//		}
		this.contratoFactura.setAccionContrato(accionContratoSelected);
//		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row State ", null);

//		System.out.println( ((TwAccionContrato) event.getData()).toString());
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void editFactura(TwAccionContrato accionContrato) {
		this.accionContratoSelected = accionContrato;
		this.contratoFactura = new TwContratoFactura();
		this.contratoFactura.setPoliza(new Polien());
		this.contratoFactura.setAccionContrato(accionContratoSelected);
	}

	public void saveFactura() {

		Polien polien = polienRepository.findFirstByMespolAndAnopolAndConpolAndTippolAndIdsectorOrderById(
				contratoFactura.getPoliza().getMespol(), 2022, contratoFactura.getPoliza().getConpol(),
				contratoFactura.getPoliza().getTippol(), this.getUserDetails().getIdSector());

		if (polien != null) {
			this.contratoFactura.setPoliza(polien);
			this.contratoFactura.setAccionContrato(this.accionContratoSelected);
			this.controlPresupuestarioService.saveContratoFactura(contratoFactura);

			listAreas = controlPresupuestarioService.getAreasWithPresupuesto();
			this.contratoFactura = new TwContratoFactura();
			this.contratoFactura.setPoliza(new Polien());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!",
					"La Factura se ha registrado correctamente."));
			RequestContext.getCurrentInstance().execute("PF('facturasDlg').hide();");
			RequestContext.getCurrentInstance().update("facturaForm:dialogId");
			RequestContext.getCurrentInstance().update("form:tabView:areas");
			RequestContext.getCurrentInstance().update("form:messages");

		} else {
			RequestContext.getCurrentInstance().execute("PF('facturasDlg').open();");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn!",
					"Póliza no encontrada, Favor de seleccionar una poliza que se encuentre registrada."));
		}
	}
	
	public void saveContrato() {
		
		TcContrato contrato = contratosRepository.findByNumeroContrato(tcContrato.getNumeroContrato());
		
		if(contrato != null) {
			TwAccionContrato accionContrato = new TwAccionContrato();
			
			accionContrato.setContrato(contrato);
			accionContrato.setAreaAccion(areaAccionSelected);
			
			this.controlPresupuestarioService.saveAccionConytrato(accionContrato);
			
			listAreas = controlPresupuestarioService.getAreasWithPresupuesto();
			tcContrato = new TcContrato();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!",
					"Contrato registrado correctamente."));
			RequestContext.getCurrentInstance().execute("PF('contratosDlg').hide();");
			RequestContext.getCurrentInstance().update("contratoForm:dialogId");
			RequestContext.getCurrentInstance().update("form:tabView:areas");
			RequestContext.getCurrentInstance().update("form:messages");
		}else {
			RequestContext.getCurrentInstance().execute("PF('contratosDlg').open();");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn!",
					"Contrato no encontrado, Favor de seleccionar una contrato valido."));
		}
	}

	public TcControlPresupuestario getFam() {
		return fam;
	}

	public void setFam(TcControlPresupuestario fam) {
		this.fam = fam;
	}

	public TcControlPresupuestario getPad() {
		return pad;
	}

	public void setPad(TcControlPresupuestario pad) {
		this.pad = pad;
	}

	public ControlPresupuestarioService getControlPresupuestarioService() {
		return controlPresupuestarioService;
	}

	public void setControlPresupuestarioService(ControlPresupuestarioService controlPresupuestarioService) {
		this.controlPresupuestarioService = controlPresupuestarioService;
	}

	public List<TrPresupuestoArea> getListAreas() {
		return listAreas;
	}

	public void setListAreas(List<TrPresupuestoArea> listAreas) {
		this.listAreas = listAreas;
	}

	public TwContratoFactura getContratoFactura() {
		return contratoFactura;
	}

	public void setContratoFactura(TwContratoFactura contratoFactura) {
		this.contratoFactura = contratoFactura;
	}

	public TcContratosRepository getContratosRepository() {
		return contratosRepository;
	}

	public void setContratosRepository(TcContratosRepository contratosRepository) {
		this.contratosRepository = contratosRepository;
	}

	public PolienRepository getPolienRepository() {
		return polienRepository;
	}

	public void setPolienRepository(PolienRepository polienRepository) {
		this.polienRepository = polienRepository;
	}

	public TcStatusPagoRepository getStatusPagoRepository() {
		return statusPagoRepository;
	}

	public void setStatusPagoRepository(TcStatusPagoRepository statusPagoRepository) {
		this.statusPagoRepository = statusPagoRepository;
	}

	public List<TcStatusPago> getLiStatusPagos() {
		return liStatusPagos;
	}

	public void setLiStatusPagos(List<TcStatusPago> liStatusPagos) {
		this.liStatusPagos = liStatusPagos;
	}

	public TwAccionContrato getAccionContratoSelected() {
		return accionContratoSelected;
	}

	public void setAccionContratoSelected(TwAccionContrato accionContratoSelected) {
		this.accionContratoSelected = accionContratoSelected;
	}

	public List<TcPeriodo> getListMeses() {
		return listMeses;
	}

	public void setListMeses(List<TcPeriodo> listMeses) {
		this.listMeses = listMeses;
	}

	public List<Cattpo> getListTipos() {
		return listTipos;
	}

	public void setListTipos(List<Cattpo> listTipos) {
		this.listTipos = listTipos;
	}

	public TrAreaAccion getAreaAccionSelected() {
		return areaAccionSelected;
	}

	public void setAreaAccionSelected(TrAreaAccion areaAccionSelected) {
		this.areaAccionSelected = areaAccionSelected;
	}

	public String getTabchange() {
		return tabchange;
	}

	public void setTabchange(String tabchange) {
		this.tabchange = tabchange;
	}

	public Integer getTabindex() {
		return tabindex;
	}

	public void setTabindex(Integer tabindex) {
		this.tabindex = tabindex;
	}

	public Boolean getToggleFAM() {
		return toggleFAM;
	}

	public void setToggleFAM(Boolean toggleFAM) {
		this.toggleFAM = toggleFAM;
	}

	public Boolean getTogglePAD() {
		return togglePAD;
	}

	public void setTogglePAD(Boolean togglePAD) {
		this.togglePAD = togglePAD;
	}

	public TcContrato getTcContrato() {
		return tcContrato;
	}

	public void setTcContrato(TcContrato tcContrato) {
		this.tcContrato = tcContrato;
	}

}
