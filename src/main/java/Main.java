import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=c5Nx81Y5WDnWoMPq4t1cqDrXUKge9BEhfFK9Udc2");
            CloseableHttpResponse response = httpClient.execute(request);
            NASA_Answer answer = mapper.readValue(response.getEntity().getContent(), NASA_Answer.class);
            HttpGet request2 = new HttpGet(answer.getUrl());
            CloseableHttpResponse response2 = httpClient.execute(request2);
            byte [] bytes = response2.getEntity().getContent().readAllBytes();
            File file = new File ("NASA_image.jpg");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);
            bos.flush();
            bos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
