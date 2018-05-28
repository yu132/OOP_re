package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import core_refactoring.*;

/**
 * 牌局管理器的实现类
 * @author 87663
 *
 */
public class CardManagementImpl implements CardManagement{
	
	/**
	 * 定义移动的形式的枚举
	 * @author 87663
	 *
	 */
	private enum Operation{
		move,next;
	}
	
	/**
	 * 移动的信息的类
	 * @author 87663
	 *
	 */
	private class Operation_pair{
		private Operation o;
		private Components from;
		private Components to;
		public Operation_pair(Operation o, Components from, Components to) {
			super();
			this.o = o;
			this.from = from;
			this.to = to;
		}
	}
	
	/**
	 * 需要分析的标志
	 */
	private boolean needAnalyze=true;
	
	/**
	 * 快照的存储栈
	 */
	private Deque<Operation_pair> snapshot=new ArrayDeque<>();

	/**
	 * 发牌器
	 */
	private Dealer dealer;
	
	/**
	 * 收集器
	 */
	private Box box_1;
	private Box box_2;
	private Box box_3;
	private Box box_4;
	
	/**
	 * 牌堆
	 */
	private CardHeap cardHeap_1;
	private CardHeap cardHeap_2;
	private CardHeap cardHeap_3;
	private CardHeap cardHeap_4;
	private CardHeap cardHeap_5;
	private CardHeap cardHeap_6;
	private CardHeap cardHeap_7;
	
	/**
	 * 计分器
	 */
	private PointCounter pointCounter;
	
	/**
	 * 提示获取器
	 */
	private CardGameAnalyzer tipsGetter;
	
	/**
	 * 上一次的移动
	 */
	private String lastMove="";
	
	/**
	 * 从名称枚举到具体对象的映射
	 */
	private Map<Components,Component> map=new HashMap<>();
	
	public CardManagementImpl(Dealer dealer, Box box_1, Box box_2, Box box_3, Box box_4, CardHeap cardHeap_1,
			CardHeap cardHeap_2, CardHeap cardHeap_3, CardHeap cardHeap_4, CardHeap cardHeap_5, CardHeap cardHeap_6,
			CardHeap cardHeap_7, PointCounter pointCounter, CardGameAnalyzer tipsGetter) {
		super();
		
		//初始化属性
		this.dealer = dealer;
		this.box_1 = box_1;
		this.box_2 = box_2;
		this.box_3 = box_3;
		this.box_4 = box_4;
		this.cardHeap_1 = cardHeap_1;
		this.cardHeap_2 = cardHeap_2;
		this.cardHeap_3 = cardHeap_3;
		this.cardHeap_4 = cardHeap_4;
		this.cardHeap_5 = cardHeap_5;
		this.cardHeap_6 = cardHeap_6;
		this.cardHeap_7 = cardHeap_7;
		this.pointCounter = pointCounter;
		this.tipsGetter = tipsGetter;
		
		//初始化从名称枚举到具体对象的映射
		map.put(Components.DEALER, this.dealer);
		map.put(Components.BOX_1, this.box_1);
		map.put(Components.BOX_2, this.box_2);
		map.put(Components.BOX_3, this.box_3);
		map.put(Components.BOX_4, this.box_4);
		map.put(Components.CARD_HEAP_1, this.cardHeap_1);
		map.put(Components.CARD_HEAP_2, this.cardHeap_2);
		map.put(Components.CARD_HEAP_3, this.cardHeap_3);
		map.put(Components.CARD_HEAP_4, this.cardHeap_4);
		map.put(Components.CARD_HEAP_5, this.cardHeap_5);
		map.put(Components.CARD_HEAP_6, this.cardHeap_6);
		map.put(Components.CARD_HEAP_7, this.cardHeap_7);
	}
	
	/**
	 * 初始化方法
	 */
	public void init(){
		//需要分析的话，做第一次分析
		if(needAnalyze){
			needAnalyze=false;
			tipsGetter.analyzerGame(this);
			needAnalyze=true;
		}
	}

	/**
	 * 获取快照的内部方法
	 * @param from 操作的来源组件
	 * @param to 操作的去往组件
	 * @return 一个移动信息
	 */
	private Operation_pair getSnapshot(Components from, Components to){
		return new Operation_pair(Operation.move, from, to);
	}
	
	/**
	 * 移动单张牌
	 * @param from 操作的来源组件
	 * @param to 操作的去往组件
	 * @return 操作的结果
	 */
	public MoveState moveSingleCard(Components from, Components to) {
		
		if(from==to)//如果来源和去往的组件相同，则是非法的
			return MoveState.ILLEGAL_MOVE;
		
		//这部分lastMove的变动主要是为了如果移动成功的话，如果触发了发牌操作，
		//内部会调用分析，如果不在这个时候暂时修改的话，则会造成错误
		
		String templm=lastMove;//暂存上次移动的信息
		lastMove=from+" "+to+" "+1;//将上次移动标记为本次移动的信息
		MoveState ms=map.get(from).sentSingleCard(map.get(to));//调用具体组件，并得到结果
		lastMove=templm;//将上次移动的信息恢复
		
		if(ms==MoveState.SUCCESS){//如果上次移动成功
			
			snapshot.push(getSnapshot(from, to));//存快照
		
			pointCounter.addPoint(map.get(from), map.get(to), ms);//计分
			
			if(needAnalyze){//如果需要分析
				needAnalyze=false;//防止分析时造成递归
				
				lastMove=from+" "+to+" "+1;//把上次移动标记为本次移动的信息
				tipsGetter.analyzerGame(this);//分析牌局
				
				needAnalyze=true;//防止分析时造成递归
			}
		
		}
		
		return ms;//返回移动结果
	}

	@Override
	public MoveState moveCards(Components from, Components to, int number) {
		
		if(from==to)//如果来源和去往的组件相同，则是非法的
			return MoveState.ILLEGAL_MOVE;
		
		if(number==1){//如果是一张牌，就调用移动一张牌的方法
			return moveSingleCard(from,to);
		}
		
		//这部分lastMove的变动主要是为了如果移动成功的话，如果触发了发牌操作，
		//内部会调用分析，如果不在这个时候暂时修改的话，则会造成错误
		
		String templm=lastMove;//暂存上次移动的信息
		lastMove=from+" "+to+" "+number;//将上次移动标记为本次移动的信息
		MoveState ms=map.get(from).sentCards(map.get(to),number);//调用具体组件，并得到结果
		lastMove=templm;//将上次移动的信息恢复
		
		if(ms==MoveState.SUCCESS){	//如果上次移动成功
			
			snapshot.push(getSnapshot(from, to));//存快照
		
			pointCounter.addPoint(map.get(from), map.get(to), ms);//计分
			
			if(needAnalyze){//如果需要分析
				needAnalyze=false;//防止分析时造成递归
				
				lastMove=from+" "+to+" "+number;//把上次移动标记为本次移动的信息
				tipsGetter.analyzerGame(this);//分析牌局
				
				needAnalyze=true;//防止分析时造成递归
			}
		}
		
		return ms;//返回移动结果
	}

	@Override
	public void nextCard() {
		
		snapshot.push(new Operation_pair(Operation.next,null,null));//存快照
		
		dealer.nextCards();//调用具体组件
		
		if(needAnalyze){//如果需要分析
			needAnalyze=false;//防止分析时造成递归
			lastMove="next";//把上次移动标记为本次移动的信息
			tipsGetter.analyzerGame(this);//分析牌局
			
			needAnalyze=true;//防止分析时造成递归
		}
		
	}
	
	@Override
	public ArrayList<String> getTopCard(Components c) {
		return map.get(c).getTopCard();//调用具体组件
	}

	@Override
	public ArrayList<String> getAllCard(Components c) {
		return map.get(c).getAllCard();//调用具体组件
	}

	@Override
	public boolean undo() {
		if(snapshot.isEmpty())//如果快照为空，则不能撤销
			return false;
		
		Operation_pair op=snapshot.pop();//获取最后一张快照
		
		if(op.o==Operation.next){//如果是调用的NEXT
			
			dealer.undo();//调用具体组件
			
		}else{//如果调用的是MOVE
			
			map.get(op.from).undo();//调用具体组件
			map.get(op.to).undo();//调用具体组件
			pointCounter.undo();//调用具体组件			
		}
		
		if(snapshot.isEmpty()){
			if(needAnalyze){
				needAnalyze=false;//防止分析时造成递归
				
				lastMove="";
				tipsGetter.analyzerGame(this);//分析牌局	
				
				needAnalyze=true;//防止分析时造成递归	
			}
		}else if(needAnalyze){//如果需要分析
			needAnalyze=false;//防止分析时造成递归
			Operation_pair op2=snapshot.pop();
			if(op2.o==Operation.next){//如果是调用的NEXT
				lastMove="next";//把上次移动标记为本次移动的信息
				tipsGetter.analyzerGame(this);//分析牌局
				
				needAnalyze=true;//防止分析时造成递归
			}else{//如果调用的是MOVE	
				lastMove=op2.from+" "+op2.to+" "+1;//把上次移动标记为本次移动的信息
				tipsGetter.analyzerGame(this);//分析牌局			

			}
			needAnalyze=true;//防止分析时造成递归	
		}
		
		return true;//返回撤销成功
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())//如果快照为空，则不能撤销
			return false;
		
		snapshot.clear();//清空快照
		
		for(Component c:map.values()){//对于每个组件，都需要全部撤销
			c.undoAll();//调用具体组件
		}
		
		pointCounter.undoAll();//调用具体组件
		
		return true;//返回撤销成功
	}

	@Override
	public boolean undoable() {
		return !snapshot.isEmpty();//如果快照不空，则可以撤销
	}
	
	@Override
	public String lastMove() {
		return lastMove;//返回上次移动的信息
	}


}
