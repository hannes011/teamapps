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
import { ConferenceData, Capturing } from './interfaces';
import { determineVideoSize, MediaStreamWithMixiSizingInfo } from './MultiStreamsMixer';
export declare class Conference {
    private readonly server_url;
    private readonly uid;
    private readonly token;
    private params;
    private live;
    private videoContainer;
    private kind;
    private streamActiveTimeout;
    private _sendStream;
    private _adjustProfile;
    private ms;
    private socket;
    private room;
    private peers;
    private transport;
    private producers;
    private lastProduced;
    constructor(data: ConferenceData);
    private getPermissionsUrl;
    private request;
    private prepareCapture;
    private setupConnection;
    private setupRoom;
    private startSendStream;
    private startListenStream;
    private stopPublish;
    protected __setVideoSource(videoContainer: HTMLMediaElement | null, streamFlow?: MediaStream): void;
    protected __whenStreamIsActive(getStream: Function, callback: Function): boolean;
    protected __hookup(capturing: Capturing): void;
    protected __doConnects(): void;
    protected __connectProducer(type: string, track: MediaStreamTrack | undefined): void;
    protected __startStream(peer: any): MediaStream;
    protected __makeAutoAdjustProfile(videoConsumer: any): Function;
    setPreferredQuality(qualityProfile: string): void;
    private processRoom;
    publish(stream: MediaStream): Promise<string>;
    play(): Promise<string>;
    stop(): Promise<string>;
    static getUserMedia(constraints: MediaStreamConstraints, isDisplay?: boolean): Promise<MediaStream>;
    static mixStreams(inputMediaStreams: MediaStreamWithMixiSizingInfo[], constraints?: MediaStreamConstraints, frameRate?: number): Promise<MediaStream | undefined>;
    static testStreamActive(stream: MediaStream): boolean;
    static listenStreamEnded(stream: MediaStream, listener: () => void): void;
    static determineVideoSize: typeof determineVideoSize;
    static isFirefox: boolean;
    static isOpera: boolean;
    static isChrome: boolean;
}
