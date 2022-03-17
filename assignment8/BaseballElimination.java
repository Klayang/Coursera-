package assignment8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination 
{
	private int numberOfTeams;
	private HashMap<String, Integer> teams = new HashMap<>();
	private int[] wins, losses, remainings;
	private int[][] schedule;
	private boolean[] isEliminated;
	private ArrayList<Set<String>> set = new ArrayList<>(5);
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		numberOfTeams = in.readInt();
		isEliminated = new boolean[numberOfTeams];
		wins = new int[numberOfTeams];
		losses = new int[numberOfTeams];
		remainings = new int[numberOfTeams];
		schedule = new int[numberOfTeams][numberOfTeams];
		for(int i = 0; i < numberOfTeams; i++)
		{
			String nameOfTheTeam = in.readString();
			teams.put(nameOfTheTeam, i);
			wins[i] = in.readInt();
			losses[i] = in.readInt();
			remainings[i] = in.readInt();
			for(int j = 0; j < numberOfTeams; j++)
				schedule[i][j] = in.readInt();
		}
		checkElimination();
	}
	public int numberOfTeams() {return numberOfTeams;}
	public Iterable<String> teams()
	{
		return teams.keySet();
	}
	public int wins(String team)                      // number of wins for given team
	{
		if(!teams.containsKey(team)) throw new IllegalArgumentException();
		return wins[teams.get(team)];
	}
	public int losses(String team)                    // number of losses for given team
	{
		if(!teams.containsKey(team)) throw new IllegalArgumentException();
		return losses[teams.get(team)];
	}
	public int remaining(String team)                 // number of remaining games for given team
	{
		if(!teams.containsKey(team)) throw new IllegalArgumentException();
		return remainings[teams.get(team)];
	}
	public int against(String team1, String team2)
	{
		if(!teams.containsKey(team1) || !teams.containsKey(team2)) throw new IllegalArgumentException();
		return schedule[teams.get(team1)][teams.get(team2)];
	}
	public boolean isEliminated(String team)              // is given team eliminated?
	{
		if(!teams.containsKey(team)) throw new IllegalArgumentException();
		return isEliminated[teams.get(team)];
	}
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if(!teams.containsKey(team)) throw new IllegalArgumentException();
		return set.get(teams.get(team));
	}
	private void checkElimination()
	{
		int most_wins = 0, team_with_most_wins = 0;
		for(int i = 0; i < numberOfTeams; i++)
			if(wins[i] > most_wins)
			{
				most_wins = wins[i];
				team_with_most_wins = i;
			}
		for(int i = 0; i < numberOfTeams; i++)
			if(wins[i] + remainings[i] < most_wins) 
			{
				isEliminated[i] = true;
				Set<String> s = new HashSet<>();
				for(String team: teams.keySet())
					if(teams.get(team) == team_with_most_wins)
					{
						s.add(team);
						break;
					}
				set.add(s);
			}
			else
			{
				int remaining_games = 0;
				for(int m = 0; m < numberOfTeams; m++)
				{
					if(m == i)continue;
					for(int n = 0; n < numberOfTeams; n++)
					{
						if(n == i)continue;
						remaining_games += schedule[m][n];
					}
				}
				remaining_games /= 2;
				int sumIndex = ((numberOfTeams - 2) * (numberOfTeams - 1)) / 2 + numberOfTeams + 1;
				FlowNetwork flowNetwork = new FlowNetwork(sumIndex + 1);
				int most_possible_wins = wins[i] + remainings[i];
				int indexOfVertex = 1;
				for(int m = 0; m < numberOfTeams; m++)
				{
					if(m == i) continue;
					for(int n = m + 1; n < numberOfTeams; n++)
					{
						if(n == i) continue;
						FlowEdge edge = new FlowEdge(0, indexOfVertex, schedule[m][n]);
						flowNetwork.addEdge(edge);
						FlowEdge e1 = new FlowEdge(indexOfVertex, sumIndex - numberOfTeams + m, Double.POSITIVE_INFINITY);
						flowNetwork.addEdge(e1);
						FlowEdge e2 = new FlowEdge(indexOfVertex, sumIndex - numberOfTeams + n, Double.POSITIVE_INFINITY);
						flowNetwork.addEdge(e2);
						indexOfVertex++;
					}
				}
				for(int t = 0; t < numberOfTeams; t++)
				{
					if(t == i) continue;
					FlowEdge e = new FlowEdge(sumIndex - numberOfTeams + t, sumIndex, most_possible_wins - wins[t]);
					flowNetwork.addEdge(e);
				}
				FordFulkerson f = new FordFulkerson(flowNetwork, 0, sumIndex);
				if(f.value() < remaining_games) isEliminated[i] = true;
				Set<String> s = new HashSet<>(); 
				for(int k = 0; k < numberOfTeams; k++)
				{
					if(k == i) continue;
					if(f.inCut(sumIndex - numberOfTeams + k))
					{
						for(String team: teams.keySet())
						{
							if(teams.get(team) == k) 
							{
								s.add(team);
								break;
							}
						}
					}
				}
				set.add(s);
			}
	}
	public static void main(String[] args) 
	{
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) 
	        if (division.isEliminated(team)) 
	        {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) 
	                StdOut.print(t + " ");
	            StdOut.println("}");
	        }
	        else StdOut.println(team + " is not eliminated");
	}
}
