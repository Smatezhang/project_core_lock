package com.itek.library_core.util;
/**
 * Created by Simonzhang on 2019/4/9.
 */

import android.util.Log;

import com.itek.library_common.constant.MyConstant;
import com.itek.library_common.constant.SPKeyGlobal;
import com.itek.library_common.constant.UrlConstant;
import com.itek.library_core.utils.Md5Utils;
import com.itek.library_core.utils.SPUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author:：simon
 * @date：2019/4/9:10:23 AM
 * @mail：simon@itekiot.com
 * @description：
 */
public class NetUtils {


    /**
     * 向指定 URL 发送POST方法的请求
     *
     */
    public static String sendPost(String url, String param, String charset) {
        PrintWriter out = null;
        BufferedReader in = null;
        String sp_perfix = "";
        String result = "";
        String line;
        StringBuffer sb=new StringBuffer();

        long timetemp = System.currentTimeMillis();

        if (url.contains(UrlConstant.projectname_smartlock2n)){
            sp_perfix = UrlConstant.projectname_smartlock2n + MyConstant.SplitKey;
        }else if (url.contains(UrlConstant.projectname_smartlock2n)){
            sp_perfix = UrlConstant.projectname_smartlock2n + MyConstant.SplitKey;
        }


        String secretkey = Md5Utils.MD5(Md5Utils.MD5(UrlConstant.token + timetemp));

        String token = SPUtils.getInstance().getString(sp_perfix+ SPKeyGlobal.SP_TOKEN);

        String userid = SPUtils.getInstance().getString(sp_perfix + SPKeyGlobal.SP_USERID);

        try {

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("timetemp",timetemp+"");
            conn.setRequestProperty("token",token);
            conn.setRequestProperty("userid",userid);
            conn.setRequestProperty("secretkey",secretkey);
            conn.setRequestProperty("appversion","");

            //设置超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {

            Log.i("Net","发送 POST请求出现异常!" +e);

            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String sendPost(String url, Map<String, Object> map, String charset){
        StringBuffer sb=new StringBuffer();

        if(map!=null&&map.size()>0){
            for (Map.Entry<String, Object> e : map.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        return  sendPost(url,sb.toString(),charset);
    }



}
