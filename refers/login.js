var $select_certificate;
var nationality;
var certificate=0;

$(function () {
    $select_certificate = $('#select_certificate');
    getCertificateList();
    $("#btn_close").on('click', function () {
        $("#div_promptDialog").hide();
        $("#div_promptDialogContent").hide();
    });
    $("#btn_login").on('click', function () {
        // if (isEmpty(nationality)) {
        //     layerHelp.msg("请选择国籍/地区");
        //     return;
        // }
        // if (isEmpty(certificate)) {
        //     layerHelp.msg(layerCertificate);
        //     return;
        // }
        var inputIdCardNo = $("#input_idCardNo").val().trim();
        var inputPwd = $("#input_pwd").val().trim();
        if (isEmpty(inputIdCardNo)) {
            layerHelp.msg(layerIdCardNo);
            return;
        }
        if (isEmpty(inputPwd)) {
            layerHelp.msg(layerPwd);
            return;
        }
        var verifyCode = $("#input_verifyCode").val().trim();
        login(certificate, inputIdCardNo, inputPwd,verifyCode);
    });
    $select_certificate.change(function () {
        certificate = $select_certificate.val();
        var certificateText = $select_certificate.find("option:selected").text();
        $("#div_certificate").html(certificateText);
    });
    $("#btn_register").on('click', function () {
        window.location.href = "/userPage/register";
    });

    $("#div_nationality").on('click', function () {
        $("#div_login").hide();
        $("#div_chooseNationality").show();
    });

    //选择国籍
    $('.container').show();
    // $('body').on('click', '.city-list p', function () {
    //     alert($(this).html())
    // });
    $('body').on('click', '.letter a', function () {
        var s = $(this).html();
        //$(window).scrollTop($('#' + s + '1').offset().top);
        $('html,body').animate({scrollTop: $('#' + s + '1').offset().top}, 800);
        $("#showLetter span").html(s);
        $("#showLetter").show().delay(500).hide(0);
    });
    $('body').on('onMouse', '.showLetter span', function () {
        $("#showLetter").show().delay(500).hide(0);
    });
    $("#a_closeChooseNationality").on('click', function () {
        $("#div_login").show();
        $("#div_chooseNationality").hide();
    });
    getNationalityTypeList();
    getVerify();
});

/**
 * 获取证件类型
 */
function getCertificateList() {

    $.ajax({
        url: "/nationality/getCertificateList",
        type: "POST",
        data: {},
        dataType: "json",
        success: function (resp) {
            if (resp.status == 200 && resp.data != null) {
                var data = resp.data;
                var content = '';
                content += '<option value="0">'+layerIdCard+'</option>';
                data.forEach(function (it, i) {
                    content += '<option value="' + it.id + '">' + it.name + '</option>';
                });
                $('#select_certificate').html(content);
            } else {
                layerHelp.msg(resp.msg);
            }
        }, error: function () {
        }
    });
}

/**
 * 登录
 * @param certificate
 * @param inputIdCardNo
 * @param inputPwd
 */
function login(certificate, inputIdCardNo, inputPwd,verifyCode) {
    $.ajax({
        url: "/user/login",
        type: "POST",
        data: {
            certType: certificate,
            verifyCode: verifyCode,
            certNo: BASE64.encode(inputIdCardNo),
            pwd: BASE64.encode(md5(inputPwd))
        },
        dataType: "json",
        beforeSend: function () {
            layerHelp.loading(logining + "...");
        },
        success: function (resp) {
            layer.closeAll();
            if (resp.status == 200) {
                //已填报
                if (resp.data.fillStatus == 1) {
                    window.location.href = "/userPage/userCenter";
                } else {
                    window.location.href = "/report/index";
                }
            } else {
                layerHelp.msg(resp.msg);
            }
        }, error: function () {
            layer.closeAll();
        }, complete: function () {
        }
    });
}

/**
 * 获取国籍、地区列表
 */
function getNationalityTypeList() {
    $.ajax({
        url: "/nationality/getNationalityTypeList",
        type: "POST",
        data: {},
        dataType: "json",
        success: function (resp) {
            if (resp.status == 200 && resp.data != null) {
                var data = resp.data;
                var content = '';
                if (data != null && data.length > 0) {
                    data.forEach(function (it, i) {
                        content += '<div class="city-list"><span class="city-letter" id="' + it.letter + '1">' + it.letter + '</span>\n';
                        if (it.nationalityVoList != null && it.nationalityVoList.length > 0) {
                            it.nationalityVoList.forEach(function (it, i) {
                                content += '<p data-id="' + it.code_3 + '" onclick="clickItem(\'' + it.name + '\')">' + it.name + '</p>\n';
                            });
                        }
                        content += '</div>';
                    });
                }
                $('#div_cityList').html(content);
            } else {
                layerHelp.msg(resp.msg);
            }
        }, error: function () {

        }, complete: function () {
        }
    });
}

/**
 * 点击国籍事件
 * @param name
 */
function clickItem(name) {
    $("#div_login").show();
    $("#div_chooseNationality").hide();
    $("#div_nationality").html(name);
    nationality = name;
}

//获取验证码
function getVerify() {
    // $("#imgCode").on("click", function() {
    $("#img_verify").attr("src", '/user/getVerify?' + Math.random());//jquery方式
    // });
}
