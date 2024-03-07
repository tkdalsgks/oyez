$(document).ready(function() {
		
    //console.log(roomName + ", " + roomId + ", " + membername);

    var sockJs = new SockJS("/ws/chat");
    // 1. SockJS를 내부에 들고있는 stomp를 내어준다.
    var stomp = Stomp.over(sockJs);

    // 2. connection이 맺어지면 실행
    stomp.connect({}, function () {
       //console.log("STOMP Connection")

       // 4. subscribe(path, callback)으로 메세지를 받을 수 있음
       stomp.subscribe("/sub/chat/room/" + roomId, function(chat) {
           var chatroom = document.getElementById("msgArea");
           var content = JSON.parse(chat.body);

           var writer = content.writer;
           var writerId = content.writerId;
           var message = content.message;
           var str = '';
           
           //console.log(writer + " " + writerId + " " + message);
           
           if(writer === membername) {
               str = "<p class='chat__me'>" + message + "</p>";
    	   } else {
               str = "<p class='chat__others'>" + '&#x1F607' + " " + writer + " : " + message + "</p>";
           }
           
           $("#msgArea").append(str);
           chatroom.scrollBy(0, 500);
       });
       
       // 4-1. 입장 메세지
       stomp.subscribe("/sub/chat/entry/" + roomId, function(chat) {
           var chatroom = document.getElementById("msgArea");
           var content = JSON.parse(chat.body);

           var writer = content.writer;
           var message = content.message;
           var str = '';
           
           str = "<div class='alert alert-secondary'>";
    	   str += "<p class='chat__enter'>" + message + "</p>";
    	   str += "</div>";
    	   
    	   $("#msgArea").append(str);
           chatroom.scrollBy(0, 500);
       });
       
       // 4-2. 퇴장 메세지
       stomp.subscribe("/sub/chat/leave/" + roomId, function(chat) {
           var chatroom = document.getElementById("msgArea");
           var content = JSON.parse(chat.body);

           var writer = content.writer;
           var message = content.message;
           var str = '';
           
           str = "<div class='alert alert-secondary'>";
    	   str += "<p class='chat__enter'>" + message + "</p>";
    	   str += "</div>";
    	   
    	   $("#msgArea").append(str);
           chatroom.scrollBy(0, 500);
       });
       
       // 3. send(path, header, message)로 메세지를 보낼 수 있음
       stomp.send('/pub/chat/enter', {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername}))
    });
    
    $("#msg").keydown(function(e) {
    	if(e.keyCode === 13) {
    		const msg = document.getElementById("msg");
    		
    		if (msg.value === "" || msg.value == null) {
    			toastr.warning('내용을 입력하세요.');
				msg.focus();
				return false;
    		} else {
    			//console.log(membername + ":" + msg.value);
    			stomp.send('/pub/chat/message', {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername, message: msg.value}));
	    		msg.value = '';
	    		//savePoints();
    		}
    	}
    });
    
    $("#button-send").on("click", function(e){
    	const msg = document.getElementById("msg");
    	
    	if (msg.value === "" || msg.value == null) {
    		toastr.warning('내용을 입력하세요.');
    		msg.focus();
			return false;
    	} else {
    		//console.log(membername + ":" + msg.value);
    		stomp.send('/pub/chat/message', {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername, message: msg.value}));
	    	msg.value = '';
	    	//savePoints();
    	}
    });
});

// 채팅 내역 불러오기
listchat.forEach(function(value) {
	var list_key = Object.keys(value);
	var list_value = value[list_key];
	
	if(memberId === value.writerId) {
        str = "<p class='chat__me'>" + value.message + "</p>";
	} else {
        str = "<p class='chat__others'>" + '&#x1F607' + " " + value.writer + " : " + value.message + "</p>";
    }
	$("#msgArea").append(str);
});

// 포인트 적립
function savePoints() {
	var pointsCd = "4";
	var points = "1";
	
	var headers = { "Content-Type": "application/json", "X-HTTP-Method-Override": "POST" };
	var params = { "pointsCd": pointsCd, "points": points, "memberId": memberId };
	
	//console.log("params : " + JSON.stringify(params));
	
	$.ajax({
		url: "/points",
		type: "POST",
		headers: headers,
		dataType: "JSON",
		data: JSON.stringify(params),
		success: function(response) {
			if(response.result == false) {
				toastr.warning('포인트 적립에 실패했습니다.');
				return false;
			} else {
				return true;
			}
		},
		error: function(xhr, status, error) {
			toastr.options = {
				progressBar: true,
			 	showMethod: 'slideDown',
			 	timeOut: 1500
			};
			toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			return false;
		}
	});
}

