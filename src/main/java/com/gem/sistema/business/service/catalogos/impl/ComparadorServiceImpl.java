package com.gem.sistema.business.service.catalogos.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gem.sistema.business.dao.ComparadorDAO;
import com.gem.sistema.business.dto.ComparadorDTO;
import com.gem.sistema.business.service.catalogos.ComparadorService;

/**
 * @author Mateo
 * Fecha Modificacion        Autor                            Descripcion                    Version
 * ------------------   -----------------------------    ---------------------------------  --------
 * 24/03/2021           Julio Cesar de la O Espinoza     Se crea La Implementacion del Serv  1.0
 *                                                       para la comparación de presupuesto
 *                                                       
 * 29/03/201			Javier Tenorio				     Se crea la implementacio de servicio 
 * 														 para genera Query					  1.1
 */

@Service(value = "comparadorService")
public class ComparadorServiceImpl implements ComparadorService {

	@Autowired
	private ComparadorDAO comparadorDAO;

	@Override
	public List<ComparadorDTO> comparar(Integer mes, String pathFile) {
		return comparadorDAO.comparar(mes, pathFile);
	}

	public ComparadorDAO getComparadorDAO() {
		return comparadorDAO;
	}

	public void setComparadorDAO(ComparadorDAO comparadorDAO) {
		this.comparadorDAO = comparadorDAO;
	}
	/*
	 * JTL29032021
	 */
	@Override
	public String generaQuery(Integer mes) {
		return this.comparadorDAO.generaQuery(mes);
	}
	/*
	 * JTL29032021
	 */

}
