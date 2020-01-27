function login() {

    var ename = $("#ename").val();
    var password = $("#password").val();
    var remember = $("input[name='remember']:checked").val();
    if (ename === undefined || ename === '') {
        toastr.error("请输入用户名");
        return;
    }
    if (password === undefined || password === '') {
        toastr.error("请输入密码");
        return;
    }

    var params = JSON.stringify({
        "ename": ename,
        "password": hex_md5(password),
        "remember": remember
    });
    $.ajax({
        cache: false,
        type: "POST",
        contentType: 'application/json',
        url: "/member/rest/login",
        data: params,
        async: false,
        success: function (data) {
            if (data.code === 0) {
                toastr.success(data.message);
                //window.location.href="/";
                window.location.reload()
            } else {
                toastr.error(data.message);
            }
        },
        error: function (data) {
            toastr.error(data.message);
        }
    });
}

function hideReg() {
    $('#register').modal('hide');
}

function hideLogin() {
    $('#login').modal('hide');
}

function logout() {
    $.ajax({
        cache: false,
        type: "POST",
        contentType: 'application/json',
        url: "/member/rest/logout",
        async: false,
        success: function (data) {
            if (data.code === 0) {
                //toastr.success(data.message);
                //window.location.href = "/";
                window.location.reload()

            } else {
                //toastr.error(data.message);
                setTimeout(function () {
                    window.location.href = "/";
                }, 3000);

            }
        },
        error: function (data) {
            //toastr.error(data.message);
        }
    });
}


function checkMail(v) {
    var reg = /^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/; //正则表达式
    if (!reg.test(v)) { //正则验证不通过，格式不对
        return false;
    } else {
        return true;
    }
}

function reg() {

    var regEname = $("#regEname").val();
    var regCname = $("#regCname").val();
    var regEmail = $("#regEmail").val();
    var regPassword = $("#regPassword").val();
    var regVerCode = $("#regVerCode").val();

    if (regEname === undefined || regEname === '') {
        toastr.error("请输入用户名");
        return;
    }

    if (regCname === undefined || regCname === '') {
        toastr.error("请输入昵称");
        return;
    }

    if (regEmail === undefined || regEmail === '') {
        toastr.error("请输入邮箱");
        return;
    }
    if (!checkMail(regEmail)) {
        toastr.error("邮箱格式有误");
        return;
    }


    if (regPassword === undefined || regPassword === '') {
        toastr.error("请输入密码");
        return;
    }

    if (regVerCode === undefined || regVerCode === '') {
        toastr.error("请输入验证码");
        return;
    }

    var params = JSON.stringify({
        "ename": regEname,
        "cname": regCname,
        "email": regEmail,
        "password": hex_md5(regPassword),
        "regVerCode": regVerCode
    });


    $.ajax({
        cache: false,
        type: "POST",
        contentType: 'application/json',
        url: "/member/rest/reg",
        data: params,
        async: false,
        success: function (data) {
            if (data.code === 0) {
                toastr.success(data.message);
                //window.location.href="/";
                window.location.reload()
            } else {
                toastr.error(data.message);
                $("#regVerCode").val('');
                var regCaptcha = document.getElementById("regCaptcha");
                regCaptcha.src = '/member/rest/reg/image?m=' + Math.floor(Math.random() * 10000);
            }
        },
        error: function (data) {
            toastr.error(data.message);
        }
    });
}