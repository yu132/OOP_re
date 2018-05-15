package core_refactoring;

import java.util.ArrayList;

/**
 * 牌局分析器，通过分析当前牌局给出一个比较好的解决方案
 * @author 87663
 */
public interface CardGameAnalyzer {
	
	/**
	 * 获取提示
	 * @return 提示
	 */
	ArrayList<String> getTips();
	
	/**
	 * 获取最顶上的提示
	 * @return 最顶上的提示
	 * @throws NoSuchElementException 当没有提示时
	 */
	String getBestTips();
	
	/**
	 * 检查游戏是否胜利
	 * @return 游戏是否胜利
	 */
	boolean isGameFinish();
	
	/**
	 * 检查游戏是否失败
	 * @return 游戏是否失败
	 */
	boolean isGameOver();
	
	/**
	 * 分析一个牌局的情况
	 * @param cardManagement 一个牌局
	 */
	public void analyzerGame(CardManagement cardManagement);
	
}
