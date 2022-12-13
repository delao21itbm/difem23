package com.gem.sistema.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.TcOrigenRecurso;
import com.gem.sistema.business.repository.catalogs.TcOrigenRecursoRepository;

@ManagedBean(name = "catalogOrigenRecursosMB")
@ViewScoped
public class CatalogOrigenRecursosMB extends AbstractSimpleCatalog<TcOrigenRecurso, TcOrigenRecursoRepository>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{tcOrigenRecursoRepository}")
	private TcOrigenRecursoRepository origenRecursoRepository;

	@Override
	public TcOrigenRecurso getNewType() {
		return new TcOrigenRecurso();
	}

	@Override
	public Boolean isUsed(TcOrigenRecurso entity) {
		return false;
	}

	@Override
	public void cleanFieldsBean() {
		getBeanFind().setDescripcion("");
	}

	@Override
	public TcOrigenRecursoRepository getRepository() {
		return this.origenRecursoRepository;
	}

	public TcOrigenRecursoRepository getOrigenRecursoRepository() {
		return origenRecursoRepository;
	}

	public void setOrigenRecursoRepository(TcOrigenRecursoRepository origenRecursoRepository) {
		this.origenRecursoRepository = origenRecursoRepository;
	}

}
