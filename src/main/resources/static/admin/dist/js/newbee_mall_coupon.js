$(function () {
    $("#jqGrid").jqGrid({
        url: _ctx + 'admin/coupon/list',
        datatype: "json",
        viewrecords: true,
        colModel: [
            {label: 'id', name: 'couponId', index: 'couponId', key: true, hidden: true},
            {label: '优惠卷名称', name: 'couponName', index: 'couponName'},
            {label: '数量', name: 'couponTotal', index: 'couponTotal', width: '80px', formatter: totalFormatter},
            {label: '优惠金额', name: 'discount', index: 'discount', width: '90px'},
            {label: '最低消费', name: 'min', index: 'min', width: '100px'},
            {label: '领取限制', name: 'couponLimit', index: 'couponLimit', width: '100px', formatter: limitFormatter},
            {label: '开始时间', name: 'couponStartTime', index: 'days', width: '110px'},
            {label: '结束时间', name: 'couponEndTime', index: 'days', width: '110px'},
            {label: '类型', name: 'couponType', index: 'couponType', width: '100px', formatter: typeFormatter},
            {label: '状态', name: 'couponStatus', index: 'couponStatus', formatter: statusFormatter},
            {label: '创建时间', name: 'createTime', index: 'createTime'}
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

    function totalFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "无限制";
        }
        return cellvalue;
    }

    function typeFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "通用卷";
        } else if (cellvalue == 1) {
            return "注册用卷";
        } else {
            return "优惠码兑换卷";
        }
    }

    function limitFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "不限制";
        } else if (cellvalue == 1) {
            return "限领一张";
        }
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type='button' class='btn btn-block btn-success btn-sm' style='width: 80%;'>可用</button>";
        } else if (cellvalue == 1) {
            return "<button type='button' class='btn btn-block btn-warning btn-sm' style='width: 80%;'>已过期</button>";
        } else {
            return "<button type='button' class='btn btn-block btn-danger btn-sm' style='width: 80%;'>已下架</button>";
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

    $('#couponStartTime, #couponEndTime').daterangepicker({
        singleDatePicker: true,
        autoUpdateInput: false,
        showDropdowns: true,
        timePicker: false, // false 不开启时间选择
        timePicker24Hour: false,
        timePickerSeconds: false,
        startDate: moment(), //设置开始日期
        locale: datepickerLocale()
    });

    $('#couponStartTime, #couponEndTime').on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
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

function searchCoupon() {
    var couponName = $('#name').val() || '';
    var couponType = $('#couponType').val() || '';
    var couponStatus = $('#couponStatus').val() || '';
    var createTime = $('#createTime').val() || '';
    var timeArr = createTime && createTime.split('-') || ['', ''];
    var startTime = timeArr[0].trim();
    var endTime = timeArr[1].trim();
    // 数据封装
    var searchData = {
        couponName: couponName,
        couponType: couponType,
        couponStatus: couponStatus,
        startTime: startTime,
        endTime: endTime
    };
    // 传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    // 点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    // 提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: 'coupon/list'}).trigger("reloadGrid");
}

function couponAdd() {
    reset();
    $('[name="couponLimit"]:eq(0)').attr("checked", true).parent().addClass("active");
    $('[name="couponType"]:eq(0)').attr("checked", true).parent().addClass("active");
    $('[name="couponStatus"]:eq(0)').attr("checked", true).parent().addClass("active");
    $('[name="goodsType"]:eq(0)').attr("checked", true).parent().addClass("active");
    $('.modal-title').html('优惠卷添加');
    $('#couponModal').modal('show');
}

function couponEdit() {
    reset();
    $('.modal-title').html('优惠卷修改');
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var url = _ctx + 'admin/coupon/' + id;
    $.get(url, function (res) {
        if (res.resultCode != 200) {
            swal("操作失败", {
                icon: "error",
            });
            return
        }
        var couponInfo = res.data
        $('#couponId').val(couponInfo.couponId);
        $('#couponName').val(couponInfo.couponName);
        $('#couponTotal').val(couponInfo.couponTotal);
        $('#discount').val(couponInfo.discount);
        $('#min').val(couponInfo.min);
        $('#couponStartTime').val(couponInfo.couponStartTime);
        $('#couponEndTime').val(couponInfo.couponEndTime);
        $('#couponDesc').val(couponInfo.couponDesc);
        $('[name="couponLimit"][value="' + couponInfo.couponLimit + '"]').attr("checked", true).parent().addClass("active");
        $('[name="couponType"][value="' + couponInfo.couponType + '"]').attr("checked", true).parent().addClass("active");
        $('[name="couponStatus"][value="' + couponInfo.couponStatus + '"]').attr("checked", true).parent().addClass("active");
        $('[name="goodsType"][value="' + couponInfo.goodsType + '"]').attr("checked", true).parent().addClass("active");
        if (couponInfo.goodsType == 1 || couponInfo.goodsType == 2) {
            $('#goodsTypeShow').show();
            $('#goodsValue').val(couponInfo.goodsValue);
        }
    }, 'json')
    $('#couponModal').modal('show');
}

function couponDelete() {
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
            var url = _ctx + 'admin/coupon/' + id;
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
    var formObj = $('#couponForm').serializeArray();
    var form = {};
    $.each(formObj, function (index, item) {
        form[item.name] = item.value;
    });
    if (isNull(form.couponName)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入优惠卷名称！");
        return;
    }
    if (isNull(form.couponTotal) || form.couponTotal < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入优惠卷数量且不能小于0！");
        return;
    }
    if (isNull(form.discount) || form.discount < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入优惠金额且不能小于0！");
        return;
    }
    if (isNull(form.min) || form.min < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入最少消费金额且不能小于0！");
        return;
    }
    if (isNull(form.couponStartTime) || form.couponStartTime < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入优惠卷开始时间！");
        return;
    }
    if (isNull(form.couponEndTime) || form.couponEndTime < 0) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入优惠卷结束时间！");
        return;
    }
    if (form.goodsType != 0 && isNull(form.goodsValue)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入" + this.goodsValueLabel + "！");
        return;
    }
    form.couponLimit = parseInt(form.couponLimit);
    form.couponType = parseInt(form.couponType);
    form.couponStatus = parseInt(form.couponStatus);
    form.goodsType = parseInt(form.goodsType);
    var url = _ctx + 'admin/coupon/save';
    if (!isNull(form.couponId)) {
        url = _ctx + 'admin/coupon/update';
    }
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        contentType: 'application/json',
        data: JSON.stringify(form),
        success: function (result) {
            if (result.resultCode == 200) {
                $('#couponModal').modal('hide');
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

function changeGoodsType(goodsType) {
    $('#goodsTypeShow').hide();
    $('#goodsValue').val('');
    if (goodsType == 1) {
        $('#goodsTypeShow').show();
        $('#goodsValueLabel').text('类目id');
    } else if (goodsType == 2) {
        $('#goodsTypeShow').show();
        $('#goodsValueLabel').text('商品id');
    }
}

function reset() {
    $('#edit-error-msg').css("display", "none");
    $('#goodsTypeShow').hide();
    $('#couponForm')[0].reset();
    $('[name="couponLimit"]').removeAttr("checked").parent().removeClass("active")
    $('[name="couponType"]').removeAttr("checked").parent().removeClass("active")
    $('[name="couponStatus"]').removeAttr("checked").parent().removeClass("active")
    $('[name="goodsType"]').removeAttr("checked").parent().removeClass("active")
}

/*var vm = new Vue({
    el: '#app',
    data: {
        title: '',
        goodsValueLabel: '',
        form: {
            couponName: '',
            couponDesc: '',
            couponTotal: undefined,
            discount: undefined,
            min: undefined,
            couponStartTime: undefined,
            couponEndTime: undefined,
            couponLimit: 0,
            couponType: 0,
            couponStatus: 0,
            goodsType: 0,
            goodsValue: ''
        }
    },
    methods: {
        couponAdd() {
            this.reset();
            this.title = '优惠卷添加';
            $('#couponModal').modal('show');
        },
        couponEdit() {
            this.reset();
            this.title = '优惠卷编辑';
            var that = this;
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            var url = _ctx + 'admin/coupon/' + id;
            $.get(url, function (res) {
                if (res.resultCode != 200) {
                    swal("操作失败", {
                        icon: "error",
                    });
                    return
                }
                that.form = res.data
                if (that.form.goodsType == 1) {
                    that.goodsValueLabel = '类目id';
                } else if (that.form.goodsType == 2) {
                    that.goodsValueLabel = '商品id'
                }
            }, 'json')
            $('#couponModal').modal('show');
        },
        changeGoodsType(goodsType) {
            this.form.goodsValue = '';
            if (goodsType == 1) {
                this.goodsValueLabel = '类目id'
            } else if (goodsType == 2) {
                this.goodsValueLabel = '商品id'
            }
        },
        reset() {
            $('#edit-error-msg').css("display", "none");
            vm.form = {
                couponName: '',
                couponDesc: '',
                couponTotal: undefined,
                discount: undefined,
                min: undefined,
                couponStartTime: undefined,
                couponEndTime: undefined,
                couponLimit: 0,
                couponType: 0,
                couponStatus: 0,
                goodsType: 0,
                goodsValue: ''
            }
        },
        save() {
            var form = this.form
            if (isNull(form.couponName)) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入优惠卷名称！");
                return;
            }
            if (isNull(form.couponTotal) || form.couponTotal < 0) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入优惠卷数量且不能小于0！");
                return;
            }
            if (isNull(form.discount) || form.discount < 0) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入优惠金额且不能小于0！");
                return;
            }
            if (isNull(form.min) || form.min < 0) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入最少消费金额且不能小于0！");
                return;
            }
            if (isNull(form.couponStartTime) || form.couponStartTime < 0) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入优惠卷开始时间！");
                return;
            }
            if (isNull(form.couponEndTime) || form.couponEndTime < 0) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入优惠卷结束时间！");
                return;
            }
            if (form.goodsType != 0 && isNull(form.goodsValue)) {
                $('#edit-error-msg').css("display", "block");
                $('#edit-error-msg').html("请输入" + this.goodsValueLabel + "！");
                return;
            }
            var url = _ctx + 'admin/coupon/save';
            var data = this.form;
            if (form.couponId != null) {
                url = _ctx + 'admin/coupon/update';
            }
            $.ajax({
                type: 'POST',//方法类型
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function (result) {
                    if (result.resultCode == 200) {
                        $('#couponModal').modal('hide');
                        swal("保存成功", {
                            icon: "success",
                        });
                        reload();
                    } else {
                        // $('#couponModal').modal('hide');
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
        },
        deleteCoupon() {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            var url = _ctx + 'admin/coupon/' + id;
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
    }
})*/
