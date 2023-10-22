package org.teamapps.ux.component.collapsible;

import org.teamapps.dto.UiCollapsible;
import org.teamapps.icons.Icon;
import org.teamapps.ux.component.AbstractComponent;
import org.teamapps.ux.component.Component;

public class Collapsible extends AbstractComponent {

	private Icon<?, ?> icon;
	private String caption;
	private Component content;
	private boolean collapsed;

	public Collapsible() {
		this(null, null, null);
	}

	public Collapsible(Icon<?, ?> icon, String caption) {
		this(icon, caption, null);
	}

	public Collapsible(Icon<?, ?> icon, String caption, Component content) {
		this.icon = icon;
		this.caption = caption;
		this.content = content;
	}

	@Override
	public UiCollapsible createUiComponent() {
		UiCollapsible ui = new UiCollapsible();
		mapAbstractUiComponentProperties(ui);
		ui.setIcon(getSessionContext().resolveIcon(icon));
		ui.setCaption(caption);
		ui.setContent(content != null ? content.createUiReference() : null);
		ui.setCollapsed(collapsed);
		return ui;
	}

	public Component getContent() {
		return content;
	}

	public void setContent(Component content) {
		this.content = content;
		queueCommandIfRendered(() -> new UiCollapsible.SetContentCommand(getId(), content != null ? content.createUiReference() : null));
	}

	public Icon<?, ?> getIcon() {
		return icon;
	}

	public void setIcon(Icon<?, ?> icon) {
		this.icon = icon;
		queueCommandIfRendered(() -> new UiCollapsible.SetIconAndCaptionCommand(getId(), getSessionContext().resolveIcon(icon), caption));
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
		queueCommandIfRendered(() -> new UiCollapsible.SetIconAndCaptionCommand(getId(), getSessionContext().resolveIcon(icon), caption));
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		queueCommandIfRendered(() -> new UiCollapsible.SetCollapsedCommand(getId(), collapsed));
	}
}
