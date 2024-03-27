package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.LabDTO;
import redlib.backend.model.Lab;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.LabVO;

import java.util.Map;

public class LabUtils {
    /**
     * 规范并校验labDTO
     *
     * @param labDTO
     */
    public static void validateLab(LabDTO labDTO) {
        FormatUtils.trimFieldToNull(labDTO);
        Assert.notNull(labDTO, "实验室输入数据不能为空");
        Assert.hasText(labDTO.getLabName(), "实验室名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param lab 实体对象
     * @param nameMap
     * @return VO对象
     */
    public static LabVO convertToVO(Lab lab, Map<Integer, String> nameMap) {
        LabVO labVO = new LabVO();
        BeanUtils.copyProperties(lab, labVO);

        labVO.setCreatedByDesc(nameMap.get(lab.getCreatedBy()));
        return labVO;
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param lab 实体对象
     * @param codeMap
     * @return VO对象
     */
    public static LabVO convertToVOFree(Lab lab, Map<String, String> codeMap) {
        LabVO labVO = new LabVO();
        BeanUtils.copyProperties(lab, labVO);

        labVO.setLabCode(codeMap.get(lab.getLabCode()));
        return labVO;
    }
}
