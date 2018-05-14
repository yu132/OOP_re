package core_refactoring;

/**
 * 移动操作的返回状态
 * @author 87663
 */
public enum MoveState {

	SUCCESS,//成功
	HEAP_TOP_IS_NOT_K,//堆顶不是K
	WRONG_COLOR,//花色错误
	WRONG_NUMBER,//数字错误
	WRONG_COLLECTION,//收集没有按顺序或者花色
	ILLEGAL_MOVE;//非法的移动
	
}
