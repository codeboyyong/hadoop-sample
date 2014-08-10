package com.codeboy.hadoop.tools;



import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.security.UserGroupInformation;

public class MRJarRunner {

	  /** Pattern that matches any string */
	  public static final Pattern MATCH_ANY = Pattern.compile(".*");

	  /**
	   * Priority of the RunJar shutdown hook.
	   */
	  public static final int SHUTDOWN_HOOK_PRIORITY = 10;

//	  /**
//	   * Unpack a jar file into a directory.
//	   *
//	   * This version unpacks all files inside the jar regardless of filename.
//	   */
//	  public static void unJar(File jarFile, File toDir) throws IOException {
//	    unJar(jarFile, toDir, MATCH_ANY);
//	  }

//	  /**
//	   * Unpack matching files from a jar. Entries inside the jar that do
//	   * not match the given pattern will be skipped.
//	   *
//	   * @param jarFile the .jar file to unpack
//	   * @param toDir the destination directory into which to unpack the jar
//	   * @param unpackRegex the pattern to match jar entries against
//	   */
//	  public static void unJar(File jarFile, File toDir, Pattern unpackRegex)
//	    throws IOException {
//	    JarFile jar = new JarFile(jarFile);
//	    try {
//	      Enumeration<JarEntry> entries = jar.entries();
//	      while (entries.hasMoreElements()) {
//	        JarEntry entry = (JarEntry)entries.nextElement();
//	        if (!entry.isDirectory() &&
//	            unpackRegex.matcher(entry.getName()).matches()) {
//	          InputStream in = jar.getInputStream(entry);
//	          try {
//	            File file = new File(toDir, entry.getName());
//	            ensureDirectory(file.getParentFile());
//	            OutputStream out = new FileOutputStream(file);
//	            try {
//	              IOUtils.copyBytes(in, out, 8192);
//	            } finally {
//	              out.close();
//	            }
//	          } finally {
//	            in.close();
//	          }
//	        }
//	      }
//	    } finally {
//	      jar.close();
//	    }
//	  }

	  /**
	   * Ensure the existence of a given directory.
	   *
	   * @throws IOException if it cannot be created and does not already exist
	   */
	  private static void ensureDirectory(File dir) throws IOException {
	    if (!dir.mkdirs() && !dir.isDirectory()) {
	      throw new IOException("Mkdirs failed to create " +
	                            dir.toString());
	    }
	  }

	  /** Run a Hadoop job jar.  If the main class is not in the jar's manifest,
	   * then it must be provided on the command line. */
	  public static void main(String[] args) throws Throwable {
	    String usage = "RunJar jarFile [mainClass] args...";

	    if (args.length < 1) {
	      System.err.println(usage);
	      System.exit(-1);
	    }

	    int firstArg = 0;
	    String fileName = args[firstArg++];
	    File file = new File(fileName);
	    if (!file.exists() || !file.isFile()) {
	      System.err.println("Not a valid JAR: " + file.getCanonicalPath());
	      System.exit(-1);
	    }
	    String mainClassName = null;

	    JarFile jarFile;
	    try {
	      jarFile = new JarFile(fileName);
	    } catch(IOException io) {
	      throw new IOException("Error opening job jar: " + fileName)
	        .initCause(io);
	    }

	    Manifest manifest = jarFile.getManifest();
	    if (manifest != null) {
	      mainClassName = manifest.getMainAttributes().getValue("Main-Class");
	    }
	    jarFile.close();

	    if (mainClassName == null) {
	      if (args.length < 2) {
	        System.err.println(usage);
	        System.exit(-1);
	      }
	      mainClassName = args[firstArg++];
	    }
	    mainClassName = mainClassName.replaceAll("/", ".");

	    File tmpDir = new File(new Configuration().get("hadoop.tmp.dir"));
	    ensureDirectory(tmpDir);

	    final File workDir;
	    try { 
	      workDir = File.createTempFile("hadoop-unjar", "", tmpDir);
	    } catch (IOException ioe) {
	      // If user has insufficient perms to write to tmpDir, default  
	      // "Permission denied" message doesn't specify a filename. 
	      System.err.println("Error creating temp dir in hadoop.tmp.dir "
	                         + tmpDir + " due to " + ioe.getMessage());
	      System.exit(-1);
	      return;
	    }

	    if (!workDir.delete()) {
	      System.err.println("Delete failed for " + workDir);
	      System.exit(-1);
	    }
	    ensureDirectory(workDir);

	    Runtime.getRuntime().addShutdownHook( 
	      new Thread() {
	        @Override
	        public void run() {
	          try {
				FileUtil.fullyDelete(workDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        }
	      });


//	    unJar(file, workDir);
//
//	    ArrayList<URL> classPath = new ArrayList<URL>();
//	    classPath.add(new File(workDir+"/").toURI().toURL());
//	    classPath.add(file.toURI().toURL());
//	    classPath.add(new File(workDir, "classes/").toURI().toURL());
//	    File[] libs = new File(workDir, "lib").listFiles();
//	    if (libs != null) {
//	      for (int i = 0; i < libs.length; i++) {
//	        classPath.add(libs[i].toURI().toURL());
//	      }
//	    }
// 	    ClassLoader loader =
//	      new URLClassLoader(classPath.toArray(new URL[0]));
//
//	    Thread.currentThread().setContextClassLoader(loader);
	    System.out.println("mainClassName=" +mainClassName);
	    Class<?> mainClass = Class.forName(mainClassName, true, MRJarRunner.class.getClassLoader());
	    final  Method main = mainClass.getMethod("main", new Class[] {
	      Array.newInstance(String.class, 0).getClass()
	    });
	    final String[] newArgs = Arrays.asList(args)
	      .subList(firstArg, args.length).toArray(new String[0]);
	     
	      
	    	  PrivilegedExceptionAction action = new PrivilegedExceptionAction() {
	                public Object run() throws Exception {
	                		return main.invoke(null, new Object[] { newArgs });
	                }
	            };
	            
	    		UserGroupInformation ugi = null;
 	    			Method[] methods = UserGroupInformation.class.getMethods();
	    			for (Method method : methods) {
	    				if (method.getName().equals("createRemoteUser")
	    						&& method.getParameterTypes()[0].equals(String.class)) {
	    					ugi = (UserGroupInformation) method.invoke(
	    							UserGroupInformation.class, "xx");
	    					break;
	    				}
	    			}
 	    		
 	               performPrivilegedExceptionAction(
	                    ugi, action);
	   
	   
	  }
	  
	  public static Object performPrivilegedExceptionAction(UserGroupInformation ugi,
				PrivilegedExceptionAction  action) throws  Exception {
			Method[] methods = UserGroupInformation.class.getMethods();

	        for (Method method : methods) {
	            if (method.getName().equals("doAs")
	                    && method.getParameterTypes()[0]
	                    .equals(PrivilegedExceptionAction.class)) {
	                try {
	                    return method.invoke(ugi, action);
	                } catch (Exception e) {//nullpoint, empty...
 	                    if (e instanceof InvocationTargetException) {
	                        Throwable targetException = ((InvocationTargetException) e).getTargetException();
	                        if (targetException instanceof UndeclaredThrowableException) {
	                            Throwable ue = ((UndeclaredThrowableException) targetException).getUndeclaredThrowable();
	                        
	                              if (ue instanceof PrivilegedActionException) {
	                                throw ((PrivilegedActionException) ue).getException();
	                            }  else if (ue instanceof Exception) {
	                                throw ((Exception) ue) ;//this for the change in CD5, see 69201736
	                            }
	                            else {
	                                throw e;
	                            }
	                        } else {
	                            if (((InvocationTargetException) e).getTargetException() != null
	                                    && ((InvocationTargetException) e).getTargetException() instanceof Exception) {
	                                throw (Exception) ((InvocationTargetException) e).getTargetException();
	                            } else {
	                                throw e;
	                            }

	                        }
	                    } else {
	                        throw e;
	                    }
	                }

	            }
	        }
			    return null;
		}
	  
}
