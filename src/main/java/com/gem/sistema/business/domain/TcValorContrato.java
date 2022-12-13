package com.gem.sistema.business.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TC_VALORES_CONTRATOS database table.
 * 
 */
@Entity
@Table(name = "TC_VALORES_CONTRATOS")
@NamedQuery(name = "TcValorContrato.findAll", query = "SELECT t FROM TcValorContrato t")
public class TcValorContrato extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String valor;

	public TcValorContrato() {
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}