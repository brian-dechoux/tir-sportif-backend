package com.tirsportif.backend.service;

import com.tirsportif.backend.property.ApiProperties;

abstract class AbstractService {

    protected final ApiProperties apiProperties;

    public AbstractService(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

}
