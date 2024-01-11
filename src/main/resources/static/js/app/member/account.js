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
	
	$.ajax({
		url: "/api/v1" + certifiedEmail_uri,
		type: "POST",
		contentType: "application/json",
		dataType: "json",
		data: JSON.stringify(params),
		success: function(response) {
			toastr.success('이메일로 인증링크를 보냈습니다.');
		},
		error: function(response) {
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
