 document.addEventListener('DOMContentLoaded', function () {
        const deliveryDateSelect = document.getElementById('deliveryDate');
        console.log('Delivery Date Select:', deliveryDateSelect);

        const deliveryDates = [
            { daysToAdd: 7, fee: 0, message: 'Delivery in 7 days (No additional fee)' },
            { daysToAdd: 5, fee: 5, message: 'Delivery in 5 days ($5 additional fee)' },
            { daysToAdd: 3, fee: 10, message: 'Delivery in 3 days ($10 additional fee)' }
        ];

        deliveryDates.forEach(function (option) {
            const deliveryDate = new Date();
            deliveryDate.setDate(deliveryDate.getDate() + option.daysToAdd);
            console.log('Computed delivery date:', deliveryDate);

            const optionElem = document.createElement('option');
            optionElem.value = deliveryDate.toISOString().slice(0, 10); // Chỉ lấy phần ngày
            optionElem.textContent = deliveryDate.toLocaleDateString() + ' - ' + option.message;
            console.log('Created option element:', optionElem);

            deliveryDateSelect.appendChild(optionElem);
        });
    });