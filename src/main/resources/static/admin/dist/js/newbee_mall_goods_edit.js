$(function () {
    const {createEditor, createToolbar} = window.wangEditor

    const editorConfig = {
        MENU_CONF: {},
        placeholder: 'Type here...',
        onChange(editor) {
            const html = editor.getHtml()
            console.log('editor content', html)
            // 也可以同步到 <textarea>
        }
    }
    editorConfig.MENU_CONF['uploadImage'] = {
        server: '/admin/upload/files',
        // 自定义插入图片
        customInsert(res, insertFn) {  // TS 语法
            // customInsert(res, insertFn) {
            // res 即服务端的返回结果
            // 从 res 中找到 url alt href ，然后插图图片
            if (res != null && res.resultCode == 200) {
                // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
                res.data.forEach(img => {
                    insertFn(img)
                });
            } else if (result != null && result.resultCode != 200) {
                alert(result.message);
            } else {
                alert("error");
            }
        },
        // form-data fieldName ，默认值 'wangeditor-uploaded-image'
        fieldName: 'wangeditor-uploaded-image',

        // 单个文件的最大体积限制，默认为 2M
        maxFileSize: 1 * 1024 * 1024, // 1M

        // 最多可上传几个文件，默认为 100
        maxNumberOfFiles: 10,

        // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
        allowedFileTypes: ['image/*'],

        // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
        meta: {
            token: 'xxx',
            otherKey: 'yyy'
        },

        // 将 meta 拼接到 url 参数中，默认 false
        metaWithUrl: false,

        // 自定义增加 http  header
        // headers: {
        //     Accept: 'text/x-json',
        //     otherKey: 'xxx'
        // },

        // 跨域是否传递 cookie ，默认为 false
        withCredentials: true,

        // 超时时间，默认为 10 秒
        timeout: 5 * 1000, // 5 秒
    }

    const editor = createEditor({
        selector: '#editor-container',
        html: html,
        config: editorConfig,
        mode: 'default', // or 'simple'
    })

    const toolbarConfig = {}

    const toolbar = createToolbar({
        editor,
        selector: '#toolbar-container',
        config: toolbarConfig,
        mode: 'default', // or 'simple'
    })

    //图片上传插件初始化 用于商品预览图上传
    new AjaxUpload('#uploadGoodsCoverImg', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.resultCode == 200) {
                $("#goodsCoverImg").attr("src", r.data);
                $("#goodsCoverImg").attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else if (r != null && r.resultCode != 200) {
                alert(r.message);
                return false;
            } else {
                alert("error");
            }
        }
    });


    $('#saveButton').click(function () {
        var goodsId = $('#goodsId').val();
        var goodsCategoryId = $('#levelThree option:selected').val();
        var goodsName = $('#goodsName').val();
        var tag = $('#tag').val();
        var originalPrice = $('#originalPrice').val();
        var sellingPrice = $('#sellingPrice').val();
        var goodsIntro = $('#goodsIntro').val();
        var stockNum = $('#stockNum').val();
        var goodsSellStatus = $("input[name='goodsSellStatus']:checked").val();
        var goodsDetailContent = editor.getHtml();
        var goodsCoverImg = $('#goodsCoverImg')[0].src;
        if (isNull(goodsCategoryId)) {
            swal("请选择分类", {
                icon: "error",
            });
            return;
        }
        if (isNull(goodsName)) {
            swal("请输入商品名称", {
                icon: "error",
            });
            return;
        }
        if (!validLength(goodsName, 100)) {
            swal("请输入商品名称", {
                icon: "error",
            });
            return;
        }
        if (isNull(tag)) {
            swal("请输入商品小标签", {
                icon: "error",
            });
            return;
        }
        if (!validLength(tag, 100)) {
            swal("标签过长", {
                icon: "error",
            });
            return;
        }
        if (isNull(goodsIntro)) {
            swal("请输入商品简介", {
                icon: "error",
            });
            return;
        }
        if (!validLength(goodsIntro, 100)) {
            swal("简介过长", {
                icon: "error",
            });
            return;
        }
        if (isNull(originalPrice) || originalPrice < 1) {
            swal("请输入商品价格", {
                icon: "error",
            });
            return;
        }
        if (isNull(sellingPrice) || sellingPrice < 1) {
            swal("请输入商品售卖价", {
                icon: "error",
            });
            return;
        }
        if (isNull(stockNum) || sellingPrice < 0) {
            swal("请输入商品库存数", {
                icon: "error",
            });
            return;
        }
        if (isNull(goodsSellStatus)) {
            swal("请选择上架状态", {
                icon: "error",
            });
            return;
        }
        if (isNull(goodsDetailContent)) {
            swal("请输入商品介绍", {
                icon: "error",
            });
            return;
        }
        if (!validLength(goodsDetailContent, 50000)) {
            swal("商品介绍内容过长", {
                icon: "error",
            });
            return;
        }
        if (isNull(goodsCoverImg) || goodsCoverImg.indexOf('img-upload') != -1) {
            swal("封面图片不能为空", {
                icon: "error",
            });
            return;
        }
        var url = '/admin/goods/save';
        var swlMessage = '保存成功';
        var data = {
            "goodsName": goodsName,
            "goodsIntro": goodsIntro,
            "goodsCategoryId": goodsCategoryId,
            "tag": tag,
            "originalPrice": originalPrice,
            "sellingPrice": sellingPrice,
            "stockNum": stockNum,
            "goodsDetailContent": goodsDetailContent,
            "goodsCoverImg": goodsCoverImg,
            "goodsCarousel": goodsCoverImg,
            "goodsSellStatus": goodsSellStatus
        };
        if (goodsId > 0) {
            url = '/admin/goods/update';
            swlMessage = '修改成功';
            data = {
                "goodsId": goodsId,
                "goodsName": goodsName,
                "goodsIntro": goodsIntro,
                "goodsCategoryId": goodsCategoryId,
                "tag": tag,
                "originalPrice": originalPrice,
                "sellingPrice": sellingPrice,
                "stockNum": stockNum,
                "goodsDetailContent": goodsDetailContent,
                "goodsCoverImg": goodsCoverImg,
                "goodsCarousel": goodsCoverImg,
                "goodsSellStatus": goodsSellStatus
            };
        }
        console.log(data);
        $.ajax({
            type: 'POST',//方法类型
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.resultCode == 200) {
                    swal({
                        title: swlMessage,
                        type: 'success',
                        showCancelButton: false,
                        confirmButtonColor: '#1baeae',
                        confirmButtonText: '返回商品列表',
                        confirmButtonClass: 'btn btn-success',
                        buttonsStyling: false
                    }).then(function () {
                        window.location.href = "/admin/goods";
                    })
                } else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });

    $('#cancelButton').click(function () {
        window.location.href = "/admin/goods";
    });

    $('#levelOne').on('change', function () {
        $.ajax({
            url: '/admin/categories/listForSelect?categoryId=' + $(this).val(),
            type: 'GET',
            success: function (result) {
                if (result.resultCode == 200) {
                    var levelTwoSelect = '';
                    var secondLevelCategories = result.data.secondLevelCategories;
                    var length2 = secondLevelCategories.length;
                    for (var i = 0; i < length2; i++) {
                        levelTwoSelect += '<option value=\"' + secondLevelCategories[i].categoryId + '\">' + secondLevelCategories[i].categoryName + '</option>';
                    }
                    $('#levelTwo').html(levelTwoSelect);
                    var levelThreeSelect = '';
                    var thirdLevelCategories = result.data.thirdLevelCategories;
                    var length3 = thirdLevelCategories.length;
                    for (var i = 0; i < length3; i++) {
                        levelThreeSelect += '<option value=\"' + thirdLevelCategories[i].categoryId + '\">' + thirdLevelCategories[i].categoryName + '</option>';
                    }
                    $('#levelThree').html(levelThreeSelect);
                } else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });

    $('#levelTwo').on('change', function () {
        $.ajax({
            url: '/admin/categories/listForSelect?categoryId=' + $(this).val(),
            type: 'GET',
            success: function (result) {
                if (result.resultCode == 200) {
                    var levelThreeSelect = '';
                    var thirdLevelCategories = result.data.thirdLevelCategories;
                    var length = thirdLevelCategories.length;
                    for (var i = 0; i < length; i++) {
                        levelThreeSelect += '<option value=\"' + thirdLevelCategories[i].categoryId + '\">' + thirdLevelCategories[i].categoryName + '</option>';
                    }
                    $('#levelThree').html(levelThreeSelect);
                } else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    });

});
