layui.use(['form','jquery','jquery_cookie','table','formSelects'], function () {
    var form = layui.form,
        layer = layui.layer,
        table=layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($),
    // 引⼊ formSelects 模块
    formSelects = layui.formSelects;


    /**
     * 触发表单
     */
    //submit(addOrUpdateUser)
    form.on("submit(addOrUpdateUser)",function (obj){
       var dataField= obj.field;
      // console.log(obj.field+"<<<<");
        /**
         * 发送ajax
         */
            // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
                icon:16, // 图标
                time:false, // 不关闭
                shade:0.8 // 设置遮罩的透明度
            });
        var url=ctx+"/user/save";
        //获取隐藏域信息，决定添加或者是修改
        if( $("input[name=id]").val()){
            url=ctx+"/user/update";
        }
        //
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function(data){
                if(data.code == 200){
                    //添加成功了
                    layer.msg("添加成功了",{icon:6});
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe;
                    layer.closeAll("iframe");
                    //刷新父目录
                    parent.location.reload();
                }else{
                    //添加失败
                    layer.msg(data.msg,{icon: 5 });
                }
            }
        });
        //阻止表单提交
        return false;
    });

    /**
     * 取消
     */

    $("#closeBtn").click(function(){
        //假设这是iframe页
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /**
     * 加载下拉框数据
     */
    var userId=$("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId="+userId,
        //⾃定义返回数据中name的key, 默认 name
        keyName: 'roleName',
        // ⾃定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);

});