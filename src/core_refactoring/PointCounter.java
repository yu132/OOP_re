package core_refactoring;

/**
 * 计分器，计算上一次得分，并可以获取得分
 * @author 87663
 *
 */
public interface PointCounter {

	/**
	 * 通过上一次牌的移动来计算上一次得分
	 * @param c1 牌移动的来源
	 * @param c2 牌移动的去向
	 */
	void addPoint(Component c1,Component c2,MoveState ms);
	
	/**
	 * 获取当前得分
	 * @return 当前得分
	 */
	int getPoint();
	
	/**
	 * 撤销上次操作所产生的得分
	 * @return 是否撤销成功
	 */
	boolean undo();
	
	/**
	 * 撤销所有的得分
	 * @return 是否撤销成功
	 */
	boolean undoAll();
	
}
