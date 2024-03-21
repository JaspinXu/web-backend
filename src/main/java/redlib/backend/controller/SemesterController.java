package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.SemesterDTO;
import redlib.backend.dto.query.SemesterQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.SemesterService;
import redlib.backend.vo.SemesterVO;

import java.util.List;

@RestController
@RequestMapping("/api/semester")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class SemesterController {
    @Autowired
    private SemesterService semesterService;

    @PostMapping("listSemester")
    @Privilege("page")
    public Page<SemesterVO> listSemester(@RequestBody SemesterQueryDTO queryDTO) {
        return semesterService.listByPage(queryDTO);
    }

    @PostMapping("addSemester")
    @Privilege("add")
    public Integer addSemester(@RequestBody SemesterDTO semesterDTO) {
        return semesterService.addSemester(semesterDTO);
    }

    @PostMapping("updateSemester")
    @Privilege("update")
    public Integer updateSemester(@RequestBody SemesterDTO semesterDTO) {
        return semesterService.updateSemester(semesterDTO);
    }

    @GetMapping("getSemester")
    @Privilege("update")
    public SemesterDTO getSemester(Integer id) {
        return semesterService.getById(id);
    }

    @PostMapping("deleteSemester")
    @Privilege("delete")
    public void deleteSemester(@RequestBody List<Integer> ids) {
        semesterService.deleteByCodes(ids);
    }

}
