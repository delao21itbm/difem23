package com.gem.sistema.business.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TC_LEGISLACIONES database table.
 * 
 */
@Entity
@Table(name = "TC_LEGISLACIONES")
@NamedQuery(name = "TcLegislacion.findAll", query = "SELECT t FROM TcLegislacion t")
public class TcLegislacion extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String descripcion;

	public TcLegislacion() {
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}