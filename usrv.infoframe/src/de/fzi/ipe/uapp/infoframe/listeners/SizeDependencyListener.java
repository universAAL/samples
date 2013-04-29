package de.fzi.ipe.uapp.infoframe.listeners;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;
import org.universAAL.ucc.configuration.model.validators.IntMinMaxValidator;
import de.fzi.ipe.uapp.infoframe.validation.RowValidator;

/**
 * This Listener calculate the maximum amount of rows for the infoframe image.
 * This depends on the height, the font and the font scale.
 * @author Sebastian Schoebinger
 *
 */

public class SizeDependencyListener implements OnConfigurationChangedListener {

	static Logger logger = LoggerFactory.getLogger(SizeDependencyListener.class);
	
	int fullHeight, fullWidth;
	double fontScale;
	String fontName;
	
	int topOffset;
	int headers;
	int rows;
	
	@Override
	public void configurationChanged(ConfigOptionRegistry registry,
			ConfigurationOption option) {
		logger.debug("SizeDependencyListener was called!");
		refreshValues(registry);
		setHeightValidator(registry);
		calculateRows(registry);
		setRowLimits(registry);
	}

	private void setRowLimits(ConfigOptionRegistry registry) {
		ConfigurationOption configOption = registry.getConfigOptionForId("mailMsgCount");
		configOption.updateValidatorAttributesForId(RowValidator.class.getName(),new String[]{""+rows});
		
		configOption = registry.getConfigOptionForId("calendarMsgCount");
		configOption.updateValidatorAttributesForId(RowValidator.class.getName(),new String[]{""+rows});
		
		configOption = registry.getConfigOptionForId("MaxDispItems1");
		configOption.updateValidatorAttributesForId(RowValidator.class.getName(),new String[]{""+rows});
		
		configOption = registry.getConfigOptionForId("MaxDispItems2");
		configOption.updateValidatorAttributesForId(RowValidator.class.getName(),new String[]{""+rows});
		
		configOption = registry.getConfigOptionForId("MaxDispItems3");
		configOption.updateValidatorAttributesForId(RowValidator.class.getName(),new String[]{""+rows});
	}

	private void calculateRows(ConfigOptionRegistry registry) {
		headers = 0;
		if("1".equals(registry.getConfigOptionForId("addMailPlugin").getValue())){
			headers += 1;
		}
		if("1".equals(registry.getConfigOptionForId("addCalendarPlugin").getValue())){
			headers += 1;
		}
		if("1".equals(registry.getConfigOptionForId("AddFeedPlugin").getValue())){
			headers += 1;
		}
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage background = gfxConf.createCompatibleImage(fullWidth, fullHeight);
		Graphics gc = background.getGraphics();
		gc.setFont(new Font(fontName, Font.ROMAN_BASELINE, (int)Math.round(20*fontScale)));
		int sumHeaderHeight = (int) (headers * (1.2*gc.getFontMetrics().getHeight()));
		gc.setFont(new Font(fontName, Font.ROMAN_BASELINE, (int)Math.round(16*fontScale)));
		int lineHeight = (int)(1.2*gc.getFontMetrics().getHeight());
		rows = ((fullHeight - topOffset - sumHeaderHeight -(4*lineHeight))/lineHeight)/2;
	}

	private void setHeightValidator(ConfigOptionRegistry registry) {
		ConfigurationOption configOption = registry.getConfigOptionForId("ImageHeight");
		if(fontScale >= 0.5 && fontScale < 1){
			configOption.updateValidatorAttributesForId(IntMinMaxValidator.class.getName(),new String[]{"400","1080"});
		}else if(fontScale >= 1 && fontScale <= 1.5){
			configOption.updateValidatorAttributesForId(IntMinMaxValidator.class.getName(),new String[]{"700","1080"});
		}
	}

	private void refreshValues(ConfigOptionRegistry registry) throws NumberFormatException {
		try{
			fullHeight = Integer.parseInt(registry.getConfigOptionForId("ImageHeight").getValue());
			fullWidth = Integer.parseInt(registry.getConfigOptionForId("ImageWidth").getValue());
			fontScale = Double.parseDouble(registry.getConfigOptionForId("FontScale").getValue());
			fontName = registry.getConfigOptionForId("InfoFont").getValue();
			topOffset = (int)Math.round(fullHeight * 0.20);
		} catch (NumberFormatException e) {
			logger.error(e.toString());
		}
	}

}
