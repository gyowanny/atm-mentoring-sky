package com.atm.modules;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.atm.client.AtmClient;

public class FtModule extends AbstractModule {

    @Override
    protected void configure() {
        Names.bindProperties(binder(), System.getProperties());
        bind(HttpRequestFactory.class).toInstance(
                new NetHttpTransport().createRequestFactory(request -> request.setParser(new JsonObjectParser(new JacksonFactory())))
        );
        bind(AtmClient.class);
    }

}
