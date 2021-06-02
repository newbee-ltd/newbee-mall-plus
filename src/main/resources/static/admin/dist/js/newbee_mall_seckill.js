$(function () {
    $("#jqGrid").jqGrid({
        url: _ctx + 'admin/seckill/list',
        datatype: "json",
        viewrecords: true,
        colModel: [
            {label: '序号', name: 'seckillId', index: 'seckillId', key: true, width: '40px'},
            {label: '秒杀商品id', name: 'goodsId', index: 'goodsId', width: '70px'},
            {label: '秒杀价格', name: 'seckillPrice', index: 'seckillPrice', width: '70px'},
            {label: '数量', name: 'seckillNum', index: 'seckillNum', width: '40px'},
            {label: '状态', name: 'seckillStatus', index: 'seckillStatus', formatter: statusFormatter, width: '80px'},
            {label: '开始时间', name: 'seckillBegin', index: 'seckillBegin', width: '110px'},
            {label: '结束时间', name: 'seckillEnd', index: 'seckillEnd', width: '110px'},
            {label: '创建时间', name: 'createTime', index: 'createTime', width: '110px'}
        ],
        height: 460,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type='button' class='btn btn-block btn-danger btn-sm' style='width: 80%;'>已下架</button>";
        } else {
            return "<button type='button' class='btn btn-block btn-success btn-sm' style='width: 80%;'>上架中</button>";
        }
    }

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    $('#createTime').daterangepicker({
        autoUpdateInput: false,
        showDropdowns: true,
        startDate: moment().startOf('hour'),
        endDate: moment().startOf('hour').add(12, 'hour'),
        locale: datepickerLocale()
    });

    $('#createTime').on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY/MM/DD') + ' - ' + picker.endDate.format('YYYY/MM/DD'));
    });

    $('#seckillBegin, #seckillEnd').daterangepicker({
        singleDatePicker: true,
        autoUpdateInput: false,
        showDropdowns: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerSeconds: true,
        startDate: moment().hours(0).minutes(0).seconds(0), //设置开始日期
        locale: datepickerLocale()
    });

    $('#seckillBegin, #seckillEnd').on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
    });
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page,
    }).trigger("reloadGrid");
}

function searchSecKillGoods() {
    var goodsId = $('#queryGoodsId').val() || '';
    var createTime = $('#createTime').val() || '';
    var timeArr = createTime && createTime.split('-') || ['', ''];
    var startTime = timeArr[0].trim();
    var endTime = timeArr[1].trim();
    // 数据封装
    var searchData = {
        goodsId: goodsId,
        startTime: startTime,
        endTime: endTime
    };
    // 传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    // 点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    // 提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: 'seckill/list'}).trigger("reloadGrid");
}

function seckillAdd() {
    this.reset();
    $('[name="seckillStatus"][value="0"]').attr("checked", true).parent().addClass("active");
    $('.modal-title').html('秒杀商品添加');
    $('#seckillModal').modal('show');
}

function seckillEdit() {
    reset();
    $('.modal-title').html('秒杀商品修改');
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var url = _ctx + 'admin/seckill/' + id;
    $.get(url, function (res) {
        if (res.resultCode != 200) {
            swal("操作失败", {
                icon: "error",
            });
            return
        }
        var seckillInfo = res.data
        $('#seckillId').val(seckillInfo.seckillId);
        $('#goodsId').val(seckillInfo.goodsId);
        $('#seckillPrice').val(seckillInfo.seckillPrice);
        $('#seckillNum').val(seckillInfo.seckillNum);
        $('#seckillBegin').val(seckillInfo.seckillBegin);
        $('#seckillEnd').val(seckillInfo.seckillEnd);
        $('#seckillRank').val(seckillInfo.seckillRank);
        var seckillStatusValue = seckillInfo.seckillStatus ? 1 : 0;
        $('[name="seckillStatus"][value="' + seckillStatusValue + '"]').attr("checked", true).parent().addClass("active");
    }, 'json')
    $('#seckillModal').modal('show');
}

function seckillDelete() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            var url = _ctx + 'admin/seckill/' + id;
            $.ajax({
                url: url,
                cache: false,
                type: 'delete',
                dataType: 'json',
                success: function (result) {
                    if (result.resultCode == 200) {
                        swal("删除成功", {
                            icon: "success",
                        });
                        reload();
                    } else {
                        swal(result.message, {
                            icon: "error",
                        });
                    }
                },
                error: function () {
                    swal("操作失败", {
                        icon: "error",
                    });
                }
            });
        }
    });

}

// 绑定modal上的保存按钮
$('#saveButton').click(function () {
    var formObj = $('#seckillForm').serializeArray();
    var form = {};
    $.each(formObj, function (index, item) {
        form[item.name] = item.value;
    });
    if (isNull(form.goodsId)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入商品ID！");
        return;
    }
    if (isNull(form.seckillPrice)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入秒杀价格！");
        return;
    }
    if (isNull(form.seckillNum)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入秒杀数量！");
        return;
    }
    if (isNull(form.seckillBegin)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("秒杀开始时间！");
        return;
    }
    if (isNull(form.seckillEnd)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("秒杀结束时间！");
        return;
    }
    form.seckillStatus = parseInt(form.seckillStatus);
    var url = _ctx + 'admin/seckill/save';
    if (!isNull(form.seckillId)) {
        url = _ctx + 'admin/seckill/update';
    }
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        contentType: 'application/json',
        data: JSON.stringify(form),
        success: function (result) {
            if (result.resultCode == 200) {
                $('#seckillModal').modal('hide');
                swal("保存成功", {
                    icon: "success",
                });
                reload();
            } else {
                swal(result.message, {
                    icon: "error",
                });
            }
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

function reset() {
    $('#edit-error-msg').css("display", "none");
    $('#seckillForm')[0].reset();
    $('#seckillId').val('');
    $('[name="seckillStatus"]').removeAttr("checked").parent().removeClass("active")
}
