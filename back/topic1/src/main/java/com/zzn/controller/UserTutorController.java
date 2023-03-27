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
import com.zzn.pojo.dict.DictTutorTitle;
import com.zzn.pojo.user.UserUpdatePassword;
import com.zzn.pojo.user.student.UserStudent;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.pojo.user.tutor.TutorStudent;
import com.zzn.pojo.user.tutor.UserTutor;
import com.zzn.pojo.user.tutor.UserTutorWithDict;
import com.zzn.pojo.user.tutor.UserTutorWithExcel;
import com.zzn.service.*;
import com.zzn.service.impl.UserStudentServiceImpl;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
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
@RequestMapping("/userTutor")
public class UserTutorController {
    @Autowired
    private UserTutorService userTutorService;

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

    @Autowired
    private DictTutorTitleService dictTutorTitleService;

    /*
     * 登录
     * */
    @GetMapping("/login/{account}/{password}")
    public Result<String> login(@PathVariable String account, @PathVariable String password, HttpServletRequest request) {

        Result<String> result;

        String jwt = userTutorService.login(account, password, request);
        if (jwt == null) {
            result = new Result<>(HttpStatus.UNAUTHORIZED.value(), "账号或密码错误！请重新输入", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, jwt);
        }

        return result;
    }

    /*
     * 根据tId和password 修改password
     * */
    @PutMapping("/updatePassword")
    public Result<Boolean> updatePasswordByAid(@RequestBody UserUpdatePassword user) {
        Result<Boolean> result;
        String tid = user.getId();
        //根据id和oldPassword查询用户
        LambdaQueryWrapper<UserTutor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserTutor::getTId, tid);
        wrapper.eq(UserTutor::getPassword, user.getPass());
        UserTutor userDb = userTutorService.getOne(wrapper);

        if (userDb == null) {
            result = new Result<>(HttpStatus.FORBIDDEN.value(), "旧密码错误,请重新输入！", false);
        } else {
            //验证通过，修改密码
            LambdaUpdateWrapper<UserTutor> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserTutor::getTId, userDb.getTId());
            updateWrapper.set(UserTutor::getPassword, user.getNewPass());
            //更新updatedBy和updatedTime字段
            updateWrapper.set(UserTutor::getUpdatedBy, userDb.getTId());
            updateWrapper.set(UserTutor::getUpdatedTime, new Date());
            boolean update = userTutorService.update(userDb, updateWrapper);
            if (update) {
                result = new Result<>(HttpStatus.OK.value(), "密码已修改！", true);

                //发送消息 同步redis的用户信息
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_TUTOR_UPDATE + "." + tid, tid);
            } else {
                result = new Result<>(HttpStatus.OK.value(), "密码修改失败！", false);
            }

        }

        return result;
    }

    /*
     * 根据tId修改用户信息
     * */
    @PutMapping("/updateMsg")
    public Result<Boolean> updateMsgByAid(@RequestBody HashMap<String, String> userMap) {
        LambdaUpdateWrapper<UserTutor> updateWrapper = new LambdaUpdateWrapper<>();

        String tId = userMap.get("id");
        String phone = userMap.get("phone");
        String email = userMap.get("email");

        updateWrapper.eq(UserTutor::getTId, tId);
        updateWrapper.set(UserTutor::getPhone, phone);
        updateWrapper.set(UserTutor::getEmail, email);
        updateWrapper.set(UserTutor::getUpdatedBy, tId);
        updateWrapper.set(UserTutor::getUpdatedTime, new Date());

        boolean update = userTutorService.update(updateWrapper);

        Result<Boolean> result;
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "信息修改成功！重新登录系统可查看新信息", true);
            //发送消息 同步redis的用户信息
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_TUTOR_UPDATE + "." + tId, tId);

        } else {
            result = new Result<>(HttpStatus.OK.value(), "信息修改失败！", false);
        }

        return result;

    }

    /*
     * 分页 条件 查询
     * */
    @PostMapping("{currentPage}/{pageSize}/{college}")
    public Result<Page<UserTutorWithDict>> getByConditionAndPage(@PathVariable("currentPage") Integer currentPage
            , @PathVariable("pageSize") Integer pageSize, @PathVariable("college") String college, @RequestBody UserTutor userTutor) {
        Result<Page<UserTutorWithDict>> result;
        Page<UserTutorWithDict> page = userTutorService.getByConditionAndPage(currentPage, pageSize, college, userTutor.getTId(), userTutor.getTutorName(), userTutor.getMajor());

        if (page != null) {
            result = new Result<>(HttpStatus.OK.value(), null, page);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "获取导师信息失败！", null);
        }

        return result;

    }

    /*
     * 根据tId查询单用户
     * */
    @GetMapping("{tId}")
    public Result<UserTutorWithDict> getByTId(@PathVariable("tId") String tId) {
        Result<UserTutorWithDict> result;
        UserTutorWithDict userDb = userTutorService.getOneByTid(tId);

        if (userDb == null) {
            result = new Result<>(HttpStatus.OK.value(), "没有该用户信息！", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, userDb);
        }

        return result;
    }

    /*
    * 根据tid查询毕业生信息
    *
    * */
    @GetMapping("/getStudentList/{tid}")
    public  Result<List<TutorStudent>> getStudentList(@PathVariable String tid){
        return new Result<>(200, null, userTutorService.getStudentByTid(tid));
    }

    /*
     * 根据id删除单用户
     * */
    @DeleteMapping("{id}")
    public Result<Boolean> deleteById(@PathVariable("id") String id) {
        Result<Boolean> result;
        boolean remove = userTutorService.removeById(id);

        if (remove) {
            result = new Result<>(HttpStatus.OK.value(), "删除成功！", true);

        } else {
            result = new Result<>(HttpStatus.OK.value(), "删除失败！", false);
        }

        return result;
    }

    private String getUserRedis(String id) {
        UserTutor tutor = userTutorService.getById(id);
        return stringRedisTemplate.opsForValue().get(UserStudentServiceImpl.KEY_PREFIX + tutor.getTId());
    }
    /*
     * 修改用户信息
     * */
    @PutMapping()
    public Result<Boolean> updateById(@RequestBody UserTutor tutor) {
        Result<Boolean> result;

        LambdaUpdateWrapper<UserTutor> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(UserTutor::getId, tutor.getId());

        boolean update = userTutorService.update(tutor, updateWrapper);

        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "成功修改信息！", true);

            //            数据同步
            //检查id对应的用户是否处于登录状态（redis有无用户信息）
            String userRedis = getUserRedis(String.valueOf(tutor.getId()));

            if (userRedis != null) {
                //发送消息 同步redis的用户信息
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.EXCHANGE_NAME,
                        RabbitMqConfig.ROUTING_NAME_TUTOR_UPDATE + "." + tutor.getTId(), tutor.getTId());
            }
        } else {
            result = new Result<>(HttpStatus.OK.value(), "修改信息失败！", false);
        }

        return result;
    }

    /*
     * 新增导师
     * */
    @PostMapping()
    public Result<Boolean> addUser(@RequestBody UserTutor userTutor) {
        Result<Boolean> result;

        LambdaQueryWrapper<UserTutor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTutor::getTId, userTutor.getTId());

        UserTutor tutorDb = userTutorService.getOne(queryWrapper);

        if (tutorDb == null) {
            //新增
            boolean save = userTutorService.save(userTutor);
            if (save) {
                result = new Result<>(HttpStatus.OK.value(), "成功新增用户！", true);
            } else {
                result = new Result<>(HttpStatus.OK.value(), "新增用户失败！", false);
            }

        } else {
            //数据库已有
            result = new Result<>(HttpStatus.OK.value(), "该工号已被占用，请重新输入！", false);
        }

        return result;
    }

    /*
     * 获取 当前条件的最大信息数目（excel）
     * */
    @PostMapping("/getExcelMax/{college}")
    public Result<Long> getExcelMax(@PathVariable String college, @RequestBody HashMap<String,String> condition) {

        String major = condition.get("major");

        LambdaQueryWrapper<UserTutor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTutor::getCollege, college);
        queryWrapper.eq(!major.equals(""), UserTutor::getMajor, major);

        long max = userTutorService.count(queryWrapper);

        return new Result<>(200, null, max);

    }

    //---------------------------------------------excel文件---------------------------------------

    /*
     * 用户信息 文件导入
     * */
    @PostMapping("/importMsg")
    public Result<String> importMsg(MultipartFile file, String updatedBy, Date updatedTime) throws IOException{
        //读取第一个sheet 文件流会自动关闭
        EasyExcel.read(file.getInputStream(), UserTutorWithExcel.class, new ReadListener<UserTutorWithExcel>() {
                    /**
                     * 单次缓存的数据量
                     */
                    public static final int BATCH_COUNT = 100;
                    /**
                     * 临时存储
                     */
                    private List<UserTutor> cachedList = new ArrayList<>();


                    @SneakyThrows
                    @Override
                    public void invoke(UserTutorWithExcel userExcel, AnalysisContext analysisContext) {
                        /*
                         * 对象类型转换
                         * */
                        UserTutor user = importDataTransfer(userExcel, updatedBy, updatedTime);

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
                        userTutorService.saveBatch(cachedList);
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

        List<UserTutorWithDict> list = userTutorService.listByCollege(college);

        //数据转化
        List<UserTutorWithExcel> excelList = exportDataTransfer(list);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("指导老师信息", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), UserTutorWithExcel.class).sheet("指导老师信息").doWrite(excelList);

    }

    /*
     * 用户信息 文件导出
     * 获取毕业生信息
     * 根据college 及 筛选条件
     * */
    @PostMapping("/exportMsgByCondition/{college}")
    public void exportMsgByCondition(@PathVariable String college,@RequestBody HashMap<String,String> condition, HttpServletResponse response) throws IOException {

        String major = condition.get("major");
        Integer number = Integer.parseInt(condition.get("number")) ;
        List<UserTutorWithDict> list = userTutorService.exportMsgByCondition(college, major, number);

        //数据转化
        List<UserTutorWithExcel> excelList = exportDataTransfer(list);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("指导老师信息", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), UserTutorWithExcel.class).sheet("指导老师信息").doWrite(excelList);
    }

    //文件数据转换

    /*
     * 导入文件的数据转换
     * */
    private UserTutor importDataTransfer(UserTutorWithExcel withExcel, String updatedBy, Date updatedTime) throws FileDataBodyFormatErrorException, FileDataTitleFormatErrorException {
        if (withExcel.getCollege() == null
                || withExcel.getMajor() == null
                || withExcel.getTId() == null
                || withExcel.getEmail() == null
                || withExcel.getTutorName() == null
                || withExcel.getPhone() == null
                || withExcel.getGender() == null
                || withExcel.getOffice() == null
                || withExcel.getTitle() == null
        ) {
            //数据文件的标题异常
            throw new FileDataTitleFormatErrorException();
        }

        //转换 gender college major
        Integer gender = genderTransfer(withExcel.getGender());
        String college = collegeTransfer(withExcel.getCollege());
        String major = majorTransfer(withExcel.getMajor());
        Integer title = titleTransfer(withExcel.getTitle());

        String defaultPassword = "123";
        UserTutor user = new UserTutor();

        user.setTId(withExcel.getTId());
        user.setTutorName(withExcel.getTutorName());
        user.setPhone(withExcel.getPhone());
        user.setEmail(withExcel.getEmail());
        user.setPassword(defaultPassword);
        user.setOffice(withExcel.getOffice());

        user.setUpdatedBy(updatedBy);
        user.setUpdatedTime(updatedTime);

        user.setGender(gender);
        user.setCollege(college);
        user.setMajor(major);
        user.setTitle(title);

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

        String s = StringUtils.delete(major, "系");
        LambdaQueryWrapper<DictMajor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictMajor::getDescription, s);
        DictMajor dictMajor = dictMajorService.getOne(queryWrapper);

        if (dictMajor == null) {
            //文件的数据格式异常
            throw new FileDataBodyFormatErrorException();
        } else {
            return dictMajor.getMajor();
        }
    }
    private Integer titleTransfer(String title) throws FileDataBodyFormatErrorException {
        LambdaQueryWrapper<DictTutorTitle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictTutorTitle::getDescription, title);
        DictTutorTitle dictTutorTitle = dictTutorTitleService.getOne(queryWrapper);

        if (dictTutorTitle == null) {
            //文件的数据格式异常
            throw new FileDataBodyFormatErrorException();
        } else {
            return dictTutorTitle.getTitle();
        }
    }

    /*
     * 导出文件的数据转换
     * */
    private List<UserTutorWithExcel> exportDataTransfer(List<UserTutorWithDict> list){

        ArrayList<UserTutorWithExcel> excelList = new ArrayList<>();

        for (UserTutorWithDict user:list) {

            UserTutorWithExcel userExcel = new UserTutorWithExcel();

            userExcel.setCollege(user.getDictCollege().getDescription());
            userExcel.setEmail(user.getEmail());
            userExcel.setGender(user.getDictGender().getDescription());
            userExcel.setMajor(user.getDictMajor().getDescription());
            userExcel.setPhone(user.getPhone());
            userExcel.setTitle(user.getDictTutorTitle().getDescription());
            userExcel.setTId(user.getTId());
            userExcel.setTutorName(user.getTutorName());
            userExcel.setOffice(user.getOffice());

            excelList.add(userExcel);
        }

        return excelList;

    }



}
