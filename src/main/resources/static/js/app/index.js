$('#others-login').on('click' ,function() {
	const one = document.getElementById('form-index-one');
	const two = document.getElementById('form-index-two');
	
	one.style.display = 'none';
	two.style.display = 'initial';
});

$('.btn-kakao-login').on('click', function() {
	window.location = "/oauth2/authorization/kakao";
})

$('.btn-google-login').on('click', function() {
	window.location = "/oauth2/authorization/google";
})

var index = {
    init: function() {
        index.bind();
    },
    bind: function() {
    	$('#login-general').on('click', function() {
			index.login();
    	});
    	$('#memberId').on('keyup', function(e) {
    		if(e.keyCode === 13) {
	    		index.login();    		
    		}
    	});
    	$('#memberPwd').on('keyup', function(e) {
    		if(e.keyCode === 13) {
	    		index.login();    		
    		}
    	});
    },
    login: function() {
    	if( ($("#memberId").val() == "") || ( $("#memberId").val() == null ) ) {
			toastr.warning('아이디를 입력하세요.');
	        $("#memberId").focus();
	        return false;
	    } else if( ($("#memberPwd").val() == "") || ($("#memberPwd").val() == null) ) {
	        toastr.warning('비밀번호를 입력하세요.');
	        $("#memberPwd").focus();
	        return false;
	    }
	    
        $.ajax({
           url: '/login',
           type: 'post',
           dataType: 'json',
           data: {
           		memberId: $("#memberId").val(),
           		memberPwd: $("#memberPwd").val()
           },
           success: function(res) {
           		if(res.code === '200') {
	            	window.location = "/";
            	} else {
					toastr.warning(res.message);
            	}
           },
           error: function(res) {
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

$(function() {
   index.init();
});

$('#explore-login').on('click', function() {
	$.ajax({
		url: '/login',
		type: 'post',
		data: {
			memberId: 'test',
       		memberPwd: 'test'
       	},
       	success: function() {
			window.location = "/";
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
});
