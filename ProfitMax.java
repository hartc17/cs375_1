import java.io.*;
import java.util.*;


class BaseballCard{
  String name;
  int price;

  public BaseballCard(String st){
    String[] splited = new String[2];
    splited = st.split(" ");
    name = splited[0];
    price = Integer.parseInt(splited[1]);
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public String printCard(){
    return (name + " " + price);
  }
}

class ProfitMax {

  public static ArrayList<BaseballCard> marketPrices = new ArrayList<BaseballCard>();
  public static ArrayList<BaseballCard> priceList = new ArrayList<BaseballCard>();


  public static int sumCards(ArrayList<Integer> cards){
    int sum = 0;
    for (int i = 0; i < cards.size(); i++){
      sum += cards.get(i);
    }
    return sum;
  }



  public static int calcProfit(ArrayList<BaseballCard> cards, ArrayList<Integer> ints){
    int priceSum = sumCards(ints);
    int marketSum = 0;
    for (int i = 0; i < cards.size(); i++){
      for (int x = 0; x < marketPrices.size(); x++){
        if (cards.get(i).getName().equals(marketPrices.get(x).getName())){
          marketSum += marketPrices.get(x).getPrice();
        }
      }
    }
    return (marketSum - priceSum);
  }

  static ArrayList<ArrayList<Integer>> subsets(ArrayList<Integer> S) {
  	if (S == null)
  		return null;

  	Collections.sort(S);

  	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

  	for (int i = 0; i < S.size(); i++) {
  		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
  		for (ArrayList<Integer> a : result) {
  			temp.add(new ArrayList<Integer>(a));
  		}
  		for (ArrayList<Integer> a : temp) {
  			a.add(S.get(i));
      }
  		ArrayList<Integer> single = new ArrayList<Integer>();
  		single.add(S.get(i));
  		temp.add(single);
  		result.addAll(temp);
  	}
  	result.add(new ArrayList<Integer>());
  	return result;
  }

  static ArrayList<Integer> getIntList(ArrayList<BaseballCard> cards){
    ArrayList<Integer> ret = new ArrayList<Integer>();
    for (int i = 0; i < cards.size(); i++){
      ret.add(cards.get(i).getPrice());
    }
    return ret;
  }

  static ArrayList<BaseballCard> intToCard(ArrayList<Integer> intList, ArrayList<BaseballCard> gertrudeCards){
    ArrayList<BaseballCard> ret = new ArrayList<BaseballCard>();
    for (int i = 0 ; i < intList.size(); i++){
      for (int j = 0; j < gertrudeCards.size(); j++){
        if (intList.get(i) == gertrudeCards.get(j).getPrice())
          ret.add(gertrudeCards.get(j));
        }
    }
    return ret;
  }



  public static void computeMaxProfit(ArrayList<BaseballCard> gertrudeCards, int budget){
    double startTime = (double)System.nanoTime();
    int maxProfit = 0;
    ArrayList<Integer> currentSet = new ArrayList<Integer>();
    ArrayList<Integer> maxSet = new ArrayList<Integer>();
      ArrayList<Integer> intList = getIntList(gertrudeCards);
    if (sumCards(intList) <= budget){
      double endTime = (double)System.nanoTime();
      double totalTime = (endTime - startTime)/1000000000.0;
      System.out.println(gertrudeCards.size() + " " + calcProfit(gertrudeCards, intList) + " " + gertrudeCards.size() + " " + totalTime);
      try {
        FileWriter myWriter = new FileWriter("output.txt", true);
        myWriter.write(gertrudeCards.size() + " " + calcProfit(gertrudeCards, maxSet) + " " + maxSet.size() + " " + totalTime + System.getProperty( "line.separator" ));

        myWriter.close();
        System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      return;
    }

    ArrayList<ArrayList<Integer>> powerSet = subsets(intList);
    for (int i = 0; i < powerSet.size(); i++){
      currentSet = powerSet.get(i);
      ArrayList<BaseballCard> c = intToCard(currentSet, gertrudeCards);
      if (sumCards(currentSet) <= budget){
        //System.out.println(calcProfit(c, currentSet) + " " + maxProfit);
        if (calcProfit(c, currentSet) > maxProfit){
          maxProfit = calcProfit(c, currentSet);
          maxSet = currentSet;
        }
      }
    }
    double endTime = (double)System.nanoTime();
    double totalTime = (endTime - startTime)/1000000000.0;
    System.out.println(gertrudeCards.size() + " " + calcProfit(gertrudeCards, maxSet) + " " + maxSet.size() + totalTime);
    try {
      FileWriter myWriter = new FileWriter("output.txt", true);
      myWriter.write(gertrudeCards.size() + " " + calcProfit(gertrudeCards, maxSet) + " " + maxSet.size() + " " + totalTime + System.getProperty( "line.separator" ));

      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    return;
  }

  public static void setMarketPrices(String file){
    File mfile = new File(file);
    try {
      Scanner scanner = new Scanner(new File("market_price.txt"));
      String st = "";
      String[] splited = new String[2];
      int len = Integer.parseInt(scanner.nextLine());
      //System.out.println(len);
      for (int i = 0; i < len; i++)
        marketPrices.add(new BaseballCard(scanner.nextLine()));
    }
    catch (FileNotFoundException e){
      System.out.println(e);
    }

  }

  public static int[] lenBudget(String line){
    String[] splited = new String[2];
    splited = line.split(" ");
    int len = Integer.parseInt(splited[0]);
    int budget = Integer.parseInt(splited[1]);
    int[] ret = new int[2];
    ret[0] = len;
    ret[1] = budget;
    return ret;
  }

  public static void main(String args[]){

    setMarketPrices(args[0]);
    //System.out.println(marketPrices);

    try {
      Scanner scanner = new Scanner(new File(args[1]));
      ArrayList<String> lines = new ArrayList<String>();
      while (scanner.hasNextLine())
        lines.add(scanner.nextLine());
      //System.out.println(lines);
      int len = 0;
      int budget = 0;
      int[] lb = new int[2];
      String line = "";
      for (int x = 0; x < lines.size(); x++) {
        line = lines.get(x);
        len = 0;
        budget = 0;
        lb = new int[2];
        lb = lenBudget(line);
        len = lb[0];
        budget = lb[1];
        for (int i = 1; i <= len; i++){
          priceList.add(new BaseballCard(lines.get(i)));
          x++;
        }
        //System.out.println(len + " | " + budget + " | " + priceList);
        computeMaxProfit(priceList, budget);
        priceList.clear();

        }
    }
    catch (FileNotFoundException e){
        System.out.println(e);
    }
  }
}
