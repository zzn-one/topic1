package com.zzn.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzn.config.RabbitMqConfig;
import com.zzn.exception.FileDataBodyFormatErrorException;
import com.zzn.exception.FileDataTitleFormatErrorException;
import com.zzn.pojo.Page;
import com.zzn.pojo.Result;
import com.zzn.pojo.dict.DictCollege;
import com.zzn.pojo.dict.DictGender;
import com.zzn.pojo.dict.DictMajor;
import com.zzn.pojo.user.UserUpdatePassword;
import com.zzn.pojo.user.student.UserStudent;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.pojo.user.student.UserStudentWithExcel;
import com.zzn.service.DictCollegeService;
import com.zzn.service.DictGenderService;
import com.zzn.service.DictMajorService;
import com.zzn.service.UserStudentService;
import com.zzn.service.impl.UserStudentServiceImpl;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zzn
 * @create 2022-12-16 19:55
 */
@RestController
@RequestMapping("/userStudent")
public class UserStudentController {
    @Autowired
    private UserStudentService userStudentService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DictCollegeService dictCollegeService;

    @Autowired
    private DictMajorService dictMajorService;

    @Autowired
    private DictGenderService dictGenderService;

    @GetMapping("/login/{account}/{password}")
    public Result<String> login(@PathVariable String account, @PathVariable String password, HttpServletRequest request) {
        Result<String> result;
        String jwt = userStudentService.login(account, password, request);
        if (jwt == null) {
            result = new Result<>(HttpStatus.UNAUTHORIZED.value(), "账号或密码错误！", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, jwt);
        }
        return result;
    }

    /*
     * 分页 条件 查询
     * */
    @PostMapping("{currentPage}/{pageSize}/{college}")
    public Result<Page<UserStudentWithDict>> getByConditionAndPage(@PathVariable("currentPage") Integer currentPage
            , @PathVariable("pageSize") Integer pageSize, @PathVariable("college") String college, @RequestBody UserStudent userStudent) {

        Result<Page<UserStudentWithDict>> result;

        Page<UserStudentWithDict> page = userStudentService.pageByCondition(currentPage, pageSize, college, userStudent.getSId(),
                userStudent.getSName(), userStudent.getMajor());
        if (page != null) {
            result = new Result<>(HttpStatus.OK.value(), null, page);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "获取毕业生信息失败！", null);
        }
        return result;
    }

    /*
     * 根据sId查询单用户
     * */
    @GetMapping("{sId}")
    public Result<UserStudentWithDict> getByTId(@PathVariable("sId") String sId) {
        Result<UserStudentWithDict> result;

        UserStudentWithDict userStudent = userStudentService.getOneBySid(sId);
        if (userStudent == null) {
            result = new Result<>(HttpStatus.OK.value(), "没有该用户信息！", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, userStudent);
        }
        return result;
    }

    //---------------------------------------------单表---------------------------------------

    /*
     * 根据sId和password 修改password
     * */
    @PutMapping("/updatePassword")
    public Result<Boolean> updatePasswordByAid(@RequestBody UserUpdatePassword user) {
        Result<Boolean> result;
        //根据id和oldPassword查询用户

        String sid = user.getId();

        LambdaQueryWrapper<UserStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserStudent::getSId, sid);
        wrapper.eq(UserStudent::getPassword, user.getPass());

        UserStudent userDb = userStudentService.getOne(wrapper);

        if (userDb == null) {
            result = new Result<>(HttpStatus.FORBIDDEN.value(), "旧密码错误,请重新输入！", false);
        } else {
            //验证通过，修改密码
            LambdaUpdateWrapper<UserStudent> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(UserStudent::getPassword, user.getNewPass());
            updateWrapper.eq(UserStudent::getSId, userDb.getSId());
            //更新updatedBy和updatedTime字段
            updateWrapper.set(UserStudent::getUpdatedBy, userDb.getSId());
            updateWrapper.set(UserStudent::getUpdatedTime, new Date());

            boolean update = userStudentService.update(userDb, updateWrapper);

            if (update) {
                result = new Result<>(HttpStatus.OK.value(), "密码已修改！", true);
                //发送消息 同步redis的用户信息
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_STUDENT_UPDATE + "." + sid, sid);

            } else {
                result = new Result<>(HttpStatus.OK.value(), "密码修改失败！", false);
            }

        }

        return result;
    }

    /*
     * 根据sId修改用户信息
     * */
    @PutMapping("/updateMsg")
    public Result<Boolean> updateMsgByAid(@RequestBody HashMap<String, String> userMap) {
        LambdaUpdateWrapper<UserStudent> updateWrapper = new LambdaUpdateWrapper<>();

        String sid = userMap.get("id");
        String phone = userMap.get("phone");
        String email = userMap.get("email");

        updateWrapper.eq(UserStudent::getSId, sid);
        updateWrapper.set(UserStudent::getPhone, phone);
        updateWrapper.set(UserStudent::getEmail, email);
        updateWrapper.set(UserStudent::getUpdatedBy, sid);
        updateWrapper.set(UserStudent::getUpdatedTime, new Date());

        boolean update = userStudentService.update(updateWrapper);

        Result<Boolean> result;
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "信息已修改！", true);
            //信息同步redis
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_STUDENT_UPDATE + "." + sid, sid);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "信息修改失败！", false);
        }
        return result;
    }

    /*
     * 新增毕业生
     * */
    @PostMapping()
    public Result<Boolean> addUser(@RequestBody UserStudent userStudent) {
        Result<Boolean> result;

        LambdaQueryWrapper<UserStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserStudent::getSId, userStudent.getSId());

        UserStudent studentDb = userStudentService.getOne(queryWrapper);
        if (studentDb == null) {
            //新增
            boolean save = userStudentService.save(userStudent);
            if (save) {
                result = new Result<>(HttpStatus.OK.value(), "成功新增用户！", true);
            } else {
                result = new Result<>(HttpStatus.OK.value(), "新增用户失败！", false);
            }
        } else {
            //数据库已有
            result = new Result<>(HttpStatus.OK.value(), "该学号已被占用，请重新输入！", false);
        }
        return result;
    }


    /*
     * 根据id删除单用户
     * */
    @DeleteMapping("{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String id) {
        Result<Boolean> result;

        //先检查id对应的用户是否处于登录状态（redis有无用户信息）
        String userRedis = getUserRedis(id);
        if (userRedis != null) {
            //用户已登录，禁止删除该用户信息
            result = new Result<>(HttpStatus.OK.value(), "该用户正处于登录状态，禁止删除！！", false);
        } else {
            //用户未登录，可以直接删除信息
            boolean remove = userStudentService.removeById(id);
            if (remove) {
                result = new Result<>(HttpStatus.OK.value(), "删除成功！", true);
            } else {
                result = new Result<>(HttpStatus.OK.value(), "删除失败！", false);
            }
        }

        return result;
    }

    @Nullable
    private String getUserRedis(String id) {
        UserStudent student = userStudentService.getById(id);
        return stringRedisTemplate.opsForValue().get(UserStudentServiceImpl.KEY_PREFIX + student.getSId());
    }

    /*
     * 修改用户信息
     * */
    @PutMapping()
    public Result<Boolean> updateById(@RequestBody UserStudent student) {
        Result<Boolean> result;

        LambdaUpdateWrapper<UserStudent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserStudent::getId, student.getId());

        boolean update = userStudentService.update(student, updateWrapper);
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "成功修改信息！", true);
//            数据同步
            //检查id对应的用户是否处于登录状态（redis有无用户信息）
            String userRedis = getUserRedis(String.valueOf(student.getId()));

            if (userRedis != null) {
                //发送消息 同步redis的用户信息
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.EXCHANGE_NAME,
                        RabbitMqConfig.ROUTING_NAME_STUDENT_UPDATE + "." + student.getSId(), student.getSId());
            }
        } else {
            result = new Result<>(HttpStatus.OK.value(), "修改信息失败！", false);
        }
        return result;
    }

    /*
     * 获取毕业年份(excel)
     * 根据college
     * */

    @GetMapping("/getYearList/{college}")
    public Result<List<String>> getYearList(@PathVariable("college") String college) {
        List<String> list = userStudentService.getYearListByCollege(college);
        return new Result<>(200, null, list);
    }
    /*
    * 获取 当前条件的最大信息数目（excel）
    * */
    @PostMapping("/getExcelMax/{college}")
    public Result<Long> getExcelMax(@PathVariable String college, @RequestBody HashMap<String,String> condition) {

        String major = condition.get("major");
        String year = condition.get("year");

        LambdaQueryWrapper<UserStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserStudent::getCollege, college);
        queryWrapper.eq(!major.equals(""), UserStudent::getMajor, major);
        queryWrapper.eq(!year.equals("全部"), UserStudent::getGraduationYear, year);

        long max = userStudentService.count(queryWrapper);

        return new Result<>(200, null, max);

    }

    //---------------------------------------------excel文件---------------------------------------

    /*
     * 用户信息 文件导入
     * */
    @PostMapping("/importMsg")
    public Result<String> importMsg(MultipartFile file, String updatedBy, Date updatedTime) throws IOException {
        //读取第一个sheet 文件流会自动关闭
        EasyExcel.read(file.getInputStream(), UserStudentWithExcel.class, new ReadListener<UserStudentWithExcel>() {
                    /**
                     * 单次缓存的数据量
                     */
                    public static final int BATCH_COUNT = 100;
                    /**
                     * 临时存储
                     */
                    private List<UserStudent> cachedList = new ArrayList<>();


                    @SneakyThrows
                    @Override
                    public void invoke(UserStudentWithExcel userExcel, AnalysisContext analysisContext) {
                        /*
                         * 对象类型转换
                         * */
                        UserStudent user = importDataTransfer(userExcel, updatedBy, updatedTime);

                        cachedList.add(user);

                        if (cachedList.size() >= BATCH_COUNT) {
                            saveData();
                            //清除缓存数据
                            cachedList.clear();
                        }
                    }

                    /*
                     * 文件数据读取完后执行的操作
                     * */
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        saveData();
                    }

                    /*
                     * 文件数据存储到数据库
                     * */
                    private void saveData() {
                        //把cachedList的数据存储到数据库
                        userStudentService.saveBatch(cachedList);
                    }

                }
        ).sheet().doRead();

        return new Result<>(200, "文件数据导入成功！", null);
    }

    /*
     * 用户信息 文件导出
     * 获取所有毕业生信息
     * 根据college
     * */
    @GetMapping("/exportAllMsg/{college}")
    public void exportAllMsg(@PathVariable("college") String college, HttpServletResponse response) throws IOException {

        List<UserStudentWithDict> list = userStudentService.listByCollege(college);

        //数据转化
        List<UserStudentWithExcel> excelList = exportDataTransfer(list);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("毕业生信息", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), UserStudentWithExcel.class).sheet("毕业生信息").doWrite(excelList);

    }

    /*
     * 用户信息 文件导出
     * 获取毕业生信息
     * 根据college 及 筛选条件
     * */
    @PostMapping("/exportMsgByCondition/{college}")
    public void exportMsgByCondition(@PathVariable String college,@RequestBody HashMap<String,String> condition, HttpServletResponse response) throws IOException {

        String major = condition.get("major");
        String year = condition.get("year");
        Integer number = Integer.parseInt(condition.get("number")) ;
        List<UserStudentWithDict> list = userStudentService.exportMsgByCondition(college, major, year, number);

        //数据转化
        List<UserStudentWithExcel> excelList = exportDataTransfer(list);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("毕业生信息", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), UserStudentWithExcel.class).sheet("毕业生信息").doWrite(excelList);
    }

    //文件数据转换

    /*
    * 导入文件的数据转换
    * */
    private UserStudent importDataTransfer(UserStudentWithExcel studentWithExcel, String updatedBy, Date updatedTime) throws FileDataBodyFormatErrorException, FileDataTitleFormatErrorException {
        if (studentWithExcel.getCollege() == null
                || studentWithExcel.getMajor() == null
                || studentWithExcel.getSId() == null
                || studentWithExcel.getEmail() == null
                || studentWithExcel.getSName() == null
                || studentWithExcel.getPhone() == null
                || studentWithExcel.getGender() == null
                || studentWithExcel.getGraduationYear() == null
        ) {
            //数据文件的标题异常
            throw new FileDataTitleFormatErrorException();
        }

        //转换 gender college major
        Integer gender = genderTransfer(studentWithExcel.getGender());
        String college = collegeTransfer(studentWithExcel.getCollege());
        String major = majorTransfer(studentWithExcel.getMajor());

        String defaultPassword = "123";
        UserStudent user = new UserStudent();

        user.setSId(studentWithExcel.getSId());
        user.setSName(studentWithExcel.getSName());
        user.setGraduationYear(studentWithExcel.getGraduationYear());
        user.setPhone(studentWithExcel.getPhone());
        user.setEmail(studentWithExcel.getEmail());
        user.setPassword(defaultPassword);

        user.setUpdatedBy(updatedBy);
        user.setUpdatedTime(updatedTime);

        user.setGender(gender);
        user.setCollege(college);
        user.setMajor(major);

        return user;
    }

    private Integer genderTransfer(String gender) throws FileDataBodyFormatErrorException {
        LambdaQueryWrapper<DictGender> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictGender::getDescription, gender);
        DictGender dictGender = dictGenderService.getOne(queryWrapper);

        if (dictGender == null) {
            //文件的数据格式异常
            throw new FileDataBodyFormatErrorException();
        } else {
            return dictGender.getGender();
        }
    }

    private String collegeTransfer(String college) throws FileDataBodyFormatErrorException {
        LambdaQueryWrapper<DictCollege> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictCollege::getDescription, college);
        DictCollege dictCollege = dictCollegeService.getOne(queryWrapper);

        if (dictCollege == null) {
            //文件的数据格式异常
            throw new FileDataBodyFormatErrorException();

        } else {
            return dictCollege.getCollege();
        }
    }

    private String majorTransfer(String major) throws FileDataBodyFormatErrorException {
        LambdaQueryWrapper<DictMajor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictMajor::getDescription, major);
        DictMajor dictMajor = dictMajorService.getOne(queryWrapper);

        if (dictMajor == null) {
            //文件的数据格式异常
            throw new FileDataBodyFormatErrorException();
        } else {
            return dictMajor.getMajor();
        }
    }

    /*
    * 导出文件的数据转换
    * */
    private List<UserStudentWithExcel> exportDataTransfer(List<UserStudentWithDict> list){

        ArrayList<UserStudentWithExcel> excelList = new ArrayList<>();

        for (UserStudentWithDict user:list) {

            UserStudentWithExcel userExcel = new UserStudentWithExcel();

            userExcel.setCollege(user.getDictCollege().getDescription());
            userExcel.setEmail(user.getEmail());
            userExcel.setGender(user.getDictGender().getDescription());
            userExcel.setMajor(user.getDictMajor().getDescription());
            userExcel.setPhone(user.getPhone());
            userExcel.setGraduationYear(user.getGraduationYear());
            userExcel.setSId(user.getSId());
            userExcel.setSName(user.getSName());

            excelList.add(userExcel);
        }

        return excelList;

    }





}
