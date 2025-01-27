package com.traveloffersystem.mvccontroller;

import com.traveloffersystem.persistence.BDe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
@RequestMapping("/lucene")
public class LuceneController {

    @Resource
    private BDe bde;

    /**
     * 向某个主键对应的记录添加文本文件
     * 访问方式：POST /lucene/addText?key=123
     * body: "文本内容..."
     */
    @PostMapping("/addText")
    @ResponseBody
    public String addText(@RequestParam("key") String key, @RequestBody String content) throws IOException {
        bde.addTextFile(key, content);
        return "Text for key=" + key + " added successfully.";
    }

    /**
     * 重建索引
     * 访问方式：GET /lucene/rebuild
     */
    @GetMapping("/rebuild")
    @ResponseBody
    public String rebuildIndex() {
        try {
            bde.createIndex();
            return "Index rebuilt successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to rebuild index: " + e.getMessage();
        }
    }
}
