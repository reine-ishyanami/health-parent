package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.entity.Result;
import com.reine.pojo.OrderSetting;
import com.reine.service.OrderSettingService;
import com.reine.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 *
 * @author reine
 * @since 2022/4/29 9:47
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 文件上传，导入数据
     *
     * @param excelFile excel文件
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/upload")
    public Result upload(@RequestParam MultipartFile excelFile) {
        try {
            // 解析数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            ArrayList<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                String orderData = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderData), Integer.parseInt(number));
                data.add(orderSetting);
            }
            // 调用服务实现将预约设置数据批量导入数据库
            orderSettingService.add(data);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 根据指定年月查询数据库中的预约设置信息
     *
     * @param date 年月信息
     * @return 查询到的预约信息
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 修改指定日期的可预约人数
     * @param orderSetting 预约设置信息
     * @return 成功或失败信息
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
