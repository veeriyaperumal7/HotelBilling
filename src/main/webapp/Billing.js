var billNo=null;

document.addEventListener("DOMContentLoaded", function () {
	billNo=document.getElementById('BillNumberValue').innerText;
	var currentDate = new Date();
    var formattedDate = currentDate.toISOString().split("T")[0]; // Format: YYYY-MM-DD
    document.getElementById("BillDateValue").textContent = formattedDate;
	var dishItems = document.querySelectorAll('.dishItem');
    var billingTable = document.querySelector('.tableBody');

    dishItems.forEach(function (item) {
        item.addEventListener('click', function () {
            var itemName = item.getAttribute('data-food');
            var itemPrice = parseFloat(item.getAttribute('data-price'));
            var dishId = item.getAttribute('data-id'); // Retrieve dishId from the data attribute
            addItemToBillingTable(itemName, itemPrice, dishId);
        });
    });

	function addItemToBillingTable(itemName, itemPrice,dishId) {
		var existingRow = findExistingRow(itemName);
		var existingRow = null;
		if (existingRow == null) {
			// Create a new row in the billing table
			var newRow = billingTable.insertRow(-1);
			var cell1 = newRow.insertCell(0);
			var cell2 = newRow.insertCell(1);
			var cell3 = newRow.insertCell(2);
			var cell4 = newRow.insertCell(3);
			newRow.dataset.id = dishId; // Set dish id.

			var priceText = document.createElement('h5');
			priceText.className = 'BillingTablePriceText'

			// Populate cells with data
			cell1.innerHTML = billingTable.rows.length;
			cell2.innerHTML = itemName;

			// Create a div inside cell3 for additional data
			var cell3Div = document.createElement('div');
			cell3Div.className = 'Cell3Data';
			cell3.appendChild(cell3Div);

			// Create an input field for quantity
			var quantityInput = document.createElement('input');
			quantityInput.className = 'QuantityInputBox';
			quantityInput.type = 'text';
			quantityInput.value = 1; // Default quantity is 1

			quantityInput.readOnly = true; // Make the input read-only
			cell3Div.appendChild(quantityInput);

			// Create buttons for increasing and decreasing quantity
			var increaseButton = document.createElement('button');
			var decreaseButton = document.createElement('button');
			decreaseButton.disabled = true;

			increaseButton.innerHTML = '+';
			increaseButton.className = 'QuantityIncreaseButton';
			increaseButton.addEventListener('click', function () {
				var currentQuantity = parseInt(quantityInput.value, 10);
				changeButtonStatus(increaseButton, decreaseButton, currentQuantity+1);
				quantityInput.value = currentQuantity + 1;
				updateTotal(priceText, itemPrice, currentQuantity + 1);
				updateFinalTotal();
			}
			);


			decreaseButton.innerHTML = '-';
			decreaseButton.className = 'QuantityDecreaseButton';
			decreaseButton.addEventListener('click', function () {
				var currentQuantity = parseInt(quantityInput.value, 10);
				changeButtonStatus(increaseButton, decreaseButton, currentQuantity-1);
				quantityInput.value = currentQuantity - 1;
				updateTotal(priceText, itemPrice, currentQuantity - 1);
				updateFinalTotal();
			});

			var removeButton = document.createElement('button');
			removeButton.innerHTML = 'X';
			removeButton.className = 'RemoveItemButton';
			removeButton.addEventListener('click', function () {
				removeItemFromBillingTable(newRow);
			});

			cell3Div.append(decreaseButton, quantityInput, increaseButton);
			// Display price with 2 decimal places
			var cell4Div = document.createElement('div');
			cell4Div.className = 'Cell4Data';
			updateTotal(priceText, itemPrice, 1);

			cell4Div.append(priceText, removeButton);
			cell4.appendChild(cell4Div);

		}
		updateFinalTotal();
	}

	function changeButtonStatus(increaseButton, decreaseButton, currentQuantity) {
		console.log(currentQuantity)
		if (currentQuantity > 1 && currentQuantity < 1000) {
			decreaseButton.disabled = false; 
			increaseButton.disabled = false;
		} else {
			if (currentQuantity > 1) {
				decreaseButton.disabled = false;
			} else {
				decreaseButton.disabled = true;
			}

			if (currentQuantity < 1000) {
				increaseButton.disabled = false;
			} else {
				increaseButton.disabled = true;
			}
		}
	}

	function findExistingRow(itemName) {
		
		for (var i = 1; i < billingTable.rows.length; i++) {
			var row = billingTable.rows[i];
			var itemNameCell = row.cells[1];
			if (itemNameCell.innerHTML === itemName) {
				return row;
			}
		}
		return null;
	}
	
	 function updateSerialNumber() {
        for (var i = 0; i < billingTable.rows.length; i++) {
            var row = billingTable.rows[i];
            var serialNo = row.cells[0];
            serialNo.innerHTML = i + 1;
        }
    }
	

	function updateTotal(priceText, itemPrice, quantity) {
		var total = itemPrice * quantity;
		priceText.innerHTML = '₹ ' + total.toFixed(2);
	}

	function removeItemFromBillingTable(row) {
		 row.parentNode.removeChild(row);
		updateSerialNumber();
		updateFinalTotal();
	}	
});

function clearBill(){
	var billingTable = document.querySelector('.tableBody');
	billingTable.innerHTML = '';
	updateFinalTotal();
	var paymentTypeSelect = document.getElementById('paymentType');
   paymentTypeSelect.value = '';
  document.getElementById('BillNumberValue').innerText=billNo; 
    var currentDate = new Date();
    var formattedDate = currentDate.toISOString().split("T")[0]; // Format: YYYY-MM-DD
    document.getElementById("BillDateValue").textContent = formattedDate;
}

function updateFinalTotal() {
    var billingTable = document.querySelector('.tableBody');
    var totalAmount = 0;

    for (var i = 0; i < billingTable.rows.length; i++) {
        var row = billingTable.rows[i];
        var cellValue = parseFloat(row.cells[3].innerText.replace('₹', ''));
        if (!isNaN(cellValue)) {
            totalAmount += cellValue;
        }
    }
    var total = document.getElementById('TotalAmount');
    total.innerHTML = 'Rs : ' + totalAmount.toFixed(2);
}

  async function submitBill(event) {
	event.preventDefault();
	var billingTable = document.querySelector('.tableBody');
	var paymentTypeSelect = document.getElementById('paymentType');
    var selectedPaymentType = paymentTypeSelect.value;
    
    if(billingTable.rows.length<1){
		alert("Please select atleast one product");
        return;
	}
    else if (!selectedPaymentType || selectedPaymentType === "") {
        alert("Please select a payment type");
        return;
    }
    
    
	var billItems = [];
	

	for (var i = 0; i < billingTable.rows.length; i++) {
		var row = billingTable.rows[i];
		var itemName = row.cells[1].innerText;
		var quantity = parseInt(row.cells[2].querySelector('.QuantityInputBox').value, 10);
		var amount = parseFloat(row.cells[3].querySelector('.BillingTablePriceText').innerText.replace('₹ ', ''));
		var dishId = row.dataset.id; // Retrieve dishId from the dataset

		var billItem = {
			dishId: dishId,
			dishName: itemName,
			dishQuantity: quantity,
			dishAmount: amount/quantity
		};

		billItems.push(billItem);
	}
	var cookies = document.cookie.split('; ');
	var jsonValue=null;
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].split('=');
        if (cookie[0] === "UserData") {
            // Decode the URI component and parse the JSON string
            var decodedValue = decodeURIComponent(cookie[1]);//Decode the cookie value
             jsonValue = JSON.parse(decodedValue);
			 break;
        }
    }

	var billData = {
		"dishData" : billItems,
		"paymentType" : selectedPaymentType,
		"userData" : jsonValue
	}
	console.log(billData);
	var servletURL="/RestaurantBillingSoftware/BillSaveController";
	const response = await fetch(servletURL, {
		method : "POST",
		headers : {
			"Content-Type" : "application/json",
		},
		body : JSON.stringify(billData),
	});
	let jsonResponse = await response.json();
	if (response.status === 200) {
		 if(jsonResponse.message==="Bill Saved Successfully"){
			 alert("Bill Submitted Successfully");
			 billNo++;
			 clearBill();
		 }else{
			 alert("Error submitting the bill");
		 }
		 
	} else {
		alert("An unexpected error occurred. Please try again.");
		console.error(text);
	}
}

function filterDishes() {
    var searchInput = document.getElementById('search').value.toLowerCase();
    var dishItems = document.querySelectorAll('.dishItem');

    dishItems.forEach(function (item) {
        var dishName = item.getAttribute('data-food').toLowerCase();
        if (dishName.includes(searchInput)) {
            item.style.display = 'block'; // Show matching items
        } else {
            item.style.display = 'none'; // Hide non-matching items
        }
    });
}
