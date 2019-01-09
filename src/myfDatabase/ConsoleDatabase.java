package myfDatabase;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class ConsoleDatabase {

	private static MysqlDataSource dataSource;
	private static Connection conn;
	private static Statement stmt;
	
	private static int id;
	private static DBValueBag vbag;
	
	private enum OperationType {
		Refresh,
		Add,
		Delete,
		Search
	};
	
	public static void main(String[] args) {
		if (!InitDatabase()) {
			System.out.println("Cannot connect to database. Exiting...");
			return;
		}
		UpdateRecord(OperationType.Refresh);
		Scanner s = new Scanner(System.in);
		s.useDelimiter("\n");
		while (true) {
			String input = s.next().trim();
			if (input.equals("exit")) {
				break;
			}
			String[] strs = input.split(" ");
			String op = strs[0].toLowerCase();
			switch (op) {
			case "show": //show table; show 1;
				if (strs.length < 2) {
					System.out.println("Syntax error.");
					break;
				}
				if (strs[1].toLowerCase().equals("table")) {
					UpdateRecord(OperationType.Refresh);
				}
				else {
					try {
						id = Integer.parseInt(strs[1]);
						UpdateRecord(OperationType.Search);
					}
					catch (Exception e) {
						System.out.println("Syntax error.");
					}
				}
				break;
			case "add": //add 1; Yufei Miao; 18; 19991106; Shanghai, China
				strs = input.substring(3, input.length()).trim().split(";");
				if (strs.length != 5) {
					System.out.println("Syntax error.");
					break;
				}
				vbag = new DBValueBag(false, strs[0].trim(), strs[1].trim(), strs[2].trim(), strs[3].trim(), strs[4].trim());
				UpdateRecord(OperationType.Add);
				break;
			case "delete":
				String strid = strs[1];
				try {
					id = Integer.parseInt(strid);
					UpdateRecord(OperationType.Delete);
				}
				catch (Exception e) {
					System.out.println("Syntax error.");
					break;
				}
				break;
			default:
				System.out.println("Unknown command.");
				break;
			}
		}
		s.close();
	}

	private static boolean UpdateRecord(OperationType type) {
		if (conn == null) {
			if (!InitDatabase()) {
				System.out.println("Cannot connect to database!");
				return false;
			}
		}
		try {
			if (stmt == null) {
				stmt = conn.createStatement();
			}
			
			switch (type) {
				case Refresh:
					ResultSet rsr = stmt.executeQuery("SELECT * FROM `tb_employee`;");
					System.out.format("%4s", "ID");
					System.out.format("%12s", "Name");
					System.out.format("%4s", "Age");
					System.out.format("%10s", "Birthday");
					System.out.format("%18s", "Address");
					System.out.println();
					while (rsr.next()) {
						System.out.format("%4s", rsr.getString("eid"));
						System.out.format("%12s", rsr.getString("ename"));
						System.out.format("%4s", rsr.getString("eage"));
						System.out.format("%10s", rsr.getString("ebirthday").split(" ")[0].replace("-", ""));
						System.out.format("%18s", rsr.getString("eaddress"));
						System.out.println();
					};
					rsr.close();
					break;
				case Add:
					String querya = "INSERT INTO `tb_employee` (`eid`, `ename`, `eage`, `ebirthday`, `eaddress`) VALUES (\"" +
							vbag.rID + "\", \"" +
							vbag.rName + "\", \"" +
							vbag.rAge + "\", \"" +
							vbag.rBirthday + "\", \"" +
							vbag.rAddress + "\");";
					System.out.println(querya);
					stmt.execute(querya);
					break;
				case Delete:
					String queryd = "DELETE FROM `tb_employee` WHERE `eid`=" + id + ";";
					System.out.println(queryd);
					stmt.execute(queryd);
					break;
				case Search:
					ResultSet rss = stmt.executeQuery("SELECT * FROM `tb_employee` WHERE `eid`=" + id + ";");
					System.out.format("%4s", "ID");
					System.out.format("%12s", "Name");
					System.out.format("%4s", "Age");
					System.out.format("%10s", "Birthday");
					System.out.format("%18s", "Address");
					System.out.println();
					if (!rss.next()) {
						System.out.println("No record matches.");
					}
					else {
						rss.previous();
						while (rss.next()) {
							System.out.format("%4s", rss.getString("eid"));
							System.out.format("%12s", rss.getString("ename"));
							System.out.format("%4s", rss.getString("eage"));
							System.out.format("%10s", rss.getString("ebirthday").split(" ")[0].replace("-", ""));
							System.out.format("%18s", rss.getString("eaddress"));
							System.out.println();
						};
					}
					rss.close();
					break;
				default:
					break;
			}
			
		} catch (Exception e) {
			CloseDatabase();
			System.out.println("Cannot communicate with database or SQL error!\n" + e.getLocalizedMessage());
			return false;
		}
		return true;
	}
	
	private static boolean InitDatabase() {
		CloseDatabase();
		if (dataSource == null) {
			/*
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (Exception e) {
				return false;
			}
			*/
			dataSource = new MysqlDataSource();
		}
		else {
			dataSource.reset();
		}
		dataSource.setUser("myfdbuser");
		dataSource.setPassword("myfdbpwd");
		dataSource.setServerName("localhost");
		dataSource.setDatabaseName("db_employee");
		try {
			conn = dataSource.getConnection();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static boolean CloseDatabase() {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
