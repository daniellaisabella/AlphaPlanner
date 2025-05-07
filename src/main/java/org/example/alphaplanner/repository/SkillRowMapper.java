package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Skill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {

    @Override
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Skill(
               rs.getString("skill_name")
        );
    }
}
