package loggingTutorial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.*;

/**
 * The purpose of this class is to read and merge financial transactions, and print a summary:
 * - total amount 
 * - highest/lowest amount
 * - number of transactions 
 * @author jens dietrich
 */
public class MergeTransactions {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	private static NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());
	
	// Loggers
	static Logger filelogger = Logger.getLogger("FILE");
	static Logger transactionlogger = Logger.getLogger("TRANSACTIONS");

		
	public static void main(String[] args) {
	
		BasicConfigurator.configure();
		// Set file logger level to info - info is the lowest level priority required by this logger
		filelogger.setLevel(Level.INFO);
		// Set file logger level to debug - debug is the lowest level priority required by this logger
		transactionlogger.setLevel(Level.DEBUG);
		
		try {
			// File logger appenders (File & Console)
			filelogger.addAppender(
				new org.apache.log4j.FileAppender(
					new org.apache.log4j.SimpleLayout(), "logs.txt"
				)
			);
			filelogger.addAppender(
					new org.apache.log4j.ConsoleAppender(
						new org.apache.log4j.SimpleLayout())
				);
			
			// Transaction logger appender (Console Only)
			transactionlogger.addAppender(
					new org.apache.log4j.ConsoleAppender(
							new org.apache.log4j.SimpleLayout())
					);
		} catch (IOException e) {
			// Could not add appenders
			filelogger.error("Could not add appenders");
		}
		
		List<Purchase> transactions = new ArrayList<Purchase>();
		
		// read data from 4 files
		readData("src/resources/transactions1.csv", transactions);
		readData("src/resources/transactions2.csv", transactions);
		readData("src/resources/transactions3.csv", transactions);
		readData("src/resources/transactions4.csv", transactions);
		
		// print some info for the user
		transactionlogger.info("" + transactions.size() + " transactions imported");
		transactionlogger.info("total value: " + CURRENCY_FORMAT.format(computeTotalValue(transactions)));
		transactionlogger.info("max value: " + CURRENCY_FORMAT.format(computeMaxValue(transactions)));
	}
	
	private static double computeTotalValue(List<Purchase> transactions) {
		double v = 0.0;
		for (Purchase p:transactions) {
			v = v + p.getAmount();
		}
		return v;
	}
	
	private static double computeMaxValue(List<Purchase> transactions) {
		double v = 0.0;
		for (Purchase p:transactions) {
			v = Math.max(v,p.getAmount());
		}
		return v;
	}

	// read transactions from a file, and add them to a list
	private static void readData(String fileName, List<Purchase> transactions) {
		
		File file = new File(fileName);
		String line = null;
		
		// print info for user
		filelogger.info("import data from " + fileName);
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine())!=null) {
				String[] values = line.split(",");
				Purchase purchase = new Purchase(
						values[0],
						Double.parseDouble(values[1]),
						DATE_FORMAT.parse(values[2])
				);
				transactions.add(purchase);
				
				// this is for debugging only
				transactionlogger.debug("imported transaction " + purchase);
			} 
		}
		catch (FileNotFoundException x) {
			// print warning
			filelogger.warn("file " + fileName + " does not exist - skip");
		}
		catch (IOException x) {
			// print error message and details
			filelogger.error("problem reading file " + fileName);
		}
		// happens if date parsing fails
		catch (ParseException x) { 
			// print error message and details
			filelogger.error("cannot parse date from string - please check whether syntax is correct: " + line);
		}
		// happens if double parsing fails
		catch (NumberFormatException x) {
			// print error message and details
			filelogger.error("cannot parse double from string - please check whether syntax is correct: " + line);
		}
		catch (Exception x) {
			// any other exception 
			// print error message and details
			filelogger.error("exception reading data from file " + fileName + ", line: " + line);
		}
		finally {
			try {
				if (reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				// print error message and details
				filelogger.error("cannot close reader used to access " + fileName);
			}
		}
	}

}
