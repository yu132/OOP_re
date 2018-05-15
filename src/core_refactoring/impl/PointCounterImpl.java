 package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import core_refactoring.*;

public class PointCounterImpl implements PointCounter{

	/**
	 * 分数
	 */
	private int point=0;
	
	/**
	 * 快照栈
	 */
	private Deque<Integer> snapshot=new ArrayDeque<>();
	
	@Override
	public int getPoint() {
		return point;//返回分数
	}

	@Override
	public void addPoint(Component c1, Component c2, MoveState ms) {
		
		snapshot.push(point);//拍快照
		
		if(ms==MoveState.SUCCESS){//如果移动成功的话
			if(c1 instanceof Dealer){//如果来源是发牌器
				if(c2 instanceof Box){//去收集器
					point+=10;//加10分
				}else{//去牌堆
					point+=5;//加5分
				}
			}else if(c1 instanceof Box){//如果来源是收集器
				point-=10;//减10分
			}else if(c1 instanceof CardHeap){//如果来源是牌堆
				if(c2 instanceof Box){//去收集器
					point+=10;//加10分
				}
				if(((CardHeap) c1).openCardLastRound()){//如果上次移动导致有一张牌被打开
					point+=5;//加5分
				}
				
			}
		}
	}

	@Override
	public boolean undo() {
		if(snapshot.isEmpty())//如果快照为空，则不能恢复
			return false;
		
		point=snapshot.pop();//从快照中恢复分数
		return true;
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())//如果快照为空，则不能恢复
			return false;
		
		//重置回初始状态
		snapshot.clear();
		point=0;
		
		return true;
	}

}
