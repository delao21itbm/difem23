package com.gem.sistema.business.service.catalogos;

import java.util.List;

import com.gem.sistema.business.dto.ComparadorDTO;
/**
 * @author Mateo
 * Fecha Modificacion        Autor                            Descripcion                    Version
 * ------------------   -----------------------------    ---------------------------------  --------
 * 24/03/2021           Julio Cesar de la O Espinoza     Se crea La  Serv                    1.0
 *                                                       para la comparación de presupuesto
 *   
 * 29/03/2021			Javier Tenorio					 Se crea el Servicio para generar 
 * 															Query	 						  1.1
 *
 */
public interface ComparadorService {

	List<ComparadorDTO> comparar(Integer mes, String pathFile);
	/*
	 * JTL29032021
	 */
	String generaQuery(Integer mes);
	/*
	 * JTL29032021
	 */
}
