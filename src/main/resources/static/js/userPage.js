// 서버로부터 가져온 기존의 기술 스택 데이터를 파싱하여 배열로 변환하는 함수
function parseSkillStack(skillStack) {
    return skillStack.split(",");
}

// 기존 기술 스택들을 페이지 로드 시에 보여주기 위해 초기화하는 함수
function initSkills() {
    $.ajax({
        type: "GET",
        url: "/user/userSkillStack", // 서버에서 데이터를 가져올 엔드포인트 설정
        dataType: "json",
        success: function (data) {
            var skillList = document.getElementById("selected-skills-list");
            var skills = parseSkillStack(data.userSkillStack);

            // 기존 기술 스택들을 동적으로 생성하여 추가합니다.
            skills.forEach(function (skill) {
                addSkill(skillList, skill);
            });
        },
        error: function () {
            alert("서버에서 데이터를 가져오는데 실패했습니다.");
        }
    });
}

// 기술스택을 동적으로 추가하는 함수
function addSkill(skillList, skill) {
    var li = document.createElement("li");
    li.innerText = skill;

    var deleteButton = document.createElement("button");
    deleteButton.innerText = "삭제";
    deleteButton.className = "btn btn-light-danger text-danger btn-sm";
    deleteButton.addEventListener("click", function () {
        removeSkill(skillList, li);
    });

    li.appendChild(deleteButton);
    skillList.appendChild(li);
}

function removeSkill(skillList, skillItem) {
    skillList.removeChild(skillItem);

}

// 페이지 로드 시에 기존 기술스택들을 초기화합니다.
document.addEventListener("DOMContentLoaded", function () {
    initSkills();
});


function clickButton() {

    var data = {};
    data.username = $("#username").val();
    // data.password = $("#password1").val();
    // data.password2 = $("#password2").val();
    // data.skillStack = $("#selected-skills-input").val();
    var skillList = document.querySelectorAll("#selected-skills-list li");
    var skillStackArray = [];
    skillList.forEach(function (skillItem) {
        var skillText = skillItem.innerText.replace("삭제", "").trim();
        skillStackArray.push(skillText);
    });
    // 추가한 기술 스택 수집
    var newSkillList = document.querySelectorAll(".new-skill");
    newSkillList.forEach(function (newSkillItem) {
        skillStackArray.push(newSkillItem.innerText);
    });

    data.skillStack = skillStackArray.join(",");

    // data.experience = $("#experience").val();

    var selectedExperienceId = $('input[name="experience"]:checked').attr('id');
    var selectedExperienceValue = $('label[for="' + selectedExperienceId + '"]').text();
    data.experience = selectedExperienceValue;
    data.email = $("#email").val();


    var jsonStr = JSON.stringify(data);

    $.ajax({
        type: 'PUT',
        url: "/user/modify",
        data: jsonStr,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            Swal.fire(
                data.title,
                data.message,
                data.flag
            );
            if (data.flag === 'success') {
                setTimeout(function () {
                    window.location.href = "/";
                }, 3000);
            }
        },
        error: function (err) {
            alert('서버 통신 실패');
        }
    });
}

document.getElementById('delete-button').addEventListener('click', function () {
    // 사용자의 ID 가져오기
    var userId = document.getElementById('id').value;


    fetch('/user/' + userId, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                // 성공적으로 삭제되었을 때
                alert('회원 탈퇴가 완료되었습니다');
                location.href = '/';
            } else {
                // 오류 발생 시
                alert('회원 탈퇴를 할수 없습니다');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('알수 없는 오류로 실패하였습니다.');
        });
});
