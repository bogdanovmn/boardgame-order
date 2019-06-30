class Discount {
	constructor(total, totalWithFixPrice) {
		this.total = total;
		this.totalWithFixPrice = totalWithFixPrice;
	}

	value() {
		let x = (this.total - this.totalWithFixPrice) * 0.92 + this.totalWithFixPrice;
		// round to 50 RUB
		const y = x % 50;
		if (y > 0) {
			x = x + 50 - y;
		}
		return x;
	};
}