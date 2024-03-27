package redlib.backend.service;


import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.LabDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.LabVO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LabService {
    Page<LabVO> listByPage(LabQueryDTO queryDTO);

    /**
     * 这个方法的主要作用是根据一组实验室名称，快速获取对应的实验室编码
     *
     * @param labsFree 可用实验室名称集合
     * @return 实验室信息
     */
    Map<String, String> getCodeMap(Set<String> labsFree);

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

