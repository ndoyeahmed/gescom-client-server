package com.mthiam.gescom.services;

import com.mthiam.gescom.models.Configuration;

public interface IConfiguration {

    boolean isNotConfigured();

    boolean saveConfiguration(Configuration configuration);

    boolean notHaveAdmin();

    Configuration getConfiguration();
}
