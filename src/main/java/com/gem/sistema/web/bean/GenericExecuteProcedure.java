package com.gem.sistema.web.bean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.collections4.MapUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.gem.sistema.business.service.callsp.ExecutePlService;
import com.gem.sistema.business.service.reportador.BByteArrayInputStream;
import com.gem.sistema.business.service.reportador.JasperGenericService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.web.util.FrontProperty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class GenericExecuteProcedure.
 */
@ManagedBean
@ViewScoped
public abstract class GenericExecuteProcedure extends AbstractMB{
	
	/** The Constant LOGGER. */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseDirectReport.class);
	
	/** The Constant MESSAGE_ERROR. */
	protected static final String MESSAGE_ERROR = FrontProperty.getPropertyValue("catalog.message.error");
	
	/** The procedure name. */
	protected String procedureName;
	
	/** The file name. */
	protected String fileName;
	
	/** The execute pl service. */
	@ManagedProperty("#{executePlService}")
	protected ExecutePlService executePlService;
	
	/**
	 * Gets the procedure name.
	 *
	 * @return the procedure name
	 */
	public String getProcedureName() {
		return procedureName;
	}
	
	/**
	 * Sets the procedure name.
	 *
	 * @param procedureName the new procedure name
	 */
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	
	/**
	 * Gets the parameters reports.
	 *
	 * @return the parameters reports
	 * @throws ReportValidationException the report validation exception
	 */
	abstract public SqlParameterSource getParametersReports() throws ReportValidationException;
	
	/**
	 * Down load file.
	 *
	 * @throws ReportValidationException the report validation exception
	 */
	abstract public void downLoadFile() throws ReportValidationException;
	
	
	
//	public StreamedContent getFileTxt() {
//		StreamedContent streamedContent = null;
//		LOGGER.info(":: Generando reporte de texto plano getFileTxt");
//		// media = null;
//		try {
//			//streamedContent =  generaReporteSimple(ReporteadorDirecto.TXT);
//			final FacesContext context = FacesContext.getCurrentInstance();
//			final ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
//			Map<String, Object> out = executePlService.callProcedure(procedureName, getParametersReports());
//			
//			final String pathFile = servletContext.getRealPath(REPORT_PATH_JASPER +  fileName  );
//			streamedContent =  downLoadFile();
//		} catch (final Exception genericExc) {
//			LOGGER.error("getFileTxt", genericExc);
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, StringUtils.EMPTY, "Hubo un error al generar el reporte, contacte a su administrador.");
//			FacesContext.getCurrentInstance().addMessage(null, message);
//			genericExc.printStackTrace();
//		}
//		return streamedContent;
//	}
//	
//	public InputStream getOutPutFile(String pathFile){
//		InputStream stream = null;
//		final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//		
//		stream = new BByteArrayInputStream(arrayOutputStream.toByteArray());
//		return stream;
//	}

}
