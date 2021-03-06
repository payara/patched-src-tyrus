/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.tyrus.test.standard_config;

import java.util.concurrent.CountDownLatch;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author Danny Coward (danny.coward at oracle.com)
 */
public class HelloTextClient extends Endpoint {
    boolean gotSomethingBack = false;
    String message = null;
    private final CountDownLatch messageLatch;

    public HelloTextClient(CountDownLatch messageLatch) {
        this.messageLatch = messageLatch;
    }

    @Override
    public void onOpen(Session session, EndpointConfig EndpointConfig) {
        System.out.println("HELLOCLIENT opened !!");
        try {
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String text) {
                    System.out.println("HELLOCLIENT received: " + text);
                    gotSomethingBack = true;
                    message = text;
                    messageLatch.countDown();
                }
            });
            session.getBasicRemote().sendText("Client says hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
