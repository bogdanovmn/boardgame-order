class Discount {
	constructor(total) {
		this.total = total;
	}

	value() {
		let x = this.total * 0.92;
		// round to 50 RUB
		const y = x % 50;
		if (y > 0) {
			x = x + 50 - y;
		}
		return x;
	};
}