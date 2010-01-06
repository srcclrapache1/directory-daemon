/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.daemon;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;


/**
 * ApacheDS allows the same installation to support multiple instances of the
 * server.  This requires ApacheDS software installations to be kept separate
 * from ApacheDS instance folders.  To follow OS conventions, sometimes the
 * instances root is not kept under the installation home directory.  Even 
 * worse sometimes the log, run and var directories of instances are 
 * distributed across the file system without subordinating to the same 
 * instance home directory.
 * 
 * This bean tracks the installation it's associated with using a contained
 * InstallLayout bean, and leverages that along with additional information 
 * from the how it is created and the system properties to determine what the
 * instance's layout looks like.
 *
 *       ${ads-instance-name}
 *       |-- conf
 *       |-- ldif
 *       |-- log
 *       |-- partitions
 *       |   |-- example
 *       |   |-- schema
 *       |   `-- system
 *       `-- run
 *       
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class InstanceLayout
{
    /** the name of the server process id file */
    private static final String SERVER_PIDFILE = "server.pid";

    /** system property for var directory override */
    public static final String VAR_DIR_KEY = "apacheds.var.dir";
    
    /** system property for log directory override */
    public static final String LOG_DIR_KEY = "apacheds.log.dir";
    
    /** system property for run directory override */
    public static final String RUN_DIR_KEY = "apacheds.run.dir";
 
    /** the name of the instances directory */
    public static final String INSTANCES_DIRNAME = "instances";
    
    /** the name of the configuration directory */
    public static final String CONF_DIRNAME = "conf";
    
    /** the name of the var directory */
    public static final String VAR_DIRNAME = "var";
    
    /** the name of the var directory */
    public static final String PARTITION_DIRNAME = "partitions";
    
    /** the name of the log directory */
    public static final String LOG_DIRNAME = "log";
    
    /** the name of the run directory */
    public static final String RUN_DIRNAME = "run";
    
    /** the name of the instance */
    private final String name;
    
    /** the instance's home directory */
    private final File instanceDir;
    
    /** the installation layout */
    private final InstallLayout installLayout;

    
    /** a file system directory filter */
    private FileFilter dirFilter = new FileFilter()
    {
        public boolean accept( File pathname )
        {
            return pathname.isDirectory();
        }
    };


    /**
     * Creates a new instance of InstanceLayout which uses a specific instance 
     * location which may or may not be under the installation home directory's 
     * instances directory.  The name is explicitly set regardless of what the
     * instanceDir name is.
     * 
     * @param name the name of the instance if different from last path name
     * @param installLayout the installation layout
     * @param instanceDir the location of the instance home directory
     */
    public InstanceLayout( String name, InstallLayout installLayout, File instanceDir )
    {
        this.name = name;
        this.instanceDir = instanceDir;
        this.installLayout = installLayout;
    }


    /**
     * Creates a new instance of InstanceLayout which uses a specific 
     * instance location which may or may not be under the installation home 
     * directory's instances directory.  The name of the instance is taken
     * from the last path component of the instanceDir.
     *
     * @param installLayout the installation layout
     * @param instanceDir the location of the instance home directory
     */
    public InstanceLayout( InstallLayout installLayout, File instanceDir )
    {
        this( instanceDir.getName(), installLayout, instanceDir );
    }


    /**
     * Creates a new instance of InstanceLayout which presumes the instances
     * directory resides under the installation home directory and the 
     * instance directory inside this instances directory is simply the name 
     * of the instance.
     * 
     * @param name the name of the instance
     * @param installLayout the installation layout
     */
    public InstanceLayout( String name, InstallLayout installLayout )
    {
        this.name = name;
        this.installLayout = installLayout;
        this.instanceDir = new File( installLayout.getInstallHomeDir(), INSTANCES_DIRNAME );
    }

    
    /**
     * Get's the name of the instance.
     *
     * @return the name of the instance
     */
    public String getName()
    {
        return name;
    }
    

    /**
     * Get's the partitions directory for this InstanceLayout.
     *
     * @return the partitions directory for this IntanceLayout
     */
    public File getPartitionsDir()
    {
        return new File( getVarDir(), PARTITION_DIRNAME );
    }
    
    
    /**
     * Get's the var directory for this InstanceLayout.
     *
     * @return the var directory for this IntanceLayout
     */
    public File getVarDir()
    {
        if ( System.getProperty( VAR_DIR_KEY ) == null )
        {
            return new File( instanceDir, VAR_DIRNAME );
        }
        
        return new File( System.getProperty( VAR_DIR_KEY ) );
    }


    /**
     * Get's the log directory for this InstanceLayout.
     *
     * @return the log directory for this IntanceLayout
     */
    public File getLogDir()
    {
        if ( System.getProperty( LOG_DIR_KEY ) == null )
        {
            return new File( instanceDir, LOG_DIRNAME );
        }
        
        return new File( System.getProperty( LOG_DIR_KEY ) );
    }


    /**
     * Get's the conf directory for this InstanceLayout.
     *
     * @return the conf directory for this IntanceLayout
     */
    public File getConfDir()
    {
        return new File( instanceDir, CONF_DIRNAME );
    }


    /**
     * Get's the run directory for this InstanceLayout.
     *
     * @return the run directory for this IntanceLayout
     */
    public File getRunDir()
    {
        if ( System.getProperty( RUN_DIR_KEY ) == null )
        {
            return new File( instanceDir, RUN_DIRNAME );
        }
        
        return new File( System.getProperty( RUN_DIR_KEY ) );
    }
    
    
    /**
     * Gets the installation layout bean for this InstanceLayout.
     *
     * @return the {@link InstallLayout} for this InstanceLayout
     */
    public InstallLayout getInstallLayout()
    {
        return this.installLayout;
    }


    /**
     * returns a list of partition directories
     * @return list of partition directories
     */
    public List<File> getAllPartitions()
    {
        List<File> partitions = new ArrayList<File>();

        File[] dirs = getPartitionsDir().listFiles( dirFilter );

        for ( File f : dirs )
        {
            partitions.add( f );
        }

        return partitions;
    }


    public File getPidFile()
    {
        return new File( getRunDir(), SERVER_PIDFILE );
    }
    
    
    public void verifyLayout()
    {
        installLayout.verifyInstallation();
    }


    /**
     * returns a list of jdbm partition directories
     * @return list of partition directories
     */
    public List<File> getJdbmPartitions()
    {
        List<File> partitions = new ArrayList<File>();

        File[] dirs = getPartitionsDir().listFiles( dirFilter );

        for ( File f : dirs )
        {
            File masterFile = new File( f, "master.db" );
            if ( masterFile.isFile() && masterFile.exists() )
            {
                partitions.add( f );
            }
        }

        return partitions;
    }
}
