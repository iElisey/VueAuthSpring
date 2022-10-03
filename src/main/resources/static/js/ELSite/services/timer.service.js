export default class Timer {
    constructor(delayMs, callbackFunc) {
        this.delayMs = delayMs;
        this.callbackFunc = callbackFunc;
        this.timerState = 'new';
    }
  start = function () {
        if (this.tmr) return;

        var self = this;
        this.timerState = 'running';
        this.tmr = setTimeout(function () {
            self._handleTmr();
        }, this.delayMs);
    }
   cancel = function () {
        if (!this.tmr) return;

        clearTimeout(this.tmr);
        this.tmr = null;
        this.timerState = 'canceled';
    }
   _handleTmr = function () {
        this.tmr = null;
        this.timerState = 'completed';
        this.callbackFunc();
    }
}
