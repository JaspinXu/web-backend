package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.LabMapper;
import redlib.backend.dao.ScheduleMapper;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.dto.query.CheckQueryDTO;
import redlib.backend.dto.query.ScheduleQueryDTO;
import redlib.backend.model.Lab;
import redlib.backend.model.Schedule;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.LabService;
import redlib.backend.service.ScheduleService;
import redlib.backend.service.utils.LabUtils;
import redlib.backend.service.utils.ScheduleUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.LabVO;
import redlib.backend.vo.ScheduleVO;

import javax.swing.*;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private LabService labService;

    /**
     * 分页获取实验安排表信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的列表
     */
    @Override
    public Page<ScheduleVO> listByPage(ScheduleQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ScheduleQueryDTO();
        }

        queryDTO.setTeacherName(FormatUtils.makeFuzzySearchTerm(queryDTO.getTeacherName()));
        Integer size = scheduleMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Schedule> list = scheduleMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Schedule::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Schedule::getUpdatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<ScheduleVO> voList = new ArrayList<>();
        for (Schedule schedule : list) {
            // Schedule对象转VO对象
            ScheduleVO vo = ScheduleUtils.convertToVO(schedule, nameMap);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    /**
     * 分页获取可用实验室信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的可用实验室列表
     */
    @Override
    public Page<LabVO> listFreeLab(CheckQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new CheckQueryDTO();
        }

        //这里应该开始找有交集的实验安排，然后用总实验室数目减去它，获得空闲的实验室个数
        Integer size = labMapper.countSum() - scheduleMapper.countOccupy(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Schedule> list1 = scheduleMapper.listOccupy(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list1列表中的实验室名称字段，到一个Set集合中去，即占用的实验室
        Set<String> labsOccupy = list1.stream().map(Schedule::getLabName).collect(Collectors.toSet());

        // 获取所有实验室名称
        List<Lab> list2 = labMapper.listAll();

        // 提取list2列表中的实验室名称字段，到一个Set集合中去，即所有的实验室
        Set<String> labsAll = list2.stream().map(Lab::getLabName).collect(Collectors.toSet());

        // 两者相减，得到空闲的实验室名称集合
        Set<String> labsFree = new HashSet<>(labsAll);
        labsFree.removeAll(labsOccupy);

        //将集合转换成列表
        List<String> labsFreeNames = new ArrayList<>(labsFree);

        //获取可用实验室的信息
        List<Lab> list3 = labMapper.listByNames(new ArrayList<>(labsFreeNames));

        // 获取实验室名到实验室编码的映射
        //Map<String, String> codeMap = labService.getCodeMap(labsFree);

        List<LabVO> voList = new ArrayList<>();
        for (Lab lab : list3) {
            // Lab对象转VO对象
            LabVO vo = LabUtils.convertToVOFree(lab);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    /**
     * 新建实验安排
     *
     * @param scheduleDTO 实验安排输入对象
     * @return 实验安排编码
     */
    @Override
    public Integer addSchedule(ScheduleDTO scheduleDTO) {
        Token token = ThreadContextHolder.getToken();
        // 校验输入数据正确性
        ScheduleUtils.validateSchedule(scheduleDTO);
        // 创建实体对象，用以保存到数据库
        Schedule schedule = new Schedule();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setCreatedAt(new Date());
        schedule.setUpdatedAt(new Date());
        schedule.setCreatedBy(token.getUserId());
        schedule.setUpdatedBy(token.getUserId());
        // 调用DAO方法保存到数据库表
        scheduleMapper.insert(schedule);
        return schedule.getId();
    }

    @Override
    public ScheduleDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "实验安排id不能为空");
        Schedule schedule = scheduleMapper.selectByPrimaryKey(id);
        Assert.notNull(schedule, "id不存在");
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        return dto;
    }

    /**
     * 更新实验安排数据
     *
     * @param scheduleDTO 实验安排输入对象
     * @return 实验安排编码
     */
    @Override
    public Integer updateSchedule(ScheduleDTO scheduleDTO) {
        // 校验输入数据正确性
        Token token = ThreadContextHolder.getToken();
        ScheduleUtils.validateSchedule(scheduleDTO);
        Assert.notNull(scheduleDTO.getId(), "实验安排id不能为空");
        Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleDTO.getId());
        Assert.notNull(schedule, "没有找到实验安排，Id为：" + scheduleDTO.getId());

        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setUpdatedBy(token.getUserId());
        schedule.setUpdatedAt(new Date());
        scheduleMapper.updateByPrimaryKey(schedule);
        return schedule.getId();
    }

    /**
     * 根据编码列表，批量删除实验安排
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "实验安排id列表不能为空");
        scheduleMapper.deleteByCodes(ids);
    }

    @Override
    public Workbook export(ScheduleQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "实验安排ID");
        map.put("labName", "实验室名称");
        map.put("courseName", "课程名称");
        map.put("teacherName", "教师名称");
        map.put("courseTime", "节次");
        map.put("courseWeek", "周次");
        map.put("courseDay", "星期");
        map.put("semesterName", "学期名");
        map.put("studentNum", "学生人数");
        map.put("contactPhone", "联系电话");
        map.put("description", "备注");
        map.put("createdBy", "创建人");
        map.put("updatedBy", "更新人");
        map.put("createdAt", "创建时间");
        map.put("updatedAt", "更新时间");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<ScheduleVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }

    @Override
    public int importSchedule(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("labName", "实验室名称");
        map.put("courseName", "课程名称");
        map.put("teacherName", "教师名称");
        map.put("courseTime", "节次");
        map.put("courseWeek", "周次");
        map.put("courseDay", "星期");
        map.put("semesterName", "学期名");
        map.put("studentNum", "学生人数");
        map.put("contactPhone", "联系电话");
        map.put("description", "备注");
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (scheduleDTO) -> {
            addSchedule(scheduleDTO);
            row.incrementAndGet();
        }, map, ScheduleDTO.class);

        return row.get();
    }
}
