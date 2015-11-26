/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peiban.service;

import android.util.Log;

import com.peiban.command.NetworkUtils;
import com.xmpp.push.sns.ConnectionListener;

public class PersistentConnectionListener implements ConnectionListener {

    private static final String LOGTAG = "PersissstentConnectionListener";

    private final XmppManager xmppManager;

    public PersistentConnectionListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void connectionClosed() {
        Log.d(LOGTAG, "connectionClosed()...");
        try {
        	if(NetworkUtils.isnetWorkAvilable(xmppManager.getSnsService()) 
        			&& xmppManager.getSnsService().isServiceRunState())
        	{
				xmppManager.startReconnectionThread();
			}
		} catch (Exception e) {
		}
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(LOGTAG, "connectionClosedOnError()...");
        try {
        	xmppManager.getConnection().disconnect();
		} catch (Exception e2) {
			e.printStackTrace();
			e2.printStackTrace();
		}
		try {
			if(NetworkUtils.isnetWorkAvilable(xmppManager.getSnsService()) && xmppManager.getSnsService().isServiceRunState()){
				xmppManager.startReconnectionThread();
			}
		} catch (Exception e2) {
		}
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(LOGTAG, "reconnectingIn()...");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(LOGTAG, "reconnectionFailed()...");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(LOGTAG, "reconnectionSuccessful()...");
    }

}
