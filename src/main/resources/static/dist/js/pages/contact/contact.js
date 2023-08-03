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

  $("#input-search").on("keyup", function () {
    var rex = new RegExp($(this).val(), "i");
    $(".search-table .search-items:not(.header-item)").hide();
    $(".search-table .search-items:not(.header-item)")
      .filter(function () {
        return rex.test($(this).text());
      })
      .show();
  });

  $("#btn-add-contact").on("click", function (event) {
    $("#addContactModal #btn-add").show();
    $("#addContactModal #btn-edit").hide();
    $("#addContactModal").modal("show");
  });

  function deleteContact() {
    $(".delete").on("click", function (event) {
      event.preventDefault();
      /* Act on the event */
      $(this).parents(".search-items").remove();
    });
  }

  function addContact() {
    $("#btn-add").click(function () {
      var getParent = $(this).parents(".modal-content");

      var $_name = getParent.find("#username");
      var $_email = getParent.find("#c-email");

      var $_getValidationField =
        document.getElementsByClassName("validation-text");
      var $_nameValue = $_name.val();
      var $_emailValue = $_email.val();

      if ($_nameValue == "") {
        $_getValidationField[0].innerHTML = "유저 아이디는 빈칸일수 없습니다.";
        $_getValidationField[0].style.display = "block";
      } else {
        $_getValidationField[0].style.display = "none";
      }

      if ($_emailValue == "") {
        $_getValidationField[1].innerHTML = "이메일을 채워주세요";
        $_getValidationField[1].style.display = "block";
      } else if (reg.test($_emailValue) == false) {
        $_getValidationField[1].innerHTML = "Invalid Email";
        $_getValidationField[1].style.display = "block";
      } else {
        $_getValidationField[1].style.display = "none";
      }

      if (
        $_nameValue == "" ||
        $_emailValue == "" ||
        reg.test($_emailValue) == false
      ) {
        return false;
      }

      $html =
        '<tr class="search-items">' +
        "<td>" +
        '<div class="n-chk align-self-center text-center">' +
        '<div class="form-check">' +
        '<input type="checkbox" class="form-check-input contact-chkbox primary" id="' +
        cdate +
        '">' +
        '<label class="form-check-label" for="' +
        cdate +
        '"></label>' +
        "</div>" +
        "</div>" +
        "</td>" +
        "<td>" +
        '<div class="d-flex align-items-center">' +
        '<img src="../../assets/images/users/2.jpg" alt="avatar" class="rounded-circle" width="45">' +
        '<div class="ms-3">' +
        '<div class="user-meta-info">' +
        '<h5 class="user-name mb-0" data-name=' +
        $_nameValue +
        ">" +
        $_nameValue +
        "</h5>" +
        "</div>" +
        "</div>" +
        "</div>" +
        "</td>" +
        "<td>" +
        '<span class="usr-email-addr" data-email=' +
        $_emailValue +
        ">" +
        $_emailValue +
        "</span>" +
        "</td>" +
        "<td>" +
        '<div class="action-btn">' +
        '<a href="javascript:void(0)" class="text-info edit"><i data-feather="eye" class="feather-sm fill-white"></i></a>' +
        '<a href="javascript:void(0)" class="text-dark delete ms-2"><i data-feather="trash-2" class="feather-sm fill-white"></i></a>' +
        "</div>" +
        "</td>" +
        "</tr>";

      $(".search-table > tbody >tr:first").before($html);
      $("#addContactModal").modal("hide");
      feather.replace();

      var $_setNameValueEmpty = $_name.val("");
      var $_setEmailValueEmpty = $_email.val("");

      deleteContact();
      editContact();
      checkall("contact-check-all", "contact-chkbox");
    });
  }

  $("#addContactModal").on("hidden.bs.modal", function (e) {
    var $_name = document.getElementById("username");
    var $_email = document.getElementById("c-email");
    var $_getValidationField =
      document.getElementsByClassName("validation-text");
    var $_setNameValueEmpty = ($_name.value = "");
    var $_setEmailValueEmpty = ($_email.value = "");

    for (var i = 0; i < $_getValidationField.length; i++) {
      e.preventDefault();
      $_getValidationField[i].style.display = "none";
    }
  });

  function editContact() {
    $(".edit").on("click", function (event) {
      $("#addContactModal #btn-add").hide();
      $("#addContactModal #btn-edit").show();

      // Get Parents
      var getParentItem = $(this).parents(".search-items");
      var getModal = $("#addContactModal");

      // Get List Item Fields
      var $_name = getParentItem.find(".user-name");
      var $_email = getParentItem.find(".usr-email-addr");
      // Get Attributes
      var $_nameAttrValue = $_name.attr("data-name");
      var $_emailAttrValue = $_email.attr("data-email");

      // Get Modal Attributes
      var $_getModalNameInput = getModal.find("#username");
      var $_getModalEmailInput = getModal.find("#c-email");

      // Set Modal Field's Value
      var $_setModalNameValue = $_getModalNameInput.val($_nameAttrValue);
      var $_setModalEmailValue = $_getModalEmailInput.val($_emailAttrValue);
      $("#addContactModal").modal("show");

      $("#btn-edit").click(function () {
        var getParent = $(this).parents(".modal-content");

        var $_getInputName = getParent.find("#username");
        var $_getInputNmail = getParent.find("#c-email");

        var $_nameValue = $_getInputName.val();
        var $_emailValue = $_getInputNmail.val();

        var setUpdatedNameValue = $_name.text($_nameValue);
        var setUpdatedEmailValue = $_email.text($_emailValue);
        var setUpdatedAttrNameValue = $_name.attr("data-name", $_nameValue);
        var setUpdatedAttrEmailValue = $_email.attr("data-email", $_emailValue);

        $("#addContactModal").modal("hide");
      });
    });
  }

  $(".delete-multiple").on("click", function () {
    var inboxCheckboxParents = $(".contact-chkbox:checked").parents(
      ".search-items"
    );
    inboxCheckboxParents.remove();
  });

  deleteContact();
  addContact();
  editContact();
});

// Validation Process

var $_getValidationField = document.getElementsByClassName("validation-text");
getNameInput = document.getElementById("username");

getNameInput.addEventListener("input", function () {
  getNameInputValue = this.value;

  if (getNameInputValue == "") {
    $_getValidationField[0].innerHTML = "유저 아이디를 채워주세요";
    $_getValidationField[0].style.display = "block";
  } else {
    $_getValidationField[0].style.display = "none";
  }
});

getEmailInput = document.getElementById("c-email");
getEmailInput.addEventListener("input", function () {
  getEmailInputValue = this.value;

  if (getEmailInputValue == "") {
    $_getValidationField[1].innerHTML = "이메일을 채워주세요";
    $_getValidationField[1].style.display = "block";
  } else if (reg.test(getEmailInputValue) == false) {
    $_getValidationField[1].innerHTML = "Invalid Email";
    $_getValidationField[1].style.display = "block";
  } else {
    $_getValidationField[1].style.display = "none";
  }
});

