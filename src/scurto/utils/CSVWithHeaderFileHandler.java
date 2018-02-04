package scurto.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class to manipulate table data on a structured csv file
 * @author Sergio Curto
 *
 */
public class CSVWithHeaderFileHandler {
	
	public static final String DEFAULT_DELIMITER = "\t";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_ENCODING_EXCEL2011 = "ISO-8859-1";
	
	final String delimiter;
	final String encoding;
	List<String> columns = new ArrayList<String>();
	List<Map<String, String>> lines = new ArrayList<Map<String,String>>();
	
	public CSVWithHeaderFileHandler() {
		this.delimiter = DEFAULT_DELIMITER;
		this.encoding = DEFAULT_ENCODING;
	}
	
	public CSVWithHeaderFileHandler(String delimiter) {
		this.delimiter = delimiter;
		this.encoding = DEFAULT_ENCODING;
	}
	
	public CSVWithHeaderFileHandler(String delimiter, String encoding) {
		this.delimiter = delimiter;
		this.encoding = encoding;
	}
	
	public void loadCSVWithHeaderFile(File f) throws IOException {
		InputStreamReader converter = new InputStreamReader(new FileInputStream(f));
		BufferedReader is = null; 
		
		try {
			is = new BufferedReader(converter);

			String line = null;
			int lineNumber = 0;
			while((line = is.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if(!line.isEmpty()){
					List<String> lineInformation = processRawCSVLine(line);
					if(lineNumber == 1) {
						setColumns(lineInformation);
					} else {
						HashMap<String, String> lineHashMap = new HashMap<String, String>();

						if(lineInformation.size() > columns.size()) {
							throw new IOException("The number of columns on the csv file line '"+lineNumber+"' is bigger then the number of columns on the header of the file!");
						}

						for(int i=0; i<lineInformation.size() && i<columns.size(); i++) {
							lineHashMap.put(columns.get(i), lineInformation.get(i));
						}
						lines.add(lineHashMap);
					}
				}
			}
		} finally {
			if(is!=null) {
				is.close();
				converter.close();
			}
		}
	}
	
	private List<String> processRawCSVLine(String line) {
		String[] lineValues = line.split(this.delimiter);
		ArrayList<String> result = new ArrayList<String>();
		
		for(String value : lineValues) {
			result.add(value);
		}
		
		result.trimToSize();
		return result;
	}

	public void writeCSVWithHeaderFile(File f) throws IOException {
		f.getParentFile().mkdirs();
		f.createNewFile();
		FileOutputStream os = new FileOutputStream(f);
		OutputStreamWriter writer = null;
		
		try {
			writer = new OutputStreamWriter(os, encoding);
			
			writeCSVHeader(writer, delimiter, columns);
			
			for(Map<String, String> lineMap  : this.lines) {
				writeCVSLine(writer, delimiter, columns, lineMap);
			}
		} finally {
			if(writer != null) {
				writer.close();
			}
			os.close();
		}
	}

	private static void writeCVSLine(OutputStreamWriter writer,
			String delimiter, List<String> columns, Map<String, String> line) throws IOException {
		boolean first = true;
		for(String column : columns) {
			if(first) {
				first = false;
			} else {
				writer.append(delimiter);				
			}
			String tableRow = line.get(column);
			
			writer.append(tableRow == null? "":tableRow);
		}
		writer.append("\n");
	}

	private static void writeCSVHeader(OutputStreamWriter writer, String delimiter, List<String> columns) throws IOException {
		boolean first = true;
		for(String column : columns) {
			if(first) {
				first = false;
			} else {
				writer.append(delimiter);				
			}
			
			writer.append(column);
		}
		writer.append("\n");
	}

	/**
	 * Adds a column to the csv representation
	 * @param name name of the column to add
	 * @return false if the column already existed
	 */
	public boolean addColumn(String name){
		boolean result = false;
		if(!columns.contains(name)){
			columns.add(name);
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Adds a line represented by an ordered list aligned with the columns. Empty list aren't added. 
	 * @param list
	 */
	public void addLine(List<String> list) {
		Map<String, String> csvLine = new HashMap<String, String>();
		
		Iterator<String> it = list.iterator();
		boolean addedElements = false;
		for(String column : columns) {
			if(!it.hasNext()){
				break;
			}
			csvLine.put(column, it.next());
			addedElements = true;
		}
		
		if(addedElements) {
			this.lines.add(csvLine);
		}
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public List<Map<String, String>> getLines() {
		return lines;
	}

	public void setLines(List<Map<String, String>> lines) {
		this.lines = lines;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for(String column : this.columns){
			sb.append("\t|> ");
			sb.append(column);
			sb.append(" <|");
		}
		sb.append("\n");
		
		for(Map<String,String> line : this.lines){
			for(String column : this.columns) {
				String lineValue = line.get(column);
				
				sb.append("\t|> ");
				sb.append(lineValue);
				sb.append(" <|");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
