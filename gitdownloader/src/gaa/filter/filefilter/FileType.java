package gaa.filter.filefilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jcabi.immutable.Array;

public enum FileType {
	JAVA, JAVASCRIPT, PHP, RUBY, PYTHON, C_LIKE, DOCUMENTATION, EXAMPLES, JSON, HTML, CSS, OTHERLANGUAGES, LIBRARY, NOTIDENTIFIED;
	//Usar preferencialmente por ter melhor desempenho
	static String patters[][] = { 
		/*JAVA*/ {"%.java", "%.jsp"},
		/*JAVASCRIPT*/ {"%.js"},
		/*PHP*/ {"%.php", "%.php_","%.phtml"},
		/*RUBY*/ {"%.rb", "%.erb"},
		/*PYTHON*/ {"%.py","%.py_"},
		/*CLIKE*/ {"%.c","%.cpp","%.c++","%.cc","%.cp","%.cxx","%.h","%.h++","%.hh","%.hpp","%.hxx"},
		/*DOCUMENTATION*/ {"docs/%","%/docs/%","documentation/%","%/documentation/%","javadoc/%","%/javadoc/%","man/%"},
		/*EXAMPLES*/ {"examples/%", "%/examples/%"},
		/*JSON*/ {"%.json", "%.json5", "%.jsonld"},
		/*HTML*/ {"%.html", "%.htm", "%.xhtml"},
		/*CSS*/ {"%.css", "%.scss"},
		/*OTHERLANGUAGES*/ {"%.js.coffee"},
		/*LIBRARIES*/ {"%.min.css","%.min.js","vendor/%","vendors/%","extern/%","external/%"}
		};
	
	//Usar apenas quando não for possível usar padrão LIKE
	static String POSIXpatters[][] = { 
		/*JAVA*/ {},
		/*JAVASCRIPT*/ {},
		/*PHP*/ {},
		/*RUBY*/ {},
		/*PYTHON*/ {},
		/*CLIKE*/ {},
		/*DOCUMENTATION*/ {},
		/*EXAMPLES*/ {},
		/*JSON*/ {},
		/*HTML*/ {},
		/*CSS*/ {},
		/*OTHERLANGUAGES*/ {},
		/*LIBRARIES*/ {"(^|/)jquery([^.]*)\\.js$", "(^|/)jquery\\-\\d\\.\\d+(\\.\\d+)?\\.js$"}
		};
	
	public static List<String> getPatterns(FileType fTypes){
		String result[] = null;
		if (fTypes == FileType.JAVA)
			result = patters[0];
		if (fTypes == FileType.JAVASCRIPT)
			result = patters[1];
		if (fTypes == FileType.PHP)
			result = patters[2];
		if (fTypes == FileType.RUBY)
			result = patters[3];
		if (fTypes == FileType.PYTHON)
			result = patters[4];
		if (fTypes == FileType.C_LIKE)
			result = patters[5];
		if (fTypes == FileType.DOCUMENTATION)
			result = patters[6];
		if (fTypes == FileType.EXAMPLES)
			result = patters[7];
		if (fTypes == FileType.JSON)
			result = patters[8];
		if (fTypes == FileType.HTML)
			result = patters[9];
		if (fTypes == FileType.CSS)
			result = patters[10];
		if (fTypes == FileType.OTHERLANGUAGES)
			result = patters[11];
		if (fTypes == FileType.LIBRARY)
			result = patters[12];
		return Arrays.asList(result);
	}
	
	public static List<String> getPosixPatterns(FileType fTypes){
		String result[] = null;
		if (fTypes == FileType.JAVA)
			result = POSIXpatters[0];
		if (fTypes == FileType.JAVASCRIPT)
			result = POSIXpatters[1];
		if (fTypes == FileType.PHP)
			result = POSIXpatters[2];
		if (fTypes == FileType.RUBY)
			result = POSIXpatters[3];
		if (fTypes == FileType.PYTHON)
			result = POSIXpatters[4];
		if (fTypes == FileType.C_LIKE)
			result = POSIXpatters[5];
		if (fTypes == FileType.DOCUMENTATION)
			result = POSIXpatters[6];
		if (fTypes == FileType.EXAMPLES)
			result = POSIXpatters[7];
		if (fTypes == FileType.JSON)
			result = POSIXpatters[8];
		if (fTypes == FileType.HTML)
			result = POSIXpatters[9];
		if (fTypes == FileType.CSS)
			result = POSIXpatters[10];
		if (fTypes == FileType.OTHERLANGUAGES)
			result = POSIXpatters[11];
		if (fTypes == FileType.LIBRARY)
			result = POSIXpatters[12];
		return Arrays.asList(result);
	}
	
}
