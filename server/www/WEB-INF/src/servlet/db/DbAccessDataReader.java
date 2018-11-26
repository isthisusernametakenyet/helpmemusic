package servlet.db;

import java.io.*;
import java.util.*;

public class DbAccessDataReader {

	public DbAccessData readDbAccessData(String path) {
	
		DbAccessData data = null;
		List<String> lines = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			for (String line; (line = br.readLine()) != null; ) {
				lines.add(line);
			}
			data = new DbAccessData(
					lines.get(0),
					lines.get(1),
					lines.get(2),
					lines.get(3)
			);
		} catch (IOException ioe) {
			System.err.println("unable to read access data " + ioe.getMessage());
			System.exit(1);
		}
		return data;
	}
}
