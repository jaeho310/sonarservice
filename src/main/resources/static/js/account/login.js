$(document).ready(function(){
    $("#sign_in_btn").on("click", function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        let userId = $("#userId").val();
        let password = $("#password").val();
        let data = new Object();
        data.userId = userId;
        data.password = password;
        $.ajax({
            url: '/api/users/login',
            contentType: 'application/json',
            data : JSON.stringify(data),
            type : 'post',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                alert("로그인성공");
                window.location.href="/";
            },
            error: function(request,status,error){
                alert(" message = " + request.responseText); // 실패 시 처리
            }
        });
    });

});


