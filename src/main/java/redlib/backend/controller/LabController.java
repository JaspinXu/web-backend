package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.LabDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.LabService;
import redlib.backend.vo.LabVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/lab")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class LabController {
    @Autowired
    private LabService labService;

    @PostMapping("listLab")
    @Privilege("page")
    public Page<LabVO> listLab(@RequestBody LabQueryDTO queryDTO) {
        return labService.listByPage(queryDTO);
    }

    @PostMapping("addLab")
    @Privilege("add")
    public Integer addLab(@RequestBody LabDTO labDTO) {
        return labService.addLab(labDTO);
    }

    @PostMapping("updateLab")
    @Privilege("update")
    public Integer updateLab(@RequestBody LabDTO labDTO) {
        return labService.updateLab(labDTO);
    }

    @GetMapping("getLab")
    @Privilege("update")
    public LabDTO getLab(Integer id) {
        return labService.getById(id);
    }

    @PostMapping("deleteLab")
    @Privilege("delete")
    public void deleteLab(@RequestBody List<Integer> ids) {
        labService.deleteByCodes(ids);
    }

    @PostMapping("exportLab")
    @Privilege("page")
    public void exportLab(@RequestBody LabQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = labService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }

    @PostMapping("importLab")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return labService.importLab(file.getInputStream(), file.getOriginalFilename());
    }
}
