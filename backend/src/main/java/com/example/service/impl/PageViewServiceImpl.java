package com.example.service.impl;

import com.example.mapper.PageViewMapper;
import com.example.pojo.PageView;
import com.example.service.PageViewService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PageViewServiceImpl implements PageViewService {
    @Autowired
    private PageViewMapper pageViewMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `page_view` (" +
            "`id` int NOT NULL AUTO_INCREMENT, " +
            "`view_type` varchar(10) NOT NULL, " +
            "`count` bigint NOT NULL DEFAULT 0, " +
            "PRIMARY KEY (`id`))");
        jdbcTemplate.execute("INSERT IGNORE INTO `page_view` (`id`, `view_type`, `count`) VALUES (1, 'visitor', 5000)");
        jdbcTemplate.execute("INSERT IGNORE INTO `page_view` (`id`, `view_type`, `count`) VALUES (2, 'user', 3000)");
        System.out.println("PageViewService: table init done, counts=" + pageViewMapper.selectAll());
    }

    @Override
    @Transactional
    public List<PageView> recordAndGet(String viewType) {
        System.out.println("PageViewService: incrementing viewType=" + viewType);
        pageViewMapper.increment(viewType);
        List<PageView> result = pageViewMapper.selectAll();
        System.out.println("PageViewService: after increment, counts=" + result);
        return result;
    }
}
