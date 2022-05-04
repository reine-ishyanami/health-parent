package com.reine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.reine.constant.MessageConstant;
import com.reine.entity.Result;
import com.reine.service.MemberService;
import com.reine.service.ReportService;
import com.reine.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理报表
 *
 * @author reine
 * @since 2022/5/3 9:51
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员数量折线图数据
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        // 使用模拟数据测试对象格式
        // Map map = mockData();

        Map<String, List> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        // 计算过去一年的12个月
        // 获得日历对象，默认是当前时间
        Calendar calendar = Calendar.getInstance();
        // 获得当前时间往前推12个月的时间
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months", months);
        List<Integer> memberCount = memberService.findMemberCountByMonths(months);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 套餐预约占比饼形图
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount();
            data.put("setmealCount", setmealCount);
            List<String> setmealNames = setmealCount.stream().map(count -> (String) count.get("name")).collect(Collectors.toList());
            data.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> data = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try (OutputStream outputStream = response.getOutputStream()) {
            Map<String, Object> result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            // 基于提供的excel文件在内存创建一个excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(filePath));
            XSSFSheet sheet = excel.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            // 使用输出流进行表格下载，基于浏览器作为客户端下载
            // 声明文件类型（excel文件类型）
            response.setContentType("application/vnd.ms-excel");
            // 以附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出PDF
     *
     * @return
     */
    @RequestMapping("/exportBusinessReport4PDF")
    public void exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();

            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";

            // 编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            // 填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));
            // 声明文件类型（PDF文件类型）
            response.setContentType("application/pdf");
            // 以附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 模拟假数据
     *
     * @return
     */
    private Map mockData() {
        Map<String, List> map = new HashMap<>();
        List<String> setmealNames = new ArrayList<>();
        setmealNames.add("儿童套餐");
        setmealNames.add("入职套餐");
        setmealNames.add("驾考套餐");
        setmealNames.add("高考套餐");
        setmealNames.add("老年套餐");
        map.put("setmealNames", setmealNames);
        List<Map> setmealCount = new ArrayList<>();
        for (int i = 0; i < setmealNames.size(); i++) {
            Map count = new HashMap<String, Object>();
            count.put("name", setmealNames.get(i));
            count.put("value", 20 + i);
            setmealCount.add(count);
        }
        map.put("setmealCount", setmealCount);
        return map;
    }
}
