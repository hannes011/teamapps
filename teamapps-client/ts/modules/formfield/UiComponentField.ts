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
import {UiComponentConfig} from "../../generated/UiComponentConfig";
import {UiFieldEditingMode} from "../../generated/UiFieldEditingMode";
import {UiField} from "./UiField";
import {TeamAppsUiContext} from "../TeamAppsUiContext";
import {UiComponentFieldCommandHandler, UiComponentFieldConfig, UiComponentFieldEventSource} from "../../generated/UiComponentFieldConfig";
import {TeamAppsUiComponentRegistry} from "../TeamAppsUiComponentRegistry";
import {createUiBorderCssObject, createUiColorCssString} from "../util/CssFormatUtil";
import {UiColorConfig} from "../../generated/UiColorConfig";
import {UiBorderConfig} from "../../generated/UiBorderConfig";
import {parseHtml} from "../Common";
import {UiComponent} from "../UiComponent";


export class UiComponentField extends UiField<UiComponentFieldConfig, void> implements UiComponentFieldEventSource, UiComponentFieldCommandHandler {

	private component: UiComponent<UiComponentConfig>;
	private $componentWrapper: HTMLElement;

	protected initialize(config: UiComponentFieldConfig, context: TeamAppsUiContext) {
		this.$componentWrapper = parseHtml('<div class="UiComponentField"></div>');
		this.setBackgroundColor(config.backgroundColor);
		this.setBorder(config.border);
		this.setSize(config.width, config.height);
		this.setComponent(config.component as UiComponent);
	}

	isValidData(v: void): boolean {
		return true;
	}

	setBackgroundColor(backgroundColor: UiColorConfig): void {
		this.$componentWrapper.style.backgroundColor = backgroundColor ? createUiColorCssString(backgroundColor) : '';
	}

	setBorder(border: UiBorderConfig): void {
		Object.assign(this.$componentWrapper.style, createUiBorderCssObject(border));
	}

	setComponent(component: UiComponent): void {
		if (this.component != null) {
			this.component.getMainElement().remove();
		}
		this.component = component;
		this.$componentWrapper.appendChild(this.component.getMainElement());
	}

	setSize(width: number, height: number): void {
		Object.assign(this.$componentWrapper.style, {
			height: height ? `${height}px` : '',
			width: width ? `${width}px` : ''
		});
	}

	public getMainInnerDomElement(): HTMLElement {
		return this.$componentWrapper;
	}

	public getFocusableElement(): HTMLElement {
		return null;
	}

	protected displayCommittedValue(): void {
		// do nothing
	}

	focus(): void {
		// do nothing
	}

	getTransientValue(): void {
	}

	protected onEditingModeChanged(editingMode: UiFieldEditingMode): void {
		// do nothing (default implementation)
	}

	getDefaultValue(): void {
	}

	public valuesChanged(v1: void, v2: void): boolean {
		return false;
	}

}

TeamAppsUiComponentRegistry.registerFieldClass("UiComponentField", UiComponentField);
