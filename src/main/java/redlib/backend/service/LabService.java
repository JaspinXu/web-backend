package redlib.backend.service;


import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.LabDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.LabVO;

import java.io.InputStream;
import java.util.List;

public interface LabService {
    Page<LabVO> listByPage(LabQueryDTO queryDTO);

    /**
     * 新建实验室
     *
     * @param labDTO 实验室输入对象
     * @return 实验室编码
     */
    Integer addLab(LabDTO labDTO);

    LabDTO getById(Integer id);

    /**
     * 更新实验室数据
     *
     * @param labDTO 实验室输入对象
     * @return 实验室编码
     */
    Integer updateLab(LabDTO labDTO);

    /**
     * 根据编码列表，批量删除实验室
     *
     * @param ids 编码列表
     */
    void deleteByCodes(List<Integer> ids);

    Workbook export(LabQueryDTO queryDTO);

    int importLab(
            InputStream inputStream,
            String fileName) throws Exception;
}

