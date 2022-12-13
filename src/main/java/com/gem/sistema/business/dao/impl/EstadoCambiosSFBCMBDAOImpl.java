package com.gem.sistema.business.dao.impl;

import org.springframework.stereotype.Repository;

import com.gem.sistema.business.dao.EstadoCambiosSFBCMBDAO;

// TODO: Auto-generated Javadoc
/**
 * The Class EstadoCambiosSFBCMBDAOImpl.
 *
 * @author Dalia Tovar
 */
@Repository
public class EstadoCambiosSFBCMBDAOImpl implements EstadoCambiosSFBCMBDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gem.sistema.business.dao.EstadoCambiosSFBCMBDAO#generaQueryActivo(java.
	 * lang.Integer, java.lang.Integer)
	 */
	@Override
	public String generaQueryActivo(Integer idsector, Integer mesInicial, Integer mesFinal,
			Boolean mesAnteriorAcumulado) {
		StringBuilder sSqlActivo = new StringBuilder();
		StringBuilder cargosAnt = new StringBuilder();
		StringBuilder abonosAnt = new StringBuilder();
		StringBuilder cargosAct = new StringBuilder();
		StringBuilder abonosAct = new StringBuilder();
		if (mesAnteriorAcumulado) {
			cargosAnt.append(" 0.00");
			abonosAnt.append(" 0.00");
		} else {
			if (mesFinal == 1) {
				cargosAnt.append(" 0.00");
				abonosAnt.append(" 0.00");
			} else {

				for (int i = mesInicial; i < mesFinal; i++) {
					cargosAnt.append(" NVL(C.CARGOS_" + i + ",0.00)+");
					abonosAnt.append(" NVL(C.ABONOS_" + i + ",0.00)+");

				}
			}
		}
		for (int i = mesInicial; i <= mesFinal; i++) {
			cargosAct.append(" NVL(C.CARGOS_" + i + ",0.00)+");
			abonosAct.append(" NVL(C.ABONOS_" + i + ",0.00)+");

		}

		sSqlActivo.append("SELECT T3.GRUPO,").append(" T3.NATCTA,").append(" T3.CUENTA,").append(" T3.NOMCTA,")
				.append(" (T3.MESACT-T3.MESANT) TOTAL").append(" FROM (SELECT T2.GRUPO,").append(" T2.NATCTA,")
				.append(" T2.CUENTA,").append("T2.NOMCTA,")
				.append(" CASE WHEN T2.CUENTA ='1115' OR T2.CUENTA='1162' THEN T2.SALINI+T2.CARGOSANT-T2.ABONOSANT")
				.append(" ELSE T2.MESANT").append(" END MESANT,")
				.append(" CASE WHEN T2.CUENTA ='1115' OR T2.CUENTA='1162' THEN T2.SALINI+T2.CARGOSACT-T2.ABONOSACT")
				.append(" ELSE T2.MESACT").append(" END MESACT").append(" FROM (SELECT T1.GRUPO,").append(" T1.NATCTA,")
				.append(" T1.CUENTA,").append(" T1.NOMCTA,").append(" T1.SALINI,").append(" T1.CARGOSANT,")
				.append(" T1.ABONOSANT,").append(" T1.CARGOSACT,").append(" T1.ABONOSACT,")
				.append(" CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSANT-T1.ABONOSANT")
				.append(" ELSE T1. SALINI-T1.CARGOSANT+T1.ABONOSANT").append(" END MESANT,")
				.append(" CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSACT-T1.ABONOSACT")
				.append(" ELSE T1. SALINI-T1.CARGOSACT+T1.ABONOSACT").append(" END MESACT")
				.append(" FROM (SELECT SUBSTR(C.CUENTA,1,2) GRUPO,").append(" M.NATCTA NATCTA,")
				.append(" C.CUENTA CUENTA,").append(" C.NOMCTA NOMCTA,").append(" NVL(C.SALINI,0.00) SALINI,")
				.append(" C.NOTCTA NOTCTA,").append(" SUM(").append(cargosAnt.substring(1, cargosAnt.length() - 1))
				.append(") CARGOSANT,").append(" SUM(").append(abonosAnt.substring(1, abonosAnt.length() - 1))
				.append(") ABONOSANT,").append(" SUM(").append(cargosAct.substring(1, cargosAct.length() - 1))
				.append(") CARGOSACT,").append(" SUM(").append(abonosAct.substring(1, abonosAct.length() - 1))
				.append(") ABONOSACT").append(" FROM CUENTA C").append(" JOIN MAYCTA M ON C.CUENTA = M.CUENTA")
				.append(" WHERE C.IDSECTOR=" + idsector).append(" AND (C.CUENTA >='1000' AND C.CUENTA <='1293')")
				.append(" AND C.SCTA =''").append(" AND C.NOTCTA=0").append(" GROUP BY SUBSTR(C.CUENTA,1,2),")
				.append(" M.NATCTA,").append(" C.CUENTA,").append(" C.NOMCTA,").append(" C.SALINI,").append(" C.NOTCTA")
				.append(" ORDER BY C.CUENTA)T1)T2)T3");

		return sSqlActivo.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gem.sistema.business.dao.EstadoCambiosSFBCMBDAO#generaQueryPasivo(java.
	 * lang.Integer, java.lang.Integer)
	 */
	@Override
	public String generaQueryPasivo(Integer idsector, Integer mesInicial, Integer mesFinal,
			Boolean mesAnteriorAcumulado) {

		StringBuilder sSqlPasivo = new StringBuilder();
		StringBuilder cargosAnt = new StringBuilder();
		StringBuilder abonosAnt = new StringBuilder();
		StringBuilder cargosAct = new StringBuilder();
		StringBuilder abonosAct = new StringBuilder();
		if (mesAnteriorAcumulado) {
			cargosAnt.append(" 0.00");
			abonosAnt.append(" 0.00");
		} else {
			if (mesFinal == 1) {
				cargosAnt.append(" 0.00");
				abonosAnt.append(" 0.00");
			} else {

				for (int i = mesInicial; i < mesFinal; i++) {
					cargosAnt.append(" NVL(C.CARGOS_" + i + ",0.00)+");
					abonosAnt.append(" NVL(C.ABONOS_" + i + ",0.00)+");

				}
			}
		}
		for (int i = mesInicial; i <= mesFinal; i++) {
			cargosAct.append(" NVL(C.CARGOS_" + i + ",0.00)+");
			abonosAct.append(" NVL(C.ABONOS_" + i + ",0.00)+");

		}

		sSqlPasivo.append("SELECT T4.GRUPO,").append(" T4.NATCTA,").append(" T4.CUENTA,").append(" T4.NOMCTA,")
				.append(" ((T4.MESACT)-(T4.MESANT))TOTAL").append(" FROM (SELECT T3.GRUPO,").append(" T3.NATCTA,")
				.append("T3.CUENTA,").append(" T3.NOMCTA,").append("  CASE WHEN T3.CUENTA='3241' THEN ")
				.append(" CASE WHEN T3.MESANT < 0 THEN ABS(T3.MESANT)").append(" ELSE (T3.MESANT)*(-1)").append(" END")
				.append(" ELSE T3.MESANT").append(" END MESANT,").append(" CASE WHEN T3.CUENTA='3241' THEN")
				.append(" CASE WHEN T3.MESACT< 0 THEN ABS(T3.MESACT)").append(" ELSE (T3.MESACT)*(-1)").append(" END")
				.append(" ELSE T3.MESACT").append(" END MESACT ").append(" FROM (SELECT T2.GRUPO,")
				.append(" T2.NATCTA,").append(" T2.CUENTA,").append(" T2.NOMCTA,")
				.append(" CASE WHEN T2.CUENTA='3241' AND T2.NATCTA='D' THEN T2.SALINI-T2.CARGOSANT+T2.ABONOSANT")
				.append(" ELSE T2.MESANT").append(" END MESANT,")
				.append(" CASE WHEN T2.CUENTA='3241' AND T2.NATCTA='D' THEN T2.SALINI-T2.CARGOSACT+T2.ABONOSACT")
				.append(" ELSE T2.MESACT").append(" END MESACT").append(" FROM (SELECT T1.GRUPO,").append(" T1.NATCTA,")
				.append(" T1.CUENTA,").append(" T1.NOMCTA,").append(" T1.SALINI,").append(" T1.CARGOSANT,")
				.append(" T1.ABONOSANT,").append(" T1.CARGOSACT,").append(" T1.ABONOSACT,")
				.append(" CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSANT-T1.ABONOSANT")
				.append(" ELSE T1. SALINI-T1.CARGOSANT+T1.ABONOSANT").append(" END MESANT,")
				.append(" CASE WHEN T1.NATCTA ='D' THEN T1. SALINI+T1.CARGOSACT-T1.ABONOSACT")
				.append(" ELSE T1. SALINI-T1.CARGOSACT+T1.ABONOSACT").append(" END MESACT").append(" FROM")
				.append(" (SELECT SUBSTR(C.CUENTA,1,2) GRUPO,").append(" M.NATCTA NATCTA,").append(" C.CUENTA CUENTA ,")
				.append(" C.NOMCTA NOMCTA,").append(" NVL(C.SALINI,0.00) SALINI,").append(" C.NOTCTA NOTCTA,")
				.append(" SUM(").append(cargosAnt.substring(1, cargosAnt.length() - 1)).append(") CARGOSANT,")
				.append(" SUM(").append(abonosAnt.substring(1, abonosAnt.length() - 1)).append(") ABONOSANT,")
				.append(" SUM(").append(cargosAct.substring(1, cargosAct.length() - 1)).append(") CARGOSACT,")
				.append(" SUM(").append(abonosAct.substring(1, abonosAct.length() - 1)).append(") ABONOSACT")
				.append(" FROM CUENTA C").append(" JOIN MAYCTA M ON C.CUENTA = M.CUENTA")
				.append(" WHERE C.IDSECTOR=" + idsector).append(" AND (C.CUENTA >='2000' AND C.CUENTA <='3321')")
				.append(" AND C.SCTA =''").append(" AND C.NOTCTA=0").append(" GROUP BY SUBSTR(C.CUENTA,1,2),")
				.append(" M.NATCTA,").append(" C.CUENTA,").append(" C.NOMCTA,").append(" C.SALINI,").append(" C.NOTCTA")
				.append(" ORDER BY C.CUENTA)T1)T2)T3)T4");

		return sSqlPasivo.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gem.sistema.business.dao.EstadoCambiosSFBCMBDAO#generaQuery3211(java.lang
	 * .Integer, java.lang.Integer)
	 */
	@Override
	public String generaQuery3211(Integer idsector, Integer mesInicial, Integer mesFinal,
			Boolean mesAnteriorAcumulado) {
		StringBuilder cargonant = new StringBuilder();
		StringBuilder abonosant = new StringBuilder();
		StringBuilder cargosact = new StringBuilder();
		StringBuilder abonosact = new StringBuilder();
		StringBuilder sSql = new StringBuilder();
		if (mesAnteriorAcumulado) {
			cargonant.append(" 0.00");
			abonosant.append(" 0.00");
		} else {
			if (mesFinal == 1) {
				cargonant.append(" 0.00");
				abonosant.append(" 0.00");
			} else {

				for (int i = mesInicial; i < mesFinal; i++) {
					cargonant.append(" C.CARGOS_" + i + "+");
					abonosant.append(" C.ABONOS_" + i + "+");
				}
			}
		}
		for (int i = mesInicial; i <= mesFinal; i++) {
			cargosact.append(" C.CARGOS_" + i + "+");
			abonosact.append(" C.ABONOS_" + i + "+");
		}

		sSql.append("SELECT (SUM(T3.VAN4)-SUM(T3.VAN5)) TOTAL_ANT,").append(" (SUM(T3.VACT4)-SUM(T3.VACT5)) TOTAL_ACT")
				.append(" FROM (SELECT ").append(" CASE WHEN T2.GRUPO='4' THEN SUM(T2.VMANT) ELSE 0.00 END VAN4,")
				.append(" CASE WHEN T2.GRUPO='5' THEN SUM(T2.VMANT) ELSE 0.00 END VAN5,")
				.append(" CASE WHEN T2.GRUPO='4' THEN SUM(T2.VMACT) ELSE 0.00 END VACT4,")
				.append(" CASE WHEN T2.GRUPO='5' THEN SUM(T2.VMACT)ELSE 0.00 END VACT5")
				.append(" FROM(SELECT  T1.GRUPO, ").append(" T1.SALINI,")
				.append(" CASE WHEN T1.GRUPO  ='4' THEN (T1.SALINI+ABONOS_ANT-CARGOS_ANT)")
				.append(" ELSE (T1.SALINI-ABONOS_ANT+CARGOS_ANT)").append(" END VMANT,")
				.append(" CASE WHEN T1.GRUPO  ='4' THEN (T1.SALINI+ABONOS_ACT-CARGOS_ACT)")
				.append(" ELSE (T1.SALINI-ABONOS_ACT+CARGOS_ACT)").append(" END VMACT")
				.append(" FROM (SELECT SUBSTR(C.CUENTA,1,1) GRUPO, ").append(" C.CUENTA CUENTA ,")
				.append(" C.SALINI SALINI,").append(" SUM(").append(cargonant.substring(1, cargonant.length() - 1))
				.append(") CARGOS_ANT,").append(" SUM(").append(abonosant.substring(1, abonosant.length() - 1))
				.append(") ABONOS_ANT,").append(" SUM(").append(cargosact.substring(1, cargosact.length() - 1))
				.append(") CARGOS_ACT,").append(" SUM(").append(abonosact.substring(1, abonosact.length() - 1))
				.append(") ABONOS_ACT").append(" FROM CUENTA C").append(" WHERE C.IDSECTOR=" + idsector)
				.append(" AND (((C.CUENTA >='4100' AND C.CUENTA<='4399' AND SUBSTR(C.CUENTA,4,1)<>'0')")
				.append(" OR (C.CUENTA IN ('5100','5200','5300','5400','5600','5700')))").append(" AND C.SCTA ='')")
				.append(" OR (C.CUENTA ='5500' AND C.SCTA<>'' AND C.SSCTA ='' AND C.NOTCTA=0)")
				.append(" GROUP BY C.CUENTA,").append(" C.SALINI").append(" ORDER BY C.CUENTA)T1)T2")
				.append(" GROUP BY T2.GRUPO )T3");

		return sSql.toString();

	}

}
