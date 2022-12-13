package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcLegislacion;
import com.gem.sistema.business.repository.catalogs.TcLegislacionesRepository;

@ManagedBean(name = "catalogLegislacionesMB")
@ViewScoped
public class CatalogLegislacionesMB extends AbstractSimpleCatalog<TcLegislacion, TcLegislacionesRepository>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcLegislacionesRepository}")
	private TcLegislacionesRepository legislacionesRepository;

	@Override
	public TcLegislacion getNewType() {
		return new TcLegislacion();
	}

	@Override
	public Boolean isUsed(TcLegislacion entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setDescripcion("");
		;
	}

	@Override
	public TcLegislacionesRepository getRepository() {
		return this.legislacionesRepository;
	}

	public TcLegislacionesRepository getLegislacionesRepository() {
		return legislacionesRepository;
	}

	public void setLegislacionesRepository(TcLegislacionesRepository legislacionesRepository) {
		this.legislacionesRepository = legislacionesRepository;
	}

}
