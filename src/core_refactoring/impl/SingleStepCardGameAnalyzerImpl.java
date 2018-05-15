package core_refactoring.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import core_refactoring.*;

public class SingleStepCardGameAnalyzerImpl implements CardGameAnalyzer{

	/**
	 * 牌堆数字对名称的映射
	 */
	private static Map<Integer,Components> map=new HashMap<>();
	
	/**
	 * 收集器数字对名称的映射
	 */
	private static Map<Integer,Components> boxmap=new HashMap<>();
	
	/**
	 * 以上两个映射的初始化
	 */
	static{
		map.put(1, Components.CARD_HEAP_1);
		map.put(2, Components.CARD_HEAP_2);
		map.put(3, Components.CARD_HEAP_3);
		map.put(4, Components.CARD_HEAP_4);
		map.put(5, Components.CARD_HEAP_5);
		map.put(6, Components.CARD_HEAP_6);
		map.put(7, Components.CARD_HEAP_7);
		
		boxmap.put(1, Components.BOX_1);
		boxmap.put(2, Components.BOX_2);
		boxmap.put(3, Components.BOX_3);
		boxmap.put(4, Components.BOX_4);
	}
	
	/**
	 * 提示列表
	 */
	private ArrayList<String> tips;
	
	/**
	 * 游戏是否完成
	 */
	private boolean finishedGame;
	
	/**
	 * 是否只找一个提示
	 */
	private boolean fastMode;

	/**
	 * 构造器
	 * @param fastMode 模式
	 */
	public SingleStepCardGameAnalyzerImpl(boolean fastMode) {
		super();
		this.fastMode = fastMode;
	}
	
	/**
	 * 返回模式
	 * @return 模式
	 */
	public boolean isFastMode() {
		return fastMode;
	}

	/**
	 * 设置模式
	 * @param fastMode 模式
	 */
	public void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}

	@Override
	public void analyzerGame(CardManagement cardGame){
		
		tips=new ArrayList<>();//建立数组
		
		if(cardGame.getAllCard(Components.DEALER).size()==0){//如果发牌器为空
			boolean flag=true;
			for(int i=1;i<7;i++){
				if(cardGame.getAllCard(map.get(i)).contains("null")){//如果牌堆没有仍未被翻开的牌
					flag=false;
					break;
				}
			}
			if(flag){
				finishedGame=true;//则游戏已经完成
				return;
			}
		}
		
		for(int i=1;i<=7;i++){//对于每个牌堆
			for(int j=1;j<=4;j++){//看每个收集器是否能够移动牌
				if(cardGame.moveCards(map.get(i), boxmap.get(j), 1)==MoveState.SUCCESS){//如果成功
					
					tips.add(map.get(i)+" "+boxmap.get(j)+" "+1);//添加提示
					
					cardGame.undo();//提示做完之后要撤销
					
					if(fastMode){
						return;
					}
				}
			}
		}
		
		for(int i=0;i<8;i++){//对于进行的next数量
			
			for(int j=1;j<=7;j++){//对于每个牌堆，检查发牌器能否移动牌进去
				
				if(cardGame.moveCards(Components.DEALER, map.get(j), 1)==MoveState.SUCCESS){
					
					tips.add(Components.DEALER+" "+map.get(j)+" "+1+" "+i);
					
					cardGame.undo();
					
					if(fastMode){
						while(i--!=0)
							cardGame.undo();
						return;
					}
				}
				
			}
			
			for(int j=1;j<=4;j++){//对于每个收集器，检查发牌器是否能移动牌进去
				
				if(cardGame.moveCards(Components.DEALER, boxmap.get(j), 1)==MoveState.SUCCESS){
					
					tips.add(Components.DEALER+" "+boxmap.get(j)+" "+1+" "+i);
					
					cardGame.undo();
					
					if(fastMode){
						while(i--!=0)
							cardGame.undo();
						return;
					}
				}
			}

			cardGame.nextCard();//进行NEXT操作
		}
		
		for(int i=0;i<8;i++){
			cardGame.undo();//之前NEXT了8次，则要撤销8次NEXT
		}
		
		//防止单次重复
		String[] sp=cardGame.lastMove().split(" ");
		
		boolean move=sp.length!=1;
		
		Components from = null;
		Components to = null;
		int numx = 0;
		
		if(move){
			from=Components.valueOf(sp[0]);
			to=Components.valueOf(sp[1]);
			numx=Integer.parseInt(sp[2]);
		}
		//防止单次重复
		
		for(int num=1;num<13;num++){//对于1-13张牌
			for(int i=1;i<=7;i++){//检查牌堆能否
				for(int j=1;j<=7;j++){//向另一个牌堆移动牌
					
					if(i==j)//如果来源和去往相同，则可以跳过
						continue;
					
					if(move)//如果上一次操作是移动
						if(map.get(i)==to&&map.get(j)==from&&num==numx)//检查本次移动是否和上次移动恰好相反
							continue;//如果是则跳过
					
					if(cardGame.moveCards(map.get(i), map.get(j), num)==MoveState.SUCCESS){
						
						tips.add(map.get(i)+" "+map.get(j)+" "+num);
						
						cardGame.undo();
						
						if(fastMode){
							return;
						}
					}
				}
			}
		}
		
	}

	@Override
	public ArrayList<String> getTips() {
		return tips;//获取提示
	}

	@Override
	public String getBestTips() {
		if(!tips.isEmpty())//如果提示数组不为空
			return tips.get(0);//获取第一个提示
		throw new NoSuchElementException("No tips available");//否则抛出费油提示的异常
	}

	@Override
	public boolean isGameFinish() {
		return finishedGame;//检查游戏是否完成
	}

	@Override
	public boolean isGameOver() {
		return !finishedGame&&tips.isEmpty();//如果游戏没有完成，并且没有提示，那么游戏就失败了
	}
	
}
