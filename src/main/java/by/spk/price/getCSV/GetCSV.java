package by.spk.price.getCSV;

import java.io.*;

import by.spk.price.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;


public class GetCSV {

    private static String login;
    private static String password;
    private static String formLogin;
    private static String formPassword;
    private static String urlLogin;
    private static String urlFile;
    private static String pathFile;

    private static void init() throws IOException {

        login = Utils.getPropertiesValue("csv_login");
        password = Utils.getPropertiesValue("csv_password");
        formLogin = Utils.getPropertiesValue("csv_formlogin");
        formPassword = Utils.getPropertiesValue("csv_formpassword");
        urlLogin = Utils.getPropertiesValue("csv_urllogin");
        urlFile = Utils.getPropertiesValue("csv_urlfile");
        pathFile = Utils.getPropertiesValue("csv_filepath");
    }

    public static void get() throws IOException {

        init();

        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(urlLogin + "?" +
                formLogin + "=" + login + "&" +
                formPassword + "=" + password);

        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException ex) {
            throw ex;
        }

        // if (response != null) response.getEntity().consumeContent();

        HttpGet httpget = new HttpGet(urlFile);
        response = httpClient.execute(httpget);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pathFile)))) {
                int inByte;
                while ((inByte = bis.read()) != -1) {
                    bos.write(inByte);
                }
            } catch (IOException | RuntimeException ex) {
                httpget.abort();
                throw ex;
            }
            //  httpClient.getConnectionManager().shutdown();
        }
    }
}


