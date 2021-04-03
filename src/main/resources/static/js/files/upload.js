let fileList = new Object();
let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function(){
    getFileList();
    errorMsg = $("#message").html();
    if (errorMsg) {
        alert(errorMsg)
    }
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
                html += '<td>' + data[i].projectName + '</td>';
                html += '<td>' + data[i].fileName + '</td>';
                html += '</tr>';
            }
            $("#fileTableBody").empty();
            $("#fileTableBody").append(html);

            pagination();
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

function pagination() {
    var req_num_row=5;
    var $tr=jQuery('tbody tr');
    var total_num_row=$tr.length;
    if (total_num_row == 0 || total_num_row < 6){
        $(".pagination").hide();
    }
    var num_pages=0;
    if(total_num_row % req_num_row ==0){
      num_pages=total_num_row / req_num_row;
    }
    if(total_num_row % req_num_row >=1){
      num_pages=total_num_row / req_num_row;
      num_pages++;
      num_pages=Math.floor(num_pages++);
    }

    jQuery('.pagination').append("<li><a class=\"prev\">Previous</a></li>");

    for(var i=1; i<=num_pages; i++){
      jQuery('.pagination').append("<li><a>"+i+"</a></li>");
    jQuery('.pagination li:nth-child(2)').addClass("active");
    jQuery('.pagination a').addClass("pagination-link");
    }

    jQuery('.pagination').append("<li><a class=\"next\">Next</a></li>");

    $tr.each(function(i){
    jQuery(this).hide();
    if(i+1 <= req_num_row){
          $tr.eq(i).show();
      }
    });

    jQuery('.pagination a').click('.pagination-link', function(e){
      e.preventDefault();
      $tr.hide();
      var page=jQuery(this).text();
      var temp=page-1;
      var start=temp*req_num_row;
    var current_link = temp;

    jQuery('.pagination li').removeClass("active");
      jQuery(this).parent().addClass("active");

      for(var i=0; i< req_num_row; i++){
          $tr.eq(start+i).show();
      }

    if(temp >= 1){
    jQuery('.pagination li:first-child').removeClass("disabled");
    }
    else {
    jQuery('.pagination li:first-child').addClass("disabled");
    }

    });

    jQuery('.prev').click(function(e){
    e.preventDefault();
    jQuery('.pagination li:first-child').removeClass("active");
    });

    jQuery('.next').click(function(e){
    e.preventDefault();
    jQuery('.pagination li:last-child').removeClass("active");
    });
}


