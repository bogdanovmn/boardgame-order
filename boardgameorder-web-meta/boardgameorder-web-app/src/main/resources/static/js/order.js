class Order{
	constructor(itemsCount, total) {
		this.itemsCount = itemsCount;
		this.total = total;
	}

	addItem(id) {
		const item = new OrderItem(id);
		this.total += item.getPrice();
		this.itemsCount++;
		item.render(1, item.getPrice())
	};

	delItem(id) {
		const item = new OrderItem(id);
		this.total -= item.getPrice();
		this.itemsCount--;
		item.render(-1, -1*item.getPrice())
	};

	render() {
		$("#orderSummary div.data").html(
			`<p>Итого: ${this.total} руб.<p>Со скидкой: <b>${new Discount(this.total).value()}</b> руб.`
		)
	};

	getText() {
		let text = "";
		$("table.price-and-item tr").each(function(i) {
			const count = parseInt($(this).find("td.count span").text());
			if (count !== 0) {
				if (count > 1) {
					text += "[КОЛ-ВО: " + count + "] ";
				}
				text += $(this).find("td.title span").text() + "\r\n";
			}
		});
		return text;
	}
}

class OrderItem {
	constructor(id) {
		this.id = id;
		this.element = $("#item-" + id);
	}

	getPrice() {
		return parseInt(
			this.element.attr("data-item-price")
		);
	}

	render(countInc, priceInc) {
		const itemCountElement = this.element.find("td.count span");
		const itemTotalElement = this.element.find("td.price span");

		itemCountElement.text(
			parseInt(itemCountElement.text()) + countInc
		);

		itemTotalElement.text(
			parseInt(itemTotalElement.text()) + priceInc
		);
	}
}