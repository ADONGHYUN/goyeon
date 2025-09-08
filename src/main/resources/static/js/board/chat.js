var ws;

function connect() {
    ws = new WebSocket("ws://localhost:8080/chat");
    ws.onmessage = function(event) {
        var chatArea = document.getElementById("chatArea");
        chatArea.value += event.data + "\n";
    };
}

function sendMessage() {
    let userId = document.getElementById("userId").value; // hidden input에서 ID 가져오기

    if (userId === "null") {
        alert("로그인이 필요합니다");
        window.location.href = "/login";
        return; // ID가 없으면 메시지 전송을 막음
    }

    var message = document.getElementById("message").value;
    ws.send(message);
    document.getElementById("message").value = '';
}

window.onload = connect;
