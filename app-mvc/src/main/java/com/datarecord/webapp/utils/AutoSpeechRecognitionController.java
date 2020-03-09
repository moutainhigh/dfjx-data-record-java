package com.datarecord.webapp.utils;

import com.webapp.support.httpClient.HttpClientSupport;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by SongCQ on 2018/9/29.
 */

@Controller
@RequestMapping()
@MultipartConfig
public class AutoSpeechRecognitionController extends AbstractClientController{

    Logger logger = LoggerFactory.getLogger(AutoSpeechRecognitionController.class);

    private final static String pythonHost = "http://localhost:8010";
//    private final static String pythonHost = "http://192.168.1.184:8888";

    private HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);

    @RequestMapping(path = "/server/**",method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT})
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String nlpRequest(HttpServletRequest request) throws IOException, URISyntaxException, ServletException {

        String response = clientRequest(request,true);

        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS,"YYY",null,JsonSupport.jsonToMap(response));
    }

    @Override
    public HttpClientSupport httpClientSupport() {
        return this.httpClientSupport;
    }
}
