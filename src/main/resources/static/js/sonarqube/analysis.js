let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let fileList = new Object();

$(document).ready(function(){
    getProject();

    $("#analysis_btn").on("click", function() {
        let projectName = $("#projectBox").val();
        let fileId = fileList[projectName].id;
        let result = confirm("분석에 시간이 오래걸립니다. 분석하시겠습니까?");
        if (!result) {
            return;
        }
        $("#loading").show();
        $.ajax({
            url: '/api/sonar/analysis?fileId=' + fileId,
            contentType: 'application/json',
            type : 'post',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                alert("분석완료, 대시보드창을 확인해주세요");
                $("#loading").hide();
            },
            error: function(request,status,error){
                alert(" message = " + request.responseText);
                $("#loading").hide();
            }
        });
    });

});

function getProject() {
    $.ajax({
        url: '/api/files',
        contentType: 'application/json',
        type : 'get',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            let selectBox = $("#projectBox")
            for (let i = 0; i<data.length; i++) {
                let option = document.createElement('option');
                option.innerText = data[i].projectName;
                selectBox.append(option);
                fileList[data[i].projectName] = data[i];
            }
        },
        error: function(request,status,error){
            alert(" message = " + request.responseText);
        }
    });
}
