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
package org.teamapps.ux.component.field;

import org.teamapps.dto.UiCheckBox;
import org.teamapps.dto.UiField;
import org.teamapps.common.format.Color;

import static org.teamapps.util.UiUtil.createUiColor;

public class CheckBox extends AbstractField<Boolean> {

	private String caption;
	private Color backgroundColor = new Color(255, 255, 255);
	private Color checkColor = new Color(70, 70, 70);
	private Color borderColor = new Color(204, 204, 204);

	public CheckBox(String caption) {
		super();
		setValue(false);
		this.caption = caption;
	}

	public CheckBox() {
		this(null);
	}

	@Override
	public UiField createUiComponent() {
		UiCheckBox uiCheckBox = new UiCheckBox();
		mapAbstractFieldAttributesToUiField(uiCheckBox);
		uiCheckBox.setCaption(caption);
		uiCheckBox.setBackgroundColor(backgroundColor != null ? createUiColor(backgroundColor) : null);
		uiCheckBox.setCheckColor(checkColor != null ? createUiColor(checkColor) : null);
		uiCheckBox.setBorderColor(borderColor != null ? createUiColor(borderColor) : null);
		return uiCheckBox;
	}

	public String getCaption() {
		return caption;
	}

	public CheckBox setCaption(String caption) {
		this.caption = caption;
		queueCommandIfRendered(() -> new UiCheckBox.SetCaptionCommand(getId(), caption));
		return this;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public CheckBox setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		queueCommandIfRendered(() -> new UiCheckBox.SetBackgroundColorCommand(getId(), backgroundColor != null ? createUiColor(backgroundColor) : null));
		return this;
	}

	public Color getCheckColor() {
		return checkColor;
	}

	public CheckBox setCheckColor(Color checkColor) {
		this.checkColor = checkColor;
		queueCommandIfRendered(() -> new UiCheckBox.SetCheckColorCommand(getId(), checkColor != null ? createUiColor(checkColor) : null));
		return this;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public CheckBox setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		queueCommandIfRendered(() -> new UiCheckBox.SetBorderColorCommand(getId(), borderColor != null ? createUiColor(borderColor) : null));
		return this;
	}
}
