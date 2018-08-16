package com.cdx.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by cdx0312
 * 2018/3/11
 */
public class TestFreeMarker {
    /**
     * freemark静态化流程
     * @throws Exception
     */
    @Test
    public void testFreeMarker() throws Exception {
        //1、创建一个模板文件
        //2、创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3、设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java_Code\\taotao-shangcheng\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
        //4、设置模板的字符集
        configuration.setDefaultEncoding("utf-8");
        //5、使用Configuration对象加载一个模板文件，需要制定模板文件的文件名
//        Template template = configuration.getTemplate("hello.ftl");
        Template template = configuration.getTemplate("student.ftl");
        //6、创建一个数据集，可以是pojo，也可以是map
        Map map = new HashMap();
        map.put("hello", "hello freemarker");
        Student student = new Student(1, "dd", 12, "aaa");
        map.put("student", student);
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "dd", 12, "aaa"));
        list.add(new Student(1, "dd", 12, "aaa"));
        list.add(new Student(2, "dd", 12, "aaa"));
        list.add(new Student(3, "dd", 12, "aaa"));
        list.add(new Student(4, "dd", 12, "aaa"));
        list.add(new Student(5, "dd", 12, "aaa"));
        list.add(new Student(6, "dd", 12, "aaa"));
        list.add(new Student(7, "dd", 12, "aaa"));
        map.put("stuList", list);
        map.put("date", new Date());
        //7、创建一个Writer对象，制定输出文件的路径及文件名
        Writer out = new FileWriter("webmall-itemDetail-web/src/main/webapp/WEB-INF/freemarkerOutFile/student.html");
        template.process(map, out);
        //9、关闭流
        out.close();
    }
}
