package com.gem.sistema.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Mateo
 * Object: ComparadorDTO
 * Fecha Modificacion     Autor                               Descripcion                       Version
 * ------------------  ------------------------------- -------------------------------------   --------
 * 24/03/2021          Julio Cesar de la O Espinoza    Se crea DTO para comparar el presupu-    1.0
 *                                                     esto disponible
 *
 */
public class ComparadorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cuenta;
	private String sCta;
	private String ssCta;
	private String sssCta;
	private String ssssCta;
	private BigDecimal saldo;
	private BigDecimal monto;
	private BigDecimal disponibilidad;
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getsCta() {
		return sCta;
	}
	public void setsCta(String sCta) {
		this.sCta = sCta;
	}
	public String getSsCta() {
		return ssCta;
	}
	public void setSsCta(String ssCta) {
		this.ssCta = ssCta;
	}
	public String getSssCta() {
		return sssCta;
	}
	public void setSssCta(String sssCta) {
		this.sssCta = sssCta;
	}
	public String getSsssCta() {
		return ssssCta;
	}
	public void setSsssCta(String ssssCta) {
		this.ssssCta = ssssCta;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public BigDecimal getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(BigDecimal disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	
	

}
