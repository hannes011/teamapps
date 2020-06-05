"use strict";
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (Object.hasOwnProperty.call(mod, k)) result[k] = mod[k];
    result["default"] = mod;
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
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
var utils_1 = require("./utils");
var conference_api_1 = require("./conference-api");
var constants_1 = require("../../config/constants");
var debug = __importStar(require("debug"));
var mediasoup_rest_api_1 = require("./mediasoup-rest-api");
window.debug = debug;
window.Utils = utils_1.Utils;
window.ConferenceApi = conference_api_1.ConferenceApi;
window.ERROR = constants_1.ERROR;
window.MediasoupRestApi = mediasoup_rest_api_1.MediasoupRestApi;
//# sourceMappingURL=index.js.map
