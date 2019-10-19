package com.auth.NetworkUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected boolean completeStatus = false;

    public boolean checkStatus() {return completeStatus;}
}
