package ru.petrosoft.newage.propertiesservice;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 */
@Singleton
@Startup
@EJB(name = "java:global/erratum/ApplicationPropertiesBean", beanInterface = ApplicationPropertiesBean.class)
public class ApplicationPropertiesBean {

    private Configuration config;

    @PostConstruct
    public void init() {
        try {
            String configFilePath = System.getProperty(PropertiesNames.PROPERTY_FILE_PATH);
            if (configFilePath != null) {
                config = new PropertiesConfiguration(configFilePath);
            } else {
                throw new IllegalArgumentException(PropertiesNames.PROPERTY_FILE_PATH + " property is not set!");
            }
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getApplicationCode() {
        return config.getString(PropertiesNames.APPLICATION_CODE);
    }
}
