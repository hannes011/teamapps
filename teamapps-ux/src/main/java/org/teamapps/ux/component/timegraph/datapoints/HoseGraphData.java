/*-
 * ========================LICENSE_START=================================
 * TeamApps
 * ---
 * Copyright (C) 2014 - 2022 TeamApps.org
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
package org.teamapps.ux.component.timegraph.datapoints;

import org.teamapps.dto.UiGraphData;
import org.teamapps.dto.UiHoseGraphData;
import org.teamapps.ux.component.timegraph.Interval;

import java.util.Objects;
import java.util.stream.Stream;

public interface HoseGraphData extends GraphData {

	LineGraphData getMiddleLineData();

	LineGraphData getLowerLineData();

	LineGraphData getUpperLineData();

	@Override
	default Interval getInterval() {
		return Stream.of(getMiddleLineData(), getLowerLineData(), getUpperLineData())
				.filter(Objects::nonNull)
				.map(lineGraphData -> lineGraphData.getInterval())
				.reduce(Interval::intersection)
				.orElse(Interval.empty());
	}

	@Override
	default UiGraphData toUiGraphData() {
		final LineGraphData lowerLineData = getLowerLineData();
		final LineGraphData middleLineData = getMiddleLineData();
		final LineGraphData upperLineData = getUpperLineData();
		return new UiHoseGraphData(
				lowerLineData != null ? lowerLineData.toUiGraphData() : null,
				middleLineData != null ? middleLineData.toUiGraphData() : null,
				upperLineData != null ? upperLineData.toUiGraphData() : null,
				getInterval().toUiLongInterval()
		);
	}
}
