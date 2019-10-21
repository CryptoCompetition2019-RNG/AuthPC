package com.auth.NetworkUtils;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionKeyHandlerTest {
    @Test
    public void getSessionKeyHandlerTest(){
        SessionKeyHandler sessionKeyHandler = new SessionKeyHandler();
        assertTrue(sessionKeyHandler.checkStatus());
    }
}