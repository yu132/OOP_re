package core_refactoring;

import java.util.ArrayList;

public interface CardManagement {

	/**
	 * 一次移动多张纸牌的方法
	 * @param from 纸牌来源的组件
	 * @param to 纸牌去往的组件
	 * @param number 一次移动纸牌的数量
	 * @return 是否能够完成这次移动
	 */
	public MoveState moveCards(Components from,Components to,int number);
	
	/**
	 * 让发牌器换下一波牌
	 */
	public void nextCard();
	
	/**
	 * 获取某个组件内部显示出来的牌
	 * @param c 需要获取的组件
	 * @return 显示出来的牌
	 */
	public ArrayList<String> getTopCard(Components c);
	
	/**
	 * 获取某个组件内部所有的牌，没显示的用指定字符串填充
	 * @param c 需要获取的组件
	 * @return 所有的牌
	 */
	public ArrayList<String> getAllCard(Components c);
	
	/**
	 * 是否能够撤销，如果已经到开始的时候，则不能够撤销
	 * @return 是否能够撤销
	 */
	public boolean undoable();
	
	/**
	 * 撤销上一次的移动
	 * @return 是否撤销成功，如果已经到开始的时候，则不能够撤销
	 */
	public boolean undo();
	
	/**
	 * 撤销所有的移动，回到纸牌游戏一开始的情况
	 * @return 是否撤销成功，如果已经到开始的时候，则不能够撤销
	 */
	public boolean undoAll();
	
	/**
	 * 获取上次的移动行为
	 * @return 上次的移动行为
	 */
	public String lastMove();
	
}
