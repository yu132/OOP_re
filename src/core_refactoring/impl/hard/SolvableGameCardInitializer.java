package core_refactoring.impl.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import core_refactoring.util.RandomUniqueNumber;
import core_refactoring.*;
import core_refactoring.impl.*;

public class SolvableGameCardInitializer implements CardInitializer{

	public static class SolvableCardGame{
		
		public static void main(String[] args) {
			SolvableCardGame scg=SolvableCardGame.getASolvableCardGame();
			scg.showGame();
		}
		
		private final static Map<Integer,Components> heapMap=new HashMap<>();
		
		static {
			heapMap.put(0, Components.CARD_HEAP_1);
			heapMap.put(1, Components.CARD_HEAP_2);
			heapMap.put(2, Components.CARD_HEAP_3);
			heapMap.put(3, Components.CARD_HEAP_4);
			heapMap.put(4, Components.CARD_HEAP_5);
			heapMap.put(5, Components.CARD_HEAP_6);
			heapMap.put(6, Components.CARD_HEAP_7);
		}
		
		public static interface  Operation{}
		
		public static class NextOperation implements Operation{
			@Override
			public String toString() {
				return "Next";
			}

			@Override
			public boolean equals(Object obj) {
				if(obj instanceof NextOperation)
					return true;
				else 
					return false;
			}
		}
		
		public static class IndexAndSize implements Comparable<IndexAndSize>{
			private int index;
			private int size;
			public IndexAndSize(int index, int size) {
				super();
				this.index = index;
				this.size = size;
			}
			@Override
			public int compareTo(IndexAndSize o) {
				return o.size-size;
			}
		}
		
		public static class MoveOperation implements Operation{ 
			private Components from;
			private Components to;
			private int num;
			
			public Components getFrom() {
				return from;
			}
			public Components getTo() {
				return to;
			}
			public int getNum() {
				return num;
			}
			public MoveOperation(Components from, Components to, int num) {
				super();
				this.from = from;
				this.to = to;
				this.num = num;
			}
			@Override
			public String toString() {
				return "Move"+ " "+from + " " + to + " " + num   ;
			}
			@Override
			public boolean equals(Object obj) {
				if(obj instanceof MoveOperation){
					if( (((MoveOperation) obj).from==from) && (((MoveOperation) obj).to==to) && (((MoveOperation) obj).num==num) ){
						return true;
					}
				}
				return false;
			}
		}
		
		private ArrayList<Card>[] CardHeap;
		private ArrayList<Card> Dealer;
		private ArrayList<Operation> mvlist;
		
		public void showGame(){
			Set<Card> all=new HashSet<>();
			for(int i=1;i<=7;i++){
				System.out.println("牌堆"+i+":"+CardHeap[i-1]);
				all.addAll(CardHeap[i-1]);
			}
			System.out.println();
			
			System.out.println("发牌器:"+Dealer);
			all.addAll(Dealer);
			
			all.add(null);
			
			System.out.println("总牌数："+(all.size()-1));
			
			System.out.println(mvlist);
		}
		
		private SolvableCardGame(ArrayList<Card>[] cardHeap, ArrayList<Card> dealer, ArrayList<Operation> mvlist) {
			super();
			CardHeap = cardHeap;
			Dealer = dealer;
			this.mvlist = mvlist;
		}
		
		private static Random r=new Random();
		
		private static void initHeap(ArrayList<Card> CardHeap[]){
			boolean f=true;
			for(int i=12;i>=0;i--){
				if(f){
					if(r.nextBoolean()){
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i],CardType.CLUBS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i],CardType.SPADES));
					}else{
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i],CardType.SPADES));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i],CardType.CLUBS));
					}
					
					if(r.nextBoolean()){
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i],CardType.DIAMONDS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i],CardType.HEARTS));
					}else{
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i],CardType.HEARTS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i],CardType.DIAMONDS));
					}
					
					f=false;
				}else{
					if(r.nextBoolean()){
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i],CardType.DIAMONDS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i],CardType.HEARTS));
					}else{
						CardHeap[3].add(CardImpl.valueOf(CardNumber.values()[i],CardType.HEARTS));
						CardHeap[4].add(CardImpl.valueOf(CardNumber.values()[i],CardType.DIAMONDS));
					}
					
					if(r.nextBoolean()){
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i],CardType.CLUBS));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i],CardType.SPADES));
					}else{
						CardHeap[5].add(CardImpl.valueOf(CardNumber.values()[i],CardType.SPADES));
						CardHeap[6].add(CardImpl.valueOf(CardNumber.values()[i],CardType.CLUBS));
					}
					
					f=true;
				}
			}
		}
		
		private static boolean fromHeaptoHeap(ArrayList<Card> CardHeap[],int[] coverNumber,int from,int to,int num,ArrayList<Operation> mvlist){
			if(num<=0)
				return false;
			if(CardHeap[from].size()-coverNumber[from]<num)
				return false;
			if(CardHeap[from].size()-coverNumber[from]==1)
				return false;
			
			if(CardHeap[to].size()!=coverNumber[to]){
				if(!CardHeap[to].isEmpty()&&CardHeap[to].get(CardHeap[to].size()-1)
					.isStackableInHeap(CardHeap[from].get(CardHeap[from].size()-num))
					!=MoveState.SUCCESS)
					return false;
			}
			
			CardHeap[to].addAll(CardHeap[from]
					.subList(CardHeap[from].size()-num, CardHeap[from].size()));
			for(int i=0;i<num;i++){
				CardHeap[from].remove(CardHeap[from].size()-1);
			}
			mvlist.add(new MoveOperation(heapMap.get(from), heapMap.get(to), num));
			return true;
		}
		
		private static boolean coverHeapCard(ArrayList<Card> CardHeap[],int[] coverNumber,int x){
			if(coverNumber[x]==x)
				return false;
			if(CardHeap[x].size()==coverNumber[x]+1){
			/*	if(CardHeap[x].get(coverNumber[x]).getCardNumber()==CardNumber.KING){
					if(r.nextInt(100)<95)
						return false;
				}*/
				coverNumber[x]++;
				return true;
			}
			return false;
		}
		
		private static boolean isHeapCoverableMax(ArrayList<Card> CardHeap[],int[] coverNumber,int x){
			if(coverNumber[x]==x)
				return false;
			else
				return true;
		}
		
		private static boolean fromHeapToDealer(ArrayList<Card> CardHeap[],int[] coverNumber,Card[][] tempDealer,int from,int to,int[] togiveNum,int lastGroup,ArrayList<Operation> mvlist){
			if(CardHeap[from].size()-coverNumber[from]<=1){
				return false;
			}
			
			Card c=CardHeap[from].remove(CardHeap[from].size()-1);
			if(to!=lastGroup)
				tempDealer[to][3-(togiveNum[to]--)]=c;
			else{
				for(int i=0;i<3;i++){
					if(tempDealer[to][i]==null){
						tempDealer[to][i]=c;
						togiveNum[to]--;
						break;
					}
				}
			}
			mvlist.add(new MoveOperation(heapMap.get(from), Components.DEALER, 1));
			return true;
		}
		
		private static boolean organizeHeap(ArrayList<Card> CardHeap[],int[] coverNumber,ArrayList<Operation> mvlist){
			int[] need=new int[7];
			int[] togive=new int[7];
			boolean[] finish=new boolean[7];
			
			RandomUniqueNumber ruH1=new RandomUniqueNumber(0,6);
			RandomUniqueNumber ruH2=new RandomUniqueNumber(1,6);
			
			for(int i=0;i<7;i++){
				need[i]=i-coverNumber[i];
				togive[i]=CardHeap[i].size()-coverNumber[i];
				if(need[i]==0&&togive[i]==1)
					finish[i]=true;
			}
			
			boolean kasi=true;
			
			while(true){
				
				if(finish[0]){
					finish[0]=finish[1]&&finish[2]&&finish[3]&&finish[4]&&finish[5]&&finish[6];
				}
				
				boolean flag=true;
				for(int i=1;i<7;i++)
					if(!finish[i]){
						flag=false;
						break;
					}
				if(flag)
					break;
				
				boolean flagx=r.nextBoolean();
				int h1=0;
				int h2=0;
				ruH1.reSet();
				l:
				for(int x=0;x<7;x++){
					
					if(x==0)
						flagx=false;
					
					h1=ruH1.getNum();
					if(finish[h1])
						continue;
					if(togive[h1]<=1)
						continue;
					ruH2.reSet();
					for(int y=1;y<7;y++){
						h2=ruH2.getNum();
						if(h1==h2)
							continue;
						if(finish[h2])
							continue;
						int num=flagx?CardHeap[h1].size()-coverNumber[h1]-1:1;
						
						if(fromHeaptoHeap(CardHeap, coverNumber, h1, h2,num,mvlist)){
							
							if(flagx){
								coverHeapCard(CardHeap, coverNumber, h1);
							}else{
								coverHeapCard(CardHeap, coverNumber, h2);
							}
							
							togive[h1]=CardHeap[h1].size()-coverNumber[h1];
							need[h1]=h1-coverNumber[h1];
							
							togive[h2]=CardHeap[h2].size()-coverNumber[h2];
							need[h2]=h2-coverNumber[h2];
							
							if(need[h1]==0&&togive[h1]==1)
								finish[h1]=true;
							if(need[h2]==0&&togive[h2]==1)
								finish[h2]=true;
							
							
							kasi=false;
							
							if(mvlist.get(mvlist.size()-1).equals(mvlist.get(mvlist.size()-3)))
								kasi=true;
							
							break l;
						}
					}
				}
				
				if(kasi){
					
					boolean cover=false;
					
					for(int i=1;i<7;i++)
						if(coverHeapCard(CardHeap, coverNumber, i))
							cover=true;
					if(!cover){
						
						ArrayList<IndexAndSize> slist=new ArrayList<>();
						for(int i=1;i<7;i++){
							slist.add(new IndexAndSize(i, CardHeap[i].size()-coverNumber[i]));
						}
						Collections.sort(slist);
						
						boolean faliure=true;
						
						for(int x=0;x<6;x++){
							h1=slist.get(x).index;
							if(finish[h1])
								continue;
							if(togive[h1]<=1)
								continue;
							h2=0;
							if(fromHeaptoHeap(CardHeap, coverNumber, h1, h2,CardHeap[h1].size()-coverNumber[h1]-1,mvlist)){
								
								faliure=false;
								
								coverHeapCard(CardHeap, coverNumber, h1);
								
								togive[h1]=CardHeap[h1].size()-coverNumber[h1];
								need[h1]=h1-coverNumber[h1];
								
								togive[h2]=CardHeap[h2].size()-coverNumber[h2];
								need[h2]=h2-coverNumber[h2];
								
								if(need[h1]==0&&togive[h1]==1)
									finish[h1]=true;
								if(need[h2]==0&&togive[h2]==1)
									finish[h2]=true;
								
							}
							
						}
						
						if(faliure)
							return false;
					}
				}else{
					kasi=true;
				}
				
			}
			return true;
		}
		
		private static int[] organizeDealer(Card[][] tempDealer,int[] togiveNum,int allDealerNum){
			
			int[] ret=new int[2];
			
			int x=0;
			int group=0;
			
			l:
			for(int i=1;i<=8;i++){
				group=i;
				for(int j=0;j<3;j++){
					if(tempDealer[i-1][j]==null)
						break l;
					else
						x++;
				}
			}
			
			int temp1=r.nextInt(100)+1;
			
			int groupNext;
			int addGroup;
			
			if(temp1<30)
				addGroup=0;
			else if(temp1<60)
				addGroup=1;
			else if(temp1<87)
				addGroup=2;
			else if(temp1<89)
				addGroup=3;
			else if(temp1<91)
				addGroup=4;
			else if(temp1<92)
				addGroup=5;
			else if(temp1<93)
				addGroup=6;
			else{
				for(int i=0;i<togiveNum.length;i++)
					togiveNum[i]=0;
				
				ret[0]=allDealerNum;
				ret[1]=-1;
				
				return ret;
			}
			
			if(group+addGroup>=8)
				groupNext=8;
			else
				groupNext=group+addGroup;
			
			int nextCardNumber=(groupNext-1)*3+r.nextInt(3)+1;
			
			if(nextCardNumber>24)
				nextCardNumber=24;
			
			if(nextCardNumber<=x){
				if(r.nextBoolean()){
					nextCardNumber=x+1;
				}else{
					if(r.nextBoolean()){
						nextCardNumber=x+2;
					}else{
						for(int i=0;i<togiveNum.length;i++)
							togiveNum[i]=0;
						
						ret[0]=allDealerNum;
						ret[1]=-1;
						
						return ret;
					}
				}
			}
			
			int orgNum=nextCardNumber-x;
			
			int xx=((nextCardNumber-1)/3+1);
			if(xx<8)
				groupNext=xx;
			else
				groupNext=8;
			
			if(allDealerNum-orgNum>=0)
				allDealerNum-=orgNum;
			else{
				orgNum=allDealerNum;
				allDealerNum=0;
			}
			
			int leave=nextCardNumber-(groupNext-1)*3;
			
			if(groupNext-2>=0){
				RandomUniqueNumber ru=new RandomUniqueNumber(0,groupNext-2);
				
				while(true){
					ru.reSet();
					
					for(int i=0;i<groupNext-1;i++){
						
						int g=ru.getNum();
						
						int next=r.nextInt(orgNum+1>4?4:orgNum+1);
						
						if(next>orgNum){
							next=orgNum;
						}
						
						if(togiveNum[g]+next>3){
							orgNum=orgNum+togiveNum[g]-3;
							togiveNum[g]=3;
						}else{
							orgNum-=next;
							togiveNum[g]+=next;
						}
						
					}
					
					if(orgNum<=leave){
						if(r.nextBoolean())
							break;
					}
				}
			}
			togiveNum[groupNext-1]=orgNum;
			
			Card[][] tempDealerNew=new Card[8][3];
			
			int inxde1=0;
			int indexIn=0;
			
			
			for(int i=0;i<groupNext-1;i++){
				for(int j=0;j<3-togiveNum[i];j++){
					if(indexIn==3){
						indexIn=0;
						inxde1++;
					}
					tempDealerNew[i][j]=tempDealer[inxde1][indexIn++];
					x--;
				}
			}
			
			
			for(int i=0;i<x;i++){
				if(indexIn==3){
					indexIn=0;
					inxde1++;
				}
				tempDealerNew[groupNext-1][i]=tempDealer[inxde1][indexIn++];
			}
			
			ret[0]=allDealerNum;
			ret[1]=groupNext-1;
			
			for(int i=0;i<8;i++)
				for(int j=0;j<3;j++)
					tempDealer[i][j]=tempDealerNew[i][j];
			
			return ret;
			
			
		}
		
		private static void print(ArrayList<Card>[] CardHeap,Card[][] tempDealer,int[] coverNumber,ArrayList<Operation> mvlist){
			Set<Card> all=new HashSet<>();
			for(int i=1;i<=7;i++){
				System.out.println("牌堆"+i+":"+CardHeap[i-1]);
				System.out.println("盖上的数量:"+coverNumber[i-1]);
				all.addAll(CardHeap[i-1]);
			}
			System.out.println();
			
			
			ArrayList<Card> Dealer=new ArrayList<>();
			
			for(int i=0;i<8;i++){
				for(int j=0;j<3;j++)
					Dealer.add(tempDealer[i][j]);
			}
			
			System.out.println("发牌器:"+Dealer);
			all.addAll(Dealer);
			
			all.add(null);
			
			System.out.println("总牌数："+(all.size()-1));
			
			Collections.reverse(mvlist);
			System.out.println(mvlist);
			Collections.reverse(mvlist);
			System.out.println();
			
		}
		
		private static SolvableCardGame getASolvableCardGamePro(){
			
			ArrayList<Operation> mvlist=new ArrayList<>();
			
			@SuppressWarnings("unchecked")
			ArrayList<Card>[] CardHeap = new ArrayList[7];
			
			int[] coverNumber=new int[7];
			
			for(int i=0;i<7;i++){
				CardHeap[i]=new ArrayList<>();
			}
			
			int allDealerNum=24;
			
			RandomUniqueNumber ruH1=new RandomUniqueNumber(1,6);
			RandomUniqueNumber ruH2=new RandomUniqueNumber(1,6);
			
			Card[][] tempDealer=new Card[8][3];
			
			int[] togiveNum=new int[8];
			
			initHeap(CardHeap);
			
			print(CardHeap, tempDealer,coverNumber,mvlist);//
			
			while(allDealerNum!=0){
				
				int[] ret=organizeDealer(tempDealer, togiveNum, allDealerNum);
				
				print(CardHeap, tempDealer,coverNumber,mvlist);//
				
				allDealerNum=ret[0];
				
				for(int i=7;i>=0;i--){
					
					int tt=togiveNum[i];
					for(int j=0;j<tt;j++){
						
						int temp=0;
						while(temp<50){
							int temp2=r.nextInt(100);
							if(temp2<40){
								ruH1.reSet();
								l:
								for(int x=1;x<=6;x++){
									int h1=ruH1.getNum();
									ruH2.reSet();
									for(int y=1;y<=6;y++){
										int h2=ruH2.getNum();
										if(h1==h2)
											continue;
										
										int num=CardHeap[h1].size()-coverNumber[h1]-1;
										if(isHeapCoverableMax(CardHeap, coverNumber, h1)&&fromHeaptoHeap(CardHeap, coverNumber, h1, h2,num,mvlist)){
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h1);
											
											print(CardHeap, tempDealer,coverNumber,mvlist);//
											
											break l;
										}
									}
								}
							}else if(temp2<80){
								ruH1.reSet();
								l:
								for(int x=1;x<=6;x++){
									int h1=ruH1.getNum();
									ruH2.reSet();
									for(int y=1;y<=6;y++){
										int h2=ruH2.getNum();
										if(h1==h2)
											continue;
										if(isHeapCoverableMax(CardHeap, coverNumber, h2)&&fromHeaptoHeap(CardHeap, coverNumber, h1, h2, 1,mvlist)){
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h2);
											
											print(CardHeap, tempDealer,coverNumber,mvlist);//										
											
											break l;
										}
									}
								}
									
							}else if(temp2<95){
								ruH1.reSet();
								l:
								for(int x=1;x<=6;x++){
									int h1=ruH1.getNum();
									ruH2.reSet();
									
									if(CardHeap[h1].size()-coverNumber[h1]==0)
										continue;
									
									for(int y=1;y<=6;y++){
										int h2=ruH2.getNum();
										if(h1==h2)
											continue;
										int num=(CardHeap[h1].size()-coverNumber[h1])/2;
										if(num==1)
											continue;
										if(fromHeaptoHeap(CardHeap, coverNumber, h1, h2, num,mvlist)){
											coverHeapCard(CardHeap, coverNumber, h1);
											coverHeapCard(CardHeap, coverNumber, h2);
											
											print(CardHeap, tempDealer,coverNumber,mvlist);//
											
											break l;
										}
									}
								}
							}
							
							temp=r.nextInt(100);
						}
						
						ruH1.reSet();
						for(int x=1;x<=6;x++){
							int from=ruH1.getNum();
							
							if(CardHeap[from].size()-coverNumber[from]==1)
								continue;
							
							if(fromHeapToDealer(CardHeap, coverNumber, tempDealer, from, i, togiveNum,ret[1],mvlist)){
								coverHeapCard(CardHeap, coverNumber, from);
								
								print(CardHeap, tempDealer,coverNumber,mvlist);//
								
								break;
							}
						}
					}
					
					mvlist.add(new NextOperation());
				}
			}
			
			
			ArrayList<Card> Dealer=new ArrayList<>();
			for(int i=0;i<8;i++){
				for(int j=0;j<3;j++){
					Dealer.add(tempDealer[i][j]);
				}
			}
			
			Collections.reverse(mvlist);
			System.out.println(mvlist);
			Collections.reverse(mvlist);
			
			if(!organizeHeap(CardHeap, coverNumber,mvlist))
				return null;
			
			print(CardHeap, tempDealer,coverNumber,mvlist);//
			
			Collections.reverse(mvlist);
			System.out.println(mvlist);
			Collections.reverse(mvlist);
			
			
			
			return new SolvableCardGame(CardHeap, Dealer, mvlist);
		}
		
		public static SolvableCardGame getASolvableCardGame(){
			SolvableCardGame ans=getASolvableCardGamePro();
			while(ans==null)
				ans=getASolvableCardGamePro();
			Collections.reverse(ans.mvlist);
			
			for(int i=0;i<7;i++)
				Collections.reverse(ans.CardHeap[i]);
			
			return ans;
		}
		
	}
	
	private SolvableCardGame scg=SolvableCardGame.getASolvableCardGame();
	
	private int dealerIndex=0;
	
	private int[] heapIndexes=new int[7]; 
	
	@Override
	public Card getCard(Components c) {
		
		switch(c){
		case DEALER:
			return scg.Dealer.get(dealerIndex++);
		case CARD_HEAP_1:
			return scg.CardHeap[0].get(heapIndexes[0]++);
		case CARD_HEAP_2:
			return scg.CardHeap[1].get(heapIndexes[1]++);
		case CARD_HEAP_3:
			return scg.CardHeap[2].get(heapIndexes[2]++);
		case CARD_HEAP_4:
			return scg.CardHeap[3].get(heapIndexes[3]++);
		case CARD_HEAP_5:
			return scg.CardHeap[4].get(heapIndexes[4]++);
		case CARD_HEAP_6:
			return scg.CardHeap[5].get(heapIndexes[5]++);
		case CARD_HEAP_7:
			return scg.CardHeap[6].get(heapIndexes[6]++);
		default:
			return null;
		}
	}
	
}