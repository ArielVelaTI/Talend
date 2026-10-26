
package capacitacion_talend.mp01_input_port_olist_0_1;

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

//the import part of tJavaRow_1
//import java.util.List;

@SuppressWarnings("unused")

/**
 * Job: mp01_input_port_olist Purpose: Integrar y automatizar la carga de datos
 * Olist hacia SingleStore.<br>
 * Description: Job orquestador del Input Port Olist que ejecuta cinco Jobs
 * hijos para cargar clientes, productos, órdenes, detalles y pagos en
 * SingleStore. Gestiona contextos DEV, QA y PROD, validaciones, registros
 * rechazados y auditoría de ejecuciones. <br>
 * 
 * @author Chango, Kevin
 * @version 8.0.1.20260619_1127-patch
 * @status
 */
public class mp01_input_port_olist implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "mp01_input_port_olist.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(mp01_input_port_olist.class);

	static {
		System.setProperty("talend.component.record.nullable.check", "true");
		String javaUtilLoggingConfigFile = System.getProperty("java.util.logging.config.file");
		if (javaUtilLoggingConfigFile == null) {
			setupDefaultJavaUtilLogging();
		}
	}

	/**
	 * This class replaces the default {@code System.err} stream used by Java Util
	 * Logging (JUL). You can use your own configuration through the
	 * {@code java.util.logging.config.file} system property, enabling you to
	 * specify an external logging configuration file for tailored logging setup.
	 */
	public static class StandardConsoleHandler extends java.util.logging.StreamHandler {
		public StandardConsoleHandler() {
			// Set System.out as default log output stream
			super(System.out, new java.util.logging.SimpleFormatter());
		}

		/**
		 * Publish a {@code LogRecord}. The logging request was made initially to a
		 * {@code Logger} object, which initialized the {@code LogRecord} and forwarded
		 * it here.
		 *
		 * @param record description of the log event. A null record is silently ignored
		 *               and is not published
		 */
		@Override
		public void publish(java.util.logging.LogRecord record) {
			super.publish(record);
			flush();
		}

		/**
		 * Override {@code StreamHandler.close} to do a flush but not to close the
		 * output stream. That is, we do <b>not</b> close {@code System.out}.
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

	// volatile is needed for OSGi: the thread that calls the blueprint destroy
	// method is not the same that sets the values
	public static volatile String taskExecutionId = null;
	public static volatile String jobExecutionId = null;
	public static volatile boolean sealCounters = false;

	public static final java.util.Set<Thread> threadList = java.util.Collections
			.newSetFromMap(new java.util.concurrent.ConcurrentHashMap<>());

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
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

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

			if (DB_HOST != null) {

				this.setProperty("DB_HOST", DB_HOST.toString());

			}

			if (DB_NAME != null) {

				this.setProperty("DB_NAME", DB_NAME.toString());

			}

			if (DB_PASSWORD != null) {

				this.setProperty("DB_PASSWORD", DB_PASSWORD.toString());

			}

			if (DB_PORT != null) {

				this.setProperty("DB_PORT", DB_PORT.toString());

			}

			if (DB_USER != null) {

				this.setProperty("DB_USER", DB_USER.toString());

			}

			if (ENV_NAME != null) {

				this.setProperty("ENV_NAME", ENV_NAME.toString());

			}

			if (FILE_OLIST_CUSTOMERS != null) {

				this.setProperty("FILE_OLIST_CUSTOMERS", FILE_OLIST_CUSTOMERS.toString());

			}

			if (FILE_OLIST_ORDER_ITEMS != null) {

				this.setProperty("FILE_OLIST_ORDER_ITEMS", FILE_OLIST_ORDER_ITEMS.toString());

			}

			if (FILE_OLIST_ORDER_PAYMENTS != null) {

				this.setProperty("FILE_OLIST_ORDER_PAYMENTS", FILE_OLIST_ORDER_PAYMENTS.toString());

			}

			if (FILE_OLIST_ORDERS != null) {

				this.setProperty("FILE_OLIST_ORDERS", FILE_OLIST_ORDERS.toString());

			}

			if (FILE_OLIST_PRODUCTS != null) {

				this.setProperty("FILE_OLIST_PRODUCTS", FILE_OLIST_PRODUCTS.toString());

			}

			if (LOAD_SOURCE_TAG != null) {

				this.setProperty("LOAD_SOURCE_TAG", LOAD_SOURCE_TAG.toString());

			}

			if (PIPELINE_NAME != null) {

				this.setProperty("PIPELINE_NAME", PIPELINE_NAME.toString());

			}

			if (RUN_ID != null) {

				this.setProperty("RUN_ID", RUN_ID.toString());

			}

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

		public String DB_HOST;

		public String getDB_HOST() {
			return this.DB_HOST;
		}

		public String DB_NAME;

		public String getDB_NAME() {
			return this.DB_NAME;
		}

		public String DB_PASSWORD;

		public String getDB_PASSWORD() {
			return this.DB_PASSWORD;
		}

		public String DB_PORT;

		public String getDB_PORT() {
			return this.DB_PORT;
		}

		public String DB_USER;

		public String getDB_USER() {
			return this.DB_USER;
		}

		public String ENV_NAME;

		public String getENV_NAME() {
			return this.ENV_NAME;
		}

		public String FILE_OLIST_CUSTOMERS;

		public String getFILE_OLIST_CUSTOMERS() {
			return this.FILE_OLIST_CUSTOMERS;
		}

		public String FILE_OLIST_ORDER_ITEMS;

		public String getFILE_OLIST_ORDER_ITEMS() {
			return this.FILE_OLIST_ORDER_ITEMS;
		}

		public String FILE_OLIST_ORDER_PAYMENTS;

		public String getFILE_OLIST_ORDER_PAYMENTS() {
			return this.FILE_OLIST_ORDER_PAYMENTS;
		}

		public String FILE_OLIST_ORDERS;

		public String getFILE_OLIST_ORDERS() {
			return this.FILE_OLIST_ORDERS;
		}

		public String FILE_OLIST_PRODUCTS;

		public String getFILE_OLIST_PRODUCTS() {
			return this.FILE_OLIST_PRODUCTS;
		}

		public String LOAD_SOURCE_TAG;

		public String getLOAD_SOURCE_TAG() {
			return this.LOAD_SOURCE_TAG;
		}

		public String PIPELINE_NAME;

		public String getPIPELINE_NAME() {
			return this.PIPELINE_NAME;
		}

		public Long RUN_ID;

		public Long getRUN_ID() {
			return this.RUN_ID;
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
	private final String jobName = "mp01_input_port_olist";
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

	private String cLabel = null;

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private final JobStructureCatcherUtils talendJobLog = new JobStructureCatcherUtils(jobName,
			"_07K9cM7yEfGK8YS6DqfsfQ", "0.1");
	private org.talend.job.audit.JobAuditLogger runtime_lineage_logger_talendJobLog = null;
	private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

	private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	public void setDataSourceReferences(List serviceReferences) throws Exception {

		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		java.util.Map<String, javax.sql.DataSource> dataSources = new java.util.HashMap<String, javax.sql.DataSource>();

		for (java.util.Map.Entry<String, javax.sql.DataSource> entry : BundleUtils
				.getServices(serviceReferences, javax.sql.DataSource.class).entrySet()) {
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
		private String cLabel = null;

		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		private TalendException(Exception e, String errorComponent, String errorComponentLabel,
				final java.util.Map<String, Object> globalMap) {
			this(e, errorComponent, globalMap);
			this.cLabel = errorComponentLabel;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
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
				if (virtualComponentName != null && currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
				}
			}
			if (!(e instanceof TalendException)) {
				mp01_input_port_olist.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(mp01_input_port_olist.this, new Object[] { e, currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						if (enableLogStash) {
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

	public void tPostjob_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tPostjob_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBClose_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBClose_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tPrejob_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tPrejob_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBConnection_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBConnection_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBInput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tJavaRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRunJob_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		tRunJob_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRunJob_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		tRunJob_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRunJob_3_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		tRunJob_3_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRunJob_4_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		tRunJob_4_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRunJob_5_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		tRunJob_5_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_3_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_3_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_6_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_6_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_8_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_8_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_7_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_7_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_5_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_5_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBRow_4_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBRow_4_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tPostjob_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBClose_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tPrejob_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBConnection_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBInput_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_2_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tRunJob_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "ERROR", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

		try {

			if (this.execStat) {
				runStat.updateStatOnConnection("OnSubjobError1", 0, "error");
			}

			errorCode = null;
			tDBRow_4Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tRunJob_2_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "ERROR", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

		try {

			if (this.execStat) {
				runStat.updateStatOnConnection("OnSubjobError2", 0, "error");
			}

			errorCode = null;
			tDBRow_5Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tRunJob_3_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "ERROR", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

		try {

			if (this.execStat) {
				runStat.updateStatOnConnection("OnSubjobError4", 0, "error");
			}

			errorCode = null;
			tDBRow_7Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tRunJob_4_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "ERROR", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

		try {

			if (this.execStat) {
				runStat.updateStatOnConnection("OnSubjobError5", 0, "error");
			}

			errorCode = null;
			tDBRow_8Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tRunJob_5_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "ERROR", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

		try {

			if (this.execStat) {
				runStat.updateStatOnConnection("OnSubjobError3", 0, "error");
			}

			errorCode = null;
			tDBRow_6Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tDBRow_3_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_6_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_8_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_7_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_5_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tDBRow_4_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tPostjob_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tPostjob_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tPostjob_1", "pfkhhF_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tPostjob_1 begin ] start
				 */

				sh("tPostjob_1");

				s(currentComponent = "tPostjob_1");

				int tos_count_tPostjob_1 = 0;

				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tPostjob_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

							} catch (java.lang.Exception e_tPostjob_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tPostjob_1", "tPostjob",
							new ParameterUtil_tPostjob_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tPostjob_1", "tPostjob_1", "tPostjob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tPostjob_1 begin ] stop
				 */

				/**
				 * [tPostjob_1 main ] start
				 */

				s(currentComponent = "tPostjob_1");

				tos_count_tPostjob_1++;

				/**
				 * [tPostjob_1 main ] stop
				 */

				/**
				 * [tPostjob_1 process_data_begin ] start
				 */

				s(currentComponent = "tPostjob_1");

				/**
				 * [tPostjob_1 process_data_begin ] stop
				 */

				/**
				 * [tPostjob_1 process_data_end ] start
				 */

				s(currentComponent = "tPostjob_1");

				/**
				 * [tPostjob_1 process_data_end ] stop
				 */

				/**
				 * [tPostjob_1 end ] start
				 */

				s(currentComponent = "tPostjob_1");

				ok_Hash.put("tPostjob_1", true);
				end_Hash.put("tPostjob_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk6", 0, "ok");
				}
				tDBClose_1Process(globalMap);

				/**
				 * [tPostjob_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tPostjob_1 finally ] start
				 */

				s(currentComponent = "tPostjob_1");

				/**
				 * [tPostjob_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tPostjob_1_SUBPROCESS_STATE", 1);
	}

	public void tDBClose_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBClose_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBClose_1", "ygTpqB_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBClose_1 begin ] start
				 */

				sh("tDBClose_1");

				s(currentComponent = "tDBClose_1");

				int tos_count_tDBClose_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBClose_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBClose_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBClose_1 = new StringBuilder();
							log4jParamters_tDBClose_1.append("Parameters:");
							log4jParamters_tDBClose_1.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBClose_1.append(" | ");
							log4jParamters_tDBClose_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlClose");
							log4jParamters_tDBClose_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBClose_1 - " + (log4jParamters_tDBClose_1));
						}
					}
					new BytesLimit65535_tDBClose_1().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBClose_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlClose"));

							} catch (java.lang.Exception e_tDBClose_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBClose_1", "tMysqlClose",
							new ParameterUtil_tDBClose_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBClose_1", "tDBClose_1", "tMysqlClose");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tDBClose_1 begin ] stop
				 */

				/**
				 * [tDBClose_1 main ] start
				 */

				s(currentComponent = "tDBClose_1");

				java.sql.Connection conn_tDBClose_1 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (conn_tDBClose_1 != null && !conn_tDBClose_1.isClosed()) {

					log.debug("tDBClose_1 - Closing the connection 'tDBConnection_1' to the database.");

					conn_tDBClose_1.close();

					if ("com.mysql.cj.jdbc.Driver".equals((String) globalMap.get("driverClass_tDBConnection_1"))
							&& routines.system.BundleUtils.inOSGi()) {
						Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread").getMethod("checkedShutdown")
								.invoke(null, (Object[]) null);
					}

					log.debug("tDBClose_1 - Connection 'tDBConnection_1' to the database closed.");

				}

				tos_count_tDBClose_1++;

				/**
				 * [tDBClose_1 main ] stop
				 */

				/**
				 * [tDBClose_1 process_data_begin ] start
				 */

				s(currentComponent = "tDBClose_1");

				/**
				 * [tDBClose_1 process_data_begin ] stop
				 */

				/**
				 * [tDBClose_1 process_data_end ] start
				 */

				s(currentComponent = "tDBClose_1");

				/**
				 * [tDBClose_1 process_data_end ] stop
				 */

				/**
				 * [tDBClose_1 end ] start
				 */

				s(currentComponent = "tDBClose_1");

				if (log.isDebugEnabled())
					log.debug("tDBClose_1 - " + ("Done."));

				ok_Hash.put("tDBClose_1", true);
				end_Hash.put("tDBClose_1", System.currentTimeMillis());

				/**
				 * [tDBClose_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBClose_1 finally ] start
				 */

				s(currentComponent = "tDBClose_1");

				/**
				 * [tDBClose_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBClose_1_SUBPROCESS_STATE", 1);
	}

	public void tPrejob_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tPrejob_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tPrejob_1", "6OFXVw_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tPrejob_1 begin ] start
				 */

				sh("tPrejob_1");

				s(currentComponent = "tPrejob_1");

				int tos_count_tPrejob_1 = 0;

				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tPrejob_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

							} catch (java.lang.Exception e_tPrejob_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tPrejob_1", "tPrejob",
							new ParameterUtil_tPrejob_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tPrejob_1", "tPrejob_1", "tPrejob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tPrejob_1 begin ] stop
				 */

				/**
				 * [tPrejob_1 main ] start
				 */

				s(currentComponent = "tPrejob_1");

				tos_count_tPrejob_1++;

				/**
				 * [tPrejob_1 main ] stop
				 */

				/**
				 * [tPrejob_1 process_data_begin ] start
				 */

				s(currentComponent = "tPrejob_1");

				/**
				 * [tPrejob_1 process_data_begin ] stop
				 */

				/**
				 * [tPrejob_1 process_data_end ] start
				 */

				s(currentComponent = "tPrejob_1");

				/**
				 * [tPrejob_1 process_data_end ] stop
				 */

				/**
				 * [tPrejob_1 end ] start
				 */

				s(currentComponent = "tPrejob_1");

				ok_Hash.put("tPrejob_1", true);
				end_Hash.put("tPrejob_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk1", 0, "ok");
				}
				tDBConnection_1Process(globalMap);

				/**
				 * [tPrejob_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tPrejob_1 finally ] start
				 */

				s(currentComponent = "tPrejob_1");

				/**
				 * [tPrejob_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tPrejob_1_SUBPROCESS_STATE", 1);
	}

	public void tDBConnection_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBConnection_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBConnection_1", "aoWhn7_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBConnection_1 begin ] start
				 */

				sh("tDBConnection_1");

				s(currentComponent = "tDBConnection_1");

				int tos_count_tDBConnection_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBConnection_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBConnection_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBConnection_1 = new StringBuilder();
							log4jParamters_tDBConnection_1.append("Parameters:");
							log4jParamters_tDBConnection_1.append("DB_VERSION" + " = " + "MYSQL_8");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("HOST" + " = " + "context.DB_HOST");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("PORT" + " = " + "context.DB_PORT");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("DBNAME" + " = " + "context.DB_NAME");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1
									.append("PROPERTIES" + " = " + "\"noDatetimeStringSync=true\"");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("USER" + " = " + "context.DB_USER");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("PASS" + " = "
									+ String.valueOf(
											routines.system.PasswordEncryptUtil.encryptPassword(context.DB_PASSWORD))
											.substring(0, 4)
									+ "...");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("USE_SHARED_CONNECTION" + " = " + "false");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("AUTO_COMMIT" + " = " + "true");
							log4jParamters_tDBConnection_1.append(" | ");
							log4jParamters_tDBConnection_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlConnection");
							log4jParamters_tDBConnection_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBConnection_1 - " + (log4jParamters_tDBConnection_1));
						}
					}
					new BytesLimit65535_tDBConnection_1().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBConnection_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("DB_VERSION", String.valueOf("MYSQL_8"));
								component_parameters.put("HOST", String.valueOf(context.DB_HOST));
								component_parameters.put("PORT", String.valueOf(context.DB_PORT));
								component_parameters.put("DBNAME", String.valueOf(context.DB_NAME));
								component_parameters.put("PROPERTIES", String.valueOf("noDatetimeStringSync=true"));
								component_parameters.put("USER", String.valueOf(context.DB_USER));
								component_parameters.put("USE_SHARED_CONNECTION", String.valueOf("false"));
								component_parameters.put("SPECIFY_DATASOURCE_ALIAS", String.valueOf("false"));
								component_parameters.put("AUTO_COMMIT", String.valueOf("true"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlConnection"));

							} catch (java.lang.Exception e_tDBConnection_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBConnection_1", "tMysqlConnection",
							new ParameterUtil_tDBConnection_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBConnection_1", "tDBConnection_1", "tMysqlConnection");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				String properties_tDBConnection_1 = "noDatetimeStringSync=true";
				if (properties_tDBConnection_1 == null || properties_tDBConnection_1.trim().length() == 0) {
					properties_tDBConnection_1 = "rewriteBatchedStatements=true&allowLoadLocalInfile=true";
				} else {
					if (!properties_tDBConnection_1.contains("rewriteBatchedStatements=")) {
						properties_tDBConnection_1 += "&rewriteBatchedStatements=true";
					}

					if (!properties_tDBConnection_1.contains("allowLoadLocalInfile=")) {
						properties_tDBConnection_1 += "&allowLoadLocalInfile=true";
					}
				}

				String url_tDBConnection_1 = "jdbc:mysql://" + context.DB_HOST + ":" + context.DB_PORT + "/"
						+ context.DB_NAME + "?" + properties_tDBConnection_1;
				String dbUser_tDBConnection_1 = context.DB_USER;

				final String decryptedPassword_tDBConnection_1 = java.util.Optional.ofNullable(context.DB_PASSWORD)
						.orElse("");
				String dbPwd_tDBConnection_1 = decryptedPassword_tDBConnection_1;

				java.sql.Connection conn_tDBConnection_1 = null;

				String driverClass_tDBConnection_1 = com.mysql.cj.jdbc.Driver.class.getName();
				java.lang.Class jdbcclazz_tDBConnection_1 = java.lang.Class.forName(driverClass_tDBConnection_1);
				globalMap.put("driverClass_tDBConnection_1", driverClass_tDBConnection_1);

				log.debug("tDBConnection_1 - Driver ClassName: " + driverClass_tDBConnection_1 + ".");

				log.debug("tDBConnection_1 - Connection attempt to '"
						+ url_tDBConnection_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********")
						+ "' with the username '" + dbUser_tDBConnection_1 + "'.");

				conn_tDBConnection_1 = java.sql.DriverManager.getConnection(url_tDBConnection_1, dbUser_tDBConnection_1,
						dbPwd_tDBConnection_1);
				log.debug("tDBConnection_1 - Connection to '"
						+ url_tDBConnection_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********")
						+ "' has succeeded.");

				globalMap.put("conn_tDBConnection_1", conn_tDBConnection_1);
				if (null != conn_tDBConnection_1) {

					log.debug("tDBConnection_1 - Connection is set auto commit to 'true'.");
					conn_tDBConnection_1.setAutoCommit(true);
				}

				globalMap.put("db_tDBConnection_1", context.DB_NAME);

				/**
				 * [tDBConnection_1 begin ] stop
				 */

				/**
				 * [tDBConnection_1 main ] start
				 */

				s(currentComponent = "tDBConnection_1");

				tos_count_tDBConnection_1++;

				/**
				 * [tDBConnection_1 main ] stop
				 */

				/**
				 * [tDBConnection_1 process_data_begin ] start
				 */

				s(currentComponent = "tDBConnection_1");

				/**
				 * [tDBConnection_1 process_data_begin ] stop
				 */

				/**
				 * [tDBConnection_1 process_data_end ] start
				 */

				s(currentComponent = "tDBConnection_1");

				/**
				 * [tDBConnection_1 process_data_end ] stop
				 */

				/**
				 * [tDBConnection_1 end ] start
				 */

				s(currentComponent = "tDBConnection_1");

				if (log.isDebugEnabled())
					log.debug("tDBConnection_1 - " + ("Done."));

				ok_Hash.put("tDBConnection_1", true);
				end_Hash.put("tDBConnection_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk2", 0, "ok");
				}
				tDBRow_1Process(globalMap);

				/**
				 * [tDBConnection_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBConnection_1 finally ] start
				 */

				s(currentComponent = "tDBConnection_1");

				/**
				 * [tDBConnection_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBConnection_1_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_1", "L1StbH_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_1 begin ] start
				 */

				sh("tDBRow_1");

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				int tos_count_tDBRow_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_1 = new StringBuilder();
							log4jParamters_tDBRow_1.append("Parameters:");
							log4jParamters_tDBRow_1.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("QUERY" + " = "
									+ "\"INSERT INTO DSADEV.audit_pipeline_runs_gr5 \" + \"(pipeline_name, subjob_name, rows_in, rows_out, rows_rejected, status, started_at, notes) \" + \"VALUES ('mp01_input_port_olist', 'ORCHESTRATOR', 0, 0, 0, 'RUNNING', NOW(), 'Inicio MP01 equipo 5')\"");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_1.append(" | ");
							log4jParamters_tDBRow_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_1 - " + (log4jParamters_tDBRow_1));
						}
					}
					new BytesLimit65535_tDBRow_1().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("INSERT INTO DSADEV.audit_pipeline_runs_gr5 "
												+ "(pipeline_name, subjob_name, rows_in, rows_out, rows_rejected, status, started_at, notes) "
												+ "VALUES ('mp01_input_port_olist', 'ORCHESTRATOR', 0, 0, 0, 'RUNNING', NOW(), 'Inicio MP01 equipo 5')"));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_1", "tMysqlRow",
							new ParameterUtil_tDBRow_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_1", "Auditor\u00EDa RUNNING", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_1 = null;
				String query_tDBRow_1 = "";
				boolean whetherReject_tDBRow_1 = false;
				int count_tDBRow_1 = 0;
				conn_tDBRow_1 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_1 != null) {
					if (conn_tDBRow_1.getMetaData() != null) {

						log.debug("tDBRow_1 - Uses an existing connection with username '"
								+ conn_tDBRow_1.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_1.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_1", conn_tDBRow_1);
				java.sql.Statement stmt_tDBRow_1 = conn_tDBRow_1.createStatement();
				resourceMap.put("stmt_tDBRow_1", stmt_tDBRow_1);

				/**
				 * [tDBRow_1 begin ] stop
				 */

				/**
				 * [tDBRow_1 main ] start
				 */

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				query_tDBRow_1 = "INSERT INTO DSADEV.audit_pipeline_runs_gr5 "
						+ "(pipeline_name, subjob_name, rows_in, rows_out, rows_rejected, status, started_at, notes) "
						+ "VALUES ('mp01_input_port_olist', 'ORCHESTRATOR', 0, 0, 0, 'RUNNING', NOW(), 'Inicio MP01 equipo 5')";
				whetherReject_tDBRow_1 = false;
				log.debug("tDBRow_1 - Executing the query: '" + query_tDBRow_1 + "'.");

				globalMap.put("tDBRow_1_QUERY", query_tDBRow_1);
				try {
					stmt_tDBRow_1.execute(query_tDBRow_1);
					log.debug("tDBRow_1 - Execute the query: '" + query_tDBRow_1 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_1 = true;

					throw (e);

				}

				if (!whetherReject_tDBRow_1) {

				}

				tos_count_tDBRow_1++;

				/**
				 * [tDBRow_1 main ] stop
				 */

				/**
				 * [tDBRow_1 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				/**
				 * [tDBRow_1 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_1 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				/**
				 * [tDBRow_1 process_data_end ] stop
				 */

				/**
				 * [tDBRow_1 end ] start
				 */

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				globalMap.put("tDBRow_1_NB_LINE", count_tDBRow_1);
				stmt_tDBRow_1.close();
				resourceMap.remove("stmt_tDBRow_1");
				resourceMap.put("statementClosed_tDBRow_1", true);
				resourceMap.put("finish_tDBRow_1", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_1 - " + ("Done."));

				ok_Hash.put("tDBRow_1", true);
				end_Hash.put("tDBRow_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk3", 0, "ok");
				}
				tDBInput_1Process(globalMap);

				/**
				 * [tDBRow_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_1 finally ] start
				 */

				s(currentComponent = "tDBRow_1");

				cLabel = "Auditor\u00EDa RUNNING";

				if (resourceMap.get("statementClosed_tDBRow_1") == null) {
					java.sql.Statement stmtToClose_tDBRow_1 = null;
					if ((stmtToClose_tDBRow_1 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_1")) != null) {
						stmtToClose_tDBRow_1.close();
					}
				}

				/**
				 * [tDBRow_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_1_SUBPROCESS_STATE", 1);
	}

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_CAPACITACION_TALEND_mp01_input_port_olist = new byte[0];
		static byte[] commonByteArray_CAPACITACION_TALEND_mp01_input_port_olist = new byte[0];

		public Long run_id;

		public Long getRun_id() {
			return this.run_id;
		}

		public Boolean run_idIsNullable() {
			return true;
		}

		public Boolean run_idIsKey() {
			return false;
		}

		public Integer run_idLength() {
			return 20;
		}

		public Integer run_idPrecision() {
			return 0;
		}

		public String run_idDefault() {

			return null;

		}

		public String run_idComment() {

			return "";

		}

		public String run_idPattern() {

			return "";

		}

		public String run_idOriginalDbColumnName() {

			return "run_id";

		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_mp01_input_port_olist) {

				try {

					int length = 0;

					length = dis.readByte();
					if (length == -1) {
						this.run_id = null;
					} else {
						this.run_id = dis.readLong();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_mp01_input_port_olist) {

				try {

					int length = 0;

					length = dis.readByte();
					if (length == -1) {
						this.run_id = null;
					} else {
						this.run_id = dis.readLong();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Long

				if (this.run_id == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.run_id);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Long

				if (this.run_id == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.run_id);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("run_id=" + String.valueOf(run_id));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (run_id == null) {
				sb.append("<null>");
			} else {
				sb.append(run_id);
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

	public void tDBInput_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBInput_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBInput_1", "AIO4dW_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();

				/**
				 * [tJavaRow_1 begin ] start
				 */

				sh("tJavaRow_1");

				s(currentComponent = "tJavaRow_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tJavaRow_1 = 0;

				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tJavaRow_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter(final row1Struct row1) throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("CODE", String.valueOf("context.RUN_ID = input_row.run_id;"));
								component_parameters.put("IMPORT", String.valueOf("//import java.util.List;"));

							} catch (java.lang.Exception e_tJavaRow_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tJavaRow_1", "tJavaRow",
							new ParameterUtil_tJavaRow_1().getParameter(row1));
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tJavaRow_1", "tJavaRow_1", "tJavaRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				int nb_line_tJavaRow_1 = 0;

				/**
				 * [tJavaRow_1 begin ] stop
				 */

				/**
				 * [tDBInput_1 begin ] start
				 */

				sh("tDBInput_1");

				s(currentComponent = "tDBInput_1");

				int tos_count_tDBInput_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBInput_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBInput_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBInput_1 = new StringBuilder();
							log4jParamters_tDBInput_1.append("Parameters:");
							log4jParamters_tDBInput_1.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TABLE" + " = " + "\"\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERY" + " = " + "\"SELECT LAST_INSERT_ID() AS run_id\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("ENABLE_STREAM" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_ALL_COLUMN" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_COLUMN" + " = " + "[{TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("run_id") + "}]");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlInput");
							log4jParamters_tDBInput_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBInput_1 - " + (log4jParamters_tDBInput_1));
						}
					}
					new BytesLimit65535_tDBInput_1().limitLog4jByte();
				}
				boolean init_tDBInput_1_0 = true;
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBInput_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("TABLE", String.valueOf(""));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY", String.valueOf("SELECT LAST_INSERT_ID() AS run_id"));
								component_parameters.put("ENABLE_STREAM", String.valueOf("false"));
								component_parameters.put("TRIM_ALL_COLUMN", String.valueOf("false"));
								component_parameters.put("TRIM_COLUMN",
										String.valueOf(new StringBuilder().append("[{TRIM=").append("false")
												.append(", SCHEMA_COLUMN=").append("run_id").append("}]").toString()));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlInput"));

							} catch (java.lang.Exception e_tDBInput_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBInput_1", "tMysqlInput",
							new ParameterUtil_tDBInput_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBInput_1", "tDBInput_1", "tMysqlInput");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.util.Calendar calendar_tDBInput_1 = java.util.Calendar.getInstance();
				calendar_tDBInput_1.set(0, 0, 0, 0, 0, 0);
				java.util.Date year0_tDBInput_1 = calendar_tDBInput_1.getTime();
				int nb_line_tDBInput_1 = 0;
				java.sql.Connection conn_tDBInput_1 = null;
				conn_tDBInput_1 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBInput_1 != null) {
					if (conn_tDBInput_1.getMetaData() != null) {

						log.debug("tDBInput_1 - Uses an existing connection with username '"
								+ conn_tDBInput_1.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBInput_1.getMetaData().getURL() + ".");

					}
				}

				java.sql.Statement stmt_tDBInput_1 = conn_tDBInput_1.createStatement();

				String dbquery_tDBInput_1 = "SELECT LAST_INSERT_ID() AS run_id";

				log.debug("tDBInput_1 - Executing the query: '" + dbquery_tDBInput_1 + "'.");

				globalMap.put("tDBInput_1_QUERY", dbquery_tDBInput_1);

				java.sql.ResultSet rs_tDBInput_1 = null;

				try {
					rs_tDBInput_1 = stmt_tDBInput_1.executeQuery(dbquery_tDBInput_1);
					java.sql.ResultSetMetaData rsmd_tDBInput_1 = rs_tDBInput_1.getMetaData();
					int colQtyInRs_tDBInput_1 = rsmd_tDBInput_1.getColumnCount();

					String tmpContent_tDBInput_1 = null;

					log.debug("tDBInput_1 - Retrieving records from the database.");

					while (rs_tDBInput_1.next()) {
						nb_line_tDBInput_1++;

						if (colQtyInRs_tDBInput_1 < 1) {
							row1.run_id = null;
						} else {

							row1.run_id = rs_tDBInput_1.getLong(1);
							if (rs_tDBInput_1.wasNull()) {
								row1.run_id = null;
							}
						}

						log.debug("tDBInput_1 - Retrieving the record " + nb_line_tDBInput_1 + ".");

						/**
						 * [tDBInput_1 begin ] stop
						 */

						/**
						 * [tDBInput_1 main ] start
						 */

						s(currentComponent = "tDBInput_1");

						// QTUP-3575
						if (enableLineage && init_tDBInput_1_0) {
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
									if (row1 == null) {
										return s;
									}
									a(s, "run_id", "run_id", "false", "id_Long", "BIGINT", "true", "", "20", "0");
									return s;
								}

							}

							if (row1 != null) {
								talendJobLog.addConnectionSchemaMessage("tDBInput_1", "tMysqlInput", "tJavaRow_1",
										"tJavaRow", "row1" + iterateId, new SchemaUtil_row1().getSchema(row1));
								talendJobLogProcess(globalMap);
								init_tDBInput_1_0 = false;
							}

						}
						// QTUP-3575

						tos_count_tDBInput_1++;

						/**
						 * [tDBInput_1 main ] stop
						 */

						/**
						 * [tDBInput_1 process_data_begin ] start
						 */

						s(currentComponent = "tDBInput_1");

						/**
						 * [tDBInput_1 process_data_begin ] stop
						 */

						/**
						 * [tJavaRow_1 main ] start
						 */

						s(currentComponent = "tJavaRow_1");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tDBInput_1", "tDBInput_1", "tMysqlInput", "tJavaRow_1", "tJavaRow_1",
								"tJavaRow"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
						}

						context.RUN_ID = row1.run_id;

						nb_line_tJavaRow_1++;

						tos_count_tJavaRow_1++;

						/**
						 * [tJavaRow_1 main ] stop
						 */

						/**
						 * [tJavaRow_1 process_data_begin ] start
						 */

						s(currentComponent = "tJavaRow_1");

						/**
						 * [tJavaRow_1 process_data_begin ] stop
						 */

						/**
						 * [tJavaRow_1 process_data_end ] start
						 */

						s(currentComponent = "tJavaRow_1");

						/**
						 * [tJavaRow_1 process_data_end ] stop
						 */

						/**
						 * [tDBInput_1 process_data_end ] start
						 */

						s(currentComponent = "tDBInput_1");

						/**
						 * [tDBInput_1 process_data_end ] stop
						 */

						/**
						 * [tDBInput_1 end ] start
						 */

						s(currentComponent = "tDBInput_1");

					}
				} finally {
					if (rs_tDBInput_1 != null) {
						rs_tDBInput_1.close();
					}
					if (stmt_tDBInput_1 != null) {
						stmt_tDBInput_1.close();
					}
				}
				globalMap.put("tDBInput_1_NB_LINE", nb_line_tDBInput_1);
				log.debug("tDBInput_1 - Retrieved records count: " + nb_line_tDBInput_1 + " .");

				if (log.isDebugEnabled())
					log.debug("tDBInput_1 - " + ("Done."));

				ok_Hash.put("tDBInput_1", true);
				end_Hash.put("tDBInput_1", System.currentTimeMillis());

				/**
				 * [tDBInput_1 end ] stop
				 */

				/**
				 * [tJavaRow_1 end ] start
				 */

				s(currentComponent = "tJavaRow_1");

				globalMap.put("tJavaRow_1_NB_LINE", nb_line_tJavaRow_1);
				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tDBInput_1", "tDBInput_1", "tMysqlInput", "tJavaRow_1", "tJavaRow_1", "tJavaRow", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tJavaRow_1", true);
				end_Hash.put("tJavaRow_1", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk5", 0, "ok");
				}
				tDBRow_2Process(globalMap);

				/**
				 * [tJavaRow_1 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBInput_1 finally ] start
				 */

				s(currentComponent = "tDBInput_1");

				/**
				 * [tDBInput_1 finally ] stop
				 */

				/**
				 * [tJavaRow_1 finally ] start
				 */

				s(currentComponent = "tJavaRow_1");

				/**
				 * [tJavaRow_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBInput_1_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_2Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_2", "ZFgbqX_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_2 begin ] start
				 */

				sh("tDBRow_2");

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				int tos_count_tDBRow_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_2 = new StringBuilder();
							log4jParamters_tDBRow_2.append("Parameters:");
							log4jParamters_tDBRow_2.append("DB_VERSION" + " = " + "MYSQL_8");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("USE_EXISTING_CONNECTION" + " = " + "false");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("HOST" + " = " + "context.DB_HOST");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("PORT" + " = " + "context.DB_PORT");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("DBNAME" + " = " + "context.DB_NAME");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("USER" + " = " + "context.DB_USER");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("PASS" + " = "
									+ String.valueOf(
											routines.system.PasswordEncryptUtil.encryptPassword(context.DB_PASSWORD))
											.substring(0, 4)
									+ "...");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("QUERY" + " = "
									+ "\"TRUNCATE TABLE DSADEV.fact_order_payments_gr5; TRUNCATE TABLE DSADEV.fact_order_items_gr5; TRUNCATE TABLE DSADEV.fact_orders_gr5; TRUNCATE TABLE DSADEV.dim_product_gr5; TRUNCATE TABLE DSADEV.dim_customer_gr5;\"");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("PROPERTIES" + " = "
									+ "\"noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true\"");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("COMMIT_EVERY" + " = " + "10000");
							log4jParamters_tDBRow_2.append(" | ");
							log4jParamters_tDBRow_2.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_2 - " + (log4jParamters_tDBRow_2));
						}
					}
					new BytesLimit65535_tDBRow_2().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_2 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("DB_VERSION", String.valueOf("MYSQL_8"));
								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("false"));
								component_parameters.put("HOST", String.valueOf(context.DB_HOST));
								component_parameters.put("PORT", String.valueOf(context.DB_PORT));
								component_parameters.put("DBNAME", String.valueOf(context.DB_NAME));
								component_parameters.put("USER", String.valueOf(context.DB_USER));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY", String.valueOf(new StringBuilder().append(
										"TRUNCATE TABLE DSADEV.fact_order_payments_gr5; TRUNCATE TABLE DSADEV.fact_order_items_gr5; TRUNCATE TABLE DSADEV.fact_o"
												+ "rders_gr5; TRUNCATE TABLE DSADEV.dim_product_gr5; TRUNCATE TABLE DSADEV.dim_customer_gr5;")
										.toString()));
								component_parameters.put("SPECIFY_DATASOURCE_ALIAS", String.valueOf("false"));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPERTIES", String.valueOf(
										"noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("COMMIT_EVERY", String.valueOf("10000"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_2) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_2", "tMysqlRow",
							new ParameterUtil_tDBRow_2().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_2", "TRUNCATE", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_2 = null;
				String query_tDBRow_2 = "";
				boolean whetherReject_tDBRow_2 = false;
				int count_tDBRow_2 = 0;
				String driverClass_tDBRow_2 = "com.mysql.cj.jdbc.Driver";
				java.lang.Class jdbcclazz_tDBRow_2 = java.lang.Class.forName(driverClass_tDBRow_2);

				String properties_tDBRow_2 = "noDatetimeStringSync=true&serverTimezone=America/Guayaquil&allowMultiQueries=true";
				if (properties_tDBRow_2 == null || properties_tDBRow_2.trim().length() == 0) {
					properties_tDBRow_2 = "allowLoadLocalInfile=true";
				} else {
					if (!properties_tDBRow_2.contains("allowLoadLocalInfile=")) {
						properties_tDBRow_2 += "&allowLoadLocalInfile=true";
					}
				}

				String url_tDBRow_2 = "jdbc:mysql://" + context.DB_HOST + ":" + context.DB_PORT + "/" + context.DB_NAME
						+ "?" + properties_tDBRow_2;
				log.debug("tDBRow_2 - Driver ClassName: " + driverClass_tDBRow_2 + ".");

				String dbUser_tDBRow_2 = context.DB_USER;

				final String decryptedPassword_tDBRow_2 = java.util.Optional.ofNullable(context.DB_PASSWORD).orElse("");

				String dbPwd_tDBRow_2 = decryptedPassword_tDBRow_2;

				log.debug("tDBRow_2 - Connection attempt to '"
						+ url_tDBRow_2.replaceAll("(?<=trustStorePassword=)[^;]*", "********") + "' with the username '"
						+ dbUser_tDBRow_2 + "'.");

				conn_tDBRow_2 = java.sql.DriverManager.getConnection(url_tDBRow_2, dbUser_tDBRow_2, dbPwd_tDBRow_2);

				log.debug("tDBRow_2 - Connection to '"
						+ url_tDBRow_2.replaceAll("(?<=trustStorePassword=)[^;]*", "********") + "' has succeeded.");

				resourceMap.put("conn_tDBRow_2", conn_tDBRow_2);
				if (conn_tDBRow_2.getAutoCommit()) {

					log.debug("tDBRow_2 - Connection is set auto commit to 'false'.");

					conn_tDBRow_2.setAutoCommit(false);

				}
				int commitEvery_tDBRow_2 = 10000;
				int commitCounter_tDBRow_2 = 0;

				java.sql.Statement stmt_tDBRow_2 = conn_tDBRow_2.createStatement();
				resourceMap.put("stmt_tDBRow_2", stmt_tDBRow_2);

				/**
				 * [tDBRow_2 begin ] stop
				 */

				/**
				 * [tDBRow_2 main ] start
				 */

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				query_tDBRow_2 = new StringBuilder().append(
						"TRUNCATE TABLE DSADEV.fact_order_payments_gr5; TRUNCATE TABLE DSADEV.fact_order_items_gr5; TRUNCATE TABLE DSADEV.fact_o"
								+ "rders_gr5; TRUNCATE TABLE DSADEV.dim_product_gr5; TRUNCATE TABLE DSADEV.dim_customer_gr5;")
						.toString();
				whetherReject_tDBRow_2 = false;
				log.debug("tDBRow_2 - Executing the query: '" + query_tDBRow_2 + "'.");

				globalMap.put("tDBRow_2_QUERY", query_tDBRow_2);
				try {
					stmt_tDBRow_2.execute(query_tDBRow_2);
					log.debug("tDBRow_2 - Execute the query: '" + query_tDBRow_2 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_2 = true;

					throw (e);

				}

				if (!whetherReject_tDBRow_2) {

				}

				commitCounter_tDBRow_2++;
				if (commitEvery_tDBRow_2 <= commitCounter_tDBRow_2) {

					log.debug("tDBRow_2 - Connection starting to commit.");

					conn_tDBRow_2.commit();

					log.debug("tDBRow_2 - Connection commit has succeeded.");

					commitCounter_tDBRow_2 = 0;
				}

				tos_count_tDBRow_2++;

				/**
				 * [tDBRow_2 main ] stop
				 */

				/**
				 * [tDBRow_2 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				/**
				 * [tDBRow_2 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_2 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				/**
				 * [tDBRow_2 process_data_end ] stop
				 */

				/**
				 * [tDBRow_2 end ] start
				 */

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				globalMap.put("tDBRow_2_NB_LINE", count_tDBRow_2);
				stmt_tDBRow_2.close();
				resourceMap.remove("stmt_tDBRow_2");
				resourceMap.put("statementClosed_tDBRow_2", true);
				if (commitEvery_tDBRow_2 > commitCounter_tDBRow_2) {

					log.debug("tDBRow_2 - Connection starting to commit.");

					conn_tDBRow_2.commit();

					log.debug("tDBRow_2 - Connection commit has succeeded.");

					commitCounter_tDBRow_2 = 0;

				}
				log.debug("tDBRow_2 - Closing the connection to the database.");

				conn_tDBRow_2.close();

				if ("com.mysql.cj.jdbc.Driver".equals((String) globalMap.get("driverClass_"))
						&& routines.system.BundleUtils.inOSGi()) {
					Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread").getMethod("checkedShutdown")
							.invoke(null, (Object[]) null);
				}

				log.debug("tDBRow_2 - Connection to the database closed.");

				resourceMap.put("finish_tDBRow_2", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_2 - " + ("Done."));

				ok_Hash.put("tDBRow_2", true);
				end_Hash.put("tDBRow_2", System.currentTimeMillis());

				if (execStat) {
					runStat.updateStatOnConnection("OnComponentOk4", 0, "ok");
				}
				tRunJob_1Process(globalMap);

				/**
				 * [tDBRow_2 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_2 finally ] start
				 */

				s(currentComponent = "tDBRow_2");

				cLabel = "TRUNCATE";

				try {
					if (resourceMap.get("statementClosed_tDBRow_2") == null) {
						java.sql.Statement stmtToClose_tDBRow_2 = null;
						if ((stmtToClose_tDBRow_2 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_2")) != null) {
							stmtToClose_tDBRow_2.close();
						}
					}
				} finally {
					if (resourceMap.get("finish_tDBRow_2") == null) {
						java.sql.Connection ctn_tDBRow_2 = null;
						if ((ctn_tDBRow_2 = (java.sql.Connection) resourceMap.get("conn_tDBRow_2")) != null) {
							try {
								log.debug("tDBRow_2 - Closing the connection to the database.");

								ctn_tDBRow_2.close();
								log.debug("tDBRow_2 - Connection to the database closed.");

							} catch (java.sql.SQLException sqlEx_tDBRow_2) {
								String errorMessage_tDBRow_2 = "failed to close the connection in tDBRow_2 :"
										+ sqlEx_tDBRow_2.getMessage();
								log.error("tDBRow_2 - " + sqlEx_tDBRow_2.getMessage());

								System.err.println(errorMessage_tDBRow_2);
							}
						}
					}
				}

				/**
				 * [tDBRow_2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_2_SUBPROCESS_STATE", 1);
	}

	public void tRunJob_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRunJob_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRunJob_1", "fiMLQM_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tRunJob_1 begin ] start
				 */

				sh("tRunJob_1");

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				int tos_count_tRunJob_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tRunJob_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRunJob_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRunJob_1 = new StringBuilder();
							log4jParamters_tRunJob_1.append("Parameters:");
							log4jParamters_tRunJob_1.append("USE_DYNAMIC_JOB" + " = " + "false");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("PROCESS" + " = " + "load_dim_customer");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("USE_INDEPENDENT_PROCESS" + " = " + "false");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("DIE_ON_CHILD_ERROR" + " = " + "true");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("TRANSMIT_WHOLE_CONTEXT" + " = " + "true");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("CONTEXTPARAMS" + " = " + "[]");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("PROPAGATE_CHILD_RESULT" + " = " + "false");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("PRINT_PARAMETER" + " = " + "false");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("USE_DYNAMIC_CONTEXT" + " = " + "false");
							log4jParamters_tRunJob_1.append(" | ");
							log4jParamters_tRunJob_1.append("USE_BASE64" + " = " + "true");
							log4jParamters_tRunJob_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tRunJob_1 - " + (log4jParamters_tRunJob_1));
						}
					}
					new BytesLimit65535_tRunJob_1().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tRunJob_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_DYNAMIC_JOB", String.valueOf("false"));
								component_parameters.put("PROCESS", String.valueOf("load_dim_customer"));
								component_parameters.put("USE_INDEPENDENT_PROCESS", String.valueOf("false"));
								component_parameters.put("DIE_ON_CHILD_ERROR", String.valueOf("true"));
								component_parameters.put("TRANSMIT_WHOLE_CONTEXT", String.valueOf("true"));
								component_parameters.put("CONTEXTPARAMS", String.valueOf("[]"));
								component_parameters.put("PROPAGATE_CHILD_RESULT", String.valueOf("false"));
								component_parameters.put("PRINT_PARAMETER", String.valueOf("false"));
								component_parameters.put("USE_DYNAMIC_CONTEXT", String.valueOf("false"));
								component_parameters.put("USE_BASE64", String.valueOf("true"));

							} catch (java.lang.Exception e_tRunJob_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tRunJob_1", "tRunJob",
							new ParameterUtil_tRunJob_1().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tRunJob_1", "load_dim_customer", "tRunJob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRunJob_1 begin ] stop
				 */

				/**
				 * [tRunJob_1 main ] start
				 */

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				java.util.List<String> paraList_tRunJob_1 = new java.util.ArrayList<String>();

				paraList_tRunJob_1.add("--father_pid=" + pid);

				paraList_tRunJob_1.add("--root_pid=" + rootPid);

				paraList_tRunJob_1.add("--father_node=tRunJob_1");

				paraList_tRunJob_1.add("--context=Default");

				if (!"".equals(log4jLevel)) {
					paraList_tRunJob_1.add("--log4jLevel=" + log4jLevel);
				}

				if (enableLogStash) {
					paraList_tRunJob_1.add("--audit.enabled=" + enableLogStash);
				}

				paraList_tRunJob_1.add("--skipCBPAuth=true");

				// for feature:10589

				paraList_tRunJob_1.add("--stat_port=" + portStats);

				if (resuming_logs_dir_path != null) {
					paraList_tRunJob_1.add("--resuming_logs_dir_path=" + resuming_logs_dir_path);
				}
				String childResumePath_tRunJob_1 = ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path);
				String tRunJobName_tRunJob_1 = ResumeUtil.getRighttRunJob(resuming_checkpoint_path);
				if ("tRunJob_1".equals(tRunJobName_tRunJob_1) && childResumePath_tRunJob_1 != null) {
					paraList_tRunJob_1.add("--resuming_checkpoint_path="
							+ ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path));
				}
				paraList_tRunJob_1.add("--parent_part_launcher=JOB:" + jobName + "/NODE:tRunJob_1");

				java.util.Map<String, Object> parentContextMap_tRunJob_1 = new java.util.HashMap<String, Object>();

				context.synchronizeContext();
				class ContextProcessor_tRunJob_1 {
					private void transmitContext_0() {
						parentContextMap_tRunJob_1.put("DB_HOST", context.DB_HOST);
						paraList_tRunJob_1.add("--context_type " + "DB_HOST" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("DB_NAME", context.DB_NAME);
						paraList_tRunJob_1.add("--context_type " + "DB_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("DB_PASSWORD", context.DB_PASSWORD);
						paraList_tRunJob_1.add("--context_type " + "DB_PASSWORD" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("DB_PORT", context.DB_PORT);
						paraList_tRunJob_1.add("--context_type " + "DB_PORT" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("DB_USER", context.DB_USER);
						paraList_tRunJob_1.add("--context_type " + "DB_USER" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("ENV_NAME", context.ENV_NAME);
						paraList_tRunJob_1.add("--context_type " + "ENV_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("FILE_OLIST_CUSTOMERS", context.FILE_OLIST_CUSTOMERS);
						paraList_tRunJob_1.add("--context_type " + "FILE_OLIST_CUSTOMERS" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("FILE_OLIST_ORDER_ITEMS", context.FILE_OLIST_ORDER_ITEMS);
						paraList_tRunJob_1.add("--context_type " + "FILE_OLIST_ORDER_ITEMS" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("FILE_OLIST_ORDER_PAYMENTS", context.FILE_OLIST_ORDER_PAYMENTS);
						paraList_tRunJob_1.add("--context_type " + "FILE_OLIST_ORDER_PAYMENTS" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("FILE_OLIST_ORDERS", context.FILE_OLIST_ORDERS);
						paraList_tRunJob_1.add("--context_type " + "FILE_OLIST_ORDERS" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("FILE_OLIST_PRODUCTS", context.FILE_OLIST_PRODUCTS);
						paraList_tRunJob_1.add("--context_type " + "FILE_OLIST_PRODUCTS" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("LOAD_SOURCE_TAG", context.LOAD_SOURCE_TAG);
						paraList_tRunJob_1.add("--context_type " + "LOAD_SOURCE_TAG" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("PIPELINE_NAME", context.PIPELINE_NAME);
						paraList_tRunJob_1.add("--context_type " + "PIPELINE_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_1.put("RUN_ID", context.RUN_ID);
						paraList_tRunJob_1.add("--context_type " + "RUN_ID" + "=" + "id_Long");
					}

					public void transmitAllContext() {
						transmitContext_0();
					}
				}
				new ContextProcessor_tRunJob_1().transmitAllContext();
				java.util.Enumeration<?> propertyNames_tRunJob_1 = context.propertyNames();
				while (propertyNames_tRunJob_1.hasMoreElements()) {
					String key_tRunJob_1 = (String) propertyNames_tRunJob_1.nextElement();
					Object value_tRunJob_1 = (Object) context.get(key_tRunJob_1);
					if (value_tRunJob_1 != null) {

						paraList_tRunJob_1.add("--context_param " + key_tRunJob_1 + "=" + value_tRunJob_1);

					} else {
						paraList_tRunJob_1.add("--context_param " + key_tRunJob_1 + "="
								+ NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY);
					}

				}

				Object obj_tRunJob_1 = null;

				capacitacion_talend.load_dim_customer_0_1.load_dim_customer childJob_tRunJob_1 = new capacitacion_talend.load_dim_customer_0_1.load_dim_customer();
				// pass DataSources
				java.util.Map<String, routines.system.TalendDataSource> talendDataSources_tRunJob_1 = (java.util.Map<String, routines.system.TalendDataSource>) globalMap
						.get(KEY_DB_DATASOURCES);
				if (null != talendDataSources_tRunJob_1) {
					java.util.Map<String, javax.sql.DataSource> dataSources_tRunJob_1 = new java.util.HashMap<String, javax.sql.DataSource>();
					for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry_tRunJob_1 : talendDataSources_tRunJob_1
							.entrySet()) {
						dataSources_tRunJob_1.put(talendDataSourceEntry_tRunJob_1.getKey(),
								talendDataSourceEntry_tRunJob_1.getValue().getRawDataSource());
					}
					childJob_tRunJob_1.setDataSources(dataSources_tRunJob_1);
				}

				childJob_tRunJob_1.parentContextMap = parentContextMap_tRunJob_1;

				log.info(
						"tRunJob_1 - The child job 'capacitacion_talend.load_dim_customer_0_1.load_dim_customer' starts on the version '0.1' with the context 'Default'.");

				String[][] childReturn_tRunJob_1 = childJob_tRunJob_1
						.runJob((String[]) paraList_tRunJob_1.toArray(new String[paraList_tRunJob_1.size()]));

				log.info(
						"tRunJob_1 - The child job 'capacitacion_talend.load_dim_customer_0_1.load_dim_customer' is done.");

				if (childJob_tRunJob_1.getErrorCode() == null) {
					globalMap.put("tRunJob_1_CHILD_RETURN_CODE",
							childJob_tRunJob_1.getStatus() != null && ("failure").equals(childJob_tRunJob_1.getStatus())
									? 1
									: 0);
				} else {
					globalMap.put("tRunJob_1_CHILD_RETURN_CODE", childJob_tRunJob_1.getErrorCode());
				}
				if (childJob_tRunJob_1.getExceptionStackTrace() != null) {
					globalMap.put("tRunJob_1_CHILD_EXCEPTION_STACKTRACE", childJob_tRunJob_1.getExceptionStackTrace());
				}
				errorCode = childJob_tRunJob_1.getErrorCode();
				if (childJob_tRunJob_1.getErrorCode() != null || ("failure").equals(childJob_tRunJob_1.getStatus())) {
					java.lang.Exception ce_tRunJob_1 = childJob_tRunJob_1.getException();
					throw new RuntimeException("Child job running failed.\n" + ((ce_tRunJob_1 != null)
							? (ce_tRunJob_1.getClass().getName() + ": " + ce_tRunJob_1.getMessage())
							: ""));
				}

				tos_count_tRunJob_1++;

				/**
				 * [tRunJob_1 main ] stop
				 */

				/**
				 * [tRunJob_1 process_data_begin ] start
				 */

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				/**
				 * [tRunJob_1 process_data_begin ] stop
				 */

				/**
				 * [tRunJob_1 process_data_end ] start
				 */

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				/**
				 * [tRunJob_1 process_data_end ] stop
				 */

				/**
				 * [tRunJob_1 end ] start
				 */

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				if (log.isDebugEnabled())
					log.debug("tRunJob_1 - " + ("Done."));

				ok_Hash.put("tRunJob_1", true);
				end_Hash.put("tRunJob_1", System.currentTimeMillis());

				/**
				 * [tRunJob_1 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRunJob_1:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk1", 0, "ok");
			}

			tRunJob_2Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRunJob_1 finally ] start
				 */

				s(currentComponent = "tRunJob_1");

				cLabel = "load_dim_customer";

				/**
				 * [tRunJob_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRunJob_1_SUBPROCESS_STATE", 1);
	}

	public void tRunJob_2Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRunJob_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRunJob_2", "fohPOE_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tRunJob_2 begin ] start
				 */

				sh("tRunJob_2");

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				int tos_count_tRunJob_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tRunJob_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRunJob_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRunJob_2 = new StringBuilder();
							log4jParamters_tRunJob_2.append("Parameters:");
							log4jParamters_tRunJob_2.append("USE_DYNAMIC_JOB" + " = " + "false");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("PROCESS" + " = " + "load_dim_product");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("USE_INDEPENDENT_PROCESS" + " = " + "false");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("DIE_ON_CHILD_ERROR" + " = " + "true");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("TRANSMIT_WHOLE_CONTEXT" + " = " + "true");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("CONTEXTPARAMS" + " = " + "[]");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("PROPAGATE_CHILD_RESULT" + " = " + "false");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("PRINT_PARAMETER" + " = " + "false");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("USE_DYNAMIC_CONTEXT" + " = " + "false");
							log4jParamters_tRunJob_2.append(" | ");
							log4jParamters_tRunJob_2.append("USE_BASE64" + " = " + "true");
							log4jParamters_tRunJob_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tRunJob_2 - " + (log4jParamters_tRunJob_2));
						}
					}
					new BytesLimit65535_tRunJob_2().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tRunJob_2 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_DYNAMIC_JOB", String.valueOf("false"));
								component_parameters.put("PROCESS", String.valueOf("load_dim_product"));
								component_parameters.put("USE_INDEPENDENT_PROCESS", String.valueOf("false"));
								component_parameters.put("DIE_ON_CHILD_ERROR", String.valueOf("true"));
								component_parameters.put("TRANSMIT_WHOLE_CONTEXT", String.valueOf("true"));
								component_parameters.put("CONTEXTPARAMS", String.valueOf("[]"));
								component_parameters.put("PROPAGATE_CHILD_RESULT", String.valueOf("false"));
								component_parameters.put("PRINT_PARAMETER", String.valueOf("false"));
								component_parameters.put("USE_DYNAMIC_CONTEXT", String.valueOf("false"));
								component_parameters.put("USE_BASE64", String.valueOf("true"));

							} catch (java.lang.Exception e_tRunJob_2) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tRunJob_2", "tRunJob",
							new ParameterUtil_tRunJob_2().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tRunJob_2", "load_dim_product", "tRunJob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRunJob_2 begin ] stop
				 */

				/**
				 * [tRunJob_2 main ] start
				 */

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				java.util.List<String> paraList_tRunJob_2 = new java.util.ArrayList<String>();

				paraList_tRunJob_2.add("--father_pid=" + pid);

				paraList_tRunJob_2.add("--root_pid=" + rootPid);

				paraList_tRunJob_2.add("--father_node=tRunJob_2");

				paraList_tRunJob_2.add("--context=Default");

				if (!"".equals(log4jLevel)) {
					paraList_tRunJob_2.add("--log4jLevel=" + log4jLevel);
				}

				if (enableLogStash) {
					paraList_tRunJob_2.add("--audit.enabled=" + enableLogStash);
				}

				paraList_tRunJob_2.add("--skipCBPAuth=true");

				// for feature:10589

				paraList_tRunJob_2.add("--stat_port=" + portStats);

				if (resuming_logs_dir_path != null) {
					paraList_tRunJob_2.add("--resuming_logs_dir_path=" + resuming_logs_dir_path);
				}
				String childResumePath_tRunJob_2 = ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path);
				String tRunJobName_tRunJob_2 = ResumeUtil.getRighttRunJob(resuming_checkpoint_path);
				if ("tRunJob_2".equals(tRunJobName_tRunJob_2) && childResumePath_tRunJob_2 != null) {
					paraList_tRunJob_2.add("--resuming_checkpoint_path="
							+ ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path));
				}
				paraList_tRunJob_2.add("--parent_part_launcher=JOB:" + jobName + "/NODE:tRunJob_2");

				java.util.Map<String, Object> parentContextMap_tRunJob_2 = new java.util.HashMap<String, Object>();

				context.synchronizeContext();
				class ContextProcessor_tRunJob_2 {
					private void transmitContext_0() {
						parentContextMap_tRunJob_2.put("DB_HOST", context.DB_HOST);
						paraList_tRunJob_2.add("--context_type " + "DB_HOST" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("DB_NAME", context.DB_NAME);
						paraList_tRunJob_2.add("--context_type " + "DB_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("DB_PASSWORD", context.DB_PASSWORD);
						paraList_tRunJob_2.add("--context_type " + "DB_PASSWORD" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("DB_PORT", context.DB_PORT);
						paraList_tRunJob_2.add("--context_type " + "DB_PORT" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("DB_USER", context.DB_USER);
						paraList_tRunJob_2.add("--context_type " + "DB_USER" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("ENV_NAME", context.ENV_NAME);
						paraList_tRunJob_2.add("--context_type " + "ENV_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("FILE_OLIST_CUSTOMERS", context.FILE_OLIST_CUSTOMERS);
						paraList_tRunJob_2.add("--context_type " + "FILE_OLIST_CUSTOMERS" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("FILE_OLIST_ORDER_ITEMS", context.FILE_OLIST_ORDER_ITEMS);
						paraList_tRunJob_2.add("--context_type " + "FILE_OLIST_ORDER_ITEMS" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("FILE_OLIST_ORDER_PAYMENTS", context.FILE_OLIST_ORDER_PAYMENTS);
						paraList_tRunJob_2.add("--context_type " + "FILE_OLIST_ORDER_PAYMENTS" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("FILE_OLIST_ORDERS", context.FILE_OLIST_ORDERS);
						paraList_tRunJob_2.add("--context_type " + "FILE_OLIST_ORDERS" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("FILE_OLIST_PRODUCTS", context.FILE_OLIST_PRODUCTS);
						paraList_tRunJob_2.add("--context_type " + "FILE_OLIST_PRODUCTS" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("LOAD_SOURCE_TAG", context.LOAD_SOURCE_TAG);
						paraList_tRunJob_2.add("--context_type " + "LOAD_SOURCE_TAG" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("PIPELINE_NAME", context.PIPELINE_NAME);
						paraList_tRunJob_2.add("--context_type " + "PIPELINE_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_2.put("RUN_ID", context.RUN_ID);
						paraList_tRunJob_2.add("--context_type " + "RUN_ID" + "=" + "id_Long");
					}

					public void transmitAllContext() {
						transmitContext_0();
					}
				}
				new ContextProcessor_tRunJob_2().transmitAllContext();
				java.util.Enumeration<?> propertyNames_tRunJob_2 = context.propertyNames();
				while (propertyNames_tRunJob_2.hasMoreElements()) {
					String key_tRunJob_2 = (String) propertyNames_tRunJob_2.nextElement();
					Object value_tRunJob_2 = (Object) context.get(key_tRunJob_2);
					if (value_tRunJob_2 != null) {

						paraList_tRunJob_2.add("--context_param " + key_tRunJob_2 + "=" + value_tRunJob_2);

					} else {
						paraList_tRunJob_2.add("--context_param " + key_tRunJob_2 + "="
								+ NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY);
					}

				}

				Object obj_tRunJob_2 = null;

				capacitacion_talend.load_dim_product_0_1.load_dim_product childJob_tRunJob_2 = new capacitacion_talend.load_dim_product_0_1.load_dim_product();
				// pass DataSources
				java.util.Map<String, routines.system.TalendDataSource> talendDataSources_tRunJob_2 = (java.util.Map<String, routines.system.TalendDataSource>) globalMap
						.get(KEY_DB_DATASOURCES);
				if (null != talendDataSources_tRunJob_2) {
					java.util.Map<String, javax.sql.DataSource> dataSources_tRunJob_2 = new java.util.HashMap<String, javax.sql.DataSource>();
					for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry_tRunJob_2 : talendDataSources_tRunJob_2
							.entrySet()) {
						dataSources_tRunJob_2.put(talendDataSourceEntry_tRunJob_2.getKey(),
								talendDataSourceEntry_tRunJob_2.getValue().getRawDataSource());
					}
					childJob_tRunJob_2.setDataSources(dataSources_tRunJob_2);
				}

				childJob_tRunJob_2.parentContextMap = parentContextMap_tRunJob_2;

				log.info(
						"tRunJob_2 - The child job 'capacitacion_talend.load_dim_product_0_1.load_dim_product' starts on the version '0.1' with the context 'Default'.");

				String[][] childReturn_tRunJob_2 = childJob_tRunJob_2
						.runJob((String[]) paraList_tRunJob_2.toArray(new String[paraList_tRunJob_2.size()]));

				log.info(
						"tRunJob_2 - The child job 'capacitacion_talend.load_dim_product_0_1.load_dim_product' is done.");

				if (childJob_tRunJob_2.getErrorCode() == null) {
					globalMap.put("tRunJob_2_CHILD_RETURN_CODE",
							childJob_tRunJob_2.getStatus() != null && ("failure").equals(childJob_tRunJob_2.getStatus())
									? 1
									: 0);
				} else {
					globalMap.put("tRunJob_2_CHILD_RETURN_CODE", childJob_tRunJob_2.getErrorCode());
				}
				if (childJob_tRunJob_2.getExceptionStackTrace() != null) {
					globalMap.put("tRunJob_2_CHILD_EXCEPTION_STACKTRACE", childJob_tRunJob_2.getExceptionStackTrace());
				}
				errorCode = childJob_tRunJob_2.getErrorCode();
				if (childJob_tRunJob_2.getErrorCode() != null || ("failure").equals(childJob_tRunJob_2.getStatus())) {
					java.lang.Exception ce_tRunJob_2 = childJob_tRunJob_2.getException();
					throw new RuntimeException("Child job running failed.\n" + ((ce_tRunJob_2 != null)
							? (ce_tRunJob_2.getClass().getName() + ": " + ce_tRunJob_2.getMessage())
							: ""));
				}

				tos_count_tRunJob_2++;

				/**
				 * [tRunJob_2 main ] stop
				 */

				/**
				 * [tRunJob_2 process_data_begin ] start
				 */

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				/**
				 * [tRunJob_2 process_data_begin ] stop
				 */

				/**
				 * [tRunJob_2 process_data_end ] start
				 */

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				/**
				 * [tRunJob_2 process_data_end ] stop
				 */

				/**
				 * [tRunJob_2 end ] start
				 */

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				if (log.isDebugEnabled())
					log.debug("tRunJob_2 - " + ("Done."));

				ok_Hash.put("tRunJob_2", true);
				end_Hash.put("tRunJob_2", System.currentTimeMillis());

				/**
				 * [tRunJob_2 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRunJob_2:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk2", 0, "ok");
			}

			tRunJob_3Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRunJob_2 finally ] start
				 */

				s(currentComponent = "tRunJob_2");

				cLabel = "load_dim_product";

				/**
				 * [tRunJob_2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRunJob_2_SUBPROCESS_STATE", 1);
	}

	public void tRunJob_3Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRunJob_3_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRunJob_3", "C9mtVu_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tRunJob_3 begin ] start
				 */

				sh("tRunJob_3");

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				int tos_count_tRunJob_3 = 0;

				if (log.isDebugEnabled())
					log.debug("tRunJob_3 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRunJob_3 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRunJob_3 = new StringBuilder();
							log4jParamters_tRunJob_3.append("Parameters:");
							log4jParamters_tRunJob_3.append("USE_DYNAMIC_JOB" + " = " + "false");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("PROCESS" + " = " + "load_fact_orders");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("USE_INDEPENDENT_PROCESS" + " = " + "false");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("DIE_ON_CHILD_ERROR" + " = " + "true");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("TRANSMIT_WHOLE_CONTEXT" + " = " + "true");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("CONTEXTPARAMS" + " = " + "[]");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("PROPAGATE_CHILD_RESULT" + " = " + "false");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("PRINT_PARAMETER" + " = " + "false");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("USE_DYNAMIC_CONTEXT" + " = " + "false");
							log4jParamters_tRunJob_3.append(" | ");
							log4jParamters_tRunJob_3.append("USE_BASE64" + " = " + "true");
							log4jParamters_tRunJob_3.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tRunJob_3 - " + (log4jParamters_tRunJob_3));
						}
					}
					new BytesLimit65535_tRunJob_3().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tRunJob_3 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_DYNAMIC_JOB", String.valueOf("false"));
								component_parameters.put("PROCESS", String.valueOf("load_fact_orders"));
								component_parameters.put("USE_INDEPENDENT_PROCESS", String.valueOf("false"));
								component_parameters.put("DIE_ON_CHILD_ERROR", String.valueOf("true"));
								component_parameters.put("TRANSMIT_WHOLE_CONTEXT", String.valueOf("true"));
								component_parameters.put("CONTEXTPARAMS", String.valueOf("[]"));
								component_parameters.put("PROPAGATE_CHILD_RESULT", String.valueOf("false"));
								component_parameters.put("PRINT_PARAMETER", String.valueOf("false"));
								component_parameters.put("USE_DYNAMIC_CONTEXT", String.valueOf("false"));
								component_parameters.put("USE_BASE64", String.valueOf("true"));

							} catch (java.lang.Exception e_tRunJob_3) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tRunJob_3", "tRunJob",
							new ParameterUtil_tRunJob_3().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tRunJob_3", "load_fact_orders", "tRunJob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRunJob_3 begin ] stop
				 */

				/**
				 * [tRunJob_3 main ] start
				 */

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				java.util.List<String> paraList_tRunJob_3 = new java.util.ArrayList<String>();

				paraList_tRunJob_3.add("--father_pid=" + pid);

				paraList_tRunJob_3.add("--root_pid=" + rootPid);

				paraList_tRunJob_3.add("--father_node=tRunJob_3");

				paraList_tRunJob_3.add("--context=Default");

				if (!"".equals(log4jLevel)) {
					paraList_tRunJob_3.add("--log4jLevel=" + log4jLevel);
				}

				if (enableLogStash) {
					paraList_tRunJob_3.add("--audit.enabled=" + enableLogStash);
				}

				paraList_tRunJob_3.add("--skipCBPAuth=true");

				// for feature:10589

				paraList_tRunJob_3.add("--stat_port=" + portStats);

				if (resuming_logs_dir_path != null) {
					paraList_tRunJob_3.add("--resuming_logs_dir_path=" + resuming_logs_dir_path);
				}
				String childResumePath_tRunJob_3 = ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path);
				String tRunJobName_tRunJob_3 = ResumeUtil.getRighttRunJob(resuming_checkpoint_path);
				if ("tRunJob_3".equals(tRunJobName_tRunJob_3) && childResumePath_tRunJob_3 != null) {
					paraList_tRunJob_3.add("--resuming_checkpoint_path="
							+ ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path));
				}
				paraList_tRunJob_3.add("--parent_part_launcher=JOB:" + jobName + "/NODE:tRunJob_3");

				java.util.Map<String, Object> parentContextMap_tRunJob_3 = new java.util.HashMap<String, Object>();

				context.synchronizeContext();
				class ContextProcessor_tRunJob_3 {
					private void transmitContext_0() {
						parentContextMap_tRunJob_3.put("DB_HOST", context.DB_HOST);
						paraList_tRunJob_3.add("--context_type " + "DB_HOST" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("DB_NAME", context.DB_NAME);
						paraList_tRunJob_3.add("--context_type " + "DB_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("DB_PASSWORD", context.DB_PASSWORD);
						paraList_tRunJob_3.add("--context_type " + "DB_PASSWORD" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("DB_PORT", context.DB_PORT);
						paraList_tRunJob_3.add("--context_type " + "DB_PORT" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("DB_USER", context.DB_USER);
						paraList_tRunJob_3.add("--context_type " + "DB_USER" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("ENV_NAME", context.ENV_NAME);
						paraList_tRunJob_3.add("--context_type " + "ENV_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("FILE_OLIST_CUSTOMERS", context.FILE_OLIST_CUSTOMERS);
						paraList_tRunJob_3.add("--context_type " + "FILE_OLIST_CUSTOMERS" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("FILE_OLIST_ORDER_ITEMS", context.FILE_OLIST_ORDER_ITEMS);
						paraList_tRunJob_3.add("--context_type " + "FILE_OLIST_ORDER_ITEMS" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("FILE_OLIST_ORDER_PAYMENTS", context.FILE_OLIST_ORDER_PAYMENTS);
						paraList_tRunJob_3.add("--context_type " + "FILE_OLIST_ORDER_PAYMENTS" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("FILE_OLIST_ORDERS", context.FILE_OLIST_ORDERS);
						paraList_tRunJob_3.add("--context_type " + "FILE_OLIST_ORDERS" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("FILE_OLIST_PRODUCTS", context.FILE_OLIST_PRODUCTS);
						paraList_tRunJob_3.add("--context_type " + "FILE_OLIST_PRODUCTS" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("LOAD_SOURCE_TAG", context.LOAD_SOURCE_TAG);
						paraList_tRunJob_3.add("--context_type " + "LOAD_SOURCE_TAG" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("PIPELINE_NAME", context.PIPELINE_NAME);
						paraList_tRunJob_3.add("--context_type " + "PIPELINE_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_3.put("RUN_ID", context.RUN_ID);
						paraList_tRunJob_3.add("--context_type " + "RUN_ID" + "=" + "id_Long");
					}

					public void transmitAllContext() {
						transmitContext_0();
					}
				}
				new ContextProcessor_tRunJob_3().transmitAllContext();
				java.util.Enumeration<?> propertyNames_tRunJob_3 = context.propertyNames();
				while (propertyNames_tRunJob_3.hasMoreElements()) {
					String key_tRunJob_3 = (String) propertyNames_tRunJob_3.nextElement();
					Object value_tRunJob_3 = (Object) context.get(key_tRunJob_3);
					if (value_tRunJob_3 != null) {

						paraList_tRunJob_3.add("--context_param " + key_tRunJob_3 + "=" + value_tRunJob_3);

					} else {
						paraList_tRunJob_3.add("--context_param " + key_tRunJob_3 + "="
								+ NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY);
					}

				}

				Object obj_tRunJob_3 = null;

				capacitacion_talend.load_fact_orders_0_1.load_fact_orders childJob_tRunJob_3 = new capacitacion_talend.load_fact_orders_0_1.load_fact_orders();
				// pass DataSources
				java.util.Map<String, routines.system.TalendDataSource> talendDataSources_tRunJob_3 = (java.util.Map<String, routines.system.TalendDataSource>) globalMap
						.get(KEY_DB_DATASOURCES);
				if (null != talendDataSources_tRunJob_3) {
					java.util.Map<String, javax.sql.DataSource> dataSources_tRunJob_3 = new java.util.HashMap<String, javax.sql.DataSource>();
					for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry_tRunJob_3 : talendDataSources_tRunJob_3
							.entrySet()) {
						dataSources_tRunJob_3.put(talendDataSourceEntry_tRunJob_3.getKey(),
								talendDataSourceEntry_tRunJob_3.getValue().getRawDataSource());
					}
					childJob_tRunJob_3.setDataSources(dataSources_tRunJob_3);
				}

				childJob_tRunJob_3.parentContextMap = parentContextMap_tRunJob_3;

				log.info(
						"tRunJob_3 - The child job 'capacitacion_talend.load_fact_orders_0_1.load_fact_orders' starts on the version '0.1' with the context 'Default'.");

				String[][] childReturn_tRunJob_3 = childJob_tRunJob_3
						.runJob((String[]) paraList_tRunJob_3.toArray(new String[paraList_tRunJob_3.size()]));

				log.info(
						"tRunJob_3 - The child job 'capacitacion_talend.load_fact_orders_0_1.load_fact_orders' is done.");

				if (childJob_tRunJob_3.getErrorCode() == null) {
					globalMap.put("tRunJob_3_CHILD_RETURN_CODE",
							childJob_tRunJob_3.getStatus() != null && ("failure").equals(childJob_tRunJob_3.getStatus())
									? 1
									: 0);
				} else {
					globalMap.put("tRunJob_3_CHILD_RETURN_CODE", childJob_tRunJob_3.getErrorCode());
				}
				if (childJob_tRunJob_3.getExceptionStackTrace() != null) {
					globalMap.put("tRunJob_3_CHILD_EXCEPTION_STACKTRACE", childJob_tRunJob_3.getExceptionStackTrace());
				}
				errorCode = childJob_tRunJob_3.getErrorCode();
				if (childJob_tRunJob_3.getErrorCode() != null || ("failure").equals(childJob_tRunJob_3.getStatus())) {
					java.lang.Exception ce_tRunJob_3 = childJob_tRunJob_3.getException();
					throw new RuntimeException("Child job running failed.\n" + ((ce_tRunJob_3 != null)
							? (ce_tRunJob_3.getClass().getName() + ": " + ce_tRunJob_3.getMessage())
							: ""));
				}

				tos_count_tRunJob_3++;

				/**
				 * [tRunJob_3 main ] stop
				 */

				/**
				 * [tRunJob_3 process_data_begin ] start
				 */

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				/**
				 * [tRunJob_3 process_data_begin ] stop
				 */

				/**
				 * [tRunJob_3 process_data_end ] start
				 */

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				/**
				 * [tRunJob_3 process_data_end ] stop
				 */

				/**
				 * [tRunJob_3 end ] start
				 */

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				if (log.isDebugEnabled())
					log.debug("tRunJob_3 - " + ("Done."));

				ok_Hash.put("tRunJob_3", true);
				end_Hash.put("tRunJob_3", System.currentTimeMillis());

				/**
				 * [tRunJob_3 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRunJob_3:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk3", 0, "ok");
			}

			tRunJob_4Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRunJob_3 finally ] start
				 */

				s(currentComponent = "tRunJob_3");

				cLabel = "load_fact_orders";

				/**
				 * [tRunJob_3 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRunJob_3_SUBPROCESS_STATE", 1);
	}

	public void tRunJob_4Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRunJob_4_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRunJob_4", "afjCN1_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tRunJob_4 begin ] start
				 */

				sh("tRunJob_4");

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				int tos_count_tRunJob_4 = 0;

				if (log.isDebugEnabled())
					log.debug("tRunJob_4 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRunJob_4 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRunJob_4 = new StringBuilder();
							log4jParamters_tRunJob_4.append("Parameters:");
							log4jParamters_tRunJob_4.append("USE_DYNAMIC_JOB" + " = " + "false");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("PROCESS" + " = " + "load_fact_order_items");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("USE_INDEPENDENT_PROCESS" + " = " + "false");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("DIE_ON_CHILD_ERROR" + " = " + "true");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("TRANSMIT_WHOLE_CONTEXT" + " = " + "true");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("CONTEXTPARAMS" + " = " + "[]");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("PROPAGATE_CHILD_RESULT" + " = " + "false");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("PRINT_PARAMETER" + " = " + "false");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("USE_DYNAMIC_CONTEXT" + " = " + "false");
							log4jParamters_tRunJob_4.append(" | ");
							log4jParamters_tRunJob_4.append("USE_BASE64" + " = " + "true");
							log4jParamters_tRunJob_4.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tRunJob_4 - " + (log4jParamters_tRunJob_4));
						}
					}
					new BytesLimit65535_tRunJob_4().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tRunJob_4 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_DYNAMIC_JOB", String.valueOf("false"));
								component_parameters.put("PROCESS", String.valueOf("load_fact_order_items"));
								component_parameters.put("USE_INDEPENDENT_PROCESS", String.valueOf("false"));
								component_parameters.put("DIE_ON_CHILD_ERROR", String.valueOf("true"));
								component_parameters.put("TRANSMIT_WHOLE_CONTEXT", String.valueOf("true"));
								component_parameters.put("CONTEXTPARAMS", String.valueOf("[]"));
								component_parameters.put("PROPAGATE_CHILD_RESULT", String.valueOf("false"));
								component_parameters.put("PRINT_PARAMETER", String.valueOf("false"));
								component_parameters.put("USE_DYNAMIC_CONTEXT", String.valueOf("false"));
								component_parameters.put("USE_BASE64", String.valueOf("true"));

							} catch (java.lang.Exception e_tRunJob_4) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tRunJob_4", "tRunJob",
							new ParameterUtil_tRunJob_4().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tRunJob_4", "load_fact_order_items", "tRunJob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRunJob_4 begin ] stop
				 */

				/**
				 * [tRunJob_4 main ] start
				 */

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				java.util.List<String> paraList_tRunJob_4 = new java.util.ArrayList<String>();

				paraList_tRunJob_4.add("--father_pid=" + pid);

				paraList_tRunJob_4.add("--root_pid=" + rootPid);

				paraList_tRunJob_4.add("--father_node=tRunJob_4");

				paraList_tRunJob_4.add("--context=Default");

				if (!"".equals(log4jLevel)) {
					paraList_tRunJob_4.add("--log4jLevel=" + log4jLevel);
				}

				if (enableLogStash) {
					paraList_tRunJob_4.add("--audit.enabled=" + enableLogStash);
				}

				paraList_tRunJob_4.add("--skipCBPAuth=true");

				// for feature:10589

				paraList_tRunJob_4.add("--stat_port=" + portStats);

				if (resuming_logs_dir_path != null) {
					paraList_tRunJob_4.add("--resuming_logs_dir_path=" + resuming_logs_dir_path);
				}
				String childResumePath_tRunJob_4 = ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path);
				String tRunJobName_tRunJob_4 = ResumeUtil.getRighttRunJob(resuming_checkpoint_path);
				if ("tRunJob_4".equals(tRunJobName_tRunJob_4) && childResumePath_tRunJob_4 != null) {
					paraList_tRunJob_4.add("--resuming_checkpoint_path="
							+ ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path));
				}
				paraList_tRunJob_4.add("--parent_part_launcher=JOB:" + jobName + "/NODE:tRunJob_4");

				java.util.Map<String, Object> parentContextMap_tRunJob_4 = new java.util.HashMap<String, Object>();

				context.synchronizeContext();
				class ContextProcessor_tRunJob_4 {
					private void transmitContext_0() {
						parentContextMap_tRunJob_4.put("DB_HOST", context.DB_HOST);
						paraList_tRunJob_4.add("--context_type " + "DB_HOST" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("DB_NAME", context.DB_NAME);
						paraList_tRunJob_4.add("--context_type " + "DB_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("DB_PASSWORD", context.DB_PASSWORD);
						paraList_tRunJob_4.add("--context_type " + "DB_PASSWORD" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("DB_PORT", context.DB_PORT);
						paraList_tRunJob_4.add("--context_type " + "DB_PORT" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("DB_USER", context.DB_USER);
						paraList_tRunJob_4.add("--context_type " + "DB_USER" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("ENV_NAME", context.ENV_NAME);
						paraList_tRunJob_4.add("--context_type " + "ENV_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("FILE_OLIST_CUSTOMERS", context.FILE_OLIST_CUSTOMERS);
						paraList_tRunJob_4.add("--context_type " + "FILE_OLIST_CUSTOMERS" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("FILE_OLIST_ORDER_ITEMS", context.FILE_OLIST_ORDER_ITEMS);
						paraList_tRunJob_4.add("--context_type " + "FILE_OLIST_ORDER_ITEMS" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("FILE_OLIST_ORDER_PAYMENTS", context.FILE_OLIST_ORDER_PAYMENTS);
						paraList_tRunJob_4.add("--context_type " + "FILE_OLIST_ORDER_PAYMENTS" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("FILE_OLIST_ORDERS", context.FILE_OLIST_ORDERS);
						paraList_tRunJob_4.add("--context_type " + "FILE_OLIST_ORDERS" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("FILE_OLIST_PRODUCTS", context.FILE_OLIST_PRODUCTS);
						paraList_tRunJob_4.add("--context_type " + "FILE_OLIST_PRODUCTS" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("LOAD_SOURCE_TAG", context.LOAD_SOURCE_TAG);
						paraList_tRunJob_4.add("--context_type " + "LOAD_SOURCE_TAG" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("PIPELINE_NAME", context.PIPELINE_NAME);
						paraList_tRunJob_4.add("--context_type " + "PIPELINE_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_4.put("RUN_ID", context.RUN_ID);
						paraList_tRunJob_4.add("--context_type " + "RUN_ID" + "=" + "id_Long");
					}

					public void transmitAllContext() {
						transmitContext_0();
					}
				}
				new ContextProcessor_tRunJob_4().transmitAllContext();
				java.util.Enumeration<?> propertyNames_tRunJob_4 = context.propertyNames();
				while (propertyNames_tRunJob_4.hasMoreElements()) {
					String key_tRunJob_4 = (String) propertyNames_tRunJob_4.nextElement();
					Object value_tRunJob_4 = (Object) context.get(key_tRunJob_4);
					if (value_tRunJob_4 != null) {

						paraList_tRunJob_4.add("--context_param " + key_tRunJob_4 + "=" + value_tRunJob_4);

					} else {
						paraList_tRunJob_4.add("--context_param " + key_tRunJob_4 + "="
								+ NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY);
					}

				}

				Object obj_tRunJob_4 = null;

				capacitacion_talend.load_fact_order_items_0_1.load_fact_order_items childJob_tRunJob_4 = new capacitacion_talend.load_fact_order_items_0_1.load_fact_order_items();
				// pass DataSources
				java.util.Map<String, routines.system.TalendDataSource> talendDataSources_tRunJob_4 = (java.util.Map<String, routines.system.TalendDataSource>) globalMap
						.get(KEY_DB_DATASOURCES);
				if (null != talendDataSources_tRunJob_4) {
					java.util.Map<String, javax.sql.DataSource> dataSources_tRunJob_4 = new java.util.HashMap<String, javax.sql.DataSource>();
					for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry_tRunJob_4 : talendDataSources_tRunJob_4
							.entrySet()) {
						dataSources_tRunJob_4.put(talendDataSourceEntry_tRunJob_4.getKey(),
								talendDataSourceEntry_tRunJob_4.getValue().getRawDataSource());
					}
					childJob_tRunJob_4.setDataSources(dataSources_tRunJob_4);
				}

				childJob_tRunJob_4.parentContextMap = parentContextMap_tRunJob_4;

				log.info(
						"tRunJob_4 - The child job 'capacitacion_talend.load_fact_order_items_0_1.load_fact_order_items' starts on the version '0.1' with the context 'Default'.");

				String[][] childReturn_tRunJob_4 = childJob_tRunJob_4
						.runJob((String[]) paraList_tRunJob_4.toArray(new String[paraList_tRunJob_4.size()]));

				log.info(
						"tRunJob_4 - The child job 'capacitacion_talend.load_fact_order_items_0_1.load_fact_order_items' is done.");

				if (childJob_tRunJob_4.getErrorCode() == null) {
					globalMap.put("tRunJob_4_CHILD_RETURN_CODE",
							childJob_tRunJob_4.getStatus() != null && ("failure").equals(childJob_tRunJob_4.getStatus())
									? 1
									: 0);
				} else {
					globalMap.put("tRunJob_4_CHILD_RETURN_CODE", childJob_tRunJob_4.getErrorCode());
				}
				if (childJob_tRunJob_4.getExceptionStackTrace() != null) {
					globalMap.put("tRunJob_4_CHILD_EXCEPTION_STACKTRACE", childJob_tRunJob_4.getExceptionStackTrace());
				}
				errorCode = childJob_tRunJob_4.getErrorCode();
				if (childJob_tRunJob_4.getErrorCode() != null || ("failure").equals(childJob_tRunJob_4.getStatus())) {
					java.lang.Exception ce_tRunJob_4 = childJob_tRunJob_4.getException();
					throw new RuntimeException("Child job running failed.\n" + ((ce_tRunJob_4 != null)
							? (ce_tRunJob_4.getClass().getName() + ": " + ce_tRunJob_4.getMessage())
							: ""));
				}

				tos_count_tRunJob_4++;

				/**
				 * [tRunJob_4 main ] stop
				 */

				/**
				 * [tRunJob_4 process_data_begin ] start
				 */

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				/**
				 * [tRunJob_4 process_data_begin ] stop
				 */

				/**
				 * [tRunJob_4 process_data_end ] start
				 */

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				/**
				 * [tRunJob_4 process_data_end ] stop
				 */

				/**
				 * [tRunJob_4 end ] start
				 */

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				if (log.isDebugEnabled())
					log.debug("tRunJob_4 - " + ("Done."));

				ok_Hash.put("tRunJob_4", true);
				end_Hash.put("tRunJob_4", System.currentTimeMillis());

				/**
				 * [tRunJob_4 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRunJob_4:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk4", 0, "ok");
			}

			tRunJob_5Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRunJob_4 finally ] start
				 */

				s(currentComponent = "tRunJob_4");

				cLabel = "load_fact_order_items";

				/**
				 * [tRunJob_4 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRunJob_4_SUBPROCESS_STATE", 1);
	}

	public void tRunJob_5Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRunJob_5_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRunJob_5", "mpx3B2_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tRunJob_5 begin ] start
				 */

				sh("tRunJob_5");

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				int tos_count_tRunJob_5 = 0;

				if (log.isDebugEnabled())
					log.debug("tRunJob_5 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRunJob_5 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRunJob_5 = new StringBuilder();
							log4jParamters_tRunJob_5.append("Parameters:");
							log4jParamters_tRunJob_5.append("USE_DYNAMIC_JOB" + " = " + "false");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("PROCESS" + " = " + "load_fact_order_payments");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("USE_INDEPENDENT_PROCESS" + " = " + "false");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("DIE_ON_CHILD_ERROR" + " = " + "true");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("TRANSMIT_WHOLE_CONTEXT" + " = " + "true");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("CONTEXTPARAMS" + " = " + "[]");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("PROPAGATE_CHILD_RESULT" + " = " + "false");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("PRINT_PARAMETER" + " = " + "false");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("USE_DYNAMIC_CONTEXT" + " = " + "false");
							log4jParamters_tRunJob_5.append(" | ");
							log4jParamters_tRunJob_5.append("USE_BASE64" + " = " + "true");
							log4jParamters_tRunJob_5.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tRunJob_5 - " + (log4jParamters_tRunJob_5));
						}
					}
					new BytesLimit65535_tRunJob_5().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tRunJob_5 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_DYNAMIC_JOB", String.valueOf("false"));
								component_parameters.put("PROCESS", String.valueOf("load_fact_order_payments"));
								component_parameters.put("USE_INDEPENDENT_PROCESS", String.valueOf("false"));
								component_parameters.put("DIE_ON_CHILD_ERROR", String.valueOf("true"));
								component_parameters.put("TRANSMIT_WHOLE_CONTEXT", String.valueOf("true"));
								component_parameters.put("CONTEXTPARAMS", String.valueOf("[]"));
								component_parameters.put("PROPAGATE_CHILD_RESULT", String.valueOf("false"));
								component_parameters.put("PRINT_PARAMETER", String.valueOf("false"));
								component_parameters.put("USE_DYNAMIC_CONTEXT", String.valueOf("false"));
								component_parameters.put("USE_BASE64", String.valueOf("true"));

							} catch (java.lang.Exception e_tRunJob_5) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tRunJob_5", "tRunJob",
							new ParameterUtil_tRunJob_5().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tRunJob_5", "load_fact_order_payments", "tRunJob");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRunJob_5 begin ] stop
				 */

				/**
				 * [tRunJob_5 main ] start
				 */

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				java.util.List<String> paraList_tRunJob_5 = new java.util.ArrayList<String>();

				paraList_tRunJob_5.add("--father_pid=" + pid);

				paraList_tRunJob_5.add("--root_pid=" + rootPid);

				paraList_tRunJob_5.add("--father_node=tRunJob_5");

				paraList_tRunJob_5.add("--context=Default");

				if (!"".equals(log4jLevel)) {
					paraList_tRunJob_5.add("--log4jLevel=" + log4jLevel);
				}

				if (enableLogStash) {
					paraList_tRunJob_5.add("--audit.enabled=" + enableLogStash);
				}

				paraList_tRunJob_5.add("--skipCBPAuth=true");

				// for feature:10589

				paraList_tRunJob_5.add("--stat_port=" + portStats);

				if (resuming_logs_dir_path != null) {
					paraList_tRunJob_5.add("--resuming_logs_dir_path=" + resuming_logs_dir_path);
				}
				String childResumePath_tRunJob_5 = ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path);
				String tRunJobName_tRunJob_5 = ResumeUtil.getRighttRunJob(resuming_checkpoint_path);
				if ("tRunJob_5".equals(tRunJobName_tRunJob_5) && childResumePath_tRunJob_5 != null) {
					paraList_tRunJob_5.add("--resuming_checkpoint_path="
							+ ResumeUtil.getChildJobCheckPointPath(resuming_checkpoint_path));
				}
				paraList_tRunJob_5.add("--parent_part_launcher=JOB:" + jobName + "/NODE:tRunJob_5");

				java.util.Map<String, Object> parentContextMap_tRunJob_5 = new java.util.HashMap<String, Object>();

				context.synchronizeContext();
				class ContextProcessor_tRunJob_5 {
					private void transmitContext_0() {
						parentContextMap_tRunJob_5.put("DB_HOST", context.DB_HOST);
						paraList_tRunJob_5.add("--context_type " + "DB_HOST" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("DB_NAME", context.DB_NAME);
						paraList_tRunJob_5.add("--context_type " + "DB_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("DB_PASSWORD", context.DB_PASSWORD);
						paraList_tRunJob_5.add("--context_type " + "DB_PASSWORD" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("DB_PORT", context.DB_PORT);
						paraList_tRunJob_5.add("--context_type " + "DB_PORT" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("DB_USER", context.DB_USER);
						paraList_tRunJob_5.add("--context_type " + "DB_USER" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("ENV_NAME", context.ENV_NAME);
						paraList_tRunJob_5.add("--context_type " + "ENV_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("FILE_OLIST_CUSTOMERS", context.FILE_OLIST_CUSTOMERS);
						paraList_tRunJob_5.add("--context_type " + "FILE_OLIST_CUSTOMERS" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("FILE_OLIST_ORDER_ITEMS", context.FILE_OLIST_ORDER_ITEMS);
						paraList_tRunJob_5.add("--context_type " + "FILE_OLIST_ORDER_ITEMS" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("FILE_OLIST_ORDER_PAYMENTS", context.FILE_OLIST_ORDER_PAYMENTS);
						paraList_tRunJob_5.add("--context_type " + "FILE_OLIST_ORDER_PAYMENTS" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("FILE_OLIST_ORDERS", context.FILE_OLIST_ORDERS);
						paraList_tRunJob_5.add("--context_type " + "FILE_OLIST_ORDERS" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("FILE_OLIST_PRODUCTS", context.FILE_OLIST_PRODUCTS);
						paraList_tRunJob_5.add("--context_type " + "FILE_OLIST_PRODUCTS" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("LOAD_SOURCE_TAG", context.LOAD_SOURCE_TAG);
						paraList_tRunJob_5.add("--context_type " + "LOAD_SOURCE_TAG" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("PIPELINE_NAME", context.PIPELINE_NAME);
						paraList_tRunJob_5.add("--context_type " + "PIPELINE_NAME" + "=" + "id_String");
						parentContextMap_tRunJob_5.put("RUN_ID", context.RUN_ID);
						paraList_tRunJob_5.add("--context_type " + "RUN_ID" + "=" + "id_Long");
					}

					public void transmitAllContext() {
						transmitContext_0();
					}
				}
				new ContextProcessor_tRunJob_5().transmitAllContext();
				java.util.Enumeration<?> propertyNames_tRunJob_5 = context.propertyNames();
				while (propertyNames_tRunJob_5.hasMoreElements()) {
					String key_tRunJob_5 = (String) propertyNames_tRunJob_5.nextElement();
					Object value_tRunJob_5 = (Object) context.get(key_tRunJob_5);
					if (value_tRunJob_5 != null) {

						paraList_tRunJob_5.add("--context_param " + key_tRunJob_5 + "=" + value_tRunJob_5);

					} else {
						paraList_tRunJob_5.add("--context_param " + key_tRunJob_5 + "="
								+ NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY);
					}

				}

				Object obj_tRunJob_5 = null;

				capacitacion_talend.load_fact_order_payments_0_1.load_fact_order_payments childJob_tRunJob_5 = new capacitacion_talend.load_fact_order_payments_0_1.load_fact_order_payments();
				// pass DataSources
				java.util.Map<String, routines.system.TalendDataSource> talendDataSources_tRunJob_5 = (java.util.Map<String, routines.system.TalendDataSource>) globalMap
						.get(KEY_DB_DATASOURCES);
				if (null != talendDataSources_tRunJob_5) {
					java.util.Map<String, javax.sql.DataSource> dataSources_tRunJob_5 = new java.util.HashMap<String, javax.sql.DataSource>();
					for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry_tRunJob_5 : talendDataSources_tRunJob_5
							.entrySet()) {
						dataSources_tRunJob_5.put(talendDataSourceEntry_tRunJob_5.getKey(),
								talendDataSourceEntry_tRunJob_5.getValue().getRawDataSource());
					}
					childJob_tRunJob_5.setDataSources(dataSources_tRunJob_5);
				}

				childJob_tRunJob_5.parentContextMap = parentContextMap_tRunJob_5;

				log.info(
						"tRunJob_5 - The child job 'capacitacion_talend.load_fact_order_payments_0_1.load_fact_order_payments' starts on the version '0.1' with the context 'Default'.");

				String[][] childReturn_tRunJob_5 = childJob_tRunJob_5
						.runJob((String[]) paraList_tRunJob_5.toArray(new String[paraList_tRunJob_5.size()]));

				log.info(
						"tRunJob_5 - The child job 'capacitacion_talend.load_fact_order_payments_0_1.load_fact_order_payments' is done.");

				if (childJob_tRunJob_5.getErrorCode() == null) {
					globalMap.put("tRunJob_5_CHILD_RETURN_CODE",
							childJob_tRunJob_5.getStatus() != null && ("failure").equals(childJob_tRunJob_5.getStatus())
									? 1
									: 0);
				} else {
					globalMap.put("tRunJob_5_CHILD_RETURN_CODE", childJob_tRunJob_5.getErrorCode());
				}
				if (childJob_tRunJob_5.getExceptionStackTrace() != null) {
					globalMap.put("tRunJob_5_CHILD_EXCEPTION_STACKTRACE", childJob_tRunJob_5.getExceptionStackTrace());
				}
				errorCode = childJob_tRunJob_5.getErrorCode();
				if (childJob_tRunJob_5.getErrorCode() != null || ("failure").equals(childJob_tRunJob_5.getStatus())) {
					java.lang.Exception ce_tRunJob_5 = childJob_tRunJob_5.getException();
					throw new RuntimeException("Child job running failed.\n" + ((ce_tRunJob_5 != null)
							? (ce_tRunJob_5.getClass().getName() + ": " + ce_tRunJob_5.getMessage())
							: ""));
				}

				tos_count_tRunJob_5++;

				/**
				 * [tRunJob_5 main ] stop
				 */

				/**
				 * [tRunJob_5 process_data_begin ] start
				 */

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				/**
				 * [tRunJob_5 process_data_begin ] stop
				 */

				/**
				 * [tRunJob_5 process_data_end ] start
				 */

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				/**
				 * [tRunJob_5 process_data_end ] stop
				 */

				/**
				 * [tRunJob_5 end ] start
				 */

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				if (log.isDebugEnabled())
					log.debug("tRunJob_5 - " + ("Done."));

				ok_Hash.put("tRunJob_5", true);
				end_Hash.put("tRunJob_5", System.currentTimeMillis());

				/**
				 * [tRunJob_5 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRunJob_5:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk5", 0, "ok");
			}

			tDBRow_3Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRunJob_5 finally ] start
				 */

				s(currentComponent = "tRunJob_5");

				cLabel = "load_fact_order_payments";

				/**
				 * [tRunJob_5 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRunJob_5_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_3Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_3_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_3", "utnmVP_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_3 begin ] start
				 */

				sh("tDBRow_3");

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				int tos_count_tDBRow_3 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_3 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_3 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_3 = new StringBuilder();
							log4jParamters_tDBRow_3.append("Parameters:");
							log4jParamters_tDBRow_3.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'OK', \" + \"ended_at = NOW(), \" + \"notes = 'Pipeline MP-01 completado correctamente' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_3.append(" | ");
							log4jParamters_tDBRow_3.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_3.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_3 - " + (log4jParamters_tDBRow_3));
						}
					}
					new BytesLimit65535_tDBRow_3().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_3 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'OK', "
												+ "ended_at = NOW(), "
												+ "notes = 'Pipeline MP-01 completado correctamente' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_3) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_3", "tMysqlRow",
							new ParameterUtil_tDBRow_3().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_3", "Auditor\u00EDa OK", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_3 = null;
				String query_tDBRow_3 = "";
				boolean whetherReject_tDBRow_3 = false;
				int count_tDBRow_3 = 0;
				conn_tDBRow_3 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_3 != null) {
					if (conn_tDBRow_3.getMetaData() != null) {

						log.debug("tDBRow_3 - Uses an existing connection with username '"
								+ conn_tDBRow_3.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_3.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_3", conn_tDBRow_3);
				java.sql.Statement stmt_tDBRow_3 = conn_tDBRow_3.createStatement();
				resourceMap.put("stmt_tDBRow_3", stmt_tDBRow_3);

				/**
				 * [tDBRow_3 begin ] stop
				 */

				/**
				 * [tDBRow_3 main ] start
				 */

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				query_tDBRow_3 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'OK', " + "ended_at = NOW(), "
						+ "notes = 'Pipeline MP-01 completado correctamente' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_3 = false;
				log.debug("tDBRow_3 - Executing the query: '" + query_tDBRow_3 + "'.");

				globalMap.put("tDBRow_3_QUERY", query_tDBRow_3);
				try {
					stmt_tDBRow_3.execute(query_tDBRow_3);
					log.debug("tDBRow_3 - Execute the query: '" + query_tDBRow_3 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_3 = true;

					throw (e);

				}

				tos_count_tDBRow_3++;

				/**
				 * [tDBRow_3 main ] stop
				 */

				/**
				 * [tDBRow_3 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				/**
				 * [tDBRow_3 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_3 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				/**
				 * [tDBRow_3 process_data_end ] stop
				 */

				/**
				 * [tDBRow_3 end ] start
				 */

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				globalMap.put("tDBRow_3_NB_LINE", count_tDBRow_3);
				stmt_tDBRow_3.close();
				resourceMap.remove("stmt_tDBRow_3");
				resourceMap.put("statementClosed_tDBRow_3", true);
				resourceMap.put("finish_tDBRow_3", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_3 - " + ("Done."));

				ok_Hash.put("tDBRow_3", true);
				end_Hash.put("tDBRow_3", System.currentTimeMillis());

				/**
				 * [tDBRow_3 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_3 finally ] start
				 */

				s(currentComponent = "tDBRow_3");

				cLabel = "Auditor\u00EDa OK";

				if (resourceMap.get("statementClosed_tDBRow_3") == null) {
					java.sql.Statement stmtToClose_tDBRow_3 = null;
					if ((stmtToClose_tDBRow_3 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_3")) != null) {
						stmtToClose_tDBRow_3.close();
					}
				}

				/**
				 * [tDBRow_3 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_3_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_6Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_6_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_6", "WMWGKj_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_6 begin ] start
				 */

				sh("tDBRow_6");

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				int tos_count_tDBRow_6 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_6 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_6 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_6 = new StringBuilder();
							log4jParamters_tDBRow_6.append("Parameters:");
							log4jParamters_tDBRow_6.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'FAILED', ended_at = NOW(), \" + \"notes = 'Error durante la ejecucion del pipeline' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_6.append(" | ");
							log4jParamters_tDBRow_6.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_6.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_6 - " + (log4jParamters_tDBRow_6));
						}
					}
					new BytesLimit65535_tDBRow_6().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_6 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 "
												+ "SET status = 'FAILED', ended_at = NOW(), "
												+ "notes = 'Error durante la ejecucion del pipeline' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_6) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_6", "tMysqlRow",
							new ParameterUtil_tDBRow_6().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_6", "Auditor\u00EDa FAILED_order_payments", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_6 = null;
				String query_tDBRow_6 = "";
				boolean whetherReject_tDBRow_6 = false;
				int count_tDBRow_6 = 0;
				conn_tDBRow_6 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_6 != null) {
					if (conn_tDBRow_6.getMetaData() != null) {

						log.debug("tDBRow_6 - Uses an existing connection with username '"
								+ conn_tDBRow_6.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_6.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_6", conn_tDBRow_6);
				java.sql.Statement stmt_tDBRow_6 = conn_tDBRow_6.createStatement();
				resourceMap.put("stmt_tDBRow_6", stmt_tDBRow_6);

				/**
				 * [tDBRow_6 begin ] stop
				 */

				/**
				 * [tDBRow_6 main ] start
				 */

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				query_tDBRow_6 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'FAILED', ended_at = NOW(), "
						+ "notes = 'Error durante la ejecucion del pipeline' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_6 = false;
				log.debug("tDBRow_6 - Executing the query: '" + query_tDBRow_6 + "'.");

				globalMap.put("tDBRow_6_QUERY", query_tDBRow_6);
				try {
					stmt_tDBRow_6.execute(query_tDBRow_6);
					log.debug("tDBRow_6 - Execute the query: '" + query_tDBRow_6 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_6 = true;

					throw (e);

				}

				tos_count_tDBRow_6++;

				/**
				 * [tDBRow_6 main ] stop
				 */

				/**
				 * [tDBRow_6 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				/**
				 * [tDBRow_6 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_6 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				/**
				 * [tDBRow_6 process_data_end ] stop
				 */

				/**
				 * [tDBRow_6 end ] start
				 */

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				globalMap.put("tDBRow_6_NB_LINE", count_tDBRow_6);
				stmt_tDBRow_6.close();
				resourceMap.remove("stmt_tDBRow_6");
				resourceMap.put("statementClosed_tDBRow_6", true);
				resourceMap.put("finish_tDBRow_6", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_6 - " + ("Done."));

				ok_Hash.put("tDBRow_6", true);
				end_Hash.put("tDBRow_6", System.currentTimeMillis());

				/**
				 * [tDBRow_6 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_6 finally ] start
				 */

				s(currentComponent = "tDBRow_6");

				cLabel = "Auditor\u00EDa FAILED_order_payments";

				if (resourceMap.get("statementClosed_tDBRow_6") == null) {
					java.sql.Statement stmtToClose_tDBRow_6 = null;
					if ((stmtToClose_tDBRow_6 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_6")) != null) {
						stmtToClose_tDBRow_6.close();
					}
				}

				/**
				 * [tDBRow_6 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_6_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_8Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_8_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_8", "PSPDkW_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_8 begin ] start
				 */

				sh("tDBRow_8");

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				int tos_count_tDBRow_8 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_8 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_8 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_8 = new StringBuilder();
							log4jParamters_tDBRow_8.append("Parameters:");
							log4jParamters_tDBRow_8.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'FAILED', ended_at = NOW(), \" + \"notes = 'Error durante la ejecucion del pipeline' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_8.append(" | ");
							log4jParamters_tDBRow_8.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_8.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_8 - " + (log4jParamters_tDBRow_8));
						}
					}
					new BytesLimit65535_tDBRow_8().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_8 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 "
												+ "SET status = 'FAILED', ended_at = NOW(), "
												+ "notes = 'Error durante la ejecucion del pipeline' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_8) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_8", "tMysqlRow",
							new ParameterUtil_tDBRow_8().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_8", "Auditor\u00EDa FAILED_orders_items", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_8 = null;
				String query_tDBRow_8 = "";
				boolean whetherReject_tDBRow_8 = false;
				int count_tDBRow_8 = 0;
				conn_tDBRow_8 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_8 != null) {
					if (conn_tDBRow_8.getMetaData() != null) {

						log.debug("tDBRow_8 - Uses an existing connection with username '"
								+ conn_tDBRow_8.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_8.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_8", conn_tDBRow_8);
				java.sql.Statement stmt_tDBRow_8 = conn_tDBRow_8.createStatement();
				resourceMap.put("stmt_tDBRow_8", stmt_tDBRow_8);

				/**
				 * [tDBRow_8 begin ] stop
				 */

				/**
				 * [tDBRow_8 main ] start
				 */

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				query_tDBRow_8 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'FAILED', ended_at = NOW(), "
						+ "notes = 'Error durante la ejecucion del pipeline' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_8 = false;
				log.debug("tDBRow_8 - Executing the query: '" + query_tDBRow_8 + "'.");

				globalMap.put("tDBRow_8_QUERY", query_tDBRow_8);
				try {
					stmt_tDBRow_8.execute(query_tDBRow_8);
					log.debug("tDBRow_8 - Execute the query: '" + query_tDBRow_8 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_8 = true;

					throw (e);

				}

				tos_count_tDBRow_8++;

				/**
				 * [tDBRow_8 main ] stop
				 */

				/**
				 * [tDBRow_8 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				/**
				 * [tDBRow_8 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_8 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				/**
				 * [tDBRow_8 process_data_end ] stop
				 */

				/**
				 * [tDBRow_8 end ] start
				 */

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				globalMap.put("tDBRow_8_NB_LINE", count_tDBRow_8);
				stmt_tDBRow_8.close();
				resourceMap.remove("stmt_tDBRow_8");
				resourceMap.put("statementClosed_tDBRow_8", true);
				resourceMap.put("finish_tDBRow_8", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_8 - " + ("Done."));

				ok_Hash.put("tDBRow_8", true);
				end_Hash.put("tDBRow_8", System.currentTimeMillis());

				/**
				 * [tDBRow_8 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_8 finally ] start
				 */

				s(currentComponent = "tDBRow_8");

				cLabel = "Auditor\u00EDa FAILED_orders_items";

				if (resourceMap.get("statementClosed_tDBRow_8") == null) {
					java.sql.Statement stmtToClose_tDBRow_8 = null;
					if ((stmtToClose_tDBRow_8 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_8")) != null) {
						stmtToClose_tDBRow_8.close();
					}
				}

				/**
				 * [tDBRow_8 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_8_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_7Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_7_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_7", "1oBGK2_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_7 begin ] start
				 */

				sh("tDBRow_7");

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				int tos_count_tDBRow_7 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_7 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_7 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_7 = new StringBuilder();
							log4jParamters_tDBRow_7.append("Parameters:");
							log4jParamters_tDBRow_7.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'FAILED', ended_at = NOW(), \" + \"notes = 'Error durante la ejecucion del pipeline' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_7.append(" | ");
							log4jParamters_tDBRow_7.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_7.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_7 - " + (log4jParamters_tDBRow_7));
						}
					}
					new BytesLimit65535_tDBRow_7().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_7 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 "
												+ "SET status = 'FAILED', ended_at = NOW(), "
												+ "notes = 'Error durante la ejecucion del pipeline' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_7) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_7", "tMysqlRow",
							new ParameterUtil_tDBRow_7().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_7", "Auditor\u00EDa FAILED_orders", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_7 = null;
				String query_tDBRow_7 = "";
				boolean whetherReject_tDBRow_7 = false;
				int count_tDBRow_7 = 0;
				conn_tDBRow_7 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_7 != null) {
					if (conn_tDBRow_7.getMetaData() != null) {

						log.debug("tDBRow_7 - Uses an existing connection with username '"
								+ conn_tDBRow_7.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_7.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_7", conn_tDBRow_7);
				java.sql.Statement stmt_tDBRow_7 = conn_tDBRow_7.createStatement();
				resourceMap.put("stmt_tDBRow_7", stmt_tDBRow_7);

				/**
				 * [tDBRow_7 begin ] stop
				 */

				/**
				 * [tDBRow_7 main ] start
				 */

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				query_tDBRow_7 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'FAILED', ended_at = NOW(), "
						+ "notes = 'Error durante la ejecucion del pipeline' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_7 = false;
				log.debug("tDBRow_7 - Executing the query: '" + query_tDBRow_7 + "'.");

				globalMap.put("tDBRow_7_QUERY", query_tDBRow_7);
				try {
					stmt_tDBRow_7.execute(query_tDBRow_7);
					log.debug("tDBRow_7 - Execute the query: '" + query_tDBRow_7 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_7 = true;

					throw (e);

				}

				tos_count_tDBRow_7++;

				/**
				 * [tDBRow_7 main ] stop
				 */

				/**
				 * [tDBRow_7 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				/**
				 * [tDBRow_7 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_7 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				/**
				 * [tDBRow_7 process_data_end ] stop
				 */

				/**
				 * [tDBRow_7 end ] start
				 */

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				globalMap.put("tDBRow_7_NB_LINE", count_tDBRow_7);
				stmt_tDBRow_7.close();
				resourceMap.remove("stmt_tDBRow_7");
				resourceMap.put("statementClosed_tDBRow_7", true);
				resourceMap.put("finish_tDBRow_7", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_7 - " + ("Done."));

				ok_Hash.put("tDBRow_7", true);
				end_Hash.put("tDBRow_7", System.currentTimeMillis());

				/**
				 * [tDBRow_7 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_7 finally ] start
				 */

				s(currentComponent = "tDBRow_7");

				cLabel = "Auditor\u00EDa FAILED_orders";

				if (resourceMap.get("statementClosed_tDBRow_7") == null) {
					java.sql.Statement stmtToClose_tDBRow_7 = null;
					if ((stmtToClose_tDBRow_7 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_7")) != null) {
						stmtToClose_tDBRow_7.close();
					}
				}

				/**
				 * [tDBRow_7 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_7_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_5Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_5_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_5", "6ayIhh_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_5 begin ] start
				 */

				sh("tDBRow_5");

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				int tos_count_tDBRow_5 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_5 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_5 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_5 = new StringBuilder();
							log4jParamters_tDBRow_5.append("Parameters:");
							log4jParamters_tDBRow_5.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'FAILED', ended_at = NOW(), \" + \"notes = 'Error durante la ejecucion del pipeline' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_5.append(" | ");
							log4jParamters_tDBRow_5.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_5.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_5 - " + (log4jParamters_tDBRow_5));
						}
					}
					new BytesLimit65535_tDBRow_5().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_5 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 "
												+ "SET status = 'FAILED', ended_at = NOW(), "
												+ "notes = 'Error durante la ejecucion del pipeline' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_5) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_5", "tMysqlRow",
							new ParameterUtil_tDBRow_5().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_5", "Auditor\u00EDa FAILED_product", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_5 = null;
				String query_tDBRow_5 = "";
				boolean whetherReject_tDBRow_5 = false;
				int count_tDBRow_5 = 0;
				conn_tDBRow_5 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_5 != null) {
					if (conn_tDBRow_5.getMetaData() != null) {

						log.debug("tDBRow_5 - Uses an existing connection with username '"
								+ conn_tDBRow_5.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_5.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_5", conn_tDBRow_5);
				java.sql.Statement stmt_tDBRow_5 = conn_tDBRow_5.createStatement();
				resourceMap.put("stmt_tDBRow_5", stmt_tDBRow_5);

				/**
				 * [tDBRow_5 begin ] stop
				 */

				/**
				 * [tDBRow_5 main ] start
				 */

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				query_tDBRow_5 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'FAILED', ended_at = NOW(), "
						+ "notes = 'Error durante la ejecucion del pipeline' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_5 = false;
				log.debug("tDBRow_5 - Executing the query: '" + query_tDBRow_5 + "'.");

				globalMap.put("tDBRow_5_QUERY", query_tDBRow_5);
				try {
					stmt_tDBRow_5.execute(query_tDBRow_5);
					log.debug("tDBRow_5 - Execute the query: '" + query_tDBRow_5 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_5 = true;

					throw (e);

				}

				tos_count_tDBRow_5++;

				/**
				 * [tDBRow_5 main ] stop
				 */

				/**
				 * [tDBRow_5 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				/**
				 * [tDBRow_5 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_5 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				/**
				 * [tDBRow_5 process_data_end ] stop
				 */

				/**
				 * [tDBRow_5 end ] start
				 */

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				globalMap.put("tDBRow_5_NB_LINE", count_tDBRow_5);
				stmt_tDBRow_5.close();
				resourceMap.remove("stmt_tDBRow_5");
				resourceMap.put("statementClosed_tDBRow_5", true);
				resourceMap.put("finish_tDBRow_5", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_5 - " + ("Done."));

				ok_Hash.put("tDBRow_5", true);
				end_Hash.put("tDBRow_5", System.currentTimeMillis());

				/**
				 * [tDBRow_5 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_5 finally ] start
				 */

				s(currentComponent = "tDBRow_5");

				cLabel = "Auditor\u00EDa FAILED_product";

				if (resourceMap.get("statementClosed_tDBRow_5") == null) {
					java.sql.Statement stmtToClose_tDBRow_5 = null;
					if ((stmtToClose_tDBRow_5 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_5")) != null) {
						stmtToClose_tDBRow_5.close();
					}
				}

				/**
				 * [tDBRow_5 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_5_SUBPROCESS_STATE", 1);
	}

	public void tDBRow_4Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBRow_4_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBRow_4", "kgcped_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [tDBRow_4 begin ] start
				 */

				sh("tDBRow_4");

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				int tos_count_tDBRow_4 = 0;

				if (log.isDebugEnabled())
					log.debug("tDBRow_4 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDBRow_4 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDBRow_4 = new StringBuilder();
							log4jParamters_tDBRow_4.append("Parameters:");
							log4jParamters_tDBRow_4.append("USE_EXISTING_CONNECTION" + " = " + "true");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("CONNECTION" + " = " + "tDBConnection_1");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("QUERY" + " = "
									+ "\"UPDATE DSADEV.audit_pipeline_runs_gr5 \" + \"SET status = 'FAILED', ended_at = NOW(), \" + \"notes = 'Error durante la ejecucion del pipeline' \" + \"WHERE run_id = \" + context.RUN_ID");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("PROPAGATE_RECORD_SET" + " = " + "false");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("USE_PREPAREDSTATEMENT" + " = " + "false");
							log4jParamters_tDBRow_4.append(" | ");
							log4jParamters_tDBRow_4.append("UNIFIED_COMPONENTS" + " = " + "tMysqlRow");
							log4jParamters_tDBRow_4.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDBRow_4 - " + (log4jParamters_tDBRow_4));
						}
					}
					new BytesLimit65535_tDBRow_4().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tDBRow_4 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter() throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("true"));
								component_parameters.put("CONNECTION", String.valueOf("tDBConnection_1"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("UPDATE DSADEV.audit_pipeline_runs_gr5 "
												+ "SET status = 'FAILED', ended_at = NOW(), "
												+ "notes = 'Error durante la ejecucion del pipeline' "
												+ "WHERE run_id = " + context.RUN_ID));
								component_parameters.put("DIE_ON_ERROR", String.valueOf("true"));
								component_parameters.put("PROPAGATE_RECORD_SET", String.valueOf("false"));
								component_parameters.put("USE_PREPAREDSTATEMENT", String.valueOf("false"));
								component_parameters.put("UNIFIED_COMPONENTS", String.valueOf("tMysqlRow"));

							} catch (java.lang.Exception e_tDBRow_4) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tDBRow_4", "tMysqlRow",
							new ParameterUtil_tDBRow_4().getParameter());
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tDBRow_4", "Auditor\u00EDa FAILED_customer", "tMysqlRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				java.sql.Connection conn_tDBRow_4 = null;
				String query_tDBRow_4 = "";
				boolean whetherReject_tDBRow_4 = false;
				int count_tDBRow_4 = 0;
				conn_tDBRow_4 = (java.sql.Connection) globalMap.get("conn_tDBConnection_1");

				if (log.isDebugEnabled() && conn_tDBRow_4 != null) {
					if (conn_tDBRow_4.getMetaData() != null) {

						log.debug("tDBRow_4 - Uses an existing connection with username '"
								+ conn_tDBRow_4.getMetaData().getUserName() + "'. Connection URL: "
								+ conn_tDBRow_4.getMetaData().getURL() + ".");

					}
				}

				resourceMap.put("conn_tDBRow_4", conn_tDBRow_4);
				java.sql.Statement stmt_tDBRow_4 = conn_tDBRow_4.createStatement();
				resourceMap.put("stmt_tDBRow_4", stmt_tDBRow_4);

				/**
				 * [tDBRow_4 begin ] stop
				 */

				/**
				 * [tDBRow_4 main ] start
				 */

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				query_tDBRow_4 = "UPDATE DSADEV.audit_pipeline_runs_gr5 " + "SET status = 'FAILED', ended_at = NOW(), "
						+ "notes = 'Error durante la ejecucion del pipeline' " + "WHERE run_id = " + context.RUN_ID;
				whetherReject_tDBRow_4 = false;
				log.debug("tDBRow_4 - Executing the query: '" + query_tDBRow_4 + "'.");

				globalMap.put("tDBRow_4_QUERY", query_tDBRow_4);
				try {
					stmt_tDBRow_4.execute(query_tDBRow_4);
					log.debug("tDBRow_4 - Execute the query: '" + query_tDBRow_4 + "' has finished.");

				} catch (java.lang.Exception e) {
					whetherReject_tDBRow_4 = true;

					throw (e);

				}

				tos_count_tDBRow_4++;

				/**
				 * [tDBRow_4 main ] stop
				 */

				/**
				 * [tDBRow_4 process_data_begin ] start
				 */

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				/**
				 * [tDBRow_4 process_data_begin ] stop
				 */

				/**
				 * [tDBRow_4 process_data_end ] start
				 */

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				/**
				 * [tDBRow_4 process_data_end ] stop
				 */

				/**
				 * [tDBRow_4 end ] start
				 */

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				globalMap.put("tDBRow_4_NB_LINE", count_tDBRow_4);
				stmt_tDBRow_4.close();
				resourceMap.remove("stmt_tDBRow_4");
				resourceMap.put("statementClosed_tDBRow_4", true);
				resourceMap.put("finish_tDBRow_4", true);

				if (log.isDebugEnabled())
					log.debug("tDBRow_4 - " + ("Done."));

				ok_Hash.put("tDBRow_4", true);
				end_Hash.put("tDBRow_4", System.currentTimeMillis());

				/**
				 * [tDBRow_4 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tDBRow_4 finally ] start
				 */

				s(currentComponent = "tDBRow_4");

				cLabel = "Auditor\u00EDa FAILED_customer";

				if (resourceMap.get("statementClosed_tDBRow_4") == null) {
					java.sql.Statement stmtToClose_tDBRow_4 = null;
					if ((stmtToClose_tDBRow_4 = (java.sql.Statement) resourceMap.remove("stmt_tDBRow_4")) != null) {
						stmtToClose_tDBRow_4.close();
					}
				}

				/**
				 * [tDBRow_4 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tDBRow_4_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [talendJobLog begin ] start
				 */

				sh("talendJobLog");

				s(currentComponent = "talendJobLog");

				int tos_count_talendJobLog = 0;

				for (JobStructureCatcherUtils.JobStructureCatcherMessage jcm : talendJobLog.getMessages()) {
					org.talend.job.audit.JobContextBuilder builder_talendJobLog = org.talend.job.audit.JobContextBuilder
							.create().jobName(jcm.job_name).jobId(jcm.job_id).jobVersion(jcm.job_version)
							.custom("process_id", jcm.pid).custom("thread_id", jcm.tid).custom("pid", pid)
							.custom("father_pid", fatherPid).custom("root_pid", rootPid);
					org.talend.logging.audit.Context log_context_talendJobLog = null;

					if (jcm.log_type == JobStructureCatcherUtils.LogType.PERFORMANCE) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.sourceId(jcm.sourceId)
								.sourceLabel(jcm.sourceLabel).sourceConnectorType(jcm.sourceComponentName)
								.targetId(jcm.targetId).targetLabel(jcm.targetLabel)
								.targetConnectorType(jcm.targetComponentName).connectionName(jcm.current_connector)
								.rows(jcm.row_count).duration(duration).build();
						auditLogger_talendJobLog.flowExecution(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBSTART) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).build();
						auditLogger_talendJobLog.jobstart(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBEND) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).duration(duration)
								.status(jcm.status).build();
						auditLogger_talendJobLog.jobstop(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNCOMPONENT) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment)
								.connectorType(jcm.component_name).connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label).build();
						auditLogger_talendJobLog.runcomponent(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWINPUT) {// log current component
																							// input line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowInput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWOUTPUT) {// log current component
																								// output/reject line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowOutput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBERROR) {
						java.lang.Exception e_talendJobLog = jcm.exception;
						if (e_talendJobLog != null) {
							try (java.io.StringWriter sw_talendJobLog = new java.io.StringWriter();
									java.io.PrintWriter pw_talendJobLog = new java.io.PrintWriter(sw_talendJobLog)) {
								e_talendJobLog.printStackTrace(pw_talendJobLog);
								builder_talendJobLog.custom("stacktrace", sw_talendJobLog.getBuffer().substring(0,
										java.lang.Math.min(sw_talendJobLog.getBuffer().length(), 512)));
							}
						}

						if (jcm.extra_info != null) {
							builder_talendJobLog.connectorId(jcm.component_id).custom("extra_info", jcm.extra_info);
						}

						log_context_talendJobLog = builder_talendJobLog
								.connectorType(jcm.component_id.substring(0, jcm.component_id.lastIndexOf('_')))
								.connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label == null ? jcm.component_id : jcm.component_label)
								.build();

						auditLogger_talendJobLog.exception(log_context_talendJobLog);
					}

					if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNTIMEPARAMETER) {
						builder_talendJobLog.connectorType(jcm.component_name).connectorId(jcm.component_id);

						for (java.util.Map.Entry<String, String> entry : jcm.component_parameters.entrySet()) {
							builder_talendJobLog.custom("P_" + entry.getKey(), entry.getValue());
						}

						log_context_talendJobLog = builder_talendJobLog.build();

						runtime_lineage_logger_talendJobLog.componentParameters(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNTIMESCHEMA) {
						builder_talendJobLog.sourceConnectorType(jcm.sourceComponentName).sourceId(jcm.sourceId)
								.connectionName(jcm.current_connector).schema(jcm.component_schema.toString())
								.targetConnectorType(jcm.targetComponentName).targetId(jcm.targetId);

						if (jcm.current_connector_type != null) {
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

				s(currentComponent = "talendJobLog");

				tos_count_talendJobLog++;

				/**
				 * [talendJobLog main ] stop
				 */

				/**
				 * [talendJobLog process_data_begin ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_begin ] stop
				 */

				/**
				 * [talendJobLog process_data_end ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_end ] stop
				 */

				/**
				 * [talendJobLog end ] start
				 */

				s(currentComponent = "talendJobLog");

				ok_Hash.put("talendJobLog", true);
				end_Hash.put("talendJobLog", System.currentTimeMillis());

				/**
				 * [talendJobLog end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendJobLog finally ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
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
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	protected PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	private final static java.util.Properties jobInfo = new java.util.Properties();
	private final static java.util.Map<String, String> mdcInfo = new java.util.HashMap<>();
	private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();

	public static void main(String[] args) {
		final mp01_input_port_olist mp01_input_port_olistClass = new mp01_input_port_olist();
		int exitCode = mp01_input_port_olistClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'mp01_input_port_olist' - Done.");
		}

		System.exit(exitCode);

	}

	private void getjobInfo() {
		final String TEMPLATE_PATH = "src/main/templates/jobInfo_template.properties";
		final String BUILD_PATH = "../jobInfo.properties";
		final String path = this.getClass().getResource("").getPath();
		if (path.lastIndexOf("target") > 0) {
			final java.io.File templateFile = new java.io.File(
					path.substring(0, path.lastIndexOf("target")).concat(TEMPLATE_PATH));
			if (templateFile.exists()) {
				readJobInfo(templateFile);
				return;
			}
		}
		readJobInfo(new java.io.File(BUILD_PATH));
	}

	private void readJobInfo(java.io.File jobInfoFile) {

		if (jobInfoFile.exists()) {
			try (java.io.InputStream is = new java.io.FileInputStream(jobInfoFile)) {
				jobInfo.load(is);
			} catch (IOException e) {

				log.debug("Read jobInfo.properties file fail: " + e.getMessage());

			}
		}
		log.info(String.format("Project name: %s\tJob name: %s\tGIT Commit ID: %s\tTalend Version: %s", projectName,
				jobName, jobInfo.getProperty("gitCommitId"), "8.0.1.20260619_1127-patch"));

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

		if (!"".equals(log4jLevel)) {

			if ("trace".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.OFF);
			}
			org.apache.logging.log4j.core.config.Configurator
					.setLevel(org.apache.logging.log4j.LogManager.getRootLogger().getName(), log.getLevel());
		}

		getjobInfo();

		log.info("TalendJob: 'mp01_input_port_olist' - Start.");

		java.util.Set<Object> jobInfoKeys = jobInfo.keySet();
		for (Object jobInfoKey : jobInfoKeys) {
			org.slf4j.MDC.put("_" + jobInfoKey.toString(), jobInfo.get(jobInfoKey).toString());
		}
		org.slf4j.MDC.put("_pid", pid);
		org.slf4j.MDC.put("_rootPid", rootPid);
		org.slf4j.MDC.put("_fatherPid", fatherPid);
		org.slf4j.MDC.put("_projectName", projectName);
		org.slf4j.MDC.put("_startTimestamp", java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
				.format(java.time.format.DateTimeFormatter.ISO_INSTANT));
		org.slf4j.MDC.put("_jobRepositoryId", "_07K9cM7yEfGK8YS6DqfsfQ");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2026-10-23T22:02:39.654390400Z");

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
			if (org.talend.metrics.CBPClient.getInstanceForCurrentVM() == null) {
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
						if (jobInfo.isEmpty()) {
							getjobInfo();
						}
					}
					org.talend.metrics.CBPClient.startListenIfNotStarted(enableCBP, isRooutJob, jobType,
							jobInfo.getProperty("artifactId"), true, jobInfo.getProperty("cmdLineVersion"),
							jobInfo.getProperty("patch"), jobInfo.getProperty("date"));
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
		if ((lineageDirPath != null && !lineageDirPath.isEmpty())
				|| (payloadFilePath != null && !payloadFilePath.isEmpty())) {
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
			if (lineageFilePath == null || lineageFilePath.isEmpty()) {
				String finalLineageDirPath = lineageDirPath;
				if (lineageDirPath == null || lineageDirPath.isEmpty()) {
					finalLineageDirPath = payloadFilePath + "_dir";
				}

				System.setProperty("runtime.lineage.appender.file.path",
						new StringBuilder().append(finalLineageDirPath)
								.append((finalLineageDirPath.endsWith("/") || finalLineageDirPath.endsWith("\\")) ? ""
										: java.io.File.separator)
								.append(projectName).append(java.io.File.separatorChar).append(jobName)
								.append(java.io.File.separatorChar).append(jobVersion)
								.append(java.io.File.separatorChar).append("runtime_log_")
								.append(new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()))
								.append(".json").toString());
			}

			System.getProperties().stringPropertyNames().stream()
					.filter(it -> it.startsWith("runtime.lineage.") && !"runtime.lineage.outputpath".equals(it))
					.forEach(key -> p_talendJobLog.setProperty(key.substring("runtime.lineage.".length()),
							System.getProperty(key)));

			org.apache.logging.log4j.core.config.Configurator.setLevel(p_talendJobLog.getProperty("root.logger"),
					org.apache.logging.log4j.Level.DEBUG);

			runtime_lineage_logger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory
					.createJobAuditLogger(p_talendJobLog);
		}

		if (enableLogStash) {
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

			System.getProperties().stringPropertyNames().stream().filter(it -> it.startsWith("audit.logger."))
					.forEach(key -> properties_talendJobLog.setProperty(key.substring("audit.logger.".length()),
							System.getProperty(key)));

			org.apache.logging.log4j.core.config.Configurator
					.setLevel(properties_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);

			auditLogger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory
					.createJobAuditLogger(properties_talendJobLog);
		}

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		org.slf4j.MDC.put("_pid", pid);

		if (rootPid == null) {
			rootPid = pid;
		}

		org.slf4j.MDC.put("_rootPid", rootPid);

		if (fatherPid == null) {
			fatherPid = pid;
		} else {
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
					contextStr = (String) jobProperties.get("context");
				}

				// extract params from caller
				String taskExecutionIdForSOAP = null;
				String jobExecutionIdForSOAP = null;
				boolean skipCBPAuth = false;
				boolean isSoapService = false;
				for (String arg : args) {
					// for SOAP service, the taskExecutionId and jobExecutionId are passed in as
					// context params from Talend Runtime
					if (arg.startsWith("--context_param")
							&& (arg.contains("taskExecutionId") || arg.contains("jobExecutionId"))) {
						String keyValue = arg.replace("--context_param", "");
						String[] parts = keyValue.split("=");
						String[] cleanParts = java.util.Arrays.stream(parts).filter(s -> !s.isEmpty())
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
			if (isStandaloneMS) {
				context.putAll(this.getDefaultProperties());
			}
			// call job/subjob with an existing context, like: --context=production. if
			// without this parameter, there will use the default context instead.
			java.io.InputStream inContext = mp01_input_port_olist.class.getClassLoader().getResourceAsStream(
					"capacitacion_talend/mp01_input_port_olist_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = mp01_input_port_olist.class.getClassLoader()
						.getResourceAsStream("config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null) {
				try {
					// defaultProps is in order to keep the original context value
					if (context != null && context.isEmpty()) {
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
					if (isStandaloneMS) {
						// override context key-value pairs if provided using --context=contextName
						defaultProps.load(inContext);
						context.putAll(defaultProps);
					}
				} finally {
					inContext.close();
				}
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param is not empty.
				System.err.println("Could not find the context " + contextStr);
			}
			// override key-value pairs if provided via --config.location=file1.file2 OR
			// --config.additional-location=file1,file2
			if (isStandaloneMS) {
				context.putAll(this.getAdditionalProperties());
			}

			// override key-value pairs if provide via command line like
			// --key1=value1,--key2=value2
			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}

			class ContextProcessing {
				private void processContext_0() {
					context.setContextType("DB_HOST", "id_String");
					if (context.getStringValue("DB_HOST") == null) {
						context.DB_HOST = null;
					} else {
						context.DB_HOST = (String) context.getProperty("DB_HOST");
					}
					context.setContextType("DB_NAME", "id_String");
					if (context.getStringValue("DB_NAME") == null) {
						context.DB_NAME = null;
					} else {
						context.DB_NAME = (String) context.getProperty("DB_NAME");
					}
					context.setContextType("DB_PASSWORD", "id_String");
					if (context.getStringValue("DB_PASSWORD") == null) {
						context.DB_PASSWORD = null;
					} else {
						context.DB_PASSWORD = (String) context.getProperty("DB_PASSWORD");
					}
					context.setContextType("DB_PORT", "id_String");
					if (context.getStringValue("DB_PORT") == null) {
						context.DB_PORT = null;
					} else {
						context.DB_PORT = (String) context.getProperty("DB_PORT");
					}
					context.setContextType("DB_USER", "id_String");
					if (context.getStringValue("DB_USER") == null) {
						context.DB_USER = null;
					} else {
						context.DB_USER = (String) context.getProperty("DB_USER");
					}
					context.setContextType("ENV_NAME", "id_String");
					if (context.getStringValue("ENV_NAME") == null) {
						context.ENV_NAME = null;
					} else {
						context.ENV_NAME = (String) context.getProperty("ENV_NAME");
					}
					context.setContextType("FILE_OLIST_CUSTOMERS", "id_String");
					if (context.getStringValue("FILE_OLIST_CUSTOMERS") == null) {
						context.FILE_OLIST_CUSTOMERS = null;
					} else {
						context.FILE_OLIST_CUSTOMERS = (String) context.getProperty("FILE_OLIST_CUSTOMERS");
					}
					context.setContextType("FILE_OLIST_ORDER_ITEMS", "id_String");
					if (context.getStringValue("FILE_OLIST_ORDER_ITEMS") == null) {
						context.FILE_OLIST_ORDER_ITEMS = null;
					} else {
						context.FILE_OLIST_ORDER_ITEMS = (String) context.getProperty("FILE_OLIST_ORDER_ITEMS");
					}
					context.setContextType("FILE_OLIST_ORDER_PAYMENTS", "id_String");
					if (context.getStringValue("FILE_OLIST_ORDER_PAYMENTS") == null) {
						context.FILE_OLIST_ORDER_PAYMENTS = null;
					} else {
						context.FILE_OLIST_ORDER_PAYMENTS = (String) context.getProperty("FILE_OLIST_ORDER_PAYMENTS");
					}
					context.setContextType("FILE_OLIST_ORDERS", "id_String");
					if (context.getStringValue("FILE_OLIST_ORDERS") == null) {
						context.FILE_OLIST_ORDERS = null;
					} else {
						context.FILE_OLIST_ORDERS = (String) context.getProperty("FILE_OLIST_ORDERS");
					}
					context.setContextType("FILE_OLIST_PRODUCTS", "id_String");
					if (context.getStringValue("FILE_OLIST_PRODUCTS") == null) {
						context.FILE_OLIST_PRODUCTS = null;
					} else {
						context.FILE_OLIST_PRODUCTS = (String) context.getProperty("FILE_OLIST_PRODUCTS");
					}
					context.setContextType("LOAD_SOURCE_TAG", "id_String");
					if (context.getStringValue("LOAD_SOURCE_TAG") == null) {
						context.LOAD_SOURCE_TAG = null;
					} else {
						context.LOAD_SOURCE_TAG = (String) context.getProperty("LOAD_SOURCE_TAG");
					}
					context.setContextType("PIPELINE_NAME", "id_String");
					if (context.getStringValue("PIPELINE_NAME") == null) {
						context.PIPELINE_NAME = null;
					} else {
						context.PIPELINE_NAME = (String) context.getProperty("PIPELINE_NAME");
					}
					context.setContextType("RUN_ID", "id_Long");
					if (context.getStringValue("RUN_ID") == null) {
						context.RUN_ID = null;
					} else {
						try {
							context.RUN_ID = routines.system.ParserUtils.parseTo_Long(context.getProperty("RUN_ID"));
						} catch (NumberFormatException e) {
							log.warn(String.format("Null value will be used for context parameter %s: %s", "RUN_ID",
									e.getMessage()));
							context.RUN_ID = null;
						}
					}
				}

				public void processAllContext() {
					processContext_0();
				}
			}

			new ContextProcessing().processAllContext();
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
			if (parentContextMap.containsKey("DB_HOST")) {
				context.DB_HOST = (String) parentContextMap.get("DB_HOST");
			}
			if (parentContextMap.containsKey("DB_NAME")) {
				context.DB_NAME = (String) parentContextMap.get("DB_NAME");
			}
			if (parentContextMap.containsKey("DB_PASSWORD")) {
				context.DB_PASSWORD = (String) parentContextMap.get("DB_PASSWORD");
			}
			if (parentContextMap.containsKey("DB_PORT")) {
				context.DB_PORT = (String) parentContextMap.get("DB_PORT");
			}
			if (parentContextMap.containsKey("DB_USER")) {
				context.DB_USER = (String) parentContextMap.get("DB_USER");
			}
			if (parentContextMap.containsKey("ENV_NAME")) {
				context.ENV_NAME = (String) parentContextMap.get("ENV_NAME");
			}
			if (parentContextMap.containsKey("FILE_OLIST_CUSTOMERS")) {
				context.FILE_OLIST_CUSTOMERS = (String) parentContextMap.get("FILE_OLIST_CUSTOMERS");
			}
			if (parentContextMap.containsKey("FILE_OLIST_ORDER_ITEMS")) {
				context.FILE_OLIST_ORDER_ITEMS = (String) parentContextMap.get("FILE_OLIST_ORDER_ITEMS");
			}
			if (parentContextMap.containsKey("FILE_OLIST_ORDER_PAYMENTS")) {
				context.FILE_OLIST_ORDER_PAYMENTS = (String) parentContextMap.get("FILE_OLIST_ORDER_PAYMENTS");
			}
			if (parentContextMap.containsKey("FILE_OLIST_ORDERS")) {
				context.FILE_OLIST_ORDERS = (String) parentContextMap.get("FILE_OLIST_ORDERS");
			}
			if (parentContextMap.containsKey("FILE_OLIST_PRODUCTS")) {
				context.FILE_OLIST_PRODUCTS = (String) parentContextMap.get("FILE_OLIST_PRODUCTS");
			}
			if (parentContextMap.containsKey("LOAD_SOURCE_TAG")) {
				context.LOAD_SOURCE_TAG = (String) parentContextMap.get("LOAD_SOURCE_TAG");
			}
			if (parentContextMap.containsKey("PIPELINE_NAME")) {
				context.PIPELINE_NAME = (String) parentContextMap.get("PIPELINE_NAME");
			}
			if (parentContextMap.containsKey("RUN_ID")) {
				context.RUN_ID = (Long) parentContextMap.get("RUN_ID");
			}
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "",
				"", "", "", "", ResumeUtil.convertToJsonText(context, ContextProperties.class, parametersToEncrypt));

		org.slf4j.MDC.put("_context", contextStr);
		log.info("TalendJob: 'mp01_input_port_olist' - Started.");
		java.util.Optional.ofNullable(org.slf4j.MDC.getCopyOfContextMap()).ifPresent(mdcInfo::putAll);

		if (execStat) {
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

		this.globalResumeTicket = true;// to run tPreJob

		try {
			errorCode = null;
			tPrejob_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tPrejob_1) {
			globalMap.put("tPrejob_1_SUBPROCESS_STATE", -1);

			e_tPrejob_1.printStackTrace();

		}

		if (enableLogStash) {
			talendJobLog.addJobStartMessage();
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		this.globalResumeTicket = false;// to run others jobs

		this.globalResumeTicket = true;// to run tPostJob

		try {
			errorCode = null;
			tPostjob_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tPostjob_1) {
			globalMap.put("tPostjob_1_SUBPROCESS_STATE", -1);

			e_tPostjob_1.printStackTrace();

		}

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println(
					(endUsedMemory - startUsedMemory) + " bytes memory increase when running : mp01_input_port_olist");
		}
		if (enableLogStash) {
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

		if (!isChildJob) {
			// TODO TMC need to make sure the key or provide a way to pass it to job,
			// consider java command length too long risk in windows OS
			String taskId = System.getProperty("tmc.task.id");// not the same key with CBP
			String taskVersion = System.getProperty("tmc.task.version");
			String taskExecId = System.getProperty("tmc.task.execution.id");
			String lineageFilePath = System.getProperty("runtime.lineage.appender.file.path");
			try {
				org.talend.studio.ProducerHelper.newPayload(payloadFilePath, lineageFilePath, taskExecId, taskId,
						taskVersion, lineageDirPath == null ? true : false);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		int returnCode = 0;

		if (!inOSGi) {
			if (needSendForCBP && org.talend.metrics.CBPClient.getInstanceForCurrentVM() != null) {
				s("none");
				if (!isChildJob) {
					org.talend.metrics.CBPClient.getInstanceForCurrentVM().setStatus(status);
				}
				org.talend.metrics.CBPClient.getInstanceForCurrentVM().sendData();
			}
		}

		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "",
				"" + returnCode, "", "", "");
		resumeUtil.flush();

		org.slf4j.MDC.remove("_subJobName");
		org.slf4j.MDC.remove("_subJobPid");
		org.slf4j.MDC.remove("_systemPid");
		log.info("TalendJob: 'mp01_input_port_olist' - Finished - status: " + status + " returnCode: " + returnCode);

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

		closeSqlDbConnections();
		// check for orphan threads if still alive after undeploy
		for (java.util.Iterator<Thread> it = threadList.iterator(); it.hasNext();) {
			Thread thread = it.next();
			if (thread != null && thread.isAlive()) {
				boolean suppressLogs = Boolean
						.parseBoolean(System.getProperty("thread.terminate.logs.suppress", "false"));
				if (!suppressLogs) {
					System.err.println(
							"Initiating thread cleanup prior to bundle undeployment. This is a precautionary step to ensure no memory leaks.");
					System.err.println("Forcefully interrupting thread with ID = " + thread.getId()
							+ ". This may result in expected errors due to abrupt termination. Please verify if the thread was performing critical operations.");
				}
				thread.interrupt();
			}
			it.remove();
		}
		// end of destroy()
	}

	private void closeSqlDbConnections() {
		try {
			Object obj_conn;
			obj_conn = globalMap.remove("conn_tDBConnection_1");
			if (null != obj_conn) {
				((java.sql.Connection) obj_conn).close();
			}
		} catch (java.lang.Exception e) {
		}
	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();
		connections.put("conn_tDBConnection_1", globalMap.get("conn_tDBConnection_1"));

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
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1));
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
					if ((index = line.indexOf('=')) > -1) {
						if (line.startsWith("--context_param")) {
							if ("id_Password".equals(context_param.getContextType(line.substring(16, index)))) {
								context_param.put(line.substring(16, index),
										routines.system.PasswordEncryptUtil.decryptPassword(line.substring(index + 1)));
							} else {
								context_param.put(line.substring(16, index), line.substring(index + 1));
							}
						} else {// --context_type
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
		} else if (arg.startsWith("--audit.enabled") && arg.contains("=")) {// for trunjob call
			final int equal = arg.indexOf('=');
			final String key = arg.substring("--".length(), equal);
			System.setProperty(key, arg.substring(equal + 1));
		}
	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" }, { "\\'", "\'" }, { "\\r", "\r" },
			{ "\\f", "\f" }, { "\\b", "\b" }, { "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the
			// result
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
 * 287842 characters generated by Qlik Talend Cloud Enterprise Edition on the 23
 * de octubre de 2026, 17:02:39 ECT
 ************************************************************************************************/