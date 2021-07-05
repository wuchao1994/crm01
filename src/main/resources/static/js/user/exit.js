layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    $(".login-out").click(function () {
        // 退出系统后，删除对应的cookie
        $.removeCookie("userIdStr", {domain: "localhost", path: "/mycrm"});
        $.removeCookie("userName", {domain: "localhost", path: "/mycrm"});
        $.removeCookie("trueName", {domain: "localhost", path: "/mycrm"});
        //跳转登录页面
        window.parent.location.href = ctx + "/index"
    });
});
