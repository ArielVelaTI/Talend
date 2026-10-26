
package capacitacion_talend.load_dim_customer_0_1;

import routines.Numeric;
import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.MDM;
import routines.StringHandling;
import routines.Relational;
import routines.TalendDate;
import routines.Mathematical;
import routines.SQLike;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;
 






@SuppressWarnings("unused")

/**
 * Job: load_dim_customer Purpose: Cargar y actualizar la dimensión de clientes de Olist<br>
 * Description: Job encargado de leer el archivo de clientes, validar los datos, realizar las transformaciones necesarias y cargarlos en la tabla dim_customer de SingleStore. Registra los datos rechazados para su auditoría. <br>
 * @author Chango, Kevin
 * @version 8.0.1.20260619_1127-patch
 * @status 
 */
public class load_dim_customer implements TalendJob {
	static {System.setProperty("TalendJob.log", "load_dim_customer.log");}

	

	
	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(load_dim_customer.class);
	

static {
		 System.setProperty("talend.component.record.nullable.check", "true");
         String javaUtilLoggingConfigFile = System.getProperty("java.util.logging.config.file");
         if (javaUtilLoggingConfigFile == null) {
             setupDefaultJavaUtilLogging();
         }
}

/**
* This class replaces the default {@code System.err} stream used by Java Util Logging (JUL).
* You can use your own configuration through the
* {@code java.util.logging.config.file} system property, enabling you to specify an external
* logging configuration file for tailored logging setup.
*/
public static class StandardConsoleHandler extends java.util.logging.StreamHandler {
     public StandardConsoleHandler() {
         // Set System.out as default log output stream
         super(System.out, new java.util.logging.SimpleFormatter());
     }

     /**
      * Publish a {@code LogRecord}.
      * The logging request was made initially to a {@code Logger} object,
      * which initialized the {@code LogRecord} and forwarded it here.
      *
      * @param  record  description of the log event. A null record is
      *                 silently ignored and is not published
      */
      @Override
      public void publish(java.util.logging.LogRecord record) {
            super.publish(record);
            flush();
      }

      /**
      * Override {@code StreamHandler.close} to do a flush but not
      * to close the output stream.  That is, we do <b>not</b>
      * close {@code System.out}.
      */
      @Override
      public void close() {
            flush();
      }
}


protected static void setupDefaultJavaUtilLogging() {
      java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();

      // Get the root logger
      java.util.logging.Logger rootLogger = logManager.getLogger("");

      // Remove existing handlers to set standard console handler only
      java.util.logging.Handler[] handlers = rootLogger.getHandlers();
      for (java.util.logging.Handler handler : handlers) {
            rootLogger.removeHandler(handler);
      }

      rootLogger.addHandler(new StandardConsoleHandler());
      rootLogger.setLevel(java.util.logging.Level.INFO);
}

protected static void logIgnoredError(String message, Throwable cause) {
       log.error(message, cause);

}


	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}
	
	private final static String defaultCharset = java.nio.charset.Charset.defaultCharset().name();

	
	private final static String utf8Charset = "UTF-8";

    // volatile is needed for OSGi: the thread that calls the blueprint destroy method is not the same that sets the values
	public static volatile String taskExecutionId = null;
	public static volatile String jobExecutionId = null;
	public static volatile boolean sealCounters = false;

    public static final java.util.Set<Thread> threadList = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());	

	//contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String,String> propertyTypes = new java.util.HashMap<>();
		
		public PropertiesWithType(java.util.Properties properties){
			super(properties);
		}
		public PropertiesWithType(){
			super();
		}
		
		public void setContextType(String key, String type) {
			propertyTypes.put(key,type);
		}
	
		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}	
	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();
		

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties){
			super(properties);
		}
		public ContextProperties(){
			super();
		}

		public void synchronizeContext(){
			
			if(DB_HOST != null){
				
					this.setProperty("DB_HOST", DB_HOST.toString());
				
			}
			
			if(DB_NAME != null){
				
					this.setProperty("DB_NAME", DB_NAME.toString());
				
			}
			
			if(DB_PASSWORD != null){
				
					this.setProperty("DB_PASSWORD", DB_PASSWORD.toString());
				
			}
			
			if(DB_PORT != null){
				
					this.setProperty("DB_PORT", DB_PORT.toString());
				
			}
			
			if(DB_USER != null){
				
					this.setProperty("DB_USER", DB_USER.toString());
				
			}
			
			if(ENV_NAME != null){
				
					this.setProperty("ENV_NAME", ENV_NAME.toString());
				
			}
			
			if(FILE_OLIST_CUSTOMERS != null){
				
					this.setProperty("FILE_OLIST_CUSTOMERS", FILE_OLIST_CUSTOMERS.toString());
				
			}
			
			if(FILE_OLIST_ORDER_ITEMS != null){
				
					this.setProperty("FILE_OLIST_ORDER_ITEMS", FILE_OLIST_ORDER_ITEMS.toString());
				
			}
			
			if(FILE_OLIST_ORDER_PAYMENTS != null){
				
					this.setProperty("FILE_OLIST_ORDER_PAYMENTS", FILE_OLIST_ORDER_PAYMENTS.toString());
				
			}
			
			if(FILE_OLIST_ORDERS != null){
				
					this.setProperty("FILE_OLIST_ORDERS", FILE_OLIST_ORDERS.toString());
				
			}
			
			if(FILE_OLIST_PRODUCTS != null){
				
					this.setProperty("FILE_OLIST_PRODUCTS", FILE_OLIST_PRODUCTS.toString());
				
			}
			
			if(LOAD_SOURCE_TAG != null){
				
					this.setProperty("LOAD_SOURCE_TAG", LOAD_SOURCE_TAG.toString());
				
			}
			
			if(PIPELINE_NAME != null){
				
					this.setProperty("PIPELINE_NAME", PIPELINE_NAME.toString());
				
			}
			
		}
		
		//if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if(NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

public String DB_HOST;
public String getDB_HOST(){
	return this.DB_HOST;
}
public String DB_NAME;
public String getDB_NAME(){
	return this.DB_NAME;
}
public String DB_PASSWORD;
public String getDB_PASSWORD(){
	return this.DB_PASSWORD;
}
public String DB_PORT;
public String getDB_PORT(){
	return this.DB_PORT;
}
public String DB_USER;
public String getDB_USER(){
	return this.DB_USER;
}
public String ENV_NAME;
public String getENV_NAME(){
	return this.ENV_NAME;
}
public String FILE_OLIST_CUSTOMERS;
public String getFILE_OLIST_CUSTOMERS(){
	return this.FILE_OLIST_CUSTOMERS;
}
public String FILE_OLIST_ORDER_ITEMS;
public String getFILE_OLIST_ORDER_ITEMS(){
	return this.FILE_OLIST_ORDER_ITEMS;
}
public String FILE_OLIST_ORDER_PAYMENTS;
public String getFILE_OLIST_ORDER_PAYMENTS(){
	return this.FILE_OLIST_ORDER_PAYMENTS;
}
public String FILE_OLIST_ORDERS;
public String getFILE_OLIST_ORDERS(){
	return this.FILE_OLIST_ORDERS;
}
public String FILE_OLIST_PRODUCTS;
public String getFILE_OLIST_PRODUCTS(){
	return this.FILE_OLIST_PRODUCTS;
}
public String LOAD_SOURCE_TAG;
public String getLOAD_SOURCE_TAG(){
	return this.LOAD_SOURCE_TAG;
}
public String PIPELINE_NAME;
public String getPIPELINE_NAME(){
	return this.PIPELINE_NAME;
}
	}
			
	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.
	public ContextProperties getContext() {
		return this.context;
	}

	protected java.util.Map<String, String> defaultProperties = new java.util.HashMap<String, String>();
	protected java.util.Map<String, String> additionalProperties = new java.util.HashMap<String, String>();

	public java.util.Map<String, String> getDefaultProperties() {
	    return this.defaultProperties;
	}

	public java.util.Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

	private final String jobVersion = "0.1";
	private final String jobName = "load_dim_customer";
	private final String projectName = "CAPACITACION_TALEND";
	public Integer errorCode = null;
	private String currentComponent = "";
	public static boolean isStandaloneMS = Boolean.valueOf("false");
	
	private void s(final String component) {
		try {
			org.talend.metrics.DataReadTracker.setCurrentComponent(jobName, component);
		} catch (Exception | NoClassDefFoundError e) {
			// ignore
		}
	}
	
	
	private void mdc(final String subJobName, final String subJobPidPrefix) {
		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", subJobName);
		org.slf4j.MDC.put("_subJobPid", subJobPidPrefix + subJobPidCounter.getAndIncrement());
	}
	
	
	private void sh(final String componentId) {
		ok_Hash.put(componentId, false);
		start_Hash.put(componentId, System.currentTimeMillis());
	}

	{
	s("none");
	}

	
	private String cLabel =  null;
	
		private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
        private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();
	
		private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
		private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
		private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
		public  final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();
	

private final JobStructureCatcherUtils talendJobLog = new JobStructureCatcherUtils(jobName, "_sciu0M7zEfGK8YS6DqfsfQ", "0.1");
private org.talend.job.audit.JobAuditLogger runtime_lineage_logger_talendJobLog = null;
private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";
	
	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(), new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}
	
	public void setDataSourceReferences(List serviceReferences) throws Exception{
		
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		java.util.Map<String, javax.sql.DataSource> dataSources = new java.util.HashMap<String, javax.sql.DataSource>();
		
		for (java.util.Map.Entry<String, javax.sql.DataSource> entry : BundleUtils.getServices(serviceReferences,  javax.sql.DataSource.class).entrySet()) {
                    dataSources.put(entry.getKey(), entry.getValue());
                    talendDataSources.put(entry.getKey(), new routines.system.TalendDataSource(entry.getValue()));
		}

		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}


private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(new java.io.BufferedOutputStream(baos));

public String getExceptionStackTrace() {
	if ("failure".equals(this.getStatus())) {
		errorMessagePS.flush();
		return baos.toString();
	}
	return null;
}

private Exception exception;

public Exception getException() {
	if ("failure".equals(this.getStatus())) {
		return this.exception;
	}
	return null;
}

private class TalendException extends Exception {

	private static final long serialVersionUID = 1L;

	private java.util.Map<String, Object> globalMap = null;
	private Exception e = null;
	
	private String currentComponent = null;
	private String cLabel =  null;
	
	private String virtualComponentName = null;
	
	public void setVirtualComponentName (String virtualComponentName){
		this.virtualComponentName = virtualComponentName;
	}

	private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
		this.currentComponent= errorComponent;
		this.globalMap = globalMap;
		this.e = e;
	}
	
	private TalendException(Exception e, String errorComponent, String errorComponentLabel, final java.util.Map<String, Object> globalMap) {
		this(e, errorComponent, globalMap);
		this.cLabel = errorComponentLabel;
	}

	public Exception getException() {
		return this.e;
	}

	public String getCurrentComponent() {
		return this.currentComponent;
	}

	
    public String getExceptionCauseMessage(Exception e){
        Throwable cause = e;
        String message = null;
        int i = 10;
        while (null != cause && 0 < i--) {
            message = cause.getMessage();
            if (null == message) {
                cause = cause.getCause();
            } else {
                break;          
            }
        }
        if (null == message) {
            message = e.getClass().getName();
        }   
        return message;
    }

	@Override
	public void printStackTrace() {
		if (!(e instanceof TalendException || e instanceof TDieException)) {
			if(virtualComponentName!=null && currentComponent.indexOf(virtualComponentName+"_")==0){
				globalMap.put(virtualComponentName+"_ERROR_MESSAGE",getExceptionCauseMessage(e));
			}
			globalMap.put(currentComponent+"_ERROR_MESSAGE",getExceptionCauseMessage(e));
			System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
		}
		if (!(e instanceof TDieException)) {
			if(e instanceof TalendException){
				e.printStackTrace();
			} else {
				e.printStackTrace();
				e.printStackTrace(errorMessagePS);
			}
		}
		if (!(e instanceof TalendException)) {
			load_dim_customer.this.exception = e;
		}
		if (!(e instanceof TalendException)) {
		try {
			for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
				if (m.getName().compareTo(currentComponent + "_error") == 0) {
					m.invoke(load_dim_customer.this, new Object[] { e , currentComponent, globalMap});
					break;
				}
			}

			if(!(e instanceof TDieException)){
		if(enableLogStash) {
			talendJobLog.addJobExceptionMessage(currentComponent, cLabel, null, e);
			talendJobLogProcess(globalMap);
		}
			}
		} catch (Exception e) {
			this.e.printStackTrace();
		}
		}
	}
}

			public void tFileInputDelimited_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tMap_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tDBOutput_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tDBOutput_2_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void talendJobLog_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {
				
				end_Hash.put(errorComponent, System.currentTimeMillis());
				
				status = "failure";
				
					talendJobLog_onSubJobError(exception, errorComponent, globalMap);
			}
			
			public void tFileInputDelimited_1_onSubJobError(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {

resumeUtil.addLog("SYSTEM_LOG", "NODE:"+ errorComponent, "", Thread.currentThread().getId()+ "", "FATAL", "", exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception),"");

			}
			public void talendJobLog_onSubJobError(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap) throws TalendException {

resumeUtil.addLog("SYSTEM_LOG", "NODE:"+ errorComponent, "", Thread.currentThread().getId()+ "", "FATAL", "", exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception),"");

			}	



public static class out_dim_customerStruct implements routines.system.IPersistableRow<out_dim_customerStruct> {
    final static byte[] commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer = new byte[0];
    static byte[] commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[0];

	
			    public String customer_id;

				public String getCustomer_id () {
					return this.customer_id;
				}

				public Boolean customer_idIsNullable(){
				    return true;
				}
				public Boolean customer_idIsKey(){
				    return false;
				}
				public Integer customer_idLength(){
				    return 34;
				}
				public Integer customer_idPrecision(){
				    return 0;
				}
				public String customer_idDefault(){
				
					return null;
				
				}
				public String customer_idComment(){
				
				    return "";
				
				}
				public String customer_idPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_idOriginalDbColumnName(){
				
					return "customer_id";
				
				}

				
			    public String customer_unique_id;

				public String getCustomer_unique_id () {
					return this.customer_unique_id;
				}

				public Boolean customer_unique_idIsNullable(){
				    return true;
				}
				public Boolean customer_unique_idIsKey(){
				    return false;
				}
				public Integer customer_unique_idLength(){
				    return 34;
				}
				public Integer customer_unique_idPrecision(){
				    return 0;
				}
				public String customer_unique_idDefault(){
				
					return null;
				
				}
				public String customer_unique_idComment(){
				
				    return "";
				
				}
				public String customer_unique_idPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_unique_idOriginalDbColumnName(){
				
					return "customer_unique_id";
				
				}

				
			    public String customer_zip_code_prefix;

				public String getCustomer_zip_code_prefix () {
					return this.customer_zip_code_prefix;
				}

				public Boolean customer_zip_code_prefixIsNullable(){
				    return true;
				}
				public Boolean customer_zip_code_prefixIsKey(){
				    return false;
				}
				public Integer customer_zip_code_prefixLength(){
				    return 100;
				}
				public Integer customer_zip_code_prefixPrecision(){
				    return 0;
				}
				public String customer_zip_code_prefixDefault(){
				
					return null;
				
				}
				public String customer_zip_code_prefixComment(){
				
				    return "";
				
				}
				public String customer_zip_code_prefixPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_zip_code_prefixOriginalDbColumnName(){
				
					return "customer_zip_code_prefix";
				
				}

				
			    public String customer_city;

				public String getCustomer_city () {
					return this.customer_city;
				}

				public Boolean customer_cityIsNullable(){
				    return true;
				}
				public Boolean customer_cityIsKey(){
				    return false;
				}
				public Integer customer_cityLength(){
				    return 100;
				}
				public Integer customer_cityPrecision(){
				    return 0;
				}
				public String customer_cityDefault(){
				
					return null;
				
				}
				public String customer_cityComment(){
				
				    return "";
				
				}
				public String customer_cityPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_cityOriginalDbColumnName(){
				
					return "customer_city";
				
				}

				
			    public String customer_state;

				public String getCustomer_state () {
					return this.customer_state;
				}

				public Boolean customer_stateIsNullable(){
				    return true;
				}
				public Boolean customer_stateIsKey(){
				    return false;
				}
				public Integer customer_stateLength(){
				    return 2;
				}
				public Integer customer_statePrecision(){
				    return 0;
				}
				public String customer_stateDefault(){
				
					return null;
				
				}
				public String customer_stateComment(){
				
				    return "";
				
				}
				public String customer_statePattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_stateOriginalDbColumnName(){
				
					return "customer_state";
				
				}

				
			    public java.util.Date load_ts;

				public java.util.Date getLoad_ts () {
					return this.load_ts;
				}

				public Boolean load_tsIsNullable(){
				    return true;
				}
				public Boolean load_tsIsKey(){
				    return false;
				}
				public Integer load_tsLength(){
				    return null;
				}
				public Integer load_tsPrecision(){
				    return null;
				}
				public String load_tsDefault(){
				
					return null;
				
				}
				public String load_tsComment(){
				
				    return "";
				
				}
				public String load_tsPattern(){
				
					return "yyyy-MM-dd HH:mm:ss";
				
				}
				public String load_tsOriginalDbColumnName(){
				
					return "load_ts";
				
				}

				
			    public String load_source;

				public String getLoad_source () {
					return this.load_source;
				}

				public Boolean load_sourceIsNullable(){
				    return true;
				}
				public Boolean load_sourceIsKey(){
				    return false;
				}
				public Integer load_sourceLength(){
				    return 100;
				}
				public Integer load_sourcePrecision(){
				    return null;
				}
				public String load_sourceDefault(){
				
					return null;
				
				}
				public String load_sourceComment(){
				
				    return "";
				
				}
				public String load_sourcePattern(){
				
					return "";
				
				}
				public String load_sourceOriginalDbColumnName(){
				
					return "load_source";
				
				}

				



	private String readString(ObjectInputStream dis) throws IOException{
		String strReturn = null;
		int length = 0;
        length = dis.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			dis.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}
	
	private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException{
		String strReturn = null;
		int length = 0;
        length = unmarshaller.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			unmarshaller.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}

    private void writeString(String str, ObjectOutputStream dos) throws IOException{
		if(str == null) {
            dos.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
	    	dos.writeInt(byteArray.length);
			dos.write(byteArray);
    	}
    }
    
    private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException{
		if(str == null) {
			marshaller.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
            marshaller.writeInt(byteArray.length);
            marshaller.write(byteArray);
    	}
    }

	private java.util.Date readDate(ObjectInputStream dis) throws IOException{
		java.util.Date dateReturn = null;
        int length = 0;
        length = dis.readByte();
		if (length == -1) {
			dateReturn = null;
		} else {
	    	dateReturn = new Date(dis.readLong());
		}
		return dateReturn;
	}
	
	private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException{
		java.util.Date dateReturn = null;
        int length = 0;
        length = unmarshaller.readByte();
		if (length == -1) {
			dateReturn = null;
		} else {
	    	dateReturn = new Date(unmarshaller.readLong());
		}
		return dateReturn;
	}

    private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException{
		if(date1 == null) {
            dos.writeByte(-1);
		} else {
			dos.writeByte(0);
	    	dos.writeLong(date1.getTime());
    	}
    }
    
    private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException{
		if(date1 == null) {
			marshaller.writeByte(-1);
		} else {
			marshaller.writeByte(0);
			marshaller.writeLong(date1.getTime());
    	}
    }

    public void readData(ObjectInputStream dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this.customer_id = readString(dis);
					
					this.customer_unique_id = readString(dis);
					
					this.customer_zip_code_prefix = readString(dis);
					
					this.customer_city = readString(dis);
					
					this.customer_state = readString(dis);
					
					this.load_ts = readDate(dis);
					
					this.load_source = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }
    
    public void readData(org.jboss.marshalling.Unmarshaller dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this.customer_id = readString(dis);
					
					this.customer_unique_id = readString(dis);
					
					this.customer_zip_code_prefix = readString(dis);
					
					this.customer_city = readString(dis);
					
					this.customer_state = readString(dis);
					
					this.load_ts = readDate(dis);
					
					this.load_source = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }

    public void writeData(ObjectOutputStream dos) {
        try {

		
					// String
				
						writeString(this.customer_id,dos);
					
					// String
				
						writeString(this.customer_unique_id,dos);
					
					// String
				
						writeString(this.customer_zip_code_prefix,dos);
					
					// String
				
						writeString(this.customer_city,dos);
					
					// String
				
						writeString(this.customer_state,dos);
					
					// java.util.Date
				
						writeDate(this.load_ts,dos);
					
					// String
				
						writeString(this.load_source,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }
    
    public void writeData(org.jboss.marshalling.Marshaller dos) {
        try {

		
					// String
				
						writeString(this.customer_id,dos);
					
					// String
				
						writeString(this.customer_unique_id,dos);
					
					// String
				
						writeString(this.customer_zip_code_prefix,dos);
					
					// String
				
						writeString(this.customer_city,dos);
					
					// String
				
						writeString(this.customer_state,dos);
					
					// java.util.Date
				
						writeDate(this.load_ts,dos);
					
					// String
				
						writeString(this.load_source,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }


    public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("[");
		sb.append("customer_id="+customer_id);
		sb.append(",customer_unique_id="+customer_unique_id);
		sb.append(",customer_zip_code_prefix="+customer_zip_code_prefix);
		sb.append(",customer_city="+customer_city);
		sb.append(",customer_state="+customer_state);
		sb.append(",load_ts="+String.valueOf(load_ts));
		sb.append(",load_source="+load_source);
	    sb.append("]");

	    return sb.toString();
    }
        public String toLogString(){
        	StringBuilder sb = new StringBuilder();
        	
        				if(customer_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_id);
            			}
            		
        			sb.append("|");
        		
        				if(customer_unique_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_unique_id);
            			}
            		
        			sb.append("|");
        		
        				if(customer_zip_code_prefix == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_zip_code_prefix);
            			}
            		
        			sb.append("|");
        		
        				if(customer_city == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_city);
            			}
            		
        			sb.append("|");
        		
        				if(customer_state == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_state);
            			}
            		
        			sb.append("|");
        		
        				if(load_ts == null){
        					sb.append("<null>");
        				}else{
            				sb.append(load_ts);
            			}
            		
        			sb.append("|");
        		
        				if(load_source == null){
        					sb.append("<null>");
        				}else{
            				sb.append(load_source);
            			}
            		
        			sb.append("|");
        		
        	return sb.toString();
        }

    /**
     * Compare keys
     */
    public int compareTo(out_dim_customerStruct other) {

		int returnValue = -1;
		
	    return returnValue;
    }


    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
		if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    private int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }


}

public static class out_rejectsStruct implements routines.system.IPersistableRow<out_rejectsStruct> {
    final static byte[] commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer = new byte[0];
    static byte[] commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[0];

	
			    public String customer_id;

				public String getCustomer_id () {
					return this.customer_id;
				}

				public Boolean customer_idIsNullable(){
				    return true;
				}
				public Boolean customer_idIsKey(){
				    return false;
				}
				public Integer customer_idLength(){
				    return 34;
				}
				public Integer customer_idPrecision(){
				    return 0;
				}
				public String customer_idDefault(){
				
					return null;
				
				}
				public String customer_idComment(){
				
				    return "";
				
				}
				public String customer_idPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String customer_idOriginalDbColumnName(){
				
					return "customer_id";
				
				}

				
			    public String reject_reason;

				public String getReject_reason () {
					return this.reject_reason;
				}

				public Boolean reject_reasonIsNullable(){
				    return true;
				}
				public Boolean reject_reasonIsKey(){
				    return false;
				}
				public Integer reject_reasonLength(){
				    return 100;
				}
				public Integer reject_reasonPrecision(){
				    return null;
				}
				public String reject_reasonDefault(){
				
					return null;
				
				}
				public String reject_reasonComment(){
				
				    return "";
				
				}
				public String reject_reasonPattern(){
				
					return "";
				
				}
				public String reject_reasonOriginalDbColumnName(){
				
					return "reject_reason";
				
				}

				
			    public String load_source;

				public String getLoad_source () {
					return this.load_source;
				}

				public Boolean load_sourceIsNullable(){
				    return true;
				}
				public Boolean load_sourceIsKey(){
				    return false;
				}
				public Integer load_sourceLength(){
				    return 100;
				}
				public Integer load_sourcePrecision(){
				    return null;
				}
				public String load_sourceDefault(){
				
					return null;
				
				}
				public String load_sourceComment(){
				
				    return "";
				
				}
				public String load_sourcePattern(){
				
					return "";
				
				}
				public String load_sourceOriginalDbColumnName(){
				
					return "load_source";
				
				}

				
			    public java.util.Date rejected_at;

				public java.util.Date getRejected_at () {
					return this.rejected_at;
				}

				public Boolean rejected_atIsNullable(){
				    return true;
				}
				public Boolean rejected_atIsKey(){
				    return false;
				}
				public Integer rejected_atLength(){
				    return null;
				}
				public Integer rejected_atPrecision(){
				    return null;
				}
				public String rejected_atDefault(){
				
					return null;
				
				}
				public String rejected_atComment(){
				
				    return "";
				
				}
				public String rejected_atPattern(){
				
					return "yyyy-MM-dd HH:mm:ss";
				
				}
				public String rejected_atOriginalDbColumnName(){
				
					return "rejected_at";
				
				}

				
			    public String payload;

				public String getPayload () {
					return this.payload;
				}

				public Boolean payloadIsNullable(){
				    return true;
				}
				public Boolean payloadIsKey(){
				    return false;
				}
				public Integer payloadLength(){
				    return null;
				}
				public Integer payloadPrecision(){
				    return null;
				}
				public String payloadDefault(){
				
					return null;
				
				}
				public String payloadComment(){
				
				    return "";
				
				}
				public String payloadPattern(){
				
					return "";
				
				}
				public String payloadOriginalDbColumnName(){
				
					return "payload";
				
				}

				



	private String readString(ObjectInputStream dis) throws IOException{
		String strReturn = null;
		int length = 0;
        length = dis.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			dis.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}
	
	private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException{
		String strReturn = null;
		int length = 0;
        length = unmarshaller.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			unmarshaller.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}

    private void writeString(String str, ObjectOutputStream dos) throws IOException{
		if(str == null) {
            dos.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
	    	dos.writeInt(byteArray.length);
			dos.write(byteArray);
    	}
    }
    
    private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException{
		if(str == null) {
			marshaller.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
            marshaller.writeInt(byteArray.length);
            marshaller.write(byteArray);
    	}
    }

	private java.util.Date readDate(ObjectInputStream dis) throws IOException{
		java.util.Date dateReturn = null;
        int length = 0;
        length = dis.readByte();
		if (length == -1) {
			dateReturn = null;
		} else {
	    	dateReturn = new Date(dis.readLong());
		}
		return dateReturn;
	}
	
	private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException{
		java.util.Date dateReturn = null;
        int length = 0;
        length = unmarshaller.readByte();
		if (length == -1) {
			dateReturn = null;
		} else {
	    	dateReturn = new Date(unmarshaller.readLong());
		}
		return dateReturn;
	}

    private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException{
		if(date1 == null) {
            dos.writeByte(-1);
		} else {
			dos.writeByte(0);
	    	dos.writeLong(date1.getTime());
    	}
    }
    
    private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException{
		if(date1 == null) {
			marshaller.writeByte(-1);
		} else {
			marshaller.writeByte(0);
			marshaller.writeLong(date1.getTime());
    	}
    }

    public void readData(ObjectInputStream dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this.customer_id = readString(dis);
					
					this.reject_reason = readString(dis);
					
					this.load_source = readString(dis);
					
					this.rejected_at = readDate(dis);
					
					this.payload = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }
    
    public void readData(org.jboss.marshalling.Unmarshaller dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this.customer_id = readString(dis);
					
					this.reject_reason = readString(dis);
					
					this.load_source = readString(dis);
					
					this.rejected_at = readDate(dis);
					
					this.payload = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }

    public void writeData(ObjectOutputStream dos) {
        try {

		
					// String
				
						writeString(this.customer_id,dos);
					
					// String
				
						writeString(this.reject_reason,dos);
					
					// String
				
						writeString(this.load_source,dos);
					
					// java.util.Date
				
						writeDate(this.rejected_at,dos);
					
					// String
				
						writeString(this.payload,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }
    
    public void writeData(org.jboss.marshalling.Marshaller dos) {
        try {

		
					// String
				
						writeString(this.customer_id,dos);
					
					// String
				
						writeString(this.reject_reason,dos);
					
					// String
				
						writeString(this.load_source,dos);
					
					// java.util.Date
				
						writeDate(this.rejected_at,dos);
					
					// String
				
						writeString(this.payload,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }


    public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("[");
		sb.append("customer_id="+customer_id);
		sb.append(",reject_reason="+reject_reason);
		sb.append(",load_source="+load_source);
		sb.append(",rejected_at="+String.valueOf(rejected_at));
		sb.append(",payload="+payload);
	    sb.append("]");

	    return sb.toString();
    }
        public String toLogString(){
        	StringBuilder sb = new StringBuilder();
        	
        				if(customer_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(customer_id);
            			}
            		
        			sb.append("|");
        		
        				if(reject_reason == null){
        					sb.append("<null>");
        				}else{
            				sb.append(reject_reason);
            			}
            		
        			sb.append("|");
        		
        				if(load_source == null){
        					sb.append("<null>");
        				}else{
            				sb.append(load_source);
            			}
            		
        			sb.append("|");
        		
        				if(rejected_at == null){
        					sb.append("<null>");
        				}else{
            				sb.append(rejected_at);
            			}
            		
        			sb.append("|");
        		
        				if(payload == null){
        					sb.append("<null>");
        				}else{
            				sb.append(payload);
            			}
            		
        			sb.append("|");
        		
        	return sb.toString();
        }

    /**
     * Compare keys
     */
    public int compareTo(out_rejectsStruct other) {

		int returnValue = -1;
		
	    return returnValue;
    }


    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
		if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    private int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }


}

public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
    final static byte[] commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer = new byte[0];
    static byte[] commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[0];

	
			    public String _customer_id;

				public String get_customer_id () {
					return this._customer_id;
				}

				public Boolean _customer_idIsNullable(){
				    return true;
				}
				public Boolean _customer_idIsKey(){
				    return false;
				}
				public Integer _customer_idLength(){
				    return 34;
				}
				public Integer _customer_idPrecision(){
				    return 0;
				}
				public String _customer_idDefault(){
				
					return null;
				
				}
				public String _customer_idComment(){
				
				    return "";
				
				}
				public String _customer_idPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String _customer_idOriginalDbColumnName(){
				
					return "_customer_id";
				
				}

				
			    public String _customer_unique_id;

				public String get_customer_unique_id () {
					return this._customer_unique_id;
				}

				public Boolean _customer_unique_idIsNullable(){
				    return true;
				}
				public Boolean _customer_unique_idIsKey(){
				    return false;
				}
				public Integer _customer_unique_idLength(){
				    return 34;
				}
				public Integer _customer_unique_idPrecision(){
				    return 0;
				}
				public String _customer_unique_idDefault(){
				
					return null;
				
				}
				public String _customer_unique_idComment(){
				
				    return "";
				
				}
				public String _customer_unique_idPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String _customer_unique_idOriginalDbColumnName(){
				
					return "_customer_unique_id";
				
				}

				
			    public String _customer_zip_code_prefix;

				public String get_customer_zip_code_prefix () {
					return this._customer_zip_code_prefix;
				}

				public Boolean _customer_zip_code_prefixIsNullable(){
				    return true;
				}
				public Boolean _customer_zip_code_prefixIsKey(){
				    return false;
				}
				public Integer _customer_zip_code_prefixLength(){
				    return 7;
				}
				public Integer _customer_zip_code_prefixPrecision(){
				    return 0;
				}
				public String _customer_zip_code_prefixDefault(){
				
					return null;
				
				}
				public String _customer_zip_code_prefixComment(){
				
				    return "";
				
				}
				public String _customer_zip_code_prefixPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String _customer_zip_code_prefixOriginalDbColumnName(){
				
					return "_customer_zip_code_prefix";
				
				}

				
			    public String _customer_city;

				public String get_customer_city () {
					return this._customer_city;
				}

				public Boolean _customer_cityIsNullable(){
				    return true;
				}
				public Boolean _customer_cityIsKey(){
				    return false;
				}
				public Integer _customer_cityLength(){
				    return 23;
				}
				public Integer _customer_cityPrecision(){
				    return 0;
				}
				public String _customer_cityDefault(){
				
					return null;
				
				}
				public String _customer_cityComment(){
				
				    return "";
				
				}
				public String _customer_cityPattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String _customer_cityOriginalDbColumnName(){
				
					return "_customer_city";
				
				}

				
			    public String _customer_state;

				public String get_customer_state () {
					return this._customer_state;
				}

				public Boolean _customer_stateIsNullable(){
				    return true;
				}
				public Boolean _customer_stateIsKey(){
				    return false;
				}
				public Integer _customer_stateLength(){
				    return 2;
				}
				public Integer _customer_statePrecision(){
				    return 0;
				}
				public String _customer_stateDefault(){
				
					return null;
				
				}
				public String _customer_stateComment(){
				
				    return "";
				
				}
				public String _customer_statePattern(){
				
					return "dd-MM-yyyy";
				
				}
				public String _customer_stateOriginalDbColumnName(){
				
					return "_customer_state";
				
				}

				



	private String readString(ObjectInputStream dis) throws IOException{
		String strReturn = null;
		int length = 0;
        length = dis.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			dis.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}
	
	private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException{
		String strReturn = null;
		int length = 0;
        length = unmarshaller.readInt();
		if (length == -1) {
			strReturn = null;
		} else {
			if(length > commonByteArray_CAPACITACION_TALEND_load_dim_customer.length) {
				if(length < 1024 && commonByteArray_CAPACITACION_TALEND_load_dim_customer.length == 0) {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[1024];
				} else {
   					commonByteArray_CAPACITACION_TALEND_load_dim_customer = new byte[2 * length];
   				}
			}
			unmarshaller.readFully(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length);
			strReturn = new String(commonByteArray_CAPACITACION_TALEND_load_dim_customer, 0, length, utf8Charset);
		}
		return strReturn;
	}

    private void writeString(String str, ObjectOutputStream dos) throws IOException{
		if(str == null) {
            dos.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
	    	dos.writeInt(byteArray.length);
			dos.write(byteArray);
    	}
    }
    
    private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException{
		if(str == null) {
			marshaller.writeInt(-1);
		} else {
            byte[] byteArray = str.getBytes(utf8Charset);
            marshaller.writeInt(byteArray.length);
            marshaller.write(byteArray);
    	}
    }

    public void readData(ObjectInputStream dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this._customer_id = readString(dis);
					
					this._customer_unique_id = readString(dis);
					
					this._customer_zip_code_prefix = readString(dis);
					
					this._customer_city = readString(dis);
					
					this._customer_state = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }
    
    public void readData(org.jboss.marshalling.Unmarshaller dis) {

		synchronized(commonByteArrayLock_CAPACITACION_TALEND_load_dim_customer) {

        	try {

        		int length = 0;
		
					this._customer_id = readString(dis);
					
					this._customer_unique_id = readString(dis);
					
					this._customer_zip_code_prefix = readString(dis);
					
					this._customer_city = readString(dis);
					
					this._customer_state = readString(dis);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);

		

        }

		

      }


    }

    public void writeData(ObjectOutputStream dos) {
        try {

		
					// String
				
						writeString(this._customer_id,dos);
					
					// String
				
						writeString(this._customer_unique_id,dos);
					
					// String
				
						writeString(this._customer_zip_code_prefix,dos);
					
					// String
				
						writeString(this._customer_city,dos);
					
					// String
				
						writeString(this._customer_state,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }
    
    public void writeData(org.jboss.marshalling.Marshaller dos) {
        try {

		
					// String
				
						writeString(this._customer_id,dos);
					
					// String
				
						writeString(this._customer_unique_id,dos);
					
					// String
				
						writeString(this._customer_zip_code_prefix,dos);
					
					// String
				
						writeString(this._customer_city,dos);
					
					// String
				
						writeString(this._customer_state,dos);
					
        	} catch (IOException e) {
	            throw new RuntimeException(e);
        }


    }


    public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("[");
		sb.append("_customer_id="+_customer_id);
		sb.append(",_customer_unique_id="+_customer_unique_id);
		sb.append(",_customer_zip_code_prefix="+_customer_zip_code_prefix);
		sb.append(",_customer_city="+_customer_city);
		sb.append(",_customer_state="+_customer_state);
	    sb.append("]");

	    return sb.toString();
    }
        public String toLogString(){
        	StringBuilder sb = new StringBuilder();
        	
        				if(_customer_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(_customer_id);
            			}
            		
        			sb.append("|");
        		
        				if(_customer_unique_id == null){
        					sb.append("<null>");
        				}else{
            				sb.append(_customer_unique_id);
            			}
            		
        			sb.append("|");
        		
        				if(_customer_zip_code_prefix == null){
        					sb.append("<null>");
        				}else{
            				sb.append(_customer_zip_code_prefix);
            			}
            		
        			sb.append("|");
        		
        				if(_customer_city == null){
        					sb.append("<null>");
        				}else{
            				sb.append(_customer_city);
            			}
            		
        			sb.append("|");
        		
        				if(_customer_state == null){
        					sb.append("<null>");
        				}else{
            				sb.append(_customer_state);
            			}
            		
        			sb.append("|");
        		
        	return sb.toString();
        }

    /**
     * Compare keys
     */
    public int compareTo(row1Struct other) {

		int returnValue = -1;
		
	    return returnValue;
    }


    private int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
		if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }

        return returnValue;
    }

    private int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }


}

public void tFileInputDelimited_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
	globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 0);

	final boolean execStat = this.execStat;

		mdc("tFileInputDelimited_1", "EpfY6T_");

	
		String iterateId = "";
	
	
	String currentComponent = "";
	s("none");
	String cLabel =  null;
	java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

	try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { //start the resume
				globalResumeTicket = true;



		row1Struct row1 = new row1Struct();
out_dim_customerStruct out_dim_customer = new out_dim_customerStruct();
out_rejectsStruct out_rejects = new out_rejectsStruct();





	
	/**
	 * [tDBOutput_1 begin ] start
	 */

	

	
		
		sh("tDBOutput_1");
		
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		
			runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,0,0,"out_dim_customer");
			
		int tos_count_tDBOutput_1 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tDBOutput_1{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tDBOutput_1 = new StringBuilder();
                    log4jParamters_tDBOutput_1.append("Parameters:");
                            log4jParamters_tDBOutput_1.append("DB_VERSION" + " = " + "MYSQL_8");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("USE_EXISTING_CONNECTION" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("HOST" + " = " + "context.DB_HOST");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("PORT" + " = " + "context.DB_PORT");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("DBNAME" + " = " + "context.DB_NAME");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("USER" + " = " + "context.DB_USER");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("PASS" + " = " + String.valueOf(routines.system.PasswordEncryptUtil.encryptPassword(context.DB_PASSWORD)).substring(0, 4) + "...");     
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("TABLE" + " = " + "\"dim_customer_gr5\"");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("TABLE_ACTION" + " = " + "NONE");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("DATA_ACTION" + " = " + "INSERT");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("DIE_ON_ERROR" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("PROPERTIES" + " = " + "\"noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true\"");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("USE_BATCH_SIZE" + " = " + "true");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("BATCH_SIZE" + " = " + "10000");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("COMMIT_EVERY" + " = " + "10000");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("ADD_COLS" + " = " + "[]");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("USE_FIELD_OPTIONS" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("USE_HINT_OPTIONS" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("ENABLE_DEBUG_MODE" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("ON_DUPLICATE_KEY_UPDATE" + " = " + "false");
                        log4jParamters_tDBOutput_1.append(" | ");
                            log4jParamters_tDBOutput_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlOutput");
                        log4jParamters_tDBOutput_1.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + (log4jParamters_tDBOutput_1) );
                    } 
                } 
            new BytesLimit65535_tDBOutput_1().limitLog4jByte();
            }
        // QTUP-3575
        if(enableLineage) {
        class ParameterUtil_tDBOutput_1{
            
            private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
                java.util.Map<String, String> field = new java.util.HashMap<>();
                field.put("name", values[0]);
                field.put("talend_type", values[1]);
                schema.add(field);
            }
        
            public java.util.Map<String, String> getParameter(final out_dim_customerStruct out_dim_customer) throws Exception{
                java.util.Map<String, String> component_parameters = new java.util.HashMap<>();
                
                try {
                
                            component_parameters.put("DB_VERSION", String.valueOf("MYSQL_8"));
                            component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("false"));
                            component_parameters.put("HOST", String.valueOf(context.DB_HOST));
                            component_parameters.put("PORT", String.valueOf(context.DB_PORT));
                            component_parameters.put("DBNAME", String.valueOf(context.DB_NAME));
                            component_parameters.put("USER", String.valueOf(context.DB_USER));
                            component_parameters.put("TABLE", String.valueOf("dim_customer_gr5"));
                            component_parameters.put("TABLE_ACTION", String.valueOf("NONE"));
                            component_parameters.put("DATA_ACTION", String.valueOf("INSERT"));
                            component_parameters.put("SPECIFY_DATASOURCE_ALIAS", String.valueOf("false"));
                            component_parameters.put("DIE_ON_ERROR", String.valueOf("false"));
                            component_parameters.put("PROPERTIES", String.valueOf("noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true"));
                            component_parameters.put("USE_BATCH_SIZE", String.valueOf("true"));
                            component_parameters.put("BATCH_SIZE", String.valueOf("10000"));
                            component_parameters.put("COMMIT_EVERY", String.valueOf("10000"));
                            component_parameters.put("ADD_COLS", String.valueOf("[]"));
                            component_parameters.put("USE_FIELD_OPTIONS", String.valueOf("false"));
                            component_parameters.put("USE_HINT_OPTIONS", String.valueOf("false"));
                            component_parameters.put("ENABLE_DEBUG_MODE", String.valueOf("false"));
                            component_parameters.put("ON_DUPLICATE_KEY_UPDATE", String.valueOf("false"));
                            component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlOutput"));
                
                
                
                } catch (java.lang.Exception e_tDBOutput_1) {
                    //do nothing
                }
                
                return component_parameters;
            }
        }
        
        talendJobLog.addComponentParameterMessage("tDBOutput_1", "tMysqlOutput",
          new ParameterUtil_tDBOutput_1().getParameter(out_dim_customer));
        talendJobLogProcess(globalMap);
        s(currentComponent);
        }
        // QTUP-3575
		
			if(enableLogStash) {
				talendJobLog.addCM("tDBOutput_1", "tDBO_stg_customers", "tMysqlOutput");
				talendJobLogProcess(globalMap);
				s(currentComponent);
			}
			







int nb_line_tDBOutput_1 = 0;
int nb_line_update_tDBOutput_1 = 0;
int nb_line_inserted_tDBOutput_1 = 0;
int nb_line_deleted_tDBOutput_1 = 0;
int nb_line_rejected_tDBOutput_1 = 0;

int deletedCount_tDBOutput_1=0;
int updatedCount_tDBOutput_1=0;
int insertedCount_tDBOutput_1=0;
int rowsToCommitCount_tDBOutput_1=0;
int rejectedCount_tDBOutput_1=0;

String tableName_tDBOutput_1 = "dim_customer_gr5";
boolean whetherReject_tDBOutput_1 = false;

java.util.Calendar calendar_tDBOutput_1 = java.util.Calendar.getInstance();
calendar_tDBOutput_1.set(1, 0, 1, 0, 0, 0);
long year1_tDBOutput_1 = calendar_tDBOutput_1.getTime().getTime();
calendar_tDBOutput_1.set(10000, 0, 1, 0, 0, 0);
long year10000_tDBOutput_1 = calendar_tDBOutput_1.getTime().getTime();
long date_tDBOutput_1;

java.sql.Connection conn_tDBOutput_1 = null;
		
			
        String properties_tDBOutput_1 = "noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true";
        if (properties_tDBOutput_1 == null || properties_tDBOutput_1.trim().length() == 0) {
            properties_tDBOutput_1 = "rewriteBatchedStatements=true&allowLoadLocalInfile=true";
        }else {
            if (!properties_tDBOutput_1.contains("rewriteBatchedStatements=")) {
                properties_tDBOutput_1 += "&rewriteBatchedStatements=true";
            }

            if (!properties_tDBOutput_1.contains("allowLoadLocalInfile=")) {
                properties_tDBOutput_1 += "&allowLoadLocalInfile=true";
            }
        }

        String url_tDBOutput_1 = "jdbc:mysql://" + context.DB_HOST + ":" + context.DB_PORT + "/" + context.DB_NAME + "?" + properties_tDBOutput_1;

			String driverClass_tDBOutput_1 = "com.mysql.cj.jdbc.Driver";
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Driver ClassName: ")  + (driverClass_tDBOutput_1)  + (".") );
			String dbUser_tDBOutput_1 = context.DB_USER;
			

			
	final String decryptedPassword_tDBOutput_1 = java.util.Optional.ofNullable(context.DB_PASSWORD).orElse("");

			String dbPwd_tDBOutput_1 = decryptedPassword_tDBOutput_1;
			java.lang.Class.forName(driverClass_tDBOutput_1);
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection attempts to '")  + (url_tDBOutput_1)  + ("' with the username '")  + (dbUser_tDBOutput_1)  + ("'.") );
			conn_tDBOutput_1 = java.sql.DriverManager.getConnection(url_tDBOutput_1, dbUser_tDBOutput_1, dbPwd_tDBOutput_1);
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection to '")  + (url_tDBOutput_1)  + ("' has succeeded.") );
			
	resourceMap.put("conn_tDBOutput_1", conn_tDBOutput_1);
	
			conn_tDBOutput_1.setAutoCommit(false);
			int commitEvery_tDBOutput_1 = 10000;
			int commitCounter_tDBOutput_1 = 0;
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection is set auto commit to '")  + (conn_tDBOutput_1.getAutoCommit())  + ("'.") );

		int count_tDBOutput_1=0;
		
				
			

		
						String insert_tDBOutput_1 = "INSERT INTO `" + "dim_customer_gr5" + "` (`customer_id`,`customer_unique_id`,`customer_zip_code_prefix`,`customer_city`,`customer_state`,`load_ts`,`load_source`) VALUES (?,?,?,?,?,?,?)";
		
						int batchSize_tDBOutput_1 = 10000;
						int batchSizeCounter_tDBOutput_1=0;
						    
						java.sql.PreparedStatement pstmt_tDBOutput_1 = conn_tDBOutput_1.prepareStatement(insert_tDBOutput_1);
						resourceMap.put("pstmt_tDBOutput_1", pstmt_tDBOutput_1);


 



		

/**
 * [tDBOutput_1 begin ] stop
 */





	
	/**
	 * [tDBOutput_2 begin ] start
	 */

	

	
		
		sh("tDBOutput_2");
		
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		
			runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,0,0,"out_rejects");
			
		int tos_count_tDBOutput_2 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tDBOutput_2{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tDBOutput_2 = new StringBuilder();
                    log4jParamters_tDBOutput_2.append("Parameters:");
                            log4jParamters_tDBOutput_2.append("DB_VERSION" + " = " + "MYSQL_8");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("USE_EXISTING_CONNECTION" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("HOST" + " = " + "context.DB_HOST");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("PORT" + " = " + "context.DB_PORT");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("DBNAME" + " = " + "context.DB_NAME");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("USER" + " = " + "context.DB_USER");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("PASS" + " = " + String.valueOf(routines.system.PasswordEncryptUtil.encryptPassword(context.DB_PASSWORD)).substring(0, 4) + "...");     
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("TABLE" + " = " + "\"audit_reject_customer_gr5\"");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("TABLE_ACTION" + " = " + "NONE");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("DATA_ACTION" + " = " + "INSERT");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("DIE_ON_ERROR" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("PROPERTIES" + " = " + "\"noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true\"");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("USE_BATCH_SIZE" + " = " + "true");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("BATCH_SIZE" + " = " + "10000");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("COMMIT_EVERY" + " = " + "10000");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("ADD_COLS" + " = " + "[]");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("USE_FIELD_OPTIONS" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("USE_HINT_OPTIONS" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("ENABLE_DEBUG_MODE" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("ON_DUPLICATE_KEY_UPDATE" + " = " + "false");
                        log4jParamters_tDBOutput_2.append(" | ");
                            log4jParamters_tDBOutput_2.append("UNIFIED_COMPONENTS" + " = " + "tMysqlOutput");
                        log4jParamters_tDBOutput_2.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + (log4jParamters_tDBOutput_2) );
                    } 
                } 
            new BytesLimit65535_tDBOutput_2().limitLog4jByte();
            }
        // QTUP-3575
        if(enableLineage) {
        class ParameterUtil_tDBOutput_2{
            
            private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
                java.util.Map<String, String> field = new java.util.HashMap<>();
                field.put("name", values[0]);
                field.put("talend_type", values[1]);
                schema.add(field);
            }
        
            public java.util.Map<String, String> getParameter(final out_rejectsStruct out_rejects) throws Exception{
                java.util.Map<String, String> component_parameters = new java.util.HashMap<>();
                
                try {
                
                            component_parameters.put("DB_VERSION", String.valueOf("MYSQL_8"));
                            component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("false"));
                            component_parameters.put("HOST", String.valueOf(context.DB_HOST));
                            component_parameters.put("PORT", String.valueOf(context.DB_PORT));
                            component_parameters.put("DBNAME", String.valueOf(context.DB_NAME));
                            component_parameters.put("USER", String.valueOf(context.DB_USER));
                            component_parameters.put("TABLE", String.valueOf("audit_reject_customer_gr5"));
                            component_parameters.put("TABLE_ACTION", String.valueOf("NONE"));
                            component_parameters.put("DATA_ACTION", String.valueOf("INSERT"));
                            component_parameters.put("SPECIFY_DATASOURCE_ALIAS", String.valueOf("false"));
                            component_parameters.put("DIE_ON_ERROR", String.valueOf("false"));
                            component_parameters.put("PROPERTIES", String.valueOf("noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true"));
                            component_parameters.put("USE_BATCH_SIZE", String.valueOf("true"));
                            component_parameters.put("BATCH_SIZE", String.valueOf("10000"));
                            component_parameters.put("COMMIT_EVERY", String.valueOf("10000"));
                            component_parameters.put("ADD_COLS", String.valueOf("[]"));
                            component_parameters.put("USE_FIELD_OPTIONS", String.valueOf("false"));
                            component_parameters.put("USE_HINT_OPTIONS", String.valueOf("false"));
                            component_parameters.put("ENABLE_DEBUG_MODE", String.valueOf("false"));
                            component_parameters.put("ON_DUPLICATE_KEY_UPDATE", String.valueOf("false"));
                            component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlOutput"));
                
                
                
                } catch (java.lang.Exception e_tDBOutput_2) {
                    //do nothing
                }
                
                return component_parameters;
            }
        }
        
        talendJobLog.addComponentParameterMessage("tDBOutput_2", "tMysqlOutput",
          new ParameterUtil_tDBOutput_2().getParameter(out_rejects));
        talendJobLogProcess(globalMap);
        s(currentComponent);
        }
        // QTUP-3575
		
			if(enableLogStash) {
				talendJobLog.addCM("tDBOutput_2", "tDBO_stg_customers_rejects", "tMysqlOutput");
				talendJobLogProcess(globalMap);
				s(currentComponent);
			}
			







int nb_line_tDBOutput_2 = 0;
int nb_line_update_tDBOutput_2 = 0;
int nb_line_inserted_tDBOutput_2 = 0;
int nb_line_deleted_tDBOutput_2 = 0;
int nb_line_rejected_tDBOutput_2 = 0;

int deletedCount_tDBOutput_2=0;
int updatedCount_tDBOutput_2=0;
int insertedCount_tDBOutput_2=0;
int rowsToCommitCount_tDBOutput_2=0;
int rejectedCount_tDBOutput_2=0;

String tableName_tDBOutput_2 = "audit_reject_customer_gr5";
boolean whetherReject_tDBOutput_2 = false;

java.util.Calendar calendar_tDBOutput_2 = java.util.Calendar.getInstance();
calendar_tDBOutput_2.set(1, 0, 1, 0, 0, 0);
long year1_tDBOutput_2 = calendar_tDBOutput_2.getTime().getTime();
calendar_tDBOutput_2.set(10000, 0, 1, 0, 0, 0);
long year10000_tDBOutput_2 = calendar_tDBOutput_2.getTime().getTime();
long date_tDBOutput_2;

java.sql.Connection conn_tDBOutput_2 = null;
		
			
        String properties_tDBOutput_2 = "noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true";
        if (properties_tDBOutput_2 == null || properties_tDBOutput_2.trim().length() == 0) {
            properties_tDBOutput_2 = "rewriteBatchedStatements=true&allowLoadLocalInfile=true";
        }else {
            if (!properties_tDBOutput_2.contains("rewriteBatchedStatements=")) {
                properties_tDBOutput_2 += "&rewriteBatchedStatements=true";
            }

            if (!properties_tDBOutput_2.contains("allowLoadLocalInfile=")) {
                properties_tDBOutput_2 += "&allowLoadLocalInfile=true";
            }
        }

        String url_tDBOutput_2 = "jdbc:mysql://" + context.DB_HOST + ":" + context.DB_PORT + "/" + context.DB_NAME + "?" + properties_tDBOutput_2;

			String driverClass_tDBOutput_2 = "com.mysql.cj.jdbc.Driver";
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Driver ClassName: ")  + (driverClass_tDBOutput_2)  + (".") );
			String dbUser_tDBOutput_2 = context.DB_USER;
			

			
	final String decryptedPassword_tDBOutput_2 = java.util.Optional.ofNullable(context.DB_PASSWORD).orElse("");

			String dbPwd_tDBOutput_2 = decryptedPassword_tDBOutput_2;
			java.lang.Class.forName(driverClass_tDBOutput_2);
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection attempts to '")  + (url_tDBOutput_2)  + ("' with the username '")  + (dbUser_tDBOutput_2)  + ("'.") );
			conn_tDBOutput_2 = java.sql.DriverManager.getConnection(url_tDBOutput_2, dbUser_tDBOutput_2, dbPwd_tDBOutput_2);
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection to '")  + (url_tDBOutput_2)  + ("' has succeeded.") );
			
	resourceMap.put("conn_tDBOutput_2", conn_tDBOutput_2);
	
			conn_tDBOutput_2.setAutoCommit(false);
			int commitEvery_tDBOutput_2 = 10000;
			int commitCounter_tDBOutput_2 = 0;
			
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection is set auto commit to '")  + (conn_tDBOutput_2.getAutoCommit())  + ("'.") );

		int count_tDBOutput_2=0;
		
				
			

		
						String insert_tDBOutput_2 = "INSERT INTO `" + "audit_reject_customer_gr5" + "` (`customer_id`,`reject_reason`,`load_source`,`rejected_at`,`payload`) VALUES (?,?,?,?,?)";
		
						int batchSize_tDBOutput_2 = 10000;
						int batchSizeCounter_tDBOutput_2=0;
						    
						java.sql.PreparedStatement pstmt_tDBOutput_2 = conn_tDBOutput_2.prepareStatement(insert_tDBOutput_2);
						resourceMap.put("pstmt_tDBOutput_2", pstmt_tDBOutput_2);


 



		

/**
 * [tDBOutput_2 begin ] stop
 */




	
	/**
	 * [tMap_1 begin ] start
	 */

	

	
		
		sh("tMap_1");
		
	
	s(currentComponent="tMap_1");
	
			
			
	
			runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,0,0,"row1");
			
		int tos_count_tMap_1 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tMap_1{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tMap_1 = new StringBuilder();
                    log4jParamters_tMap_1.append("Parameters:");
                            log4jParamters_tMap_1.append("LINK_STYLE" + " = " + "AUTO");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("TEMPORARY_DATA_DIRECTORY" + " = " + "");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("ROWS_BUFFER_SIZE" + " = " + "2000000");
                        log4jParamters_tMap_1.append(" | ");
                            log4jParamters_tMap_1.append("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL" + " = " + "true");
                        log4jParamters_tMap_1.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + (log4jParamters_tMap_1) );
                    } 
                } 
            new BytesLimit65535_tMap_1().limitLog4jByte();
            }
                boolean init_tMap_1_0 = true;
                boolean init_tMap_1_1 = true;
        // QTUP-3575
        if(enableLineage) {
        class ParameterUtil_tMap_1{
            
            private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
                java.util.Map<String, String> field = new java.util.HashMap<>();
                field.put("name", values[0]);
                field.put("talend_type", values[1]);
                schema.add(field);
            }
        
            public java.util.Map<String, String> getParameter(final row1Struct row1) throws Exception{
                java.util.Map<String, String> component_parameters = new java.util.HashMap<>();
                
                try {
                
                            component_parameters.put("LINK_STYLE", String.valueOf("AUTO"));
                            component_parameters.put("ROWS_BUFFER_SIZE", String.valueOf("2000000"));
                            component_parameters.put("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL", String.valueOf("true"));
                component_parameters.put("mapperData", String.valueOf("{\"inputTables\":[{\"name\":\"row1\",\"metadataTableEntries\":[{\"name\":\"_customer_id\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_unique_id\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_zip_code_prefix\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_city\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_state\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false}]}],\"outputTables\":[{\"name\":\"out_dim_customer\",\"metadataTableEntries\":[{\"name\":\"customer_id\",\"expression\":\"row1._customer_id\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"customer_unique_id\",\"expression\":\"row1._customer_unique_id\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"customer_zip_code_prefix\",\"expression\":\"row1._customer_zip_code_prefix\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"customer_city\",\"expression\":\"row1._customer_city\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"customer_state\",\"expression\":\"row1._customer_state == null ? null : row1._customer_state.trim().toUpperCase()\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"load_ts\",\"expression\":\"TalendDate.getCurrentDate()\",\"type\":\"id_Date\",\"nullable\":false},{\"name\":\"load_source\",\"expression\":\"context.LOAD_SOURCE_TAG\",\"type\":\"id_String\",\"nullable\":false}]},{\"name\":\"out_rejects\",\"metadataTableEntries\":[{\"name\":\"customer_id\",\"expression\":\"row1._customer_id\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"reject_reason\",\"expression\":\"row1._customer_id == null || row1._customer_id.trim().isEmpty() ? \\\"missing_customer_id\\\" : \\\"other\\\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"load_source\",\"expression\":\"context.LOAD_SOURCE_TAG\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"rejected_at\",\"expression\":\"TalendDate.getCurrentDate()\",\"type\":\"id_Date\",\"nullable\":false},{\"name\":\"payload\",\"expression\":\"\\\"customer_id=\\\" + row1._customer_id + \\\"; customer_unique_id=\\\" + row1._customer_unique_id + \\\"; customer_city=\\\" + row1._customer_city\",\"type\":\"id_String\",\"nullable\":false}]}],\"varsTables\":[{\"name\":\"Var\",\"metadataTableEntries\":[]}]}"));
                
                
                
                } catch (java.lang.Exception e_tMap_1) {
                    //do nothing
                }
                
                return component_parameters;
            }
        }
        
        talendJobLog.addComponentParameterMessage("tMap_1", "tMap",
          new ParameterUtil_tMap_1().getParameter(row1));
        talendJobLogProcess(globalMap);
        s(currentComponent);
        }
        // QTUP-3575
		
			if(enableLogStash) {
				talendJobLog.addCM("tMap_1", "tMap_1", "tMap");
				talendJobLogProcess(globalMap);
				s(currentComponent);
			}
			




// ###############################
// # Lookup's keys initialization
		int count_row1_tMap_1 = 0;
		
// ###############################        

// ###############################
// # Vars initialization
class  Var__tMap_1__Struct  {
}
Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
// ###############################

// ###############################
// # Outputs initialization
				int count_out_dim_customer_tMap_1 = 0;
				
out_dim_customerStruct out_dim_customer_tmp = new out_dim_customerStruct();
				int count_out_rejects_tMap_1 = 0;
				
out_rejectsStruct out_rejects_tmp = new out_rejectsStruct();
// ###############################

        
        



        









 



		

/**
 * [tMap_1 begin ] stop
 */




	
	/**
	 * [tFileInputDelimited_1 begin ] start
	 */

	

	
		
		sh("tFileInputDelimited_1");
		
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		
		int tos_count_tFileInputDelimited_1 = 0;
		
                if(log.isDebugEnabled())
            log.debug("tFileInputDelimited_1 - "  + ("Start to work.") );
            if (log.isDebugEnabled()) {
                class BytesLimit65535_tFileInputDelimited_1{
                    public void limitLog4jByte() throws Exception{
                    StringBuilder log4jParamters_tFileInputDelimited_1 = new StringBuilder();
                    log4jParamters_tFileInputDelimited_1.append("Parameters:");
                            log4jParamters_tFileInputDelimited_1.append("USE_EXISTING_DYNAMIC" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("FILENAME" + " = " + "context.FILE_OLIST_CUSTOMERS + \"olist_customers_dataset.csv\"");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("CSV_OPTION" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("FIELDSEPARATOR" + " = " + "\",\"");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("HEADER" + " = " + "1");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("FOOTER" + " = " + "0");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("LIMIT" + " = " + "");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("REMOVE_EMPTY_ROW" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("UNCOMPRESS" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("DIE_ON_ERROR" + " = " + "true");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("RANDOM" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("TRIMALL" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("TRIMSELECT" + " = " + "[{TRIM="+("false")+", SCHEMA_COLUMN="+("_customer_id")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("_customer_unique_id")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("_customer_zip_code_prefix")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("_customer_city")+"}, {TRIM="+("false")+", SCHEMA_COLUMN="+("_customer_state")+"}]");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("CHECK_FIELDS_NUM" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("CHECK_DATE" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("ENCODING" + " = " + "\"UTF-8\"");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("SPLITRECORD" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("ENABLE_DECODE" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                            log4jParamters_tFileInputDelimited_1.append("USE_HEADER_AS_IS" + " = " + "false");
                        log4jParamters_tFileInputDelimited_1.append(" | ");
                if(log.isDebugEnabled())
            log.debug("tFileInputDelimited_1 - "  + (log4jParamters_tFileInputDelimited_1) );
                    } 
                } 
            new BytesLimit65535_tFileInputDelimited_1().limitLog4jByte();
            }
                boolean init_tFileInputDelimited_1_0 = true;
        // QTUP-3575
        if(enableLineage) {
        class ParameterUtil_tFileInputDelimited_1{
            
            private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
                java.util.Map<String, String> field = new java.util.HashMap<>();
                field.put("name", values[0]);
                field.put("talend_type", values[1]);
                schema.add(field);
            }
        
            public java.util.Map<String, String> getParameter() throws Exception{
                java.util.Map<String, String> component_parameters = new java.util.HashMap<>();
                
                try {
                
                            component_parameters.put("USE_EXISTING_DYNAMIC", String.valueOf("false"));
                            component_parameters.put("FILENAME", String.valueOf(context.FILE_OLIST_CUSTOMERS + "olist_customers_dataset.csv"));
                            component_parameters.put("CSV_OPTION", String.valueOf("false"));
                            component_parameters.put("ROWSEPARATOR", String.valueOf("\\n"));
                            component_parameters.put("FIELDSEPARATOR", String.valueOf(","));
                            component_parameters.put("HEADER", String.valueOf("1"));
                            component_parameters.put("FOOTER", String.valueOf("0"));
                            component_parameters.put("LIMIT", String.valueOf(""));
                            component_parameters.put("REMOVE_EMPTY_ROW", String.valueOf("false"));
                            component_parameters.put("UNCOMPRESS", String.valueOf("false"));
                            component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
                            component_parameters.put("ADVANCED_SEPARATOR", String.valueOf("false"));
                            component_parameters.put("RANDOM", String.valueOf("false"));
                            component_parameters.put("TRIMALL", String.valueOf("false"));
                            component_parameters.put("TRIMSELECT", String.valueOf(new StringBuilder().append("[{TRIM=").append("false").append(", SCHEMA_COLUMN=").append("_customer_id").append("}, {TRIM=").append("false").append(", SCHEMA_COLUMN=").append("_customer_unique_id").append("}, {TRIM=").append("false").append(", SCHEMA_COLUMN=").append("_customer_zip_code_prefix").append("}, {TRIM=").append("false").append(", SCHEMA_COLUMN=").append("_customer_city").append("}, {TRIM=").append("false").append(", SCHEMA_COLUMN=").append("_customer_state").append("}]").toString()));
                            component_parameters.put("CHECK_FIELDS_NUM", String.valueOf("false"));
                            component_parameters.put("CHECK_DATE", String.valueOf("false"));
                            component_parameters.put("ENCODING", String.valueOf("UTF-8"));
                            component_parameters.put("SPLITRECORD", String.valueOf("false"));
                            component_parameters.put("ENABLE_DECODE", String.valueOf("false"));
                            component_parameters.put("DESTINATION", String.valueOf(""));
                            component_parameters.put("USE_HEADER_AS_IS", String.valueOf("false"));
                
                
                
                } catch (java.lang.Exception e_tFileInputDelimited_1) {
                    //do nothing
                }
                
                return component_parameters;
            }
        }
        
        talendJobLog.addComponentParameterMessage("tFileInputDelimited_1", "tFileInputDelimited",
          new ParameterUtil_tFileInputDelimited_1().getParameter());
        talendJobLogProcess(globalMap);
        s(currentComponent);
        }
        // QTUP-3575
		
			if(enableLogStash) {
				talendJobLog.addCM("tFileInputDelimited_1", "tFII_customers", "tFileInputDelimited");
				talendJobLogProcess(globalMap);
				s(currentComponent);
			}
			
	
	
	
 
	
	
	final routines.system.RowState rowstate_tFileInputDelimited_1 = new routines.system.RowState();
	
	
				int nb_line_tFileInputDelimited_1 = 0;
				org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_1 = null;
				int limit_tFileInputDelimited_1 = -1;
				try{
					
						Object filename_tFileInputDelimited_1 = context.FILE_OLIST_CUSTOMERS + "olist_customers_dataset.csv";
						if(filename_tFileInputDelimited_1 instanceof java.io.InputStream){
							
			int footer_value_tFileInputDelimited_1 = 0, random_value_tFileInputDelimited_1 = -1;
			if(footer_value_tFileInputDelimited_1 >0 || random_value_tFileInputDelimited_1 > 0){
				throw new java.lang.Exception("When the input source is a stream,footer and random shouldn't be bigger than 0.");				
			}
		
						}
						try {
							fid_tFileInputDelimited_1 = new org.talend.fileprocess.FileInputDelimited(context.FILE_OLIST_CUSTOMERS + "olist_customers_dataset.csv", "UTF-8",",","\n",false,1,0,
									limit_tFileInputDelimited_1
								,-1, false);
						} catch(java.lang.Exception e) {
globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",e.getMessage());
							
								throw e;
							
						}
					
				    
				    	log.info("tFileInputDelimited_1 - Retrieving records from the datasource.");
				    
					while (fid_tFileInputDelimited_1!=null && fid_tFileInputDelimited_1.nextRecord()) {
						rowstate_tFileInputDelimited_1.reset();
						
			    						row1 = null;			
												
									boolean whetherReject_tFileInputDelimited_1 = false;
									row1 = new row1Struct();
									try {
										
				int columnIndexWithD_tFileInputDelimited_1 = 0;
				
					columnIndexWithD_tFileInputDelimited_1 = 0;
					
							row1._customer_id = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
						
				
					columnIndexWithD_tFileInputDelimited_1 = 1;
					
							row1._customer_unique_id = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
						
				
					columnIndexWithD_tFileInputDelimited_1 = 2;
					
							row1._customer_zip_code_prefix = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
						
				
					columnIndexWithD_tFileInputDelimited_1 = 3;
					
							row1._customer_city = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
						
				
					columnIndexWithD_tFileInputDelimited_1 = 4;
					
							row1._customer_state = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
						
				
				
										
										if(rowstate_tFileInputDelimited_1.getException()!=null) {
											throw rowstate_tFileInputDelimited_1.getException();
										}
										
										
							
			    					} catch (java.lang.Exception e) {
globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",e.getMessage());
			        					whetherReject_tFileInputDelimited_1 = true;
			        					
			            					throw(e);
			            				
			    					}
								
			log.debug("tFileInputDelimited_1 - Retrieving the record " + fid_tFileInputDelimited_1.getRowNumber() + ".");
		

 



		

/**
 * [tFileInputDelimited_1 begin ] stop
 */

	
	/**
	 * [tFileInputDelimited_1 main ] start
	 */

	

	
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		

 
      
      
      // QTUP-3575
      if(enableLineage && init_tFileInputDelimited_1_0) {
      class SchemaUtil_row1 {
      
          private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
              java.util.Map<String, String> field = new java.util.HashMap<>();
              field.put("name", values[0]);
              field.put("origin_name", values[1]);
              field.put("iskey", values[2]);
              field.put("talend_type", values[3]);
              field.put("type", values[4]);
              field.put("nullable", values[5]);
              field.put("pattern", values[6]);
              field.put("length", values[7]);
              field.put("precision", values[8]);
              schema.add(field);
          }
          
          public java.util.List<java.util.Map<String, String>> getSchema(final row1Struct row1) {
              java.util.List<java.util.Map<String, String>> s = new java.util.ArrayList<>();
              if(row1 == null) {
                  return s;
              }
              a(s,"_customer_id","_customer_id","false","id_String","null","true","dd-MM-yyyy","34","0");
              a(s,"_customer_unique_id","_customer_unique_id","false","id_String","null","true","dd-MM-yyyy","34","0");
              a(s,"_customer_zip_code_prefix","_customer_zip_code_prefix","false","id_String","null","true","dd-MM-yyyy","7","0");
              a(s,"_customer_city","_customer_city","false","id_String","null","true","dd-MM-yyyy","23","0");
              a(s,"_customer_state","_customer_state","false","id_String","null","true","dd-MM-yyyy","2","0");
              return s;
          }
          
      }
      
      if(row1 != null) {
          talendJobLog.addConnectionSchemaMessage("tFileInputDelimited_1","tFileInputDelimited",
            "tMap_1","tMap", "row1" + iterateId, new SchemaUtil_row1().getSchema(row1));
          talendJobLogProcess(globalMap);
          init_tFileInputDelimited_1_0 = false;
      }
      
      }
  // QTUP-3575


	tos_count_tFileInputDelimited_1++;

		

/**
 * [tFileInputDelimited_1 main ] stop
 */

	
	/**
	 * [tFileInputDelimited_1 process_data_begin ] start
	 */

	

	
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		

 



		

/**
 * [tFileInputDelimited_1 process_data_begin ] stop
 */

// Start of branch "row1"
if(row1 != null) { 



	
	/**
	 * [tMap_1 main ] start
	 */

	

	
	
	s(currentComponent="tMap_1");
	
			
			
	
			if(runStat.update(execStat,enableLogStash,iterateId,1,1
				
					,"row1","tFileInputDelimited_1","tFII_customers","tFileInputDelimited","tMap_1","tMap_1","tMap"
				
			)) {
				talendJobLogProcess(globalMap);
			}
			
    			if(log.isTraceEnabled()){
    				log.trace("row1 - " + (row1==null? "": row1.toLogString()));
    			}
    		

		
		
		boolean hasCasePrimitiveKeyWithNull_tMap_1 = false;
		
		// ###############################
		// # Input tables (lookups)
		
		boolean rejectedInnerJoin_tMap_1 = false;
		boolean mainRowRejected_tMap_1 = false;
		// ###############################
        { // start of Var scope
        
	        // ###############################
        	// # Vars tables
        
Var__tMap_1__Struct Var = Var__tMap_1;// ###############################
        // ###############################
        // # Output tables

out_dim_customer = null;
out_rejects = null;


// # Output table : 'out_dim_customer'
// # Filter conditions 
if( 

row1._customer_id != null
&& !row1._customer_id.trim().isEmpty()

 ) {
count_out_dim_customer_tMap_1++;

out_dim_customer_tmp.customer_id = row1._customer_id ;
out_dim_customer_tmp.customer_unique_id = row1._customer_unique_id ;
out_dim_customer_tmp.customer_zip_code_prefix = row1._customer_zip_code_prefix ;
out_dim_customer_tmp.customer_city = row1._customer_city ;
out_dim_customer_tmp.customer_state = row1._customer_state == null ? null : row1._customer_state.trim().toUpperCase();
out_dim_customer_tmp.load_ts = TalendDate.getCurrentDate();
out_dim_customer_tmp.load_source = context.LOAD_SOURCE_TAG;
out_dim_customer = out_dim_customer_tmp;
log.debug("tMap_1 - Outputting the record " + count_out_dim_customer_tMap_1 + " of the output table 'out_dim_customer'.");

} // closing filter/reject

// # Output table : 'out_rejects'
// # Filter conditions 
if( 

row1._customer_id == null
|| row1._customer_id.trim().isEmpty()

 ) {
count_out_rejects_tMap_1++;

out_rejects_tmp.customer_id = row1._customer_id ;
out_rejects_tmp.reject_reason = row1._customer_id == null || row1._customer_id.trim().isEmpty() ? "missing_customer_id" : "other";
out_rejects_tmp.load_source = context.LOAD_SOURCE_TAG;
out_rejects_tmp.rejected_at = TalendDate.getCurrentDate();
out_rejects_tmp.payload = "customer_id=" + row1._customer_id + "; customer_unique_id=" + row1._customer_unique_id + "; customer_city=" + row1._customer_city;
out_rejects = out_rejects_tmp;
log.debug("tMap_1 - Outputting the record " + count_out_rejects_tMap_1 + " of the output table 'out_rejects'.");

} // closing filter/reject
// ###############################

} // end of Var scope

rejectedInnerJoin_tMap_1 = false;










 
      
      
      // QTUP-3575
      if(enableLineage && init_tMap_1_0) {
      class SchemaUtil_out_dim_customer {
      
          private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
              java.util.Map<String, String> field = new java.util.HashMap<>();
              field.put("name", values[0]);
              field.put("origin_name", values[1]);
              field.put("iskey", values[2]);
              field.put("talend_type", values[3]);
              field.put("type", values[4]);
              field.put("nullable", values[5]);
              field.put("pattern", values[6]);
              field.put("length", values[7]);
              field.put("precision", values[8]);
              schema.add(field);
          }
          
          public java.util.List<java.util.Map<String, String>> getSchema(final out_dim_customerStruct out_dim_customer) {
              java.util.List<java.util.Map<String, String>> s = new java.util.ArrayList<>();
              if(out_dim_customer == null) {
                  return s;
              }
              a(s,"customer_id","customer_id","false","id_String","null","true","dd-MM-yyyy","34","0");
              a(s,"customer_unique_id","customer_unique_id","false","id_String","null","true","dd-MM-yyyy","34","0");
              a(s,"customer_zip_code_prefix","customer_zip_code_prefix","false","id_String","null","true","dd-MM-yyyy","100","0");
              a(s,"customer_city","customer_city","false","id_String","null","true","dd-MM-yyyy","100","0");
              a(s,"customer_state","customer_state","false","id_String","null","true","dd-MM-yyyy","2","0");
              a(s,"load_ts","load_ts","false","id_Date","","true","yyyy-MM-dd HH:mm:ss","null","null");
              a(s,"load_source","load_source","false","id_String","","true","","100","null");
              return s;
          }
          
      }
      
      if(out_dim_customer != null) {
          talendJobLog.addConnectionSchemaMessage("tMap_1","tMap",
            "tDBOutput_1","tMysqlOutput", "out_dim_customer" + iterateId, new SchemaUtil_out_dim_customer().getSchema(out_dim_customer));
          talendJobLogProcess(globalMap);
          init_tMap_1_0 = false;
      }
      
      }
  // QTUP-3575
      
      
      // QTUP-3575
      if(enableLineage && init_tMap_1_1) {
      class SchemaUtil_out_rejects {
      
          private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
              java.util.Map<String, String> field = new java.util.HashMap<>();
              field.put("name", values[0]);
              field.put("origin_name", values[1]);
              field.put("iskey", values[2]);
              field.put("talend_type", values[3]);
              field.put("type", values[4]);
              field.put("nullable", values[5]);
              field.put("pattern", values[6]);
              field.put("length", values[7]);
              field.put("precision", values[8]);
              schema.add(field);
          }
          
          public java.util.List<java.util.Map<String, String>> getSchema(final out_rejectsStruct out_rejects) {
              java.util.List<java.util.Map<String, String>> s = new java.util.ArrayList<>();
              if(out_rejects == null) {
                  return s;
              }
              a(s,"customer_id","customer_id","false","id_String","null","true","dd-MM-yyyy","34","0");
              a(s,"reject_reason","reject_reason","false","id_String","","true","","100","null");
              a(s,"load_source","load_source","false","id_String","","true","","100","null");
              a(s,"rejected_at","rejected_at","false","id_Date","","true","yyyy-MM-dd HH:mm:ss","null","null");
              a(s,"payload","payload","false","id_String","","true","","null","null");
              return s;
          }
          
      }
      
      if(out_rejects != null) {
          talendJobLog.addConnectionSchemaMessage("tMap_1","tMap",
            "tDBOutput_2","tMysqlOutput", "out_rejects" + iterateId, new SchemaUtil_out_rejects().getSchema(out_rejects));
          talendJobLogProcess(globalMap);
          init_tMap_1_1 = false;
      }
      
      }
  // QTUP-3575


	tos_count_tMap_1++;

		

/**
 * [tMap_1 main ] stop
 */

	
	/**
	 * [tMap_1 process_data_begin ] start
	 */

	

	
	
	s(currentComponent="tMap_1");
	
			
			
	

 



		

/**
 * [tMap_1 process_data_begin ] stop
 */

// Start of branch "out_dim_customer"
if(out_dim_customer != null) { 



	
	/**
	 * [tDBOutput_1 main ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		
			if(runStat.update(execStat,enableLogStash,iterateId,1,1
				
					,"out_dim_customer","tMap_1","tMap_1","tMap","tDBOutput_1","tDBO_stg_customers","tMysqlOutput"
				
			)) {
				talendJobLogProcess(globalMap);
			}
			
    			if(log.isTraceEnabled()){
    				log.trace("out_dim_customer - " + (out_dim_customer==null? "": out_dim_customer.toLogString()));
    			}
    		



        whetherReject_tDBOutput_1 = false;
                            if(out_dim_customer.customer_id == null) {
pstmt_tDBOutput_1.setNull(1, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(1, out_dim_customer.customer_id);
}

                            if(out_dim_customer.customer_unique_id == null) {
pstmt_tDBOutput_1.setNull(2, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(2, out_dim_customer.customer_unique_id);
}

                            if(out_dim_customer.customer_zip_code_prefix == null) {
pstmt_tDBOutput_1.setNull(3, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(3, out_dim_customer.customer_zip_code_prefix);
}

                            if(out_dim_customer.customer_city == null) {
pstmt_tDBOutput_1.setNull(4, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(4, out_dim_customer.customer_city);
}

                            if(out_dim_customer.customer_state == null) {
pstmt_tDBOutput_1.setNull(5, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(5, out_dim_customer.customer_state);
}

                            if(out_dim_customer.load_ts != null) {
date_tDBOutput_1 = out_dim_customer.load_ts.getTime();
if(date_tDBOutput_1 < year1_tDBOutput_1 || date_tDBOutput_1 >= year10000_tDBOutput_1) {
pstmt_tDBOutput_1.setString(6, "0000-00-00 00:00:00");
} else {pstmt_tDBOutput_1.setTimestamp(6, new java.sql.Timestamp(date_tDBOutput_1));
}
} else {
pstmt_tDBOutput_1.setNull(6, java.sql.Types.DATE);
}

                            if(out_dim_customer.load_source == null) {
pstmt_tDBOutput_1.setNull(7, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_1.setString(7, out_dim_customer.load_source);
}

                    pstmt_tDBOutput_1.addBatch();
                    nb_line_tDBOutput_1++;

                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Adding the record ")  + (nb_line_tDBOutput_1)  + (" to the ")  + ("INSERT")  + (" batch.") );
                      batchSizeCounter_tDBOutput_1++;
                if ( batchSize_tDBOutput_1 <= batchSizeCounter_tDBOutput_1) {
                try {
                        int countSum_tDBOutput_1 = 0;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
                        for(int countEach_tDBOutput_1: pstmt_tDBOutput_1.executeBatch()) {
                            countSum_tDBOutput_1 += (countEach_tDBOutput_1 == java.sql.Statement.EXECUTE_FAILED ? 0 : 1);
                        }
                        rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
                        insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
                }catch (java.sql.BatchUpdateException e){
                    globalMap.put("tDBOutput_1_ERROR_MESSAGE",e.getMessage());
                    int countSum_tDBOutput_1 = 0;
                    for(int countEach_tDBOutput_1: e.getUpdateCounts()) {
                        countSum_tDBOutput_1 += (countEach_tDBOutput_1 < 0 ? 0 : countEach_tDBOutput_1);
                    }
                    rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
                    insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
                    System.err.println(e.getMessage());
            log.error("tDBOutput_1 - "  + (e.getMessage()) );
                }

                batchSizeCounter_tDBOutput_1 = 0;
            }
                commitCounter_tDBOutput_1++;

                if(commitEvery_tDBOutput_1 <= commitCounter_tDBOutput_1) {

                        try {
                                int countSum_tDBOutput_1 = 0;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
                                for(int countEach_tDBOutput_1: pstmt_tDBOutput_1.executeBatch()) {
                                    countSum_tDBOutput_1 += (countEach_tDBOutput_1 < 0 ? 0 : 1);
                                }
                                rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
                                insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
                        }catch (java.sql.BatchUpdateException e){
                            globalMap.put("tDBOutput_1_ERROR_MESSAGE",e.getMessage());
                            int countSum_tDBOutput_1 = 0;
                            for(int countEach_tDBOutput_1: e.getUpdateCounts()) {
                                countSum_tDBOutput_1 += (countEach_tDBOutput_1 < 0 ? 0 : countEach_tDBOutput_1);
                            }
                            rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
                            insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
                            System.err.println(e.getMessage());
            log.error("tDBOutput_1 - "  + (e.getMessage()) );

                        }
                    if(rowsToCommitCount_tDBOutput_1 != 0){
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection starting to commit ")  + (rowsToCommitCount_tDBOutput_1)  + (" record(s).") );
                    }
                    conn_tDBOutput_1.commit();
                    if(rowsToCommitCount_tDBOutput_1 != 0){
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection commit has succeeded.") );
                        rowsToCommitCount_tDBOutput_1 = 0;
                    }
                    commitCounter_tDBOutput_1=0;
                }

 


	tos_count_tDBOutput_1++;

		

/**
 * [tDBOutput_1 main ] stop
 */

	
	/**
	 * [tDBOutput_1 process_data_begin ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		

 



		

/**
 * [tDBOutput_1 process_data_begin ] stop
 */

	
	/**
	 * [tDBOutput_1 process_data_end ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		

 



		

/**
 * [tDBOutput_1 process_data_end ] stop
 */


} // End of branch "out_dim_customer"




// Start of branch "out_rejects"
if(out_rejects != null) { 



	
	/**
	 * [tDBOutput_2 main ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		
			if(runStat.update(execStat,enableLogStash,iterateId,1,1
				
					,"out_rejects","tMap_1","tMap_1","tMap","tDBOutput_2","tDBO_stg_customers_rejects","tMysqlOutput"
				
			)) {
				talendJobLogProcess(globalMap);
			}
			
    			if(log.isTraceEnabled()){
    				log.trace("out_rejects - " + (out_rejects==null? "": out_rejects.toLogString()));
    			}
    		



        whetherReject_tDBOutput_2 = false;
                            if(out_rejects.customer_id == null) {
pstmt_tDBOutput_2.setNull(1, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_2.setString(1, out_rejects.customer_id);
}

                            if(out_rejects.reject_reason == null) {
pstmt_tDBOutput_2.setNull(2, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_2.setString(2, out_rejects.reject_reason);
}

                            if(out_rejects.load_source == null) {
pstmt_tDBOutput_2.setNull(3, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_2.setString(3, out_rejects.load_source);
}

                            if(out_rejects.rejected_at != null) {
date_tDBOutput_2 = out_rejects.rejected_at.getTime();
if(date_tDBOutput_2 < year1_tDBOutput_2 || date_tDBOutput_2 >= year10000_tDBOutput_2) {
pstmt_tDBOutput_2.setString(4, "0000-00-00 00:00:00");
} else {pstmt_tDBOutput_2.setTimestamp(4, new java.sql.Timestamp(date_tDBOutput_2));
}
} else {
pstmt_tDBOutput_2.setNull(4, java.sql.Types.DATE);
}

                            if(out_rejects.payload == null) {
pstmt_tDBOutput_2.setNull(5, java.sql.Types.VARCHAR);
} else {pstmt_tDBOutput_2.setString(5, out_rejects.payload);
}

                    pstmt_tDBOutput_2.addBatch();
                    nb_line_tDBOutput_2++;

                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Adding the record ")  + (nb_line_tDBOutput_2)  + (" to the ")  + ("INSERT")  + (" batch.") );
                      batchSizeCounter_tDBOutput_2++;
                if ( batchSize_tDBOutput_2 <= batchSizeCounter_tDBOutput_2) {
                try {
                        int countSum_tDBOutput_2 = 0;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
                        for(int countEach_tDBOutput_2: pstmt_tDBOutput_2.executeBatch()) {
                            countSum_tDBOutput_2 += (countEach_tDBOutput_2 == java.sql.Statement.EXECUTE_FAILED ? 0 : 1);
                        }
                        rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
                        insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
                }catch (java.sql.BatchUpdateException e){
                    globalMap.put("tDBOutput_2_ERROR_MESSAGE",e.getMessage());
                    int countSum_tDBOutput_2 = 0;
                    for(int countEach_tDBOutput_2: e.getUpdateCounts()) {
                        countSum_tDBOutput_2 += (countEach_tDBOutput_2 < 0 ? 0 : countEach_tDBOutput_2);
                    }
                    rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
                    insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
                    System.err.println(e.getMessage());
            log.error("tDBOutput_2 - "  + (e.getMessage()) );
                }

                batchSizeCounter_tDBOutput_2 = 0;
            }
                commitCounter_tDBOutput_2++;

                if(commitEvery_tDBOutput_2 <= commitCounter_tDBOutput_2) {

                        try {
                                int countSum_tDBOutput_2 = 0;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
                                for(int countEach_tDBOutput_2: pstmt_tDBOutput_2.executeBatch()) {
                                    countSum_tDBOutput_2 += (countEach_tDBOutput_2 < 0 ? 0 : 1);
                                }
                                rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
                                insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
                        }catch (java.sql.BatchUpdateException e){
                            globalMap.put("tDBOutput_2_ERROR_MESSAGE",e.getMessage());
                            int countSum_tDBOutput_2 = 0;
                            for(int countEach_tDBOutput_2: e.getUpdateCounts()) {
                                countSum_tDBOutput_2 += (countEach_tDBOutput_2 < 0 ? 0 : countEach_tDBOutput_2);
                            }
                            rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
                            insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
                            System.err.println(e.getMessage());
            log.error("tDBOutput_2 - "  + (e.getMessage()) );

                        }
                    if(rowsToCommitCount_tDBOutput_2 != 0){
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection starting to commit ")  + (rowsToCommitCount_tDBOutput_2)  + (" record(s).") );
                    }
                    conn_tDBOutput_2.commit();
                    if(rowsToCommitCount_tDBOutput_2 != 0){
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection commit has succeeded.") );
                        rowsToCommitCount_tDBOutput_2 = 0;
                    }
                    commitCounter_tDBOutput_2=0;
                }

 


	tos_count_tDBOutput_2++;

		

/**
 * [tDBOutput_2 main ] stop
 */

	
	/**
	 * [tDBOutput_2 process_data_begin ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		

 



		

/**
 * [tDBOutput_2 process_data_begin ] stop
 */

	
	/**
	 * [tDBOutput_2 process_data_end ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		

 



		

/**
 * [tDBOutput_2 process_data_end ] stop
 */


} // End of branch "out_rejects"




	
	/**
	 * [tMap_1 process_data_end ] start
	 */

	

	
	
	s(currentComponent="tMap_1");
	
			
			
	

 



		

/**
 * [tMap_1 process_data_end ] stop
 */


} // End of branch "row1"




	
	/**
	 * [tFileInputDelimited_1 process_data_end ] start
	 */

	

	
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		

 



		

/**
 * [tFileInputDelimited_1 process_data_end ] stop
 */

	
	/**
	 * [tFileInputDelimited_1 end ] start
	 */

	

	
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		



            }
            }finally{
                if(!((Object)(context.FILE_OLIST_CUSTOMERS + "olist_customers_dataset.csv") instanceof java.io.InputStream)){
                	if(fid_tFileInputDelimited_1!=null){
                		fid_tFileInputDelimited_1.close();
                	}
                }
                if(fid_tFileInputDelimited_1!=null){
                	globalMap.put("tFileInputDelimited_1_NB_LINE", fid_tFileInputDelimited_1.getRowNumber());
					
						log.info("tFileInputDelimited_1 - Retrieved records count: "+ fid_tFileInputDelimited_1.getRowNumber() + ".");
					
                }
			}
			  

 
                if(log.isDebugEnabled())
            log.debug("tFileInputDelimited_1 - "  + ("Done.") );

ok_Hash.put("tFileInputDelimited_1", true);
end_Hash.put("tFileInputDelimited_1", System.currentTimeMillis());




		

/**
 * [tFileInputDelimited_1 end ] stop
 */


	
	/**
	 * [tMap_1 end ] start
	 */

	

	
	
	s(currentComponent="tMap_1");
	
			
			
	


// ###############################
// # Lookup hashes releasing
// ###############################      
				log.debug("tMap_1 - Written records count in the table 'out_dim_customer': " + count_out_dim_customer_tMap_1 + ".");
				log.debug("tMap_1 - Written records count in the table 'out_rejects': " + count_out_rejects_tMap_1 + ".");





			 		if(runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,"row1",2,0,
			 			"tFileInputDelimited_1","tFII_customers","tFileInputDelimited","tMap_1","tMap_1","tMap","output")) {
						talendJobLogProcess(globalMap);
					}
				
 
                if(log.isDebugEnabled())
            log.debug("tMap_1 - "  + ("Done.") );

ok_Hash.put("tMap_1", true);
end_Hash.put("tMap_1", System.currentTimeMillis());




		

/**
 * [tMap_1 end ] stop
 */


	
	/**
	 * [tDBOutput_1 end ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		



				
					try {
						if (batchSizeCounter_tDBOutput_1 != 0) {
							int countSum_tDBOutput_1 = 0;
							
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
							for(int countEach_tDBOutput_1: pstmt_tDBOutput_1.executeBatch()) {
								countSum_tDBOutput_1 += (countEach_tDBOutput_1 == java.sql.Statement.EXECUTE_FAILED ? 0 : 1);
							}
							rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
							
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
							
								insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
							
						}
					}catch (java.sql.BatchUpdateException e){
						globalMap.put(currentComponent+"_ERROR_MESSAGE",e.getMessage());
						
							int countSum_tDBOutput_1 = 0;
							for(int countEach_tDBOutput_1: e.getUpdateCounts()) {
								countSum_tDBOutput_1 += (countEach_tDBOutput_1 < 0 ? 0 : countEach_tDBOutput_1);
							}
							rowsToCommitCount_tDBOutput_1 += countSum_tDBOutput_1;
							
								insertedCount_tDBOutput_1 += countSum_tDBOutput_1;
								
            log.error("tDBOutput_1 - "  + (e.getMessage()) );
							System.err.println(e.getMessage());
							
					}
					batchSizeCounter_tDBOutput_1 = 0;
					
			
		

		if(pstmt_tDBOutput_1 != null) {
			
				pstmt_tDBOutput_1.close();
				resourceMap.remove("pstmt_tDBOutput_1");
			
		}
		
	resourceMap.put("statementClosed_tDBOutput_1", true);
	
			if (commitCounter_tDBOutput_1 > 0 && rowsToCommitCount_tDBOutput_1 != 0) {
				
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection starting to commit ")  + (rowsToCommitCount_tDBOutput_1)  + (" record(s).") );
			}
			conn_tDBOutput_1.commit();
			if (commitCounter_tDBOutput_1 > 0 && rowsToCommitCount_tDBOutput_1 != 0) {
				
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection commit has succeeded.") );
				rowsToCommitCount_tDBOutput_1 = 0;
			}
			commitCounter_tDBOutput_1 = 0;
			
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Closing the connection to the database.") );
		conn_tDBOutput_1 .close();
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection to the database has closed.") );
		resourceMap.put("finish_tDBOutput_1", true);
	

	nb_line_deleted_tDBOutput_1=nb_line_deleted_tDBOutput_1+ deletedCount_tDBOutput_1;
	nb_line_update_tDBOutput_1=nb_line_update_tDBOutput_1 + updatedCount_tDBOutput_1;
	nb_line_inserted_tDBOutput_1=nb_line_inserted_tDBOutput_1 + insertedCount_tDBOutput_1;
	nb_line_rejected_tDBOutput_1=nb_line_rejected_tDBOutput_1 + rejectedCount_tDBOutput_1;
	
        globalMap.put("tDBOutput_1_NB_LINE",nb_line_tDBOutput_1);
        globalMap.put("tDBOutput_1_NB_LINE_UPDATED",nb_line_update_tDBOutput_1);
        globalMap.put("tDBOutput_1_NB_LINE_INSERTED",nb_line_inserted_tDBOutput_1);
        globalMap.put("tDBOutput_1_NB_LINE_DELETED",nb_line_deleted_tDBOutput_1);
        globalMap.put("tDBOutput_1_NB_LINE_REJECTED", nb_line_rejected_tDBOutput_1);
    

	
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Has ")  + ("inserted")  + (" ")  + (nb_line_inserted_tDBOutput_1)  + (" record(s).") );

			 		if(runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,"out_dim_customer",2,0,
			 			"tMap_1","tMap_1","tMap","tDBOutput_1","tDBO_stg_customers","tMysqlOutput","output")) {
						talendJobLogProcess(globalMap);
					}
				
 
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Done.") );

ok_Hash.put("tDBOutput_1", true);
end_Hash.put("tDBOutput_1", System.currentTimeMillis());




		

/**
 * [tDBOutput_1 end ] stop
 */





	
	/**
	 * [tDBOutput_2 end ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		



				
					try {
						if (batchSizeCounter_tDBOutput_2 != 0) {
							int countSum_tDBOutput_2 = 0;
							
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Executing the ")  + ("INSERT")  + (" batch.") );
							for(int countEach_tDBOutput_2: pstmt_tDBOutput_2.executeBatch()) {
								countSum_tDBOutput_2 += (countEach_tDBOutput_2 == java.sql.Statement.EXECUTE_FAILED ? 0 : 1);
							}
							rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
							
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("The ")  + ("INSERT")  + (" batch execution has succeeded.") );
							
								insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
							
						}
					}catch (java.sql.BatchUpdateException e){
						globalMap.put(currentComponent+"_ERROR_MESSAGE",e.getMessage());
						
							int countSum_tDBOutput_2 = 0;
							for(int countEach_tDBOutput_2: e.getUpdateCounts()) {
								countSum_tDBOutput_2 += (countEach_tDBOutput_2 < 0 ? 0 : countEach_tDBOutput_2);
							}
							rowsToCommitCount_tDBOutput_2 += countSum_tDBOutput_2;
							
								insertedCount_tDBOutput_2 += countSum_tDBOutput_2;
								
            log.error("tDBOutput_2 - "  + (e.getMessage()) );
							System.err.println(e.getMessage());
							
					}
					batchSizeCounter_tDBOutput_2 = 0;
					
			
		

		if(pstmt_tDBOutput_2 != null) {
			
				pstmt_tDBOutput_2.close();
				resourceMap.remove("pstmt_tDBOutput_2");
			
		}
		
	resourceMap.put("statementClosed_tDBOutput_2", true);
	
			if (commitCounter_tDBOutput_2 > 0 && rowsToCommitCount_tDBOutput_2 != 0) {
				
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection starting to commit ")  + (rowsToCommitCount_tDBOutput_2)  + (" record(s).") );
			}
			conn_tDBOutput_2.commit();
			if (commitCounter_tDBOutput_2 > 0 && rowsToCommitCount_tDBOutput_2 != 0) {
				
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection commit has succeeded.") );
				rowsToCommitCount_tDBOutput_2 = 0;
			}
			commitCounter_tDBOutput_2 = 0;
			
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Closing the connection to the database.") );
		conn_tDBOutput_2 .close();
		
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection to the database has closed.") );
		resourceMap.put("finish_tDBOutput_2", true);
	

	nb_line_deleted_tDBOutput_2=nb_line_deleted_tDBOutput_2+ deletedCount_tDBOutput_2;
	nb_line_update_tDBOutput_2=nb_line_update_tDBOutput_2 + updatedCount_tDBOutput_2;
	nb_line_inserted_tDBOutput_2=nb_line_inserted_tDBOutput_2 + insertedCount_tDBOutput_2;
	nb_line_rejected_tDBOutput_2=nb_line_rejected_tDBOutput_2 + rejectedCount_tDBOutput_2;
	
        globalMap.put("tDBOutput_2_NB_LINE",nb_line_tDBOutput_2);
        globalMap.put("tDBOutput_2_NB_LINE_UPDATED",nb_line_update_tDBOutput_2);
        globalMap.put("tDBOutput_2_NB_LINE_INSERTED",nb_line_inserted_tDBOutput_2);
        globalMap.put("tDBOutput_2_NB_LINE_DELETED",nb_line_deleted_tDBOutput_2);
        globalMap.put("tDBOutput_2_NB_LINE_REJECTED", nb_line_rejected_tDBOutput_2);
    

	
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Has ")  + ("inserted")  + (" ")  + (nb_line_inserted_tDBOutput_2)  + (" record(s).") );

			 		if(runStat.updateStatAndLog(execStat,enableLogStash,resourceMap,iterateId,"out_rejects",2,0,
			 			"tMap_1","tMap_1","tMap","tDBOutput_2","tDBO_stg_customers_rejects","tMysqlOutput","output")) {
						talendJobLogProcess(globalMap);
					}
				
 
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Done.") );

ok_Hash.put("tDBOutput_2", true);
end_Hash.put("tDBOutput_2", System.currentTimeMillis());




		

/**
 * [tDBOutput_2 end ] stop
 */







				}//end the resume

				



	
			}catch(java.lang.Exception e){	
				
				    if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
					   log.fatal(currentComponent + " " + e.getMessage(),e);
					}
				
				TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);
				
				throw te;
			}catch(java.lang.Error error){	
				
					runStat.stopThreadStat();
				
				throw error;
			}finally{
				
				try{
					
	
	/**
	 * [tFileInputDelimited_1 finally ] start
	 */

	

	
	
	s(currentComponent="tFileInputDelimited_1");
	
			
			
	
			cLabel="tFII_customers";
		

 



		

/**
 * [tFileInputDelimited_1 finally ] stop
 */


	
	/**
	 * [tMap_1 finally ] start
	 */

	

	
	
	s(currentComponent="tMap_1");
	
			
			
	

 



		

/**
 * [tMap_1 finally ] stop
 */


	
	/**
	 * [tDBOutput_1 finally ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_1");
	
			
			
	
			cLabel="tDBO_stg_customers";
		



    try {
    if (resourceMap.get("statementClosed_tDBOutput_1") == null) {
                java.sql.PreparedStatement pstmtToClose_tDBOutput_1 = null;
                if ((pstmtToClose_tDBOutput_1 = (java.sql.PreparedStatement) resourceMap.remove("pstmt_tDBOutput_1")) != null) {
                    pstmtToClose_tDBOutput_1.close();
                }
    }
    } finally {
        if(resourceMap.get("finish_tDBOutput_1") == null){
            java.sql.Connection ctn_tDBOutput_1 = null;
            if((ctn_tDBOutput_1 = (java.sql.Connection)resourceMap.get("conn_tDBOutput_1")) != null){
                try {
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Closing the connection to the database.") );
                    ctn_tDBOutput_1.close();
                if(log.isDebugEnabled())
            log.debug("tDBOutput_1 - "  + ("Connection to the database has closed.") );
                } catch (java.sql.SQLException sqlEx_tDBOutput_1) {
                    String errorMessage_tDBOutput_1 = "failed to close the connection in tDBOutput_1 :" + sqlEx_tDBOutput_1.getMessage();
            log.error("tDBOutput_1 - "  + (errorMessage_tDBOutput_1) );
                    System.err.println(errorMessage_tDBOutput_1);
                }
            }
        }
    }
 



		

/**
 * [tDBOutput_1 finally ] stop
 */





	
	/**
	 * [tDBOutput_2 finally ] start
	 */

	

	
	
	s(currentComponent="tDBOutput_2");
	
			
			
	
			cLabel="tDBO_stg_customers_rejects";
		



    try {
    if (resourceMap.get("statementClosed_tDBOutput_2") == null) {
                java.sql.PreparedStatement pstmtToClose_tDBOutput_2 = null;
                if ((pstmtToClose_tDBOutput_2 = (java.sql.PreparedStatement) resourceMap.remove("pstmt_tDBOutput_2")) != null) {
                    pstmtToClose_tDBOutput_2.close();
                }
    }
    } finally {
        if(resourceMap.get("finish_tDBOutput_2") == null){
            java.sql.Connection ctn_tDBOutput_2 = null;
            if((ctn_tDBOutput_2 = (java.sql.Connection)resourceMap.get("conn_tDBOutput_2")) != null){
                try {
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Closing the connection to the database.") );
                    ctn_tDBOutput_2.close();
                if(log.isDebugEnabled())
            log.debug("tDBOutput_2 - "  + ("Connection to the database has closed.") );
                } catch (java.sql.SQLException sqlEx_tDBOutput_2) {
                    String errorMessage_tDBOutput_2 = "failed to close the connection in tDBOutput_2 :" + sqlEx_tDBOutput_2.getMessage();
            log.error("tDBOutput_2 - "  + (errorMessage_tDBOutput_2) );
                    System.err.println(errorMessage_tDBOutput_2);
                }
            }
        }
    }
 



		

/**
 * [tDBOutput_2 finally ] stop
 */







				}catch(java.lang.Exception e){	
					//ignore
				}catch(java.lang.Error error){
					//ignore
				}
				resourceMap = null;
			}
		

		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 1);
	}
	


public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
	globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

	final boolean execStat = this.execStat;


	
		String iterateId = "";
	
	
	String currentComponent = "";
	s("none");
	String cLabel =  null;
	java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

	try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { //start the resume
				globalResumeTicket = true;





	
	/**
	 * [talendJobLog begin ] start
	 */

	

	
		
		sh("talendJobLog");
		
	
	s(currentComponent="talendJobLog");
	
			
			
	
		int tos_count_talendJobLog = 0;
		

	for (JobStructureCatcherUtils.JobStructureCatcherMessage jcm : talendJobLog.getMessages()) {
		org.talend.job.audit.JobContextBuilder builder_talendJobLog = org.talend.job.audit.JobContextBuilder.create().jobName(jcm.job_name).jobId(jcm.job_id).jobVersion(jcm.job_version)
			.custom("process_id", jcm.pid).custom("thread_id", jcm.tid).custom("pid", pid).custom("father_pid", fatherPid).custom("root_pid", rootPid);
		org.talend.logging.audit.Context log_context_talendJobLog = null;
		
		
		if(jcm.log_type == JobStructureCatcherUtils.LogType.PERFORMANCE){
			long timeMS = jcm.end_time - jcm.start_time;
			String duration = String.valueOf(timeMS);
			
			log_context_talendJobLog = builder_talendJobLog
				.sourceId(jcm.sourceId).sourceLabel(jcm.sourceLabel).sourceConnectorType(jcm.sourceComponentName)
				.targetId(jcm.targetId).targetLabel(jcm.targetLabel).targetConnectorType(jcm.targetComponentName)
				.connectionName(jcm.current_connector).rows(jcm.row_count).duration(duration).build();
			auditLogger_talendJobLog.flowExecution(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.JOBSTART) {
			log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).build();
			auditLogger_talendJobLog.jobstart(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.JOBEND) {
			long timeMS = jcm.end_time - jcm.start_time;
			String duration = String.valueOf(timeMS);
		
			log_context_talendJobLog = builder_talendJobLog
				.timestamp(jcm.moment).duration(duration).status(jcm.status).build();
			auditLogger_talendJobLog.jobstop(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.RUNCOMPONENT) {
			log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment)
				.connectorType(jcm.component_name).connectorId(jcm.component_id).connectorLabel(jcm.component_label).build();
			auditLogger_talendJobLog.runcomponent(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.FLOWINPUT) {//log current component input line
			long timeMS = jcm.end_time - jcm.start_time;
			String duration = String.valueOf(timeMS);
			
			log_context_talendJobLog = builder_talendJobLog
				.connectorType(jcm.component_name).connectorId(jcm.component_id).connectorLabel(jcm.component_label)
				.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
				.rows(jcm.total_row_number).duration(duration).build();
			auditLogger_talendJobLog.flowInput(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.FLOWOUTPUT) {//log current component output/reject line
			long timeMS = jcm.end_time - jcm.start_time;
			String duration = String.valueOf(timeMS);
			
			log_context_talendJobLog = builder_talendJobLog
				.connectorType(jcm.component_name).connectorId(jcm.component_id).connectorLabel(jcm.component_label)
				.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
				.rows(jcm.total_row_number).duration(duration).build();
			auditLogger_talendJobLog.flowOutput(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.JOBERROR) {
			java.lang.Exception e_talendJobLog = jcm.exception;
			if(e_talendJobLog!=null) {
				try(java.io.StringWriter sw_talendJobLog = new java.io.StringWriter();java.io.PrintWriter pw_talendJobLog = new java.io.PrintWriter(sw_talendJobLog)) {
					e_talendJobLog.printStackTrace(pw_talendJobLog);
					builder_talendJobLog.custom("stacktrace", sw_talendJobLog.getBuffer().substring(0,java.lang.Math.min(sw_talendJobLog.getBuffer().length(), 512)));
				}
			}

			if(jcm.extra_info!=null) {
				builder_talendJobLog.connectorId(jcm.component_id).custom("extra_info", jcm.extra_info);
			}
				
			log_context_talendJobLog = builder_talendJobLog
				.connectorType(jcm.component_id.substring(0, jcm.component_id.lastIndexOf('_')))
				.connectorId(jcm.component_id)
				.connectorLabel(jcm.component_label == null ? jcm.component_id : jcm.component_label).build();

			auditLogger_talendJobLog.exception(log_context_talendJobLog);
		}
		
		
		
		if(jcm.log_type == JobStructureCatcherUtils.LogType.RUNTIMEPARAMETER) {
			builder_talendJobLog.connectorType(jcm.component_name).connectorId(jcm.component_id);
			
			for(java.util.Map.Entry<String,String> entry : jcm.component_parameters.entrySet()) {
				builder_talendJobLog.custom("P_" + entry.getKey(), entry.getValue());
			}
			
			log_context_talendJobLog = builder_talendJobLog.build();
			
			runtime_lineage_logger_talendJobLog.componentParameters(log_context_talendJobLog);
		} else if(jcm.log_type == JobStructureCatcherUtils.LogType.RUNTIMESCHEMA) {
			builder_talendJobLog.sourceConnectorType(jcm.sourceComponentName).sourceId(jcm.sourceId)
				.connectionName(jcm.current_connector).schema(jcm.component_schema.toString())
				.targetConnectorType(jcm.targetComponentName).targetId(jcm.targetId);
				
			if(jcm.current_connector_type != null) {
				builder_talendJobLog.custom("lineStyle", jcm.current_connector_type);
			}
			log_context_talendJobLog = builder_talendJobLog.build();
			runtime_lineage_logger_talendJobLog.schema(log_context_talendJobLog);
		}
		
	}

 



		

/**
 * [talendJobLog begin ] stop
 */

	
	/**
	 * [talendJobLog main ] start
	 */

	

	
	
	s(currentComponent="talendJobLog");
	
			
			
	

 


	tos_count_talendJobLog++;

		

/**
 * [talendJobLog main ] stop
 */

	
	/**
	 * [talendJobLog process_data_begin ] start
	 */

	

	
	
	s(currentComponent="talendJobLog");
	
			
			
	

 



		

/**
 * [talendJobLog process_data_begin ] stop
 */

	
	/**
	 * [talendJobLog process_data_end ] start
	 */

	

	
	
	s(currentComponent="talendJobLog");
	
			
			
	

 



		

/**
 * [talendJobLog process_data_end ] stop
 */

	
	/**
	 * [talendJobLog end ] start
	 */

	

	
	
	s(currentComponent="talendJobLog");
	
			
			
	

 

ok_Hash.put("talendJobLog", true);
end_Hash.put("talendJobLog", System.currentTimeMillis());




		

/**
 * [talendJobLog end ] stop
 */

				}//end the resume

				



	
			}catch(java.lang.Exception e){	
				
				    if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
					   log.fatal(currentComponent + " " + e.getMessage(),e);
					}
				
				TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);
				
				throw te;
			}catch(java.lang.Error error){	
				
					runStat.stopThreadStat();
				
				throw error;
			}finally{
				
				try{
					
	
	/**
	 * [talendJobLog finally ] start
	 */

	

	
	
	s(currentComponent="talendJobLog");
	
			
			
	

 



		

/**
 * [talendJobLog finally ] stop
 */

				}catch(java.lang.Exception e){	
					//ignore
				}catch(java.lang.Error error){
					//ignore
				}
				resourceMap = null;
			}
		

		globalMap.put("talendJobLog_SUBPROCESS_STATE", 1);
	}
	
    public String resuming_logs_dir_path = null;
    public String resuming_checkpoint_path = null;
    public String parent_part_launcher = null;
    private String resumeEntryMethodName = null;
    private boolean globalResumeTicket = false;

    public boolean watch = false;
    // portStats is null, it means don't execute the statistics
    public Integer portStats = null;
    public int portTraces = 4334;
    public String clientHost;
    public String defaultClientHost = "localhost";
    public String contextStr = "Default";
    public boolean isDefaultContext = true;
    public String pid = "0";
    public String rootPid = null;
    public String fatherPid = null;
    public String fatherNode = null;
    public long startTime = 0;
    public boolean isChildJob = false;
    public String log4jLevel = "";
    
    private boolean enableLogStash;
    private boolean enableLineage;

    private boolean execStat = true;
    
    private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
        protected java.util.Map<String, String> initialValue() {
            java.util.Map<String,String> threadRunResultMap = new java.util.HashMap<String, String>();
            threadRunResultMap.put("errorCode", null);
            threadRunResultMap.put("status", "");
            return threadRunResultMap;
        };
    };


    protected PropertiesWithType context_param = new PropertiesWithType();
    public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

    public String status= "";
    
    
    private final static java.util.Properties jobInfo = new java.util.Properties();
    private final static java.util.Map<String,String> mdcInfo = new java.util.HashMap<>();
    private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();


    public static void main(String[] args){
        final load_dim_customer load_dim_customerClass = new load_dim_customer();
        int exitCode = load_dim_customerClass.runJobInTOS(args);
	        if(exitCode==0){
		        log.info("TalendJob: 'load_dim_customer' - Done.");
	        }

		
		System.exit(exitCode);
		
    }
	

		
	private void getjobInfo() {
		final String TEMPLATE_PATH = "src/main/templates/jobInfo_template.properties";
		final String BUILD_PATH = "../jobInfo.properties";
		final String path = this.getClass().getResource("").getPath();
		if(path.lastIndexOf("target") > 0) {
			final java.io.File templateFile = new java.io.File(
					path.substring(0, path.lastIndexOf("target")).concat(TEMPLATE_PATH));
			if (templateFile.exists()) {
				readJobInfo(templateFile);
				return;
			}
		}
			readJobInfo(new java.io.File(BUILD_PATH));
	}

    private void readJobInfo(java.io.File jobInfoFile){
	
        if(jobInfoFile.exists()) {
            try (java.io.InputStream is = new java.io.FileInputStream(jobInfoFile)) {
            	jobInfo.load(is);
            } catch (IOException e) {
            	 
                log.debug("Read jobInfo.properties file fail: " + e.getMessage());
                
            }
        }
		log.info(String.format("Project name: %s\tJob name: %s\tGIT Commit ID: %s\tTalend Version: %s",
				projectName,jobName,jobInfo.getProperty("gitCommitId"), "8.0.1.20260619_1127-patch"));
		
    }


    public String[][] runJob(String[] args) {

        int exitCode = runJobInTOS(args);
        String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

        return bufferValue;
    }

    public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;
    	
        return hastBufferOutput;
    }

    public int runJobInTOS(String[] args) {
	   	// reset status
	   	status = "";
	   	
        String lastStr = "";
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--context_param")) {
                lastStr = arg;
            } else if (lastStr.equals("")) {
                evalParam(arg);
            } else {
                evalParam(lastStr + " " + arg);
                lastStr = "";
            }
        }

        enableLogStash = "true".equalsIgnoreCase(System.getProperty("audit.enabled"));

            if(!"".equals(log4jLevel)){

                if("trace".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.TRACE);
                }else if("debug".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.DEBUG);
                }else if("info".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.INFO);
                }else if("warn".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.WARN);
                }else if("error".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.ERROR);
                }else if("fatal".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.FATAL);
                }else if ("off".equalsIgnoreCase(log4jLevel)){
                    org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(), org.apache.logging.log4j.Level.OFF);
                }
                org.apache.logging.log4j.core.config.Configurator.setLevel(org.apache.logging.log4j.LogManager.getRootLogger().getName(), log.getLevel());
            }

	         
	         getjobInfo();
	         

            log.info("TalendJob: 'load_dim_customer' - Start.");

            java.util.Set<Object> jobInfoKeys = jobInfo.keySet();
            for(Object jobInfoKey: jobInfoKeys) {
                org.slf4j.MDC.put("_" + jobInfoKey.toString(), jobInfo.get(jobInfoKey).toString());
            }
            org.slf4j.MDC.put("_pid", pid);
            org.slf4j.MDC.put("_rootPid", rootPid);
            org.slf4j.MDC.put("_fatherPid", fatherPid);
            org.slf4j.MDC.put("_projectName", projectName);
            org.slf4j.MDC.put("_startTimestamp",java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC ).format( java.time.format.DateTimeFormatter.ISO_INSTANT ));
            org.slf4j.MDC.put("_jobRepositoryId","_sciu0M7zEfGK8YS6DqfsfQ");
            org.slf4j.MDC.put("_compiledAtTimestamp","2026-10-23T22:02:37.765932800Z");

            java.lang.management.RuntimeMXBean mx = java.lang.management.ManagementFactory.getRuntimeMXBean();
            String[] mxNameTable = mx.getName().split("@"); //$NON-NLS-1$
            if (mxNameTable.length == 2) {
                org.slf4j.MDC.put("_systemPid", mxNameTable[0]);
            } else {
                org.slf4j.MDC.put("_systemPid", String.valueOf(java.lang.Thread.currentThread().getId()));
            }


        final boolean enableCBP = false;
        boolean inOSGi = routines.system.BundleUtils.inOSGi();

        boolean needSendForCBP = false;
        if (!inOSGi) {
            if(org.talend.metrics.CBPClient.getInstanceForCurrentVM() == null) {
                try {
                    needSendForCBP = true;

                    String jobType = "DI";
                    boolean isRooutJob = fatherPid == null ? true : false;
                    if (isStandaloneMS || inOSGi) {
                        jobType = "DATA_SERVICE";
                        // for APPINT cases everything is packaged as single job
                        isRooutJob = true;
                    } else {
                        // print only for DI job for microservice it is already printed
                        // for OSGI runtime will take care
                        if(jobInfo.isEmpty()) {
                            getjobInfo();
                        }
                    }
                    org.talend.metrics.CBPClient.startListenIfNotStarted(enableCBP, isRooutJob, jobType, jobInfo.getProperty("artifactId"), true, 
                        jobInfo.getProperty("cmdLineVersion"), jobInfo.getProperty("patch"), jobInfo.getProperty("date"));
                } catch (java.lang.Exception e) {
                    errorCode = 1;
                    status = "failure";
                    e.printStackTrace();
                    return 1;
                }
	    }
	}
        
		
		    final String lineageDirPath = System.getProperty("runtime.lineage.outputpath");
		    final String payloadFilePath = System.getProperty("tmc.task.payload.path");
		    if((lineageDirPath != null && !lineageDirPath.isEmpty()) || (payloadFilePath !=null && !payloadFilePath.isEmpty())) {
		        enableLineage = true;
		        
    			java.util.Properties p_talendJobLog = new java.util.Properties();
    			p_talendJobLog.setProperty("root.logger", "runtime");
    			p_talendJobLog.setProperty("encoding", "UTF-8");
    			p_talendJobLog.setProperty("application.name", "Talend Studio");
    			p_talendJobLog.setProperty("service.name", "Talend Studio Job");
    			p_talendJobLog.setProperty("instance.name", "Talend Studio Job Instance");
    			p_talendJobLog.setProperty("propagate.appender.exceptions", "none");
    			p_talendJobLog.setProperty("log.appender", "file");
    			p_talendJobLog.setProperty("appender.file.path", "runtime.json");
    			p_talendJobLog.setProperty("appender.file.maxsize", "52428800");
    			p_talendJobLog.setProperty("appender.file.maxbackup", "20");
    			p_talendJobLog.setProperty("host", "false");
    			
    			final String lineageFilePath = System.getProperty("runtime.lineage.appender.file.path");
    			if(lineageFilePath==null || lineageFilePath.isEmpty()) {
    			    String finalLineageDirPath = lineageDirPath;
    			    if(lineageDirPath == null || lineageDirPath.isEmpty()) {
                       finalLineageDirPath = payloadFilePath + "_dir";
                    }
    			
					System.setProperty("runtime.lineage.appender.file.path", 
						new StringBuilder().append(finalLineageDirPath)
						.append((finalLineageDirPath.endsWith("/") || finalLineageDirPath.endsWith("\\")) ? "" : java.io.File.separator)
						.append(projectName)
						.append(java.io.File.separatorChar)
						.append(jobName)
						.append(java.io.File.separatorChar)
						.append(jobVersion)
						.append(java.io.File.separatorChar)
						.append("runtime_log_")
						.append(new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()))
						.append(".json")
						.toString()
					);
    			}
    			
    			System.getProperties().stringPropertyNames().stream()
    					.filter(it -> it.startsWith("runtime.lineage.") && !"runtime.lineage.outputpath".equals(it))
    					.forEach(key -> p_talendJobLog.setProperty(key.substring("runtime.lineage.".length()), System.getProperty(key)));

    			
    			
    			
    			org.apache.logging.log4j.core.config.Configurator.setLevel(p_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);
    			
    			
    			runtime_lineage_logger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory.createJobAuditLogger(p_talendJobLog);
			}
		
			if(enableLogStash) {
				java.util.Properties properties_talendJobLog = new java.util.Properties();
				properties_talendJobLog.setProperty("root.logger", "audit");
				properties_talendJobLog.setProperty("encoding", "UTF-8");
				properties_talendJobLog.setProperty("application.name", "Talend Studio");
				properties_talendJobLog.setProperty("service.name", "Talend Studio Job");
				properties_talendJobLog.setProperty("instance.name", "Talend Studio Job Instance");
				properties_talendJobLog.setProperty("propagate.appender.exceptions", "none");
				properties_talendJobLog.setProperty("log.appender", "file");
				properties_talendJobLog.setProperty("appender.file.path", "audit.json");
				properties_talendJobLog.setProperty("appender.file.maxsize", "52428800");
				properties_talendJobLog.setProperty("appender.file.maxbackup", "20");
				properties_talendJobLog.setProperty("host", "false");

				System.getProperties().stringPropertyNames().stream()
					.filter(it -> it.startsWith("audit.logger."))
					.forEach(key -> properties_talendJobLog.setProperty(key.substring("audit.logger.".length()), System.getProperty(key)));

				
				
				
				org.apache.logging.log4j.core.config.Configurator.setLevel(properties_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);
				
				auditLogger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory.createJobAuditLogger(properties_talendJobLog);
			}
		

        if(clientHost == null) {
            clientHost = defaultClientHost;
        }

        if(pid == null || "0".equals(pid)) {
            pid = TalendString.getAsciiRandomString(6);
        }

            org.slf4j.MDC.put("_pid", pid);

        if (rootPid==null) {
            rootPid = pid;
        }

            org.slf4j.MDC.put("_rootPid", rootPid);

        if (fatherPid==null) {
            fatherPid = pid;
        }else{
            isChildJob = true;
        }
            org.slf4j.MDC.put("_fatherPid", fatherPid);

        if (portStats != null) {
            // portStats = -1; //for testing
            if (portStats < 0 || portStats > 65535) {
                // issue:10869, the portStats is invalid, so this client socket can't open
                System.err.println("The statistics socket port " + portStats + " is invalid.");
                execStat = false;
            }
        } else {
            execStat = false;
        }

        java.util.Dictionary<String, Object> jobProperties = null;
        try {
            if (inOSGi) {
                jobProperties = routines.system.BundleUtils.getJobProperties(jobName);
    
                if (jobProperties != null && jobProperties.get("context") != null) {
                    contextStr = (String)jobProperties.get("context");
                }

                // extract params from caller
                String taskExecutionIdForSOAP = null;
                String jobExecutionIdForSOAP = null;
                boolean skipCBPAuth = false;
                boolean isSoapService = false;
                for(String arg : args) {
                    // for SOAP service, the taskExecutionId and jobExecutionId are passed in as context params from Talend Runtime
                    if(arg.startsWith("--context_param")
                            && (arg.contains("taskExecutionId") || arg.contains("jobExecutionId"))) {
                        String keyValue = arg.replace("--context_param", "");
                        String[] parts = keyValue.split("=");
                        String[] cleanParts = java.util.Arrays.stream(parts)
                                .filter(s -> !s.isEmpty())
                                .toArray(String[]::new);
                        if (cleanParts.length == 2) {
                            String key = cleanParts[0];
                            String value = cleanParts[1];
                            if ("taskExecutionId".equals(key.trim()) && null != value) {
                                taskExecutionIdForSOAP = value.trim();
                            } else if ("jobExecutionId".equals(key.trim()) && null != value) {
                                jobExecutionIdForSOAP = value.trim();
                            }
                        }
                    } else if (arg.equalsIgnoreCase("--sealCounters=true")) {
                        sealCounters = true;
                    } else if (arg.equalsIgnoreCase("--skipCBPAuth=true")) {
                        skipCBPAuth = true;
                    } else if (arg.equalsIgnoreCase("--isSOAPService=true")) {
                        isSoapService = true;
                        skipCBPAuth = true;
                    }
                }
            }

            // first load default key-value pairs from application.properties
            if(isStandaloneMS) {
                context.putAll(this.getDefaultProperties());
            }
            //call job/subjob with an existing context, like: --context=production. if without this parameter, there will use the default context instead.
            java.io.InputStream inContext = load_dim_customer.class.getClassLoader().getResourceAsStream("capacitacion_talend/load_dim_customer_0_1/contexts/" + contextStr + ".properties");
            if (inContext == null) {
                inContext = load_dim_customer.class.getClassLoader().getResourceAsStream("config/contexts/" + contextStr + ".properties");
            }
            if (inContext != null) {
                try {
                    //defaultProps is in order to keep the original context value
                    if(context != null && context.isEmpty()) {
    	                defaultProps.load(inContext);
    	                if (inOSGi && jobProperties != null) {
                             java.util.Enumeration<String> keys = jobProperties.keys();
                             while (keys.hasMoreElements()) {
                                 String propKey = keys.nextElement();
                                 if (defaultProps.containsKey(propKey)) {
                                     defaultProps.put(propKey, (String) jobProperties.get(propKey));
                                 }
                             }
    	                }
    	                context = new ContextProperties(defaultProps);
                    }
                    if(isStandaloneMS) {
                        // override context key-value pairs if provided using --context=contextName
                        defaultProps.load(inContext);
                        context.putAll(defaultProps);
                    }
                } finally {
                    inContext.close();
                }
            } else if (!isDefaultContext) {
                //print info and job continue to run, for case: context_param is not empty.
                System.err.println("Could not find the context " + contextStr);
            }
            // override key-value pairs if provided via --config.location=file1.file2 OR --config.additional-location=file1,file2
            if(isStandaloneMS) {
                context.putAll(this.getAdditionalProperties());
            }
            
            // override key-value pairs if provide via command line like --key1=value1,--key2=value2
            if(!context_param.isEmpty()) {
                context.putAll(context_param);
				//set types for params from parentJobs
				for (Object key: context_param.keySet()){
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
            }

            class ContextProcessing {
                private void processContext_0() {
                        context.setContextType("DB_HOST", "id_String");
                        if(context.getStringValue("DB_HOST") == null) {
                            context.DB_HOST = null;
                        } else {
                            context.DB_HOST=(String) context.getProperty("DB_HOST");
                        }
                        context.setContextType("DB_NAME", "id_String");
                        if(context.getStringValue("DB_NAME") == null) {
                            context.DB_NAME = null;
                        } else {
                            context.DB_NAME=(String) context.getProperty("DB_NAME");
                        }
                        context.setContextType("DB_PASSWORD", "id_String");
                        if(context.getStringValue("DB_PASSWORD") == null) {
                            context.DB_PASSWORD = null;
                        } else {
                            context.DB_PASSWORD=(String) context.getProperty("DB_PASSWORD");
                        }
                        context.setContextType("DB_PORT", "id_String");
                        if(context.getStringValue("DB_PORT") == null) {
                            context.DB_PORT = null;
                        } else {
                            context.DB_PORT=(String) context.getProperty("DB_PORT");
                        }
                        context.setContextType("DB_USER", "id_String");
                        if(context.getStringValue("DB_USER") == null) {
                            context.DB_USER = null;
                        } else {
                            context.DB_USER=(String) context.getProperty("DB_USER");
                        }
                        context.setContextType("ENV_NAME", "id_String");
                        if(context.getStringValue("ENV_NAME") == null) {
                            context.ENV_NAME = null;
                        } else {
                            context.ENV_NAME=(String) context.getProperty("ENV_NAME");
                        }
                        context.setContextType("FILE_OLIST_CUSTOMERS", "id_String");
                        if(context.getStringValue("FILE_OLIST_CUSTOMERS") == null) {
                            context.FILE_OLIST_CUSTOMERS = null;
                        } else {
                            context.FILE_OLIST_CUSTOMERS=(String) context.getProperty("FILE_OLIST_CUSTOMERS");
                        }
                        context.setContextType("FILE_OLIST_ORDER_ITEMS", "id_String");
                        if(context.getStringValue("FILE_OLIST_ORDER_ITEMS") == null) {
                            context.FILE_OLIST_ORDER_ITEMS = null;
                        } else {
                            context.FILE_OLIST_ORDER_ITEMS=(String) context.getProperty("FILE_OLIST_ORDER_ITEMS");
                        }
                        context.setContextType("FILE_OLIST_ORDER_PAYMENTS", "id_String");
                        if(context.getStringValue("FILE_OLIST_ORDER_PAYMENTS") == null) {
                            context.FILE_OLIST_ORDER_PAYMENTS = null;
                        } else {
                            context.FILE_OLIST_ORDER_PAYMENTS=(String) context.getProperty("FILE_OLIST_ORDER_PAYMENTS");
                        }
                        context.setContextType("FILE_OLIST_ORDERS", "id_String");
                        if(context.getStringValue("FILE_OLIST_ORDERS") == null) {
                            context.FILE_OLIST_ORDERS = null;
                        } else {
                            context.FILE_OLIST_ORDERS=(String) context.getProperty("FILE_OLIST_ORDERS");
                        }
                        context.setContextType("FILE_OLIST_PRODUCTS", "id_String");
                        if(context.getStringValue("FILE_OLIST_PRODUCTS") == null) {
                            context.FILE_OLIST_PRODUCTS = null;
                        } else {
                            context.FILE_OLIST_PRODUCTS=(String) context.getProperty("FILE_OLIST_PRODUCTS");
                        }
                        context.setContextType("LOAD_SOURCE_TAG", "id_String");
                        if(context.getStringValue("LOAD_SOURCE_TAG") == null) {
                            context.LOAD_SOURCE_TAG = null;
                        } else {
                            context.LOAD_SOURCE_TAG=(String) context.getProperty("LOAD_SOURCE_TAG");
                        }
                        context.setContextType("PIPELINE_NAME", "id_String");
                        if(context.getStringValue("PIPELINE_NAME") == null) {
                            context.PIPELINE_NAME = null;
                        } else {
                            context.PIPELINE_NAME=(String) context.getProperty("PIPELINE_NAME");
                        }
                } 
                public void processAllContext() {
                        processContext_0();
                }
            }

            new ContextProcessing().processAllContext();
        } catch (java.io.IOException ie) {
            System.err.println("Could not load context "+contextStr);
            ie.printStackTrace();
        }

        // get context value from parent directly
        if (parentContextMap != null && !parentContextMap.isEmpty()) {if (parentContextMap.containsKey("DB_HOST")) {
                context.DB_HOST = (String) parentContextMap.get("DB_HOST");
            }if (parentContextMap.containsKey("DB_NAME")) {
                context.DB_NAME = (String) parentContextMap.get("DB_NAME");
            }if (parentContextMap.containsKey("DB_PASSWORD")) {
                context.DB_PASSWORD = (String) parentContextMap.get("DB_PASSWORD");
            }if (parentContextMap.containsKey("DB_PORT")) {
                context.DB_PORT = (String) parentContextMap.get("DB_PORT");
            }if (parentContextMap.containsKey("DB_USER")) {
                context.DB_USER = (String) parentContextMap.get("DB_USER");
            }if (parentContextMap.containsKey("ENV_NAME")) {
                context.ENV_NAME = (String) parentContextMap.get("ENV_NAME");
            }if (parentContextMap.containsKey("FILE_OLIST_CUSTOMERS")) {
                context.FILE_OLIST_CUSTOMERS = (String) parentContextMap.get("FILE_OLIST_CUSTOMERS");
            }if (parentContextMap.containsKey("FILE_OLIST_ORDER_ITEMS")) {
                context.FILE_OLIST_ORDER_ITEMS = (String) parentContextMap.get("FILE_OLIST_ORDER_ITEMS");
            }if (parentContextMap.containsKey("FILE_OLIST_ORDER_PAYMENTS")) {
                context.FILE_OLIST_ORDER_PAYMENTS = (String) parentContextMap.get("FILE_OLIST_ORDER_PAYMENTS");
            }if (parentContextMap.containsKey("FILE_OLIST_ORDERS")) {
                context.FILE_OLIST_ORDERS = (String) parentContextMap.get("FILE_OLIST_ORDERS");
            }if (parentContextMap.containsKey("FILE_OLIST_PRODUCTS")) {
                context.FILE_OLIST_PRODUCTS = (String) parentContextMap.get("FILE_OLIST_PRODUCTS");
            }if (parentContextMap.containsKey("LOAD_SOURCE_TAG")) {
                context.LOAD_SOURCE_TAG = (String) parentContextMap.get("LOAD_SOURCE_TAG");
            }if (parentContextMap.containsKey("PIPELINE_NAME")) {
                context.PIPELINE_NAME = (String) parentContextMap.get("PIPELINE_NAME");
            }
        }


        //Resume: init the resumeUtil
        resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
        resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
        resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
        //Resume: jobStart
        resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "","","","",ResumeUtil.convertToJsonText(context,ContextProperties.class,parametersToEncrypt));

            org.slf4j.MDC.put("_context", contextStr);
            log.info("TalendJob: 'load_dim_customer' - Started.");
            java.util.Optional.ofNullable(org.slf4j.MDC.getCopyOfContextMap()).ifPresent(mdcInfo::putAll);

if(execStat) {
    try {
        runStat.openSocket(!isChildJob);
        runStat.setAllPID(rootPid, fatherPid, pid, jobName);
        runStat.startThreadStat(clientHost, portStats);
        runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
    } catch (java.io.IOException ioException) {
        ioException.printStackTrace();
    }
}



	
	    java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
	    globalMap.put("concurrentHashMap", concurrentHashMap);
	

    long startUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    long endUsedMemory = 0;
    long end = 0;

    startTime = System.currentTimeMillis();


this.globalResumeTicket = true;//to run tPreJob




		if(enableLogStash) {
	        talendJobLog.addJobStartMessage();
	        try {
	            talendJobLogProcess(globalMap);
	        } catch (java.lang.Exception e) {
	            e.printStackTrace();
	        }
        }

this.globalResumeTicket = false;//to run others jobs

try {
errorCode = null;tFileInputDelimited_1Process(globalMap);
if(!"failure".equals(status)) { status = "end"; }
}catch (TalendException e_tFileInputDelimited_1) {
globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", -1);

e_tFileInputDelimited_1.printStackTrace();

}

this.globalResumeTicket = true;//to run tPostJob




        end = System.currentTimeMillis();

        if (watch) {
            System.out.println((end-startTime)+" milliseconds");
        }

        endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (false) {
            System.out.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : load_dim_customer");
        }
		if(enableLogStash) {
	        talendJobLog.addJobEndMessage(startTime, end, status);
	        try {
	            talendJobLogProcess(globalMap);
	        } catch (java.lang.Exception e) {
	            e.printStackTrace();
	        }
        }



if (execStat) {
    runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
    runStat.stopThreadStat();
}
    
    if(!isChildJob) {
        //TODO TMC need to make sure the key or provide a way to pass it to job, consider java command length too long risk in windows OS
        String taskId = System.getProperty("tmc.task.id");//not the same key with CBP
        String taskVersion = System.getProperty("tmc.task.version");
        String taskExecId = System.getProperty("tmc.task.execution.id");
        String lineageFilePath = System.getProperty("runtime.lineage.appender.file.path");
        try {
            org.talend.studio.ProducerHelper.newPayload(payloadFilePath, lineageFilePath, taskExecId, taskId, taskVersion, lineageDirPath == null ? true : false);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    int returnCode = 0;


    if (!inOSGi) {
        if(needSendForCBP && org.talend.metrics.CBPClient.getInstanceForCurrentVM() != null) {
            s("none");
            if(!isChildJob) {
                org.talend.metrics.CBPClient.getInstanceForCurrentVM().setStatus(status);
            }
            org.talend.metrics.CBPClient.getInstanceForCurrentVM().sendData();
        }
    }

    if(errorCode == null) {
         returnCode = status != null && status.equals("failure") ? 1 : 0;
    } else {
         returnCode = errorCode.intValue();
    }
    resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "","" + returnCode,"","","");
    resumeUtil.flush();


        org.slf4j.MDC.remove("_subJobName");
        org.slf4j.MDC.remove("_subJobPid");
        org.slf4j.MDC.remove("_systemPid");
        log.info("TalendJob: 'load_dim_customer' - Finished - status: " + status + " returnCode: " + returnCode );

    return returnCode;

  }

    // only for OSGi env
    public void destroy() {

    // add CBP code for OSGI Executions
    if (sealCounters && jobExecutionId != null) {
        // call to checkAuthorization was made or was being made
        try {
	       org.talend.metrics.DataReadTracker.setExecutionId(taskExecutionId, jobExecutionId, false);
	       org.talend.metrics.DataReadTracker.sealCounter();
	       org.talend.metrics.DataReadTracker.reset();
	    } catch (Exception | NoClassDefFoundError e) {
	       // ignore
	    }
	}

	// check for orphan threads if still alive after undeploy
	for (java.util.Iterator<Thread> it = threadList.iterator(); it.hasNext();) {
	    Thread thread = it.next();
	    if (thread != null && thread.isAlive()) {
	        boolean suppressLogs = Boolean.parseBoolean(System.getProperty("thread.terminate.logs.suppress", "false"));
	        if (!suppressLogs) {
	            System.err.println("Initiating thread cleanup prior to bundle undeployment. This is a precautionary step to ensure no memory leaks.");
	            System.err.println("Forcefully interrupting thread with ID = " + thread.getId() + ". This may result in expected errors due to abrupt termination. Please verify if the thread was performing critical operations.");
	        }
	        thread.interrupt();
	    }
	    it.remove();
	}
	// end of destroy()
    }













    private java.util.Map<String, Object> getSharedConnections4REST() {
        java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();






        return connections;
    }

    private void evalParam(String arg) {
        if (arg.startsWith("--resuming_logs_dir_path")) {
            resuming_logs_dir_path = arg.substring(25);
        } else if (arg.startsWith("--resuming_checkpoint_path")) {
            resuming_checkpoint_path = arg.substring(27);
        } else if (arg.startsWith("--parent_part_launcher")) {
            parent_part_launcher = arg.substring(23);
        } else if (arg.startsWith("--watch")) {
            watch = true;
        } else if (arg.startsWith("--stat_port=")) {
            String portStatsStr = arg.substring(12);
            if (portStatsStr != null && !portStatsStr.equals("null")) {
                portStats = Integer.parseInt(portStatsStr);
            }
        } else if (arg.startsWith("--trace_port=")) {
            portTraces = Integer.parseInt(arg.substring(13));
        } else if (arg.startsWith("--client_host=")) {
            clientHost = arg.substring(14);
        } else if (arg.startsWith("--context=")) {
            contextStr = arg.substring(10);
            isDefaultContext = false;
        } else if (arg.startsWith("--father_pid=")) {
            fatherPid = arg.substring(13);
        } else if (arg.startsWith("--root_pid=")) {
            rootPid = arg.substring(11);
        } else if (arg.startsWith("--father_node=")) {
            fatherNode = arg.substring(14);
        } else if (arg.startsWith("--pid=")) {
            pid = arg.substring(6);
        } else if (arg.startsWith("--context_type")) {
            String keyValue = arg.substring(15);
			int index = -1;
            if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
                if (fatherPid==null) {
                    context_param.setContextType(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
                } else { // the subjob won't escape the especial chars
                    context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1) );
                }

            }

		} else if (arg.startsWith("--context_param")) {
            String keyValue = arg.substring(16);
            int index = -1;
            if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
                if (fatherPid==null) {
                    context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
                } else { // the subjob won't escape the especial chars
                    context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1) );
                }
            }
        } else if (arg.startsWith("--context_file")) {
        	String keyValue = arg.substring(15);
        	String filePath = new String(java.util.Base64.getDecoder().decode(keyValue));
        	java.nio.file.Path contextFile = java.nio.file.Paths.get(filePath);
            try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(contextFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int index = -1;
                    if ( (index = line.indexOf('=')) > -1) {
							if (line.startsWith("--context_param")) {
								if ("id_Password".equals(context_param.getContextType(line.substring(16, index)))) {
									context_param.put(line.substring(16, index), routines.system.PasswordEncryptUtil.decryptPassword(
											line.substring(index + 1)));
								} else {
									context_param.put(line.substring(16, index), line.substring(index + 1));
								}
							}else {//--context_type
								context_param.setContextType(line.substring(15, index), line.substring(index + 1));
							}
                    }
                }
            } catch (java.io.IOException e) {
            	System.err.println("Could not load the context file: " + filePath);
                e.printStackTrace();
            }
        } else if (arg.startsWith("--log4jLevel=")) {
            log4jLevel = arg.substring(13);
		} else if (arg.startsWith("--audit.enabled") && arg.contains("=")) {//for trunjob call
		    final int equal = arg.indexOf('=');
			final String key = arg.substring("--".length(), equal);
			System.setProperty(key, arg.substring(equal + 1));
		}
    }
    
    private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

    private final String[][] escapeChars = {
        {"\\\\","\\"},{"\\n","\n"},{"\\'","\'"},{"\\r","\r"},
        {"\\f","\f"},{"\\b","\b"},{"\\t","\t"}
        };
    private String replaceEscapeChars (String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0],currIndex);
				if (index>=0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0], strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
    }

    public Integer getErrorCode() {
        return errorCode;
    }


    public String getStatus() {
        return status;
    }

    ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 *     189435 characters generated by Qlik Talend Cloud Enterprise Edition 
 *     on the 23 de octubre de 2026, 17:02:37 ECT
 ************************************************************************************************/