package by.spk.price.getCSV;

import java.io.*;

import by.spk.price.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;

public final class GetCSV {

    private GetCSV() {
    }

    public static void get() {

        HttpClient httpClient = null;
        try {
            final String login = Utils.getPropertiesValue("csv.login");
            final String password = Utils.getPropertiesValue("csv.pass");
            final String formLogin = Utils.getPropertiesValue("csv.form.login");
            final String formPassword = Utils.getPropertiesValue("csv.form.password");
            final String urlLogin = Utils.getPropertiesValue("csv.url.login");
            final String urlFile = Utils.getPropertiesValue("csv.url.file");
            final String pathFile = Utils.getPropertiesValue("csv.local.file");

            httpClient = HttpClients.createDefault();
            HttpResponse response;

            HttpPost httpPost = new HttpPost(urlLogin + "?"
                    + formLogin + "=" + login + "&" + formPassword + "=" + password);

            // login
            httpClient.execute(httpPost);
//            if (response != null) response.getEntity().consumeContent();

            HttpGet httpget = new HttpGet(urlFile);
            response = httpClient.execute(httpget);

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                System.out.println("start save file");

                try (BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(pathFile)))) {
                    int inByte;
                    while ((inByte = bis.read()) != -1) {
                        bos.write(inByte);
                    }
                }
                System.out.println("finish save file");

            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
//        finally {
//            httpClient.getConnectionManager().shutdown();
//        }
    }
}


