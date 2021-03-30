$(document).ready(function(){
    let isDuplicated = true;

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $("#checkDuplicated_btn").on("click", function() {
        let userId = $("#userId").val();
        if (!userId) {
            alert("아이디를 입력해주세요");
            return;
        }
        $.ajax({
            url: '/api/users/'+userId+'/exist',
            contentType: 'application/json',
            type : 'get',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                if (data){
                    alert("동일한 아이디가 있습니다.");
                } else {
                    alert("사용 가능한 ID 입니다.");
                    isDuplicated = false;
                }
            },
            error: function(request,status,error){
                alert(" message = " + request.responseText);
            }
        });
    });

    $("#join_btn").on("click", function() {
        let userId = $("#userId").val();
        let password = $("#password").val();
        let name = $("#userName").val();


        let data = new Object();
        data.userId = userId;
        data.password = password;
        data.name = name;
        if (isEmpty(data)) {
            alert("빈칸을 모두 채워주세요");
            return;
        }
        if (isDuplicated) {
            alert("ID중복체크가 필요합니다.");
            return;
        }
        $.ajax({
            url: '/api/users/join',
            contentType: 'application/json',
            type : 'post',
            data : JSON.stringify(data),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                alert("회원가입 성공");
                window.location.href = "/";
            },
            error: function(request,status,error){
                alert(" message = " + request.responseText);
            }
        });
    });

});

function isEmpty(data) {
    for (key in data) {
        if(!data[key]) {
            return true;
        }
    }
    return false;
}
