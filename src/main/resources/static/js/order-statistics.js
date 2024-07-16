document.addEventListener("DOMContentLoaded", function() {
    fetchOrderData();
});

function fetchOrderData() {
    fetch('/api/orders') // Replace with your endpoint to fetch order data
        .then(response => response.json())
        .then(data => {
            const totalOrders = data.length;
            const totalRevenue = calculateTotalRevenue(data);
            const bestSellingProduct = findBestSellingProduct(data);

            renderTotalOrders(totalOrders);
            renderTotalRevenue(totalRevenue);
            renderBestSellingProduct(bestSellingProduct);
        })
        .catch(error => console.error('Error fetching order data:', error));
}

function calculateTotalRevenue(orders) {
    let totalRevenue = 0;
    orders.forEach(order => {
        order.orderDetails.forEach(detail => {
            totalRevenue += detail.quantity * detail.product.price;
        });
    });
    return totalRevenue;
}

function findBestSellingProduct(orders) {
    const productCount = {};
    orders.forEach(order => {
        order.orderDetails.forEach(detail => {
            const productName = detail.product.name;
            if (productCount[productName]) {
                productCount[productName] += detail.quantity;
            } else {
                productCount[productName] = detail.quantity;
            }
        });
    });

    let bestSellingProduct = null;
    let maxQuantity = 0;
    for (const productName in productCount) {
        if (productCount[productName] > maxQuantity) {
            maxQuantity = productCount[productName];
            bestSellingProduct = productName;
        }
    }

    return bestSellingProduct;
}

function renderTotalOrders(totalOrders) {
    const totalOrdersChart = document.getElementById('totalOrdersChart');
    totalOrdersChart.innerHTML = `<h3>Total Orders: ${totalOrders}</h3>`;
}

function renderTotalRevenue(totalRevenue) {
    const totalRevenueChart = document.getElementById('totalRevenueChart');
    totalRevenueChart.innerHTML = `<h3>Total Revenue: $${totalRevenue.toFixed(2)}</h3>`;
}

function renderBestSellingProduct(bestSellingProduct) {
    const bestSellingProductChart = document.getElementById('bestSellingProductChart');
    bestSellingProductChart.innerHTML = `<h3>Best Selling Product: ${bestSellingProduct}</h3>`;
}
