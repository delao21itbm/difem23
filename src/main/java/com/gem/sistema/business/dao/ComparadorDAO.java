package com.gem.sistema.business.dao;
/*
 *	29/03/2021		Javier Tenorio		Se invoca metodo generaQuery	1.1
 */
import java.util.List;

import com.gem.sistema.business.dto.ComparadorDTO;

public interface ComparadorDAO {
	
	List<ComparadorDTO> comparar(Integer mes, String pathFile);
	/*
	 * JTL29032021
	 */
	String generaQuery(Integer mes);
	/*
	 * JTL29032021
	 */
}
