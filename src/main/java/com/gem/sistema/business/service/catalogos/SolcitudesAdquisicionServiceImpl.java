package com.gem.sistema.business.service.catalogos;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service(value = "solcitudesAdquisicionService")
public class SolcitudesAdquisicionServiceImpl implements SolcitudesAdquisicionService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void eliminarArticulo(Long id) {
		jdbcTemplate.execute("delete from TC_ARTICULOS_ADQUISICIONES  where id=" + id);
	}

}
