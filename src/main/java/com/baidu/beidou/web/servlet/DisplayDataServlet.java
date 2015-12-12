package com.baidu.beidou.web.servlet;

import com.baidu.beidou.util.DataDicitionaryParser;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author huangjinkun.
 * @date 15/12/5
 * @time 下午2:23
 */
public class DisplayDataServlet extends HttpServlet {

    private DataDicitionaryParser dataDicitionaryParser;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String path = req.getPathInfo();
        out.println(dataDicitionaryParser.getWholeDocumentHtmlByDBName(path.substring(path.lastIndexOf('/') + 1)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("doPost");
    }

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext servletContext = this.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        dataDicitionaryParser = (DataDicitionaryParser) ctx.getBean("dataDicitionaryParser");
    }

    public DataDicitionaryParser getDataDicitionaryParser() {
        return dataDicitionaryParser;
    }

    public void setDataDicitionaryParser(DataDicitionaryParser dataDicitionaryParser) {
        this.dataDicitionaryParser = dataDicitionaryParser;
    }
}
