package com.gem.sistema.business.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * The persistent class for the TC_ARTICULOS_ADQUISICIONES database table.
 * 
 */
@Entity
@Table(name = "TC_ARTICULOS_ADQUISICIONES")
@NamedQuery(name = "TcArticulosSA.findAll", query = "SELECT t FROM TcArticulosSA t")
public class TcArticulosSA extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1868637895748115259L;

	private Integer renglon;
	private String descripcion;
	@Column(name = "DESCRIPCION_TECNICA")
	private String descripcionTecnica;
	@Column(name = "UNIDAD_MEDIDA")
	private String unidadMedida;
	private String clave;
	private Integer cantidad;
	private BigDecimal precio;
	@Transient
	private BigDecimal subTotal;

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ADQUISICION")
	private TcAdquisicion adquisicion;

	public TcArticulosSA() {
	}

	public Integer getRenglon() {
		return renglon;
	}

	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public BigDecimal getSubTotal() {
		return precio.multiply(new BigDecimal(cantidad));
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public TcAdquisicion getAdquisicion() {
		return adquisicion;
	}

	public void setAdquisicion(TcAdquisicion adquisicion) {
		this.adquisicion = adquisicion;
	}

	public String getDescripcionTecnica() {
		return descripcionTecnica;
	}

	public void setDescripcionTecnica(String descripcionTecnica) {
		this.descripcionTecnica = descripcionTecnica;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

}