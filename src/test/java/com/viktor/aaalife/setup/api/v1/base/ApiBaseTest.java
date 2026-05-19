package com.viktor.aaalife.setup.api.v1.base;

import com.viktor.aaalife.setup.api.v1.clients.RestfulBookerClient;

public abstract class ApiBaseTest {
    private static final RestfulBookerClient RESTFUL_BOOKER_CLIENT = new RestfulBookerClient();

    protected RestfulBookerClient client() {
        return RESTFUL_BOOKER_CLIENT;
    }
}
