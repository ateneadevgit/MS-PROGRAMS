package com.fusm.programs.external;

import org.springframework.stereotype.Service;

@Service
public interface ICatalogService {

    String getCatalogItemValue(Integer catalogItemId);

}
