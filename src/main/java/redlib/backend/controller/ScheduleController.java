package redlib.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.dto.query.ScheduleQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ScheduleService;
import redlib.backend.vo.ScheduleVO;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("listSchedule")
    @Privilege("page")
    public Page<ScheduleVO> listSchedule(@RequestBody ScheduleQueryDTO queryDTO) {
        return scheduleService.listByPage(queryDTO);
    }

    @PostMapping("addSchedule")
    @Privilege("add")
    public Integer addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.addSchedule(scheduleDTO);
    }

    @PostMapping("updateSchedule")
    @Privilege("update")
    public Integer updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.updateSchedule(scheduleDTO);
    }

    @GetMapping("getSchedule")
    @Privilege("update")
    public ScheduleDTO getSchedule(Integer id) {
        return scheduleService.getById(id);
    }

    @PostMapping("deleteSchedule")
    @Privilege("delete")
    public void deleteSchedule(@RequestBody List<Integer> ids) {
        scheduleService.deleteByCodes(ids);
    }

    @PostMapping("exportSchedule")
    @Privilege("page")
    public void exportSchedule(@RequestBody ScheduleQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        Workbook workbook = scheduleService.export(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + ".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
        workbook.close();
    }

    @PostMapping("importSchedule")
    @Privilege("add")
    public int importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        return scheduleService.importSchedule(file.getInputStream(), file.getOriginalFilename());
    }
}
