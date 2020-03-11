package com.datarecord.webapp.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.webapp.support.httpClient.HttpClientSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by SongCQ on 2018/10/18.
 */
public abstract class AbstractClientController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract HttpClientSupport httpClientSupport();

    public String clientRequest(HttpServletRequest request,boolean useJson) throws IOException, URISyntaxException, ServletException {
        String serviceUrl = checkServiceUrl(request);

        String response = null;

        String sendMethod = request.getMethod();
        if(!Strings.isNullOrEmpty(sendMethod)){

            if(request instanceof MultipartHttpServletRequest){//有文件上传
                MultipartHttpServletRequest muliRequest = (MultipartHttpServletRequest) request;
                Iterator<String> fileNames = muliRequest.getFileNames();
                URL classPath = this.getClass().getClassLoader().getResource("/");
                String classPathStr = classPath.toString();

                logger.debug("classPathStr : {}",classPathStr);
//                classPathStr = classPathStr.replaceFirst("file:/","");
                classPathStr = classPathStr.replaceFirst("file:","");
                logger.debug("classPathStr : {}",classPathStr);

                Map<String,File> fileMap = new HashMap<>();
                while (fileNames.hasNext()){
                    MultipartFile multiUploadFile = muliRequest.getFile(fileNames.next());
                    String fileName = multiUploadFile.getOriginalFilename();
                    File uploadFile = new File(classPathStr+"/"+fileName);
                    uploadFile.deleteOnExit();
                    multiUploadFile.transferTo(uploadFile);
                    fileMap.put(multiUploadFile.getName(),uploadFile);
                }

                Map parameters = getParameter(request);

                response = httpClientSupport().sendPostWithFile(serviceUrl,parameters,fileMap);

            }else{

                logger.debug("send url is =====>{}",serviceUrl);
                if("get".equalsIgnoreCase(sendMethod)){
                    response = httpClientSupport().sendRequest(serviceUrl,getParameter(request), RequestMethod.GET,useJson);
                }else  if("post".equalsIgnoreCase(sendMethod)){
                    response = httpClientSupport().sendRequest(serviceUrl,getParameter(request),RequestMethod.POST,useJson);
                }else  if("put".equalsIgnoreCase(sendMethod)){
                    response = httpClientSupport().sendRequest(serviceUrl,getParamesWithPutDelete(request),RequestMethod.PUT,useJson);
                }else  if("delete".equalsIgnoreCase(sendMethod)){
                    response = httpClientSupport().sendRequest(serviceUrl,getParamesWithPutDelete(request),RequestMethod.DELETE,useJson);
                }

            }

            logger.debug("response json is ======>{}",response);

        }
        return response;
    }

    public String checkServiceUrl(HttpServletRequest request){
        String url  = request.getRequestURI();
        String[] urlCharts = url.split("/");

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(String chart : urlCharts){
            if(count>1){
                builder.append("/");
                builder.append(chart);
            }
            count++;
        }

        return builder.toString();
    }

    private Map<String,Object> getParameter(HttpServletRequest request){
        Map<String,Object> params = new HashMap<>();
        Enumeration<String> allNames = request.getParameterNames();
        while(allNames.hasMoreElements()){
            String paramName = allNames.nextElement();
            params.put(paramName,request.getParameter(paramName));
        }
        return params;
    }

    private Map getParamesWithPutDelete(HttpServletRequest request){
        BufferedReader br = null;
        Map<String, String> dataMap = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String data =URLDecoder.decode(br.readLine(), "utf-8") ;

            dataMap = Splitter.on('&')
                    .trimResults()
                    .withKeyValueSeparator(
                            Splitter.on('=')
                                    .limit(2)
                                    .trimResults())
                    .split(data);

            return dataMap;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return dataMap;

    }

}
