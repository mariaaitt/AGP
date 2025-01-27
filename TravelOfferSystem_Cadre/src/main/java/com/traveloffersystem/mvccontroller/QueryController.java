package com.traveloffersystem.mvccontroller;

import com.traveloffersystem.persistence.BDe;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Resource
    private BDe bde;

    /**
     * 测试用：执行 SQL 或 混合查询
     * 访问示例:
     * GET /query/exec?bdeQuery=SELECT name FROM site WHERE type='historique' WITH sculpture Renaissance
     */
    @GetMapping("/exec")
    public List<Map<String,Object>> executeQuery(@RequestParam("bdeQuery") String bdeQuery) throws Exception {
        return bde.executeQuery(bdeQuery);
    }
}
