package core_refactoring;

import java.util.ArrayList;

/**
 * 游戏组件类，表示游戏组件应该有的一些操作
 * @author 87663
 */
public interface Component {
	
	/**
	 * 移动最顶上的一张牌，去另一个组件
	 * @param c 牌移动到的组件
	 * @return 本次移动是否成功
	 */
	MoveState sentSingleCard(Component c);
	
	/**
	 * 从另一个组件上得到一张牌
	 * @param card 得到的牌
	 * @return 本次移动是否成功
	 */
	MoveState getSingleCard(Card card);
	
	/**
	 * 移动最顶上的几张牌，去另一个组件
	 * @param c 牌移动到的组件
	 * @param number 移动牌的数量
	 * @return 本次移动是否成功
	 */
	MoveState sentCards(Component c,int number);
	
	/**
	 * 从另一个组件上得到几张牌
	 * @param cards 得到的牌
	 * @return 本次移动是否成功
	 */
	MoveState getCards(ArrayList<Card> cards);
	
	public ArrayList<String> getTopCard();
	
	/**
	 * 获取所有牌，看不见的用指定字符串填充
	 * @return  牌的枚举常量
	 */
	ArrayList<String> getAllCard();
	
	/**
	 * 检查第几张牌是否能够移动
	 * @param index 牌的序号
	 * @return 能否移动
	 */
	boolean ismovable(int index);
	
	/**
	 * 撤销上一次移动
	 * @return 是否撤销成功
	 */
	boolean undo();
	
	/**
	 * 撤销所有的移动
	 * @return 是否撤销成功
	 */
	boolean undoAll();
	
}
