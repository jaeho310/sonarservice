let fileList = new Object();
let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function(){
        getFileList();
});

function getFileList() {
    $.ajax({
        url: '/api/files',
        contentType: 'application/json',
        type : 'get',
        success: function(data) {
            let selectBox = $("#projectBox")
            let html = '';
            for (let i = 0; i<data.length; i++) {
                let option = document.createElement('option');
                option.innerText = data[i].projectName;
                selectBox.append(option);
                fileList[data[i].projectName] = data[i];
                html += '<tr>';
                html += '<td>' + data[i].id + '</td>';
                html += '<td>' + data[i].projectName + '</td>';
                html += '<td>' + data[i].fileName + '</td>';
                html += '</tr>';
            }
            $("#fileTableBody").empty();
            $("#fileTableBody").append(html);
        },
        error: function(request,status,error){
            alert(" message = " + request.responseText);
        }
    });
}

$("#delete_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    let fileId = fileList[projectName].id;

    $.ajax({
        url: '/api/files?fileId=' + fileId,
        contentType: 'application/json',
        type : 'delete',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            alert("삭제되었습니다.")
            window.location.reload();
        },
        error: function(request,status,error){
            alert(" message = " + request.responseText);
        }
    });
})


