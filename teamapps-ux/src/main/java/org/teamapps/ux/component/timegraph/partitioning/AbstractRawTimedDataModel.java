/*-
 * ========================LICENSE_START=================================
 * TeamApps
 * ---
 * Copyright (C) 2014 - 2020 TeamApps.org
 * ---
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
 * =========================LICENSE_END==================================
 */
package org.teamapps.ux.component.timegraph.partitioning;

import org.teamapps.event.Event;
import org.teamapps.ux.component.timegraph.Interval;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractRawTimedDataModel implements RawTimedDataModel {

	public final Event<Void> onDataChanged = new Event<>();

	@Override
	public Event<Void> onDataChanged() {
		return onDataChanged;
	}

	@Override
	public Map<String, long[]> getRawEventTimes(Collection<String> dataSeriesIds, Interval neededIntervalX) {
		return dataSeriesIds.stream()
				.collect(Collectors.toMap(dataSeriesId -> dataSeriesId, dataSeriesId -> getRawEventTimes(dataSeriesId, neededIntervalX)));
	}

	protected abstract long[] getRawEventTimes(String dataSeriesId, Interval neededIntervalX);
}
