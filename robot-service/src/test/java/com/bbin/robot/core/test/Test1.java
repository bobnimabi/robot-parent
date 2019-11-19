package com.bbin.robot.core.test;

import com.alibaba.fastjson.JSON;
import com.bbin.robot.core.test.entity.Student;
import org.apache.http.*;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;

/**
 * Created by mrt on 2019/7/15 0015 上午 9:31
 */
public class Test1 {
    public static void main(String[] args) {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        response.addHeader("Set-Cookie",
                "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie",
                "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator("Set-Cookie"));

        while (it.hasNext()) {
            HeaderElement elem = it.nextElement();
            System.out.println(elem.getName() + " = " + elem.getValue());
            NameValuePair[] params = elem.getParameters();
            for (int i = 0; i < params.length; i++) {
                System.out.println(" " + params[i]);
            }
        }
    }
}
