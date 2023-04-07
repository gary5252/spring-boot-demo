let itemId = document.getElementById('itemId');

var jq = $.noConflict();
var websocket = new WebSocket();
websocket.connect('/connect', () => {
    subscribeRoomInfo();
    websocket.send(`/room-info`, {});
});

function subscribeRoomInfo() {
    websocket.subscribe("/user/queue/room-info", (response) => {
        response = JSON.parse(response.body);
        let roomInfo = response.itemInfo;
        console.log(response);
        // owner = roomInfo.owner;
        // roomId = roomInfo.roomId;
        // $("#room-id").text(roomId);

    })
}

function leaveRoom(roomId) {
    let data = {
        roomId: roomId,
    }
    jq.post(
        "/api/room/leave",
        data,
        (response) => {
            window.location.href = `/auctionRoomList`;
        }
    )
        .fail(function (e) {
            $("#alert-toast-title").text(e.responseJSON.message);
            $("#alert-toast").toast('show');
        })
}

$(document).ready(() => {
    $("#leave").click(function () {
        leavRoom(itemId);
    })
})