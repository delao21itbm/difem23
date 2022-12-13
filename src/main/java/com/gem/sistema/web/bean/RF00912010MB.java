package com.gem.sistema.web.bean;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.dto.RF00912010DTO;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.GeneratePreviewService;
import com.gem.sistema.business.service.catalogos.RF00912010Service;
import com.gem.sistema.business.service.reportador.ReportadorTXTServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class RF00912010MB.
 */
@ManagedBean(name = "rF00912010MB")
@ViewScoped
public class RF00912010MB extends AbstractMB {

	/** The Constant FOCUS_IN_PREVIEW. */
	private static final String FOCUS_IN_PREVIEW = "PrimeFaces.focus('form1:preViewTxt');";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RF00912010MB.class);

	/** The file name. */
	private String fileName;

	/** The file. */
	private String file;

	/** The mes. */
	private String mes;

	private Integer capitulo;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The txt preview. */
	private StringBuilder txtPreview;

	/** The b pre view. */
	private Boolean bPreView = Boolean.FALSE;

	/** The generate preview service. */
	@ManagedProperty("#{generatePreviewService}")
	private GeneratePreviewService generatePreviewService;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	/** The r F 00912010 service. */
	@ManagedProperty("#{rF00912010Service}")
	private RF00912010Service rF00912010Service;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		listMes = tcMesRepository.findAll();
		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * Sets the mes.
	 *
	 * @param mes the new mes
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * Gets the list mes.
	 *
	 * @return the list mes
	 */
	public List<TcMes> getListMes() {
		return listMes;
	}

	/**
	 * Sets the list mes.
	 *
	 * @param listMes the new list mes
	 */
	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
	}

	/**
	 * Gets the tc mes repository.
	 *
	 * @return the tc mes repository
	 */
	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	/**
	 * Sets the tc mes repository.
	 *
	 * @param tcMesRepository the new tc mes repository
	 */
	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	/**
	 * Gets the r F 00912010 service.
	 *
	 * @return the r F 00912010 service
	 */
	public RF00912010Service getrF00912010Service() {
		return rF00912010Service;
	}

	/**
	 * Sets the r F 00912010 service.
	 *
	 * @param rF00912010Service the new r F 00912010 service
	 */
	public void setrF00912010Service(RF00912010Service rF00912010Service) {
		this.rF00912010Service = rF00912010Service;
	}

	/**
	 * Gets the txt preview.
	 *
	 * @return the txt preview
	 */
	public StringBuilder getTxtPreview() {
		return txtPreview;
	}

	/**
	 * Sets the txt preview.
	 *
	 * @param txtPreview the new txt preview
	 */
	public void setTxtPreview(StringBuilder txtPreview) {
		this.txtPreview = txtPreview;
	}

	/**
	 * Gets the b pre view.
	 *
	 * @return the b pre view
	 */
	public Boolean getbPreView() {
		return bPreView;
	}

	/**
	 * Sets the b pre view.
	 *
	 * @param bPreView the new b pre view
	 */
	public void setbPreView(Boolean bPreView) {
		this.bPreView = bPreView;
	}

	/**
	 * Gets the generate preview service.
	 *
	 * @return the generate preview service
	 */
	public GeneratePreviewService getGeneratePreviewService() {
		return generatePreviewService;
	}

	/**
	 * Sets the generate preview service.
	 *
	 * @param generatePreviewService the new generate preview service
	 */
	public void setGeneratePreviewService(GeneratePreviewService generatePreviewService) {
		this.generatePreviewService = generatePreviewService;
	}

	/** The parameters DTO. */
	private RF00912010DTO parametersDTO;

	/**
	 * Execute procedure.
	 */
	public void executeProcedure() {
		RF00912010DTO parameters = new RF00912010DTO();
		parameters.setIdSector(this.getUserDetails().getIdSector());
		parameters.setiMes(Integer.valueOf(mes));
		parameters.setiCapitulo(capitulo);
		parametersDTO = rF00912010Service.executeProcedure(parameters);
		if (parametersDTO.getoCodError() > 0) {
			generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", parametersDTO.getoMsgError());
			fileName = parametersDTO.getoPathFile() + File.separator + parametersDTO.getoNameFile();
			this.dowbLoadFile();
		} else {
			generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error!", parametersDTO.getoMsgError());
		}
	}

	/**
	 * Dowb load file.
	 */
	public void dowbLoadFile() {
		FileInputStream fileInputStream = null;
		OutputStream output = null;
		try {

			File fileToSend = new File(fileName);
			byte[] ingresoRespFile = new byte[(int) fileToSend.length()];
			fileInputStream = new FileInputStream(fileToSend);
			fileInputStream.read(ingresoRespFile);

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

			file = fileToSend.getName();
			fileToSend.delete();
			response.reset();
			response.setContentType("text/plain");
			response.setCharacterEncoding(ReportadorTXTServiceImpl.CHARSET);
			response.setHeader("Content-disposition", "attachment; filename=\"" + file + "\"");

			output = response.getOutputStream();
			output.write(ingresoRespFile);
			facesContext.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fileInputStream) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}

			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

	}

	/**
	 * Pre view.
	 */
	public void preView() {
		this.setbPreView(Boolean.TRUE);
		RF00912010DTO parameters = new RF00912010DTO();
		parameters.setIdSector(this.getUserDetails().getIdSector());
		parameters.setiMes(Integer.valueOf(mes));
		parameters.setiCapitulo(capitulo);
		parametersDTO = rF00912010Service.executeProcedure(parameters);

		txtPreview = this.generatePreviewService
				.generatePreview(parametersDTO.getoPathFile() + File.separator + parametersDTO.getoNameFile());
		RequestContext.getCurrentInstance().execute(FOCUS_IN_PREVIEW);
	}

	public Integer getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(Integer capitulo) {
		this.capitulo = capitulo;
	}

}
