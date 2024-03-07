/* MENU */
document.querySelector("#show__menu").addEventListener("click", show__menu);
document.querySelector("#close__menu").addEventListener("click", close__menu);

function show__menu() {
	document.querySelector(".background__menu").className = "background__menu show__menu";
}

function close__menu() {
	document.querySelector(".background__menu").className = "background__menu";
	$(".openPopupMenu").show();
}

/* CHAT */
document.querySelector("#show__chat").addEventListener("click", show__chat);
document.querySelector("#close__chat").addEventListener("click", close__chat);
document.querySelector("#close__chat2").addEventListener("click", close__chat2);

/*
function show__chat() {
	document.querySelector(".background__chat").className = "background__chat show__chat";
}
*/

function close__chat() {
	document.querySelector(".background__chat").className = "background__chat";
	$(".openPopupChat").show();
}

function close__chat2() {
	document.querySelector(".background__chat2").className = "background__chat2";
	$("#msgArea").html("");
	location.reload(true)
	//$(".openPopupChat2").show();

}

/* CHAT - ROOM */
$(document).ready(function() {
    $("#createChatRoom").on("click", function (e){
        e.preventDefault();

        var name = $("#roomName").val();

        if(name == "") {
			toastr.warning('채팅방 이름을 입력하세요.');
		} else {
            $.ajax({
				url: "/api/v1/chat/room",
				type: "POST",
				dataType: "JSON",
				data: { "name": name },
				success: function() {
					loadChatRoom();
				},
				error: function() {
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
				}
			});
		}
    });
});

function openChatRoom() {
	
	if(role == 'GUEST') {
		swal.fire({
			title: '인증이 필요한 계정입니다.',
			text: '계정 인증 페이지로 이동할까요?',
			footer: '인증 후 OYEZ의 모든 콘텐츠 이용이 가능합니다.',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: '이동',
			cancelButtonText: '취소'
		}).then((result) => {
			if(result.isConfirmed) {
				location.href = '/' + userId + '/account';
			}
		});
	} else {
		if(certified == 'Y') {
			// 게스트 이외의 인증된 권한
			// 채팅 팝업 열림
			document.querySelector(".background__chat").className = "background__chat show__chat";
			
			// 채팅방 리스트 조회
			loadChatRoom();
		} else {
			toastr.warning('계정 인증이 필요한 메뉴입니다.');
		}
	}
}

function loadChatRoom() {

	$.ajax({
        url: "/api/v1/chat/list",
        type: "POST",
        success: function(data) {
            // 채팅방 목록 갱신
            $("#chat__list-chatrooms").html("");
            		
            $.each(data, function(index, value) {
				var chatName = value.privateYn == 'Y' ? `<img src='/img/app/board/lock.png'> ` + value.name : value.name
				
            	$("#chat__list-chatrooms").append(
					`<tr><td style="width: 50%; text-align: left; cursor: pointer; color: var(--text1); text-decoration: underline;" onclick="openChat(\'` + value.roomId + `\')">` + chatName
					+ `</td><td style="width: 25%; color: var(--text1);">` + timeForToday(value.regDate)
					+ `</td><td style="width: 25%; color: var(--text1);">` + value.regUser + `</td></tr>`
	            );
            });
        },
        error: function() {
        	toastr.options = {
				progressBar: true,
			 	showMethod: 'slideDown',
			 	timeOut: 1500
			};
			toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
        }
	});
}

function openChat(roomId) {
	
	if(confirm("채팅방에 입장할까요?")) {
		document.querySelector(".background__chat2").className = "background__chat2 show__chat2";
		
		$.ajax({
			url: "/api/v1/chat/message",
			type: "POST",
			data: { "roomId": roomId },
			success: function(data) {
				
				$.each(data, function(index, value) {
	            	if(memberId === value.memberId) {
				        str = "<div class='chat__me'>"
				        	+ "<span class='chat__me_history_time'>" + timeForToday(value.regDate) + "</span>"
				        	+ "<span class='chat__me_history_message'>" + value.message + "</div>";
					} else if("ADMIN" == value.memberId) {
						str = "<div class='chat__notice'><p class='chat__enter'>" + value.message + "</p></div>";
					} else {
				        str = "<div class='chat__others'>"
					        + "<img class='chat__profileImg' src='" + value.profileImg + "'><span class='chat__others_history_name'> " + value.memberNickname + "</span>"
					        + "<span class='chat__others_history_time'>" + timeForToday(value.regDate) + "</span>"
					        + "<div class='chat__others_history_message'>" + value.message
					        + "</span></div></div>";
				    }
					$("#msgArea").append(str);
	            });
	            
				connected(roomId)
			},
			error: function() {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			}
		});
	}
}


function connected(roomId) {
	var sockJS = new SockJS("/ws/chat", null, { transports: ["websocket", "xhr-streaming", "xhr-polling"] });
	stompClient = Stomp.over(sockJS)
	var headers = {
		name: memberId
	}
	
	
	stompClient.connect(headers, onConnected, onError)
	
	function onConnected() {
		/*
		stompClient.subscribe("/sub/chat/enter/" + roomId, function(chat) {
			var chatroom = document.getElementById("msgArea");
			var content = JSON.parse(chat.body);
			
			var writer = content.writer;
			var message = content.message;
			var str = '';
			
			str = "<div class='chat__notice'>";
			str += "<p class='chat__enter'>" + message + "</p>";
			str += "</div>";
			
			$("#msgArea").append(str);
			chatroom.scrollBy(0, chatroom.scrollHeight);
		})
		*/
		
		stompClient.subscribe("/sub/chat/room/" + roomId, function(chat) {
			var chatroom = document.getElementById("msgArea");
			var content = JSON.parse(chat.body);
			
			var writer = content.writer;
			var writerId = content.writerId;
			var message = content.message;
			var str = '';
			
			var today = new Date()
			var hours = ('0' + today.getHours()).slice(-2)
			var minutes = ('0' + today.getMinutes()).slice(-2)
			var amPm
			if(hours < 12) {
				amPm = "오전"
			} else {
				amPm = "오후"
			}
			if(hours > 12) {
				hours -= 12
			} else if(hours == 0) {
				hours = 12;
			}
			var timeString = amPm + " " + hours + ":" + minutes
			
			//console.log(writer + " " + writerId + " " + message);
			
			if(writer === membername) {
				str = "<div class='chat__me'>"
					+ "<span class='chat__me_live_time'>" + timeString + "</span>"
					+ "<span class='chat__me_live_message'>" + message + "</div>";
			} else {
				str = "<div class='chat__others'>"
			        + "<img class='chat__profileImg' src=''><span class='chat__others_live_name'> " + writer + "</span>"
			        + "<span class='chat__others_live_time'>" + timeString + "</span>"
			        + "<div class='chat__others_live_message'>" + message
			        + "</span></div></div>";
			}
			
			$("#msgArea").append(str);
			chatroom.scrollBy(0, chatroom.scrollHeight);
		})
		
		/*
		stompClient.subscribe("/sub/chat/leave/" + roomId, function(chat) {
			var chatroom = document.getElementById("msgArea");
			var content = JSON.parse(chat.body);
			
			var writer = content.writer;
			var message = content.message;
			var str = '';
			
			str = "<div class='chat__notice'>";
			str += "<p class='chat__enter'>" + message + "</p>";
			str += "</div>";
			
			$("#msgArea").append(str);
			chatroom.scrollBy(0, chatroom.scrollHeight);
		})
		*/
	}
	
	function onError(e) {
	    console.error('stomp connect error - ', e);
	}
    
    $("#close__chat2").click(function() {
		disconnected(roomId)
	})
    
    $("#msg").keydown(function(e) {
    	if(e.keyCode === 13) {
    		const msg = document.getElementById("msg");
    		
    		if (msg.value.trim() == "") {
    			toastr.warning('내용을 입력하세요.');
				msg.focus();
				return false;
    		} else {
    			stompClient.send('/pub/chat/message/' + roomId, {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername, message: msg.value}));
	    		msg.value = '';
	    		msg.focus();
	    		//savePoints();
    		}
    	}
    });
    
    $("#button-send").on("click", function(e){
    	const msg = document.getElementById("msg");
    	
    	if (msg.value.trim() === "") {
    		toastr.warning('내용을 입력하세요.');
    		msg.focus();
			return false;
    	} else {
    		stompClient.send('/pub/chat/message', {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername, message: msg.value}));
	    	msg.value = '';
	    	msg.focus();
	    	//savePoints();
    	}
    });
}

function disconnected(roomId) {
		exit(roomId)
		this.stompClient.disconnect()
}

function exit(roomId) {
	//stompClient.send('/pub/chat/leave', {}, JSON.stringify({roomId: roomId, writerId: memberId, writer: membername, message: msg.value}))
	stompClient.unsubscribe("/sub/chat/message/" + roomId, {}, JSON.stringify({roomId: roomId, writerId: memberId}))
	stompClient.unsubscribe("/sub/chat/room/" + roomId, {}, JSON.stringify({roomId: roomId, writerId: memberId}))
}



		/*
	$(document).ready(function() {
		$('#msgArea').scrollTop($('#msgArea')[0].scrollHeight);
		
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
	    */


