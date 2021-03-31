let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");
let sonarqubeIssueUrl = '';
let sonarqubeOverviewUrl = '';
let sonarqubeData = new Object();

$(document).ready(function(){
    getProject();
    getSonarqubeUrl();

});

function getSonarqubeUrl() {
    $.ajax({
            url: '/api/sonar/url',
            contentType: 'application/json',
            type : 'get',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                sonarqubeIssueUrl = data.url + "/project/issues?";
                sonarqubeOverviewUrl = data.url + "/dashboard?";
            },
            error: function(request,status,error){
                alert(" message = " + request.responseText);
            }
        });
}

function getProject() {
    $.ajax({
        url: '/api/sonar/list',
        contentType: 'application/json',
        type : 'get',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            let selectBox = $("#projectBox")
            for (let i = 0; i<data.length; i++) {
                let option = document.createElement('option');
                option.innerText = data[i].project_name;
                selectBox.append(option);
                sonarqubeData[data[i].project_name] = data[i];
            }
        },
        error: function(request,status,error){
            alert(" message = " + request.responseText);
        }
    });
}

$("#projectBox").change(function() {
    let projectName = $("#projectBox").val();
    if (projectName == "프로젝트를 선택해주세요") {
        return;
    }
    let sonarqubeId = sonarqubeData[projectName].id;
    $.ajax({
        url: '/api/sonar/measure?sonarqubeId=' + sonarqubeId,
        contentType: 'application/json',
        type : 'post',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            $("#bug").html(data.bugs);
            $("#smell").html(data.codeSmell);
            $("#security").html(data.vulnerability);
            $("#coverage").html(data.coverage +"%");
            $("#duplication").html(data.duplicated) +"%";
            $("#loc").html(data.loc);
        },
        error: function(request,status,error){
            alert(" message = " + request.responseText);
        }
    });
})

$("#bug_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey, "BUG");
})

$("#security_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey, "VULNERABILITY");

})

$("#smell_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey, "CODE_SMELL");

})

$("#coverage_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey);

})

$("#duplication_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey);
})

$("#loc_btn").on("click",function() {
    let projectName = $("#projectBox").val();
    if(projectName == '프로젝트를 선택해주세요') {
        alert("프로젝트를 선택해주세요");
        return;
    }
    let sonarqubeKey = sonarqubeData[projectName].sonarqube_key;
    openWindow(sonarqubeKey);
})

function openWindow(key, type) {

    if (type) {
        location = sonarqubeIssueUrl + "id=" + key + "&" + "type=" + type
    } else {
        location = sonarqubeOverviewUrl + + "id=" + key;
    }

    window.open(location);
}

