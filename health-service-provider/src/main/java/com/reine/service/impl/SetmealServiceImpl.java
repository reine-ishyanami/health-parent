package com.reine.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.reine.constant.RedisConstant;
import com.reine.dao.SetmealDao;
import com.reine.entity.PageResult;
import com.reine.entity.QueryPageBean;
import com.reine.pojo.Setmeal;
import com.reine.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author reine
 * @since 2022/4/23 13:11
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Value("${output.path}")
    private String outputPath;

    /**
     * 新增套餐
     *
     * @param setmeal       套餐信息
     * @param checkGroupIds 包含的检查组id
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        setSetmealAndCheckgroup(checkGroupIds, setmealId);
        // 将图片名称保存到redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

        // 添加套餐后，生成静态页面
        // generateMobileStaticHtml();
    }

    /**
     * 生成当前方法所需的静态页面
     * TODO 尝试将此方法改为AOP实现
     */
    private void generateMobileStaticHtml() {
        // 生成静态页面前需要查询数据
        List<Setmeal> list = setmealDao.findAll();
        // 生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        // 生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    /**
     * 生成套餐列表静态页面
     *
     * @param list 套餐列表
     */
    private void generateMobileSetmealListHtml(List<Setmeal> list) {
        // 提供模板数据，用于生成静态页面
        Map map = new HashMap();
        map.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    /**
     * 生成套餐详情静态页面
     *
     * @param list 套餐列表
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        Map map = new HashMap();
        list.forEach(setmeal -> {
            map.put("setmeal", setmealDao.findById4Detail(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId()+".html", map);
        });
    }

    /**
     * 生成静态页面
     *
     * @param templateName 模板名称
     * @param htmlPageName 页面名称
     * @param map          页面数据
     */
    private void generateHtml(String templateName, String htmlPageName, Map map) {
        // 获得配置对象
        Configuration configuration = freeMarkerConfig.getConfiguration();
        // 声明输出流
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(outputPath + "/" + htmlPageName);
            // 输出文件
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean 查询条件
     * @return 查询结果
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据套餐id查询对应套餐信息
     *
     * @param id 套餐id
     * @return 套餐信息
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById4Detail(id);
    }

    /**
     * 根据套餐id查询关联的检查组
     *
     * @param id 套餐id
     * @return 关联的检查组列表
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 根据id删除套餐
     *
     * @param id 套餐id
     */
    @Override
    public void deleteById(Integer id) {
        setmealDao.deleteAssociation(id);
        setmealDao.deleteById(id);

        // 删除套餐后，生成静态页面
        // generateMobileStaticHtml();
    }

    /**
     * 编辑套餐及其对应的检查组信息
     *
     * @param setmeal       套餐信息
     * @param checkGroupIds 检查组列表
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        // 修改套餐基本信息
        setmealDao.edit(setmeal);
        // 清理当前检查组关联的检查项
        Integer setmealId = setmeal.getId();
        setmealDao.deleteAssociation(setmealId);
        // 重新建立检查组和检查项的关联关系
        setSetmealAndCheckgroup(checkGroupIds, setmealId);

        // 编辑套餐后，生成静态页面
        // generateMobileStaticHtml();
    }

    /**
     * 查询所有套餐
     *
     * @return 所有套餐信息
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 查询套餐预约占比数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 建立套餐和检查组的多对多关系
     *
     * @param checkGroupIds 检查组id
     * @param setmealId     套餐id
     */
    private void setSetmealAndCheckgroup(Integer[] checkGroupIds, Integer setmealId) {
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", setmealId);
                map.put("checkgroup_id", checkGroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
