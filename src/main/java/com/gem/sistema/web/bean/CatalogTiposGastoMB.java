package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcTipoGasto;
import com.gem.sistema.business.repository.catalogs.TcTiposGastoRepository;

@ManagedBean(name = "catalogTiposGastoMB")
@ViewScoped
public class CatalogTiposGastoMB extends AbstractSimpleCatalog<TcTipoGasto, TcTiposGastoRepository>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcTiposGastoRepository}")
	private TcTiposGastoRepository tiposGastoRepository;

	@Override
	public TcTipoGasto getNewType() {
		return new TcTipoGasto();
	}

	@Override
	public Boolean isUsed(TcTipoGasto entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setDescripcion("");
		;
	}

	@Override
	public TcTiposGastoRepository getRepository() {
		return this.tiposGastoRepository;
	}

	public TcTiposGastoRepository getTiposGastoRepository() {
		return tiposGastoRepository;
	}

	public void setTiposGastoRepository(TcTiposGastoRepository tiposGastoRepository) {
		this.tiposGastoRepository = tiposGastoRepository;
	}

}
