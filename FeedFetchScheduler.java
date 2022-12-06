/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.manohar.web.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import lombok.extern.slf4j.Slf4j;

/**
 * A simple demo for cron-job like tasks that get executed regularly. It also
 * demonstrates how property values can be set. Users can set the property
 * values in /system/console/configMgr
 */
@Designate(ocd = FeedFetchScheduler.Config.class)
@Component(service = Runnable.class, immediate = true)
@Slf4j
public class FeedFetchScheduler implements Runnable {

	@ObjectClassDefinition(name = "Feed Fetch Config", description = "Simple demo for cron-job like task with properties")
	public static @interface Config {

		@AttributeDefinition(name = "Cron-job expression")
		String schedulerExpression() default "0/20 * * ? * * *";

		@AttributeDefinition(name = "Concurrent task", description = "Whether or not to schedule this task concurrently")
		boolean schedulerConcurrent() default false;
		
		@AttributeDefinition(name = "Enable Scheduler", description = "Whether or not to schedule a scheduler")
		boolean enableScheduler() default false;

		@AttributeDefinition(name = "Scheduler Name", description = "Name of the scheduler")
		String schedulerName() default "Feed Fetch";
	}

	@Reference
	private FeedFetchService feedFetchService;

	@Reference
	private Scheduler scheduler;

	private int schedulerID;

	@Override
	public void run() {
		String result = feedFetchService.getFeeds();
		log.info("Feed data :: {}", result.substring(0, 10));
	}

	@Activate
	protected void activate(final Config config) {
		log.info("FeedFetchScheduler :: activate called");
		schedulerID = config.schedulerName().hashCode();
		addScheduler(config);
	}

	@Modified
	protected void modified(final Config config) {
		removeScheduler();
		schedulerID = config.schedulerName().hashCode();
		addScheduler(config);
	}

	@Deactivate
	protected void deactivate(final Config config) {
		removeScheduler();
	}

	private void removeScheduler() {
		scheduler.unschedule(String.valueOf(schedulerID));
	}

	private void addScheduler(final Config config) {
		if(config.enableScheduler()) {
			ScheduleOptions schedulerOptions = scheduler.EXPR(config.schedulerExpression());
			schedulerOptions.name(String.valueOf(config.schedulerName()));
			schedulerOptions.canRunConcurrently(config.schedulerConcurrent());
			scheduler.schedule(this, schedulerOptions);
			log.debug("Feed Fetch Scheduler Configured");
		}
	}

}
