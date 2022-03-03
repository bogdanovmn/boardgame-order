class Item {
	constructor(id) {
		this.id = id;
		this.element = $("#item-" + this.id);
	}

	getPrice() {
		return parseInt(
			this.element.find("td.price").text()
		);
	};

	isFixPrice() {
		return $.parseJSON(this.element.attr("data-fix-price"));
	}

	renderOrder(ordered) {
		if (!ordered) {
			this.element.toggleClass("ordered");
		}
	}
}

class OrderBlock {
	constructor(itemsCount, total, totalWithFixPrice) {
		this.itemsCount = itemsCount;
		this.total = total;
		this.totalWithFixPrice = totalWithFixPrice;

		const orderedItems = {};
		$("tr.ordered div.button-add-item").each(function (i) {
			orderedItems[$(this).attr("data-id")] = 1;
		});

		this.orderedItems = orderedItems;
	}

	addItem(id) {
		const item = new Item(id);
		if (item.isFixPrice()) {
			this.totalWithFixPrice += item.getPrice();
		}
		this.total += item.getPrice();
		this.itemsCount++;
		item.renderOrder(this.orderedItems[id] === 1);
		this.orderedItems[id] = 1;
	};

	render() {
		if (this.itemsCount === 0) {
			$("#order").hide();
		} else {
			$("#order").show();
			$("#order div.details").html(`
				<p>Выбрано:<b>${this.itemsCount}</b>
				<p>Сумма:<br>${this.total} руб.
				<p>Со скидкой:<br><b>${new Discount(this.total, this.totalWithFixPrice).value()}</b> руб.`
			)
		}
	}
}