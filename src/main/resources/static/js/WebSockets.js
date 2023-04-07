/**
 * 我們重新包裝 SockJS 的操作，.send()、.subscribe() 必須要在已經連線
 * 的時候才可以觸發，他應該放在 .connect() 的那個函數內，因為這牽涉到同步
 * 與非同步問題，避免文章過於冗長且失焦，這邊用最簡單但不太靠普的方式處
 * 理 send()、subscribe()，在還沒連線時，就先延遲一秒(setTimeout())，
 * 等待 connect 執行完畢才執行，之後操作時，先生成一個 WebSocket 的物件，
 * 然後使用連線函數(connect())，並傳入對應函數來處理連線後的動作。
 */

class WebSocket {
    constructor() {
        this.stompClient = null;
    }

    connect(connect, callback) {
        let socket = new SockJS(connect);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, function (frame) {
            callback();
        });
    }

    disconnect() {
        if (this.isConnected()) {
            this.stompClient.disconnect();
        }
    }

    send(controller, json) {
        setTimeout(() => {
            this.stompClient.send(controller, {}, JSON.stringify(json));
        }, 1000);
    }

    subscribe(topic, onMessage) {
        setTimeout(() => {
            this.stompClient.subscribe(topic, onMessage);
        }, 1000);
    }

    isConnected() {
        return this.stompClient !== null;
    }
}