package com.fusm.programs.external;

import com.fusm.programs.model.external.ColumnPermission;
import com.fusm.programs.model.external.ValidatePermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISafetyMeshService {

    List<ColumnPermission> getColumnsByRoleAndModule(ValidatePermission validatePermission);

}
