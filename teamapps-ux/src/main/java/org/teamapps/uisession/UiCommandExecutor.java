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
package org.teamapps.uisession;

import org.teamapps.dto.UiSessionClosingReason;

public interface UiCommandExecutor {
	/**
	 * @param qualifiedUiSessionId
	 * @param command
	 * @return the size of the queue of unsent commands
	 */
	int sendCommand(QualifiedUiSessionId qualifiedUiSessionId, UiCommandWithResultCallback command);

	/**
	 * @param qualifiedUiSessionId
	 */
	ClientBackPressureInfo getClientBackPressureInfo(QualifiedUiSessionId qualifiedUiSessionId);

	void closeSession(QualifiedUiSessionId qualifiedUiSessionId, UiSessionClosingReason reason);
}
