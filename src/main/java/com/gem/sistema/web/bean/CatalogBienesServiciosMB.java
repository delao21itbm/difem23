package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcBienServicio;
import com.gem.sistema.business.repository.catalogs.TcBienServicioRepository;

@ManagedBean(name = "catalogBienesServiciosMB")
@ViewScoped
public class CatalogBienesServiciosMB extends AbstractSimpleCatalog<TcBienServicio, TcBienServicioRepository>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcBienServicioRepository}")
	private TcBienServicioRepository tcBienServicioRepository;

	@Override
	public TcBienServicio getNewType() {
		return new TcBienServicio();
	}

	@Override
	public Boolean isUsed(TcBienServicio entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setNombre("");
	}

	@Override
	public TcBienServicioRepository getRepository() {
		return this.tcBienServicioRepository;
	}

	public TcBienServicioRepository getTcBienServicioRepository() {
		return tcBienServicioRepository;
	}

	public void setTcBienServicioRepository(TcBienServicioRepository tcBienServicioRepository) {
		this.tcBienServicioRepository = tcBienServicioRepository;
	}

}
