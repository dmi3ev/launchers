package com.example.launcher.model;

public enum ColumnNames {
	delete(0), launch(1), delay(2), move(3);

	private int index;

	ColumnNames(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
