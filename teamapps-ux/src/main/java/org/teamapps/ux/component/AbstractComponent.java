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
package org.teamapps.ux.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teamapps.dto.UiCommand;
import org.teamapps.dto.UiComponent;
import org.teamapps.dto.UiClientObjectReference;
import org.teamapps.dto.UiRootPanel;
import org.teamapps.event.Event;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.format.Shadow;
import org.teamapps.ux.component.format.Spacing;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.css.CssStyles;
import org.teamapps.ux.session.CurrentSessionContext;
import org.teamapps.ux.session.SessionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractComponent implements Component {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractComponent.class);

	public final Event<Void> onRendered = new Event<>();

//	public final Event<Boolean> onEffectiveVisibilityChanged = new Event<>();

	private String debuggingId = "";
	private String id;
	private SessionContext sessionContext;
	private boolean rendered;
	private Container container;
	private boolean destroyed = false;

	private boolean visible = true;
	private Map<String, CssStyles> stylesBySelector = new HashMap<>(0);

	public AbstractComponent() {
		this.sessionContext = CurrentSessionContext.get();
		id = getClass().getSimpleName() + "-" + UUID.randomUUID().toString();
	}

	protected void mapAbstractUiComponentProperties(UiComponent uiComponent) {
		uiComponent.setId(id);
		uiComponent.setDebuggingId(debuggingId);
		uiComponent.setVisible(visible);
		uiComponent.setStylesBySelector((Map) stylesBySelector);
	}

	@Override
	public String getId() {
		return id;
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	@Override
	public boolean isRendered() {
		return rendered;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		boolean changed = visible != this.visible;
		this.visible = visible;
		if (changed) {
			queueCommandIfRendered(() -> new UiComponent.SetVisibleCommand(getId(), visible));
		}
	}

	@Override
	public boolean isEffectivelyVisible() {
		return isRendered() && isVisible() && getParent() != null && getParent().isChildVisible(this);
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public final void render() {
		if (rendered) {
			return; // already rendered!
		}
		if (!(this instanceof RootPanel) && this.getParent() == null) {
			LOGGER.debug("Rendering component (" + getClass().getSimpleName() + ") that has no parent set. This is a temporary logging message to be able to find Containers that do not register "
					+ "themselves as \"parent\" of their children.");
		}
		LOGGER.debug("render: " + getId());
		sessionContext.registerClientObject(this);
		UiComponent uiComponent = createUiComponent();
		sessionContext.queueCommand(new UiRootPanel.CreateComponentCommand(uiComponent));
		rendered = true; // after queuing creation! otherwise commands might be queued for this component before it creation is queued!
		onRendered.fire(null);
	}

	@Override
	public final void unrender() {
		sessionContext.unregisterClientObject(this);
		sessionContext.queueCommand(new UiRootPanel.DestroyComponentCommand(getId()));
		rendered = false;
	}

	abstract public UiComponent createUiComponent();

	@Override
	public UiClientObjectReference createUiReference() {
		LOGGER.debug("createUiClientObjectReference: " + getId());
		if (!isRendered()) {
			render();
		}
		return new UiClientObjectReference(getId());
	}

	public void reRenderIfRendered() {
		if (rendered) {
			sessionContext.queueCommand(new UiRootPanel.RefreshComponentCommand(createUiComponent()));
		}
	}

	protected void queueCommandIfRendered(Supplier<UiCommand> commandSupplier) {
		if (rendered) {
			sessionContext.queueCommand(commandSupplier.get());
		}
	}

	@Override
	public void setMinWidth(Length minWidth) {
		setCssStyle("min-width", minWidth.toCssString());
	}

	@Override
	public void setMaxWidth(Length maxWidth) {
		setCssStyle("max-width", maxWidth.toCssString());
	}

	@Override
	public void setMinHeight(Length minHeight) {
		setCssStyle("min-height", minHeight.toCssString());
	}

	@Override
	public void setMaxHeight(Length maxHeight) {
		setCssStyle("max-height", maxHeight.toCssString());
	}

	@Override
	public void setMargin(Spacing margin) {
		setCssStyle("margin", margin.toCssString());
	}

	@Override
	public void setShadow(Shadow shadow) {
		setCssStyle("box-shadow", shadow.toCssString());
	}

	@Override
	public void setCssStyle(String selector, String propertyName, String value) {
		if (selector == null) {
			selector = "";
		}
		CssStyles styles = this.stylesBySelector.computeIfAbsent(selector, s -> new CssStyles());
		styles.put(propertyName, value);

		final String selector2 = selector;
		queueCommandIfRendered(() -> new UiComponent.SetStyleCommand(getId(), selector2, styles));
	}

	//	@Override
//	public void updateEffectiveVisibility() {
//		onEffectiveVisibilityChanged.fireIfChanged(isEffectivelyVisible());
//	}

//	@Override
//	public Event<Boolean> onEffectiveVisibilityChanged() {
//		return onEffectiveVisibilityChanged;
//	}

	@Override
	public void setParent(Container container) {
		this.container = container;
	}

	@Override
	public Container getParent() {
		return container;
	}

	@Override
	public String toString() {
		return id;
	}

	public String getDebuggingId() {
		return debuggingId;
	}

	public void setDebuggingId(String debuggingId) {
		this.debuggingId = debuggingId;
	}
}
