import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.PrimitiveIterator.OfDouble;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.PSource;

class Letter
{
	String letter;
	int power;
	public Letter()
	{
		letter = "";
		power = 0;
	}
	public Letter(String x)
	{
		letter = x;
		power = 1;
	}
	public Letter(String x,int num)
	{
		letter = x;
		power = num;
	}
	public void setNull()
	{
		letter = "";
		power = 0;
	}
}

class MulParts		// like 2*3 2 2*x*y
{
	public List<Letter> parts;
	public int con;		//the const of one MulParts
	public MulParts(String input)
	{
		con = 1;
		parts = new ArrayList<Letter>();
		String letterAndNumber[] = input.split("\\*");
		for (String x : letterAndNumber)
		{
			if (isDigital(x))
			{
				int tempCon = Integer.valueOf(x);
				con *= tempCon;
			}
			else
			{
				int pos = haveLetter(x);
				if (pos != -1)
				{
					parts.set(pos, new Letter(parts.get(pos).letter,parts.get(pos).power+1));
				}
				else
				{
					parts.add(new Letter(x));
				}
			}
		}
	}
	private int haveLetter(String s) 
	{
		for (int i = 0; i < parts.size(); i++) 
		{
			if (parts.get(i).letter.equals(s))
			{
				return i;
			}
		}
		return -1;
	}
	public static boolean isDigital(String input)
	{
		Pattern pattern = Pattern.compile("[0-9]+");
		return pattern.matcher(input).matches();
		
	}
	public void calcValue(Letter l,int value)
	{
		String letter = l.letter;
		for (int i=0; i< parts.size(); i++)
		{
			Letter nowPos = parts.get(i);
			if (nowPos.letter.equals(letter))
			{
				if (nowPos.power > 0 )
				{
					con = con * value * nowPos.power;
				}
				parts.remove(i);
				i = i - 1;
			}
		}
	}
	public void calcDerivative(String x)
	{
		int pos = haveLetter(x);
		if (pos == -1)
		{
			con = 0;
			return ;
		}
		Letter now = parts.get(pos);
		con = now.power * con;
		if (now.power == 1)
		{
			parts.remove(pos);
		}
		else
		{
			Letter newLetter = new Letter(now.letter,now.power-1);
			parts.set(pos, newLetter);
		}
		
	}
	public String transToString()
	{
		String output = "";
		if (con == 0)
		{
			return "";
		}
		if (con != 0)
		{
			output += con;
		}
		int l = parts.size();
		for (int i=0 ; i < l; i++)
		{
			for (int j = 0; j < parts.get(i).power; j++)
			{
				output = output + "*" + parts.get(i).letter;
			}
		}
//		System.out.println(output);
		return output;
	}
}

class Polymerization
{
	List<MulParts> items;
	public Polymerization(String inputString)
	{
		items = new ArrayList<MulParts>();
		String[] stringParts;
		stringParts = inputString.split("\\+");
		for (String s : stringParts)
		{
//			System.out.println(s);
			MulParts mulParts = new MulParts(s);
			items.add(mulParts);
//			mulParts.transToString();
//			mulParts.calcValue(new Letter("x"), 3);
//			mulParts.transToString();
		}
	}
	public void simplify(String x,int value)
	{
		for (int i = 0; i < items.size(); i++)
		{
			MulParts mulParts = items.get(i);
			mulParts.calcValue(new Letter(x), value);
			items.set(i,mulParts);
		}
	}
	public void derivative(String x)
	{
		for (int i = 0; i < items.size(); i++)
		{
			MulParts mulParts = items.get(i);
			mulParts.calcDerivative(x);
			items.set(i,mulParts);
		}
	}
	public void expression(String inputString)
	{
		items = new ArrayList<MulParts>();
		String[] stringParts;
		stringParts = inputString.split("\\+");
		for (String s : stringParts)
		{
//			System.out.println(s);
			MulParts mulParts = new MulParts(s);
			items.add(mulParts);
//			mulParts.transToString();
//			mulParts.calcValue(new Letter("x"), 3);
//			mulParts.transToString();
		}
	}
	public String transString()
	{
		String output = "";
		output = items.get(0).transToString();
		for (int i = 1; i < items.size(); i++) 
		{
			if (items.get(i).transToString().equals(""))
			{
				continue;
			}
			output = output + "+" + items.get(i).transToString();
		}
		return output;
	}
	public void print()
	{
		System.out.println(this.transString());
	}
}

class Input
{
	static String simplifyPattern = "!simplify(( [a-z]=[0-9]+)+)";
	static String derivativePattern = "!d/d([a-z])";
	static String varifyPattern = "(([a-z]|([0-9]+))(\\*([a-z]|([0-9]+)))*)(\\+([a-z]|([0-9]+))(\\*([a-z]|([0-9]+)))*)*";
	static String getValuePattern = " ([a-z])=([0-9]+)";
	Scanner scanner;
	Polymerization polymerization;
	public Input()
	{
		scanner = new Scanner(System.in);
	}
	public static int getChoice(String inputString)
	{
		int choice  = 0;
		Matcher matcher = Pattern.compile(simplifyPattern).matcher(inputString);
		if (matcher.matches())
		{
			choice = 1;
		}
		matcher = Pattern.compile(derivativePattern).matcher(inputString);
		if (matcher.matches())
		{
			choice = 2;
		}
		matcher = Pattern.compile(varifyPattern).matcher(inputString);
		if (matcher.matches())
		{
			choice = 3;
		}
		return choice;
	}
	public void choose(int choice,String inputString)
	{
		if (choice == 0)
		{
			System.out.println("Error");
		}
		if (choice == 1)
		{
			if (polymerization == null)
			{
				System.out.println("Wrong order");
				return;
			}
			check(inputString);
		}
		if (choice == 2)
		{
			if (polymerization == null)
			{
				System.out.println("Wrong order");
				return;
			}
			Matcher matcher = Pattern.compile(derivativePattern).matcher(inputString);
			String x = "";
			if (matcher.find())
			{
				x = matcher.group(1);
			}
			polymerization.derivative(x);
		}
		if (choice == 3)
		{
			polymerization = new Polymerization(inputString);
		}
		output();
	}
	public void output()
	{
		polymerization.print();
	}
	public int check(String inpuString) {
		Matcher matcher = Pattern.compile(simplifyPattern).matcher(inpuString);
		if (matcher.matches())
		{
			String valueString = matcher.group(1);
			Matcher matcher2 = Pattern.compile(getValuePattern).matcher(valueString);
			while (matcher2.find()) {
				String x = matcher2.group(1);
				int value = Integer.valueOf(matcher2.group(2));
				polymerization.simplify(x, value);
			}
		}
		return 0;
	}
	public String getInput()
	{
		String ansString = "";
		String inputString = scanner.nextLine();
		int choice = check(inputString);
		
		return ansString;
	}
}

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputString;
		Polymerization polymerization;
		Scanner input = new Scanner(System.in);
		Input ii = new Input();
		while ((inputString = input.nextLine())!="")
		{
//			polymerization = new Polymerization(inputString);
//			System.out.println(polymerization.transString());
//			polymerizaion .simplify("x", 3);
//			polymerization.print();
//			polymerization.derivative("x");
//			polymerization.print();
			ii.choose(ii.getChoice(inputString), inputString);
		}
		input.close();
	}

}
