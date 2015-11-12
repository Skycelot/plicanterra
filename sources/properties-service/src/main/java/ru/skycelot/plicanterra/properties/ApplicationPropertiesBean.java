package ru.skycelot.plicanterra.properties;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
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
@PermitAll
public class ApplicationPropertiesBean {
    
    private Configuration config;
    
    @PostConstruct
    public void init() {
        try {
            String configFilePath = System.getProperty(PropertiesNames.PROPERTY_FILE_PATH);
            if (configFilePath != null) {
                config = new PropertiesConfiguration(configFilePath);
                ((PropertiesConfiguration) config).setThrowExceptionOnMissing(true);
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
    
    public boolean isAuthenticationEngaged() {
        return config.getBoolean(PropertiesNames.AUTHENTICATION_ENGAGED, true);
    }
}
