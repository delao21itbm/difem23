package com.gem.sistema.business.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the TC_RECIBOS database table.
 * 
 */
@Entity
@Table(name = "TC_RECIBOS")
@NamedQuery(name = "TcRecibo.findAll", query = "SELECT t FROM TcRecibo t")
public class TcRecibo extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String encargado;

	@Temporal(TemporalType.DATE)
	private Date fecemision;

	@OneToOne
	@JoinColumn(name = "ID_CONTRATO", nullable = false)
	private TcContrato contrato;

	private String obs;

	public TcRecibo() {
	}

	public String getEncargado() {
		return this.encargado;
	}

	public void setEncargado(String encargado) {
		this.encargado = encargado;
	}

	public Date getFecemision() {
		return this.fecemision;
	}

	public void setFecemision(Date fecemision) {
		this.fecemision = fecemision;
	}

	public TcContrato getContrato() {
		return contrato;
	}

	public void setContrato(TcContrato contrato) {
		this.contrato = contrato;
	}

	public String getObs() {
		return this.obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

}