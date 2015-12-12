package com.baidu.beidou.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huangjinkun.
 * @date 15/12/5
 * @time 下午1:55
 */
public class DataDicitionaryParser {
    public Properties properties = new Properties();

    public static Map<String, JdbcTemplate> TEMPLATE_MAP = new TreeMap<String, JdbcTemplate>(new Comparator<String>() {
        public int compare(String s, String t1) {
            return t1.compareTo(s);
        }
    });

    public static final String TITLE = "数据字典";
    public static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    public static final String MAX_POOL_SIZE = "jdbc.maxPoolSize";
    public static final String MIN_POOL_SIZE = "jdbc.minPoolSize";
    public static final String IDLE_CONNECTION_TEST_PERIOD = "jdbc.idleConnectionTestPeriod";
    public static final String MAX_IDLE_TIME = "jdbc.maxIdleTime";

//    @Resource(name = "oneadxJdbcTemplate")
//    private JdbcTemplate oneadxJdbcTemplate;
//
//    @Resource(name = "beidouJdbcTemplate")
//    private JdbcTemplate beidouJdbcTemplate;

    public String getWholeDocumentHtmlByDBName(String dbname) {
        StringBuilder document = new StringBuilder("");
        document.append("<html>" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<title>" + dbname + TITLE + "</title>" +
                "<style>" +
                "body,td,th {font-family:\"宋体\"; font-size:12px;}" +
                "table{border-collapse:collapse;border:1px solid #CCC;background:#6089D4;}" +
                "table caption{text-align:left; background-color:#fff; line-height:2em; font-size:14px; " +
                "font-weight:bold; }" +
                "table th{text-align:left; font-weight:bold;height:26px; line-height:25px; font-size:16px; border:3px" +
                " solid #fff; color:#ffffff; padding:5px;}" +
                "table td{height:25px; font-size:12px; border:3px solid #fff; background-color:#f0f0f0; padding:5px;" +
                "}" +
                ".c1{ width: 150px;}" +
                ".c2{ width: 130px;}" +
                ".c3{ width: 70px;}" +
                ".c4{ width: 80px;}" +
                ".c5{ width: 80px;}" +
                ".c6{ width: 300px;}" +
                "ul, li\n" +
                "        {\n" +
                "            list-style: none;\n" +
                "        }\n" +
                "        a\n" +
                "        {\n" +
                "            text-decoration: none;\n" +
                "            color: #3381BF;\n" +
                "        }\n" +
                "        a:hover\n" +
                "        {\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "        #movie_rank\n" +
                "        {\n" +
                "            " +
                "        }\n" +
                "        .box2\n" +
                "        {\n" +
                "            border: 1px solid #ADDFF2;\n" +
                "            text-align: left;\n" +
                "            overflow: hidden;\n" +
                "            color: #9C9C9C;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .box2\n" +
                "        {\n" +
                "            margin-bottom: 7px;\n" +
                "        }\n" +
                "        .box2 h2\n" +
                "        {\n" +
                "            background: #EEF7FE;\n" +
                "            height: 21px;\n" +
                "            line-height: 21px;\n" +
                "            overflow-y: hidden;\n" +
                "            border-bottom: 1px solid #ADDFF2;\n" +
                "            color: #1974C8;\n" +
                "            font-size: 12px;\n" +
                "            padding: 0px 8px;\n" +
                "        }\n" +
                "        .box2 h2 a.more\n" +
                "        {\n" +
                "            float: right;\n" +
                "            text-decoration: underline;\n" +
                "            background: url() no-repeat 100% -123px;\n" +
                "            padding-right: 8px;\n" +
                "            font-weight: normal;\n" +
                "        }\n" +
                "        .box2 h2 span\n" +
                "        {\n" +
                "            margin-left: 5px;\n" +
                "            font-weight: normal;\n" +
                "            color: #B9B7B8;\n" +
                "        }\n" +
                "        .box2 .inner\n" +
                "        {\n" +
                "            padding: 8px;\n" +
                "            line-height: 18px;\n" +
                "            overflow: hidden;\n" +
                "            color: #3083C7;\n" +
                "        }\n" +
                "        .box2 a\n" +
                "        {\n" +
                "            color: #3083C7;\n" +
                "            white-space: nowrap;\n" +
                "        }\n" +
                "        .rank_list\n" +
                "        {\n" +
                "            line-height: 14px;\n" +
                "            margin: auto;\n" +
                "            padding-top: 5px;\n" +
                "        }\n" +
                "        .rank_list li\n" +
                "        {\n" +
                "            height: 14px;\n" +
                "            margin-bottom: 8px;\n" +
                "            width: 290px;\n" +
                "            padding-left: 20px;\n" +
                "            white-space: nowrap;\n" +
                "            overflow: hidden;\n" +
                "            position: relative;\n" +
                "        }\n" +
                "        .rank_list li.top3 em\n" +
                "        {\n" +
                "            background: #FFE4B7;\n" +
                "            border: 1px solid #FFBB8B;\n" +
                "            color: #FF6800;\n" +
                "        }\n" +
                "        .rank_list em\n" +
                "        {\n" +
                "            position: absolute;\n" +
                "            left: 0;\n" +
                "            top: 0;\n" +
                "            width: 12px;\n" +
                "            height: 12px;\n" +
                "            border: 1px solid #B1E0F4;\n" +
                "            color: #6298CC;\n" +
                "            font-style: normal;\n" +
                "            font-size: 10px;\n" +
                "            font-family: Arial;\n" +
                "            background: #E6F0FD;\n" +
                "            text-align: center;\n" +
                "            line-height: 12px;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .rank_list span\n" +
                "        {\n" +
                "            position: absolute;\n" +
                "            width: 60px;\n" +
                "            color: #B7B7B7;\n" +
                "            text-align: right;\n" +
                "            height: 14px;\n" +
                "            background: #fff;\n" +
                "            left: 110px;\n" +
                "        }\n" +
                "        #movie_rank .rank_list span\n" +
                "        {\n" +
                "            position: absolute;\n" +
                "            width: 40px;\n" +
                "            color: #B7B7B7;\n" +
                "            text-align: right;\n" +
                "            height: 14px;\n" +
                "            background: #fff;\n" +
                "            left: 260px;\n" +
                "        }" +
                "#backtop a { /* back to top button */\n" +
                "\tposition: fixed;\n" +
                "\tbottom: 0px; /* 小按钮到浏览器底边的距离 */\n" +
                "\tright: 50px; /* 小按钮到浏览器右边框的距离 */\n" +
                "\tcolor: #333; /* 小按钮中文字的颜色 */\n" +
                "\tz-index: 1000;\n" +
                "\tbackground: #cfcfcf; /* 小按钮底色 */\n" +
                "\tpadding: 10px; /* 小按钮中文字到按钮边缘的距离 */\n" +
                "\tborder-radius: 4px; /* 小按钮圆角的弯曲程度（半径）*/\n" +
                "\t-moz-border-radius: 4px;\n" +
                "\t-webkit-border-radius: 4px;\n" +
                "\tfont-weight: normal; /* 小按钮中文字的粗细 */\n" +
                "\ttext-decoration: none !important;\n" +
                "}\n" +
                "\n" +
                "#backtop a:hover { /* 小按钮上有鼠标悬停时 */\n" +
                "\tbackground: #333; /* 小按钮的底色 */\n" +
                "\tcolor: #fff; /* 文字颜色 */\n" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>");
        document.append("<h1 style=\"text-align:center;\">" + dbname + TITLE + "</h1>");
        // 显示数据字典列表
        if (dbname.length() <= 0) {
            document.append(getOutlineSnippet());
        } else {
            document.append(getHtmlSnippetByDBName(dbname));
        }
        document.append("<div id=\"backtop\"><a href=\"#\">TOP</a></div>");
        document.append("</body></html>");
        return document.toString();
    }

    public String getOutlineSnippet() {
        StringBuilder outline = new StringBuilder();
        outline.append("<div class=\"box2\" id=\"movie_rank\">\n" +
                "        <h2>\n" +
                "            数据库列表</h2>\n" +
                "        <div class=\"inner\">\n" +
                "            <ul class=\"rank_list\">\n");
        int i = 0;
        for (String key : TEMPLATE_MAP.keySet()) {
            if (i < 3) {
                outline.append("<li class=\"top3\"><em>" + i + "</em><a href=\"" + key + "\">" + key +
                        "</a></li>");
            } else {
                outline.append("<li><em>" + i + "</em><a href=\"" + key + "\">" + key + "</a></li>");
            }
            i++;
        }
        outline.append("</ul></div></div>");
        return outline.toString();
    }

    public String getHtmlSnippetByDBName(String dbname) {
        StringBuilder html = new StringBuilder("");
        // 循环所有表
        List<Table> tables = getTablesByDBName(dbname);
        if (tables.size() > 0) {
            html.append("<div class=\"box2\" id=\"movie_rank\">\n" +
                    "        <h2>\n" +
                    "            表列表</h2>\n" +
                    "        <div class=\"inner\">\n" +
                    "            <ul class=\"rank_list\">\n");
            int i = 0;
            for (Table table : tables) {
                String tableName = table.tableName;
                if (i < 3) {
                    html.append("<li class=\"top3\"><em>" + i + "</em><a name=\"list" + tableName + "\" href=\"#" +
                            tableName + "\">" + tableName +
                            "</a></li>");
                } else {
                    html.append("<li><em>" + i + "</em><a name=\"list" + tableName + "\" href=\"#" + tableName +
                            "\">" + tableName + "</a></li>");
                }
                i++;
            }
            html.append("</ul></div></div>");
            html.append("");
            for (Table table : tables) {
                // html += "<p><h2>" + table.tableComment + "&nbsp;</h2>";
                String tableName = table.tableName;
                html.append("<table  border=\"1\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">");
                html.append("<caption><a name=\"" + tableName + "\" href=\"#list" + tableName + "\"></a>" + tableName
                        + "(" + table
                        .tableComment + ")" + "</caption>");
                html.append("<tbody><tr><th>字段名</th><th>数据类型</th><th>默认值</th>" +
                        "     <th>允许非空</th>" +
                        "     <th>自动递增</th><th>备注</th></tr>");
                html.append("");

                for (Column column : getColumnsByTable(table)) {
                    html.append("<tr><td class=\"c1\">" + column.columnName + "</td>");
                    html.append("<td class=\"c2\">" + column.columnType + "</td>");
                    html.append("<td class=\"c3\">&nbsp;" + column.columnDefault + "</td>");
                    html.append("<td class=\"c4\">&nbsp;" + column.isNullable + "</td>");
                    html.append("<td class=\"c5\">" + column.autoIncrement + "</td>");
                    html.append("<td class=\"c6\">&nbsp;" + column.columnComment + "</td>");
                    html.append("</tr>");
                }

                html.append("</tbody></table></p>");
            }
        }

        return html.toString();
    }

    public List<String> getTablesNamesByDBName(String dbname) {
        JdbcTemplate template = TEMPLATE_MAP.get(dbname);
        if (template != null) {
            template.execute("SET NAMES utf8");
            String queryDBName = "'" + dbname + "'";
            String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=" + queryDBName + ";";
            List<String> tablesNames = template.query(sql, new RowMapper<String>() {
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("TABLE_NAME");
                }
            });
            return tablesNames;
        }
        return null;
    }

    public List<Table> getTablesByDBName(String dbname) {
        List<Table> tables = new ArrayList<Table>();
        List<String> tablesNames = getTablesNamesByDBName(dbname);
        if (tablesNames != null) {
            for (String tableName : tablesNames) {
                String queryTableName = "'" + tableName + "'";
                String queryDBName = "'" + dbname + "'";
                String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME=" + queryTableName + " AND " +
                        "table_schema=" + queryDBName + ";";
                JdbcTemplate template = TEMPLATE_MAP.get(dbname);
                if (template != null) {
                    Table table = template.query(sql, new TableRowMapper()).get(0);
                    tables.add(table);
                }
            }
        }
        return tables;
    }

    public List<Column> getColumnsByTable(Table table) {
        String queryTableName = "'" + table.tableName + "'";
        String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=" + queryTableName + " AND " +
                "table_schema=" + "'" + table.tableSchema + "'" + ";";
        JdbcTemplate template = TEMPLATE_MAP.get(table.tableSchema);
        if (template != null) {
            List<Column> columns = template.query(sql, new ColumnRowMapper());
            return columns;
        }
        return null;
    }

    public static class Table {
        String tableName;
        String tableSchema;
        String tableComment;

        @Override
        public int hashCode() {
            return (tableSchema + "." + tableName).hashCode();
        }
    }

    public static class Column {
        String columnName;
        String columnType;
        String columnDefault;
        String isNullable;
        String autoIncrement;
        String columnComment;
    }

    public static class TableRowMapper implements RowMapper<Table> {

        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table table = new Table();
            table.tableName = rs.getString("TABLE_NAME");
            table.tableComment = rs.getString("TABLE_COMMENT");
            table.tableSchema = rs.getString("TABLE_SCHEMA");
            return table;
        }
    }

    public static class ColumnRowMapper implements RowMapper<Column> {
        public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
            Column column = new Column();
            column.columnName = rs.getString("COLUMN_NAME");
            column.columnType = rs.getString("COLUMN_TYPE");
            column.columnDefault = rs.getString("COLUMN_DEFAULT");
            column.isNullable = rs.getString("IS_NULLABLE");
            column.autoIncrement = rs.getString("EXTRA").equals("auto_increment") ? "是" : "&nbsp";
            column.columnComment = rs.getString("COLUMN_COMMENT");
            return column;
        }
    }

    public void init() {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(getClass().getResource("/jdbc-mysql.properties").getPath
                    ()));
            properties.load(in);
            Pattern pattern = Pattern.compile("(jdbc\\.)([a-zA-Z_]+)(\\.url)");

            Set keys = properties.keySet();
            for (Object obj : keys) {
                String keyName = (String) obj;
                Matcher matcher = pattern.matcher(keyName);
                if (matcher.matches()) {
                    String dbname = matcher.group(2);
                    TEMPLATE_MAP.put(dbname, getJdbcTemplateByName(dbname));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private JdbcTemplate getTemplateByDBName(String dbname) {
//        if (dbname.equals("one_adx")) {
//            return oneadxJdbcTemplate;
//        } else if (dbname.equals("beidou")) {
//            return beidouJdbcTemplate;
//        }
//        return null;
//    }

    private JdbcTemplate getJdbcTemplateByName(String dbname) {
        JdbcTemplate template = new JdbcTemplate();
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setJdbcUrl(properties.getProperty("jdbc." + dbname + ".url"));
            dataSource.setUser(properties.getProperty("jdbc." + dbname + ".username"));
            dataSource.setPassword(properties.getProperty("jdbc." + dbname + ".password"));
            dataSource.setDriverClass(properties.getProperty(DRIVER_CLASS_NAME));
            dataSource.setMaxPoolSize(Integer.parseInt(properties.getProperty(MAX_POOL_SIZE)));
            dataSource.setMinPoolSize(Integer.parseInt(properties.getProperty(MIN_POOL_SIZE)));
            dataSource.setIdleConnectionTestPeriod(Integer.parseInt(properties.getProperty
                    (IDLE_CONNECTION_TEST_PERIOD)));
            dataSource.setMaxIdleTime(Integer.parseInt(properties.getProperty(MAX_IDLE_TIME)));
            template.setDataSource(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

//    public JdbcTemplate getBeidouJdbcTemplate() {
//        return beidouJdbcTemplate;
//    }
//
//    public void setBeidouJdbcTemplate(JdbcTemplate beidouJdbcTemplate) {
//        this.beidouJdbcTemplate = beidouJdbcTemplate;
//    }
//
//    public JdbcTemplate getOneadxJdbcTemplate() {
//        return oneadxJdbcTemplate;
//    }
//
//    public void setOneadxJdbcTemplate(JdbcTemplate oneadxJdbcTemplate) {
//        this.oneadxJdbcTemplate = oneadxJdbcTemplate;
//    }
}
