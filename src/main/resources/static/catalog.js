const elements = {
    clientForm: document.getElementById("clientForm"),
    clientName: document.getElementById("clientName"),
    clientPhone: document.getElementById("clientPhone"),
    createOrderBtn: document.getElementById("createOrderBtn"),
    formError: document.getElementById("formError"),

    statusBadge: document.getElementById("statusBadge"),

    confirmBtn: document.getElementById("confirmBtn"),
    cancelBtn: document.getElementById("cancelBtn"),

    orderMeta: document.getElementById("orderMeta"),
    catalogList: document.getElementById("catalogList"),
    catalogSearch: document.getElementById("catalogSearch"),
    cartCount: document.getElementById("cartCount"),
    cartTotal: document.getElementById("cartTotal"),
    cartList: document.getElementById("cartList"),
    cartError: document.getElementById("cartError")
}

const state = {
    currentOrderId: null,
    orderStatus: null,
    catalog: [],
    cart: [],
    search: ""
}

function renderAppState() {
    if (state.currentOrderId == null){
        elements.statusBadge.textContent = "Нет активного заказа"
        elements.clientName.disabled = false
        elements.clientPhone.disabled = false
        elements.createOrderBtn.disabled = false
        elements.confirmBtn.disabled = true
        elements.cancelBtn.disabled = true
        if (elements.orderMeta) {
            elements.orderMeta.textContent = ""
        }
        if (elements.formError) {
            elements.formError.textContent = ""
        }
    }
    else{
        elements.statusBadge.textContent = "Заказ #" + state.currentOrderId + " создан"
        elements.clientName.disabled = true
        elements.clientPhone.disabled = true
        elements.createOrderBtn.disabled = true
        elements.confirmBtn.disabled = false
        elements.cancelBtn.disabled = false
        if (elements.orderMeta) {
            elements.orderMeta.textContent = "Заказ #" + state.currentOrderId
        }
    }
}

function renderCatalog(){
    elements.catalogList.innerHTML = ""

    items = state.catalog.filter(function(item){
        name = item.name.toLowerCase()
        category = item.category.toLowerCase()
        return name.includes(state.search) || category.includes(state.search)
    })

    items.forEach(function(item){
        let itemCard = document.createElement("div")
        itemCard.classList.add("card")

        let title = document.createElement("div")
        title.textContent = item.name
        itemCard.appendChild(title)

        let category = document.createElement("div")
        category.textContent = item.category
        itemCard.appendChild(category)

        let price = document.createElement("div")
        if (item.status == "ON_ORDER"){
            price.textContent = "Договорная"
        }
        else{
            price.textContent = item.price
        }
        itemCard.appendChild(price)

        let stockCount = document.createElement("div")
        if (item.status == "ON_ORDER"){
            stockCount.textContent = "-"
        }
        else{
            stockCount.textContent = item.stockCount
        }
        itemCard.appendChild(stockCount)

        let button = document.createElement("button")
        if (item.status == "ON_ORDER"){
            button.textContent = "На заказ"
        }
        else{
            button.textContent = "Добавить"
        }
        button.classList.add("btn")
        button.disabled = (state.currentOrderId == null || (item.stockCount == 0 && item.status != "ON_ORDER"))
        button.addEventListener("click", function(){
            if (isInCart(item.id)){
                return
            }
            addToCart(item.id)
        })
        itemCard.appendChild(button)

        elements.catalogList.appendChild(itemCard)
    })
}

function loadCatalog(){
    fetch("/api/catalog", {method:"GET"})
        .then(rawData => rawData.json())
        .then(data => {
            state.catalog = data
            renderCatalog()
        })
}

function loadCart(){
    if (state.currentOrderId == null){
        return
    }
    fetch("/api/orders/" + state.currentOrderId, {method: "GET"})
        .then(rawData => rawData.json())
        .then(data => state.cart = data)
}

function isInCart(itemId) {
  if (state.cart == null || state.cart.lines == null) {
    return false;
  }
  return state.cart.lines.some(function(cartLine) {
    return cartLine.item.id === itemId;
  });
}

elements.clientForm.addEventListener("submit", function(e){
    e.preventDefault()

    fetch("/api/orders?name=" + elements.clientName.value + "&phone=" + elements.clientPhone.value.trim().replace(/[^0-9]/g, ""), {method: "POST"})
        .then(rawData => rawData.json())
        .then(order => {
            state.currentOrderId = order.id
            renderAppState()
            loadCatalog()
        })
})

function renderCart(){
    savedScroll = elements.cartList.scrollTop
    elements.cartList.innerHTML = ""
    if (state.currentOrderId == null){
        elements.cartCount.textContent = 0
        elements.cartTotal.textContent = "—"
    }
    else{
        fetch("/api/orders/" + state.currentOrderId, {method: "GET"})
            .then(rawData => rawData.json())
            .then(order => {
                const lines = order.lines

                if (!lines || lines.length == 0){
                    elements.cartCount.textContent = 0
                    elements.cartTotal.textContent = "—"
                    return
                }
                
                let total = 0

                lines.forEach(function(line){
                    let cartCard = document.createElement("div")
                    cartCard.classList.add("cart-row");

                    const title = document.createElement("div")
                    title.textContent = line.item.name
                    title.classList.add("cart-title");
                    cartCard.appendChild(title)
                    
                    const price = document.createElement("div")
                    if (line.item.status == "ON_ORDER"){
                        price.textContent = ""
                    }
                    else{
                        price.textContent = line.item.price * line.quantity
                    }
                    price.classList.add("cart-price");
                    cartCard.appendChild(price)

                    const minusBtn = document.createElement("button")
                    minusBtn.textContent = "-"
                    minusBtn.classList.add("icon-btn")
                    minusBtn.addEventListener("click", function(){
                        if (line.quantity == 1){
                            return
                        }
                        fetch("/api/orders/" + state.currentOrderId + "/changeQuantity?itemId=" + line.item.id + "&count=" + (-1), {method: "POST"})
                            .then(() => {renderCart()})
                    })
                    cartCard.appendChild(minusBtn)

                    const quantity = document.createElement("div")
                    quantity.textContent = line.quantity
                    quantity.classList.add("qty-badge");
                    cartCard.appendChild(quantity)

                    const plusBtn = document.createElement("button")
                    plusBtn.textContent = "+"
                    plusBtn.classList.add("icon-btn")
                    plusBtn.addEventListener("click", function(){
                        if (line.quantity == line.item.stockCount){
                            return
                        }
                        fetch("/api/orders/" + state.currentOrderId + "/changeQuantity?itemId=" + line.item.id + "&count=" + 1, {method: "POST"})
                            .then(() => {renderCart()})
                    })
                    cartCard.appendChild(plusBtn)

                    const unitPriceSnapshot = document.createElement("div")
                    if (line.item.status == "ON_ORDER"){
                        unitPriceSnapshot.textContent = "Договорная"
                    }
                    else{
                        unitPriceSnapshot.textContent = line.unitPriceSnapshot
                    }
                    unitPriceSnapshot.classList.add("cart-price");
                    cartCard.appendChild(unitPriceSnapshot)

                    const deleteBtn = document.createElement("button")
                    deleteBtn.textContent = "🗑"
                    deleteBtn.classList.add("icon-btn", "danger");
                    deleteBtn.addEventListener("click", function(){
                        fetch("/api/orders/" + state.currentOrderId + "/removeLine?itemId=" + line.item.id, { method: "POST" })
                            .then(() => {
                                renderCart()
                                loadCart()
                            });
                    })
                    cartCard.appendChild(deleteBtn)

                    if (line.item.status != "ON_ORDER"){
                        total += line.unitPriceSnapshot * line.quantity
                    }

                    elements.cartList.appendChild(cartCard)
                })

                elements.cartTotal.textContent = total
                elements.cartCount.textContent = String(lines.length)
                elements.cartList.scrollTop = savedScroll
            })
            
    }
}

function addToCart(itemId){
    if (state.currentOrderId == null){
        elements.cartError.textContent = "Сначала создай заказ"
        elements.cartError.hidden = false
        return
    }
    fetch("/api/orders/" + state.currentOrderId +"/addItem?" + "itemId=" + itemId + "&quantity=1", {method: "POST"})
        .then(() => {
            renderCart();
            loadCart();
            loadCatalog();
        })
}

function showCartSuccess(text) {
  elements.cartError.textContent = text;
  elements.cartError.style.color = "green";
  elements.cartError.style.backgroundColor = "#e6f9ed";
  elements.cartError.style.border = "1px solid #2ecc71";
  elements.cartError.style.display = "block";
}

function showCartFailed(text) {
  elements.cartError.textContent = text;
  elements.cartError.style.color = "red";
  elements.cartError.style.display = "block";
}

function confirmOrder(){
    if (state.cart.lines.length == 0){
        return
    }
    fetch("/api/orders/" + state.currentOrderId + "/changeStatus?newStatus=CONFIRMED", { method: "POST" })
        .then(() => {
            showCartSuccess("Заказ подтверждён. Обновляю страницу…");
            setTimeout(() => location.reload(), 2000);
        })
}

function cancelOrder(){
    if (state.cart.lines.length == 0){
        return
    }
    fetch("/api/orders/" + state.currentOrderId + "/changeStatus?newStatus=CANCELLED", { method: "POST" })
        .then(() => {
            showCartFailed("Заказ отменён. Обновляю страницу…");
            setTimeout(() => location.reload(), 2000);
        })
}

elements.confirmBtn.addEventListener("click", confirmOrder)
elements.cancelBtn.addEventListener("click", cancelOrder)

elements.catalogSearch.addEventListener("input", function(){
    state.search = elements.catalogSearch.value.trim().toLowerCase()
    loadCatalog()
})

function startAutoRefresh(){
    setTimeout(async function refresh(){
        await loadCatalog();
        setTimeout(refresh, 1000);
    }, 1000);
}

renderAppState()
startAutoRefresh()