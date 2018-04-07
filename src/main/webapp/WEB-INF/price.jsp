<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<script>
    <c:if test="${updated}">
    $(document).ready(function () {
        $("#updatedModal").modal();
    })
    </c:if>
</script>
<!-- updatedModal -->
<div class="modal fade" id="updatedModal" tabindex="-1" role="dialog" aria-labelledby="updatedModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updatedModalLabel">Прайс обновился</h5>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- updatingModal -->
<div class="modal fade" id="updatingModal" tabindex="-1" role="dialog" aria-labelledby="updatingModal"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updatingModalLabel">Обновляется прайс</h5>
            </div>
            <div class="modal-body">
                <div class="progress">
                    <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                         aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm">
            <form action="<c:url value='/price'/>" method="get">
                <div class="input-group mb-3">
                    <input type="text" name="product" value="${product}" class="form-control form-control-sm"
                           placeholder="Продукт">
                    <div class="input-group-append">
                        <input type="submit" value="поиск" class="btn btn-primary btn-sm">
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm" align=right>
            <form action="<c:url value='/update'/>" method="post" class="form">
                <!-- Button trigger modal -->
                <button type="button" class="btn btn-dark btn-sm" data-toggle="modal" data-target="#updateModal">
                    Обновить - ${dateUpdated}
                </button>
                <!-- Modal -->
                <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateModalLabel">Обновление прайса</h5>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                                <button type="submit" class="btn btn-primary" data-toggle="modal"
                                        data-target="#updateModal" onclick=updatingModal()>Обновить
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<c:if test="${product != null}">
    <form id="upd" action="<c:url value='/save'/>" method="post">
        <div class="container-fluid">
            <table class="table table-bordered table-sm table-striped">
                <thead>
                <tr>
                    <th>Артикул</th>
                    <th>Продукт</th>
                    <th class="text-secondary">Цена</th>
                    <th class="text-info">-23%</th>
                    <th class="text-success" style="width: 120px">
                        <div class="form-row">
                            <div class="col">
                                <input type="text" value="15" class="form-control form-control-sm" id="percentCommon"
                                       onkeyup="calcPriceCommonAll()" placeholder="%"
                                       onkeypress="return isNumber(event)" maxlength="3">
                            </div>
                            <div class="col">
                                <div class="btn-group btn-group-sm" role="group">
                                    <button type="button" class="btn btn-primary" onclick="increaseCommon()">+</button>
                                    <button type="button" class="btn btn-primary" onclick="decreaseCommon()">-</button>
                                </div>
                            </div>
                        </div>
                    </th>
                    <th class="text-primary">Итоговая
                        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal"
                                data-target="#updateProductModal">
                            Сохранить
                        </button>
                    </th>
                </tr>
                </thead>
                <!-- Modal -->
                <div class="modal fade" id="updateProductModal" tabindex="-1" role="dialog"
                     aria-labelledby="updateProductLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="updateProductLabel">Сохранить цены</h5>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                                <button type="submit" class="btn btn-primary" data-target="#updateProductModal">
                                    Сохранить
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <tbody>
                <c:forEach items="${pageContext.request.getAttribute('prices')}" var="price">
                    <tr>
                        <td class="align-middle" style="width: 140px" align="center">
                            <a href="${urlSearch}=${price.item}" target="_blank">
                                    ${price.item}
                            </a>
                        </td>
                        <td class="align-middle" align="left">
                                ${price.product}
                        </td>
                        <td style="width: 50px" align="right" class="text-secondary align-middle">
                            <label>${price.recommendedPrice}</label>
                        </td>
                        <td style="width: 50px" align="right" class="text-info align-middle">
                            <label id="price_${price.idPrice}">${price.ourPrice}</label>
                        </td>
                        <td align="right">
                            <label id="priceCommon_${price.idPrice}" class="priceCommon text-success"
                                   value="${price.idPrice}"></label>
                            <small id="priceNoNDS_${price.idPrice}"></small>
                        </td>
                        <input type="hidden" value="${price.idProd}" name="prod_${price.idPrice}">
                        <td style="width: 245px">
                            <div class="form-row">
                                <div class="col-sm-5">
                                    <input type="text" id="lastPrice_${price.idPrice}" value="${price.lastPrice}"
                                           onkeypress="return isNumber(event)" onkeyup="calcPercent(${price.idPrice})"
                                           class="form-control form-control-sm" name="lastPrice_${price.idPrice}"
                                           maxlength="6">
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" value="${price.percent}" class="form-control form-control-sm"
                                           id="percent_${price.idPrice}"
                                           placeholder="%" name="percent_${price.idPrice}" maxlength="4"
                                           onkeypress="return isNumber(event)"
                                           onkeyup="calcLastPrice(${price.idPrice})">
                                </div>
                                <div class="col">
                                    <div class="btn-group btn-group-sm" role="group">
                                        <button type="button" class="btn btn-primary"
                                                onclick="increase(${price.idPrice})">
                                            +
                                        </button>
                                        <button type="button" class="btn btn-primary"
                                                onclick="decrease(${price.idPrice})">
                                            -
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </form>
</c:if>
<%@ include file="footer.jsp" %>