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
package org.teamapps.ux.component.charting.forcelayout;

import org.teamapps.common.format.Color;
import org.teamapps.dto.UiNetworkLink;
import org.teamapps.util.UiUtil;

public class ForceLayoutLink {

	private final ForceLayoutNode source;
	private final ForceLayoutNode target;

	private float lineWidth = 1f;
	private Color lineColor = Color.MATERIAL_GREY_500;
	private String lineDashArray;

	public ForceLayoutLink(ForceLayoutNode source, ForceLayoutNode target) {
		this.source = source;
		this.target = target;
	}

	public UiNetworkLink toUiNetworkLink() {
		UiNetworkLink ui = new UiNetworkLink(source.getId(), target.getId());
		ui.setLineWidth(lineWidth);
		ui.setLineColor(UiUtil.createUiColor(lineColor));
		ui.setLineDashArray(lineDashArray);
		return ui;
	}

	public ForceLayoutNode getSource() {
		return source;
	}

	public ForceLayoutNode getTarget() {
		return target;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public String getLineDashArray() {
		return lineDashArray;
	}

	public void setLineDashArray(String lineDashArray) {
		this.lineDashArray = lineDashArray;
	}
}
