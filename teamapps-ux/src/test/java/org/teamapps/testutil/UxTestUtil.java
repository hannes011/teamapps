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
package org.teamapps.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.teamapps.icons.api.IconTheme;
import org.teamapps.server.UxServerContext;
import org.teamapps.uisession.UiCommandExecutor;
import org.teamapps.ux.session.ClientInfo;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.uisession.QualifiedUiSessionId;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class UxTestUtil {

	public static CompletableFuture<Void> doWithMockedSessionContext(Runnable runnable) {
		SessionContext sessionContext = createDummySessionContext();
		return sessionContext.runWithContext(runnable);
	}

	public static SessionContext createDummySessionContext() {
		return new SessionContext(
				new QualifiedUiSessionId("httpSessionId", "uiSessionId"),
				new ClientInfo("ip", 1024, 768, 1000, 700, "en", false, "Europe/Berlin", 120, Collections.emptyList(), "userAgentString", "", Collections.emptyMap()), Mockito.mock(HttpSession.class),
				Mockito.mock(UiCommandExecutor.class),
				Mockito.mock(UxServerContext.class),
				Mockito.mock(IconTheme.class),
				Mockito.mock(ObjectMapper.class)
		);
	}

}
