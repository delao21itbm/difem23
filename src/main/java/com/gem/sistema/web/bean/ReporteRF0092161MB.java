package com.gem.sistema.web.bean;

/*

Object: RF009_2_1_6_1.jrxml

Fecha de Creación                    Autor                             Descripcion                      Versión                        Linea de Codigo

---------------       ---------------------------------     -----------------------------          -----------                     -------------------

10/03/2021            Luis Enrique Longino Nicolas. 		Se elimina expresión de textfield     	1.0                             	
															$F{AUTO1_4} en caso de contener un 
															valor diferente de 0
       
*/
import static com.gem.sistema.util.UtilFront.generateNotificationFront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.callsp.ExecutePlService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
// TODO: Auto-generated Javadoc
import com.gem.sistema.util.ConstantsClaveEnnum;

/**
 * The Class ReporteRF0092161MB.
 */
@ManagedBean(name = "reporteRF0092161MB")
@ViewScoped
public class ReporteRF0092161MB extends BaseDirectReport {

	/** The Constant PROCEDURE_NAME. */
	private static final String PROCEDURE_NAME = "SP_GENERA_TXT_RF0092161";
	
	/** The txt. */
	private StreamedContent txt;

	/** The execute pl service. */
	@ManagedProperty("#{executePlService}")
    protected ExecutePlService executePlService;
	
	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;
	
	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;
	
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;
	
	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	/**
	 * Gets the execute pl service.
	 *
	 * @return the execute pl service
	 */
	public ExecutePlService getExecutePlService() {
		return executePlService;
	}

	/**
	 * Sets the execute pl service.
	 *
	 * @param executePlService the new execute pl service
	 */
	public void setExecutePlService(ExecutePlService executePlService) {
		this.executePlService = executePlService;
	}
	
	/** The out. */
	Map<String, Object> out;
	
	/** The stream. */
	InputStream stream = null;
	
	/**
	 * Gets the txt.
	 *
	 * @return the txt
	 */
	public StreamedContent getTxt() {
		
MapSqlParameterSource parameter = new MapSqlParameterSource();
		
		parameter.addValue("i_idsector", this.getUserDetails().getIdSector());
		
		out = executePlService.callProcedure(PROCEDURE_NAME, parameter);
		
		if(Integer.valueOf(out.get("O_COD_ERROR").toString())>0){
            try {
                stream = new FileInputStream(
                  new File(out.get("O_RUTA_FILE").toString() + "/" + out.get("O_NAME_FILE").toString()));
               } catch (FileNotFoundException e) {
                
                e.printStackTrace();
               }
            txt = new DefaultStreamedContent(stream, "application/txt", out.get("O_NAME_FILE").toString());
               generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", out.get("O_MSG_ERROR").toString());
        }else{
        	generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error!", out.get("O_MSG_ERROR").toString());
        }
		
		return txt;
	}

	/**
	 * Sets the txt.
	 *
	 * @param txt the new txt
	 */
	public void setTxt(StreamedContent txt) {
		this.txt = txt;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "RF009_2_1_6_1";
		endFilename = jasperReporteName + ".pdf";
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
				
		parameters.put("anio", conctb.getAnoemp());
		parameters.put("municipio", conctb.getNomDep());
		parameters.put("pathImg", conctb.getImagePathLeft());
		parameters.put("pathImg2", conctb.getImagePathRigth());
		parameters.put("idSector", this.getUserDetails().getIdSector());
		
		if (idSector == 2) {
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F07.getValue());
			parameters.put("firmaP1", firma.getPuesto().getPuesto());
			parameters.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("firmaP2", firma.getPuesto().getPuesto());
			parameters.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("firmaP3", firma.getPuesto().getPuesto());
			parameters.put("firmaN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			parameters.put("firmaP4", firma.getPuesto().getPuesto());
			parameters.put("firmaN4", firma.getNombre());
		}
		return parameters;
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

}
