// Kiểm tra và cập nhật giá khi số lượng sản phẩm từ 2 trở lên
    function updateTotal() {
        var quantity = parseInt(document.getElementById("quantity").value);
        var pricePerItem = getPrice(); // Hàm để lấy giá của sản phẩm (cần phải cài đặt)

        // Nếu số lượng sản phẩm lớn hơn 1, cập nhật giá
        if (quantity > 1) {
            var totalPrice = quantity * pricePerItem;
            document.getElementById("totalPrice").value = totalPrice.toFixed(2);
        } else {
            // Nếu số lượng là 1 hoặc ít hơn, không cập nhật giá
            document.getElementById("totalPrice").value = "";
        }
    }

    // Hàm lấy giá của sản phẩm dựa trên lựa chọn
    function getPrice() {
        var product = document.getElementById("product").value;

        // Thay đổi giá của từng sản phẩm tại đây dựa trên product
        switch (product) {
            case "product1":
                return 10.00;
            case "product2":
                return 20.00;
            // Thêm các case khác tương ứng với các sản phẩm khác
            default:
                return 0.00;
        }
    }