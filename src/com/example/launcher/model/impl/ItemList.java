package com.example.launcher.model.impl;

import java.util.ArrayList;

public class ItemList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7251694731112643754L;

	/**
	 * Перемещение элементов
	 * 
	 * @param indexUp
	 *            - индекс элемента, которые перемещается вверх
	 * @param indexDown
	 *            - индекс элемента, который перемещается вниз
	 */
	public void move(int indexUp, int indexDown) {
		T item = get(indexUp);
		set(indexUp, get(indexDown));
		set(indexDown, item);
	}

	/**
	 * Переместить элемент "вверх" (к начальному элементу)
	 * 
	 * @param index
	 */
	public void moveUp(int index) {
		if (index == 0)
			return;
		move(index, index - 1);
	}

	/**
	 * Переместить элемент "вниз" (к конечному элементу)
	 * 
	 * @param index
	 */
	public void moveDown(int index) {
		if (index >= size() - 1)
			return;
		move(index + 1, index);
	}

}
