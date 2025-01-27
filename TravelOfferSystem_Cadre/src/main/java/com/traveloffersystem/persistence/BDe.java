package com.traveloffersystem.persistence;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@Component
public class BDe {

    // 假设我们只针对表 site
    private final String tableName = "site";
    private final String keyName = "site_id";

    // 文本文件目录
    private String directoryR = "C:/temp/R"; // 可以通过 setter 修改
    // Lucene 索引目录
    private String luceneIndexPath = "C:/temp/luceneIndex";

    @Resource
    private JdbcTemplate jdbcTemplate;

    // 统一的分析器
    private Analyzer analyzer = new StandardAnalyzer();

    /** 允许外部修改目录 */
    public void setDirectoryR(String directoryR) {
        this.directoryR = directoryR;
    }
    public void setLuceneIndexPath(String luceneIndexPath) {
        this.luceneIndexPath = luceneIndexPath;
    }

    /**
     * 添加一段文本到指定主键行对应的文件 <key>.txt
     */
    public void addTextFile(String key, String content) throws IOException {
        File dir = new File(directoryR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = directoryR + File.separator + key + ".txt";
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(content);
        }
    }

    /**
     * 构建（或重建）Lucene 索引
     */
    public void createIndex() throws IOException {
        File idxDir = new File(luceneIndexPath);
        if (!idxDir.exists()) {
            idxDir.mkdirs();
        }
        FSDirectory directory = FSDirectory.open(Paths.get(luceneIndexPath));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (IndexWriter writer = new IndexWriter(directory, config)) {
            File folder = new File(directoryR);
            File[] txtFiles = folder.listFiles((d, name) -> name.endsWith(".txt"));
            if (txtFiles == null) {
                return;
            }
            for (File f : txtFiles) {
                String key = f.getName().replace(".txt", "");
                String content = readFileContent(f);

                Document doc = new Document();
                // 存储主键
                doc.add(new StoredField("key", key));
                // 索引文本
                doc.add(new TextField("content", content, Field.Store.NO));
                writer.addDocument(doc);
            }
        }
        System.out.println("Lucene index directory: " + luceneIndexPath);
        System.out.println("Text file directory: " + directoryR);
    }

    private String readFileContent(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 执行混合查询：
     * - 若不含 " with "，则执行 SQL
     * - 若含 " with "，则拆分 SQL 部分和文本部分，然后分别执行，再做内存 join，并按 score 降序返回
     */
    public List<Map<String, Object>> executeQuery(String bdeQuery) throws Exception {
        String lower = bdeQuery.toLowerCase();
        int withPos = lower.indexOf(" with ");
        if (withPos < 0) {
            // 纯 SQL
            return executePureSQL(bdeQuery);
        } else {
            // 拆分
            String sqlPart = bdeQuery.substring(0, withPos).trim();
            String textPart = bdeQuery.substring(withPos + 6).trim();

            // 确保 SQL 里包含主键
            sqlPart = ensureKeyInSelect(sqlPart);
            // 1) 执行 SQL
            List<Map<String, Object>> sqlResults = executeSqlAndGetList(sqlPart);
            // 2) 执行 Lucene
            Map<String, Float> textResults = executeTextQuery(textPart);
            // 3) 内存 Join + 按 score 排序
            List<Map<String, Object>> finalList = new ArrayList<>();
            for (Map<String, Object> row : sqlResults) {
                Object keyVal = row.get(keyName);
                if (keyVal != null) {
                    String keyStr = String.valueOf(keyVal);
                    if (textResults.containsKey(keyStr)) {
                        // 在 row 中插入一个 score
                        Map<String, Object> newRow = new LinkedHashMap<>(row);
                        newRow.put("_score", textResults.get(keyStr));
                        finalList.add(newRow);
                    }
                }
            }
            // 按 _score 降序
            finalList.sort((o1, o2) -> Float.compare(
                    (Float) o2.get("_score"),
                    (Float) o1.get("_score")
            ));
            return finalList;
        }
    }

    private List<Map<String, Object>> executePureSQL(String sql) {
        return executeSqlAndGetList(sql);
    }

    private List<Map<String, Object>> executeSqlAndGetList(String sql) {
        return jdbcTemplate.query(sql, rs -> {
            List<Map<String, Object>> result = new ArrayList<>();
            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                result.add(row);
            }
            return result;
        });
    }

    private Map<String, Float> executeTextQuery(String textQuery) throws Exception {
        FSDirectory dir = FSDirectory.open(Paths.get(luceneIndexPath));
        try (DirectoryReader reader = DirectoryReader.open(dir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(textQuery);
            TopDocs topDocs = searcher.search(query, 10000);
            Map<String, Float> result = new LinkedHashMap<>();
            for (ScoreDoc sd : topDocs.scoreDocs) {
                // 通过 IndexReader 获取文档
                Document doc = reader.document(sd.doc);
                String key = doc.get("key");
                result.put(key, sd.score);
            }
            return result;
        }
    }

    /**
     * 确保 SQL 中 SELECT 包含主键
     */
    private String ensureKeyInSelect(String sql) {
        String lower = sql.toLowerCase();
        int fromPos = lower.indexOf(" from ");
        if (fromPos < 0) return sql;
        String selectPart = sql.substring(6, fromPos).trim(); // "SELECT " 之后
        if (!selectPart.toLowerCase().contains(keyName.toLowerCase())) {
            // 插入 keyName
            String newSelect = keyName + ", " + selectPart;
            String after = sql.substring(fromPos);
            return "SELECT " + newSelect + after;
        }
        return sql;
    }
}
