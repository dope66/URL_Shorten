$(function () {
    function checkall(clickchk, relChkbox) {
        var checker = $("#" + clickchk);
        var multichk = $("." + relChkbox);

        checker.click(function () {
            multichk.prop("checked", $(this).prop("checked"));
            $(".show-btn").toggle();
        });
    }

    checkall("contact-check-all", "contact-chkbox");

    $("#btn-add-contact").on("click", function (event) {
        $("#addContactModal #btn-add").show();
        $("#addContactModal").modal("show");
    });


    // function addContact() {
    //     $("#btn-add").click(function () {
    //         var getParent = $(this).parents(".modal-content");
    //         var $_username = getParent.find("#username");
    //         var $_email = getParent.find("#email");
    //         var $_password = getParent.find("#password");
    //         var formData={
    //             username: $_nameValue,
    //             email: $_emailValue,
    //             password: $_passwordValue
    //         }
    //         var $_getValidationField =
    //             document.getElementsByClassName("validation-text");
    //         var reg = /^.+@[^\.].*\.[a-z]{2,}$/;
    //         var passwordReg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
    //         var $_nameValue = $_username.val();
    //         var $_emailValue = $_email.val();
    //         var $_passwordValue = $_password.val();
    //
    //         if ($_nameValue == "") {
    //             $_getValidationField[0].innerHTML = "아이디를 채워주세요";
    //             $_getValidationField[0].style.display = "block";
    //         } else {
    //             $_getValidationField[0].style.display = "none";
    //         }
    //
    //         if ($_emailValue == "") {
    //             $_getValidationField[1].innerHTML = "이메일을 채워주세요";
    //             $_getValidationField[1].style.display = "block";
    //         } else if (reg.test($_emailValue) == false) {
    //             $_getValidationField[1].innerHTML = "이메일 형식을 맞춰주세요";
    //             $_getValidationField[1].style.display = "block";
    //         } else {
    //             $_getValidationField[1].style.display = "none";
    //         }
    //         if ($_passwordValue == "") {
    //             $_getValidationField[1].innerHTML = "비밀번호 채워주세요";
    //             $_getValidationField[1].style.display = "block";
    //         } else if (passwordReg.test($_passwordValue) == false) {
    //             $_getValidationField[1].innerHTML = "비밀번호 형식을 맞춰주세요";
    //             $_getValidationField[1].style.display = "block";
    //         } else {
    //             $_getValidationField[1].style.display = "none";
    //         }
    //
    //         if (
    //             $_nameValue == "" ||
    //             $_emailValue == "" ||
    //             reg.test($_emailValue) == false||
    //             $_passwordValue==""||
    //             passwordReg.test($_passwordValue)==false
    //         ) {
    //             return false;
    //         }
    //         if($_nameValue == "" || $_emailValue == "" || reg.test($_emailValue) == false||
    //             $_passwordValue==""|| passwordReg.test($_passwordValue)==false){
    //             $("#success-message").text(""); // 성공 메시지 지움
    //             $("#error-message").text("입력된 정보를 확인해주세요."); // 실패 메시지 표시
    //             return false;
    //         } else {
    //             $("#error-message").text("");
    //         }
    //         // $.ajax({
    //         //     type:"POST",
    //         //     url:"/admin/adminRegister",
    //         //     data:JSON.stringify(formData),
    //         //     contentType: "application/json",
    //         //     success: function (data) {
    //         //         $("#modal-message").text(""); // 이전 메시지를 지웁니다.
    //         //         $("#modal-message").text(data); // 서버로부터의 성공 메시지를 모달에 표시합니다.
    //         //     },
    //         //     error: function (xhr, textStatus, errorThrown) {
    //         //         $("#modal-message").text(""); // 이전 메시지를 지웁니다.
    //         //         $("#modal-message").text(xhr.responseText); // 서버로부터의 오류 메시지를 모달에 표시합니다.
    //         //     },
    //         // });
    //
    //             // AJAX request to submit the form
    //             $.ajax({
    //             type: "POST",
    //             url: "/admin/adminRegister",
    //             data: JSON.stringify(formData),
    //             success: function (data) {
    //
    //             $("#modal-message").text("Success: " + data);
    //             $("#modal-message").addClass("text-success"); // 성공 메시지는 초록색으로 표시합니다.
    //         },
    //             error: function (xhr, textStatus, errorThrown) {
    //
    //             $("#modal-message").text("Error: " + xhr.responseText);
    //             $("#modal-message").addClass("text-danger"); // 오류 메시지는 빨간색으로 표시합니다.
    //         },
    //         });
    //
    //
    //             // 모달이 닫힐 때 메시지와 클래스를 초기화합니다.
    //             $("#addContactModal").on("hidden.bs.modal", function () {
    //             $("#modal-message").text("");
    //             $("#modal-message").removeClass("text-success");
    //             $("#modal-message").removeClass("text-danger");
    //         });
    //
    //         $("#addContactModal").modal("hide");
    //         var $_setNameValueEmpty = $_username.val("");
    //         var $_setEmailValueEmpty = $_email.val("");
    //         var $_setPasswordValueEmpty = $_password.val("");
    //
    //         checkall("contact-check-all", "contact-chkbox");
    //     });
    // }

    $("#addContactModal").on("hidden.bs.modal", function (e) {
        var $_username = document.getElementById("username");
        var $_email = document.getElementById("c-email");
        var $_password = document.getElementById("password");
        var $_getValidationField =
            document.getElementsByClassName("validation-text");
        var $_setNameValueEmpty = ($_username.value = "");
        var $_setEmailValueEmpty = ($_email.value = "");
        var $_setPasswordValueEmpty = ($_password.value = "");

        for (var i = 0; i < $_getValidationField.length; i++) {
            e.preventDefault();
            $_getValidationField[i].style.display = "none";
        }
    });
    addContact();
});

// // Validation Process
// var $_getValidationField = document.getElementsByClassName("validation-text");
// var reg = /^.+@[^\.].*\.[a-z]{2,}$/;
// var passwordReg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
// getNameInput = document.getElementById("username");
//
// getNameInput.addEventListener("input", function () {
//     getNameInputValue = this.value;
//
//     if (getNameInputValue == "") {
//         $_getValidationField[0].innerHTML = "이름이 요구됨";
//         $_getValidationField[0].style.display = "block";
//     } else {
//         $_getValidationField[0].style.display = "none";
//     }
// });
//
// getEmailInput = document.getElementById("email");
//
// getEmailInput.addEventListener("input", function () {
//     getEmailInputValue = this.value;
//
//     if (getEmailInputValue == "") {
//         $_getValidationField[1].innerHTML = "이메일이 요구됨";
//         $_getValidationField[1].style.display = "block";
//     } else if (reg.test(getEmailInputValue) == false) {
//         $_getValidationField[1].innerHTML = "이메일 형식";
//         $_getValidationField[1].style.display = "block";
//     } else {
//         $_getValidationField[1].style.display = "none";
//     }
// });
