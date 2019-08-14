$(document).ready(function () {
    $('select option:contains("${limit}")').prop('selected', true);

    $('#upd').on('keyup keypress', function (e) {
        const keyCode = e.keyCode || e.which;
        if (keyCode === 13) {
            e.preventDefault();
            return false;
        }
    });
    calcPriceCommonAll();
});

function chkNaN(val) {
    if (isNaN(val) || val === "" || val === null) val = 0;
    return val;
}

function calcPercent(id) {
    const price = parseFloat($('#price_' + id).text());
    const lastPrice = parseFloat($('#lastPrice_' + id).val());
    const percent = (100 * lastPrice / price - 100).toFixed(1);
    $('#percent_' + id).val(chkNaN(percent));
}

// function increase(id) {
//     $('#percent_' + id).val(chkNaN(parseInt($('#percent_' + id).val())) + 1);
//     calcLastPrice(id);
// }
//
// function decrease(id) {
//     $('#percent_' + id).val(chkNaN(parseInt($('#percent_' + id).val())) - 1);
//     calcLastPrice(id);
// }
//
// function calcLastPrice(id) {
//     var percent = parseFloat($('#percent_' + id).val());
//     var price = parseFloat($('#price_' + id).text());
//     var lastPrice = (price * (1 + percent / 100)).toFixed(2);
//     $('#lastPrice_' + id).val(chkNaN(lastPrice));
// }

function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    const charCode = (evt.which) ? evt.which : evt.keyCode;
    return !((charCode > 31 && charCode < 48) || charCode > 57);
}

function updatingModal() {
    $("#updatingModal").modal();
}

function increaseCommon() {
    $('#percentCommon').val(chkNaN(parseInt($('#percentCommon').val())) + 1);
    calcPriceCommonAll();
}

function decreaseCommon() {
    $('#percentCommon').val(chkNaN(parseInt($('#percentCommon').val())) - 1);
    calcPriceCommonAll();
}

function calcPriceCommonAll() {
    const array = $('.priceCommon');
    const percent = parseFloat($('#percentCommon').val());
    for (let i = 0; i < array.length; i++) {
        //commonId = array[i].getAttribute("id");
        const priceId = array[i].getAttribute("value");
        const common = '#priceCommon_' + priceId;
        const nds = '#priceNoNDS_' + priceId;
        let price = parseFloat($('#price_' + priceId).text());
        price = chkNaN(price * (1 + percent / 100));
        $(common).text(price.toFixed(2));
        $(nds).text((price / 1.2).toFixed(2));
    }
}