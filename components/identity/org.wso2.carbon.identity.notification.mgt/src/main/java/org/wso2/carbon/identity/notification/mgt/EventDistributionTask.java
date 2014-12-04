/*
*
*   Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*   WSO2 Inc. licenses this file to you under the Apache License,
*   Version 2.0 (the "License"); you may not use this file except
*   in compliance with the License.
*   You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

package org.wso2.carbon.identity.notification.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.notification.mgt.bean.PublisherEvent;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This has a queue inside. All publishers add events to this queue and this
 * event distribution task is responsible for distributing these events to Notification sending
 * modules
 */
public class EventDistributionTask implements Runnable {

    private static final Log log = LogFactory.getLog(NotificationSender.class);
    // Queue used to add events by publishers.
    private BlockingDeque<PublisherEvent> eventQueue;
    // Registered message sending modules.
    private List<NotificationSendingModule> notificationSendingModules;
    private static ExecutorService threadPool = null;

    public EventDistributionTask(List<NotificationSendingModule> notificationSendingModules, int threadPoolSize) {
        this.notificationSendingModules = notificationSendingModules;
        this.eventQueue = new LinkedBlockingDeque<PublisherEvent>();
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void addEventToQueue(PublisherEvent publisherEvent) {
        this.eventQueue.add(publisherEvent);
    }

    @Override
    public void run() {
        // Run forever
        while (true) {
            try {
                final PublisherEvent event = eventQueue.take();
                for (final NotificationSendingModule module : notificationSendingModules) {
                    // If the module is subscribed to the event, module will be executed.
                    try {
                        if (module.isSubscribed(event)) {
                            // Create a runnable and submit to the thread pool for sending message.
                            Runnable msgSender = new Runnable() {
                                @Override
                                public void run() {
                                    if (log.isDebugEnabled()) {
                                        log.debug("Executing " + module.getModuleName() + " on event" +
                                                event.getEventName());
                                    }
                                    try {
                                        module.sendMessage(event);
                                    } catch (NotificationManagementException e) {
                                        log.error("Error while invoking notification sending module " +
                                                module.getModuleName(), e);
                                    }
                                }
                            };
                            threadPool.submit(msgSender);
                        }
                    } catch (NotificationManagementException e) {
                        log.error("Error while getting subscription status from notification module " +
                                module.getModuleName(), e);
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error while picking up event from event queue", e);
            }
        }
    }
}
