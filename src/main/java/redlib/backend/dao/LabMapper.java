package redlib.backend.dao;

import redlib.backend.model.Lab;

public interface LabMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lab record);

    int insertSelective(Lab record);

    Lab selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lab record);

    int updateByPrimaryKey(Lab record);
}