layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 提交表单
     *
     *   //监听提交
     form.on('submit(formDemo)', function(data){
    layer.msg(JSON.stringify(data.field));
    return false;
  });
     */
    form.on("submit(saveBtn)",function(obj){
        var dataField= obj.field;

        //验证原密码不能为空
        if(dataField.old_password == "undefined" || dataField.old_password.trim()==''){
            layer.msg("原密码不能为空");
            return false;
        }
        //验证新密码不能为空
        if(dataField.new_password == "undefined" || dataField.new_password.trim()==''){
            layer.msg("新密码不能为空");
            return  false;
        }
        //验证确认密码不能为空
        if(dataField.again_password == "undefined" || dataField.again_password.trim()==''){
            layer.msg("确认密码不能为空");
            return  false;
        }
        //发送
        $.ajax({
            type:"post",
            url:ctx+"/user/confirm",
            data:{
                oldPwd:dataField.old_password,
                newPwd:dataField.new_password,
                confirmPwd:dataField.again_password
            },
            dataType:"json",
            success:function(data){
                if(data.code == 200){
                    layer.msg("修改成功了，三秒后消失",function(){
                        //清空cookie
                        // 退出系统后，删除对应的cookie
                        $.removeCookie("userIdStr", {domain:"localhost",path:"/mycrm"});
                        $.removeCookie("userName", {domain:"localhost",path:"/mycrm"});
                        $.removeCookie("trueName", {domain:"localhost",path:"/mycrm"});
                        //跳转登录页面
                        window.parent.location.href=ctx+"/index"
                    });
                }else{
                    //修改失败了
                    layer.msg(data.msg);
                }
            }

        });
    });
});