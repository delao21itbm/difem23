package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcTiposPago;
import com.gem.sistema.business.repository.catalogs.TcTiposPagoRepository;

@ManagedBean(name = "catalogFormasPagoMB")
@ViewScoped
public class CatalogFormasPagoMB extends AbstractSimpleCatalog<TcTiposPago, TcTiposPagoRepository>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcTiposPagoRepository}")
	private TcTiposPagoRepository tcFormasPagoRepository;

	@Override
	public TcTiposPago getNewType() {
		return new TcTiposPago();
	}

	@Override
	public Boolean isUsed(TcTiposPago entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setNombre("");
		;
	}

	@Override
	public TcTiposPagoRepository getRepository() {
		return this.tcFormasPagoRepository;
	}

	public TcTiposPagoRepository getTcFormasPagoRepository() {
		return tcFormasPagoRepository;
	}

	public void setTcFormasPagoRepository(TcTiposPagoRepository tcFormasPagoRepository) {
		this.tcFormasPagoRepository = tcFormasPagoRepository;
	}

	
}
