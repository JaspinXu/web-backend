package redlib.backend.dao;

import redlib.backend.model.Semester;

public interface SemesterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Semester record);

    int insertSelective(Semester record);

    Semester selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Semester record);

    int updateByPrimaryKey(Semester record);
}