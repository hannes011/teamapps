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

import {css, parseHtml} from "./Common";
import {AbstractUiComponent} from "./AbstractUiComponent";
import {UiAudioLevelIndicatorCommandHandler, UiAudioLevelIndicatorConfig} from "../generated/UiAudioLevelIndicatorConfig";
import {TeamAppsUiContext} from "./TeamAppsUiContext";
import {TeamAppsUiComponentRegistry} from "./TeamAppsUiComponentRegistry";

export class UiAudioLevelIndicator extends AbstractUiComponent<UiAudioLevelIndicatorConfig> implements UiAudioLevelIndicatorCommandHandler {
	private $main: HTMLElement;
	private $activityDisplay: HTMLElement;
	private $canvas: HTMLCanvasElement;
	private mediaStreamSource: MediaStreamAudioSourceNode;
	private audioContext: AudioContext;
	private mediaStreamToBeClosedWhenUnbinding: MediaStream;
	private canvasContext: CanvasRenderingContext2D;

	constructor(config: UiAudioLevelIndicatorConfig, context: TeamAppsUiContext) {
		super(config, context)

		this.$main = parseHtml(`
<div class="UiAudioLevelIndicator">
	<canvas class="hidden"></canvas>
</div>`);
		this.$activityDisplay = this.$main;
		this.$canvas = this.$main.querySelector<HTMLElement>(':scope canvas') as HTMLCanvasElement;
		this.canvasContext = this.$canvas.getContext("2d");
	}

	private analyserNode: AnalyserNode;

	public bindToStream(mediaStream: MediaStream, mediaStreamIsExclusiveToThisComponent: boolean) {
		this.unbind();

		if (mediaStreamIsExclusiveToThisComponent) {
			this.mediaStreamToBeClosedWhenUnbinding = mediaStream;
		}

		(window as any).AudioContext = (window as any).AudioContext || (window as any).webkitAudioContext;
		this.audioContext = new AudioContext();

		let scriptProcessor = this.audioContext.createScriptProcessor(2048, 1, 1);
		scriptProcessor.connect(this.audioContext.destination);

		this.analyserNode = this.audioContext.createAnalyser();
		this.analyserNode.smoothingTimeConstant = 0.3;
		this.analyserNode.fftSize = 32; // 16 spectral lines - that's the minimum. we don't want to get a spectral analysis anyway...
		this.analyserNode.connect(scriptProcessor);

		this.mediaStreamSource = this.audioContext.createMediaStreamSource(mediaStream);
		this.mediaStreamSource.connect(this.analyserNode, 0, 0);

		scriptProcessor.onaudioprocess = this.throttle(() => window.requestAnimationFrame(() => {
			const array = new Uint8Array(this.analyserNode.frequencyBinCount);
			this.analyserNode.getByteFrequencyData(array);
			const maxVolume = UiAudioLevelIndicator.getMaxVolume(array) / 255;
			this.draw(maxVolume);
		}), 100);

		this.$canvas.classList.remove("hidden");
	}

	private draw(level: number) {
		let canvasWidth = this.canvasContext.canvas.width;
		let canvasHeight = this.canvasContext.canvas.height;

		var imageData = this.canvasContext.getImageData(this._config.barWidth, 0, canvasWidth - this._config.barWidth, canvasHeight);
		this.canvasContext.putImageData(imageData, 0, 0);
		this.canvasContext.clearRect(canvasWidth - this._config.barWidth, 0, this._config.barWidth, canvasHeight);

		var grd = this.canvasContext.createLinearGradient(0, canvasHeight, 0, 0);
		grd.addColorStop(0, "#192e83")
		grd.addColorStop(.4, "#0fb83f")
		grd.addColorStop(.6, "#0fb83f")
		grd.addColorStop(.8, "orange")
		grd.addColorStop(.95, "#e00")
		this.canvasContext.fillStyle = grd;

		// draw a bar based on the current volume
		let volumeBarSize = canvasHeight * level;
		this.canvasContext.fillRect(canvasWidth - this._config.barWidth, canvasHeight - volumeBarSize, this._config.barWidth, volumeBarSize);
	}

	/**
	 *  Returns a function, that will limit the invocation of the specified function to once in delay. Invocations that come before the end of delay are ignored!
	 */
	private throttle(func: Function, delay: number): (() => void) {
		let previousCall = 0;
		return function () {
			const time = new Date().getTime();
			if ((time - previousCall) >= delay) {
				previousCall = time;
				func.apply(this, arguments);
			}
		};
	}

	public unbind() {
		this.audioContext && this.audioContext?.close()
			.catch(reason => console.log(reason));
		this.mediaStreamSource?.disconnect();
		if (this.mediaStreamToBeClosedWhenUnbinding != null) {
			this.mediaStreamToBeClosedWhenUnbinding.getTracks().forEach(t => t.stop());
		}
		this.$canvas.classList.add("hidden");
	}

	private static getMaxVolume(sampleVolumes: Uint8Array) {
		let max = 0;
		let sum = 0;
		for (let i = 0; i < sampleVolumes.length; i++) {
			max = Math.max(max, sampleVolumes[i]);
			sum += sampleVolumes[i];
		}
		return max;
	}

	onResize() {
		this.$canvas.width = this.getWidth();
		this.$canvas.height = this.getHeight();
	}

	public doGetMainElement() {
		return this.$main;
	}

	public async setDeviceId(deviceId: string) {
		if (deviceId == null) {
			this.unbind();
			return;
		} else {
			let mediaStream = await navigator.mediaDevices.getUserMedia({
				audio: {
					deviceId
				}
			});
			this.bindToStream(mediaStream, true);
		}
	}
}

TeamAppsUiComponentRegistry.registerComponentClass("UiAudioLevelIndicator", UiAudioLevelIndicator);
