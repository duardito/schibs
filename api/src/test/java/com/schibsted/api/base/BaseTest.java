package com.schibsted.api.base;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.schibsted.server.InitServer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class BaseTest {

    @Before
    public void initServer() throws IOException {
        InitServer.startServer();
    }

    @After
    public void stopServer() throws IOException {
        InitServer.stopServer();
    }

    private String encodeUserLogin(final String username, final String password) {
        final String build = username + ":" + password;
        final byte[] message = build.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(message);
    }

    protected JsonObject getJsonResponseGet(final String url,final String username,final String password) throws IOException {

        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpGet post = new HttpGet(url);
        post.setHeader("Authorization", "Basic " + encodeUserLogin(username, password));
        final HttpResponse response = httpclient.execute(post);
        final HttpEntity entity = response.getEntity();
        return getResponse(entity.getContent());
    }

    protected JsonObject getJsonResponsePut(final String url, final Map<String, String> mapParams,final String username,final String password) throws IOException {

        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpPut post = new HttpPut(url);
        post.setHeader("Authorization", "Basic " + encodeUserLogin(username, password));
        final List<NameValuePair> params = getParams(mapParams);
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        final HttpResponse response = httpclient.execute(post);
        final HttpEntity entity = response.getEntity();
        return getResponse(entity.getContent());
    }

    protected JsonObject getJsonResponsePost(final String url, final Map<String, String> mapParams, String username, String password) throws IOException {

        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final HttpPost post = new HttpPost(url);
        post.setHeader("Authorization", "Basic " + encodeUserLogin(username, password));
        final List<NameValuePair> params = getParams(mapParams);
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        final HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        return getResponse(entity.getContent());
    }

    private JsonObject getResponse(final InputStream is) throws IOException {
        final InputStreamReader isr = new InputStreamReader(is, "utf-8");
        final BufferedReader br = new BufferedReader(isr);
        final String query = br.readLine();
        final JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(query);
    }


    private List<NameValuePair> getParams(final Map<String, String> map) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>(0);
        final Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            final Map.Entry thisEntry = (Map.Entry) entries.next();
            final String key = thisEntry.getKey().toString();
            final String value = thisEntry.getValue().toString();
            params.add(new BasicNameValuePair(key, value));
        }
        return params;
    }
}
