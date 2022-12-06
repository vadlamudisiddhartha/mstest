package com.manohar.web.core.schedulers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;

import lombok.extern.slf4j.Slf4j;

@Component(service = FeedFetchService.class, immediate = true)
@Slf4j
public class FeedFetchServiceImpl implements FeedFetchService{

    private final String FEED_API_URL = "https://api.publicapis.org/entries";

    @Override
    public String getFeeds() {
        String result = "";
        HttpGet request = new HttpGet(FEED_API_URL);
        try {
            URI uri = new URIBuilder(request.getURI()).build();
            request.setURI(uri);
            try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .build();
                 CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                	result = EntityUtils.toString(entity);
                }
            }
        } catch (IOException ioe) {
            log.error("IOException :: {}", ioe.getMessage());
        } catch (URISyntaxException use) {
            log.error("URI Syntax Exception :: {}", use.getMessage());
        }
        return result;
    }

}
