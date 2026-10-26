
package capacitacion_talend.job_07_consultas_mysql_0_1;

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
 * Job: JOB_07_CONSULTAS_MYSQL Purpose: Consultar y transformar clientes desde
 * MySQL<br>
 * Description: Consultar y transformar clientes desde MySQL <br>
 * 
 * @author Chango, Kevin
 * @version 8.0.1.20260619_1127-patch
 * @status
 */
public class JOB_07_CONSULTAS_MYSQL implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "JOB_07_CONSULTAS_MYSQL.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(JOB_07_CONSULTAS_MYSQL.class);

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

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
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
	private final String jobName = "JOB_07_CONSULTAS_MYSQL";
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
			"_QdxDUMwgEfGZzMhpNuWmEw", "0.1");
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
				JOB_07_CONSULTAS_MYSQL.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(JOB_07_CONSULTAS_MYSQL.this, new Object[] { e, currentComponent, globalMap });
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

	public void tDBInput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tMap_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tDBInput_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBInput_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class clientes_normalizadosStruct
			implements routines.system.IPersistableRow<clientes_normalizadosStruct> {
		final static byte[] commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[0];
		static byte[] commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[0];

		public String _customer_id;

		public String get_customer_id() {
			return this._customer_id;
		}

		public Boolean _customer_idIsNullable() {
			return true;
		}

		public Boolean _customer_idIsKey() {
			return false;
		}

		public Integer _customer_idLength() {
			return 34;
		}

		public Integer _customer_idPrecision() {
			return 0;
		}

		public String _customer_idDefault() {

			return null;

		}

		public String _customer_idComment() {

			return "";

		}

		public String _customer_idPattern() {

			return "";

		}

		public String _customer_idOriginalDbColumnName() {

			return "_customer_id";

		}

		public String _customer_city;

		public String get_customer_city() {
			return this._customer_city;
		}

		public Boolean _customer_cityIsNullable() {
			return true;
		}

		public Boolean _customer_cityIsKey() {
			return false;
		}

		public Integer _customer_cityLength() {
			return 100;
		}

		public Integer _customer_cityPrecision() {
			return 0;
		}

		public String _customer_cityDefault() {

			return null;

		}

		public String _customer_cityComment() {

			return "";

		}

		public String _customer_cityPattern() {

			return "";

		}

		public String _customer_cityOriginalDbColumnName() {

			return "_customer_city";

		}

		public String _customer_state;

		public String get_customer_state() {
			return this._customer_state;
		}

		public Boolean _customer_stateIsNullable() {
			return true;
		}

		public Boolean _customer_stateIsKey() {
			return false;
		}

		public Integer _customer_stateLength() {
			return 2;
		}

		public Integer _customer_statePrecision() {
			return 0;
		}

		public String _customer_stateDefault() {

			return null;

		}

		public String _customer_stateComment() {

			return "";

		}

		public String _customer_statePattern() {

			return "";

		}

		public String _customer_stateOriginalDbColumnName() {

			return "_customer_state";

		}

		public String ciudad_mayuscula;

		public String getCiudad_mayuscula() {
			return this.ciudad_mayuscula;
		}

		public Boolean ciudad_mayusculaIsNullable() {
			return true;
		}

		public Boolean ciudad_mayusculaIsKey() {
			return false;
		}

		public Integer ciudad_mayusculaLength() {
			return null;
		}

		public Integer ciudad_mayusculaPrecision() {
			return null;
		}

		public String ciudad_mayusculaDefault() {

			return null;

		}

		public String ciudad_mayusculaComment() {

			return "";

		}

		public String ciudad_mayusculaPattern() {

			return "";

		}

		public String ciudad_mayusculaOriginalDbColumnName() {

			return "ciudad_mayuscula";

		}

		public String clasificacion_estado;

		public String getClasificacion_estado() {
			return this.clasificacion_estado;
		}

		public Boolean clasificacion_estadoIsNullable() {
			return true;
		}

		public Boolean clasificacion_estadoIsKey() {
			return false;
		}

		public Integer clasificacion_estadoLength() {
			return null;
		}

		public Integer clasificacion_estadoPrecision() {
			return null;
		}

		public String clasificacion_estadoDefault() {

			return null;

		}

		public String clasificacion_estadoComment() {

			return "";

		}

		public String clasificacion_estadoPattern() {

			return "";

		}

		public String clasificacion_estadoOriginalDbColumnName() {

			return "clasificacion_estado";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length) {
					if (length < 1024 && commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length == 0) {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[1024];
					} else {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length);
				strReturn = new String(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length,
						utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length) {
					if (length < 1024 && commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length == 0) {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[1024];
					} else {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length);
				strReturn = new String(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length,
						utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL) {

				try {

					int length = 0;

					this._customer_id = readString(dis);

					this._customer_city = readString(dis);

					this._customer_state = readString(dis);

					this.ciudad_mayuscula = readString(dis);

					this.clasificacion_estado = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL) {

				try {

					int length = 0;

					this._customer_id = readString(dis);

					this._customer_city = readString(dis);

					this._customer_state = readString(dis);

					this.ciudad_mayuscula = readString(dis);

					this.clasificacion_estado = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this._customer_id, dos);

				// String

				writeString(this._customer_city, dos);

				// String

				writeString(this._customer_state, dos);

				// String

				writeString(this.ciudad_mayuscula, dos);

				// String

				writeString(this.clasificacion_estado, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this._customer_id, dos);

				// String

				writeString(this._customer_city, dos);

				// String

				writeString(this._customer_state, dos);

				// String

				writeString(this.ciudad_mayuscula, dos);

				// String

				writeString(this.clasificacion_estado, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("_customer_id=" + _customer_id);
			sb.append(",_customer_city=" + _customer_city);
			sb.append(",_customer_state=" + _customer_state);
			sb.append(",ciudad_mayuscula=" + ciudad_mayuscula);
			sb.append(",clasificacion_estado=" + clasificacion_estado);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (_customer_id == null) {
				sb.append("<null>");
			} else {
				sb.append(_customer_id);
			}

			sb.append("|");

			if (_customer_city == null) {
				sb.append("<null>");
			} else {
				sb.append(_customer_city);
			}

			sb.append("|");

			if (_customer_state == null) {
				sb.append("<null>");
			} else {
				sb.append(_customer_state);
			}

			sb.append("|");

			if (ciudad_mayuscula == null) {
				sb.append("<null>");
			} else {
				sb.append(ciudad_mayuscula);
			}

			sb.append("|");

			if (clasificacion_estado == null) {
				sb.append("<null>");
			} else {
				sb.append(clasificacion_estado);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(clientes_normalizadosStruct other) {

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
		final static byte[] commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[0];
		static byte[] commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[0];

		public String _customer_id;

		public String get_customer_id() {
			return this._customer_id;
		}

		public Boolean _customer_idIsNullable() {
			return true;
		}

		public Boolean _customer_idIsKey() {
			return false;
		}

		public Integer _customer_idLength() {
			return 34;
		}

		public Integer _customer_idPrecision() {
			return 0;
		}

		public String _customer_idDefault() {

			return null;

		}

		public String _customer_idComment() {

			return "";

		}

		public String _customer_idPattern() {

			return "";

		}

		public String _customer_idOriginalDbColumnName() {

			return "_customer_id";

		}

		public String _customer_city;

		public String get_customer_city() {
			return this._customer_city;
		}

		public Boolean _customer_cityIsNullable() {
			return true;
		}

		public Boolean _customer_cityIsKey() {
			return false;
		}

		public Integer _customer_cityLength() {
			return 100;
		}

		public Integer _customer_cityPrecision() {
			return 0;
		}

		public String _customer_cityDefault() {

			return null;

		}

		public String _customer_cityComment() {

			return "";

		}

		public String _customer_cityPattern() {

			return "";

		}

		public String _customer_cityOriginalDbColumnName() {

			return "_customer_city";

		}

		public String _customer_state;

		public String get_customer_state() {
			return this._customer_state;
		}

		public Boolean _customer_stateIsNullable() {
			return true;
		}

		public Boolean _customer_stateIsKey() {
			return false;
		}

		public Integer _customer_stateLength() {
			return 2;
		}

		public Integer _customer_statePrecision() {
			return 0;
		}

		public String _customer_stateDefault() {

			return null;

		}

		public String _customer_stateComment() {

			return "";

		}

		public String _customer_statePattern() {

			return "";

		}

		public String _customer_stateOriginalDbColumnName() {

			return "_customer_state";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length) {
					if (length < 1024 && commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length == 0) {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[1024];
					} else {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length);
				strReturn = new String(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length,
						utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length) {
					if (length < 1024 && commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL.length == 0) {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[1024];
					} else {
						commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length);
				strReturn = new String(commonByteArray_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL, 0, length,
						utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL) {

				try {

					int length = 0;

					this._customer_id = readString(dis);

					this._customer_city = readString(dis);

					this._customer_state = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_CAPACITACION_TALEND_JOB_07_CONSULTAS_MYSQL) {

				try {

					int length = 0;

					this._customer_id = readString(dis);

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

				writeString(this._customer_id, dos);

				// String

				writeString(this._customer_city, dos);

				// String

				writeString(this._customer_state, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this._customer_id, dos);

				// String

				writeString(this._customer_city, dos);

				// String

				writeString(this._customer_state, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("_customer_id=" + _customer_id);
			sb.append(",_customer_city=" + _customer_city);
			sb.append(",_customer_state=" + _customer_state);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (_customer_id == null) {
				sb.append("<null>");
			} else {
				sb.append(_customer_id);
			}

			sb.append("|");

			if (_customer_city == null) {
				sb.append("<null>");
			} else {
				sb.append(_customer_city);
			}

			sb.append("|");

			if (_customer_state == null) {
				sb.append("<null>");
			} else {
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

	public void tDBInput_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tDBInput_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tDBInput_1", "WxR8JG_");

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
				clientes_normalizadosStruct clientes_normalizados = new clientes_normalizadosStruct();

				/**
				 * [tLogRow_1 begin ] start
				 */

				sh("tLogRow_1");

				s(currentComponent = "tLogRow_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0,
						"clientes_normalizados");

				int tos_count_tLogRow_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tLogRow_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tLogRow_1 = new StringBuilder();
							log4jParamters_tLogRow_1.append("Parameters:");
							log4jParamters_tLogRow_1.append("BASIC_MODE" + " = " + "false");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("TABLE_PRINT" + " = " + "true");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("VERTICAL" + " = " + "false");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("PRINT_CONTENT_WITH_LOG4J" + " = " + "true");
							log4jParamters_tLogRow_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tLogRow_1 - " + (log4jParamters_tLogRow_1));
						}
					}
					new BytesLimit65535_tLogRow_1().limitLog4jByte();
				}
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tLogRow_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter(
								final clientes_normalizadosStruct clientes_normalizados) throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("BASIC_MODE", String.valueOf("false"));
								component_parameters.put("TABLE_PRINT", String.valueOf("true"));
								component_parameters.put("VERTICAL", String.valueOf("false"));
								component_parameters.put("PRINT_CONTENT_WITH_LOG4J", String.valueOf("true"));

							} catch (java.lang.Exception e_tLogRow_1) {
								// do nothing
							}

							return component_parameters;
						}
					}

					talendJobLog.addComponentParameterMessage("tLogRow_1", "tLogRow",
							new ParameterUtil_tLogRow_1().getParameter(clientes_normalizados));
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}
				// QTUP-3575

				if (enableLogStash) {
					talendJobLog.addCM("tLogRow_1", "tLogRow_1", "tLogRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				///////////////////////

				class Util_tLogRow_1 {

					String[] des_top = { ".", ".", "-", "+" };

					String[] des_head = { "|=", "=|", "-", "+" };

					String[] des_bottom = { "'", "'", "-", "+" };

					String name = "";

					java.util.List<String[]> list = new java.util.ArrayList<String[]>();

					int[] colLengths = new int[5];

					public void addRow(String[] row) {

						for (int i = 0; i < 5; i++) {
							if (row[i] != null) {
								colLengths[i] = Math.max(colLengths[i], row[i].length());
							}
						}
						list.add(row);
					}

					public void setTableName(String name) {

						this.name = name;
					}

					public StringBuilder format() {

						StringBuilder sb = new StringBuilder();

						sb.append(print(des_top));

						int totals = 0;
						for (int i = 0; i < colLengths.length; i++) {
							totals = totals + colLengths[i];
						}

						// name
						sb.append("|");
						int k = 0;
						for (k = 0; k < (totals + 4 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 4 - name.length() - k; i++) {
							sb.append(' ');
						}
						sb.append("|\n");

						// head and rows
						sb.append(print(des_head));
						for (int i = 0; i < list.size(); i++) {

							String[] row = list.get(i);

							java.util.Formatter formatter = new java.util.Formatter(new StringBuilder());

							StringBuilder sbformat = new StringBuilder();
							sbformat.append("|%1$-");
							sbformat.append(colLengths[0]);
							sbformat.append("s");

							sbformat.append("|%2$-");
							sbformat.append(colLengths[1]);
							sbformat.append("s");

							sbformat.append("|%3$-");
							sbformat.append(colLengths[2]);
							sbformat.append("s");

							sbformat.append("|%4$-");
							sbformat.append(colLengths[3]);
							sbformat.append("s");

							sbformat.append("|%5$-");
							sbformat.append(colLengths[4]);
							sbformat.append("s");

							sbformat.append("|\n");

							formatter.format(sbformat.toString(), (Object[]) row);

							sb.append(formatter.toString());
							if (i == 0)
								sb.append(print(des_head)); // print the head
						}

						// end
						sb.append(print(des_bottom));
						return sb;
					}

					private StringBuilder print(String[] fillChars) {
						StringBuilder sb = new StringBuilder();
						// first column
						sb.append(fillChars[0]);
						for (int i = 0; i < colLengths[0] - fillChars[0].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						for (int i = 0; i < colLengths[1] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[2] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[3] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						// last column
						for (int i = 0; i < colLengths[4] - fillChars[1].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[1]);
						sb.append("\n");
						return sb;
					}

					public boolean isTableEmpty() {
						if (list.size() > 1)
							return false;
						return true;
					}
				}
				Util_tLogRow_1 util_tLogRow_1 = new Util_tLogRow_1();
				util_tLogRow_1.setTableName("tLogRow_1");
				util_tLogRow_1.addRow(new String[] { "_customer_id", "_customer_city", "_customer_state",
						"ciudad_mayuscula", "clasificacion_estado", });
				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
///////////////////////    			

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tMap_1 begin ] start
				 */

				sh("tMap_1");

				s(currentComponent = "tMap_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tMap_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tMap_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tMap_1 {
						public void limitLog4jByte() throws Exception {
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
							if (log.isDebugEnabled())
								log.debug("tMap_1 - " + (log4jParamters_tMap_1));
						}
					}
					new BytesLimit65535_tMap_1().limitLog4jByte();
				}
				boolean init_tMap_1_0 = true;
				// QTUP-3575
				if (enableLineage) {
					class ParameterUtil_tMap_1 {

						private void a(java.util.List<java.util.Map<String, String>> schema, String... values) {
							java.util.Map<String, String> field = new java.util.HashMap<>();
							field.put("name", values[0]);
							field.put("talend_type", values[1]);
							schema.add(field);
						}

						public java.util.Map<String, String> getParameter(final row1Struct row1) throws Exception {
							java.util.Map<String, String> component_parameters = new java.util.HashMap<>();

							try {

								component_parameters.put("LINK_STYLE", String.valueOf("AUTO"));
								component_parameters.put("ROWS_BUFFER_SIZE", String.valueOf("2000000"));
								component_parameters.put("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL",
										String.valueOf("true"));
								component_parameters.put("mapperData", String.valueOf(
										"{\"inputTables\":[{\"name\":\"row1\",\"metadataTableEntries\":[{\"name\":\"_customer_id\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_city\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_state\",\"expression\":\"\",\"type\":\"id_String\",\"nullable\":false}]}],\"outputTables\":[{\"name\":\"clientes_normalizados\",\"metadataTableEntries\":[{\"name\":\"_customer_id\",\"expression\":\"row1._customer_id\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_city\",\"expression\":\"row1._customer_city\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"_customer_state\",\"expression\":\"row1._customer_state\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"ciudad_mayuscula\",\"expression\":\"row1._customer_city == null ? null : row1._customer_city.trim().toUpperCase()\",\"type\":\"id_String\",\"nullable\":false},{\"name\":\"clasificacion_estado\",\"expression\":\"row1._customer_state == null ? \\\"DESCONOCIDO\\\" :\\r\\n\\t\\t\\t\\t\\t(java.util.Arrays.asList(\\\"SP\\\",\\\"RJ\\\",\\\"MG\\\",\\\"ES\\\").contains(row1._customer_state.trim().toUpperCase()) ? \\\"SUDESTE\\\" :\\r\\n\\t\\t\\t\\t\\tjava.util.Arrays.asList(\\\"PR\\\",\\\"SC\\\",\\\"RS\\\").contains(row1._customer_state.trim().toUpperCase()) ? \\\"SUR\\\" :\\r\\n\\t\\t\\t\\t\\tjava.util.Arrays.asList(\\\"GO\\\",\\\"MT\\\",\\\"MS\\\",\\\"DF\\\").contains(row1._customer_state.trim().toUpperCase()) ? \\\"CENTRO-OESTE\\\" :\\r\\n\\t\\t\\t\\t\\tjava.util.Arrays.asList(\\\"BA\\\",\\\"PE\\\",\\\"CE\\\",\\\"RN\\\",\\\"PB\\\",\\\"AL\\\",\\\"SE\\\",\\\"PI\\\",\\\"MA\\\").contains(row1._customer_state.trim().toUpperCase()) ? \\\"NORDESTE\\\" :\\r\\n\\t\\t\\t\\t\\tjava.util.Arrays.asList(\\\"AM\\\",\\\"PA\\\",\\\"AC\\\",\\\"RO\\\",\\\"RR\\\",\\\"AP\\\",\\\"TO\\\").contains(row1._customer_state.trim().toUpperCase()) ? \\\"NORTE\\\" : \\\"DESCONOCIDO\\\")\",\"type\":\"id_String\",\"nullable\":false}]}],\"varsTables\":[{\"name\":\"Var\",\"metadataTableEntries\":[]}]}"));

							} catch (java.lang.Exception e_tMap_1) {
								// do nothing
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

				if (enableLogStash) {
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
				class Var__tMap_1__Struct {
				}
				Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
// ###############################

// ###############################
// # Outputs initialization
				int count_clientes_normalizados_tMap_1 = 0;

				clientes_normalizadosStruct clientes_normalizados_tmp = new clientes_normalizadosStruct();
// ###############################

				/**
				 * [tMap_1 begin ] stop
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
							log4jParamters_tDBInput_1.append("DB_VERSION" + " = " + "MYSQL_8");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("USE_EXISTING_CONNECTION" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("HOST" + " = " + "\"209.50.245.36\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PORT" + " = " + "\"3306\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("DBNAME" + " = " + "\"DSADEV\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("USER" + " = " + "\"dev_genius\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PASS" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:jxmYMYSPcKjU1FZ1IFkca8k2AfHLfgUTd0EQszsLptZmtrq81ibWYQRogUQ=")
									.substring(0, 4) + "...");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TABLE" + " = " + "\"practica_olist_customers\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERYSTORE" + " = " + "\"\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("QUERY" + " = "
									+ "\"SELECT _customer_id, _customer_city, _customer_state \" +  \"FROM DSADEV.practica_olist_customers LIMIT 10\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("PROPERTIES" + " = "
									+ "\"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1\"");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("ENABLE_STREAM" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_ALL_COLUMN" + " = " + "false");
							log4jParamters_tDBInput_1.append(" | ");
							log4jParamters_tDBInput_1.append("TRIM_COLUMN" + " = " + "[{TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("_customer_id") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("_customer_city") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("_customer_state") + "}]");
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

								component_parameters.put("DB_VERSION", String.valueOf("MYSQL_8"));
								component_parameters.put("USE_EXISTING_CONNECTION", String.valueOf("false"));
								component_parameters.put("HOST", String.valueOf("209.50.245.36"));
								component_parameters.put("PORT", String.valueOf("3306"));
								component_parameters.put("DBNAME", String.valueOf("DSADEV"));
								component_parameters.put("USER", String.valueOf("dev_genius"));
								component_parameters.put("TABLE", String.valueOf("practica_olist_customers"));
								component_parameters.put("QUERYSTORE", String.valueOf(""));
								component_parameters.put("QUERY",
										String.valueOf("SELECT _customer_id, _customer_city, _customer_state "
												+ "FROM DSADEV.practica_olist_customers LIMIT 10"));
								component_parameters.put("SPECIFY_DATASOURCE_ALIAS", String.valueOf("false"));
								component_parameters.put("PROPERTIES", String.valueOf(
										"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1"));
								component_parameters.put("ENABLE_STREAM", String.valueOf("false"));
								component_parameters.put("TRIM_ALL_COLUMN", String.valueOf("false"));
								component_parameters.put("TRIM_COLUMN",
										String.valueOf(new StringBuilder().append("[{TRIM=").append("false")
												.append(", SCHEMA_COLUMN=").append("_customer_id").append("}, {TRIM=")
												.append("false").append(", SCHEMA_COLUMN=").append("_customer_city")
												.append("}, {TRIM=").append("false").append(", SCHEMA_COLUMN=")
												.append("_customer_state").append("}]").toString()));
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
				String driverClass_tDBInput_1 = "com.mysql.cj.jdbc.Driver";
				java.lang.Class jdbcclazz_tDBInput_1 = java.lang.Class.forName(driverClass_tDBInput_1);
				String dbUser_tDBInput_1 = "dev_genius";

				final String decryptedPassword_tDBInput_1 = java.util.Optional
						.ofNullable(routines.system.PasswordEncryptUtil.decryptPassword(
								"enc:routine.encryption.key.v1:AVtF2qR7cHDV2qiYSYeRnbM3xGsXqpyU2++2tlY//pJwOErl5EysgMHmOcU="))
						.orElse("");

				String dbPwd_tDBInput_1 = decryptedPassword_tDBInput_1;

				String properties_tDBInput_1 = "noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1";
				if (properties_tDBInput_1 == null || properties_tDBInput_1.trim().length() == 0) {
					properties_tDBInput_1 = "";
				}
				String url_tDBInput_1 = "jdbc:mysql://" + "209.50.245.36" + ":" + "3306" + "/" + "DSADEV" + "?"
						+ properties_tDBInput_1;

				log.debug("tDBInput_1 - Driver ClassName: " + driverClass_tDBInput_1 + ".");

				log.debug("tDBInput_1 - Connection attempt to '"
						+ url_tDBInput_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********")
						+ "' with the username '" + dbUser_tDBInput_1 + "'.");

				conn_tDBInput_1 = java.sql.DriverManager.getConnection(url_tDBInput_1, dbUser_tDBInput_1,
						dbPwd_tDBInput_1);
				log.debug("tDBInput_1 - Connection to '"
						+ url_tDBInput_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********") + "' has succeeded.");

				java.sql.Statement stmt_tDBInput_1 = conn_tDBInput_1.createStatement();

				String dbquery_tDBInput_1 = "SELECT _customer_id, _customer_city, _customer_state "
						+ "FROM DSADEV.practica_olist_customers LIMIT 10";

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
							row1._customer_id = null;
						} else {

							row1._customer_id = routines.system.JDBCUtil.getString(rs_tDBInput_1, 1, false);
						}
						if (colQtyInRs_tDBInput_1 < 2) {
							row1._customer_city = null;
						} else {

							row1._customer_city = routines.system.JDBCUtil.getString(rs_tDBInput_1, 2, false);
						}
						if (colQtyInRs_tDBInput_1 < 3) {
							row1._customer_state = null;
						} else {

							row1._customer_state = routines.system.JDBCUtil.getString(rs_tDBInput_1, 3, false);
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
									a(s, "_customer_id", "_customer_id", "false", "id_String", "VARCHAR", "true", "",
											"34", "0");
									a(s, "_customer_city", "_customer_city", "false", "id_String", "VARCHAR", "true",
											"", "100", "0");
									a(s, "_customer_state", "_customer_state", "false", "id_String", "VARCHAR", "true",
											"", "2", "0");
									return s;
								}

							}

							if (row1 != null) {
								talendJobLog.addConnectionSchemaMessage("tDBInput_1", "tMysqlInput", "tMap_1", "tMap",
										"row1" + iterateId, new SchemaUtil_row1().getSchema(row1));
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
						 * [tMap_1 main ] start
						 */

						s(currentComponent = "tMap_1");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row1", "tDBInput_1", "tDBInput_1", "tMysqlInput", "tMap_1", "tMap_1", "tMap"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
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

							clientes_normalizados = null;

// # Output table : 'clientes_normalizados'
							count_clientes_normalizados_tMap_1++;

							clientes_normalizados_tmp._customer_id = row1._customer_id;
							clientes_normalizados_tmp._customer_city = row1._customer_city;
							clientes_normalizados_tmp._customer_state = row1._customer_state;
							clientes_normalizados_tmp.ciudad_mayuscula = row1._customer_city == null ? null
									: row1._customer_city.trim().toUpperCase();
							clientes_normalizados_tmp.clasificacion_estado = row1._customer_state == null
									? "DESCONOCIDO"
									: (java.util.Arrays.asList("SP", "RJ", "MG", "ES")
											.contains(row1._customer_state.trim().toUpperCase())
													? "SUDESTE"
													: java.util.Arrays.asList("PR", "SC", "RS")
															.contains(row1._customer_state.trim().toUpperCase())
																	? "SUR"
																	: java.util.Arrays.asList("GO", "MT", "MS", "DF")
																			.contains(row1._customer_state.trim()
																					.toUpperCase())
																							? "CENTRO-OESTE"
																							: java.util.Arrays.asList(
																									"BA", "PE", "CE",
																									"RN", "PB", "AL",
																									"SE", "PI", "MA")
																									.contains(
																											row1._customer_state
																													.trim()
																													.toUpperCase())
																															? "NORDESTE"
																															: java.util.Arrays
																																	.asList("AM",
																																			"PA",
																																			"AC",
																																			"RO",
																																			"RR",
																																			"AP",
																																			"TO")
																																	.contains(
																																			row1._customer_state
																																					.trim()
																																					.toUpperCase())
																																							? "NORTE"
																																							: "DESCONOCIDO");
							clientes_normalizados = clientes_normalizados_tmp;
							log.debug("tMap_1 - Outputting the record " + count_clientes_normalizados_tMap_1
									+ " of the output table 'clientes_normalizados'.");

// ###############################

						} // end of Var scope

						rejectedInnerJoin_tMap_1 = false;

						// QTUP-3575
						if (enableLineage && init_tMap_1_0) {
							class SchemaUtil_clientes_normalizados {

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

								public java.util.List<java.util.Map<String, String>> getSchema(
										final clientes_normalizadosStruct clientes_normalizados) {
									java.util.List<java.util.Map<String, String>> s = new java.util.ArrayList<>();
									if (clientes_normalizados == null) {
										return s;
									}
									a(s, "_customer_id", "_customer_id", "false", "id_String", "VARCHAR", "true", "",
											"34", "0");
									a(s, "_customer_city", "_customer_city", "false", "id_String", "VARCHAR", "true",
											"", "100", "0");
									a(s, "_customer_state", "_customer_state", "false", "id_String", "VARCHAR", "true",
											"", "2", "0");
									a(s, "ciudad_mayuscula", "ciudad_mayuscula", "false", "id_String", "", "true", "",
											"null", "null");
									a(s, "clasificacion_estado", "clasificacion_estado", "false", "id_String", "",
											"true", "", "null", "null");
									return s;
								}

							}

							if (clientes_normalizados != null) {
								talendJobLog.addConnectionSchemaMessage("tMap_1", "tMap", "tLogRow_1", "tLogRow",
										"clientes_normalizados" + iterateId,
										new SchemaUtil_clientes_normalizados().getSchema(clientes_normalizados));
								talendJobLogProcess(globalMap);
								init_tMap_1_0 = false;
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

						s(currentComponent = "tMap_1");

						/**
						 * [tMap_1 process_data_begin ] stop
						 */

// Start of branch "clientes_normalizados"
						if (clientes_normalizados != null) {

							/**
							 * [tLogRow_1 main ] start
							 */

							s(currentComponent = "tLogRow_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "clientes_normalizados", "tMap_1", "tMap_1", "tMap", "tLogRow_1", "tLogRow_1",
									"tLogRow"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("clientes_normalizados - "
										+ (clientes_normalizados == null ? "" : clientes_normalizados.toLogString()));
							}

///////////////////////		

							String[] row_tLogRow_1 = new String[5];

							if (clientes_normalizados._customer_id != null) { //
								row_tLogRow_1[0] = String.valueOf(clientes_normalizados._customer_id);

							} //

							if (clientes_normalizados._customer_city != null) { //
								row_tLogRow_1[1] = String.valueOf(clientes_normalizados._customer_city);

							} //

							if (clientes_normalizados._customer_state != null) { //
								row_tLogRow_1[2] = String.valueOf(clientes_normalizados._customer_state);

							} //

							if (clientes_normalizados.ciudad_mayuscula != null) { //
								row_tLogRow_1[3] = String.valueOf(clientes_normalizados.ciudad_mayuscula);

							} //

							if (clientes_normalizados.clasificacion_estado != null) { //
								row_tLogRow_1[4] = String.valueOf(clientes_normalizados.clasificacion_estado);

							} //

							util_tLogRow_1.addRow(row_tLogRow_1);
							nb_line_tLogRow_1++;
							log.info("tLogRow_1 - Content of row " + nb_line_tLogRow_1 + ": "
									+ TalendString.unionString("|", row_tLogRow_1));
//////

//////                    

///////////////////////    			

							tos_count_tLogRow_1++;

							/**
							 * [tLogRow_1 main ] stop
							 */

							/**
							 * [tLogRow_1 process_data_begin ] start
							 */

							s(currentComponent = "tLogRow_1");

							/**
							 * [tLogRow_1 process_data_begin ] stop
							 */

							/**
							 * [tLogRow_1 process_data_end ] start
							 */

							s(currentComponent = "tLogRow_1");

							/**
							 * [tLogRow_1 process_data_end ] stop
							 */

						} // End of branch "clientes_normalizados"

						/**
						 * [tMap_1 process_data_end ] start
						 */

						s(currentComponent = "tMap_1");

						/**
						 * [tMap_1 process_data_end ] stop
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
					if (conn_tDBInput_1 != null && !conn_tDBInput_1.isClosed()) {

						log.debug("tDBInput_1 - Closing the connection to the database.");

						conn_tDBInput_1.close();

						if ("com.mysql.cj.jdbc.Driver".equals((String) globalMap.get("driverClass_"))
								&& routines.system.BundleUtils.inOSGi()) {
							Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread")
									.getMethod("checkedShutdown").invoke(null, (Object[]) null);
						}

						log.debug("tDBInput_1 - Connection to the database closed.");

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
				 * [tMap_1 end ] start
				 */

				s(currentComponent = "tMap_1");

// ###############################
// # Lookup hashes releasing
// ###############################      
				log.debug("tMap_1 - Written records count in the table 'clientes_normalizados': "
						+ count_clientes_normalizados_tMap_1 + ".");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tDBInput_1", "tDBInput_1", "tMysqlInput", "tMap_1", "tMap_1", "tMap", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tMap_1 - " + ("Done."));

				ok_Hash.put("tMap_1", true);
				end_Hash.put("tMap_1", System.currentTimeMillis());

				/**
				 * [tMap_1 end ] stop
				 */

				/**
				 * [tLogRow_1 end ] start
				 */

				s(currentComponent = "tLogRow_1");

//////

				java.io.PrintStream consoleOut_tLogRow_1 = null;
				if (globalMap.get("tLogRow_CONSOLE") != null) {
					consoleOut_tLogRow_1 = (java.io.PrintStream) globalMap.get("tLogRow_CONSOLE");
				} else {
					consoleOut_tLogRow_1 = new java.io.PrintStream(new java.io.BufferedOutputStream(System.out));
					globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_1);
				}

				consoleOut_tLogRow_1.println(util_tLogRow_1.format().toString());
				consoleOut_tLogRow_1.flush();
//////
				globalMap.put("tLogRow_1_NB_LINE", nb_line_tLogRow_1);
				if (log.isInfoEnabled())
					log.info("tLogRow_1 - " + ("Printed row count: ") + (nb_line_tLogRow_1) + ("."));

///////////////////////    			

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "clientes_normalizados",
						2, 0, "tMap_1", "tMap_1", "tMap", "tLogRow_1", "tLogRow_1", "tLogRow", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Done."));

				ok_Hash.put("tLogRow_1", true);
				end_Hash.put("tLogRow_1", System.currentTimeMillis());

				/**
				 * [tLogRow_1 end ] stop
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
				 * [tMap_1 finally ] start
				 */

				s(currentComponent = "tMap_1");

				/**
				 * [tMap_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				s(currentComponent = "tLogRow_1");

				/**
				 * [tLogRow_1 finally ] stop
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
		final JOB_07_CONSULTAS_MYSQL JOB_07_CONSULTAS_MYSQLClass = new JOB_07_CONSULTAS_MYSQL();
		int exitCode = JOB_07_CONSULTAS_MYSQLClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'JOB_07_CONSULTAS_MYSQL' - Done.");
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

		log.info("TalendJob: 'JOB_07_CONSULTAS_MYSQL' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_QdxDUMwgEfGZzMhpNuWmEw");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2026-10-20T01:37:30.969733800Z");

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
			java.io.InputStream inContext = JOB_07_CONSULTAS_MYSQL.class.getClassLoader().getResourceAsStream(
					"capacitacion_talend/job_07_consultas_mysql_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = JOB_07_CONSULTAS_MYSQL.class.getClassLoader()
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
		log.info("TalendJob: 'JOB_07_CONSULTAS_MYSQL' - Started.");
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

		if (enableLogStash) {
			talendJobLog.addJobStartMessage();
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tDBInput_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tDBInput_1) {
			globalMap.put("tDBInput_1_SUBPROCESS_STATE", -1);

			e_tDBInput_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println(
					(endUsedMemory - startUsedMemory) + " bytes memory increase when running : JOB_07_CONSULTAS_MYSQL");
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
		log.info("TalendJob: 'JOB_07_CONSULTAS_MYSQL' - Finished - status: " + status + " returnCode: " + returnCode);

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
 * 118565 characters generated by Qlik Talend Cloud Enterprise Edition on the 19
 * de octubre de 2026, 20:37:30 ECT
 ************************************************************************************************/