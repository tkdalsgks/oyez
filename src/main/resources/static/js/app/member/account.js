function goProfile(memberId) {
    location.href = '/' + `${memberId}` + '/profile';
}

function goAccount(memberId) {
    location.href = '/' + `${memberId}` + '/account';
}

function certifiedEmail(memberId) {
	var memberId = document.getElementById("memberId");
	var memberEmail = document.getElementById("memberEmail");
	
	var params = { 
		"memberId": memberId.value,
		"memberEmail": memberEmail.value
	};
	
	if(role == 'EXPLORE') {
		toastr.options = {
			progressBar: true,
		 	showMethod: 'slideDown',
		 	timeOut: 2000
		};
		toastr.error('해당 계정은 사용할 수 없는 기능입니다.', '');
		
		return false;
	}
	
	$.ajax({
		url: certifiedEmail_uri,
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(params),
		success: function() {
			toastr.success('이메일로 인증링크를 보냈습니다.');
		},
		error: function() {
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
