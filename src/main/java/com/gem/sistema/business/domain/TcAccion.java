package com.gem.sistema.business.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the TC_ACCIONES database table.
 * 
 */
@Entity
@Table(name = "TC_ACCIONES")
@NamedQuery(name = "TcAccion.findAll", query = "SELECT t FROM TcAccion t")
public class TcAccion extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String clave;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_AT")
	private Date createAt;

	private String descripcion;

	private String nombre;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accion")
	private List<TrAreaAccion> areaAccions = new ArrayList<>();

	public TcAccion() {
		this.clave = StringUtils.EMPTY;
		this.nombre = StringUtils.EMPTY;
		this.descripcion = StringUtils.EMPTY;
	}

	@PrePersist
	public void prepersist() {
		this.createAt = new Date();
	}
	
	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getCreateAt() {
		return this.createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<TrAreaAccion> getAreaAccions() {
		return areaAccions;
	}

	public void setAreaAccions(List<TrAreaAccion> areaAccions) {
		this.areaAccions = areaAccions;
	}

	@Override
	public String toString() {
		return "TcAccion [clave=" + clave + ", createAt=" + createAt + ", descripcion=" + descripcion + ", nombre="
				+ nombre + "]";
	}

}