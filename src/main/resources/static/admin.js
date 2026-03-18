const elements = {
    inventorySearch: document.getElementById("inventorySearch"),
    inventoryList: document.getElementById("inventoryList"),
    inventoryError: document.getElementById("inventoryError"),
    createItemForm: document.getElementById("createItemForm"),
    newItemName: document.getElementById("newItemName"),
    newItemCategory: document.getElementById("newItemCategory"),
    newItemPrice: document.getElementById("newItemPrice"),
    newItemStock: document.getElementById("newItemStock"),
    ordersSearch: document.getElementById("ordersSearch"),
    ordersList: document.getElementById("ordersList"),
    ordersSearch: document.getElementById("ordersSearch"),
}

const state = {
    inventory: [],
    search: "",
    orders: [],
    ordSearch: "",
}

function renderInventory(){
    elements.inventoryList.innerHTML = "";

    const items = state.inventory.filter(function(item){
        const name = item.name.toLowerCase();
        const category = item.category.toLowerCase();
        return name.includes(state.search) || category.includes(state.search);
    });

    items.forEach(function(item){

        const row = document.createElement("div");
        row.classList.add("inventory-row");
        row.dataset.itemId = item.id;

        const title = document.createElement("div");
        title.textContent = item.name;
        title.classList.add("inventory-title");
        row.appendChild(title);

        const cat = document.createElement("div");
        cat.textContent = item.category;
        cat.classList.add("inventory-muted");
        row.appendChild(cat);

        const priceInput = document.createElement("input");
        priceInput.type = "number";
        priceInput.min = "0";
        priceInput.step = "1";
        priceInput.classList.add("mini-input");

        if (item.status === "ON_ORDER"){
            priceInput.placeholder = "Договорная";
            priceInput.disabled = true;
        } else {
            priceInput.value = item.price;
        }

        row.appendChild(priceInput);


        const qtyInput = document.createElement("input");
        qtyInput.type = "number";
        qtyInput.min = "0";
        qtyInput.step = "1";
        qtyInput.classList.add("mini-input");

        if (item.status === "ON_ORDER"){
            qtyInput.placeholder = "-";
            qtyInput.disabled = true;
        } else {
            qtyInput.value = item.stockCount;
        }

        row.appendChild(qtyInput);

        const actions = document.createElement("div");
        actions.classList.add("inventory-actions");


        const saveBtn = document.createElement("button");
        saveBtn.textContent = "Сохранить";
        saveBtn.classList.add("btn", "btn-save");
        saveBtn.disabled = true;

        saveBtn.addEventListener("click", function(){

            updateStock(item.id, Number(qtyInput.value));
            updatePrice(item.id, Number(priceInput.value));

            priceInput.classList.remove("dirty");
            qtyInput.classList.remove("dirty");
            saveBtn.disabled = true;
        });

        actions.appendChild(saveBtn);

        const orderBtn = document.createElement("button");

        if (item.status === "ON_ORDER"){
            orderBtn.textContent = "Снять заказ";
        } else {
            orderBtn.textContent = "На заказ";
        }

        orderBtn.classList.add("btn", "btn-neutral", "btn-order");

        orderBtn.addEventListener("click", function(){

            if (item.status === "ON_ORDER"){
                fetch("/api/catalog/" + item.id + "/setNotOnOrder", { method: "POST" })
                    .then(() => loadInventory());
            } else {
                fetch("/api/catalog/" + item.id + "/setOnOrder", { method: "POST" })
                    .then(() => loadInventory());
            }

        });

        actions.appendChild(orderBtn);

        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Удалить";
        deleteBtn.classList.add("btn", "btn-danger", "btn-delete");

        deleteBtn.addEventListener("click", function(){
            fetch("/api/catalog/" + item.id + "/remove", {method: "POST"})
                .then(() => loadInventory());
        });

        actions.appendChild(deleteBtn);

        const originalPrice = String(item.price);
        const originalQty = String(item.stockCount);

        function updateDirty(){

            const priceChanged = priceInput.value !== originalPrice;
            const qtyChanged = qtyInput.value !== originalQty;

            priceInput.classList.toggle("dirty", priceChanged);
            qtyInput.classList.toggle("dirty", qtyChanged);

            saveBtn.disabled = !(priceChanged || qtyChanged);
        }

        if (item.status !== "ON_ORDER"){
            priceInput.addEventListener("input", updateDirty);
            qtyInput.addEventListener("input", updateDirty);
        }


        row.appendChild(actions);
        elements.inventoryList.appendChild(row);
    });
}

elements.inventorySearch.addEventListener("input", function(){
    state.search = elements.inventorySearch.value.trim().toLowerCase()
    renderInventory()
})

elements.ordersSearch.addEventListener("input", function(){
    state.ordSearch = elements.ordersSearch.value.trim().toLowerCase()
    renderOrders()
})

function updateStock(itemId, newStock){
  if (!Number.isInteger(newStock) || newStock < 0) {
    showInventoryError("Количество должно быть целым числом ≥ 0");
    return;
  }

  fetch("/api/catalog/" + itemId + "/changeStock?newStock=" + newStock, { method: "POST" })
    .then(() => loadInventory())
}

function updatePrice(itemId, newPrice){
  if (!Number.isInteger(newPrice) || newPrice < 0) {
    showInventoryError("Цена должно быть целым числом ≥ 0");
    return;
  }

  fetch("/api/catalog/" + itemId + "/changePrice?newPrice=" + newPrice, { method: "POST" })
    .then(() => loadInventory())
}

function showInventoryError(text){
  elements.inventoryError.textContent = text;
  elements.inventoryError.hidden = false;
  elements.inventoryError.className = "message error";
}

function loadInventory(){
    fetch("/api/catalog", {method:"GET"})
        .then(rawData => rawData.json())
        .then(data => {
            state.inventory = data
            renderInventory()
        })
}

elements.createItemForm.addEventListener("submit", function(){
    fetch("/api/catalog/addItem?name=" + elements.newItemName.value + "&category=" + elements.newItemCategory.value + "&price=" + elements.newItemPrice.value + "&stockCount=" + elements.newItemStock.value, {method: "POST"})
        .then(() => {loadInventory()})
})

function getAllOrders(){
    return fetch("/api/orders/getAllOrders", {method: "GET"})
        .then(rawData => rawData.json())
        .then(data => {
            state.orders = Object.values(data)
        })
}

function renderOrders(){
    elements.ordersList.innerHTML = ""

    searchOrders = state.orders.filter(function(order){
        name = order.client.name.toLowerCase()
        phone = order.client.phone.toLowerCase()
        return name.includes(state.ordSearch) || phone.includes(state.ordSearch)
    })

    searchOrders.forEach(function(order){
        const card = document.createElement("div");
        card.classList.add("order-card");

        const top = document.createElement("div");
        top.classList.add("order-top");

        const id = document.createElement("div");
        id.classList.add("order-id");
        id.textContent = "Заказ #" + order.id;
        top.appendChild(id);

        const client = document.createElement("div");
        client.classList.add("order-client");

        const clientName = document.createElement("div");
        clientName.classList.add("order-client-name");
        clientName.textContent = order.client.name;
        client.appendChild(clientName);

        const clientPhone = document.createElement("div");
        clientPhone.classList.add("order-client-phone");
        clientPhone.textContent = order.client.phone;
        client.appendChild(clientPhone);

        top.appendChild(client);

        const status = document.createElement("div");
        status.classList.add("order-status");

        if (order.status === "CREATED") {
            status.textContent = "Создан";
            status.classList.add("status-created");
        } else if (order.status === "CONFIRMED") {
            status.textContent = "Подтверждён";
            status.classList.add("status-confirmed");
        } else if (order.status === "IN_PROGRESS") {
            status.textContent = "В производстве";
            status.classList.add("status-progress");
        } else if (order.status === "DONE") {
            status.textContent = "Завершён";
            status.classList.add("status-done");
        } else if (order.status === "CANCELLED") {
            status.textContent = "Отменён";
            status.classList.add("status-cancelled");
        }

        top.appendChild(status);
        card.appendChild(top);

        const lines = document.createElement("div");
        lines.classList.add("order-lines");

        order.lines.forEach(function(line){
            const row = document.createElement("div");
            row.classList.add("order-line");

            const name = document.createElement("div");
            name.classList.add("order-line-name");
            if (line.itemStatusSnapshot == "ON_ORDER"){
                name.textContent = line.item.name + " (На заказ)";
            }
            else {
                name.textContent = line.item.name;
            }

            const qty = document.createElement("div");
            qty.classList.add("order-line-qty");
            qty.textContent = "x" + line.quantity;

            row.appendChild(name);
            row.appendChild(qty);
            lines.appendChild(row);
        });

        card.appendChild(lines);

        const bottom = document.createElement("div");
        bottom.classList.add("order-bottom");

        const total = document.createElement("div");
        total.classList.add("order-total");
        total.textContent = "Итого: " + order.total;
        bottom.appendChild(total);

        const actions = document.createElement("div");
        actions.classList.add("order-actions");

        const progressBtn = document.createElement("button");
        progressBtn.textContent = "В производство";
        progressBtn.classList.add("btn");
        progressBtn.disabled = order.status != "CONFIRMED";
        progressBtn.addEventListener("click", function(){
            fetch("/api/orders/" + order.id + "/changeStatus?newStatus=IN_PROGRESS", {method: "POST"})
                .then(() => getAllOrders())
                .then(() => renderOrders());
        });
        actions.appendChild(progressBtn);

        const doneBtn = document.createElement("button");
        doneBtn.textContent = "Завершить";
        doneBtn.classList.add("btn", "btn-success");
        doneBtn.disabled = order.status != "IN_PROGRESS";
        doneBtn.addEventListener("click", function(){
            fetch("/api/orders/" + order.id + "/changeStatus?newStatus=DONE", {method: "POST"})
                .then(() => getAllOrders())
                .then(() => renderOrders());
        });
        actions.appendChild(doneBtn);

        const cancelBtn = document.createElement("button");
        cancelBtn.textContent = "Отменить";
        cancelBtn.classList.add("btn", "btn-danger");
        cancelBtn.disabled = !(order.status == "CONFIRMED" || order.status == "IN_PROGRESS");
        cancelBtn.addEventListener("click", function(){
            fetch("/api/orders/" + order.id + "/changeStatus?newStatus=CANCELLED", {method: "POST"})
                .then(() => getAllOrders())
                .then(() => renderOrders())
                .then(() => loadInventory())
                .then(() => renderInventory());
        });
        actions.appendChild(cancelBtn);

        bottom.appendChild(actions);
        card.appendChild(bottom);

        elements.ordersList.appendChild(card);
    });
}

function startAutoRefresh(){
    setTimeout(async function refresh(){
        await getAllOrders();
        renderOrders();
        setTimeout(refresh, 1000);
    }, 1000);
}

startAutoRefresh()
loadInventory()

