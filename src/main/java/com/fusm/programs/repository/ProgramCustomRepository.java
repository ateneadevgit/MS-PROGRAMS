package com.fusm.programs.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProgramCustomRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> callDynamicSelect(
            String columnNames, int statusType, Integer statusId, Integer facultyId, Integer campusId, String dataType, Boolean hasRestriction, Integer[] programIds) {
        String sql = "SELECT * FROM dynamic_select_columns_2(?, ?, ?, ?, ?, ?, ?) AS t(" + dataType + ")";
        return jdbcTemplate.queryForList(sql, columnNames, statusType, hasRestriction, statusId, facultyId, campusId, programIds);
    }

    public List<Map<String, Object>> callDynamicPagingSelect(
            String columnNames, int statusType, Integer statusId, Integer facultyId, Integer campusId, String dataType, Boolean hasRestriction,
            Integer page, Integer size, Integer[] programIds) {
        String sql = "SELECT * FROM dynamic_select_paginated_columns_2(?, ?, ?, ?, ?, ?, ?, ?, ?) AS t(" + dataType + ")";
        return jdbcTemplate.queryForList(sql, columnNames, statusType, hasRestriction, statusId, facultyId, campusId, page, size, programIds);
    }

}
