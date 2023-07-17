package com.cognixia.jump.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.cognixia.jump.model.Expense;
import com.cognixia.jump.model.MonthlyBudget;
import com.cognixia.jump.model.User;

public class CsvUtil {
	
	
	enum ExpenseHeaders {
		category, amount, date
	}
	
	enum BudgetHeaders {
		year, january, february, march, april, may, june, july, august, september, october, november, december, total
	}
	
	public static void writeExpensesToCsv(User user) throws IOException {
		String expenseCsv = "./expense_" + user.getName().toLowerCase() + "_" + System.currentTimeMillis() + ".csv";
		try (
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(expenseCsv));
			
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader(ExpenseHeaders.class));
		) {
			for (Expense e : user.getExpenses()) {
				csvPrinter.printRecord(e.getCategory().toString(), Double.toString(e.getAmount()), e.getDate().toString());
			}
			csvPrinter.flush();
			writer.flush();
			csvPrinter.close();
			writer.close();
		}
	}
	
	public static void writeBudgetsToCsv(User user) throws IOException {
		String expenseCsv = "./budget_" + user.getName().toLowerCase() + "_" + System.currentTimeMillis() + ".csv";
		try (
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(expenseCsv));
			
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader(BudgetHeaders.class));
		) {
			for (MonthlyBudget b : user.getBudget()) {
				csvPrinter.printRecord(b.getYear(), Double.toString(b.getJanuary()), Double.toString(b.getFebruary()),
						Double.toString(b.getMarch()), Double.toString(b.getApril()), Double.toString(b.getMay()),
						Double.toString(b.getJune()), Double.toString(b.getJuly()), Double.toString(b.getAugust()),
						Double.toString(b.getSeptember()), Double.toString(b.getOctober()), Double.toString(b.getNovember()),
						Double.toString(b.getDecember()), Double.toString(b.getYearlyBudget()));
			}
			csvPrinter.flush();
			writer.flush();
			csvPrinter.close();
			writer.close();
		}
	}

}
