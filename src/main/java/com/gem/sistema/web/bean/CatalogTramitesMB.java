package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcTramite;
import com.gem.sistema.business.repository.catalogs.TcTramitesRepository;

@ManagedBean(name = "catalogTramitesMB")
@ViewScoped
public class CatalogTramitesMB extends AbstractSimpleCatalog<TcTramite, TcTramitesRepository> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcTramitesRepository}")
	private TcTramitesRepository tramitesRepository;

	@Override
	public TcTramite getNewType() {
		return new TcTramite();
	}

	@Override
	public Boolean isUsed(TcTramite entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setTramite("");
	}

	@Override
	public TcTramitesRepository getRepository() {
		return this.tramitesRepository;
	}

	public TcTramitesRepository getTramitesRepository() {
		return tramitesRepository;
	}

	public void setTramitesRepository(TcTramitesRepository tramitesRepository) {
		this.tramitesRepository = tramitesRepository;
	}

}
