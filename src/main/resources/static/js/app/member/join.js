// 회원가입 아이디 유효성 검사
$('#duplicateMemberId').on('click' ,function() {
	const id = document.getElementById('memberId').value;
	const checkMemberId = document.getElementById('checkMemberId');
	const exp = /^[A-Za-z0-9][A-Za-z0-9]{3,16}$/;
	
	if(!id.match(exp)) {
    	checkMemberId.innerHTML = '아이디는 4~20자 사이의 영문, 숫자로 이루어져야 합니다.';
    	$("#duplicateMemberId").attr("value", "");
    } else if(id.match(exp)) {
	console.log("id " + id);
	console.log("checkMemberId " + checkMemberId);
		$.ajax({
			url : "/api/v1/check/id",
			type : "post",
			data : { 
				memberId : id
			},
			dataType : "json",
			success : function(data) {
				if(data.result == "false") {
					checkMemberId.innerHTML = '이미 사용중인 아이디입니다.';
					checkMemberId.style.color = '#DC143C';
					$("#duplicateMemberId").attr("value", "");
		        } else if(data.result == "true") {
		        	checkMemberId.innerHTML = '사용 가능한 아이디입니다.';
					checkMemberId.style.color = '#DA70D6';
		        	$('#memberId').prop('disabled', true);
		        	$('#duplicateMemberId').prop('disabled', true);
		        	$("#duplicateMemberId").attr("value", "Y");
		        }
		    },
		    error: function(data){
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

// 회원가입 비밀번호 유효성 검사
$('#memberPwd').on('keyup', function() {
	const checkMemberPwd = document.getElementById('checkMemberPwd');
	
	if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W])[A-Za-z\d\W]{8,16}$/g.test($('#memberPwd').val())) {
		checkMemberPwd.innerHTML = '비밀번호는 8~16자 사이의 대/소문자, 숫자, 특수문자가 모두 포함되어야 합니다.';
		checkMemberPwd.style.color = '#DC143C';
	} else {
		checkMemberPwd.innerHTML = '사용 가능한 비밀번호 입니다.';
		checkMemberPwd.style.color = '#DA70D6';
		
	}
});

// 회원가입 비밀번호 확인 유효성 검사
$('#memberPwdConfirm').on('keyup', function () {
	const memberPwd = document.getElementById('memberPwd').value;
	const memberPwdConfirm = document.getElementById('memberPwdConfirm').value;
	
	const checkMemberPwdConfirm = document.getElementById('checkMemberPwdConfirm');
	
	if(memberPwd != memberPwdConfirm) {
		checkMemberPwdConfirm.innerHTML = '입력한 비밀번호가 일치하지 않습니다.';
		checkMemberPwdConfirm.style.color = '#DC143C';
	} else {
	  	checkMemberPwdConfirm.innerHTML = '입력한 비밀번호가 일치합니다.';
		checkMemberPwdConfirm.style.color = '#DA70D6';
	}
});

// 회원가입 닉네임 유효성 검사
$('#memberNickname').on('keyup' ,function() {
	const nickname = document.getElementById('memberNickname').value;
	const checkMemberNickname = document.getElementById('checkMemberNickname');
	const exp = /^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,10}$/;
	
	if(!nickname.match(exp)){
    	checkMemberNickname.innerHTML = '닉네임은 2~10자 사이의 한글, 영문, 숫자로 이루어져야 합니다.';
    } else if(nickname.match(exp)) {
		$.ajax({
			url : "/api/v1/check/nickname",
			type : "post",
			dataType : "json",
			data : { "memberNickname" : $("#memberNickname").val() },
			success : function(data) {
				if(data.result == "false") {
					checkMemberNickname.innerHTML = '닉네임은 특수문자를 제외한 2~10자리로 입력해주세요. 이미 사용하고 있는 닉네임입니다.';
					checkMemberNickname.style.color = '#DC143C';
		        } else if(data.result == "true") {
		        	checkMemberNickname.innerHTML = '사용 가능한 닉네임입니다.';
		        	checkMemberNickname.style.color = '#DA70D6';
		        }
		    },
			error: function(data) {
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

// 회원가입 이메일 유효성 검사
$('#memberEmail').on('keyup', function() {
	const email = document.getElementById('memberEmail').value;
	const checkMemberEmail = document.getElementById('checkMemberEmail');
	const exp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
	
	if(!email.match(exp)) {
    	checkMemberEmail.innerHTML = '올바르지 않은 이메일 형식입니다.';
    	$('#check-email').prop('disabled', true);
    } else if(email.match(exp)) {
		$.ajax({
			url : "/api/v1/check/email",
			type : "post",
			dataType : "json",
			data : { "memberEmail" : $("#memberEmail").val() },
			success : function(data) {
				if(data.result == "false") {
					checkMemberEmail.innerHTML = '이미 사용중인 이메일입니다.';
					checkMemberEmail.style.color = '#DC143C';
					$('#check-email').prop('disabled', true);
		        } else if(data.result == "true") {
		        	checkMemberEmail.innerHTML = '사용 가능한 이메일입니다.';
		        	checkMemberEmail.style.color = '#DA70D6';
		        	$('#check-email').prop('disabled', false);
		        }
		    },
			error: function(data) {
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

// 이메일 인증 유효시간
$(function() {
	var display = $("#check-email-time");
	var isCertification = false;
	var key = "";
	var timer = null;
	var isRunning = false;
	
	// 이메일 인증 버튼
	$('#check-email').click(function() {
		$.ajax({
			type : 'post',
			url : '/api/v1/check/mail',
			data : { "memberEmail" : $("#memberEmail").val() },
			success : function(data) {
				isCertification = true;
				var leftSec = 180;
				if(isRunning) {
					clearInterval(timer);
					display.html("");
					startTimer(leftSec, display);
				} else {
					startTimer(leftSec, display);
				}
				$('#check-email-number').prop('disabled', false);
				$('#check-email-verify').prop('disabled', false);
				$('#check-email').prop('disabled', true);
				code = data;
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.success('이메일로 인증번호가 전송되었습니다.');
			},
			error: function(data) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			}
		});
	});
	
	function startTimer(count, display) {
		var minutes, seconds;
		timer = setInterval(function() {
			minutes = parseInt(count / 60, 10);
			seconds = parseInt(count % 60, 10);
			
			minutes = minutes < 10 ? "0" + minutes : minutes;
			seconds = seconds < 10 ? "0" + seconds : seconds;
			
			display.html(minutes + ":" + seconds);
			
			// 타이머 끝
			if(--count < 0) {
				clearInterval(timer);
				$('#check-email-number').prop('disabled', true);
				$('#check-email-verify').prop('disabled', true);
				$('#check-email').prop('disabled', false);
				isRunning = false;
			}
		}, 1000);
		isRunning = true;
	}
	
	// 인증 확인 버튼
	$('#check-email-verify').click(function() {
		$.ajax({
			type : 'post',
			url : '/api/v1/check/verifyCode',
			data : { "code" : $("#check-email-number").val() },
			success : function(result) {
				if(result == 1) {
					isCertification = true;
					clearInterval(timer);
					$('#memberEmail').prop('disabled', true);
					$('#check-email').prop('disabled', true);
					$('#joinSubmit').prop('disabled', false);
					$('#check-email-number').prop('disabled', true);
					$('#check-email-verify').prop('disabled', true);
					$("#check-email-verify").attr("value", "Y");
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.success('인증되었습니다.');
				} else {
					isCertification = false;
					$("#check-email-verify").attr("value", "");
					toastr.options = {
						progressBar: true,
					 	showMethod: 'slideDown',
					 	timeOut: 1500
					};
					toastr.warning('인증번호를 정확하게 입력해주세요.');
				}
			},
			error: function(result) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.error('서버와의 통신 에러입니다.', '잠시 후 재시도 바랍니다.');
			}
		});
	});
});

$('#joinSubmit').click(function() {
	const duplicate = document.getElementById('duplicateMemberId').value;
	const verify = document.getElementById('check-email-verify').value;
	
	if(duplicate != 'Y') {
		toastr.options = {
			progressBar: true,
		 	showMethod: 'slideDown',
		 	timeOut: 1500
		};
		toastr.warning('아이디 중복확인이 필요합니다.');
	} else if(verify == 'Y') {
		toastr.options = {
			progressBar: true,
		 	showMethod: 'slideDown',
		 	timeOut: 1500
		};
		toastr.warning('이메일 인증이 필요합니다.');
	} else {
		
		var joinData = { 
			"memberId" : $("#memberId").val(),
			"memberPwd" : $("#memberPwd").val(),
			"memberNickname" : $("#memberNickname").val(),
			"memberEmail" : $("#memberEmail").val(),
			"useYn" : "Y"
		};
		
		$.ajax({
			type : 'post',
			url : '/api/v1/join',
			data : JSON.stringify(joinData),
			contentType : 'application/json',
			success : function(data) {
				toastr.options = {
					progressBar: true,
				 	showMethod: 'slideDown',
				 	timeOut: 1500
				};
				toastr.options.onHidden = function() { location.href = "/"; };
				toastr.success('회원가입이 완료되었습니다.');
			},
			error: function(data) {
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

// 회원가입 취소
function goHome() {
	swal.fire({
		title: '회원가입을 취소할까요?',
		text: '현재 작성중인 내용은 저장되지 않습니다.',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: '확인',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			location.href = "javascript:history.go(-1)";
		}
	});
}
