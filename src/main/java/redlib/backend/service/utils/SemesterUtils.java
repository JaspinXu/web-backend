package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.SemesterDTO;
import redlib.backend.model.Semester;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.SemesterVO;

import java.util.Map;

public class SemesterUtils {
    /**
     * 规范并校验semesterDTO
     *
     * @param semesterDTO 111
     */
    public static void validateSemester(SemesterDTO semesterDTO) {
        FormatUtils.trimFieldToNull(semesterDTO);
        Assert.notNull(semesterDTO, "学期输入数据不能为空");
        Assert.hasText(semesterDTO.getSemesterName(), "学期名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param semester 实体对象
     * 去除了 nameMap 111
     * @return VO对象
     */
    public static SemesterVO convertToVO(Semester semester) {
        SemesterVO semesterVO = new SemesterVO();
        BeanUtils.copyProperties(semester, semesterVO);

        return semesterVO;
    }
}
