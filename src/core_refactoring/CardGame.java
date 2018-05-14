package core_refactoring;

import java.util.ArrayList;

import core_refactoring.impl.CardGameFactoryImpl;

/**
 * 是一个卡牌游戏，其实是其中各个组件的功能的外部接口
 * @author 87663
 *
 */
public interface CardGame {
	
	/**
	 * 为外部用户选择一个游戏构造器
	 */
	final static CardGameFactoryImpl cardGameFactory= new CardGameFactoryImpl();
	
	/**
	 * 返回一个默认难度的游戏
	 * @return 默认难度的游戏
	 */
	public static CardGame getCardGame(){
		return getCardGame(Mode.THREE_CARD_MODE);
	}
	
	/**
	 * 返回一个指定难度的游戏
	 * @param d 游戏难度
	 * @return 指定难度的游戏
	 */
	public static CardGame getCardGame(Difficulty d) {
		return getCardGame(d,Mode.THREE_CARD_MODE);
	}
	
	/**
	 * 返回一个指定游戏模式的游戏
	 * @param mode 游戏模式
	 * @return 指定游戏模式的游戏
	 */
	public static CardGame getCardGame(Mode mode) {
		return getCardGame(Difficulty.HARD,mode);
	}
	
	/**
	 * 返回一个指定难度和模式的游戏
	 * @param d 游戏难度
	 * @param mode 游戏模式
	 * @return 指定难度和模式的游戏
	 */
	public static CardGame getCardGame(Difficulty d, Mode mode) {
		return cardGameFactory.getCardGame(d, mode);
	}

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
	 * 获取某个组件内部所有的牌，没显示的用指定字符串填充
	 * @param c 需要获取的组件
	 * @return 所有的牌
	 */
	public ArrayList<String> getAllCard(Components c);
	
	/**
	 * 获取某个组件内部显示出来的牌
	 * @param c 需要获取的组件
	 * @return 显示出来的牌
	 */
	public ArrayList<String> getTopCard(Components c);
	
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
	 * 获取提示
	 * @return 提示的信息
	 */
	public String getTips();
	
	/**
	 * 判断是否已经失败
	 * @return 是否已经失败
	 */
	public boolean isGameOver();
	
	/**
	 * 检查游戏是否已经胜利
	 * @return 游戏是否已经胜利
	 */
	public boolean isGameFinish();
	
	/**
	 * 获取已经开始的时间长度
	 * @return 时间长度
	 */
	public long getTime();
	
	/**
	 * 获取当前得分
	 * @return 当前得分
	 */
	public int getPoint();
	
}
