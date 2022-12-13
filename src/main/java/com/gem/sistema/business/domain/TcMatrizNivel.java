package com.gem.sistema.business.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the TC_MATRIZ_NIVEL database table.
 * 
 * @author Alfredo Neri
 */
@Entity
@Table(name = "TC_MATRIZ_NIVEL")
@NamedQuery(name = "TcMatrizNivel.findAll", query = "SELECT t FROM TcMatrizNivel t")
public class TcMatrizNivel extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_MATRIZ")
	private TcMatriz matriz;

	@ManyToOne
	@JoinColumn(name = "ID_NIVEL")
	private TcNivel nivel;

	private String formula;

	private String frecuencia;

	@Column(name = "MEDIOS_VERIFICACION")
	private String mediosVerificacion;

	private String nombre;

	private String objetivo;

	private String supuesto;

	private String tipo;

	@Column(name = "ID_PADRE")
	private Long padre;
	
	 @OneToOne(mappedBy = "matrizNivel", fetch = FetchType.LAZY,
	            cascade = CascadeType.ALL)
	    private TcFicha ficha;

	@Transient
	private List<TcMatrizNivel> actividades;

	public TcMatriz getMatriz() {
		return matriz;
	}

	public void setMatriz(TcMatriz matriz) {
		this.matriz = matriz;
	}

	public TcNivel getNivel() {
		return nivel;
	}

	public void setNivel(TcNivel nivel) {
		this.nivel = nivel;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getMediosVerificacion() {
		return mediosVerificacion;
	}

	public void setMediosVerificacion(String mediosVerificacion) {
		this.mediosVerificacion = mediosVerificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getSupuesto() {
		return supuesto;
	}

	public void setSupuesto(String supuesto) {
		this.supuesto = supuesto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getPadre() {
		return padre;
	}

	public void setPadre(Long padre) {
		this.padre = padre;
	}

	public List<TcMatrizNivel> getActividades() {
		return actividades;
	}

	public void setActividades(List<TcMatrizNivel> actividades) {
		this.actividades = actividades;
	}

	public TcFicha getFicha() {
		return ficha;
	}

	public void setFicha(TcFicha ficha) {
		this.ficha = ficha;
	}

}
