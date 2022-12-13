package com.gem.sistema.business.domain;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * The persistent class for the TC_MATRIZ database table.
 * 
 * @author Alfredo Neri
 *
 */
@Entity
@Table(name = "TC_MATRIZ")
@NamedQuery(name = "TcMatriz.findAll", query = "SELECT t FROM TcMatriz t")
public class TcMatriz extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ID_DEPENDENCIA")
	private Catdgm dependencia;

	@ManyToOne
	@Nullable
	@JoinColumn(name = "ID_DEP_AUX")
	private Catdaa dependenciaAuxiliar;

	@ManyToOne
	@JoinColumn(name = "ID_PROGRAMA")
	private Muninep programa;

	@ManyToOne
	@JoinColumn(name = "ID_TEMA")
	private Cpd tema;

	@Column(name = "ID_SECTOR")
	private Integer sector;

	@Transient
	private Boolean existXcatpro = Boolean.FALSE;
	
	@Transient
	private TcMatrizNivel finalidad;

	@Transient
	private TcMatrizNivel proposito;

	@Transient
	private List<TcMatrizNivel> componentes;

	public TcMatriz() {
	}

	public Catdgm getDependencia() {
		return dependencia;
	}

	public void setDependencia(Catdgm dependencia) {
		this.dependencia = dependencia;
	}

	public Catdaa getDependenciaAuxiliar() {
		return dependenciaAuxiliar;
	}

	public void setDependenciaAuxiliar(Catdaa dependenciaAuxiliar) {
		this.dependenciaAuxiliar = dependenciaAuxiliar;
	}

	public Muninep getPrograma() {
		return programa;
	}

	public void setPrograma(Muninep programa) {
		this.programa = programa;
	}

	public Cpd getTema() {
		return tema;
	}

	public void setTema(Cpd tema) {
		this.tema = tema;
	}

	public Integer getSector() {
		return sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	public Boolean getExistXcatpro() {
		return existXcatpro;
	}

	public void setExistXcatpro(Boolean existXcatpro) {
		this.existXcatpro = existXcatpro;
	}

	public TcMatrizNivel getFinalidad() {
		return finalidad;
	}

	public void setFinalidad(TcMatrizNivel finalidad) {
		this.finalidad = finalidad;
	}

	public TcMatrizNivel getProposito() {
		return proposito;
	}

	public void setProposito(TcMatrizNivel proposito) {
		this.proposito = proposito;
	}

	public List<TcMatrizNivel> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<TcMatrizNivel> componentes) {
		this.componentes = componentes;
	}

}